package com.zila.storelastmile.listener;

import com.zila.storelastmile.data.model.CollectionPaymentModel;
import com.zila.storelastmile.ui.views.adapter.CollectPayemtAdapter;

public interface CollectPaymentInterface {
    void checkBoxClicked(boolean isChecked, int adapterPosition, int postion, CollectPayemtAdapter collectPayemtAdapter);
    void selectPaymentType(int postion, CollectionPaymentModel.PaymentGroupwisesListModel model);
    void removeOrder(CollectionPaymentModel.CustomerorderinfoEntity TripPlannerConfirmedOrderId, int postion,boolean isPaymentDone);

}