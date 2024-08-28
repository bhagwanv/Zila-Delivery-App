package com.sk.ziladelivery.listener;

import com.sk.ziladelivery.data.model.DeliveryPayments;

import java.util.ArrayList;

public interface MultiMposListener {


    void onMposAmountChange(ArrayList<DeliveryPayments> payment);


}
