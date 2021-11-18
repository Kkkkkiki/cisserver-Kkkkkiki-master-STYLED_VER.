package edu.cis.Controller;

import java.util.*;

public class Menu {
    //properties
    private ArrayList<MenuItem> eatriumItems = new ArrayList<MenuItem>();
    private String adminID;

    //constructor
    public Menu(String adminID) {
        this.adminID = adminID;
    }

    public Menu() {

    }

    //methods
    //getter and setters
    public ArrayList<MenuItem> getEatriumItems() {
        return eatriumItems;
    }

    public void setEatriumItems(MenuItem item) {
         eatriumItems.add(item);
    }

    public String getAdminID() {
        return adminID;
    }

    public void setAdminID(String adminID) {
        this.adminID = adminID;
    }

    //tostring
    @Override
    public String toString() {
        return "Menu{" +
                "eatriumItems=" + eatriumItems +
                ", adminID='" + adminID + '\'' +
                '}';
    }
    public MenuItem getItemById(String menuItemID) {
        MenuItem pickItem = null;
        for (MenuItem item : eatriumItems) {
            if (item.getId().equals(menuItemID)) {
                pickItem = item;
                break;
            }
        }
        return pickItem;
    }

}
