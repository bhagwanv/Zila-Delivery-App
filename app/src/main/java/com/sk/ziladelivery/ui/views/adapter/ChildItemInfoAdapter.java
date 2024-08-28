package com.sk.ziladelivery.ui.views.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.sk.ziladelivery.R;
import com.sk.ziladelivery.databinding.ChildItemInfoBinding;

import com.sk.ziladelivery.data.model.TripOrderStatusUpdateModel;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ChildItemInfoAdapter extends RecyclerView.Adapter<ChildItemInfoAdapter.ViewHolder> {
    private LayoutInflater layoutInflater;
    private Activity context;
    ArrayList<TripOrderStatusUpdateModel.ItemOrderDetailModel> itemOrderDetailList;

    public ChildItemInfoAdapter(Activity context, ArrayList<TripOrderStatusUpdateModel.ItemOrderDetailModel> itemOrderDetailList) {
        this.context = context;
        this.itemOrderDetailList = itemOrderDetailList;
    }

    @NonNull
    @Override
    public ChildItemInfoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        return new ChildItemInfoAdapter.ViewHolder(DataBindingUtil.inflate(layoutInflater, R.layout.child_item_info, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DecimalFormat df = new DecimalFormat("#.#");
        TripOrderStatusUpdateModel.ItemOrderDetailModel customerorderinfoEntity = itemOrderDetailList.get(position);
       holder.mBinding.itItemName.setText(customerorderinfoEntity.getItemname());
       holder.mBinding.tvQTY.setText(String.valueOf(customerorderinfoEntity.getQty()));
     // holder.mBinding.TvAmount.setText(String.valueOf(customerorderinfoEntity.getTotalAmt()));
        holder.mBinding.TvAmount.setText(df.format(customerorderinfoEntity.getTotalAmt()));
    }

    @Override
    public int getItemCount() {
        return itemOrderDetailList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ChildItemInfoBinding mBinding;

        public ViewHolder(@NonNull ChildItemInfoBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
        }
    }

}
