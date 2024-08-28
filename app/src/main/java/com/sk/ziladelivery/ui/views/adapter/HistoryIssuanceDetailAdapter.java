package com.sk.ziladelivery.ui.views.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sk.ziladelivery.R;
import com.sk.ziladelivery.databinding.HistoryissuanceAdapterBinding;
import com.sk.ziladelivery.data.model.OrderHistory;
import com.sk.ziladelivery.utilities.DateUtils;
import com.sk.ziladelivery.ui.views.viewmodels.HistoryAdapterViewModel;

import java.util.ArrayList;

public class HistoryIssuanceDetailAdapter extends RecyclerView.Adapter<HistoryIssuanceDetailAdapter.ViewHolder> {
    private LayoutInflater layoutInflater;
    private ArrayList<OrderHistory> itemLists;
    private Context context;

    public HistoryIssuanceDetailAdapter(Context context, ArrayList<OrderHistory> itemLists) {
        this.context = context;
        this.itemLists = itemLists;
    }

    @NonNull
    @Override
    public HistoryIssuanceDetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }

        HistoryissuanceAdapterBinding mBinding = DataBindingUtil.inflate(layoutInflater, R.layout.historyissuance_adapter, viewGroup, false);
        return new ViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        try {
            OrderHistory model = itemLists.get(i);
            HistoryAdapterViewModel historyAdapterViewModel = new HistoryAdapterViewModel();
            historyAdapterViewModel.setDeliveryIssuanceId(context.getString(R.string.AssignmentID) + ":" + model.getDeliveryIssuance().getDeliveryIssuanceId());
            historyAdapterViewModel.setCanceled(context.getString(R.string.order_Canceled) + model.getCanceled());
            historyAdapterViewModel.setDelivered(context.getString(R.string.order_Delivered) + model.getDelivered());
            historyAdapterViewModel.setIssuanceStatus(context.getString(R.string.Assignment_Status) + model.getDeliveryIssuance().getStatus());
            //  historyAdapterViewModel.setTotalAmount("Issuance Date :" + model.getOrders().get(i).get());
            historyAdapterViewModel.setDate(context.getString(R.string.Assignment_Date) + DateUtils.getDateFormat(model.getDeliveryIssuance().getCreatedDate()));
            //  historyAdapterViewModel.setOrderedDate((model.getOrders().get(i).getOrderedDate());
            viewHolder.bind(historyAdapterViewModel);

            viewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            viewHolder.recyclerView.setHasFixedSize(true);
            HistoryAdapter adapter = new HistoryAdapter(context, model.getOrders());
            viewHolder.recyclerView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return itemLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        HistoryissuanceAdapterBinding mBinding;
        RecyclerView recyclerView;

        public ViewHolder(HistoryissuanceAdapterBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
            recyclerView = mBinding.rvHistory;
        }

        public void bind(HistoryAdapterViewModel item) {
            mBinding.setHistoryDetailSetViewModel(item);
            mBinding.executePendingBindings();
        }
    }
}
