package com.sk.ziladelivery.ui.views.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.sk.ziladelivery.R;
import com.sk.ziladelivery.databinding.CancelReasonBinding;
import com.sk.ziladelivery.databinding.MyTripOrderAdapterBinding;

public class RejectOrderReasonAdapter extends RecyclerView.Adapter<RejectOrderReasonAdapter.ViewHolder> {
    private LayoutInflater layoutInflater;
    private Activity context;
    @NonNull
    @Override
    public RejectOrderReasonAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        return new ViewHolder(DataBindingUtil.inflate(layoutInflater, R.layout.cancel_reason, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RejectOrderReasonAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        CancelReasonBinding mBinding;

        public ViewHolder(@NonNull CancelReasonBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
        }
    }
}
