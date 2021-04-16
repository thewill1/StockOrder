package com.example.Entities;

public class TradeTransaction {
    private String buyOrderId;

    private float sellPrice;

    private int quantity;

    private String sellOrderId;

    public String getBuyOrderId() {
        return buyOrderId;
    }

    public void setBuyOrderId(String buyOrderId) {
        this.buyOrderId = buyOrderId;
    }

    public float getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(float sellPrice) {
        this.sellPrice = sellPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSellOrderId() {
        return sellOrderId;
    }

    public void setSellOrderId(String sellOrderId) {
        this.sellOrderId = sellOrderId;
    }

    public TradeTransaction(String buyOrderId, float sellPrice, int quantity, String sellOrderId) {
        this.buyOrderId = buyOrderId;
        this.sellPrice = sellPrice;
        this.quantity = quantity;
        this.sellOrderId = sellOrderId;
    }
}
