package com.wensoft.ojeku.pojo;

/**
 * Created by farhan on 12/15/16.
 */

public class Markers {
    public String marker_id;
    public String plat_nomor;
    public String nama_mitra;
    public String avatar;
    public Double Lat;
    public Double Long;


    public Markers() {
    }

    public Markers(String marker_id, String plat_nomor, String nama_mitra, Double Lat, Double Long, String avatar) {
        this.marker_id = marker_id;
        this.plat_nomor = plat_nomor;
        this.nama_mitra = nama_mitra;
        this.Lat = Lat;
        this.Long = Long;
        this.avatar = avatar;
    }


    public void setPlat_nomor(String plat_nomor) {
        this.plat_nomor = plat_nomor;
    }

    public String getPlat_nomor() {
        return plat_nomor;
    }


    public void setNama_mitra(String nama_mitra) {
        this.nama_mitra = nama_mitra;
    }

    public String getNama_mitra() {
        return nama_mitra;
    }

    public Double getLat() {
        return Lat;
    }

    public void setLat(Double lat) {
        Lat = lat;
    }

    public Double getLong() {
        return Long;
    }

    public void setLong(Double l) {
        Long = l;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatar() {
        return avatar;
    }

}
