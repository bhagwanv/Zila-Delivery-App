package com.sk.ziladelivery.ui.views.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.sk.ziladelivery.R;
import com.sk.ziladelivery.databinding.ItemEbayAssinmentBinding;
import com.sk.ziladelivery.data.model.AssignmentEwayBillModel;

import java.util.ArrayList;

public class AssinmentEbayBillAdapter extends RecyclerView.Adapter<AssinmentEbayBillAdapter.ViewHolder> {
    private LayoutInflater layoutInflater;
    private Activity context;
    ArrayList<AssignmentEwayBillModel> ewayBillList;

    public AssinmentEbayBillAdapter(Activity context, ArrayList<AssignmentEwayBillModel> ewayBillList) {
        this.context = context;
        this.ewayBillList = ewayBillList;
    }

    @NonNull
    @Override
    public AssinmentEbayBillAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        return new AssinmentEbayBillAdapter.ViewHolder(DataBindingUtil.inflate(layoutInflater, R.layout.item_ebay_assinment, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AssignmentEwayBillModel ewayBillModel= ewayBillList.get(position);
        holder.mBinding.itOrderId.setText(""+ewayBillModel.getOrderId());
        holder.mBinding.TvReson.setText(ewayBillModel.getReson());
    }

    @Override
    public int getItemCount() {
        return ewayBillList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemEbayAssinmentBinding mBinding;

        public ViewHolder(@NonNull ItemEbayAssinmentBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
        }
    }

}
