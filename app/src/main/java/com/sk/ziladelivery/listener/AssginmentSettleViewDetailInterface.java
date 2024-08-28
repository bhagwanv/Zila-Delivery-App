package com.sk.ziladelivery.listener;

import com.sk.ziladelivery.data.model.AssginmentSettleResponseModel;

import java.util.ArrayList;

public interface AssginmentSettleViewDetailInterface {
    void viewDetailClick(int deliveryIssuanceId);

    void saveCommentClick(ArrayList<AssginmentSettleResponseModel.DBoyAssignmentDeposits> deliveryIssuanceId);


}
