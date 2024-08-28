package com.sk.ziladelivery.ui.views.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sk.ziladelivery.R;
import com.sk.ziladelivery.databinding.FragmentHistoryBinding;
import com.sk.ziladelivery.data.model.HistoryDataModel;
import com.sk.ziladelivery.utilities.ApiResponseObj;
import com.sk.ziladelivery.utilities.CommonMethods;
import com.sk.ziladelivery.utilities.DateUtils;
import com.sk.ziladelivery.utilities.Logger;
import com.sk.ziladelivery.utilities.SharePrefs;
import com.sk.ziladelivery.utilities.Utils;
import com.sk.ziladelivery.ui.views.viewmodels.HistoryAdapterViewModel;
import com.sk.ziladelivery.ui.views.viewmodels.HistoryViewModel;
import com.sk.ziladelivery.ui.views.adapter.HistoryIssuanceDetailAdapter;
import com.sk.ziladelivery.ui.views.main.MainActivity;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Collections;

public class HistoryFragment extends Fragment {
    private MainActivity activity;
    private FragmentHistoryBinding mBinding;
    private String sStartDate, sEndDate;
    private HistoryViewModel historyViewModel;
    private HistoryIssuanceDetailAdapter adapter;
    public Dialog customDialog;

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
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
        if (mBinding == null) {
            mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_history, container, false);
        }
        if (!Utils.checkInternetConnection(activity)) {
            Utils.setToast(activity, getResources().getString(R.string.network_error));
        }
        /**
         * Init view
         * ***/
        initialization();
        /**
         * select start date
         * ***/
        mBinding.startdateLayout.setOnClickListener(v -> StartDate());
        /**
         * select end date
         * ***/
        mBinding.enddateLayout.setOnClickListener(v -> EndDate());
        /**
         * search btn click
         * ***/
        mBinding.search.setOnClickListener(v -> {
            sStartDate = mBinding.tvStartdate.getText().toString().trim();
            sEndDate = mBinding.tvEnddate.getText().toString().trim();

            if (sStartDate.equalsIgnoreCase("")) {
                Utils.setToast(activity, getString(R.string.start_date));
            } else if (sEndDate.equalsIgnoreCase("")) {
                Utils.setToast(activity, getString(R.string.end_date));
            } else if (DateUtils.getEpochTime(sStartDate) > DateUtils.getEpochTime(sEndDate)) {
                Utils.setToast(activity, getString(R.string.correct_date));
            } else {
                if (!Utils.checkInternetConnection(activity)) {
                    Utils.setToast(activity, getResources().getString(R.string.network_error));
                } else {
                    historyViewModel.HistoryDetailsApi(SharePrefs.getInstance(activity).getString(SharePrefs.MOBILE),
                            sStartDate, sEndDate, String.valueOf(SharePrefs.getInstance(activity).getInt(SharePrefs.PEOPLE_ID)));
                }
            }
        });
        /**
         * getting data search btn
         * ***/
        historyViewModel.getOrderDetailsData().observe(activity, apiResponseObj -> {
            if (apiResponseObj != null) {
                consumeResponse(apiResponseObj);
            }
        });
        return mBinding.getRoot();
    }

    private void initialization() {
        setActionBarConfiguration();
        Utils utils = new Utils(activity);
        historyViewModel = ViewModelProviders.of(activity).get(HistoryViewModel.class);
        mBinding.rvHistory.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mBinding.rvHistory.setHasFixedSize(true);
    }

    /*
     * method to handle response
     * */
    private void consumeResponse(ApiResponseObj apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                Utils.showProgressDialog(getActivity());
                break;
            case SUCCESS:
                Utils.hideProgressDialog(activity);
                renderSuccessResponse(apiResponse.data);
                break;
            case ERROR:
                mBinding.rvHistory.setAdapter(null);
                Utils.hideProgressDialog(activity);
//                Utils.setToast(getActivity(), getResources().getString(R.string.errorString));
                break;
            default:
                break;
        }
    }

    /*
     * method to handle success response
     * */
    private void renderSuccessResponse(JsonObject response) {
        try {
            if (Utils.isJSONValid(response.toString())) {
                if (response != null && !response.isJsonNull()) {
                    Logger.e(CommonMethods.getTag(this), response.toString());
                    try {
                        HistoryDataModel historyDataModel = new Gson().fromJson(response.toString(), HistoryDataModel.class);
                        if (historyDataModel.getStatus()) {
                            Collections.reverse(historyDataModel.getOrderHistory());
                            adapter = new HistoryIssuanceDetailAdapter(activity, historyDataModel.getOrderHistory());
                            mBinding.rvHistory.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        } else {
                            mBinding.rvHistory.setAdapter(null);
                            Toast.makeText(activity, historyDataModel.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(activity, getResources().getString(R.string.errorString), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(activity, response.toString(), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void EndDate() {
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        System.out.println("the selected " + mDay);
        DatePickerDialog dialog = new DatePickerDialog(activity, new mDateSetListener1(), mYear, mMonth, mDay);
        dialog.show();
    }

    private void StartDate() {
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        System.out.println("the selected" + mDay);
        DatePickerDialog dialog = new DatePickerDialog(activity, new mDateSetListener(), mYear, mMonth, mDay);
        dialog.show();
    }

    class mDateSetListener implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            int mYear = year;
            int mMonth = monthOfYear;
            int mDay = dayOfMonth;
            mBinding.tvStartdate.setText(new StringBuilder()
                    // Month is 0 based so add 1
                    .append(mMonth + 1).append("/").append(mDay).append("/")
                    .append(mYear).append(" "));
        }
    }

    class mDateSetListener1 implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // getCalender();
            int mYear = year;
            int mMonth = monthOfYear;
            int mDay = dayOfMonth;
            int monthEnd = monthOfYear + 1;
            mBinding.tvEnddate.setText(new StringBuilder()
                    // Month is 0 based so add 1
                    .append(mMonth + 1).append("/").append(mDay).append("/")
                    .append(mYear).append(" "));
        }
    }

    public void bind(HistoryAdapterViewModel item) {
        mBinding.setHistoryDetailSetViewModel(item);
        mBinding.executePendingBindings();
    }

    private void setActionBarConfiguration() {
        final DrawerLayout drawer = getActivity().findViewById(R.id.container);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        TextView tittleTextView = getActivity().findViewById(R.id.toolbar_title);
        TextView assignmentid = getActivity().findViewById(R.id.assignmentid);
        assignmentid.setVisibility(View.GONE);
        tittleTextView.setVisibility(View.VISIBLE);
        tittleTextView.setText(activity.getString(R.string.my_history));
        TextView timer = getActivity().findViewById(R.id.tv_timer);
        timer.setVisibility(View.GONE);
        TextView timmer = getActivity().findViewById(R.id.tv_timmer);
        timmer.setVisibility(View.GONE);
        TextView start_timer = getActivity().findViewById(R.id.start_timer);
        start_timer.setVisibility(View.GONE);
        activity.startBreak.setVisibility(View.GONE);


        toolbar.setNavigationOnClickListener(v -> {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        ((MainActivity) getActivity()).toggle.syncState();
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
}