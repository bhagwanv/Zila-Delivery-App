package com.sk.ziladelivery.ui.views.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.sk.ziladelivery.R;
import com.sk.ziladelivery.databinding.SettleHistoryFragmentBinding;
import com.sk.ziladelivery.data.model.AssginmentHistoryResponseModel;
import com.sk.ziladelivery.utilities.ApiResponse;
import com.sk.ziladelivery.utilities.EndlessRecyclerViewScrollListener;
import com.sk.ziladelivery.utilities.SharePrefs;
import com.sk.ziladelivery.utilities.Utils;
import com.sk.ziladelivery.ui.views.viewmodels.AssginmentSettleViewModel;
import com.sk.ziladelivery.ui.views.adapter.AssginmentsettleHistoryAdapter;
import com.sk.ziladelivery.ui.views.main.MainActivity;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Calendar;

public class AssginmentSettleHistoryFragment extends Fragment {
    private MainActivity activity;
    private AssginmentSettleViewModel assignmentViewModel;
    private ArrayList<AssginmentHistoryResponseModel> list;
    private SettleHistoryFragmentBinding mBinding;
    private int SlipId = 0;
    private int AssignmentId = 0;
    private int PageNumber = 1;
    int pageSize = 10;
    private String StartDate = "", EndDate = "";
    private boolean isLoadData = true;
    private LinearLayoutManager mLinearLayoutManager;
    private String searchString = "";
    private TabLayout tabLayout;
    private boolean isViewLoaded;
    boolean isSlip = true;
    boolean isAssginment = false;
    boolean isSearch = false;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.activity = (MainActivity) context;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.settle_history_fragment, container, false);
        initialization();
        return mBinding.getRoot();
    }


    private void capturePhoto() {
        if (activity != null) {
            final View mView = getLayoutInflater().inflate(R.layout.search_by_popup, null);
            final Dialog customDialog = new Dialog(activity, R.style.CustomDialog);
            customDialog.setContentView(mView);
            TextView tv_slip = mView.findViewById(R.id.tv_slip);
            TextView tv_assit_id = mView.findViewById(R.id.tv_assit_id);
            TextView tv_date = mView.findViewById(R.id.tv_date);
            tv_slip.setOnClickListener(v -> {
                mBinding.SearchBar.setVisibility(View.VISIBLE);
                mBinding.dateLayout.setVisibility(View.GONE);
                mBinding.searchTxt.setHint("Search Slip No.");
                mBinding.searchTxt.setText("");
                isSlip = true;
                isAssginment = false;
                customDialog.dismiss();
            });
            tv_assit_id.setOnClickListener(v -> {
                mBinding.SearchBar.setVisibility(View.VISIBLE);
                mBinding.dateLayout.setVisibility(View.GONE);
                mBinding.searchTxt.setHint("Search Assignment");
                mBinding.searchTxt.setText("");
                isSlip = false;
                isAssginment = true;
                customDialog.dismiss();
            });
            tv_date.setOnClickListener(v -> {
                mBinding.dateLayout.setVisibility(View.VISIBLE);
                mBinding.SearchBar.setVisibility(View.GONE);
                SlipId = 0;
                AssignmentId = 0;
                StartDate = "";
                EndDate = "";
                mBinding.tvStartdate.setHint("start date");
                mBinding.tvEnddate.setHint("End date");
                mBinding.tvStartdate.setText("");
                mBinding.tvEnddate.setText("");
                customDialog.dismiss();
            });
            customDialog.show();
        }
    }

    private void initialization() {
        setActionBarConfiguration();
        mBinding.searchTxt.setFocusable(true);
        mBinding.searchTxt.setFocusableInTouchMode(true);
        mLinearLayoutManager = new LinearLayoutManager(activity);
        mBinding.rvMyTask.setLayoutManager(mLinearLayoutManager);
        mBinding.rvMyTask.setHasFixedSize(true);
        list = new ArrayList<>();
        assignmentViewModel = ViewModelProviders.of(activity).get(AssginmentSettleViewModel.class);
        assignmentViewModel.getAssginmentHistory().observe(this, apiResponse -> {
            if (apiResponse != null) {
                consumeResponseAssignment(apiResponse);
            }
        });
        callAPI(1);
    }

    private void callAPI(int pagenumber) {
        if (!Utils.checkInternetConnection(activity)) {
            Utils.setToast(activity, getResources().getString(R.string.network_error));
        } else {

            assignmentViewModel.hitAssignmentHistoryApi(SlipId, AssignmentId, SharePrefs.getInstance(activity).getInt(SharePrefs.PEOPLE_ID), Utils.getSimpleDateFormat(StartDate), Utils.getSimpleDateFormat(EndDate), pagenumber, pageSize);
        }
    }

    private void consumeResponseAssignment(ApiResponse apiResponseObj) {
        try {
            switch (apiResponseObj.status) {
                case LOADING:
                    mBinding.progressBid.setVisibility(View.VISIBLE);
                    break;
                case SUCCESS:
                    mBinding.progressBid.setVisibility(View.GONE);
                    renderSuccessResponseAssignment(apiResponseObj.data);
                    break;
                case ERROR:
                    mBinding.progressBid.setVisibility(View.GONE);
                    Utils.setToast(activity, getResources().getString(R.string.errorString));
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void renderSuccessResponseAssignment(JsonElement response) {
        try {
            if (Utils.isJSONValid(response.toString())) {
                if (response.isJsonArray()) {
                    try {
                        JSONArray obj = new JSONArray(response.toString());
                        list.addAll(new Gson().fromJson(String.valueOf(obj),
                                new TypeToken<ArrayList<AssginmentHistoryResponseModel>>() {
                                }.getType()));
                        if (list.size() > 0) {
                            mBinding.tvMyTask.setVisibility(View.GONE);
                            mBinding.rvMyTask.setVisibility(View.VISIBLE);
                            AssginmentsettleHistoryAdapter adapter = new AssginmentsettleHistoryAdapter(activity, list);
                            mBinding.rvMyTask.setAdapter(adapter);
                        } else {
                            mBinding.tvMyTask.setVisibility(View.VISIBLE);
                            mBinding.rvMyTask.setVisibility(View.GONE);
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

    private void setActionBarConfiguration() {
        final DrawerLayout drawer = activity.findViewById(R.id.container);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        Toolbar toolbar = activity.findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        TextView tittleTextView = activity.findViewById(R.id.toolbar_title);
        TextView assignmentid = activity.findViewById(R.id.assignmentid);
        assignmentid.setVisibility(View.GONE);
        tittleTextView.setVisibility(View.VISIBLE);
        tittleTextView.setText("Assignment Settle");
        TextView starttimer = activity.findViewById(R.id.start_timer);
        starttimer.setVisibility(View.GONE);
        starttimer.setText(R.string.history);
        TextView timer = activity.findViewById(R.id.tv_timer);
        timer.setVisibility(View.GONE);
        toolbar.setNavigationOnClickListener(v -> {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        activity.toggle.syncState();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.rvMyTask.addOnScrollListener(new EndlessRecyclerViewScrollListener(mLinearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                callAPI(page);
            }
        });
        /*mBinding.searchTxt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_NULL) {
                    if (!searchString.isEmpty()) {
                        list.clear();
                        callAPI(1);
                        return true;
                    } else {
                        Toast.makeText(activity, "Enter Slip ID or Assginment ID", Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }
        });*/
        mBinding.searchTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    SlipId = 0;
                    AssignmentId = 0;
                    StartDate = "";
                    EndDate = "";
                    searchString = editable.toString().trim();
                    if (searchString.length() > 0) {
                        if (isSlip) {
                            isSearch = true;
                            SlipId = Integer.parseInt(searchString);

                        } else {
                            isSearch = true;
                            AssignmentId = Integer.parseInt(searchString);
                        }
                    } else {
                        SlipId = 0;
                        AssignmentId = 0;
                        list.clear();
                        callAPI(1);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        mBinding.ivEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchString.length() > 0) {
                    list.clear();
                    callAPI(1);
                } else {
                    Toast.makeText(activity, "Enter Slip ID or Assginment ID", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mBinding.fabBarCodeSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                capturePhoto();
            }
        });
        mBinding.startdateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartDate();
            }
        });
        mBinding.enddateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EndDate();
            }
        });
        mBinding.search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.clear();
                callAPI(1);
            }
        });
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

            StartDate = String.valueOf(new StringBuilder()
                    // Month is 0 based so add 1
                    .append(mDay).append("/").append(mMonth + 1).append("/")
                    .append(mYear).append(" "));
            mBinding.tvStartdate.setText(StartDate);

        }
    }

    class mDateSetListener1 implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // getCalender();
            int mYear = year;
            int mMonth = monthOfYear;
            int mDay = dayOfMonth;
            EndDate = String.valueOf(new StringBuilder()
                    // Month is 0 based so add 1
                    .append(mDay).append("/").append(mMonth + 1).append("/")
                    .append(mYear).append(" "));
            mBinding.tvEnddate.setText(EndDate);
        }
    }
}





