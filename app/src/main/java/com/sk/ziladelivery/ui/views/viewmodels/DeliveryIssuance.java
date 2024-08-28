
package com.sk.ziladelivery.ui.views.viewmodels;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class DeliveryIssuance implements Serializable
{

    @SerializedName("DeliveryIssuanceId")
    private Integer deliveryIssuanceId;

    @SerializedName("Cityid")
    private Integer cityid;

    @SerializedName("city")
    private String city;

    @SerializedName("DisplayName")
    private String displayName;

    @SerializedName("CompanyId")
    private Integer companyId;

    @SerializedName("WarehouseId")
    private Integer warehouseId;

    @SerializedName("PeopleID")
    private Integer peopleID;

    @SerializedName("VehicleId")
    private Integer vehicleId;

    @SerializedName("VehicleNumber")
    private String vehicleNumber;

    @SerializedName("Status")
    private String status;

    @SerializedName("RejectReason")
    private String rejectReason;

    @SerializedName("OrderdispatchIds")
    private String orderdispatchIds;

    @SerializedName("OrderIds")
    private String orderIds;

    @SerializedName("Acceptance")
    private Boolean acceptance;

    @SerializedName("IsActive")
    private Boolean isActive;

    @SerializedName("CreatedDate")
    private String createdDate;

    @SerializedName("UpdatedDate")
    private String updatedDate;

    @SerializedName("IdealTime")
    private Double idealTime;

    @SerializedName("TravelDistance")
    private Integer travelDistance;

    @SerializedName("TotalAssignmentAmount")
    private Integer totalAssignmentAmount;

    @SerializedName("details")
    private Object details;

    @SerializedName("AssignedOrders")
    private Object assignedOrders;

    @SerializedName("userid")
    private Integer userid;




    public Integer getDeliveryIssuanceId() {
        return deliveryIssuanceId;
    }

    public void setDeliveryIssuanceId(Integer deliveryIssuanceId) {
        this.deliveryIssuanceId = deliveryIssuanceId;
    }

    public Integer getCityid() {
        return cityid;
    }

    public void setCityid(Integer cityid) {
        this.cityid = cityid;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public Integer getPeopleID() {
        return peopleID;
    }

    public void setPeopleID(Integer peopleID) {
        this.peopleID = peopleID;
    }

    public Integer getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Integer vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public String getOrderdispatchIds() {
        return orderdispatchIds;
    }

    public void setOrderdispatchIds(String orderdispatchIds) {
        this.orderdispatchIds = orderdispatchIds;
    }

    public String getOrderIds() {
        return orderIds;
    }

    public void setOrderIds(String orderIds) {
        this.orderIds = orderIds;
    }

    public Boolean getAcceptance() {
        return acceptance;
    }

    public void setAcceptance(Boolean acceptance) {
        this.acceptance = acceptance;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Double getIdealTime() {
        return idealTime;
    }

    public void setIdealTime(Double idealTime) {
        this.idealTime = idealTime;
    }

    public Integer getTravelDistance() {
        return travelDistance;
    }

    public void setTravelDistance(Integer travelDistance) {
        this.travelDistance = travelDistance;
    }

    public Integer getTotalAssignmentAmount() {
        return totalAssignmentAmount;
    }

    public void setTotalAssignmentAmount(Integer totalAssignmentAmount) {
        this.totalAssignmentAmount = totalAssignmentAmount;
    }

    public Object getDetails() {
        return details;
    }

    public void setDetails(Object details) {
        this.details = details;
    }

    public Object getAssignedOrders() {
        return assignedOrders;
    }

    public void setAssignedOrders(Object assignedOrders) {
        this.assignedOrders = assignedOrders;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

}
