package com.example.Service;

import com.example.Dao.StockExchangeDao;
import com.example.Entities.Order;
import com.example.Entities.TradeAction;
import com.example.Entities.TradeTransaction;

import java.util.*;
import java.util.stream.Collectors;

public class StockExchangeService {

    private String tradeListPath;

    private StockExchangeDao stockExchangeDao;

    private Map<String, TreeSet<Order>> buyOrders = new HashMap<>();

    private Map<String, TreeSet<Order>> sellOrders = new HashMap<>();

    private List<TradeTransaction> tradeTransactions = new ArrayList<>();

    public StockExchangeService(String tradeListPath) {
        this.tradeListPath = tradeListPath;
        this.stockExchangeDao = new StockExchangeDao(tradeListPath);
    }

    public List<Order> getTradeOrders() {
        return stockExchangeDao.getTradeOrders();
    }

    public void processTrades() {
        List<Order> orders = getTradeOrders();

        updateOrdersMap(orders);

        printTransactions();
    }

    private void updateOrdersMap(List<Order> orders) {
        if (orders == null || orders.size() == 0) {
            return;
        }

        for (Order order : orders) {
            if (order == null) {
                continue;
            }

            updateStockMap(order,
                    order.getTradeAction() == TradeAction.BUY
                            ? buyOrders
                            : sellOrders,
                    TradeAction.ACTION_COMPARATOR_MAP.containsKey(order.getTradeAction())
                            ? TradeAction.ACTION_COMPARATOR_MAP.get(order.getTradeAction())
                            : (o1, o2) -> 0);

            doTransaction(order);
        }
    }

    private void updateStockMap(
            Order order,
            Map<String, TreeSet<Order>> stockOrderSetMap,
            Comparator<Order> orderComparator) {
        if (stockOrderSetMap == null) {
            stockOrderSetMap = new HashMap<>();
        }

        if (stockOrderSetMap.containsKey(order.getStockId())) {
            stockOrderSetMap.get(order.getStockId()).add(order);
        } else {
            TreeSet<Order> orders = new TreeSet<>(orderComparator);
            orders.add(order);
            stockOrderSetMap.put(order.getStockId(), orders);
        }
    }

    private void printTransactions() {
        System.out.println("<buy-order-id> <sell-price> <qty> <sell-order-id>");

        if (tradeTransactions == null || tradeTransactions.size() == 0) {
            System.out.println("Transactions Not Possible");
            return;
        }

        for (TradeTransaction tradeTransaction : tradeTransactions) {
            if (tradeTransaction == null) {
                continue;
            }

            System.out.format("%s %.2f %d %s",
                    tradeTransaction.getBuyOrderId(),
                    tradeTransaction.getSellPrice(),
                    tradeTransaction.getQuantity(),
                    tradeTransaction.getSellOrderId());

            System.out.println();
        }
    }

    private void doTransaction(Order order) {
        if (order == null || buyOrders == null || sellOrders == null) {
            return;
        }

        if (buyOrders.containsKey(order.getStockId())
                && sellOrders.containsKey(order.getStockId())) {

            if (order.getTradeAction() == TradeAction.BUY)
            {
                checkForBuyOrderTransaction(order);
            } else if(order.getTradeAction() == TradeAction.SELL)
            {
                checkForSellOrderTransaction(order);
            }
        }
    }

    private void checkForBuyOrderTransaction(Order order)
    {
        List<Order> sellOrdersPossible = sellOrders.get(order.getStockId())
                .stream()
                .filter(tempOrder -> tempOrder.getPrice() <= order.getPrice())
                .collect(Collectors.toList());

        int possibleQty = Math.min(sellOrdersPossible.size() == 0
                ? 0 : sellOrdersPossible.stream().mapToInt(Order::getQuantity).sum(), order.getQuantity());

        BuyOrderTransaction(possibleQty, order);
    }

    private void checkForSellOrderTransaction(Order order)
    {
        List<Order> buyOrdersPossible = buyOrders.get(order.getStockId())
                .stream()
                .filter(tempOrder -> tempOrder.getPrice() >= order.getPrice())
                .collect(Collectors.toList());

        int possibleQty = Math.min(buyOrdersPossible.size() == 0
                ? 0 : buyOrdersPossible.stream().mapToInt(Order::getQuantity).sum(), order.getQuantity());

        SellOrderTransaction(possibleQty, order);
    }

    private void BuyOrderTransaction(int possibleQty, Order order)
    {
        if (possibleQty > 0)
        {
            if (possibleQty == order.getQuantity())
            {
                buyOrders.get(order.getStockId()).remove(order);
            } else
            {
                for (Order tempOrder : buyOrders.get(order.getStockId())) {
                    if (tempOrder == null)
                    {
                        continue;
                    }

                    tempOrder.setQuantity(tempOrder.getQuantity() - possibleQty);
                }
            }

            updateSellOrdersOnStockBuy(possibleQty, order);
        }
    }

    private void SellOrderTransaction(int possibleQty, Order order)
    {
        if (possibleQty > 0)
        {
            if (possibleQty == order.getQuantity())
            {
                sellOrders.get(order.getStockId()).remove(order);
            } else
            {
                for (Order tempOrder : sellOrders.get(order.getStockId())) {
                    if (tempOrder == null)
                    {
                        continue;
                    }

                    tempOrder.setQuantity(tempOrder.getQuantity() - possibleQty);
                }
            }

            updateBuyOrdersOnStockSell(possibleQty, order);
        }
    }

    private void updateSellOrdersOnStockBuy(int possibleQty, Order order)
    {
        int quantityLeft = possibleQty;
        List<Order> ordersToRemove = new ArrayList<>();

        for (Order sellOrder : sellOrders.get(order.getStockId())) {
            if (sellOrder == null)
            {
                continue;
            }

            if (quantityLeft == 0)
            {
                break;
            }

            quantityLeft = quantityLeft - sellOrder.getQuantity();

            if (quantityLeft < 0)
            {
                sellOrder.setQuantity(sellOrder.getQuantity() - possibleQty);
                tradeTransactions.add(new TradeTransaction(order.getOrderId(), sellOrder.getPrice(), -quantityLeft, sellOrder.getOrderId()));
                break;
            } else
            {
                ordersToRemove.add(sellOrder);
                tradeTransactions.add(new TradeTransaction(order.getOrderId(), sellOrder.getPrice(), sellOrder.getQuantity(), sellOrder.getOrderId()));
            }
        }

        for (Order tempOrder1 : ordersToRemove) {
            sellOrders.get(order.getStockId()).remove(tempOrder1);
        }
    }

    private void updateBuyOrdersOnStockSell(int possibleQty, Order order)
    {
        int quantityLeft = possibleQty;
        List<Order> ordersToRemove = new ArrayList<>();

        for (Order buyOrder : buyOrders.get(order.getStockId())) {
            if (buyOrder == null)
            {
                continue;
            }

            if (quantityLeft == 0)
            {
                break;
            }

            quantityLeft = quantityLeft - buyOrder.getQuantity();

            if (quantityLeft < 0)
            {
                buyOrder.setQuantity(buyOrder.getQuantity() - possibleQty);
                tradeTransactions.add(new TradeTransaction(buyOrder.getOrderId(), order.getPrice(), -quantityLeft, order.getOrderId()));
                break;
            } else
            {
                ordersToRemove.add(buyOrder);
                tradeTransactions.add(new TradeTransaction(buyOrder.getOrderId(), order.getPrice(), buyOrder.getQuantity(), order.getOrderId()));
            }
        }

        for (Order tempOrder1 : ordersToRemove) {
            buyOrders.get(order.getStockId()).remove(tempOrder1);
        }
    }
}
