package com.sk.ziladelivery.listener;

import androidx.recyclerview.widget.RecyclerView;

import com.sk.ziladelivery.data.model.RearrangModel;

import java.util.ArrayList;

public interface StartDragListener {

    void requestDrag(RecyclerView.ViewHolder viewHolder, ArrayList<RearrangModel> rearrangModel);
}
