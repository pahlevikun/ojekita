package com.wensoft.ojeku.pojo;

/**
 * Created by farhan on 3/9/17.
 */

public class FoodCategory {

    String idList;
    String idCat;
    String nm;
    boolean select;

    public FoodCategory() {
    }

    public FoodCategory(String idList, String idCat, String nm,boolean select) {
        this.idList = idList;
        this.idCat = idCat;
        this.nm = nm;
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

    public String getNm() {
        return nm;
    }

    public void setNm(String nm) {
        this.nm = nm;
    }


    public boolean isSelected() {
        return select;
    }

    public void setSelected(boolean select) {
        this.select = select;
    }

}