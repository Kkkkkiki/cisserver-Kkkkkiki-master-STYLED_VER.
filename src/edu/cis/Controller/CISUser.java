package edu.cis.Controller;

import java.util.*;

public class CISUser {

    //properties
    private String userID;
    private String name;
    private String yearLevel;
    private ArrayList<Order> orders = new ArrayList<>();
    private double money;


    //constructor
    public CISUser(String userID, String name, String yearLevel) {
        this.userID = userID;
        this.name = name;
        this.yearLevel = yearLevel;
        this.money = 50.0;
    }

    //methods
    //getter and setter
    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYearLevel() {
        return yearLevel;
    }

    public void setYearLevel(String yearLevel) {
        this.yearLevel = yearLevel;
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<Order> orders) {
        this.orders = orders;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }


    @Override
    public String toString() {
        return "CISUser{" +
                "userID='" + userID + '\'' +
                ", name='" + name + '\'' +
                ", yearLevel='" + yearLevel + '\'' +
                ", orders=" + orders +
                ", money=" + money +
                '}';
    }

    // give me an order id and this method will tell you whether this order exists or not
    public Order getOrderById(String orderID) {
        Order currentOrder = null;
        System.out.println( "going to find an order in method... ");

        for (Order order : orders) {
            System.out.println( "looking at: " + order.getOrderID());
            System.out.println( "Incoming: " + orderID);
            if (order.getOrderID().equals(orderID)) {
                System.out.println( "ID found in method: " + order.getOrderID());
                currentOrder = order;
            }
        }
        return currentOrder;
    }

    public void addOrder(Order incomingOrder){
        this.orders.add(incomingOrder);
    }

    public void removeOrderById(String orderId) {
        ArrayList<Order> keepOrders = new ArrayList<>();
        for (Order order : orders) {
            if (!order.getOrderID().equals(orderId)) {
                keepOrders.add(order);
            }
        }
        orders = keepOrders;
    }
}
