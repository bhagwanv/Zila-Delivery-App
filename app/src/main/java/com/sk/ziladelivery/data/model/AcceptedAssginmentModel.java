package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public  class AcceptedAssginmentModel {


    @SerializedName("Message")
    private String Message;

    @SerializedName("status")
    private boolean status;

    @SerializedName("AssignmentAcceptPending")
    private ArrayList<AcceptedAssginmentListModel> AssignmentAcceptPending;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public ArrayList<AcceptedAssginmentListModel> getAssignmentAcceptPending() {
        return AssignmentAcceptPending;
    }

    public void setAssignmentAcceptPending(ArrayList<AcceptedAssginmentListModel> AssignmentAcceptPending) {
        this.AssignmentAcceptPending = AssignmentAcceptPending;
    }
}
