package com.wensoft.ojeku.pojo;

/**
 * Created by farhan on 3/10/17.
 */

public class OrderProcess {

    String idList;
    String idOrder;
    String orderType;
    String alamat_tujuan;
    String alamat_penjemputan;
    String invoice_number;
    String start_latitude;
    String start_longitude;
    String end_latitude;
    String end_longitude;
    String total_price;
    String jarak;
    String food_price;

    public OrderProcess() {
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

    public String getAlamat_tujuan() {
        return alamat_tujuan;
    }

    public void setAlamat_tujuan(String alamat_tujuan) {
        this.alamat_tujuan = alamat_tujuan;
    }

    public String getAlamat_penjemputan() {
        return alamat_penjemputan;
    }

    public void setAlamat_penjemputan(String alamat_penjemputan) {
        this.alamat_penjemputan = alamat_penjemputan;
    }

    public String getInvoice_number() {
        return invoice_number;
    }

    public void setInvoice_number(String invoice_number) {
        this.invoice_number = invoice_number;
    }

    public String getStart_latitude() {
        return start_latitude;
    }

    public void setStart_latitude(String start_latitude) {
        this.start_latitude = start_latitude;
    }

    public String getStart_longitude() {
        return start_longitude;
    }

    public void setStart_longitude(String start_longitude) {
        this.start_longitude = start_longitude;
    }

    public String getEnd_latitude() {
        return end_latitude;
    }

    public void setEnd_latitude(String end_latitude) {
        this.end_latitude = end_latitude;
    }

    public String getEnd_longitude() {
        return end_longitude;
    }

    public void setEnd_longitude(String end_longitude) {
        this.end_longitude = end_longitude;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getJarak() {
        return jarak;
    }

    public void setJarak(String jarak) {
        this.jarak = jarak;
    }

    public String getFood_price() {
        return food_price;
    }

    public void setFood_price(String food_price) {
        this.food_price = food_price;
    }

    public OrderProcess(String idList, String idOrder, String orderType, String alamat_penjemputan, String alamat_tujuan,
                        String invoice_number, String start_latitude, String start_longitude, String end_latitude,
                        String end_longitude, String total_price, String jarak, String food_price) {
        this.idList = idList;
        this.idOrder = idOrder;
        this.orderType = orderType;
        this.alamat_penjemputan = alamat_penjemputan;
        this.alamat_tujuan = alamat_tujuan;
        this.invoice_number = invoice_number;
        this.start_latitude = start_latitude;
        this.start_longitude = start_longitude;
        this.end_latitude = end_latitude;
        this.end_longitude = end_longitude;
        this.total_price = total_price;
        this.jarak = jarak;
        this.food_price = food_price;
    }


}
