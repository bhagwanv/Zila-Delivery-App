package com.sk.ziladelivery.ui.views.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.sk.ziladelivery.R;
import com.sk.ziladelivery.databinding.FragmentMyTaskBinding;
import com.sk.ziladelivery.listener.Presenters;
import com.sk.ziladelivery.listener.orderdetailClick;
import com.sk.ziladelivery.data.model.CurrentEndTimeModel;
import com.sk.ziladelivery.data.model.MyTaskModel;
import com.sk.ziladelivery.data.model.MyTaskModelMain;
import com.sk.ziladelivery.utilities.ApiResponse;
import com.sk.ziladelivery.utilities.CommonMethods;
import com.sk.ziladelivery.utilities.Logger;
import com.sk.ziladelivery.utilities.SharePrefs;
import com.sk.ziladelivery.utilities.Utils;
import com.sk.ziladelivery.ui.views.viewmodels.MyTaskViewModel;
import com.sk.ziladelivery.ui.views.adapter.MyTaskAdapter;
import com.sk.ziladelivery.ui.views.main.MainActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class AssignmentDetailFragment extends Fragment implements orderdetailClick {
    private FragmentMyTaskBinding mBinding;
    private MyTaskViewModel myTaskViewModel;
    private MainActivity activity;
    private TextView tv_assignmentid;
    private String time, type = "";
    private int assignmentID;
    private int searchOrderId = 0;
    private boolean isRefresh = false;
    private String searchString = "";
    private double lat, lg;
    ArrayList<MyTaskModel> list;
    ArrayList<MyTaskModel> ParsedList;
    MyTaskAdapter adapter;
    private int pastVisiblesItems = 0;
    private int visibleItemCount = 0;
    private int totalItemCount = 0;
    private int pageCount = 1;
    private boolean loading = true;
    private boolean isOrderinShipped = true;
    LinearLayoutManager linearLayoutManager;
    long millisUntilFinished = 0;
    private final String FORMAT = "%02d:%02d:%02d";
    private static String hms;
    public Handler mHandler = new Handler(); // use 'new Handler(Looper.getMainLooper());' if you want this handler to control something in the UI

    public AssignmentDetailFragment() {
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        this.activity = (MainActivity) context;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            stop();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_task, container, false);
        View view = mBinding.getRoot();
        Bundle bundle = this.getArguments();
        assignmentID = bundle.getInt("deliveryIssuanceId");
        time = bundle.getString("time");
        type = bundle.getString("type");
        linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        intView();

        myTaskViewModel.getMyTaskData().observe(activity, apiResponse -> {
            if (apiResponse != null) {
                consumeResponse(apiResponse);
            }
        });

        myTaskViewModel.getRejectedOrder().observe(activity, apiResponse -> {
            if (apiResponse != null) {
                consumeResponseRejectedOrder(apiResponse);
            }
        });

        myTaskViewModel.getStartStopTimerDetails().observe(activity, apiResponse -> {
            if (apiResponse != null) {
                startStopTimeResponse(apiResponse);
            }
        });

        if (type != null && type.equalsIgnoreCase("rejectAssign")) {
            mBinding.SearchBar.setVisibility(View.GONE);
            myTaskViewModel.hitRejectedOrderApi(assignmentID, SharePrefs.getInstance(activity).getString(SharePrefs.MOBILE));
        } else {
            callApi();
            mBinding.SearchBar.setVisibility(View.VISIBLE);
        }
      /*  mBinding.searchTxt.setOnClickListener(v -> {
            Fragment fragment = new SearchFragment();
            activity.switchContentWithStack(fragment);
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
                searchString = editable.toString().trim();
                if (searchString.length() == 0) {
                    searchOrderId = 0;
                    list.clear();
                    pageCount = 1;
                    callApi();
                }


            }
        });

        mBinding.searchIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (searchString.length() > 1) {
                    searchString = searchString.replaceAll("([a-z,A-Z])", "");
                    searchString = searchString.replaceAll("", "");
                    searchOrderId = Integer.valueOf(searchString);
                    list.clear();
                    pageCount = 1;
                    callApi();
                    Utils.HideKeyBoard(activity, mBinding.searchTxt);
                } else {
                    Toast.makeText(activity, "Enter OrderID First", Toast.LENGTH_SHORT).show();
                }

            }
        });

        mBinding.searchTxt.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            if (searchString.length() > 1) {
                                list.clear();
                                pageCount = 1;
                                callApi();
                            } else {
                                Toast.makeText(activity, "Enter SkCode or OrderID First", Toast.LENGTH_SHORT).show();
                            }
                            Utils.HideKeyBoard(activity, mBinding.searchTxt);
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        mBinding.rvMyTask.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    visibleItemCount = linearLayoutManager.getChildCount();
                    totalItemCount = linearLayoutManager.getItemCount();
                    pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();
                    if (loading) {
                        if (visibleItemCount + pastVisiblesItems >= totalItemCount) {
                            loading = false;
                            pageCount++;
                            mBinding.progressBid.setVisibility(View.VISIBLE);
                            callApi();
                        }
                    }
                }
            }
        });
        Utils.HideKeyBoard(activity, mBinding.searchTxt);
        mBinding.setPresenter(new Presenters() {
            @Override
            public void stopBtnClicked() {
                if (!isOrderinShipped) {
                    SubmitPopup();
                }
            }
        });
        mBinding.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                list.clear();
                adapter.notifyDataSetChanged();
                pageCount = 1;
                callApi();
                mBinding.swipeContainer.setRefreshing(false);
            }
        });
        return view;
    }

    private void intView() {
        ParsedList = new ArrayList<>();
        list = new ArrayList<>();
        mBinding.rvMyTask.setLayoutManager(linearLayoutManager);
        mBinding.rvMyTask.setHasFixedSize(true);
        myTaskViewModel = ViewModelProviders.of(this).get(MyTaskViewModel.class);
        adapter = new MyTaskAdapter(activity, list, type, time, this);
        mBinding.rvMyTask.setAdapter(adapter);
        mBinding.swipeContainer.setColorSchemeResources(R.color.colorAccent);

    }

    private void SubmitPopup() {
        final View mView = getLayoutInflater().inflate(R.layout.placeorder_popup, null);
        Dialog custom_Dialog = new Dialog(activity, R.style.CustomDialog);
        custom_Dialog.setContentView(mView);
        TextView title = mView.findViewById(R.id.pd_title);
        TextView okBtn = mView.findViewById(R.id.ok_btn);
        TextView cancelBtn = mView.findViewById(R.id.cancel_btn);
        title.setText(activity.getString(R.string.submitassignment));
        okBtn.setOnClickListener(v -> {
            callStopAssginmentApi();
            custom_Dialog.dismiss();

        });
        cancelBtn.setOnClickListener(v -> custom_Dialog.dismiss());
        custom_Dialog.show();

    }

    public void callApi() {
        myTaskViewModel.hitMyTaskApi(assignmentID, SharePrefs.getInstance(activity).getString(SharePrefs.MOBILE), pageCount, 10, searchOrderId);
    }

    public void callStopAssginmentApi() {
        if (!Utils.checkInternetConnection(activity)) {
            Utils.setToast(activity, getResources().getString(R.string.network_error));
        } else {
            CurrentEndTimeModel currentEndTimeModel = new CurrentEndTimeModel(assignmentID, SharePrefs.getInstance(activity).getInt(SharePrefs.PEOPLE_ID), false, "0", String.valueOf(Utils.getTemp(activity)), true);
            myTaskViewModel.hitPostTimerApi(currentEndTimeModel);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (type == null) {
            if (isRefresh) {
                isRefresh = false;
                pageCount = 1;
                list.clear();
                callApi();
            }

        } else {
            if (isRefresh) {
                isRefresh = false;
                myTaskViewModel.hitRejectedOrderApi(assignmentID, SharePrefs.getInstance(activity).getString(SharePrefs.MOBILE));
            }
        }
        setActionBarConfiguration();
        Utils.HideKeyBoard(activity, mBinding.searchTxt);
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

    private void consumeResponseRejectedOrder(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                mBinding.progressBid.setVisibility(View.VISIBLE);
                break;
            case SUCCESS:
                mBinding.progressBid.setVisibility(View.GONE);
                renderSuccessResponseRejectedOrder(apiResponse.data);
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
                try {
                    mBinding.swipeContainer.setRefreshing(false);
                    mBinding.progressBid.setVisibility(View.GONE);

                    if (pageCount == 1) {
                        list.clear();
                    }
                    JSONObject obj = new JSONObject(response.toString());
                    MyTaskModelMain myTaskModelMain = new Gson().fromJson(obj.toString(), MyTaskModelMain.class);
                    if (myTaskModelMain.getStatus()) {
                        for (MyTaskModel model : myTaskModelMain.getOrderDispatchedObj()) {
                            list.add(model);
                        }
                        tv_assignmentid.setText(activity.getString(R.string.assignment_id) + myTaskModelMain.getOrderDispatchedObj().get(0).getDeliveryIssuanceId());
                        double latitude = Utils.getDoubleLat(activity);
                        double longitutde =  Utils.getDoubleLag(activity);
                        System.out.println("Current  lat" + latitude);
                        System.out.println("Current  lag" + longitutde);
                        Location locationB = new Location("point B");
                        locationB.setLatitude(latitude);
                        locationB.setLongitude(longitutde);
                        float distance = 0f;
                        for (int i = 0; i < myTaskModelMain.getOrderDispatchedObj().size(); i++) {
                            lat = myTaskModelMain.getOrderDispatchedObj().get(i).getLat();
                            lg = myTaskModelMain.getOrderDispatchedObj().get(i).getLg();
                            if (lat != 0 && lg != 0) {
                                Location locationA = new Location("point A");
                                locationA.setLatitude(lat);
                                locationA.setLongitude(lg);
                                distance = locationB.distanceTo(locationA) / 1000;
                                System.out.println("Distance" + distance);
                                myTaskModelMain.getOrderDispatchedObj().get(i).setDistance(distance);
                            }
                        }
                        Collections.sort(list, (o1, o2) -> (int) (o1.getDistance() - o2.getDistance()));
                        adapter.setData(activity, list, type, time);
                        adapter.notifyDataSetChanged();
                        if (myTaskModelMain.getTotalOrderCount() != list.size()) {
                            loading = true;
                            if (!myTaskModelMain.isShippedAssingId()) {
                                isOrderinShipped = false;

                                if (!Utils.checkInternetConnection(activity)) {
                                    Utils.setToast(activity, getResources().getString(R.string.network_error));
                                } else {
                                    CurrentEndTimeModel currentEndTimeModel = new CurrentEndTimeModel(myTaskModelMain.getOrderDispatchedObj().get(0).getDeliveryIssuanceId(), SharePrefs.getInstance(activity).getInt(SharePrefs.PEOPLE_ID), false, "0", String.valueOf(Utils.getTemp(activity)), true);
                                    myTaskViewModel.hitPostTimerApi(currentEndTimeModel);
                                }
                                // mBinding.stopTimerBtn.setBackgroundColor(activity.getResources().getColor(R.color.colorAccent));
                            } else {
                                isOrderinShipped = true;
                                mBinding.stopTimerBtn.setBackgroundColor(activity.getResources().getColor(R.color.grey));
                            }

                        } else {
                            loading = false;
                            if (!myTaskModelMain.isShippedAssingId()) {
                                isOrderinShipped = false;
                                if (!Utils.checkInternetConnection(activity)) {
                                    Utils.setToast(activity, getResources().getString(R.string.network_error));
                                } else {
                                    CurrentEndTimeModel currentEndTimeModel = new CurrentEndTimeModel(myTaskModelMain.getOrderDispatchedObj().get(0).getDeliveryIssuanceId(), SharePrefs.getInstance(activity).getInt(SharePrefs.PEOPLE_ID), false, "0", String.valueOf(Utils.getTemp(activity)), true);
                                    // myTaskViewModel.hitPostTimerApi(currentEndTimeModel);
                                }
                                //  mBinding.stopTimerBtn.setBackgroundColor(activity.getResources().getColor(R.color.colorAccent));
                            } else {
                                isOrderinShipped = true;
                                mBinding.stopTimerBtn.setBackgroundColor(activity.getResources().getColor(R.color.grey));
                            }

                        }
                    } else {
                        Toast.makeText(activity, myTaskModelMain.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            Toast.makeText(activity, response.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void renderSuccessResponseRejectedOrder(JsonElement response) {
        if (Utils.isJSONValid(response.toString())) {
            if (!response.isJsonNull()) {
                try {
                    JSONObject obj = new JSONObject(response.toString());
                    MyTaskModelMain myTaskModelMain = new Gson().fromJson(obj.toString(), MyTaskModelMain.class);
                    if (myTaskModelMain.getStatus()) {
                        tv_assignmentid.setText(activity.getString(R.string.assignment_id) + myTaskModelMain.getOrderDispatchedObj().get(0).getDeliveryIssuanceId());
                        ArrayList<MyTaskModel> list = myTaskModelMain.getOrderDispatchedObj();
                        double latitude = Utils.getDoubleLat(activity);
                        double longitutde =  Utils.getDoubleLag(activity);
                        System.out.println("Current  lat" + latitude);
                        System.out.println("Current  lag" + longitutde);
                        Location locationB = new Location("point B");
                        locationB.setLatitude(latitude);
                        locationB.setLongitude(longitutde);
                        float distance = 0f;
                        for (int i = 0; i < myTaskModelMain.getOrderDispatchedObj().size(); i++) {
                            lat = myTaskModelMain.getOrderDispatchedObj().get(i).getLat();
                            lg = myTaskModelMain.getOrderDispatchedObj().get(i).getLg();
                            if (lat != 0 && lg != 0) {
                                Location locationA = new Location("point A");
                                locationA.setLatitude(lat);
                                locationA.setLongitude(lg);
                                distance = locationB.distanceTo(locationA) / 1000;
                                System.out.println("Distance" + distance);
                                myTaskModelMain.getOrderDispatchedObj().get(i).setDistance(distance);
                            }
                        }
                        Collections.sort(list, (o1, o2) -> (int) (o1.getDistance() - o2.getDistance()));
                        MyTaskAdapter adapter = new MyTaskAdapter(activity, list, type, "", this);
                        mBinding.rvMyTask.setAdapter(adapter);
                    } else {
                        activity.onBackPressed();
                        Toast.makeText(activity, myTaskModelMain.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                activity.onBackPressed();
            }
        } else {
            Toast.makeText(activity, response.toString(), Toast.LENGTH_SHORT).show();
            activity.onBackPressed();
        }
    }

    private void startStopTimeResponse(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                mBinding.progressBid.setVisibility(View.VISIBLE);
                break;
            case SUCCESS:
                mBinding.progressBid.setVisibility(View.GONE);
                postStartTimeResponseModel(apiResponse.data);
                break;
            case ERROR:
                mBinding.progressBid.setVisibility(View.GONE);
                Utils.setToast(activity, getResources().getString(R.string.errorString));
                break;
            default:
                break;
        }
    }

    private void postStartTimeResponseModel(JsonElement response) {
        try {
            if (Utils.isJSONValid(response.toString())) {
                if (!response.isJsonNull() && response != null) {
                    Logger.e(CommonMethods.getTag(this), response.toString());
                    JSONObject obj = new JSONObject(response.toString());
                    try {
                        if (obj.getBoolean("Status")) {
                            startActivity(new Intent(activity, MainActivity.class));
                        } else {
                            Toast.makeText(activity, obj.getString("Message"), Toast.LENGTH_SHORT).show();
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

    Runnable my_runnable = new Runnable() {
        @Override
        public void run() {
            // your code here
            long mills = Math.abs(millisUntilFinished);
            hms = String.format(FORMAT,
                    TimeUnit.MILLISECONDS.toHours(mills),
                    TimeUnit.MILLISECONDS.toMinutes(mills) - TimeUnit.HOURS.toMinutes(
                            TimeUnit.MILLISECONDS.toHours(mills)),
                    TimeUnit.MILLISECONDS.toSeconds(mills) - TimeUnit.MINUTES.toSeconds(
                            TimeUnit.MILLISECONDS.toMinutes(mills)));
           // activity.tvStartTime.setText("" + hms);
            millisUntilFinished += 1000;
            restart();
            // mHandler.postDelayed(this, 1000);
        }
    };

    // to start the handler
    public void start() {
        mHandler.postDelayed(my_runnable, 1000);
    }

    // to stop the handler
    public void stop() {
        mHandler.removeCallbacks(my_runnable);
        activity.tvStartTime.setText("00:00:00");
        //  activity.tvTimerAsd.setVisibility(View.GONE);
    }

    // to reset the handler
    public void restart() {
        mHandler.removeCallbacks(my_runnable);
        mHandler.postDelayed(my_runnable, 1000);
    }

    public void timer(String time, TextView timer) {
        try {
            long currMillis = System.currentTimeMillis();
            SimpleDateFormat sdf1 = new SimpleDateFormat(Utils.myFormat, Locale.getDefault());
            sdf1.setTimeZone(TimeZone.getDefault());
            if (time != null) {
                Date startTime = sdf1.parse(time);
                long startEpoch = startTime.getTime();
                millisUntilFinished = currMillis - startEpoch;
            }
            start();
            //  customRunnable =new CustomRunnableForAssignment(handler, timer, 1000);
            // customRunnable.holder = timer;
            // customRunnable.millisUntilFinished = millse; //Current time - received time
            //  handler.postDelayed(customRunnable, 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setActionBarConfiguration() {
        final DrawerLayout drawer = getActivity().findViewById(R.id.container);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        RelativeLayout layout = getActivity().findViewById(R.id.titlebar);
        layout.setVisibility(View.VISIBLE);
        LinearLayout linearLayout = getActivity().findViewById(R.id.ll_oder_id_view);
        linearLayout.setVisibility(View.GONE);
        TextView startTimerbtn = getActivity().findViewById(R.id.start_timer);
        TextView timer = activity.tvTimerAsd;
       activity.startBreak.setVisibility(View.GONE);
        TextView tv_timmer = getActivity().findViewById(R.id.tv_timmer);
        tv_timmer.setVisibility(View.GONE);
        activity.tvTimerAsd.setVisibility(View.GONE);
        tv_assignmentid = getActivity().findViewById(R.id.assignmentid);
        tv_assignmentid.setText("");
        TextView tittleTextView = getActivity().findViewById(R.id.toolbar_title);
        startTimerbtn.setVisibility(View.VISIBLE);
        startTimerbtn.setText("00:00:00");
        tittleTextView.setVisibility(View.GONE);
        tv_assignmentid.setVisibility(View.VISIBLE);
        if (time != null && !time.isEmpty())
            timer(time, startTimerbtn);

        toolbar.setNavigationOnClickListener(v -> {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                drawer.openDrawer(GravityCompat.START);
            }
        });
    }

    @Override
    public void onlineLayHideUnHideClicked(boolean param) {
        this.isRefresh = param;
    }
}