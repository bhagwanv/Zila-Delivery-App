package com.sk.ziladelivery.data.model;

public class RoutModel {

    private String Skcode;
    private String CustomerName;
    private String ShippingAddress;

    public RoutModel(String skcode, String customerName, String shippingAddress) {
        Skcode = skcode;
        CustomerName = customerName;
        ShippingAddress = shippingAddress;
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


}
