package com.sk.ziladelivery.listener;

public interface TripCompleteListener {
    void redispatchClick(int orderid, String reason);
    void CancelClick(int orderid, String reason);
    void UnloadingClick(int orderid, String reason, int deliveryIssuanceId);
}
