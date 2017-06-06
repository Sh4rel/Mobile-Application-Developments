package com.example.sharel.stockwatch;

import java.util.Comparator;

/**
 * Created by Sharel on 2/25/2017.
 */

public class Stock  implements Comparable<Stock>{
    private String stockSymbol ;
    private String companyName ;
    private double price;
    private double priceChange;
    private double changePercentage;
    private boolean changeValue;

    public Stock() {  }

    public Stock(String stockSymbol, String companyName, double price, double priceChange, double changePercentage, boolean changeValue) {
        this.stockSymbol = stockSymbol;
        this.companyName = companyName;
        this.price = price;
        this.priceChange = priceChange;
        this.changePercentage = changePercentage;
        this.changeValue = changeValue;
    }

    public Stock(String stockSymbol, String companyName, double price, double priceChange, double changePercentage) {
        this.stockSymbol = stockSymbol;
        this.companyName = companyName;
        this.price = price;
        this.priceChange = priceChange;
        this.changePercentage = changePercentage;
    }

    public Stock(String stockSymbol, String companyName) {
        this.stockSymbol = stockSymbol;
        this.companyName = companyName;
    }

    public Stock(String stockSymbol, double price, double priceChange, double changePercentage, boolean changeValue) {
        this.stockSymbol = stockSymbol;
        this.price = price;
        this.priceChange = priceChange;
        this.changePercentage = changePercentage;
        this.changeValue = changeValue;
    }

    public Stock(String stockSymbol, double price, double priceChange, double changePercentage) {
        this.stockSymbol = stockSymbol;
        this.price = price;
        this.priceChange = priceChange;
        this.changePercentage = changePercentage;
    }

    public boolean isChangeValue() {
        return changeValue;
    }

    public void setChangeValue(boolean changeValue) {
        this.changeValue = changeValue;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPriceChange() {
        return priceChange;
    }

    public void setPriceChange(double priceChange) {
        this.priceChange = priceChange;
    }

    public double getChangePercentage() {
        return changePercentage;
    }

    public void setChangePercentage(double changePercentage) {
        this.changePercentage = changePercentage;
    }

    @Override
    public int compareTo(Stock o) {
        return stockSymbol.compareTo(o.getStockSymbol());
    }


}