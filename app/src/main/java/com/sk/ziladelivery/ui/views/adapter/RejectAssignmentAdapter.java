package com.sk.ziladelivery.ui.views.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.sk.ziladelivery.R;
import com.sk.ziladelivery.databinding.MultipleassginmentAdapterBinding;
import com.sk.ziladelivery.listener.MultipleAssignmentInterface;
import com.sk.ziladelivery.data.model.AcceptedAssginmentListModel;
import com.sk.ziladelivery.utilities.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.TimeZone;

public class RejectAssignmentAdapter  extends RecyclerView.Adapter<RejectAssignmentAdapter.ViewHolder> {
    private LayoutInflater layoutInflater;
    Context context;
    private ArrayList<AcceptedAssginmentListModel> assignmentAcceptPending;
    MultipleassginmentAdapterBinding mBinding;
    int assignID;
    private MultipleAssignmentInterface multipleAssignmentInterface;

    public RejectAssignmentAdapter(Context context, ArrayList<AcceptedAssginmentListModel> assignmentAcceptPendinge,MultipleAssignmentInterface multipleAssignmentInterface) {
        this.assignmentAcceptPending = assignmentAcceptPending;
        this.context = context;
        this.multipleAssignmentInterface = multipleAssignmentInterface;

    }

    public void setItemListCategory(ArrayList<AcceptedAssginmentListModel> assignmentAcceptPending,MultipleAssignmentInterface multipleAssignmentInterface) {
        this.assignmentAcceptPending = assignmentAcceptPending;
        this.multipleAssignmentInterface = multipleAssignmentInterface;

        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public RejectAssignmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
         mBinding = DataBindingUtil.inflate(layoutInflater, R.layout.multipleassginment_adapter, parent, false);
        return new RejectAssignmentAdapter.ViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RejectAssignmentAdapter.ViewHolder holder, int position) {
        AcceptedAssginmentListModel assginmentListModel = assignmentAcceptPending.get(position);
        holder.assignId.setText(context.getString(R.string.assignment_id) + assginmentListModel.getDeliveryIssuanceId());
        holder.date.setText(context.getString(R.string.assignment_date) + Utils.getDateFormat(assginmentListModel.getAssignmentDate()));
        long currMillis = System.currentTimeMillis();
        SimpleDateFormat sdf1 = new SimpleDateFormat(Utils.myFormat, Locale.getDefault());
        sdf1.setTimeZone(TimeZone.getDefault());
        mBinding.tvTimerLayout.setVisibility(View.GONE);
        mBinding.llStart.setVisibility(View.GONE);
        mBinding.llOrderList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                multipleAssignmentInterface.Details(assginmentListModel.getDeliveryIssuanceId(), false, position);

            }
        });
    }

    @Override
    public int getItemCount() {
        return assignmentAcceptPending.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        MultipleassginmentAdapterBinding mBinding;
        private TextView tvtimer, assignId, date;

        public ViewHolder(MultipleassginmentAdapterBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
            assignId = mBinding.assignId;
            date = mBinding.date;

        }


    }


}
