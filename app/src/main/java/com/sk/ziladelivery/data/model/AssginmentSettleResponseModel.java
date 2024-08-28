package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class AssginmentSettleResponseModel implements Serializable {
    @Expose
    @SerializedName(value = "DBoyAssignmentDeposits", alternate = "DBoyAssignmentDepositDc")
    private ArrayList<DBoyAssignmentDeposits> DBoyAssignmentDeposits;
    @Expose
    @SerializedName("CreatedDate")
    private String CreatedDate;
    @Expose
    @SerializedName("Signature")
    private String Signature;
    @Expose
    @SerializedName("IsUNSignOffUrl")
    private String IsUNSignOffUrl = " ";

    @Expose
    @SerializedName("DBoyId")
    private int DBoyId;
    @Expose
    @SerializedName("Id")
    private int Id;

    public AssginmentSettleResponseModel(int id, int DBoyId, String Signature, String IsUNSignOffUrl, ArrayList<AssginmentSettleResponseModel.DBoyAssignmentDeposits> list) {
        this.Id = id;
        this.DBoyId = DBoyId;
        this.Signature = Signature;
        this.IsUNSignOffUrl = IsUNSignOffUrl;
        this.DBoyAssignmentDeposits = list;
    }

    public String getSignature() {
        return Signature;
    }

    public void setSignature(String signature) {
        Signature = signature;
    }

    public String getIsUNSignOffUrl() {
        return IsUNSignOffUrl;
    }

    public void setIsUNSignOffUrl(String isUNSignOffUrl) {
        IsUNSignOffUrl = isUNSignOffUrl;
    }

    public ArrayList<DBoyAssignmentDeposits> getDBoyAssignmentDeposits() {
        return DBoyAssignmentDeposits;
    }

    public void setDBoyAssignmentDeposits(ArrayList<DBoyAssignmentDeposits> DBoyAssignmentDeposits) {
        this.DBoyAssignmentDeposits = DBoyAssignmentDeposits;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String CreatedDate) {
        this.CreatedDate = CreatedDate;
    }

    public int getDBoyId() {
        return DBoyId;
    }

    public void setDBoyId(int DBoyId) {
        this.DBoyId = DBoyId;
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public class DBoyAssignmentDeposits implements Serializable {
        @Expose
        @SerializedName("Deliveryissueid")
        private int Deliveryissueid;
        @Expose
        @SerializedName("Id")
        private int Id;

        @Expose
        @SerializedName("Comment")
        private String Comment;

        public int getDeliveryissueid() {
            return Deliveryissueid;
        }

        public void setDeliveryissueid(int Deliveryissueid) {
            this.Deliveryissueid = Deliveryissueid;
        }

        public int getId() {
            return Id;
        }

        public void setId(int Id) {
            this.Id = Id;
        }

        public String getComment() {
            return Comment;
        }

        public void setComment(String Comment) {
            this.Comment = Comment;
        }
    }
}
