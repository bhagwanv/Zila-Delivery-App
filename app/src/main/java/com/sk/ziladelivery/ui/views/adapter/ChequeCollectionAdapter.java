package com.sk.ziladelivery.ui.views.adapter;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.sk.ziladelivery.R;
import com.sk.ziladelivery.databinding.BankNamePopupBinding;
import com.sk.ziladelivery.databinding.ExpChequeHeaderBinding;
import com.sk.ziladelivery.listener.ChequeImageListener;
import com.sk.ziladelivery.data.model.PostChequeCollectionModel;
import com.sk.ziladelivery.utilities.MyApplication;
import com.sk.ziladelivery.utilities.SharePrefs;
import com.sk.ziladelivery.utilities.TextUtils;
import com.sk.ziladelivery.utilities.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class ChequeCollectionAdapter extends RecyclerView.Adapter<ChequeCollectionAdapter.ViewHolder> {
    private Context context;
    private ArrayList<PostChequeCollectionModel> list;
    private ChequeImageListener listener;
    private String date;
    private boolean save = true;
    private String BankName = "";
    private ArrayList<String> BankNameList;
    public Dialog customDialog;

    public ChequeCollectionAdapter(Context context, ArrayList<PostChequeCollectionModel> list, ArrayList<String> bankNameList, ChequeImageListener listener) {
        this.context = context;
        this.list = list;
        BankNameList = bankNameList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ChequeCollectionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ChequeCollectionAdapter.ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                R.layout.exp_cheque_header, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder vh, int i) {
        try {
            PostChequeCollectionModel model = list.get(i);
            vh.mBinding.tvOrderId.setText(String.format("%s%s", context.getResources().getString(R.string.OrderID).concat(" "), model.getOrderid()));
            vh.mBinding.tvAmount.setText(String.format("₹ %s", Integer.valueOf((int) model.getChequeAmt())));
            vh.mBinding.etAmnt.setText(String.valueOf(Integer.valueOf((int) model.getChequeAmt())));
            vh.mBinding.chequeRefNo.setText(model.getChequeNumber());
            if (model.getChequeDate() != null) {
                vh.mBinding.etDate.setText(Utils.getDateFormat(model.getChequeDate()));
                vh.mBinding.tvDeliveryDate.setText(("Delivered :" + Utils.getDateFormat(model.getChequeDate())));
            }
            date = Utils.getSimpleDateFormat((vh.mBinding.etDate.getText()).toString().trim());

            Drawable vectorDrawable = AppCompatResources.getDrawable(context, R.drawable.ic_add_image_icon);

            if (!TextUtils.isNullOrEmpty(model.getChequeimagePath())) {
                Picasso.get().load(SharePrefs.getInstance(MyApplication.getInstance()).getString(SharePrefs.BASEURL) + model.getChequeimagePath()).into(vh.mBinding.ivAddCheque);
            } else {
                vh.mBinding.ivAddCheque.setImageDrawable(vectorDrawable);
            }
            if (!TextUtils.isNullOrEmpty(model.getChequeBankName())) {
                if (!BankNameList.contains(model.getChequeBankName())) {
                    BankNameList.addAll(Collections.singleton(model.getChequeBankName()));
                }
                BankName = model.getChequeBankName();
            } else {
                BankName = "Select Bank Name";
            }

            vh.mBinding.ivAddCheque.setOnClickListener(v -> {
                listener.onImageClick(vh.mBinding.ivAddCheque, i);
            });

            setSpinner(vh.mBinding.spBankList, BankNameList, i);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private void Banknamepopup(Spinner spBankList, ArrayAdapter<String> adapter) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        BankNamePopupBinding bankNamePopupBinding = DataBindingUtil.inflate(inflater, R.layout.bank_name_popup, null, false);
        customDialog = new Dialog(context, R.style.CustomDialog);
        customDialog.setContentView(bankNamePopupBinding.getRoot());
        customDialog.setCancelable(false);

        bankNamePopupBinding.okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bankNamePopupBinding.etBankName.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(context, "Enter Bank Name", Toast.LENGTH_SHORT).show();
                } else {
                    customDialog.dismiss();
                    adapter.addAll(bankNamePopupBinding.etBankName.getText().toString());
                    BankName = bankNamePopupBinding.etBankName.getText().toString();
                    spBankList.setSelection(getIndex(spBankList, BankName));
                }
            }
        });
        bankNamePopupBinding.cancelBtn.setOnClickListener(v -> {
            spBankList.setSelection(getIndex(spBankList, "Select Bank Name"));
            customDialog.dismiss();
        });
        customDialog.show();
    }

    private void setSpinner(Spinner spBankList, ArrayList<String> BankNameList, int i) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, BankNameList);
        spBankList.setAdapter(adapter);
        spBankList.setSelection(getIndex(spBankList, BankName));
        spBankList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                BankName = BankNameList.get(position);
                list.get(i).setChequeBankName(BankName);
                if (BankNameList.get(position).equalsIgnoreCase("OTHERS")) {
                    Banknamepopup(spBankList, adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void disableViews(ViewHolder vh) {
        vh.mBinding.ivAddCheque.setEnabled(false);
        vh.mBinding.chequeRefNo.setFocusableInTouchMode(false);
        vh.mBinding.chequeRefNo.setClickable(false);
        vh.mBinding.chequeRefNo.setFocusable(false);
        vh.mBinding.etAmnt.setClickable(false);
        vh.mBinding.etAmnt.setFocusable(false);
        vh.mBinding.etAmnt.setFocusableInTouchMode(false);
        vh.mBinding.dateLayout.setEnabled(false);
        vh.mBinding.btnEdit.setVisibility(View.VISIBLE);
        vh.mBinding.btnSave.setVisibility(View.GONE);
        vh.mBinding.spBankList.setEnabled(false);
        vh.mBinding.spBankList.setClickable(false);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void getListFromAdapter() {
        listener.onButtonClick(list, save);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ExpChequeHeaderBinding mBinding;

        public ViewHolder(ExpChequeHeaderBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
            /*mBinding.ivAddCheque.setEnabled(false);
            mBinding.dateLayout.setEnabled(false);*/
            mBinding.liHeader.setOnClickListener(v -> {
                if (mBinding.liItem.getVisibility() == View.GONE) {
                    mBinding.liItem.setVisibility(View.VISIBLE);
                    mBinding.ivIcon.setImageResource(R.drawable.ic_arrow_down_black);
                } else {
                    mBinding.liItem.setVisibility(View.GONE);
                    mBinding.ivIcon.setImageResource(R.drawable.ic_arrow_right);
                }
            });
            mBinding.dateLayout.setOnClickListener(v ->
            {
                CheckDate(getAdapterPosition(), mBinding.etDate);
            });

            mBinding.btnAddNote.setOnClickListener(v -> {
                if (mBinding.etNote.getVisibility() == View.GONE) {
                    mBinding.etNote.setVisibility(View.VISIBLE);
                } else {
                    mBinding.etNote.setVisibility(View.GONE);
                }
            });

            mBinding.etAmnt.addTextChangedListener(new TextWatcher() {
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
                        list.get(getAdapterPosition()).setUsedChequeAmt(Integer.valueOf(chequeAmount));
                    } else {
                        list.get(getAdapterPosition()).setUsedChequeAmt(0);
                    }

                }
            });
            mBinding.chequeRefNo.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String chequeNumber = s.toString();
                    if (s.length() > 0) {
                        list.get(getAdapterPosition()).setChequeNumber(chequeNumber);
                    } else {
                        list.get(getAdapterPosition()).setChequeNumber("");
                    }

                }
            });
            mBinding.etNote.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String ChequeNote = s.toString();
                    if (s.length() > 0) {
                        list.get(getAdapterPosition()).setChequeNote(ChequeNote);
                    } else {
                        list.get(getAdapterPosition()).setChequeNote("");
                    }

                }
            });
        }


    }

    private void CheckDate(int adapterPosition, EditText etDate) {
        try {
            Calendar c1 = Calendar.getInstance();
            int mYear = c1.get(Calendar.YEAR);
            int mMonth = c1.get(Calendar.MONTH);
            int mDay = c1.get(Calendar.DAY_OF_MONTH);
            System.out.println("the selected " + mDay);
            DatePickerDialog dialog = new DatePickerDialog(context, (view, mYear1, mMonth1, mDay1) -> {
                try {
                    etDate.setText(new StringBuilder()
                            // Month is 0 based so add 1
                            .append(mDay1).append("/").append(mMonth1 + 1).append("/")
                            .append(mYear1).append(" "));
                    String date = Utils.getSimpleDateFormat((etDate.getText()).toString().trim());
                    list.get(adapterPosition).setChequeDate(date);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, mYear, mMonth, mDay);
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}