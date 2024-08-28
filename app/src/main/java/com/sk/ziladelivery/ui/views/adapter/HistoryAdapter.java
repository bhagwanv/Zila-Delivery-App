package com.sk.ziladelivery.ui.views.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.sk.ziladelivery.R;
import com.sk.ziladelivery.databinding.HistoryAdapterBinding;
import com.sk.ziladelivery.data.model.Order;
import com.sk.ziladelivery.utilities.DateUtils;
import com.sk.ziladelivery.ui.views.viewmodels.HistoryAdapterViewModel;
import com.sk.ziladelivery.ui.views.main.HistoryDetailActivity;

import java.io.Serializable;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private LayoutInflater layoutInflater;
    private List<Order> itemLists;
    private Context context;

    public HistoryAdapter(Context context, List<Order> itemLists) {
        this.context = context;
        this.itemLists = itemLists;
    }

    @NonNull
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        HistoryAdapterBinding mBinding = DataBindingUtil.inflate(layoutInflater, R.layout.history_adapter, viewGroup, false);
        return new ViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        try {
            Order model = itemLists.get(i);
            HistoryAdapterViewModel historyAdapterViewModel = new HistoryAdapterViewModel();
            historyAdapterViewModel.setOrderedDate(DateUtils.getDateFormat(String.valueOf(model.getOrderedDate())));
            historyAdapterViewModel.setOrderId(String.valueOf(model.getOrderId()));
            historyAdapterViewModel.setShippingAddress(model.getShippingAddress());
            historyAdapterViewModel.setShopName(model.getShopName());
            historyAdapterViewModel.setStatus(model.getStatus());
            viewHolder.bind(historyAdapterViewModel);
//            if (model.getColorCode() != null && !model.getColorCode().isEmpty()) {
//                viewHolder.mBinding.tvName.setTextColor(Color.parseColor("" + model.getColorCode()));
//            } else {
//                viewHolder.mBinding.tvName.setTextColor(Color.BLACK);
//            }

            viewHolder.cardView.setOnClickListener(v -> {
                Intent intent = new Intent(context, HistoryDetailActivity.class);
                Bundle args = new Bundle();
                args.putSerializable("Arraylist", (Serializable) model.getOrderDetails());
                intent.putExtra("orderid", model.getOrderId());
                intent.putExtra("shopname", model.getShopName());
                intent.putExtra("checkamount", model.getCheckAmount());
                intent.putExtra("cashamount", model.getCashAmount());
                intent.putExtra("recivedAmount", model.getRecivedAmount());
                intent.putExtra("status", model.getStatus());
                intent.putExtra("comments", model.getComments());
                intent.putExtra("grossamnt", model.getGrossAmount());
                if (model.getPaymentThrough() != null) {
                    if (model.getPaymentThrough().equalsIgnoreCase("Mpos")) {
                        intent.putExtra("electronicPaymentNo", model.getElectronicPaymentNo());
                        intent.putExtra("electronicAmount", model.getElectronicAmount());
                    } else {
                        intent.putExtra("electronicPaymentNo", "0");
                        intent.putExtra("electronicAmount", 0);
                    }
                } else {
                    intent.putExtra("electronicPaymentNo", "0");
                    intent.putExtra("electronicAmount", 0);
                }
                intent.putExtra("BUNDLE", args);
                context.startActivity(intent);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return itemLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        HistoryAdapterBinding mBinding;
        TextView orderid, shopname, date, address, status;
        CardView cardView;

        public ViewHolder(HistoryAdapterBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
            orderid = mBinding.orderid;
            shopname = mBinding.shopname;
            date = mBinding.date;
            address = mBinding.address;
            status = mBinding.status;
            cardView = mBinding.llDetail;
        }

        public void bind(HistoryAdapterViewModel item) {
            mBinding.setHistoryDetailSetViewModel(item);
            mBinding.executePendingBindings();
        }
    }
}
