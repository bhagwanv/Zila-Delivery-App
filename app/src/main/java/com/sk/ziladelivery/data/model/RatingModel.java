package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RatingModel {
    @SerializedName("UserId")
    int userId;
    @SerializedName("AppType")
    int AppType;
    @SerializedName("Rating")
    int Rating;
    @SerializedName("OrderId")
    int OrderId;
    @SerializedName("ShopVisited")
    String ShopVisited;
    @SerializedName("RatingDetails")
    public ArrayList<UserRatingDetailDc> ratingDetails;
    @SerializedName("DisplayName")
    String DisplayName;
    @SerializedName("ProfilePic")
    String ProfilePic;
    @SerializedName("OrderedDate")
    String OrderedDate;
    @SerializedName("IsRemoveFront")
    public boolean isRemoveFront;


    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setAppType(int appType) {
        AppType = appType;
    }

    public void setRating(int rating) {
        Rating = rating;
    }

    public void setOrderId(int orderId) {
        OrderId = orderId;
    }

    public void setDisplayName(String displayName) {
        DisplayName = displayName;
    }

    public void setProfilePic(String profilePic) {
        ProfilePic = profilePic;
    }

    public void setOrderedDate(String orderedDate) {
        OrderedDate = orderedDate;
    }

    public void setRemoveFront(boolean removeFront) {
        isRemoveFront = removeFront;
    }

    public int getUserId() {
        return userId;
    }

    public int getAppType() {
        return AppType;
    }

    public int getRating() {
        return Rating;
    }

    public int getOrderId() {
        return OrderId;
    }

    public String getShopVisited() {
        return ShopVisited;
    }

    public void setShopVisited(String shopVisited) {
        ShopVisited = shopVisited;
    }

    public ArrayList<UserRatingDetailDc> getRatingDetails() {
        return ratingDetails;
    }

    public void setRatingDetails(ArrayList<UserRatingDetailDc> ratingDetails) {
        this.ratingDetails = ratingDetails;
    }

    public String getDisplayName() {
        return DisplayName;
    }

    public String getProfilePic() {
        return ProfilePic;
    }

    public String getOrderedDate() {
        return OrderedDate;
    }

    public boolean isRemoveFront() {
        return isRemoveFront;
    }
}
