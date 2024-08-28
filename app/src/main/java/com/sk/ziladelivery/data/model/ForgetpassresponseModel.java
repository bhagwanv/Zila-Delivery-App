package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public  class ForgetpassresponseModel {


    @Expose
    @SerializedName("Message")
    private String Message;
    @Expose
    @SerializedName("Status")
    private boolean Status;
    @Expose
    @SerializedName("customers")
    private Customers customers;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public boolean getStatus() {
        return Status;
    }

    public void setStatus(boolean Status) {
        this.Status = Status;
    }

    public Customers getCustomers() {
        return customers;
    }

    public void setCustomers(Customers customers) {
        this.customers = customers;
    }

    public static class Customers {
        @Expose
        @SerializedName("inTally")
        private boolean inTally;
        @Expose
        @SerializedName("userid")
        private int userid;
        @Expose
        @SerializedName("CustSupplierid")
        private int CustSupplierid;
        @Expose
        @SerializedName("thisSAppordercount")
        private int thisSAppordercount;
        @Expose
        @SerializedName("thisRAppordercount")
        private int thisRAppordercount;
        @Expose
        @SerializedName("thisordervalue")
        private int thisordervalue;
        @Expose
        @SerializedName("thisordercountpending")
        private int thisordercountpending;
        @Expose
        @SerializedName("thisordercountdelivered")
        private int thisordercountdelivered;
        @Expose
        @SerializedName("thisordercountRedispatch")
        private int thisordercountRedispatch;
        @Expose
        @SerializedName("thisordercountCancelled")
        private int thisordercountCancelled;
        @Expose
        @SerializedName("thisordercount")
        private int thisordercount;
        @Expose
        @SerializedName("ordercount")
        private int ordercount;
        @Expose
        @SerializedName("check")
        private boolean check;
        @Expose
        @SerializedName("IsCityVerified")
        private boolean IsCityVerified;
        @Expose
        @SerializedName("IsSignup")
        private boolean IsSignup;
        @Expose
        @SerializedName("fcmId")
        private String fcmId;
        @Expose
        @SerializedName("BeatNumber")
        private int BeatNumber;
        @Expose
        @SerializedName("DivisionId")
        private int DivisionId;
        @Expose
        @SerializedName("Day")
        private String Day;
        @Expose
        @SerializedName("lg")
        private double lg;
        @Expose
        @SerializedName("lat")
        private double lat;
        @Expose
        @SerializedName("Active")
        private boolean Active;
        @Expose
        @SerializedName("Deleted")
        private boolean Deleted;
        @Expose
        @SerializedName("ClusterName")
        private String ClusterName;
        @Expose
        @SerializedName("ClusterId")
        private int ClusterId;
        @Expose
        @SerializedName("ExecutiveId")
        private int ExecutiveId;
        @Expose
        @SerializedName("MonthlyTurnOver")
        private double MonthlyTurnOver;
        @Expose
        @SerializedName("UpdatedDate")
        private String UpdatedDate;
        @Expose
        @SerializedName("CreatedDate")
        private String CreatedDate;
        @Expose
        @SerializedName("Emailid")
        private String Emailid;
        @Expose
        @SerializedName("BAGPSCoordinates")
        private String BAGPSCoordinates;
        @Expose
        @SerializedName("City")
        private String City;
        @Expose
        @SerializedName("Cityid")
        private int Cityid;
        @Expose
        @SerializedName("LandMark")
        private String LandMark;
        @Expose
        @SerializedName("ShippingAddress")
        private String ShippingAddress;
        @Expose
        @SerializedName("BillingAddress")
        private String BillingAddress;
        @Expose
        @SerializedName("Password")
        private String Password;
        @Expose
        @SerializedName("Name")
        private String Name;
        @Expose
        @SerializedName("WarehouseName")
        private String WarehouseName;
        @Expose
        @SerializedName("Mobile")
        private String Mobile;
        @Expose
        @SerializedName("Warehouseid")
        private int Warehouseid;
        @Expose
        @SerializedName("ShopName")
        private String ShopName;
        @Expose
        @SerializedName("Skcode")
        private String Skcode;
        @Expose
        @SerializedName("CompanyId")
        private int CompanyId;
        @Expose
        @SerializedName("CustomerId")
        private int CustomerId;

        public boolean getInTally() {
            return inTally;
        }

        public void setInTally(boolean inTally) {
            this.inTally = inTally;
        }

        public int getUserid() {
            return userid;
        }

        public void setUserid(int userid) {
            this.userid = userid;
        }

        public int getCustSupplierid() {
            return CustSupplierid;
        }

        public void setCustSupplierid(int CustSupplierid) {
            this.CustSupplierid = CustSupplierid;
        }

        public int getThisSAppordercount() {
            return thisSAppordercount;
        }

        public void setThisSAppordercount(int thisSAppordercount) {
            this.thisSAppordercount = thisSAppordercount;
        }

        public int getThisRAppordercount() {
            return thisRAppordercount;
        }

        public void setThisRAppordercount(int thisRAppordercount) {
            this.thisRAppordercount = thisRAppordercount;
        }

        public int getThisordervalue() {
            return thisordervalue;
        }

        public void setThisordervalue(int thisordervalue) {
            this.thisordervalue = thisordervalue;
        }

        public int getThisordercountpending() {
            return thisordercountpending;
        }

        public void setThisordercountpending(int thisordercountpending) {
            this.thisordercountpending = thisordercountpending;
        }

        public int getThisordercountdelivered() {
            return thisordercountdelivered;
        }

        public void setThisordercountdelivered(int thisordercountdelivered) {
            this.thisordercountdelivered = thisordercountdelivered;
        }

        public int getThisordercountRedispatch() {
            return thisordercountRedispatch;
        }

        public void setThisordercountRedispatch(int thisordercountRedispatch) {
            this.thisordercountRedispatch = thisordercountRedispatch;
        }

        public int getThisordercountCancelled() {
            return thisordercountCancelled;
        }

        public void setThisordercountCancelled(int thisordercountCancelled) {
            this.thisordercountCancelled = thisordercountCancelled;
        }

        public int getThisordercount() {
            return thisordercount;
        }

        public void setThisordercount(int thisordercount) {
            this.thisordercount = thisordercount;
        }

        public int getOrdercount() {
            return ordercount;
        }

        public void setOrdercount(int ordercount) {
            this.ordercount = ordercount;
        }

        public boolean getCheck() {
            return check;
        }

        public void setCheck(boolean check) {
            this.check = check;
        }

        public boolean getIsCityVerified() {
            return IsCityVerified;
        }

        public void setIsCityVerified(boolean IsCityVerified) {
            this.IsCityVerified = IsCityVerified;
        }

        public boolean getIsSignup() {
            return IsSignup;
        }

        public void setIsSignup(boolean IsSignup) {
            this.IsSignup = IsSignup;
        }

        public String getFcmId() {
            return fcmId;
        }

        public void setFcmId(String fcmId) {
            this.fcmId = fcmId;
        }

        public int getBeatNumber() {
            return BeatNumber;
        }

        public void setBeatNumber(int BeatNumber) {
            this.BeatNumber = BeatNumber;
        }

        public int getDivisionId() {
            return DivisionId;
        }

        public void setDivisionId(int DivisionId) {
            this.DivisionId = DivisionId;
        }

        public String getDay() {
            return Day;
        }

        public void setDay(String Day) {
            this.Day = Day;
        }

        public double getLg() {
            return lg;
        }

        public void setLg(double lg) {
            this.lg = lg;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public boolean getActive() {
            return Active;
        }

        public void setActive(boolean Active) {
            this.Active = Active;
        }

        public boolean getDeleted() {
            return Deleted;
        }

        public void setDeleted(boolean Deleted) {
            this.Deleted = Deleted;
        }

        public String getClusterName() {
            return ClusterName;
        }

        public void setClusterName(String ClusterName) {
            this.ClusterName = ClusterName;
        }

        public int getClusterId() {
            return ClusterId;
        }

        public void setClusterId(int ClusterId) {
            this.ClusterId = ClusterId;
        }

        public int getExecutiveId() {
            return ExecutiveId;
        }

        public void setExecutiveId(int ExecutiveId) {
            this.ExecutiveId = ExecutiveId;
        }

        public double getMonthlyTurnOver() {
            return MonthlyTurnOver;
        }

        public void setMonthlyTurnOver(double MonthlyTurnOver) {
            this.MonthlyTurnOver = MonthlyTurnOver;
        }

        public String getUpdatedDate() {
            return UpdatedDate;
        }

        public void setUpdatedDate(String UpdatedDate) {
            this.UpdatedDate = UpdatedDate;
        }

        public String getCreatedDate() {
            return CreatedDate;
        }

        public void setCreatedDate(String CreatedDate) {
            this.CreatedDate = CreatedDate;
        }

        public String getEmailid() {
            return Emailid;
        }

        public void setEmailid(String Emailid) {
            this.Emailid = Emailid;
        }

        public String getBAGPSCoordinates() {
            return BAGPSCoordinates;
        }

        public void setBAGPSCoordinates(String BAGPSCoordinates) {
            this.BAGPSCoordinates = BAGPSCoordinates;
        }

        public String getCity() {
            return City;
        }

        public void setCity(String City) {
            this.City = City;
        }

        public int getCityid() {
            return Cityid;
        }

        public void setCityid(int Cityid) {
            this.Cityid = Cityid;
        }

        public String getLandMark() {
            return LandMark;
        }

        public void setLandMark(String LandMark) {
            this.LandMark = LandMark;
        }

        public String getShippingAddress() {
            return ShippingAddress;
        }

        public void setShippingAddress(String ShippingAddress) {
            this.ShippingAddress = ShippingAddress;
        }

        public String getBillingAddress() {
            return BillingAddress;
        }

        public void setBillingAddress(String BillingAddress) {
            this.BillingAddress = BillingAddress;
        }

        public String getPassword() {
            return Password;
        }

        public void setPassword(String Password) {
            this.Password = Password;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public String getWarehouseName() {
            return WarehouseName;
        }

        public void setWarehouseName(String WarehouseName) {
            this.WarehouseName = WarehouseName;
        }

        public String getMobile() {
            return Mobile;
        }

        public void setMobile(String Mobile) {
            this.Mobile = Mobile;
        }

        public int getWarehouseid() {
            return Warehouseid;
        }

        public void setWarehouseid(int Warehouseid) {
            this.Warehouseid = Warehouseid;
        }

        public String getShopName() {
            return ShopName;
        }

        public void setShopName(String ShopName) {
            this.ShopName = ShopName;
        }

        public String getSkcode() {
            return Skcode;
        }

        public void setSkcode(String Skcode) {
            this.Skcode = Skcode;
        }

        public int getCompanyId() {
            return CompanyId;
        }

        public void setCompanyId(int CompanyId) {
            this.CompanyId = CompanyId;
        }

        public int getCustomerId() {
            return CustomerId;
        }

        public void setCustomerId(int CustomerId) {
            this.CustomerId = CustomerId;
        }
    }
}
