package com.example.onestop_admin;

public class Data_model {

    String title;
    String subtitle;
    String desc;
    String image;
    String dp;
    String id;

    public Data_model(String title, String subtitle, String desc, String dp, String image, String id) {
        this.title = title;
        this.subtitle = subtitle;
        this.desc = desc;
        this.image = image;
        this.dp = dp;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getID() {
        return id;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getDesc() {
        return desc;
    }

    public String getdp() {
        return dp;
    }

    public String getImage() {
        return image;
    }
}