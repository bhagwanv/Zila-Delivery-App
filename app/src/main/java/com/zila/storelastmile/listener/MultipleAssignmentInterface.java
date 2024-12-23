package com.zila.storelastmile.listener;

import com.zila.storelastmile.data.model.SortOrderModel;

public interface MultipleAssignmentInterface {

    public void StartTimer(int deliveryIssuanceId);
    public void Details(int deliveryIssuanceId, boolean isTimerstart,int i);
    public void shortPathData(SortOrderModel sortOrderModel,int postion,int assginId);


}
