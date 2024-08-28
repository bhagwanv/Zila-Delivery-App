package com.sk.ziladelivery.listener;

import com.sk.ziladelivery.ui.views.adapter.MyListViewAdapter;

public interface ItemTouchHelperContract {

    void onRowMoved(int fromPosition, int toPosition);
    void onRowSelected(MyListViewAdapter.ViewHolder myViewHolder);
    void onRowClear(MyListViewAdapter.ViewHolder myViewHolder);
}
