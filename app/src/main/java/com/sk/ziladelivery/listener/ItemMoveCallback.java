package com.sk.ziladelivery.listener;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.sk.ziladelivery.ui.views.adapter.MyListViewAdapter;
import com.sk.ziladelivery.ui.views.adapter.RearrangeAdapter;

public class ItemMoveCallback extends ItemTouchHelper.Callback {

    private final RearrangeAdapter mAdapter;

    public ItemMoveCallback(RearrangeAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        return makeMovementFlags(dragFlags, 0);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {
        mAdapter.onRowMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

    }


    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder,
                                  int actionState) {


        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder instanceof MyListViewAdapter.ViewHolder) {
                MyListViewAdapter.ViewHolder myViewHolder=
                        ( MyListViewAdapter.ViewHolder) viewHolder;
                mAdapter.onRowSelected(myViewHolder);
            }

        }

        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(RecyclerView recyclerView,
                          RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);

        if (viewHolder instanceof MyListViewAdapter.ViewHolder) {
            MyListViewAdapter.ViewHolder myViewHolder=
                    (MyListViewAdapter.ViewHolder) viewHolder;
            mAdapter.onRowClear(myViewHolder);
        }
    }


}
