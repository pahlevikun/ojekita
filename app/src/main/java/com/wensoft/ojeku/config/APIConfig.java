package com.wensoft.ojeku.config;

/**
 * Created by farhan on 10/28/16.
 */

public class APIConfig {
    public static String API_LOGIN = "http://ojekita.com/api/auth";
    public static String API_REGISTER = "http://ojekita.com/api/register";
    public static String API_FORGOT_CEK = " http://ojekita.com/api/password/email";
    public static String API_FORGOT_CHANGE = "http://ojekita.com/api/password/reset";
    public static String API_GET_DRIVER = "http://ojekita.com/api/driver/getdriver";
    public static String API_FOOD_CATEGORY = "http://ojekita.com/api/order/getcategory";
    public static String API_FOOD_MENU = "http://ojekita.com/api/order/getmenu";
    public static String API_GET_RESTAURANTS = "http://ojekita.com/api/order/getrestaurants";
    public static String API_GET_RESTAURANTS_BY_CATEGORY = "http://ojekita.com/api/order/getrescat";
    public static String API_GET_MENU_BY_RESTAURANTS = "http://ojekita.com/api/order/getmenu";
    public static String API_HISTORY_ONGOING = "http://ojekita.com/api/user/history/ongoing";
    public static String API_HISTORY_FINISH = "http://ojekita.com/api/user/history/finished";
    public static String API_CHECK = "http://ojekita.com/api/user/checkorder";
    public static String API_ORDER_REGULAR = "http://ojekita.com/api/order/postorder";
    public static String API_ORDER_CANCEL = "http://ojekita.com/api/order/cancelorder";
    public static String API_FOOD_DETAIL = "http://ojekita.com/api/order/detailfood";
}