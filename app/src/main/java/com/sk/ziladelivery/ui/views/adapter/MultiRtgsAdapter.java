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
import com.sk.ziladelivery.databinding.MultiRtgsadapterBinding;
import com.sk.ziladelivery.listener.MultiRtgsListener;
import com.sk.ziladelivery.data.model.DeliveryPayments;
import com.sk.ziladelivery.utilities.Utils;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MultiRtgsAdapter extends RecyclerView.Adapter<MultiRtgsAdapter.ViewHolder> {
    private Activity context;
    private ArrayList<DeliveryPayments> payment;
    private MultiRtgsListener listener;
    private int orderId, deliveryIssuanceId,CustomerID;

    public MultiRtgsAdapter(Activity context, ArrayList<DeliveryPayments> rtgsPayment, int orderId, int deliveryIssuanceId, int custid, MultiRtgsListener listener) {
        this.context = context;
        this.payment = rtgsPayment;
        this.deliveryIssuanceId = deliveryIssuanceId;
        this.orderId = orderId;
        this.CustomerID= custid;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MultiRtgsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.multi_rtgsadapter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MultiRtgsAdapter.ViewHolder holder, int position) {
        holder.mBinding.rtgsReference.setText(payment.get(position).getTransId());
        holder.mBinding.rtgsAmount.setText("" +  new DecimalFormat("##.##").format(payment.get(position).getAmount()));
        payment.get(position).setPaymentFrom("RTGS/NEFT");
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
        MultiRtgsadapterBinding mBinding;

        public ViewHolder(@NonNull MultiRtgsadapterBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;

            mBinding.ivCross.setOnClickListener(v -> {
                try {
                    payment.get(getAdapterPosition()).setAmount(0);
                    payment.remove(getAdapterPosition());
                    listener.onRtgsAmountChange(payment);
                   // notifyItemRemoved(getAdapterPosition());
                    notifyDataSetChanged();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }

            });
            mBinding.rtgsReference.addTextChangedListener(new TextWatcher() {
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
          /*  mBinding.rtgsReference.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    if (hasFocus) {
                        payment.get(getAdapterPosition()).setVeryfied(false);
                        mBinding. tvVerify.setText("Verify");
                    }

                }
            });*/
            mBinding.tvVerify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VerifyRefNo(deliveryIssuanceId, payment.get(getAdapterPosition()).getTransId(), getAdapterPosition(), mBinding.rtgsReference, CustomerID, mBinding.tvVerify);

                    if (payment.get(getAdapterPosition()).getTransId().equalsIgnoreCase("")) {
                        Utils.setToast(context, "Please Enter Reference Number First");
                    } else if (payment.get(getAdapterPosition()).getTransId().length() < 8) {
                        Utils.setToast(context, "Please Enter Valid Reference Number");
                    } else {
                        VerifyRefNo(deliveryIssuanceId, payment.get(getAdapterPosition()).getTransId(), getAdapterPosition(), mBinding.rtgsReference, CustomerID, mBinding.tvVerify);

                    }

                }
            });
            mBinding.rtgsAmount.setOnFocusChangeListener((v, hasFocus) -> {
                if (mBinding.rtgsAmount.getText().toString().startsWith("0")) {
                    mBinding.rtgsAmount.setText("");
                }
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(mBinding.rtgsAmount, InputMethodManager.SHOW_IMPLICIT);
            });
            mBinding.rtgsAmount.addTextChangedListener(new TextWatcher() {
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
                    listener.onRtgsAmountChange(payment);
                }
            });
        }
    }

    public ArrayList<DeliveryPayments> getListFromAdapter() {
        return payment;
    }

    private void VerifyRefNo(int assginId, String refno, int adapterPosition, AppCompatEditText rtgsReference, int orderId, TextView tvVerify) {
        RestClient.getInstance().getService().checkRefNo(assginId, refno, orderId)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> Utils.showProgressDialog(context))

                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NotNull Boolean response) {
                        Utils.hideProgressDialog();
                        if (response) {
                            rtgsReference.setText("");
                            payment.get(adapterPosition).setVeryfied(false);
                            tvVerify.setText("Verify");
                            Utils.setToast(context, "This Reference Number is Already Used");
                        } else {
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
