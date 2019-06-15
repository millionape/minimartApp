package com.notbytes.barcodereader;

public class ProductGetter {
    String cost,date,price,product_NAME,product_NUMBER;

    public ProductGetter(String cost, String date, String price, String product_NAME, String product_NUMBER) {
        this.cost = cost;
        this.date = date;
        this.price = price;
        this.product_NAME = product_NAME;
        this.product_NUMBER = product_NUMBER;
    }

    public ProductGetter() {
    }

    public String getCost() {
        return cost;
    }

    public String getDate() {
        return date;
    }

    public String getPrice() {
        return price;
    }

    public String getProduct_NAME() {
        return product_NAME;
    }

    public String getProduct_NUMBER() {
        return product_NUMBER;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setProduct_NAME(String product_NAME) {
        this.product_NAME = product_NAME;
    }

    public void setProduct_NUMBER(String product_NUMBER) {
        this.product_NUMBER = product_NUMBER;
    }
}
