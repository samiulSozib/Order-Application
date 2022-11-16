package com.freedom.order_application;

public class Order {

    private String ProductId;
    private String ProductName;
    private String Quantity;
    private String Price;
    private String QntType;
    private String Free;

    public Order() {
    }

    public Order(String productId, String productName, String quantity, String price,String qntType,String free) {
        ProductId = productId;
        ProductName = productName;
        Quantity = quantity;
        Price = price;
        QntType=qntType;
        Free=free;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getQntType() {
        return QntType;
    }

    public void setQntType(String qntType) {
        QntType = qntType;
    }

    public String getFree() {
        return Free;
    }

    public void setFree(String free) {
        Free = free;
    }
}
