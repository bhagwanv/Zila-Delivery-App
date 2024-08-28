package com.sk.ziladelivery.ui.views.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.sk.ziladelivery.R;
import com.sk.ziladelivery.data.api.RestClient;
import com.sk.ziladelivery.databinding.MultiMposadapterBinding;
import com.sk.ziladelivery.listener.MultiMposListener;
import com.sk.ziladelivery.data.model.DeliveryPayments;
import com.sk.ziladelivery.utilities.Utils;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MultiMposAdapter extends RecyclerView.Adapter<MultiMposAdapter.ViewHolder> {
    private Activity activity;
    private ArrayList<DeliveryPayments> payment;
    private MultiMposListener listener;
    private int orderId,deliveryIssuanceId,customerID;



    public MultiMposAdapter(Activity activity, ArrayList<DeliveryPayments> mpospayment, int orderId, int deliveryIssuanceId ,int custID, MultiMposListener listener) {
        this.activity = activity;
        this.payment = mpospayment;
        this.listener = listener;
        this.orderId = orderId;
        this.customerID = custID;
        this.deliveryIssuanceId = deliveryIssuanceId;
    }

    @NonNull
    @Override
    public MultiMposAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MultiMposAdapter.ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.multi_mposadapter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MultiMposAdapter.ViewHolder holder, int position) {
        holder.mBinding.UPIReference.setText(payment.get(position).getTransId());
        holder.mBinding.UPIAmount.setText("" +  new DecimalFormat("##.##").format(payment.get(position).getAmount()));
        payment.get(position).setPaymentFrom("mPos");
        payment.get(position).setOrderId(orderId);
        if (payment.get(position).isVeryfied()) {
            holder.mBinding.tvVerify.setText("Verified");
        }
        else{
            holder.mBinding.tvVerify.setText("Verify");
        }
    }

    @Override
    public int getItemCount() {
        return payment.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        MultiMposadapterBinding mBinding;

        public ViewHolder(@NonNull MultiMposadapterBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;

            mBinding.ivCross.setOnClickListener(v -> {
                try {
                payment.get(getAdapterPosition()).setAmount(0);
                listener.onMposAmountChange(payment);
                payment.remove(getAdapterPosition());
               // notifyItemRemoved(getAdapterPosition());
                    notifyDataSetChanged();
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            });
            mBinding.UPIReference.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() > 0) {
                        if(!s.toString().equalsIgnoreCase(payment.get(getAdapterPosition()).getTransId())){
                            payment.get(getAdapterPosition()).setVeryfied(false);
                            mBinding. tvVerify.setText("Verify");
                        }
                        payment.get(getAdapterPosition()).setTransId(s.toString());
                    } else {
                        payment.get(getAdapterPosition()).setTransId("");
                    }

                }
            });

            mBinding.tvVerify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(payment.get(getAdapterPosition()).getTransId().equalsIgnoreCase("")){
                        Utils.setToast(activity,"Please Enter Reference Number First");
                    }
                    else if (payment.get(getAdapterPosition()).getTransId().length()>7) {
                        VerifyRefNo(deliveryIssuanceId, payment.get(getAdapterPosition()).getTransId(), getAdapterPosition(), mBinding.UPIReference,customerID,mBinding.tvVerify);
                    }
                    else {
                        Utils.setToast(activity,"Please Enter Valid Reference Number");
                    }

                }
            });

            mBinding.UPIAmount.setOnFocusChangeListener((v, hasFocus) -> {
                if (mBinding.UPIAmount.getText().toString().startsWith("0")) {
                    mBinding.UPIAmount.setText("");
                }else{

                }
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(mBinding.UPIAmount, InputMethodManager.SHOW_IMPLICIT);
            });
            mBinding.UPIAmount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {


                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() > 0) {
                        payment.get(getAdapterPosition()).setAmount(Double.parseDouble(s.toString()));
                    } else {
                        payment.get(getAdapterPosition()).setAmount(0);
                    }
                    listener.onMposAmountChange(payment);
                }
            });
        }
    }

    public ArrayList<DeliveryPayments> getListFromAdapter() {
        return payment;
    }

    private void VerifyRefNo(int AssginID, String refno, int adapterPosition, AppCompatEditText UPIReference, int orderId, TextView tvVerify) {
        RestClient.getInstance().getService().checkRefNo(AssginID, refno,orderId)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> Utils.showProgressDialog(activity))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NotNull Boolean response) {
                        Utils.hideProgressDialog();
                        if (response){
                            UPIReference.setText("");
                            payment.get(adapterPosition).setVeryfied(false);
                            tvVerify.setText("Verify");
                            Utils.setToast(activity,"This Reference Number is Already Used");
                        }
                        else{
                            payment.get(adapterPosition).setTransId(refno);
                            payment.get(adapterPosition).setVeryfied(true);
                            tvVerify.setText("Verified");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Utils.hideProgressDialog();

                        Log.d("TAG", "Error:" + e.toString());

                    }

                    @Override
                    public void onComplete() {
                        Utils.hideProgressDialog();

                    }
                });
    }

}
