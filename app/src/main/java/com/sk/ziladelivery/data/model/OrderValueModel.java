package com.sk.ziladelivery.data.model;

public class OrderValueModel {


    private String AssignmentID;
    private String OrderNo;
    private String DateTime;
    private String Status;
    private String orderCount;
    private String TotalAmount;


    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String orderNo) {
        OrderNo = orderNo;
    }

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }



    public String getAssignmentID() {
        return AssignmentID;
    }

    public void setAssignmentID(String assignmentID) {
        AssignmentID = assignmentID;
    }


    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(String orderCount) {
        this.orderCount = orderCount;
    }

    public String getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        TotalAmount = totalAmount;
    }
}
