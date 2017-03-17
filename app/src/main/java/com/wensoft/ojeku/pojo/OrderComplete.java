package com.wensoft.ojeku.pojo;

/**
 * Created by farhan on 3/10/17.
 */

public class OrderComplete {

    String idList;
    String idOrder;
    String orderType;
    String alamat;

    public OrderComplete() {
    }

    public OrderComplete(String idList, String idOrder, String orderType, String alamat) {
        this.idList = idList;
        this.idOrder = idOrder;
        this.orderType = orderType;
        this.alamat = alamat;
    }

    public String getIdList() {
        return idList;
    }

    public void setIdList(String idList) {
        this.idList = idList;
    }

    public String getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(String idOrder) {
        this.idOrder = idOrder;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

}
