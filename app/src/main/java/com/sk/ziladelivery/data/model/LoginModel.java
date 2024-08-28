package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.SerializedName;

public class LoginModel {

    @SerializedName("Mobile")
    private String Mobile;

    @SerializedName("Password")
    private String Password;

    @SerializedName("DeviceId")
    private String DeviceId;

    @SerializedName("FcmId")
    private String FcmId;

    @SerializedName("CurrentAPKversion")
    private String CurrentAPKversion;

    @SerializedName("PhoneOSversion")
    private String PhoneOSversion;

    @SerializedName("UserDeviceName")
    private String UserDeviceName;

    @SerializedName("IMEI")
    private String IMEI;



    public LoginModel(String mobile, String password, String deviceId, String fcmId, String currentAPKversion, String phoneOSversion, String userDeviceName, String IMEI) {
        this.  Mobile = mobile;
        this.  Password = password;
        this.  DeviceId = deviceId;
        this.  FcmId = fcmId;
        this.  CurrentAPKversion = currentAPKversion;
        this.  PhoneOSversion = phoneOSversion;
        this. UserDeviceName = userDeviceName;
        this.IMEI = IMEI;


    }
}
