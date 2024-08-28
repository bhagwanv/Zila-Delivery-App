package com.sk.ziladelivery.data.model;

public class AssignmentAdapterModel {

        private String AssignmentID;
        private String OrderNo;
        private String DateTime;
        private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAssignmentID() {
            return AssignmentID;
        }

        public void setAssignmentID(String assignmentID) {
            AssignmentID = assignmentID;
        }

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




}
