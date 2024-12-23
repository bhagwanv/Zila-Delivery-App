package com.zila.storelastmile.listener;

import android.widget.ImageView;

import com.zila.storelastmile.data.model.PostChequeCollectionModel;

import java.util.ArrayList;

public interface ChequeImageListener {
    void onImageClick(ImageView imageView, int position);
    void onButtonClick(ArrayList<PostChequeCollectionModel> list, boolean save);
}
