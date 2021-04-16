package com.example.Dao;

import com.example.Entities.Order;
import com.example.Entities.TradeAction;
import com.example.Util.Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StockExchangeDaoManager {

    public static List<Order> getTradeOrders(String tradeListPath)
    {
        if (tradeListPath == null || tradeListPath.isEmpty())
        {
            return new ArrayList<>();
        }

        File file =
                new File(tradeListPath);
        Scanner sc;
        try {
            sc = new Scanner(file);
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        }

        List<Order> orders = new ArrayList<>();

        while (sc.hasNextLine())
        {
            Order order = ProcessTradeLine(sc.nextLine());

            if (order != null) {
                orders.add(order);
            }
        }

        return orders;
    }

    private static Order ProcessTradeLine(String tradeLine)
    {
        if (tradeLine == null || tradeLine.isEmpty())
        {
            return null;
        }

        String[] tradeInfos = tradeLine.split("\\s+");

        if (tradeInfos.length == 6)
        {
            return new Order(tradeInfos[0],
                    Util.getTimeInInt(tradeInfos[1]),
                    tradeInfos[2],
                    tradeInfos[3].equals("buy") ? TradeAction.BUY : TradeAction.SELL,
                    Float.parseFloat(tradeInfos[4]),
                    Integer.parseInt(tradeInfos[5]));
        }

        return null;
    }

}
