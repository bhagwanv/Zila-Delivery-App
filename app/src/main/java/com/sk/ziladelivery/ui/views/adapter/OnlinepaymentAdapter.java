package com.sk.ziladelivery.ui.views.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.sk.ziladelivery.R;
import com.sk.ziladelivery.data.model.PostOnlineCollectionModel;
import com.sk.ziladelivery.databinding.OnlinePayAdapterBinding;
import com.sk.ziladelivery.listener.OnlinePaymentListener;
import com.sk.ziladelivery.listener.RTGSEditListener;

import java.util.ArrayList;

public class OnlinepaymentAdapter extends RecyclerView.Adapter<OnlinepaymentAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater layoutInflater;
    ArrayList<PostOnlineCollectionModel> onlineModellist;
    ArrayList<String> datelist;
    boolean save = true;
    OnlinePaymentListener onlinePaymentListener;
    RTGSEditListener rtgsEditListener;

    public OnlinepaymentAdapter(Context context, ArrayList<PostOnlineCollectionModel> onlineModellist, RTGSEditListener listener) {
        this.context = context;
        this.onlineModellist = onlineModellist;
        this.rtgsEditListener = listener;
    }

    @NonNull
    @Override
    public OnlinepaymentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }

        OnlinePayAdapterBinding mBinding = DataBindingUtil.inflate(layoutInflater, R.layout.online_pay_adapter, viewGroup, false);
        return new OnlinepaymentAdapter.ViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull OnlinepaymentAdapter.ViewHolder vh, int i) {
        PostOnlineCollectionModel model = onlineModellist.get(i);
        vh.orderId.setText("OrderID: " + model.getOrderid());
        vh.refNo.setText(model.getPaymentReferenceNO());
        vh.paymentName.setText(model.getPaymentFrom());
        vh.paidAmount.setText(String.format("₹ %s", String.valueOf(model.getPaymentGetwayAmt())));
        if (model.isEditRTGS()) {
            vh.mBinding.ivRtgsupdate.setVisibility(View.VISIBLE);
        } else {
            vh.mBinding.ivRtgsupdate.setVisibility(View.GONE);
        }
        vh.mBinding.ivRtgsupdate.setOnClickListener(view -> rtgsEditListener.isEditRTGS(model.getPaymentReferenceNO(), model.getOrderid(), model.getDeliveryIssuanceId(), model.paymentResponseRetailerAppId));

        vh.edit.setOnClickListener(v -> {
            save = false;
            vh.edit.setVisibility(View.GONE);
            vh.refNo.setVisibility(View.GONE);
            vh.etLayout.setVisibility(View.VISIBLE);
            vh.save.setVisibility(View.VISIBLE);
            vh.etrefNO.setText(model.getPaymentReferenceNO());

        });
        vh.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vh.etrefNO.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(context, R.string.enter_ref, Toast.LENGTH_SHORT).show();

                } else {
                    vh.edit.setVisibility(View.VISIBLE);
                    vh.refNo.setVisibility(View.VISIBLE);
                    vh.etLayout.setVisibility(View.GONE);
                    vh.save.setVisibility(View.GONE);
                    vh.refNo.setText(vh.etrefNO.getText().toString());
                    save = true;
                    onlineModellist.get(vh.getAdapterPosition()).setPaymentReferenceNO(vh.etrefNO.getText().toString());
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return onlineModellist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        OnlinePayAdapterBinding mBinding;
        TextView orderId, paidAmount, deliveryDate, refNo, paymentName;
        Button edit, save;
        LinearLayout etLayout;
        EditText etrefNO;

        public ViewHolder(OnlinePayAdapterBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
            orderId = mBinding.tvOrderId;
            paidAmount = mBinding.paidAmnt;
            paymentName = mBinding.paymentName;
            refNo = mBinding.tvRefNo;
            edit = mBinding.btnEdit;
            save = mBinding.btnSave;
            etLayout = mBinding.etRefNoLayout;
            etrefNO = mBinding.etRefNo;

        }
    }


    public ArrayList<PostOnlineCollectionModel> getList() {
        return onlineModellist.size() > 0 ? onlineModellist : null;
    }
}
