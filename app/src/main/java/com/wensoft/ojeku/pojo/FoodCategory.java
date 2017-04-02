package com.wensoft.ojeku.pojo;

/**
 * Created by farhan on 3/9/17.
 */

public class FoodCategory {

    public String idList;
    public String idCat;
    public String name;
    public String banner;
    public String is_featured;

    public FoodCategory() {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getIs_featured() {
        return is_featured;
    }

    public void setIs_featured(String is_featured) {
        this.is_featured = is_featured;
    }

    public FoodCategory(String idList, String idCat, String name, String banner, String is_featured) {
        this.idList = idList;
        this.idCat = idCat;
        this.name = name;
        this.banner = banner;
        this.is_featured = is_featured;
    }


}