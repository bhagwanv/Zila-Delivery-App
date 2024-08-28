package com.sk.ziladelivery.ui.views.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.sk.ziladelivery.R;
import com.sk.ziladelivery.databinding.FragmentCurrencyBinding;
import com.sk.ziladelivery.listener.CurrencyIncreaseInterface;
import com.sk.ziladelivery.data.model.CurrencyDenomination;
import com.sk.ziladelivery.data.model.CurrencyListModel;
import com.sk.ziladelivery.data.model.CurrencyResponseModel;
import com.sk.ziladelivery.data.model.PostCashCollection;
import com.sk.ziladelivery.utilities.ApiResponse;
import com.sk.ziladelivery.utilities.SharePrefs;
import com.sk.ziladelivery.utilities.Utils;
import com.sk.ziladelivery.ui.views.viewmodels.CurrencyViewModel;
import com.sk.ziladelivery.ui.views.adapter.CurrencyAdapter;
import com.sk.ziladelivery.ui.views.main.MainActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;

public class CurrencyFragment extends Fragment implements CurrencyIncreaseInterface {
    private MainActivity activity;
    private FragmentCurrencyBinding mBinding;
    RecyclerView rvCurrency;
    CurrencyAdapter adapter;

    TextView tvTotalQty, tvTotalAmt;
    ArrayList<PostCashCollection> postCashCollectionArrayList = new ArrayList<>();
    ArrayList<CurrencyDenomination> currencyCoinList = new ArrayList<>();
    CurrencyViewModel mCurrencyViewModel;
    Utils utils;
    public Dialog customDialog;
    int AssignmentID;
    double cashAmount = 0.0;

    public CurrencyFragment() {
    }


    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        this.activity = (MainActivity) context;

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_currency, container, false);
        SharePrefs.getInstance(activity).putBoolean(SharePrefs.UPDATE_DATA, true);

        /**
         *
         *init
         * **/

        initView();
        mCurrencyViewModel = ViewModelProviders.of(this).get(CurrencyViewModel.class);
        mBinding.setCurrencyViewModel(mCurrencyViewModel);
        mBinding.setLifecycleOwner(this);
        activity.viewVisibleTextView(false);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            AssignmentID = bundle.getInt("AssignmentID");
            cashAmount = bundle.getDouble("cashAmount");
        }
        mBinding.cashPaidamnt.setText("₹ " + cashAmount);
        /***
         * Getting response
         * **/
        mCurrencyViewModel.getCurrencyResponseData().observe(getViewLifecycleOwner(), apiResponse -> {
            if (apiResponse != null) {
                consumeResponse(apiResponse);
            }
        });

        mCurrencyViewModel.getCurrencyData().observe(activity, apiResponse -> {
            if (apiResponse != null) {
                CurrencyResponseList(apiResponse);
            }
        });
        boolean flag = SharePrefs.getInstance(activity).getBoolean(SharePrefs.FLAG);

        if (flag) {
            //hit Currency image API
            String Cashamount = SharePrefs.getCashmanagmentSharedPreferences(activity, SharePrefs.EDIT_AMOUNT_JSON);
            if (Cashamount != null) {
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<CurrencyDenomination>>() {
                }.getType();

                currencyCoinList = gson.fromJson(Cashamount, type);
                if (currencyCoinList != null) {
                    if (currencyCoinList.size() > 0) {
                        //currencyCoinList = sortAndAddSections(currencyCoinList);
                        adapter.setCurrencyList(currencyCoinList, mBinding.totalAmnt, mBinding.noteQty, mBinding.coinQty);
                    }
                } else {
                    mCurrencyViewModel.hitgetCurrencyData();
                }
            } else {
                mCurrencyViewModel.hitgetCurrencyData();
            }

        } else {
            String historyModel = SharePrefs.getCashmanagmentSharedPreferences(activity, SharePrefs.HISTORY_MODEL);
            setHistoryData(historyModel);
        }

        mBinding.setCurrencySubmitListener(() -> {
            int totalcoinAmnt = Integer.parseInt(mBinding.coinQty.getText().toString());
            int totalAmount = Integer.parseInt(mBinding.totalAmnt.getText().toString());
            Double overallAmnt = Double.valueOf(Math.round((cashAmount * 0.5) / 100));
            System.out.println("overallAmnt " + overallAmnt);
            if (totalcoinAmnt > 1000) {
                Toast.makeText(activity, "You can not add coin more than 1000 rupees", Toast.LENGTH_SHORT).show();
            } else if (cashAmount != totalAmount) {
                Toast.makeText(activity, "Total Amount Should be Equals to " + cashAmount, Toast.LENGTH_SHORT).show();
            } else {
                SubmitPopup();
            }
        });
        mBinding.back.setOnClickListener(v -> activity.onBackPressed());
        return mBinding.getRoot();
    }

    private void setHistoryData(String historyModel) {
        try {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<CurrencyDenomination>>() {
            }.getType();
            String Cashamount = SharePrefs.getCashmanagmentSharedPreferences(activity, SharePrefs.EDIT_AMOUNT_JSON);
            if (Cashamount != null) {
                currencyCoinList = gson.fromJson(Cashamount, type);
                if (currencyCoinList != null) {
                    if (currencyCoinList.size() > 0) {
                        // currencyCoinList = sortAndAddSections(currencyCoinList);
                        adapter.setCurrencyList(currencyCoinList, mBinding.totalAmnt, mBinding.noteQty, mBinding.coinQty);
                    }
                } else {
                    JSONObject object = new JSONObject(historyModel);
                    currencyCoinList = gson.fromJson(object.get("CashCollections").toString(), type);
                    currencyCoinList = sortAndAddSections(currencyCoinList);
                    adapter.setCurrencyList(currencyCoinList, mBinding.totalAmnt, mBinding.noteQty, mBinding.coinQty);
                }
            } else {
                JSONObject object = new JSONObject(historyModel);
                currencyCoinList = gson.fromJson(object.get("CashCollections").toString(), type);
                currencyCoinList = sortAndAddSections(currencyCoinList);
                adapter.setCurrencyList(currencyCoinList, mBinding.totalAmnt, mBinding.noteQty, mBinding.coinQty);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void CurrencyResponseData(JsonElement data) {
        if (Utils.isJSONValid(data.toString())) {
            if (!data.isJsonNull()) {
                Log.d("response=212", data.toString());
                try {
                    JSONObject obj = new JSONObject(data.toString());
                    CurrencyListModel currencyListModel = new Gson().fromJson(obj.toString(), CurrencyListModel.class);
                    if (currencyListModel.getStatus()) {
                        mBinding.cmngsoonTxt.setVisibility(View.GONE);
                        mBinding.currencyLayout.setVisibility(View.VISIBLE);
                        currencyCoinList = sortAndAddSections(currencyListModel.getCurrencyDenomination());
                        adapter.setCurrencyList(currencyCoinList, mBinding.totalAmnt, mBinding.noteQty, mBinding.coinQty);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                mBinding.cmngsoonTxt.setVisibility(View.VISIBLE);
                mBinding.currencyLayout.setVisibility(View.GONE);
                Toast.makeText(activity, getResources().getString(R.string.errorString), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(activity, data.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private ArrayList<CurrencyDenomination> sortAndAddSections(ArrayList<CurrencyDenomination> itemList) {
        Collections.sort(itemList);
        ArrayList<CurrencyDenomination> tempList = new ArrayList<>();
        //First we sort the array
        // Collections.sort(itemList);
        //Loops thorugh the list and add a section before each sectioncell start
        String header = "";
        for (int i = 0; i < itemList.size(); i++) {
            //If it is the st
            // art of a new section we create a new listcell and add it to our array
            if (!header.equalsIgnoreCase(itemList.get(i).getCurrencyType())) {
                CurrencyDenomination sectionCell = new CurrencyDenomination(null, null, itemList.get(i).getCurrencyType(), 0);
                sectionCell.setToSectionHeader();
                tempList.add(sectionCell);
                header = itemList.get(i).getCurrencyType();
            }
            tempList.add(itemList.get(i));
        }

        return tempList;
    }

    private void initView() {
        utils = new Utils(activity);
        rvCurrency = mBinding.rvCurrency;
        tvTotalQty = mBinding.totalQty;
        tvTotalAmt = mBinding.totalAmt;
        rvCurrency.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        rvCurrency.setHasFixedSize(true);
        adapter = new CurrencyAdapter(activity, currencyCoinList, this);
        rvCurrency.setAdapter(adapter);

    }

    private void SubmitPopup() {
        LayoutInflater inflater = getLayoutInflater();
        if (activity != null && inflater != null) {
            final View mView = inflater.inflate(R.layout.confirmation_popup, null);
            customDialog = new Dialog(activity, R.style.CustomDialog);
            customDialog.setContentView(mView);
            TextView okBtn = mView.findViewById(R.id.btn_yes);
            TextView cancelBtn = mView.findViewById(R.id.btn_no);
            okBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int totalAmount = Integer.parseInt(mBinding.totalAmnt.getText().toString());
                    /* if (totalAmount > 0) {*/
                    if (!Utils.checkInternetConnection(activity)) {
                        Utils.setToast(activity, CurrencyFragment.this.getResources().getString(R.string.network_error));
                    } else {
                        for (int i = 0; i < currencyCoinList.size(); i++) {
                            if (currencyCoinList.get(i).getTitle() != null) {
                                postCashCollectionArrayList.add(new PostCashCollection(i, currencyCoinList.get(i).getId(), currencyCoinList.get(i).getCurrencyCountByDBoy(), currencyCoinList.get(i).getValue(), currencyCoinList.get(i).getCurrencyType(), currencyCoinList.get(i).getTitle()));
                            }
                        }
                        Gson gson = new Gson();
                        SharePrefs.setCashmanagmentSharedPreference(activity, SharePrefs.CASH_AMOUNT_JSON, gson.toJson(postCashCollectionArrayList));
                        SharePrefs.setCashmanagmentSharedPreference(activity, SharePrefs.EDIT_AMOUNT_JSON, gson.toJson(currencyCoinList));
                        SharePrefs.setCashmanagmentSharedPreference(activity, SharePrefs.EDIT_AMOUNT_JSON,"");
                        // SharePrefs.getInstance(activity).putInt(SharePrefs.CASH_AMOUNT, totalAmount);

                        activity.onBackPressed();
                        customDialog.dismiss();
                    }
                   /* } else {
                        Utils.setToast(activity, CurrencyFragment.this.getResources().getString(R.string.select_coin));
                    }*/
                }
            });
            cancelBtn.setOnClickListener(v -> customDialog.dismiss());
            customDialog.show();
        }
    }

    @Override
    public void totalCurrencyAmt() {

    }

    /*
     * method to handle response
     * */
    private void consumeResponse(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                mBinding.progressBid.setVisibility(View.VISIBLE);
                break;
            case SUCCESS:
                mBinding.progressBid.setVisibility(View.GONE);
                renderSuccessResponse(apiResponse.data);
                break;

            case ERROR:
                mBinding.progressBid.setVisibility(View.GONE);
                Utils.setToast(activity, getResources().getString(R.string.errorString));
                break;

            default:
                break;
        }
    }

    private void CurrencyResponseList(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                mBinding.progressBid.setVisibility(View.VISIBLE);
                break;
            case SUCCESS:
                mBinding.progressBid.setVisibility(View.GONE);
                CurrencyResponseData(apiResponse.data);
                break;

            case ERROR:
                mBinding.progressBid.setVisibility(View.GONE);
                Utils.setToast(activity, getResources().getString(R.string.errorString));
                break;

            default:
                break;
        }
    }


    /*
     * method to handle success response
     * */
    private void renderSuccessResponse(JsonElement response) {
        if (Utils.isJSONValid(response.toString())) {
            if (!response.isJsonNull()) {
                Log.d("response=", response.toString());
                try {
                    JSONObject obj = new JSONObject(response.toString());
                    CurrencyResponseModel currencyResponseModel = new Gson().fromJson(obj.toString(), CurrencyResponseModel.class);
                    if (currencyResponseModel.getStatus()) {
                        Toast.makeText(activity, getResources().getString(R.string.currency_Submit), Toast.LENGTH_SHORT).show();
                        activity.onBackPressed();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(activity, getResources().getString(R.string.errorString), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(activity, response.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void showProgressDialog(Activity activity) {
        if (customDialog == null) {
            customDialog = new Dialog(activity, R.style.CustomDialog);
        }
        LayoutInflater inflater = LayoutInflater.from(activity);
        View mView = inflater.inflate(R.layout.progress_dialog, null);
        customDialog.setContentView(mView);
        if (!customDialog.isShowing()) {
            customDialog.show();
        }
    }

    public void hideProgressDialog(Activity activity) {
        if (customDialog != null) {
            //if (customDialog != null) {
            customDialog.dismiss();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Utils.keyboard(activity);
        // Utils.keyboardHide(activity);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setActionBarConfiguration();
    }

    private void setActionBarConfiguration() {
        final DrawerLayout drawer = getActivity().findViewById(R.id.container);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.GONE);
        ((MainActivity) getActivity()).toggle.syncState();
        activity.startBreak.setVisibility(View.GONE);
        activity.tvStartTime.setVisibility(View.GONE);

    }
}