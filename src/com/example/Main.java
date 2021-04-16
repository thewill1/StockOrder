package com.example;

import com.example.Controller.StockExchangeController;
import com.example.Entities.Order;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        StockExchangeController stockExchangeController = new StockExchangeController("data/orders1.txt");

        stockExchangeController.processTrades();
    }
}
