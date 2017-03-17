package com.wensoft.ojeku.pojo;

/**
 * Created by farhan on 3/9/17.
 */

public class FoodCategoryList {

    String idList;
    String idCat;
    String idRes;
    String open;
    String close;
    String lat;
    String lng;
    String nm;
    boolean select;

    public FoodCategoryList() {
    }

    public FoodCategoryList(String idList, String idCat, String idRes, String nm, String open, String close, String lat, String lng, boolean select) {
        this.idList = idList;
        this.idCat = idCat;
        this.idRes = idRes;
        this.nm = nm;
        this.open = open;
        this.close = close;
        this.lat = lat;
        this.lng = lng;
        this.select = select;
    }

    public String getIdList() {
        return idList;
    }

    public void setIdList(String idList) {
        this.idList = idList;
    }

    public String getIdCat() {
        return idCat;
    }

    public void setIdCat(String idCat) {
        this.idCat = idCat;
    }

    public String getIdRes() {
        return idRes;
    }

    public void setIdRes(String idRes) {
        this.idRes = idRes;
    }

    public String getNm() {
        return nm;
    }

    public void setNm(String nm) {
        this.nm = nm;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getClose() {
        return close;
    }

    public void setClose(String close) {
        this.close = close;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public boolean isSelected() {
        return select;
    }

    public void setSelected(boolean select) {
        this.select = select;
    }

}