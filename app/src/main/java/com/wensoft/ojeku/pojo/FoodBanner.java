package com.wensoft.ojeku.pojo;

/**
 * Created by farhan on 12/15/16.
 */

public class FoodBanner {
    public String marker_id;
    public String id;
    public String category_id;
    public String name;
    public String open_time;
    public String close_time;
    public Double latitude;
    public Double longitude;
    public String is_banner;
    public String image;


    public FoodBanner() {
    }

    public String getMarker_id() {
        return marker_id;
    }

    public void setMarker_id(String marker_id) {
        this.marker_id = marker_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOpen_time() {
        return open_time;
    }

    public void setOpen_time(String open_time) {
        this.open_time = open_time;
    }

    public String getClose_time() {
        return close_time;
    }

    public void setClose_time(String close_time) {
        this.close_time = close_time;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getIs_banner() {
        return is_banner;
    }

    public void setIs_banner(String is_banner) {
        this.is_banner = is_banner;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public FoodBanner(String marker_id, String id, String category_id, String name, String open_time, String close_time,
                      Double latitude, Double longitude, String is_banner, String image) {
        this.marker_id = marker_id;
        this.id = id;
        this.category_id = category_id;
        this.name = name;
        this.open_time = open_time;
        this.close_time = close_time;
        this.latitude = latitude;
        this.longitude = longitude;
        this.is_banner = is_banner;
        this.image = image;
    }

}
