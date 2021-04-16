package com.example.Dao;

import com.example.Entities.Order;
import java.util.List;

public class StockExchangeDao {
    private String tradeListPath;

    public StockExchangeDao(String tradeListPath) {
        this.tradeListPath = tradeListPath;
    }

    public List<Order> getTradeOrders()
    {
        return StockExchangeDaoManager.getTradeOrders(this.tradeListPath);
    }
}
