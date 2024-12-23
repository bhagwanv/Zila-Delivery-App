package com.zila.storelastmile.ui.views.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.zila.storelastmile.R;
import com.zila.storelastmile.data.model.TripOrderStatusUpdateModel;
import com.zila.storelastmile.databinding.MyTripOrderAdapterBinding;

import java.util.ArrayList;

public class TripCompleteAdapter extends RecyclerView.Adapter<TripCompleteAdapter.ViewHolder> {
    private LayoutInflater layoutInflater;
    private Activity context;
    int noitem;
    ArrayList<TripOrderStatusUpdateModel.CustomerorderinfoEntity> customerListDcs;

    public TripCompleteAdapter(Activity context, ArrayList<TripOrderStatusUpdateModel.CustomerorderinfoEntity> customerListDcs) {
        this.context = context;
        this.customerListDcs = customerListDcs;

    }

    @NonNull
    @Override
    public TripCompleteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        return new TripCompleteAdapter.ViewHolder(DataBindingUtil.inflate(layoutInflater, R.layout.my_trip_order_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TripCompleteAdapter.ViewHolder holder, int i) {
        holder.mBinding.tvOrderid.setText(customerListDcs.get(i).getOrderid() + "");
        holder.mBinding.tvAmount.setText("₹ " + customerListDcs.get(i).getGrossamount());
        holder.mBinding.tvStatus.setText(customerListDcs.get(i).getStatus());


    }

    @Override
    public int getItemCount() {
        return customerListDcs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        MyTripOrderAdapterBinding mBinding;

        public ViewHolder(@NonNull MyTripOrderAdapterBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
        }
    }
}