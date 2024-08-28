package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AssginmentHistoryResponseModel {
    @Expose
    @SerializedName("AssignmentId")
    private String AssignmentId;
    @Expose
    @SerializedName("CreatedDate")
    private String CreatedDate;
    @Expose
    @SerializedName("IsUNSignOffUrl")
    private String IsUNSignOffUrl;
    @Expose
    @SerializedName("SignOffUrl")
    private String SignOffUrl;
    @Expose
    @SerializedName("DboyName")
    private String DboyName;
    @Expose
    @SerializedName("DBoyId")
    private int DBoyId;
    @Expose
    @SerializedName("Id")
    private int Id;

    public String getAssignmentId() {
        return AssignmentId;
    }

    public void setAssignmentId(String AssignmentId) {
        this.AssignmentId = AssignmentId;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String CreatedDate) {
        this.CreatedDate = CreatedDate;
    }

    public String getIsUNSignOffUrl() {
        return IsUNSignOffUrl;
    }

    public String getSignOffUrl() {
        return SignOffUrl;
    }

    public void setIsUNSignOffUrl(String IsUNSignOffUrl) {
        this.IsUNSignOffUrl = IsUNSignOffUrl;
    }

    public String getDboyName() {
        return DboyName;
    }

    public void setDboyName(String DboyName) {
        this.DboyName = DboyName;
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
}
