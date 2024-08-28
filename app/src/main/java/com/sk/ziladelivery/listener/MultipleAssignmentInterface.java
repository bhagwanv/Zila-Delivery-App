package com.sk.ziladelivery.listener;

import com.sk.ziladelivery.data.model.SortOrderModel;

public interface MultipleAssignmentInterface {

    public void StartTimer(int deliveryIssuanceId);
    public void Details(int deliveryIssuanceId, boolean isTimerstart,int i);
    public void shortPathData(SortOrderModel sortOrderModel,int postion,int assginId);


}
