package com.sk.ziladelivery.listener;

import com.sk.ziladelivery.data.model.TripOrderStatusUpdateModel;

public interface OrderDetailReturnInterface {
    void checkBoxClicked(boolean isChecked, int position);
    void onButtonClick(String buttonText, int postion, int orderId,TripOrderStatusUpdateModel.CustomerorderinfoEntity customerorderinfoEntity);

}