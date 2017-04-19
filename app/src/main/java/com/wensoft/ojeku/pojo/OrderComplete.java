package com.wensoft.ojeku.pojo;

/**
 * Created by farhan on 3/10/17.
 */

public class OrderComplete {

    String idList;
    String idOrder;
    String orderType;
    String invoice_number;
    String alamat_tujuan;
    String alamat_penjemputan;
    String total_price;
    String jarak;
    String nama_driver;
    String avatar_driver;
    String plat_nomor;
    String telepon;
    String start_latitude;
    String start_longitude;
    String end_latitude;
    String end_longitude;
    String food_price;

    public OrderComplete() {
    }

    public OrderComplete(String idList, String idOrder, String orderType, String invoice_number, String alamat_penjemputan,
                         String alamat_tujuan, String total_price, String jarak, String nama_driver, String avatar_driver,
                         String plat_nomor, String telepon, String start_latitude, String start_longitude,
                         String end_latitude, String end_longitude, String food_price) {
        this.idList = idList;
        this.idOrder = idOrder;
        this.orderType = orderType;
        this.invoice_number = invoice_number;
        this.alamat_penjemputan = alamat_penjemputan;
        this.alamat_tujuan =alamat_tujuan;
        this.total_price = total_price;
        this.jarak = jarak;
        this.nama_driver = nama_driver;
        this.avatar_driver = avatar_driver;
        this.plat_nomor = plat_nomor;
        this.telepon = telepon;
        this.start_latitude = start_latitude;
        this.start_longitude = start_longitude;
        this.end_latitude = end_latitude;
        this.end_longitude = end_longitude;
        this.food_price = food_price;
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

    public String getInvoice_number() {
        return invoice_number;
    }

    public void setInvoice_number(String invoice_number) {
        this.invoice_number = invoice_number;
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

    public String getNama_driver() {
        return nama_driver;
    }

    public void setNama_driver(String nama_driver) {
        this.nama_driver = nama_driver;
    }

    public String getAvatar_driver() {
        return avatar_driver;
    }

    public void setAvatar_driver(String avatar_driver) {
        this.avatar_driver = avatar_driver;
    }

    public String getPlat_nomor() {
        return plat_nomor;
    }

    public void setPlat_nomor(String plat_nomor) {
        this.plat_nomor = plat_nomor;
    }

    public String getTelepons() {
        return telepon;
    }

    public void setTelepons(String telepon) {
        this.telepon = telepon;
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

    public String getFood_price() {
        return food_price;
    }

    public void setFood_price(String food_price) {
        this.food_price = food_price;
    }
}
