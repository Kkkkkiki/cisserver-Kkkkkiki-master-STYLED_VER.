/*
 * File: CIServer.java
 * ------------------------------
 * When it is finished, this program will implement a basic
 * ecommerce network management server.  Remember to update this comment!
 */

package edu.cis.Controller;

import acm.program.*;

import java.util.*;

import edu.cis.Model.CISConstants;
import edu.cis.Model.Request;
import edu.cis.Model.SimpleServerListener;
import edu.cis.Utils.SimpleServer;

public class CIServer extends ConsoleProgram
        implements SimpleServerListener {
    /* The internet port to listen to requests on */
    private static final int PORT = 8000;

    /* The server object. All you need to do is start it */
    private SimpleServer server = new SimpleServer(this, PORT);

    /**
     * Starts the server running so that when a program sends
     * a request to this server, the method requestMade is called.
     */
    public void run() {
        println("Starting server on port " + PORT);
        server.start();
    }

    /**
     * When a request is sent to this server, this method is
     * called. It must return a String.
     *
     * @param request a Request object built by SimpleServer from an
     * incoming network request by the client
     */
    // all users
    private ArrayList<CISUser> users = new ArrayList<CISUser>();
    private ArrayList<Order> orders = new ArrayList<Order>();
    private Menu menu = new Menu();
    private String resultInfo;
    public int newAmountAvailable = 0;

    //Start: my weird functions
    //Getting user by entering ID
    private CISUser getUser(String userID) {
        CISUser currentUser;
        currentUser = null;

        for (CISUser user : users) {
            if (user.getUserID().equals(userID)) {
                currentUser = user;
                break;
            }
        }
        return currentUser;
    }

    //getting menuitem
    private String getMenuItem(Request req) {
        String menuItemID = req.getParam(CISConstants.ITEM_ID_PARAM);
        MenuItem item = menu.getItemById(menuItemID);
        return item.toString();
    }

    //return the whole menu
    private String getMenu(Request req) {
        ArrayList<MenuItem> items = menu.getEatriumItems();
        String[] menuStrings = new String[items.size()];
        System.out.println("before loop");
        for (int i = 0; i < items.size(); i++) {
            menuStrings[i] = items.get(i).sealFields();
        }
        System.out.println("after loop");
        String send = String.join(";", menuStrings);
        System.out.println(send);
        return send;

        //the reason why we don't put "return" in the for loop is because for loop is filled with small parts
        //and we integrate them outside together.
    }



    //a function to create user
    private CISUser createUser(String userID, String name, String yearLevel) {
        CISUser currUser = null;

        if (name == null || yearLevel == null || userID == null) {
            resultInfo = CISConstants.PARAM_MISSING_ERR;
        } else {
            currUser = new CISUser(userID, name, yearLevel);
            users.add(currUser);
        }
        return currUser;
    }

    //Will return a serialized version of the order object. Check the tests for string format.
    public String getOrder(Request req) {
        String orderSerialized = "";
        String userID = req.getParam(CISConstants.USER_ID_PARAM);
        String orderID = req.getParam(CISConstants.ORDER_ID_PARAM);

        if (userID == null || orderID == null) {
            return CISConstants.PARAM_MISSING_ERR;
        }

        for (CISUser user : users) {
            if (user.getUserID().equals(userID)) {
                Order findOrder = user.getOrderById(orderID);
                if (findOrder != null) {
                    orderSerialized = findOrder.toString();
                }
            }
        }
        return orderSerialized;
    }

    //Will return a serialized version of all orders in the system for a specific user.
    private String getCart(Request req) {
        ArrayList<Order> cart = new ArrayList<>();
        String userID = req.getParam(CISConstants.USER_ID_PARAM);

        for (CISUser user : users) {
            if (user.getUserID().equals(userID)) {
                return user.getOrders().toString();
            }
        }
        return CISConstants.USER_INVALID_ERR;
    }
//end of functions

    public String requestMade(Request request) {
        // your code here.
        String cmd = request.getCommand();
        println(request.toString());
        System.out.println("REQUEST MADE : " + cmd);

        //These are for testings
        MenuItem hamburger = new MenuItem("Hamburger", "tasty" ,"10", "abcd0","lunch"  );
        MenuItem frenchFries = new MenuItem("french fries", "tasty" ,"9", "efgh1","breakfast");
        menu.setEatriumItems(hamburger);
        menu.setEatriumItems(frenchFries);

        //ping the server
        if (request.getCommand().equals(CISConstants.PING)) {
            final String PING_MSG = "Hello, internet";
            //println is used instead of System.out.println to print to the server GUI
            println("   => " + PING_MSG);
            return PING_MSG;
        }

        String userID = request.getParam(CISConstants.YEAR_LEVEL_PARAM);
        String name = request.getParam(CISConstants.USER_ID_PARAM);
        String yearLevel = request.getParam(CISConstants.YEAR_LEVEL_PARAM);

        //create user if user doesn't exist, 🙆‍
        if (cmd.equals(CISConstants.CREATE_USER)) {
            userID = request.getParam(CISConstants.USER_ID_PARAM);
            yearLevel = request.getParam(CISConstants.YEAR_LEVEL_PARAM);
            name = request.getParam(CISConstants.USER_NAME_PARAM);

            CISUser user = getUser(userID);
            if (user == null) {
                user = createUser(userID, name, yearLevel);
                println(user);
            }
            return CISConstants.SUCCESS;
        }

        //add menu item, 🙆‍
        if (request.getCommand().equals(CISConstants.ADD_MENU_ITEM)) {
            String itemName = request.getParam(CISConstants.ITEM_NAME_PARAM);
            String description = request.getParam(CISConstants.DESC_PARAM);
            String price = request.getParam(CISConstants.PRICE_PARAM);
            String itemType = request.getParam(CISConstants.ITEM_TYPE_PARAM);
            String itemID = request.getParam(CISConstants.ITEM_ID_PARAM);

            if (itemName == null || description == null || price == null || itemType == null || itemID == null) {
                return CISConstants.PARAM_MISSING_ERR;
            } else {
                MenuItem currMenu = new MenuItem(itemName, description, price, itemID, itemType);
                menu.setEatriumItems(currMenu);
                return CISConstants.SUCCESS;
            }
        }

        // place invalid orders 🙆‍
        try {
            if (request.getCommand().equals("placeOrder")) {
                String orderID = request.getParam(CISConstants.ORDER_ID_PARAM);
                userID = request.getParam(CISConstants.USER_ID_PARAM);
                String orderType = request.getParam(CISConstants.ORDER_TYPE_PARAM);
                String menuItemID = request.getParam(CISConstants.ITEM_ID_PARAM);

                if (orderID == null || menuItemID == null || userID == null || orderType == null) {
                    return CISConstants.PARAM_MISSING_ERR;
                }

                //does user exist?
                boolean userExistInUsers = false;
                for (CISUser user : users) {
                    if (user.getUserID().equals(userID)) {
                        userExistInUsers = true;
                    }
                }
                if (userExistInUsers == false) {
                    return CISConstants.USER_INVALID_ERR;
                }

                //Does an order in user exist with this orderID?
                //does the order uses the same id for a different person
                for (CISUser user : users) {
                    if (user.getUserID().equals(userID)) {
                        System.out.println(orderID.toString());
                        Order orderCheck = user.getOrderById(orderID); //getting the order
                        if (orderCheck != null) {
                            System.out.println("Found an orderID! " + orderCheck.getOrderID());
                            return CISConstants.DUP_ORDER_ERR;
                        }
                    }
                }

                //Does the user have enough money?
                for (CISUser user : users) {
                    if (user.getUserID().equals(userID)) {
                        if (user.getMoney() < menu.getItemById(menuItemID).getPrice()) {
                            return CISConstants.USER_BROKE_ERR;
                        }
                    }
                }

                //Does the order contain a menuItem that exists in the Menu?
                for (Order order : orders) {
                    if (order.getItemID().equals(null)) {
                        return CISConstants.EMPTY_MENU_ERR;
                    }
                }

                //Is the item sold out?
                for (MenuItem menuItem : menu.getEatriumItems()) {
                    if (menuItem.getId().equals(menuItemID)) {
                        if (menuItem.getAmountAvailable() == 0) {
                            return CISConstants.SOLD_OUT_ERR;
                        }
                    }
                }

                //order sth!
                if (menu.getItemById(menuItemID) != null) {
                    Order currOrder = new Order(menuItemID, orderType, orderID, userID);
                    newAmountAvailable = menu.getItemById(menuItemID).getAmountAvailable() - 1;
                    menu.getItemById(menuItemID).setAmountAvailable(newAmountAvailable);
                    for (CISUser user : users) {
                        if (user.getUserID().equals(userID)) {
                            double currmoney = user.getMoney() - menu.getItemById(menuItemID).getPrice();
                            user.setMoney(currmoney);
                            user.addOrder(currOrder);
                        }
                    }
                    return CISConstants.SUCCESS;
                }
            }
        } catch (Exception err) {
            err.printStackTrace();
            println("server error caught " + err.toString());
        }

        //delete order, 🙆‍
        if (request.getCommand().equals(CISConstants.DELETE_ORDER)) {
            userID = request.getParam(CISConstants.USER_ID_PARAM);
            String orderID = request.getParam(CISConstants.ORDER_ID_PARAM);

            if (userID == null || orderID == null) {
                return CISConstants.PARAM_MISSING_ERR;
            }

            for (CISUser user : users) {
                if (user.getOrderById(orderID) == null) {
                    return CISConstants.ORDER_INVALID_ERR;
                }
                user.removeOrderById(orderID);
                return CISConstants.SUCCESS;
            }
        }

        //GET ITEM 🙆
        if (request.getCommand().equals(CISConstants.GET_ITEM)) {
            return getMenuItem(request);
        }

        //GET MENU, this is for server
        if (request.getCommand().equals(CISConstants.GET_MENU)) {
            System.out.println("Get Menu REQUEST");
            return getMenu(request);
        }

        //GET USER 🙆
        if (request.getCommand().equals(CISConstants.GET_USER)) {
            userID = request.getParam(CISConstants.USER_ID_PARAM);
            if (userID == null) {
                return CISConstants.PARAM_MISSING_ERR;
            }
            CISUser currentUser = getUser(userID);
            return currentUser.toString();
        }

        //GET ORDER 🙆
        if (request.getCommand().equals(CISConstants.GET_ORDER)) {
            return getOrder(request);
        }

        //GET CART, UNSURE
        if (request.getCommand().equals(CISConstants.GET_CART)) {
            if (userID == null) {
                resultInfo = CISConstants.PARAM_MISSING_ERR;
            } else {
                String cart = getCart(request);
            }
        }
        return "Error: Unknown command " + cmd + ".";
    }

    public static void main(String[] args) {
        CIServer f = new CIServer();
        f.start(args);
    }
}
