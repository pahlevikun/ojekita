package com.wensoft.ojeku.pojo;

/**
 * Created by farhan on 3/9/17.
 */

public class FoodMenu {

    String idList;
    String idMenu;
    String idRes;
    String name;
    String price;
    String sum;

    public FoodMenu() {
    }

    public FoodMenu(String idList, String idMenu, String idRes, String nm, String price, String sum) {
        this.idList = idList;
        this.idMenu = idMenu;
        this.idRes = idRes;
        this.name = nm;
        this.price = price;
        this.sum = sum;
    }

    public String getIdList() {
        return idList;
    }

    public void setIdList(String idList) {
        this.idList = idList;
    }

    public String getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(String idMenu) {
        this.idMenu = idMenu;
    }

    public String getIdRes() {
        return idRes;
    }

    public void setIdRes(String idRes) {
        this.idRes = idRes;
    }

    public String getNm() {
        return name;
    }

    public void setNm(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

}