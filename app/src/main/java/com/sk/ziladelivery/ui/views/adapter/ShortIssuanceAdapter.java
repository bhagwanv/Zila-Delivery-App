package com.sk.ziladelivery.ui.views.adapter;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.sk.ziladelivery.R;
import com.sk.ziladelivery.databinding.ItemShortItemBinding;
import com.sk.ziladelivery.data.model.ShortIssuanceModel;
import com.sk.ziladelivery.ui.views.viewmodels.ShortViewModel;

import java.util.ArrayList;

public class ShortIssuanceAdapter extends RecyclerView.Adapter<ShortIssuanceAdapter.ViewHolder>  {

    private Context context;
    private ArrayList<ShortIssuanceModel> shortIssuanceArrayList;
    private LayoutInflater layoutInflater;


    public ShortIssuanceAdapter(Context context, ArrayList<ShortIssuanceModel> shortIssuanceArrayList) {
        this.context = context;
        this.shortIssuanceArrayList = shortIssuanceArrayList;
        layoutInflater = (LayoutInflater.from(context));
    }



    @NonNull
    @Override
    public ShortIssuanceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        ItemShortItemBinding mBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_short_item, viewGroup, false);
        return new ShortIssuanceAdapter.ViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ShortIssuanceAdapter.ViewHolder viewHolder, int i) {
        ShortIssuanceModel  shortIssuanceModel = shortIssuanceArrayList.get(i);
        ShortViewModel shortViewModel= new ShortViewModel();
        shortViewModel.setItemId(String.valueOf(i+1));
        shortViewModel.setItemName(shortIssuanceModel.getItemname()+"( Order ID: "+shortIssuanceModel.getOrderId()+")");
        shortViewModel.setOrderId(String.valueOf(shortIssuanceModel.getOrderId()));
        shortViewModel.setShortQty(String.valueOf(shortIssuanceModel.getDamageStockQty()+shortIssuanceModel.getNotinStockQty()));
        viewHolder.bind(shortViewModel);


    }


    @Override
    public int getItemCount() {

        if (shortIssuanceArrayList != null)
            return shortIssuanceArrayList.size();
        else
            return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemShortItemBinding mBinding;

        public ViewHolder(ItemShortItemBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
        }

        public void bind(ShortViewModel item) {
            mBinding.setShortViewModel(item);
            mBinding.executePendingBindings();
        }
    }
}
