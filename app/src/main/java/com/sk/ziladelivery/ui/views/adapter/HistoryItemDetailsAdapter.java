package com.sk.ziladelivery.ui.views.adapter;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sk.ziladelivery.R;
import com.sk.ziladelivery.databinding.HistoryItemdetailBinding;
import com.sk.ziladelivery.data.model.HistoryDataModel;
import com.sk.ziladelivery.data.model.OrderDetail;
import com.sk.ziladelivery.ui.views.viewmodels.ItemDetailsInfo;

import java.util.ArrayList;
import java.util.List;

public class HistoryItemDetailsAdapter extends RecyclerView.Adapter<HistoryItemDetailsAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    HistoryItemdetailBinding mBinding;
    List<OrderDetail> itemLists = new ArrayList<>();
    HistoryDataModel historyDataModel;
    QunatityInterface qunatity;
    // Collection<HistoryDataModel> itemLists ;
    Context context;
    int quantityValue=0;

List<Integer>qty=new ArrayList<>();
    public HistoryItemDetailsAdapter(Context context, List<OrderDetail> itemLists,QunatityInterface qunatity) {
        this.context = context;
        this.itemLists = itemLists;
        this.qunatity = qunatity;
    }

    @NonNull
    @Override
    public HistoryItemDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        mBinding = DataBindingUtil.inflate(layoutInflater, R.layout.history_itemdetail, viewGroup, false);
        return new ViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryItemDetailsAdapter.ViewHolder viewHolder, int i) {
        try {
            OrderDetail orderDetail = itemLists.get(i);
            ItemDetailsInfo info = new ItemDetailsInfo();
            info.setItemName(orderDetail.getItemname().toLowerCase());
            info.setQty(String.valueOf(orderDetail.getQty()));
            info.setPrice(String.valueOf(orderDetail.getUnitPrice()));
            info.setSrNo(String.valueOf(i + 1));
            quantityValue=quantityValue+orderDetail.getQty();
            qunatity.totalQunatity(quantityValue);
            viewHolder.bind(info);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return itemLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        HistoryItemdetailBinding mBinding;
        TextView orderid, shopname, date, address, status;
        CardView cardView;

        public ViewHolder(HistoryItemdetailBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;

        }

        public void bind(ItemDetailsInfo item) {
            mBinding.setItemDetailsInfo(item);
            mBinding.executePendingBindings();
        }
    }
    public interface QunatityInterface{
        public void totalQunatity(int quatity);
    }

}
