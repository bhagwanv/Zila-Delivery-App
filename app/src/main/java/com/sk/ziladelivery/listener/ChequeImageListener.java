package com.sk.ziladelivery.listener;

import android.widget.ImageView;

import com.sk.ziladelivery.data.model.PostChequeCollectionModel;

import java.util.ArrayList;

public interface ChequeImageListener {
    void onImageClick(ImageView imageView, int position);
    void onButtonClick(ArrayList<PostChequeCollectionModel> list, boolean save);
}
