package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.SerializedName;

public class UserAuth {
    @SerializedName("Password")
    public String Password;

    @SerializedName("UserName")
    public String UserName;

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }
}
