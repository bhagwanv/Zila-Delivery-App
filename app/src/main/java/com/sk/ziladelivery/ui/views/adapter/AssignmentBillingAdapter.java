package com.sk.ziladelivery.ui.views.adapter;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sk.ziladelivery.R;
import com.sk.ziladelivery.databinding.ItemAssignmentBillingBinding;
import com.sk.ziladelivery.data.model.OrderModel;
import com.sk.ziladelivery.utilities.DateUtils;
import com.sk.ziladelivery.ui.views.viewmodels.OrderAssignmentViewModel;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class AssignmentBillingAdapter extends RecyclerView.Adapter<AssignmentBillingAdapter.ViewHolder> {

    private Context context;
    private ArrayList<OrderModel> orderDelMastAssignmentsAL;
    private LayoutInflater layoutInflater;

    public AssignmentBillingAdapter(Context context, ArrayList<OrderModel> orderDelMastAssignmentsAL) {
        this.context = context;
        this.orderDelMastAssignmentsAL = orderDelMastAssignmentsAL;
    }


    @NonNull
    @Override
    public AssignmentBillingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }

        ItemAssignmentBillingBinding mBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_assignment_billing, viewGroup, false);
        return new ViewHolder(mBinding);

    }

    @Override
    public void onBindViewHolder(@NonNull AssignmentBillingAdapter.ViewHolder viewHolder, int i) {
        OrderModel orderDelMastAssignment = orderDelMastAssignmentsAL.get(i);
        OrderAssignmentViewModel orderAssignmentViewModel = new OrderAssignmentViewModel();
        orderAssignmentViewModel.setOrderId("Order Id :" + String.valueOf(orderDelMastAssignment.getOrderId()));
        orderAssignmentViewModel.setAddress(orderDelMastAssignment.getShippingAddress());
        orderAssignmentViewModel.setShopName(orderDelMastAssignment.getShopName());
        orderAssignmentViewModel.setCustomerName(orderDelMastAssignment.getCustomerName() + " (" + orderDelMastAssignment.getSkcode() + ")");

        if (orderDelMastAssignment.getCreatedDate() != null && !orderDelMastAssignment.getCreatedDate().isEmpty()) {
            orderAssignmentViewModel.setDate(DateUtils.getDateFormat(orderDelMastAssignment.getCreatedDate()));
        }
        orderAssignmentViewModel.setTotalAmount(context.getString(R.string.rs) + " " + new DecimalFormat("##.##").format(orderDelMastAssignment.getGrossAmount()));
        /*if (orderDelMastAssignment.getOrderDetails().size()>0){
            orderAssignmentViewModel.setItemCount("No. Items : " + String.valueOf(orderDelMastAssignment.getOrderDetails().size()));
        }*/
        if (orderDelMastAssignment.getStatus().equalsIgnoreCase("Delivered")) {
            viewHolder.statusTV.setTextColor(Color.parseColor("#38a561"));
            viewHolder.statusTV.setText(orderDelMastAssignment.getStatus());
        }else {
            viewHolder.statusTV.setTextColor(Color.parseColor("#fe4e4e"));
            viewHolder.statusTV.setText(orderDelMastAssignment.getStatus());
        }
        viewHolder.bind(orderAssignmentViewModel);

    }

    @Override
    public int getItemCount() {
        if (orderDelMastAssignmentsAL != null)
            return orderDelMastAssignmentsAL.size();
        else
            return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemAssignmentBillingBinding mBinding;
        private TextView statusTV;

        public ViewHolder(ItemAssignmentBillingBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
            statusTV = mBinding.tvStatus;
        }

        public void bind(OrderAssignmentViewModel item) {
            mBinding.setOrderAssignmentViewModel(item);
            mBinding.executePendingBindings();
        }
    }
}
