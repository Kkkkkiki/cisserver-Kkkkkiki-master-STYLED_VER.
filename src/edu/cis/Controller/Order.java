package edu.cis.Controller;

public class Order {
    //properties
    private String itemID;
    private String type;
    private String orderID;

    public String getUserID() {
        return userID;
    }

    private String userID;


    //constructor
    public Order(String itemID, String type, String orderID, String userID) {
        this.itemID = itemID;
        this.type = type;
        this.orderID = orderID;
        this.userID = userID;
    }

    //Methods!
    //getter and setter
    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    //to string
    @Override
    public String toString() {
        return "Order{" +
                "itemID='" + itemID + '\'' +
                ", type='" + type + '\'' +
                ", orderID='" + orderID + '\'' +
                '}';
    }
}