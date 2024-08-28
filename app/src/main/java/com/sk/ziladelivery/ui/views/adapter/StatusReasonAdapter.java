package com.sk.ziladelivery.ui.views.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.sk.ziladelivery.R;
import com.sk.ziladelivery.databinding.MyTripOrderAdapterBinding;
import com.sk.ziladelivery.databinding.RadioButtonAdapterBinding;
import com.sk.ziladelivery.listener.SubmitClick;

import java.util.ArrayList;

public class StatusReasonAdapter extends RecyclerView.Adapter<StatusReasonAdapter.ViewHolder> {
    private LayoutInflater layoutInflater;
    private Activity context;
    ArrayList<String> redispatchCommentList;
    private int lastSelectedPosition = -1;
    SubmitClick submitClick;

    public StatusReasonAdapter(Activity context, ArrayList<String> redispatchCommentList,SubmitClick submitClick) {
        this.context = context;
        this.redispatchCommentList = redispatchCommentList;
        this.submitClick = submitClick;
    }

    @NonNull
    @Override
    public StatusReasonAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        return new StatusReasonAdapter.ViewHolder(DataBindingUtil.inflate(layoutInflater, R.layout.radio_button_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull StatusReasonAdapter.ViewHolder holder, int i) {
        holder.mBinding.rbText.setText(redispatchCommentList.get(i));
        holder.mBinding.rbText.setChecked(lastSelectedPosition == i);
    }

    @Override
    public int getItemCount() {
        return redispatchCommentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RadioButtonAdapterBinding mBinding;

        public ViewHolder(@NonNull RadioButtonAdapterBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
            mBinding.rbText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastSelectedPosition = getAdapterPosition();
                    notifyDataSetChanged();
                    submitClick.submitBtnClicked(mBinding.rbText.getText().toString());
                    //Toast.makeText(StatusReasonAdapter.this.context, mBinding.rbText.getText().toString(),Toast.LENGTH_LONG).show();
                }

            });
        }
    }
}