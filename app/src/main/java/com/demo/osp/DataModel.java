package com.demo.osp;

public class DataModel {

    private String dishname;
    private String price;
    private String stock;

    public DataModel(String dishname, String price, String stock) {
        this.dishname = dishname;
        this.price = price;
        this.stock = stock;
    }

    public String getDishname() {
        return dishname;
    }

    public String getPrice() {
        return price;
    }

    public String getStock() {
        return stock;
    }
}
