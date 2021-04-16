package com.example.Controller;
import com.example.Entities.Order;
import com.example.Service.StockExchangeService;

import java.util.List;

public class StockExchangeController {

    private String tradeListPath;

    private StockExchangeService stockExchangeService;

    public StockExchangeController(String tradeListPath)
    {
        this.tradeListPath = tradeListPath;
        this.stockExchangeService = new StockExchangeService(this.tradeListPath);
    }

    public List<Order> getTradeOrders()
    {
        return stockExchangeService.getTradeOrders();
    }

    public void processTrades()
    {
        stockExchangeService.processTrades();
    }
}
