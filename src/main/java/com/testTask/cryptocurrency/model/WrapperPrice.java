package com.testTask.cryptocurrency.model;

public class WrapperPrice {

    public WrapperPrice()   {}

    public WrapperPrice(String currency, double minPrice, double maxPrice)  {
        this.currency = currency;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    String currency;
    double minPrice, maxPrice;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    public double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(double maxPrice) {
        this.maxPrice = maxPrice;
    }
}
