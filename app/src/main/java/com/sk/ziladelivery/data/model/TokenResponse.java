package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.SerializedName;

public  class TokenResponse {

    @SerializedName(".expires")
    private String expires;

    @SerializedName(".issued")
    private String issued;

    @SerializedName("AppName")
    private String AppName;

    @SerializedName("userName")
    private String userName;

    @SerializedName("as:client_id")
    private String client_id;

    @SerializedName("expires_in")
    private int expires_in;

    @SerializedName("token_type")
    private String token_type;

    @SerializedName("access_token")
    private String access_token;

    public String getExpires() {
        return expires;
    }

    public void setExpires(String expires) {
        this.expires = expires;
    }

    public String getIssued() {
        return issued;
    }

    public void setIssued(String issued) {
        this.issued = issued;
    }

    public String getAppName() {
        return AppName;
    }

    public void setAppName(String appName) {
        AppName = appName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }
}
