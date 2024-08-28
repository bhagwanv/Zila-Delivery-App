package com.sk.ziladelivery.data.model;

public class User {
    private String mobile;
    private String password;

    public User(){}
    public User(String mobile, String password) {
        this.mobile = mobile;
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getPassword() {
        return password;
    }
}