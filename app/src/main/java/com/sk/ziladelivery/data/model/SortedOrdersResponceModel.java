package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class SortedOrdersResponceModel implements Serializable {


    @SerializedName("SortedOrders")
    private ArrayList<SortedOrdersModel> sortedOrdersModels;


    public ArrayList<SortedOrdersModel> getSortedOrdersModels() {
        return sortedOrdersModels;
    }

    public void setSortedOrdersModels(ArrayList<SortedOrdersModel> sortedOrdersModels) {
        this.sortedOrdersModels = sortedOrdersModels;
    }
}
