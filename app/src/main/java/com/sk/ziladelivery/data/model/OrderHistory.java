package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.SerializedName;
import com.sk.ziladelivery.ui.views.viewmodels.DeliveryIssuance;

import java.util.List;

public class OrderHistory {
    @SerializedName("totalcash")
    private Integer totalcash;

    @SerializedName("Delivered")
    private Integer delivered;

    @SerializedName("Redispatched")
    private Integer redispatched;

    @SerializedName("Canceled")
    private Integer canceled;

    @SerializedName("deliveryIssuance")
    private DeliveryIssuance deliveryIssuance;

    @SerializedName("Orders")
    private List<Order> orders ;

    public Integer getTotalcash() {
        return totalcash;
    }

    public void setTotalcash(Integer totalcash) {
        this.totalcash = totalcash;
    }

    public Integer getDelivered() {
        return delivered;
    }

    public void setDelivered(Integer delivered) {
        this.delivered = delivered;
    }

    public Integer getRedispatched() {
        return redispatched;
    }

    public void setRedispatched(Integer redispatched) {
        this.redispatched = redispatched;
    }

    public Integer getCanceled() {
        return canceled;
    }

    public void setCanceled(Integer canceled) {
        this.canceled = canceled;
    }

    public DeliveryIssuance getDeliveryIssuance() {
        return deliveryIssuance;
    }

    public void setDeliveryIssuance(DeliveryIssuance deliveryIssuance) {
        this.deliveryIssuance = deliveryIssuance;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
