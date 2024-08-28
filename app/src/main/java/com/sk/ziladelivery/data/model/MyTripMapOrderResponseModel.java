package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MyTripMapOrderResponseModel {

    @Expose
    @SerializedName("customerListDc")
    private ArrayList<CustomerListDc> customerListDcs;
    @Expose
    @SerializedName("mapview")
    private MapViewModel mapViewModel;

    public ArrayList<CustomerListDc> getCustomerListDcs() {
        return customerListDcs;
    }

    public void setCustomerListDcs(ArrayList<CustomerListDc> customerListDcs) {
        this.customerListDcs = customerListDcs;
    }

    public MapViewModel getMapViewModel() {
        return mapViewModel;
    }

    public void setMapViewModel(MapViewModel mapViewModel) {
        this.mapViewModel = mapViewModel;
    }

    public  class CustomerListDc {
        @Expose
        @SerializedName("CustomerId")
        private int CustomerId;
        @Expose
        @SerializedName("MobileNumber")
        private String MobileNumber;
        @Expose
        @SerializedName("Skcode")
        private String Skcode;
        @Expose
        @SerializedName("CustomerName")
        private String CustomerName;
        @Expose
        @SerializedName("ShippingAddress")
        private String ShippingAddress;
        @Expose
        @SerializedName("TotalAmount")
        private double TotalAmount;
        @Expose
        @SerializedName("Lat")
        private double Lat;
        @Expose
        @SerializedName("Lng")
        private double Lng;
        @Expose
        @SerializedName("TotalTimeInMins")
        private int TotalTimeInMins;
        @Expose
        @SerializedName("UnlodingTime")
        private int UnlodingTime;

        public int getCustomerId() {
            return CustomerId;
        }

        public void setCustomerId(int customerId) {
            CustomerId = customerId;
        }

        public String getMobileNumber() {
            return MobileNumber;
        }

        public void setMobileNumber(String mobileNumber) {
            MobileNumber = mobileNumber;
        }

        public String getSkcode() {
            return Skcode;
        }

        public void setSkcode(String skcode) {
            Skcode = skcode;
        }

        public String getCustomerName() {
            return CustomerName;
        }

        public void setCustomerName(String customerName) {
            CustomerName = customerName;
        }

        public String getShippingAddress() {
            return ShippingAddress;
        }

        public void setShippingAddress(String shippingAddress) {
            ShippingAddress = shippingAddress;
        }

        public double getTotalAmount() {
            return TotalAmount;
        }

        public void setTotalAmount(double totalAmount) {
            TotalAmount = totalAmount;
        }

        public double getLat() {
            return Lat;
        }

        public void setLat(double lat) {
            Lat = lat;
        }

        public double getLng() {
            return Lng;
        }

        public void setLng(double lng) {
            Lng = lng;
        }

        public int getTotalTimeInMins() {
            return TotalTimeInMins;
        }

        public void setTotalTimeInMins(int totalTimeInMins) {
            TotalTimeInMins = totalTimeInMins;
        }

        public int getUnlodingTime() {
            return UnlodingTime;
        }

        public void setUnlodingTime(int unlodingTime) {
            UnlodingTime = unlodingTime;
        }
    }

    public  class MapViewModel {
        @Expose
        @SerializedName("TotalKM")
        private int TotalKM;
        @Expose
        @SerializedName("TotalOrderCompletionTime")
        private int TotalOrderCompletionTime;
        @Expose
        @SerializedName("TotalunlodingTime")
        private int TotalunlodingTime;

        public double getWarehouesLat() {
            return WarehouesLat;
        }

        public void setWarehouesLat(double warehouesLat) {
            WarehouesLat = warehouesLat;
        }

        public double getWarehouesLng() {
            return WarehouesLng;
        }

        public void setWarehouesLng(double warehouesLng) {
            WarehouesLng = warehouesLng;
        }

        @Expose
        @SerializedName("WarehouesLat")
        private double WarehouesLat;
        @Expose
        @SerializedName("WarehouesLng")
        private double WarehouesLng;



        public int getTotalKM() {
            return TotalKM;
        }

        public void setTotalKM(int totalKM) {
            TotalKM = totalKM;
        }

        public int getTotalOrderCompletionTime() {
            return TotalOrderCompletionTime;
        }

        public void setTotalOrderCompletionTime(int totalOrderCompletionTime) {
            TotalOrderCompletionTime = totalOrderCompletionTime;
        }

        public int getTotalunlodingTime() {
            return TotalunlodingTime;
        }

        public void setTotalunlodingTime(int totalunlodingTime) {
            TotalunlodingTime = totalunlodingTime;
        }
    }
}
