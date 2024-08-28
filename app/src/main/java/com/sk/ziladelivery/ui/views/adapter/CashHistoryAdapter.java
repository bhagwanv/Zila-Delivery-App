package com.sk.ziladelivery.ui.views.adapter;

import android.content.Context;

import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sk.ziladelivery.R;
import com.sk.ziladelivery.databinding.CashHistoryBinding;
import com.sk.ziladelivery.listener.HistoryEditListener;
import com.sk.ziladelivery.data.model.CashSummaryDetail;
import com.sk.ziladelivery.utilities.TextUtils;

import java.util.ArrayList;

public class CashHistoryAdapter extends RecyclerView.Adapter<CashHistoryAdapter.ViewHolder> {
    private Context context;
    private LayoutInflater layoutInflater;
    CashHistoryBinding mBinding;
    ArrayList<CashSummaryDetail> currencyCollectionSummaryDcs;
    HistoryEditListener historyEditListener;

    public CashHistoryAdapter(Context context, ArrayList<CashSummaryDetail> currencyCollectionSummaryDcs, HistoryEditListener historyEditListener) {

        this.currencyCollectionSummaryDcs = currencyCollectionSummaryDcs;
        this.context = context;
        this.historyEditListener = historyEditListener;
    }


    public void setHistoryData() {

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CashHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        mBinding = DataBindingUtil.inflate(layoutInflater, R.layout.cash_history, viewGroup, false);
        return new ViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CashHistoryAdapter.ViewHolder viewHolder, int i) {
        try {
            CashSummaryDetail cashSummaryDetail = currencyCollectionSummaryDcs.get(i);
            mBinding.assignId.setText(String.valueOf(cashSummaryDetail.getDeliveryissueid()));
            mBinding.assignmentAmount.setText(String.valueOf(cashSummaryDetail.getTotalDeliveryissueAmt()));
            mBinding.collectedAmnt.setText(String.valueOf(cashSummaryDetail.getTotalCollectionAmt()));
            mBinding.status.setText(cashSummaryDetail.getStatus());
            if (TextUtils.isNullOrEmpty(cashSummaryDetail.getDeclineNote())) {
                mBinding.noteLayout.setVisibility(View.GONE);
            } else {
                mBinding.noteLayout.setVisibility(View.VISIBLE);
                mBinding.note.setText(cashSummaryDetail.getDeclineNote());

            }
            if (cashSummaryDetail.getStatus().equalsIgnoreCase("Decline")) {
                mBinding.status.setTextColor(context.getResources().getColor(R.color.red));
            } else if (cashSummaryDetail.getStatus().equalsIgnoreCase("Inprogress")) {
                mBinding.status.setTextColor(context.getResources().getColor(R.color.black));
                mBinding.btnEdit.setClickable(false);
                mBinding.btnEdit.setFocusableInTouchMode(false);
                mBinding.btnEdit.setBackground(context.getResources().getDrawable(R.drawable.ic_circle_grey_shape));
            } else if (cashSummaryDetail.getStatus().equalsIgnoreCase("Settlement")) {
                mBinding.status.setTextColor(context.getResources().getColor(R.color.black));
                mBinding.btnEdit.setClickable(false);
                mBinding.btnEdit.setFocusableInTouchMode(false);
                mBinding.btnEdit.setBackground(context.getResources().getDrawable(R.drawable.ic_circle_grey_shape));
            }
            mBinding.btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cashSummaryDetail.getStatus().equalsIgnoreCase("Decline")) {
                        historyEditListener.Editclicked(cashSummaryDetail.getCurrencyCollectionId());
                    } else if (cashSummaryDetail.getStatus().equalsIgnoreCase("Inprogress")) {

                    } else if (cashSummaryDetail.getStatus().equalsIgnoreCase("Settlement")) {


                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return currencyCollectionSummaryDcs.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        CashHistoryBinding mBinding;


        public ViewHolder(CashHistoryBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;

        }


    }
}
