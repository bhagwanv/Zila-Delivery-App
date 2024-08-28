package com.sk.ziladelivery.ui.views.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.sk.ziladelivery.R;
import com.sk.ziladelivery.databinding.CashCollectionHistoryBinding;
import com.sk.ziladelivery.listener.HistoryEditListener;
import com.sk.ziladelivery.data.model.CurrencyHistoryDataModel;
import com.sk.ziladelivery.data.model.CurrencyHistoryModel;
import com.sk.ziladelivery.utilities.ApiResponse;
import com.sk.ziladelivery.utilities.SharePrefs;
import com.sk.ziladelivery.utilities.Utils;
import com.sk.ziladelivery.ui.views.viewmodels.CashHistoryViewModel;
import com.sk.ziladelivery.ui.views.adapter.CashHistoryAdapter;
import com.sk.ziladelivery.ui.views.main.MainActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

public class CashManagmentHistory extends Fragment implements HistoryEditListener {
    private CashCollectionHistoryBinding mBinding;
    private MainActivity activity;
    private CashHistoryViewModel cashHistoryViewModel;
    private CashHistoryAdapter cashHistoryAdapter;
    private int peopleId;
    private int warehouseId;


    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        this.activity = (MainActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.cash_collection_history, container, false);

        initView();
        cashHistoryViewModel = ViewModelProviders.of(this).get(CashHistoryViewModel.class);

        cashHistoryViewModel.getcashHistory().observe(getViewLifecycleOwner(), apiResponse -> {
            if (apiResponse != null) {
                consumeResponse(apiResponse);
            }
        });
        cashHistoryViewModel.getHistorydata().observe(getViewLifecycleOwner(), apiResponse -> {
            if (apiResponse != null) {
                historyRespose(apiResponse);
            }
        });
        cashHistoryViewModel.hitCashHistorydata(peopleId, warehouseId);

        return mBinding.getRoot();
    }

    private void historyRespose(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                mBinding.progressBid.setVisibility(View.VISIBLE);
                break;
            case SUCCESS:
                mBinding.progressBid.setVisibility(View.GONE);
                SuccessResponseofHistory(apiResponse.data);
                break;
            case ERROR:
                mBinding.progressBid.setVisibility(View.GONE);
                Utils.setToast(activity, getResources().getString(R.string.errorString));
                break;
            default:
                break;
        }
    }

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

    private void renderSuccessResponse(JsonElement response) {
        if (Utils.isJSONValid(response.toString())) {
            if (!response.isJsonNull()) {
                Log.d("response=", response.toString());
                try {

                    JSONObject obj = new JSONObject(response.toString());
                    CurrencyHistoryModel currencyHistoryModel = new Gson().fromJson(obj.toString(), CurrencyHistoryModel.class);
                    if (currencyHistoryModel.getStatus()) {

                        cashHistoryAdapter = new CashHistoryAdapter(activity, currencyHistoryModel.getCurrencyCollectionSummaryDcs(), this);
                        mBinding.rvHistory.setAdapter(cashHistoryAdapter);
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

    private void SuccessResponseofHistory(JsonElement response) {
        if (Utils.isJSONValid(response.toString())) {
            if (!response.isJsonNull()) {
                Log.d("response=", response.toString());
                try {

                    JSONObject obj = new JSONObject(response.toString());
                    CurrencyHistoryDataModel currencyHistoryDataModel = new Gson().fromJson(obj.toString(), CurrencyHistoryDataModel.class);
                    if (currencyHistoryDataModel.getStatus()) {
                        Gson gson = new Gson();
                        SharePrefs.setCashmanagmentSharedPreference(activity, SharePrefs.HISTORY_MODEL, gson.toJson(currencyHistoryDataModel.getCurrencyCollectionDc()));
                        SharePrefs.setCashmanagmentSharedPreference(activity, SharePrefs.CHEQUE_AMOUNT, gson.toJson(currencyHistoryDataModel.getCurrencyCollectionDc().getChequeCollections()));
                        SharePrefs.setCashmanagmentSharedPreference(activity, SharePrefs.CASH_AMOUNT_JSON, gson.toJson(currencyHistoryDataModel.getCurrencyCollectionDc().getCashCollections()));
                        SharePrefs.setCashmanagmentSharedPreference(activity, SharePrefs.ONLINE_AMOUNT, gson.toJson(currencyHistoryDataModel.getCurrencyCollectionDc().getOnlineCollections()));
                        activity.switchContentWithStack(new AssignmentCashManagment());
                    } else {
                        Toast.makeText(activity, currencyHistoryDataModel.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void initView() {
        peopleId = SharePrefs.getInstance(activity).getInt(SharePrefs.PEOPLE_ID);
        warehouseId = SharePrefs.getInstance(activity).getInt(SharePrefs.WAREHOUSE_ID);
        mBinding.rvHistory.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        mBinding.rvHistory.setHasFixedSize(true);
        mBinding.back.setOnClickListener(v -> activity.onBackPressed());
    }

    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
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
    }

    @Override
    public void onResume() {
        super.onResume();
        SharePrefs.setCashmanagmentSharedPreference(activity, SharePrefs.ASSIGN_MODEL, null);
        SharePrefs.getInstance(activity).putString(SharePrefs.ASSIGN_ID, null);
        SharePrefs.setCashmanagmentSharedPreference(activity, SharePrefs.CASH_AMOUNT_JSON, null);
        SharePrefs.setCashmanagmentSharedPreference(activity, SharePrefs.CHEQUE_AMOUNT, null);
        SharePrefs.setCashmanagmentSharedPreference(activity, SharePrefs.ONLINE_AMOUNT, null);
        SharePrefs.getInstance(activity).putInt(SharePrefs.CASH_AMOUNT, 0);



























    }

    @Override
    public void Editclicked(int ID) {
        cashHistoryViewModel.hitgetHistorydata(ID);
    }
}
