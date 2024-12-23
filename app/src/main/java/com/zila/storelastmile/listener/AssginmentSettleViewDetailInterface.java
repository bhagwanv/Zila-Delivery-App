package com.zila.storelastmile.listener;

import com.zila.storelastmile.data.model.AssginmentSettleResponseModel;

import java.util.ArrayList;

public interface AssginmentSettleViewDetailInterface {
    void viewDetailClick(int deliveryIssuanceId);

    void saveCommentClick(ArrayList<AssginmentSettleResponseModel.DBoyAssignmentDeposits> deliveryIssuanceId);


}
