package com.sk.ziladelivery.ui.views.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.sk.ziladelivery.R;
import com.sk.ziladelivery.databinding.PaidamtAdapterBinding;

import java.util.ArrayList;

public class PaidAmountAdapter extends RecyclerView.Adapter<PaidAmountAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private Context context;
    ArrayList<String> paidamountList = new ArrayList<>();
    ArrayList<String> paymentFromList = new ArrayList<>();


    public PaidAmountAdapter(Context context, ArrayList<String> paidamountList, ArrayList<String> paymentFromList) {
        this.context = context;
        this.paidamountList = paidamountList;
        this.paymentFromList = paymentFromList;
    }


    @NonNull
    @Override
    public PaidAmountAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        PaidamtAdapterBinding mBinding = DataBindingUtil.inflate(layoutInflater, R.layout.paidamt_adapter, viewGroup, false);
        return new ViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull PaidAmountAdapter.ViewHolder holder, int position) {
        holder.mBinding.paidAmtName.setText(paymentFromList.get(position));
        holder.mBinding.paidAmnt.setText(paidamountList.get(position));
    }

    @Override
    public int getItemCount() {
        return paidamountList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        PaidamtAdapterBinding mBinding;
        RecyclerView orderListRV;

        public ViewHolder(PaidamtAdapterBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
        }

    }
}
