package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AssinmentDetail {
    @SerializedName("DeliveryIssuanceId")
    public int DeliveryIssuanceId;

    @SerializedName("Cityid")
    public int Cityid;

    @SerializedName("city")
    public String city;

    @SerializedName("DisplayName")
    public String DisplayName;

    @SerializedName("CompanyId")
    public int CompanyId;

    @SerializedName("WarehouseId")
    public int WarehouseId;

    @SerializedName("PeopleID")
    public int PeopleID;

    @SerializedName("VehicleId")
    public int VehicleId;

    @SerializedName("VehicleNumber")
    public String VehicleNumber;

    @SerializedName("Status")
    public String Status;

    @SerializedName("RejectReason")
    public String RejectReason;

    @SerializedName("OrderdispatchIds")
    public String OrderdispatchIds;

    @SerializedName("OrderIds")
    public String OrderIds;

    @SerializedName("Acceptance")
    public boolean Acceptance;

    @SerializedName("IsActive")
    public boolean IsActive;

    @SerializedName("CreatedDate")
    public String CreatedDate;

    @SerializedName("UpdatedDate")
    public String UpdatedDate;

    @SerializedName("IdealTime")
    public double IdealTime;

    @SerializedName("TravelDistance")
    public int TravelDistance;

    @SerializedName("TotalAssignmentAmount")
    public int TotalAssignmentAmount;

    @SerializedName("details")
    public ArrayList<AssinmentDetail.PendingDetailsModel> pendingDetailsModels;

    @SerializedName("AssignedOrders")
    public String AssignedOrders;

    @SerializedName("userid")
    public String userid;

    public int getDeliveryIssuanceId() {
        return DeliveryIssuanceId;
    }

    public void setDeliveryIssuanceId(int deliveryIssuanceId) {
        DeliveryIssuanceId = deliveryIssuanceId;
    }

    public int getCityid() {
        return Cityid;
    }

    public void setCityid(int cityid) {
        Cityid = cityid;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDisplayName() {
        return DisplayName;
    }

    public void setDisplayName(String displayName) {
        DisplayName = displayName;
    }

    public int getCompanyId() {
        return CompanyId;
    }

    public void setCompanyId(int companyId) {
        CompanyId = companyId;
    }

    public int getWarehouseId() {
        return WarehouseId;
    }

    public void setWarehouseId(int warehouseId) {
        WarehouseId = warehouseId;
    }

    public int getPeopleID() {
        return PeopleID;
    }

    public void setPeopleID(int peopleID) {
        PeopleID = peopleID;
    }

    public int getVehicleId() {
        return VehicleId;
    }

    public void setVehicleId(int vehicleId) {
        VehicleId = vehicleId;
    }

    public String getVehicleNumber() {
        return VehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        VehicleNumber = vehicleNumber;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getRejectReason() {
        return RejectReason;
    }

    public void setRejectReason(String rejectReason) {
        RejectReason = rejectReason;
    }

    public String getOrderdispatchIds() {
        return OrderdispatchIds;
    }

    public void setOrderdispatchIds(String orderdispatchIds) {
        OrderdispatchIds = orderdispatchIds;
    }

    public String getOrderIds() {
        return OrderIds;
    }

    public void setOrderIds(String orderIds) {
        OrderIds = orderIds;
    }

    public boolean isAcceptance() {
        return Acceptance;
    }

    public void setAcceptance(boolean acceptance) {
        Acceptance = acceptance;
    }

    public boolean isActive() {
        return IsActive;
    }

    public void setActive(boolean active) {
        IsActive = active;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    public String getUpdatedDate() {
        return UpdatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        UpdatedDate = updatedDate;
    }

    public double getIdealTime() {
        return IdealTime;
    }

    public void setIdealTime(double idealTime) {
        IdealTime = idealTime;
    }

    public int getTravelDistance() {
        return TravelDistance;
    }

    public void setTravelDistance(int travelDistance) {
        TravelDistance = travelDistance;
    }

    public int getTotalAssignmentAmount() {
        return TotalAssignmentAmount;
    }

    public void setTotalAssignmentAmount(int totalAssignmentAmount) {
        TotalAssignmentAmount = totalAssignmentAmount;
    }

    public ArrayList<AssinmentDetail.PendingDetailsModel> getPendingDetailsModels() {
        return pendingDetailsModels;
    }

    public void setPendingDetailsModels(ArrayList<AssinmentDetail.PendingDetailsModel> pendingDetailsModels) {
        this.pendingDetailsModels = pendingDetailsModels;
    }

    public String getAssignedOrders() {
        return AssignedOrders;
    }

    public void setAssignedOrders(String assignedOrders) {
        AssignedOrders = assignedOrders;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public class PendingDetailsModel {
        @SerializedName("IssuanceDetailsId")
        public int IssuanceDetailsId;

        @SerializedName("OrderDispatchedMasterId")
        public int OrderDispatchedMasterId;

        @SerializedName("OrderDispatchedDetailsId")
        public int OrderDispatchedDetailsId;

        @SerializedName("OrderQty")
        public String  OrderQty;

        @SerializedName("OrderId")
        public String OrderId;

        @SerializedName("IsDispatched")
        public boolean IsDispatched;

        @SerializedName("qty")
        public int qty;

        @SerializedName("itemNumber")
        public String itemNumber;

        @SerializedName("ItemId")
        public int ItemId;

        @SerializedName("itemname")
        public String itemname;

        public int getIssuanceDetailsId() {
            return IssuanceDetailsId;
        }

        public void setIssuanceDetailsId(int issuanceDetailsId) {
            IssuanceDetailsId = issuanceDetailsId;
        }

        public int getOrderDispatchedMasterId() {
            return OrderDispatchedMasterId;
        }

        public void setOrderDispatchedMasterId(int orderDispatchedMasterId) {
            OrderDispatchedMasterId = orderDispatchedMasterId;
        }

        public int getOrderDispatchedDetailsId() {
            return OrderDispatchedDetailsId;
        }

        public void setOrderDispatchedDetailsId(int orderDispatchedDetailsId) {
            OrderDispatchedDetailsId = orderDispatchedDetailsId;
        }

        public String getOrderQty() {
            return OrderQty;
        }

        public void setOrderQty(String orderQty) {
            OrderQty = orderQty;
        }

        public String getOrderId() {
            return OrderId;
        }

        public void setOrderId(String orderId) {
            OrderId = orderId;
        }

        public boolean isDispatched() {
            return IsDispatched;
        }

        public void setDispatched(boolean dispatched) {
            IsDispatched = dispatched;
        }

        public int getQty() {
            return qty;
        }

        public void setQty(int qty) {
            this.qty = qty;
        }

        public String getItemNumber() {
            return itemNumber;
        }

        public void setItemNumber(String itemNumber) {
            this.itemNumber = itemNumber;
        }

        public int getItemId() {
            return ItemId;
        }

        public void setItemId(int itemId) {
            ItemId = itemId;
        }

        public String getItemname() {
            return itemname;
        }

        public void setItemname(String itemname) {
            this.itemname = itemname;
        }

    }

}
