package com.sk.ziladelivery.listener;

import android.widget.ImageView;

import com.sk.ziladelivery.data.model.DeliveryPayments;

import java.util.ArrayList;

public interface OrderchequeimageListener {
    void onImageClick(ImageView imageView,int position);
    void onAmountChange(ArrayList<DeliveryPayments> payment);



}
