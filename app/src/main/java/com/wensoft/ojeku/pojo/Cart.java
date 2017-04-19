package com.wensoft.ojeku.pojo;

import java.io.Serializable;

/**
 * Created by farhan on 4/7/17.
 */

public class Cart implements Serializable {

    private String idCart;
    private String idMenu;
    private String menu;
    private String harga;
    private String jumlah;
    private String total_harga;

    public Cart() {}

    public String getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(String idMenu) {
        this.idMenu = idMenu;
    }

    public Cart(String idCart, String idMenu, String menu, String harga, String jumlah, String total_harga) {
        this.idCart = idCart;
        this.idMenu = idMenu;
        this.menu = menu;
        this.harga = harga;
        this.jumlah = jumlah;
        this.total_harga = total_harga;
    }

    public String getIdCart() {
        return idCart;
    }

    public void setIdCart(String idCart) {
        this.idCart = idCart;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }

    public String getTotal_harga() {
        return total_harga;
    }

    public void setTotal_harga(String total_harga) {
        this.total_harga = total_harga;
    }



}
