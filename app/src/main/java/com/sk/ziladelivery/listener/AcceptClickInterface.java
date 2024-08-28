package com.sk.ziladelivery.listener;

public interface AcceptClickInterface {
    void acceptClicked(int deliveryIssuanceId, String aTrue);
    void rejectClicked(int deliveryIssuanceId, String aTrue);
}