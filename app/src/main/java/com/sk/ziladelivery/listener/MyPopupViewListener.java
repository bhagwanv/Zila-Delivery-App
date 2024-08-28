package com.sk.ziladelivery.listener;

import android.widget.ImageView;

import com.sk.ziladelivery.data.model.MyTripOrderResponseModel;

import java.util.ArrayList;

/**
 * Created by lenovo on 3/3/2018.
 */

public interface MyPopupViewListener {
    public void callPopupClicked(int Position, MyTripOrderResponseModel myTripOrderResponseModel, ArrayList<MyTripOrderResponseModel.OrderlistEntity> orderlist);
    public void callVoiceRecoding(int Position, MyTripOrderResponseModel myTripOrderResponseModel, ImageView tvVoice, ImageView imageView);

}
