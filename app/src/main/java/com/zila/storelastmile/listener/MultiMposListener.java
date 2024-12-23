package com.zila.storelastmile.listener;

import com.zila.storelastmile.data.model.DeliveryPayments;

import java.util.ArrayList;

public interface MultiMposListener {


    void onMposAmountChange(ArrayList<DeliveryPayments> payment);


}
