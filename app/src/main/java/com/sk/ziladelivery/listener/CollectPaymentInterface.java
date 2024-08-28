package com.sk.ziladelivery.listener;

import com.sk.ziladelivery.data.model.CollectionPaymentModel;
import com.sk.ziladelivery.ui.views.adapter.CollectPayemtAdapter;

public interface CollectPaymentInterface {
    void checkBoxClicked(boolean isChecked, int adapterPosition, int postion, CollectPayemtAdapter collectPayemtAdapter);
    void selectPaymentType(int postion, CollectionPaymentModel.PaymentGroupwisesListModel model);
    void removeOrder(CollectionPaymentModel.CustomerorderinfoEntity TripPlannerConfirmedOrderId, int postion,boolean isPaymentDone);

}