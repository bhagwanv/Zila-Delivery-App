package com.sk.ziladelivery.listener;

public interface NewAcceptRejectAssignmenClick {
    void acceptClicked(int deliveryIssuanceId, String aTrue);
    void rejectClicked(int deliveryIssuanceId, String aTrue);
    void viewAssignmentClicked(int deliveryIssuanceId);
}
