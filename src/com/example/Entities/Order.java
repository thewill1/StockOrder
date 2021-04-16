package com.example.Entities;

public class Order {
    private String orderId;

    private int time;

    private String stockId;

    private TradeAction tradeAction;

    private float price;

    private int quantity;

    public Order(String orderId, int time, String stockId, TradeAction tradeAction, float price, int quantity) {
        this.orderId = orderId;
        this.time = time;
        this.stockId = stockId;
        this.tradeAction = tradeAction;
        this.price = price;
        this.quantity = quantity;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    public TradeAction getTradeAction() {
        return tradeAction;
    }

    public void setTradeAction(TradeAction tradeAction) {
        this.tradeAction = tradeAction;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
