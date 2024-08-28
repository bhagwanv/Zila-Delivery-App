package com.sk.ziladelivery.ui.views.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.sk.ziladelivery.R;
import com.sk.ziladelivery.databinding.SettleAssginmentAdapterBinding;
import com.sk.ziladelivery.listener.AssginmentSettleViewDetailInterface;
import com.sk.ziladelivery.data.model.AssginmentSettleResponseModel;
import com.sk.ziladelivery.utilities.MyApplication;
import com.sk.ziladelivery.utilities.SharePrefs;
import com.sk.ziladelivery.utilities.Utils;

import java.util.ArrayList;

public class AssginmentsettleAdapter extends RecyclerView.Adapter<AssginmentsettleAdapter.ViewHolder> {
    Context context;
    ArrayList<AssginmentSettleResponseModel> list;
    private LayoutInflater layoutInflater;
    AssginmentSettleViewDetailInterface listener;

    public AssginmentsettleAdapter(Context context, ArrayList<AssginmentSettleResponseModel> model, AssginmentSettleViewDetailInterface listener) {
        this.context = context;
        this.list = model;
        this.listener = listener;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        SettleAssginmentAdapterBinding mBinding = DataBindingUtil.inflate(layoutInflater, R.layout.settle_assginment_adapter, viewGroup, false);
        return new ViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mBinding.assignId.setText(String.valueOf(list.get(position).getId()));
        holder.mBinding.date.setText(Utils.getDateFormat(list.get(position).getCreatedDate()));
        holder.mBinding.llView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.viewDetailClick(list.get(position).getId());
            }
        });
        holder.mBinding.btViewPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(SharePrefs.getInstance(MyApplication.getInstance()).getString(SharePrefs.BASEURL) + list.get(position).getIsUNSignOffUrl())));

            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        SettleAssginmentAdapterBinding mBinding;

        public ViewHolder(SettleAssginmentAdapterBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
        }
    }

}


