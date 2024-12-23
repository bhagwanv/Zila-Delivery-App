package com.zila.storelastmile.listener;

import com.zila.storelastmile.data.model.DeliveryPayments;

import java.util.ArrayList;

public interface MultiRtgsListener {

    void onRtgsAmountChange(ArrayList<DeliveryPayments> payment);
}
