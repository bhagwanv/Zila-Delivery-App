package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.SerializedName;

public class AppVersionModel {

    @SerializedName("id")
    public int id;
    @SerializedName("App_version")
    public String App_version;
    @SerializedName("isCompulsory")
    public boolean isCompulsory;
    @SerializedName("createdDate")
    public String createdDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getApp_version() {
        return App_version;
    }

    public void setApp_version(String app_version) {
        App_version = app_version;
    }

    public boolean isCompulsory() {
        return isCompulsory;
    }

    public void setCompulsory(boolean compulsory) {
        isCompulsory = compulsory;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}
