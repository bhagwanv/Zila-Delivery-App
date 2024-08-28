package com.sk.ziladelivery.data.model;
import com.google.gson.annotations.SerializedName;

public class TripDeliveryPaymentModel {

    @SerializedName("TransId")
    private String TransId;

    @SerializedName("amount")
    private long amount;

    @SerializedName("PaymentFrom")
    private String PaymentFrom;

    @SerializedName("ChequeImageUrl")
    private String ChequeImageUrl;

    @SerializedName("ChequeBankName")
    private String ChequeBankName ;

    @SerializedName("PaymentDate")
    private String PaymentDate;


    public TripDeliveryPaymentModel(String transId, long amount, String paymentFrom, String chequeImageUrl, String chequeBankName, String paymentDate) {
        TransId = transId;
        this.amount = amount;
        PaymentFrom = paymentFrom;
        ChequeImageUrl = chequeImageUrl;
        ChequeBankName = chequeBankName;
        PaymentDate = paymentDate;
    }

    public String getTransId() {
        return TransId;
    }

    public void setTransId(String transId) {
        TransId = transId;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getPaymentFrom() {
        return PaymentFrom;
    }

    public void setPaymentFrom(String paymentFrom) {
        PaymentFrom = paymentFrom;
    }

    public String getChequeImageUrl() {
        return ChequeImageUrl;
    }

    public void setChequeImageUrl(String chequeImageUrl) {
        ChequeImageUrl = chequeImageUrl;
    }

    public String getChequeBankName() {
        return ChequeBankName;
    }

    public void setChequeBankName(String chequeBankName) {
        ChequeBankName = chequeBankName;
    }

    public String getPaymentDate() {
        return PaymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        PaymentDate = paymentDate;
    }

}
