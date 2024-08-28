package com.sk.ziladelivery.ui.views.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.sk.ziladelivery.R;
import com.sk.ziladelivery.databinding.CommentPopupBinding;
import com.sk.ziladelivery.databinding.CustomarrayAdapterBinding;
import com.sk.ziladelivery.listener.AssginmentSettleViewDetailInterface;
import com.sk.ziladelivery.data.model.AssginmentSettleResponseModel;
import com.sk.ziladelivery.utilities.Utils;

import java.util.ArrayList;

public class AssginmentSettleDetailAdapter extends RecyclerView.Adapter<AssginmentSettleDetailAdapter.ViewHolder> {
    Context context;
    ArrayList<AssginmentSettleResponseModel.DBoyAssignmentDeposits> model;
    AssginmentSettleViewDetailInterface listener;

    public AssginmentSettleDetailAdapter(Context context, ArrayList<AssginmentSettleResponseModel.DBoyAssignmentDeposits> model, AssginmentSettleViewDetailInterface listener) {
        this.context = context;
        this.model = model;
        this.listener = listener;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                R.layout.customarray_adapter, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mBinding.assignId.setText(model.get(position).getDeliveryissueid() + "");
        holder.mBinding.llComment.setOnClickListener(v -> CommentPopup(holder.mBinding.tvComment, model.get(position)));

    }

    private void CommentPopup(TextView tvComment, AssginmentSettleResponseModel.DBoyAssignmentDeposits dBoyAssignmentDeposits) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        CommentPopupBinding rejectResonPopupBinding = DataBindingUtil.inflate(inflater, R.layout.comment_popup, null, false);
        //  final View mView = inflater.inflate(R.layout.reject_reson_popup, null);
        Dialog customDialog = new Dialog(context, R.style.CustomDialog);
        customDialog.setContentView(rejectResonPopupBinding.getRoot());
        Button submit = rejectResonPopupBinding.submit;
        ImageView dissmiss = rejectResonPopupBinding.dissmiss;
        if (dBoyAssignmentDeposits.getComment() != null) {
            rejectResonPopupBinding.etComment.setText(dBoyAssignmentDeposits.getComment());
        }
        submit.setOnClickListener(v -> {
            String sReason = String.valueOf(rejectResonPopupBinding.etComment.getText());
            /***
             * Accept API call
             * **/
            if (sReason.isEmpty()) {
                Utils.setToast(context, context.getResources().getString(R.string.plz_enter_comment));
            } else {
                dBoyAssignmentDeposits.setComment(sReason);
                tvComment.setText(sReason);
                listener.saveCommentClick(model);

                customDialog.dismiss();
            }

        });
        dissmiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.dismiss();
            }
        });
        customDialog.show();
    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CustomarrayAdapterBinding mBinding;

        public ViewHolder(CustomarrayAdapterBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
        }


    }
}
