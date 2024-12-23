package com.zila.storelastmile.listener;

public interface NewAcceptRejectAssignmenClick {
    void acceptClicked(int deliveryIssuanceId, String aTrue);
    void rejectClicked(int deliveryIssuanceId, String aTrue);
    void viewAssignmentClicked(int deliveryIssuanceId);
}
