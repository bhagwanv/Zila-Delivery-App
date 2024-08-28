package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public  class CancellationModel {


    @Expose
    @SerializedName("CompareCancellationPercant")
    private double comparecancellationpercant;
    @Expose
    @SerializedName("LastCancellationPercant")
    private double lastcancellationpercant;
    @Expose
    @SerializedName("CancellationPercant")
    private double cancellationpercant;
    @Expose
    @SerializedName("CompareCountPercent")
    private double comparecountpercent;
    @Expose
    @SerializedName("CancelCountDiff")
    private int cancelcountdiff;
    @Expose
    @SerializedName("CancelCount")
    private int cancelcount;
    @Expose
    @SerializedName("CompareAmountPercent")
    private double compareamountpercent;
    @Expose
    @SerializedName("CancelAmountDiff")
    private double cancelamountdiff;
    @Expose
    @SerializedName("CancelAmount")
    private double cancelamount;
    @Expose
    @SerializedName("WarningCount")
    private int WarningCount;

    @Expose
    @SerializedName("Backgroundcolor")
    private String Backgroundcolor;

    public String getBackgroundcolor() {
        return Backgroundcolor;
    }

    public void setBackgroundcolor(String backgroundcolor) {
        Backgroundcolor = backgroundcolor;
    }

    public int getWarningCount() {
        return WarningCount;
    }

    public void setWarningCount(int warningCount) {
        WarningCount = warningCount;
    }

    public double getComparecancellationpercant() {
        return comparecancellationpercant;
    }

    public void setComparecancellationpercant(double comparecancellationpercant) {
        this.comparecancellationpercant = comparecancellationpercant;
    }

    public double getLastcancellationpercant() {
        return lastcancellationpercant;
    }

    public void setLastcancellationpercant(double lastcancellationpercant) {
        this.lastcancellationpercant = lastcancellationpercant;
    }

    public double getCancellationpercant() {
        return cancellationpercant;
    }

    public void setCancellationpercant(double cancellationpercant) {
        this.cancellationpercant = cancellationpercant;
    }

    public double getComparecountpercent() {
        return comparecountpercent;
    }

    public void setComparecountpercent(double comparecountpercent) {
        this.comparecountpercent = comparecountpercent;
    }

    public int getCancelcountdiff() {
        return cancelcountdiff;
    }

    public void setCancelcountdiff(int cancelcountdiff) {
        this.cancelcountdiff = cancelcountdiff;
    }

    public int getCancelcount() {
        return cancelcount;
    }

    public void setCancelcount(int cancelcount) {
        this.cancelcount = cancelcount;
    }

    public double getCompareamountpercent() {
        return compareamountpercent;
    }

    public void setCompareamountpercent(double compareamountpercent) {
        this.compareamountpercent = compareamountpercent;
    }

    public double getCancelamountdiff() {
        return cancelamountdiff;
    }

    public void setCancelamountdiff(double cancelamountdiff) {
        this.cancelamountdiff = cancelamountdiff;
    }

    public double getCancelamount() {
        return cancelamount;
    }

    public void setCancelamount(double cancelamount) {
        this.cancelamount = cancelamount;
    }
}
