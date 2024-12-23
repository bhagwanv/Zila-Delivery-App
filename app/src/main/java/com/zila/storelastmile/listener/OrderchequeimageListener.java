package com.zila.storelastmile.listener;

import android.widget.ImageView;

import com.zila.storelastmile.data.model.DeliveryPayments;

import java.util.ArrayList;

public interface OrderchequeimageListener {
    void onImageClick(ImageView imageView,int position);
    void onAmountChange(ArrayList<DeliveryPayments> payment);



}
