package com.sk.ziladelivery.data.model;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class PaymentRequestModel {

    @SerializedName("ZilaTripDetailId")
    private int TripPlannerConfirmedDetailId;

    @SerializedName("ZilaTripOrderId")
    private int TripPlannerConfirmedOrderId ;

    @SerializedName("OrderIds")
    private List<Integer> OrderIds;

    @SerializedName("CashAmount")
    private double CashAmount ;

    @SerializedName("TripDeliveryPayments")
    private ArrayList<DeliveryPayments> tripDeliveryList;

    public PaymentRequestModel(int tripPlannerConfirmedDetailId, int tripPlannerConfirmedOrderId, List<Integer> orderIds, double cashAmount, ArrayList<DeliveryPayments> tripDeliveryList) {
        TripPlannerConfirmedDetailId = tripPlannerConfirmedDetailId;
        TripPlannerConfirmedOrderId = tripPlannerConfirmedOrderId;
        OrderIds = orderIds;
        CashAmount = cashAmount;
        this.tripDeliveryList = tripDeliveryList;
    }

    public int getTripPlannerConfirmedDetailId() {
        return TripPlannerConfirmedDetailId;
    }

    public void setTripPlannerConfirmedDetailId(int tripPlannerConfirmedDetailId) {
        TripPlannerConfirmedDetailId = tripPlannerConfirmedDetailId;
    }

    public int getTripPlannerConfirmedOrderId() {
        return TripPlannerConfirmedOrderId;
    }

    public void setTripPlannerConfirmedOrderId(int tripPlannerConfirmedOrderId) {
        TripPlannerConfirmedOrderId = tripPlannerConfirmedOrderId;
    }

    public List<Integer> getOrderIds() {
        return OrderIds;
    }

    public void setOrderIds(List<Integer> orderIds) {
        OrderIds = orderIds;
    }

    public double getCashAmount() {
        return CashAmount;
    }

    public void setCashAmount(double cashAmount) {
        CashAmount = cashAmount;
    }

    public ArrayList<DeliveryPayments> getTripDeliveryList() {
        return tripDeliveryList;
    }

    public void setTripDeliveryList(ArrayList<DeliveryPayments> tripDeliveryList) {
        this.tripDeliveryList = tripDeliveryList;
    }
}
