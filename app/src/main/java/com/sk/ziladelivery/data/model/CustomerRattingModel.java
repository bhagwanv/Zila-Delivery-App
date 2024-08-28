package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CustomerRattingModel {

    @SerializedName("DeliveryDboyRatingOrder")
    private DeliveryDboyRatingOrderModel DeliveryDboyRatingOrderModel;
    @SerializedName("userRatingDc")
    private ArrayList<RatingModel> ratingModels;

    public  DeliveryDboyRatingOrderModel getDeliveryDboyRatingOrderModel() {
        return DeliveryDboyRatingOrderModel;
    }

    public void setDeliveryDboyRatingOrderModel(DeliveryDboyRatingOrderModel deliveryDboyRatingOrderModel) {
        DeliveryDboyRatingOrderModel = deliveryDboyRatingOrderModel;
    }

    public ArrayList<RatingModel> getRatingModels() {
        return ratingModels;
    }

    public void setRatingModels(ArrayList<RatingModel> ratingModels) {
        this.ratingModels = ratingModels;
    }
}
