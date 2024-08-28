package com.sk.ziladelivery.ui.views.adapter;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.sk.ziladelivery.R;
import com.sk.ziladelivery.databinding.BankNamePopupBinding;
import com.sk.ziladelivery.databinding.MultiplechequeAdapterBinding;
import com.sk.ziladelivery.listener.OrderchequeimageListener;
import com.sk.ziladelivery.data.model.DeliveryPayments;
import com.sk.ziladelivery.utilities.MyApplication;
import com.sk.ziladelivery.utilities.SharePrefs;
import com.sk.ziladelivery.utilities.Utils;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MultipleChequeAdapter extends RecyclerView.Adapter<MultipleChequeAdapter.ViewHolder> {
    private Context context;
    private ArrayAdapter adapter;
    private ArrayList<String> BankNameList;
    public ArrayList<DeliveryPayments> paymentList;
    private OrderchequeimageListener listener;
    private int orderId;

    public MultipleChequeAdapter(Context context, ArrayList<String> bankNameList, ArrayList<DeliveryPayments> payment, int orderId, OrderchequeimageListener listener) {
        this.context = context;
        this.BankNameList = bankNameList;
        this.listener = listener;
        this.orderId = orderId;
        this.paymentList = payment;
    }

    @NonNull
    @Override
    public MultipleChequeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new MultipleChequeAdapter.ViewHolder(DataBindingUtil.
                inflate(LayoutInflater.from(viewGroup.getContext()),
                        R.layout.multiplecheque_adapter, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MultipleChequeAdapter.ViewHolder holder, int position) {
        paymentList.get(position).setOrderId(orderId);
        paymentList.get(position).setPaymentFrom("Cheque");

        holder.mBinding.etChequeAmount.setText("" + new DecimalFormat("##.##").format(paymentList.get(position).getAmount()));
        holder.mBinding.etChequeNumber.setText(paymentList.get(position).getTransId());
        if (paymentList.get(position).getPaymentDate()!=null){
            holder.mBinding.etDate.setText(Utils.getDateFormat(paymentList.get(position).getPaymentDate()));
        }
        else{
            holder.mBinding.etDate.setText("");
        }
        if (paymentList.get(position).getChequeImageUrl()==null||paymentList.get(position).getChequeImageUrl().equalsIgnoreCase("")){
            holder.mBinding.checkPhotoCapture.setImageResource(R.drawable.ic_add_image_icon);
        }
        else{
            Picasso.get().load(SharePrefs.getInstance(MyApplication.getInstance()).getString(SharePrefs.BASEURL) + paymentList.get(position).getChequeImageUrl()).into(holder.mBinding.checkPhotoCapture);
            //holder.mBinding.checkPhotoCapture.setImageResource(R.drawable.ic_add_image_icon);
        }
        if (paymentList.get(position).getChequeBankName()==null){
            holder.mBinding.spBankList.setSelection(0);
        }
        else{
            holder.mBinding.spBankList.setSelection(getIndex(holder.mBinding.spBankList, paymentList.get(position).getChequeBankName()));
        }


    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return paymentList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private MultiplechequeAdapterBinding mBinding;

        public ViewHolder(MultiplechequeAdapterBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;

            adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, BankNameList);
            mBinding.spBankList.setAdapter(adapter);
            mBinding.spBankList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String BankName = BankNameList.get(position);
                    paymentList.get(getAdapterPosition()).setChequeBankName(BankName);
                    if (BankName.equalsIgnoreCase("OTHERS")) {
                        BanknamePopup(mBinding.spBankList, adapter);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            mBinding.checkPhotoCapture.setOnClickListener(v -> listener.onImageClick(mBinding.checkPhotoCapture, getAdapterPosition()));

            mBinding.ivCross.setOnClickListener(v -> {
                try {
                double cancelAmt=0;
                cancelAmt= paymentList.get(getAdapterPosition()).getAmount();
                paymentList.get(getAdapterPosition()).setAmount(0);
                paymentList.remove(getAdapterPosition());
                listener.onAmountChange(paymentList);
               // notifyItemRemoved(getAdapterPosition());
                    notifyDataSetChanged();
            }catch (Exception e)
            {
                e.printStackTrace();
            }
//                notifyItemRangeChanged(getAdapterPosition(), paymentList.size());
            });
            mBinding.dateLayout.setOnClickListener(v -> CheckDate(getAdapterPosition(), mBinding));
            mBinding.etChequeAmount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String chequeAmount = s.toString();
                    if (s.length() > 0) {
                        paymentList.get(getAdapterPosition()).setAmount(Double.parseDouble(chequeAmount));
                    } else {
                        paymentList.get(getAdapterPosition()).setAmount(0);
                    }
                    listener.onAmountChange(paymentList);
                }
            });
            mBinding.etChequeAmount.setOnFocusChangeListener((v, hasFocus) -> {
                if (  mBinding.etChequeAmount.getText().toString().startsWith("0")) {
                    mBinding.etChequeAmount.setText("");
                }
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput( mBinding.etChequeAmount, InputMethodManager.SHOW_IMPLICIT);
            });
            mBinding.etChequeNumber.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String chequeNumber = s.toString();
                    if (chequeNumber.length() > 0) {
                        paymentList.get(getAdapterPosition()).setTransId(chequeNumber);
                    }
                    else{
                        paymentList.get(getAdapterPosition()).setTransId("");
                    }

                }
            });
        }
    }

    private void BanknamePopup(Spinner spBankList, ArrayAdapter<String> adapter) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        BankNamePopupBinding bankNamePopupBinding = DataBindingUtil.inflate(inflater, R.layout.bank_name_popup, null, false);
        Dialog customDialog = new Dialog(context, R.style.CustomDialog);
        customDialog.setContentView(bankNamePopupBinding.getRoot());
        customDialog.setCancelable(false);
        bankNamePopupBinding.okBtn.setOnClickListener(v -> {
            if (bankNamePopupBinding.etBankName.getText().toString().equalsIgnoreCase("")) {
                Toast.makeText(context, "Enter Bank Name", Toast.LENGTH_SHORT).show();
            } else {
                customDialog.dismiss();
                adapter.addAll(bankNamePopupBinding.etBankName.getText().toString());
                String BankName = bankNamePopupBinding.etBankName.getText().toString();
                spBankList.setSelection(getIndex(spBankList, BankName));
            }
        });
        bankNamePopupBinding.cancelBtn.setOnClickListener(v -> {
            spBankList.setSelection(getIndex(spBankList, "Select Bank Name"));
            customDialog.dismiss();
        });
        customDialog.show();
    }

    private int getIndex(Spinner spinner, String myString) {
        int index = 0;
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(myString)) {
                index = i;
            }
        }
        return index;
    }

    private void CheckDate(int position, MultiplechequeAdapterBinding holder) {
        try {
            Calendar c1 = Calendar.getInstance();
            int mYear = c1.get(Calendar.YEAR);
            int mMonth = c1.get(Calendar.MONTH);
            int mDay = c1.get(Calendar.DAY_OF_MONTH);
            System.out.println("the selected " + mDay);
            DatePickerDialog dialog = new DatePickerDialog(context, (view, mYear1, mMonth1, mDay1) -> {
                try {
                    holder.etDate.setText(new StringBuilder()
                            // Month is 0 based so add 1
                            .append(mDay1).append("/").append(mMonth1 + 1).append("/")
                            .append(mYear1).append(" "));
                    String date = Utils.getSimpleDateFormat((holder.etDate.getText()).toString().trim());
                    paymentList.get(position).setPaymentDate(date);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, mYear, mMonth, mDay);
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<DeliveryPayments> getListFromAdapter() {
        return paymentList;
    }
}
