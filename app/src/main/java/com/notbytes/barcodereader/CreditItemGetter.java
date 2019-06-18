package com.notbytes.barcodereader;

public class CreditItemGetter {
    String quantity,total,price;

    public CreditItemGetter(String quantity, String total, String price) {
        this.quantity = quantity;
        this.total = total;
        this.price = price;
    }

    public CreditItemGetter() {
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}

