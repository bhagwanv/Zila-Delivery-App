package com.sk.ziladelivery.ui.views.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sk.ziladelivery.R;
import com.sk.ziladelivery.data.api.RestClient;
import com.sk.ziladelivery.databinding.FragmentMultipleAssignmentBinding;
import com.sk.ziladelivery.listener.MultipleAssignmentInterface;
import com.sk.ziladelivery.data.model.AcceptedAssginmentListModel;
import com.sk.ziladelivery.data.model.AcceptedAssginmentModel;
import com.sk.ziladelivery.data.model.CurrentEndTimeModel;
import com.sk.ziladelivery.data.model.SortOrderModel;
import com.sk.ziladelivery.data.model.SortedOrdersModel;
import com.sk.ziladelivery.data.model.SortedOrdersResponceModel;
import com.sk.ziladelivery.data.model.TokenResponse;
import com.sk.ziladelivery.utilities.ApiResponse;
import com.sk.ziladelivery.utilities.ApiResponseObj;
import com.sk.ziladelivery.utilities.CommonMethods;
import com.sk.ziladelivery.utilities.Logger;
import com.sk.ziladelivery.utilities.SharePrefs;
import com.sk.ziladelivery.utilities.TextUtils;
import com.sk.ziladelivery.utilities.Utils;
import com.sk.ziladelivery.ui.views.viewmodels.AcceptedAssignmentViewModel;
import com.sk.ziladelivery.ui.views.viewmodels.LoginViewModel;
import com.sk.ziladelivery.ui.views.adapter.MultipleAssignmentAdapter;
import com.sk.ziladelivery.ui.views.main.LoginActivity;
import com.sk.ziladelivery.ui.views.main.MainActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MultipleAssignmentFragment extends Fragment implements MultipleAssignmentInterface {
    private FragmentMultipleAssignmentBinding mBinding;
    private MainActivity activity;
    private MultipleAssignmentAdapter adapter;
    private AcceptedAssignmentViewModel acceptedAssignmentViewModel;
    private int peopleId,assginmentID;
    private AcceptedAssginmentModel acceptedAssginmentModel;
    private ArrayList<AcceptedAssginmentListModel> assignmentAcceptPending;
    private MutableLiveData<ApiResponse> tokenLiveData;
    private CompositeDisposable disposablesa;
    private boolean isTimerStart = true;
    private String type = "";
    private  Bundle bundle;

    public MultipleAssignmentFragment() {

    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        this.activity = (MainActivity) context;

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_multiple_assignment, container, false);
        bundle = this.getArguments();

        initialization();


        acceptedAssignmentViewModel.getmapRoute().observe(activity, apiResponse -> {
            if (apiResponse != null) {
                consumeResponse(apiResponse);
            }
        });

        acceptedAssignmentViewModel.getacceptedAssignment().observe(activity, apiResponseObj -> {
            if (apiResponseObj != null) {
                consumeResponse(apiResponseObj);
            }
        });

        acceptedAssignmentViewModel.getrejectedAssignment().observe(activity, apiResponseObj -> {
            if (apiResponseObj != null) {
                consumeResponseRejectedAssignment(apiResponseObj);
            }
        });

        acceptedAssignmentViewModel.getpostStartTime().observe(activity, apiResponse -> {
            if (apiResponse != null) {
                startStopTimeResponse(apiResponse);
            }
        });

        acceptedAssignmentViewModel.getshortPath().observe(activity, apiResponse -> {
            if (apiResponse != null) {
                shortedPathResponse(apiResponse);
            }

        });
        return mBinding.getRoot();
    }
    private void consumeResponse(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                break;
            case SUCCESS:
                renderSuccessResponse(apiResponse.data);
                break;
            case ERROR:
                Utils.setToast(getContext(), getResources().getString(R.string.errorString));
                break;
            default:
                break;
        }
    }

    private void renderSuccessResponse(JsonElement response) {
        if (Utils.isJSONValid(response.toString())) {
            if (!response.isJsonNull()) {
                Logger.d(CommonMethods.getTag(this), response.toString());
                try {

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getContext(), getResources().getString(R.string.errorString), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), response.toString(), Toast.LENGTH_SHORT).show();
        }
    }
    private void consumeResponse(ApiResponseObj apiResponseObj) {
        try {
            switch (apiResponseObj.status) {
                case LOADING:
                    mBinding.progressBid.setVisibility(View.VISIBLE);
                    break;
                case SUCCESS:
                    mBinding.progressBid.setVisibility(View.GONE);
                    renderSuccessResponse(apiResponseObj.data);
                    break;
                case ERROR:
                    mBinding.progressBid.setVisibility(View.GONE);
                    Utils.setToast(activity, getResources().getString(R.string.errorString));
                    if (apiResponseObj.error.toString().contains("401")) {
                        if (TextUtils.isNullOrEmpty(SharePrefs.getInstance(activity).getString(SharePrefs.TOKEN_NAME))) {
                            PreferenceManager.getDefaultSharedPreferences(activity).edit().clear().apply();
                            SharePrefs.getInstance(activity).putBoolean(SharePrefs.LOGGED, false);
                            Intent intent = new Intent(activity, LoginActivity.class);
                            intent.putExtra("Type", "ResetPasswordActivity");
                            startActivity(intent);
                            activity.finish();
                        } else {
                            CheckToken();
                        }
                    }
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void consumeResponseRejectedAssignment(ApiResponseObj apiResponseObj) {
        try {
            switch (apiResponseObj.status) {
                case LOADING:
                    mBinding.progressBid.setVisibility(View.VISIBLE);
                    break;
                case SUCCESS:
                    mBinding.progressBid.setVisibility(View.GONE);
                    renderSuccessResponseRejectedAssignment(apiResponseObj.data);
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

    public void CheckToken() {
        String username = SharePrefs.getInstance(activity).getString(SharePrefs.TOKEN_NAME);
        String Password = SharePrefs.getInstance(activity).getString(SharePrefs.TOKEN_PASSWORD);
        hitTokenAPI("password", username, Password);
    }



    public void hitTokenAPI(String password, String username, String Password) {
        DisposableObserver<JsonObject> observerDes = RestClient.getInstance().getService().getToken(password, username, Password)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> tokenLiveData.setValue(ApiResponse.loading()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonObject>() {
                    @Override
                    public void onNext(JsonObject result) {
                        if (Utils.isJSONValid(result.toString())) {
                            if (!result.isJsonNull()) {
                                Log.d("response=", result.toString());
                                try {
                                    JSONObject obj = new JSONObject(result.toString());
                                    TokenResponse tokenResponse = new Gson().fromJson(obj.toString(), TokenResponse.class);
                                    if (tokenResponse != null) {
                                        SharePrefs.getInstance(activity).putString(SharePrefs.TOKEN, tokenResponse.getAccess_token());
                                        System.out.println("Token Save");
                                        startActivity(new Intent(activity, MainActivity.class));
                                        activity.finish();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Toast.makeText(activity, getResources().getString(R.string.errorString), Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(activity, result.toString(), Toast.LENGTH_SHORT).show();
                        }                    }

                    @Override
                    public void onError(Throwable throwable) {
                        tokenLiveData.setValue(ApiResponse.error(throwable));
                    }

                    @Override

                    public void onComplete() {

                    }
                });
        disposablesa.add(observerDes);
    }



    private void renderSuccessResponse(JsonObject response) {
        try {
            mBinding.swipeContainer.setRefreshing(false);
            if (Utils.isJSONValid(response.toString())) {
                assignmentAcceptPending.clear();
                if (!response.isJsonNull() && response != null) {
                    Logger.e(CommonMethods.getTag(this), response.toString());
                    try {
                        mBinding.tvMyTask.setVisibility(View.GONE);
                        mBinding.rvMyTask.setVisibility(View.VISIBLE);
                        acceptedAssginmentModel = new Gson().fromJson(response.toString(), AcceptedAssginmentModel.class);
                        if (acceptedAssginmentModel.getStatus()) {
                            for (int i = 0; i < acceptedAssginmentModel.getAssignmentAcceptPending().size(); i++) {
                                assignmentAcceptPending.add(acceptedAssginmentModel.getAssignmentAcceptPending().get(i));
                            }
                            adapter.setItemListCategory(assignmentAcceptPending, this);
                            mBinding.rvMyTask.setAdapter(adapter);

                            // adapter.notifyDataSetChanged();
                        } else {
                            mBinding.tvMyTask.setVisibility(View.VISIBLE);
                            mBinding.rvMyTask.setVisibility(View.GONE);
                            Toast.makeText(activity, acceptedAssginmentModel.getMessage(), Toast.LENGTH_SHORT).show();
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
  private void renderSuccessResponseRejectedAssignment(JsonObject response) {
      try {
          if (Utils.isJSONValid(response.toString())) {
              assignmentAcceptPending.clear();
              if (!response.isJsonNull() && response != null) {
                  Logger.e(CommonMethods.getTag(this), response.toString());
                  try {
                      mBinding.tvMyTask.setVisibility(View.GONE);
                      mBinding.rvMyTask.setVisibility(View.VISIBLE);
                      acceptedAssginmentModel = new Gson().fromJson(response.toString(), AcceptedAssginmentModel.class);
                      if (acceptedAssginmentModel.getStatus()) {
                          for (int i = 0; i < acceptedAssginmentModel.getAssignmentAcceptPending().size(); i++) {
                              assignmentAcceptPending.add(acceptedAssginmentModel.getAssignmentAcceptPending().get(i));
                          }
                          adapter.setItemListCategory(assignmentAcceptPending, this);
                          mBinding.rvMyTask.setAdapter(adapter);

                          // adapter.notifyDataSetChanged();
                      } else {
                          mBinding.tvMyTask.setVisibility(View.VISIBLE);
                          mBinding.rvMyTask.setVisibility(View.GONE);
                          Toast.makeText(activity, acceptedAssginmentModel.getMessage(), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.rvMyTask.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private void initialization() {
        setActionBarConfiguration();
        disposablesa = new CompositeDisposable();
        assignmentAcceptPending = new ArrayList<>();
        peopleId = SharePrefs.getInstance(getActivity()).getInt(SharePrefs.PEOPLE_ID);
        acceptedAssignmentViewModel = ViewModelProviders.of(activity).get(AcceptedAssignmentViewModel.class);
        LoginViewModel loginViewModel = ViewModelProviders.of(activity).get(LoginViewModel.class);
        mBinding.rvMyTask.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mBinding.rvMyTask.setHasFixedSize(true);
        adapter = new MultipleAssignmentAdapter(activity, assignmentAcceptPending, this);
        mBinding.rvMyTask.setAdapter(adapter);
        mBinding.searchTxt.setOnClickListener(v -> {
            Fragment fragment = new SearchFragment();
            activity.switchContentWithStack(fragment);
        });
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mBinding.searchTxt.getWindowToken(), 0);
        mBinding.swipeContainer.setOnRefreshListener(this::CallAPI );


    }
    private void CallAPI(){

        if (!Utils.checkInternetConnection(activity)) {
            Utils.setToast(activity, getResources().getString(R.string.network_error));
        } else {
            acceptedAssignmentViewModel.hitAcceptedAssignmentApi(peopleId);
        }
    }

    private void setActionBarConfiguration() {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        //actionBar.setLogo(R.drawable.ic_action_bar_logo);
        final DrawerLayout drawer = getActivity().findViewById(R.id.container);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        TextView tittleTextView = getActivity().findViewById(R.id.toolbar_title);
        TextView assignmentid = getActivity().findViewById(R.id.assignmentid);
        assignmentid.setVisibility(View.GONE);
        tittleTextView.setVisibility(View.VISIBLE);
        tittleTextView.setText(activity.getString(R.string.my_task));
        TextView starttimer = getActivity().findViewById(R.id.start_timer);
        starttimer.setVisibility(View.GONE);
        TextView timer = getActivity().findViewById(R.id.tv_timer);
        timer.setVisibility(View.GONE);
        toolbar.setNavigationOnClickListener(v -> {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        ((MainActivity) getActivity()).toggle.syncState();
    }

    @Override
    public void StartTimer(int deliveryIssuanceId) {
        if (isTimerStart) {
            if (!Utils.checkInternetConnection(activity)) {
                Utils.setToast(activity, getString(R.string.network_error));
            } else {
                CurrentEndTimeModel currentEndTimeModel = new CurrentEndTimeModel(deliveryIssuanceId, SharePrefs.getInstance(activity).getInt(SharePrefs.PEOPLE_ID), true, String.valueOf(Utils.getTemp(activity)), "0", false);
                acceptedAssignmentViewModel.hitPostTimerApi(currentEndTimeModel);
                isTimerStart = false;
            }
        }
    }

    @Override
    public void Details(int deliveryIssuanceId, boolean isTimerstart, int position) {
        String startTime = "";
        if (isTimerstart) {
            for (int i = 0; i < acceptedAssginmentModel.getAssignmentAcceptPending().size(); i++) {
                if (i == position) {
                    startTime = acceptedAssginmentModel.getAssignmentAcceptPending().get(i).getStartDateTime();
                    break;
                }
            }
            Bundle bundle = new Bundle();
            Fragment fragment = new AssignmentDetailFragment();
            bundle.putString("time", startTime);
            bundle.putInt("deliveryIssuanceId", deliveryIssuanceId);
            fragment.setArguments(bundle);
            activity.switchContentWithStack(fragment);
        } else {
            alertDialogStop();
        }
    }

    @Override
    public void shortPathData(SortOrderModel sortOrderModel, int postion,int assginId) {
        assginmentID=assginId;
        if (!Utils.checkInternetConnection(activity)) {
            Utils.setToast(activity, getResources().getString(R.string.network_error));
        } else {
            acceptedAssignmentViewModel.hitShortPathApi(sortOrderModel);
        }
    }

    private void alertDialogStop() {
        LayoutInflater inflater = activity.getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.layout_custom_dialog_stop, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setView(alertLayout);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(false);
        final AlertDialog dialog = alert.create();
        dialog.show();

        final Button ok = alertLayout.findViewById(R.id.ok);
        final TextView tvWarningtxt = alertLayout.findViewById(R.id.tv_warningtxt);
        tvWarningtxt.setText(R.string.pressstartButton);
        ok.setOnClickListener(view -> dialog.dismiss());
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
                isTimerStart = true;
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

    private void shortedPathResponse(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                mBinding.progressBid.setVisibility(View.VISIBLE);
                break;
            case SUCCESS:
                mBinding.progressBid.setVisibility(View.GONE);
                shortedPathModelResponce(apiResponse.data);
                break;
            case ERROR:
                mBinding.progressBid.setVisibility(View.GONE);
                Utils.setToast(activity, getResources().getString(R.string.errorString));
                break;
            default:
                break;
        }
    }

    private void shortedPathModelResponce(JsonElement response) {
        try {
            if (Utils.isJSONValid(response.toString())) {
                if (!response.isJsonNull() && response != null) {
                    Logger.e(CommonMethods.getTag(this), response.toString());
                    JSONObject obj = new JSONObject(response.toString());
                    SortedOrdersResponceModel ordersResponseModel = new Gson().fromJson(obj.toString(), SortedOrdersResponceModel.class);
                    if (ordersResponseModel .getSortedOrdersModels().size()>0) {
                        ArrayList<SortedOrdersModel> sortedOrdersModel = ordersResponseModel.getSortedOrdersModels();
                        /*Intent intent = new Intent(getActivity(), DrivingDirectionActivity.class);
                        intent.putExtra("SortedOrdersModel", sortedOrdersModel);
                        intent.putExtra("assginmentID", assginmentID);
                        startActivity(intent);*/
                  /*      acceptedAssignmentViewModel.hitRouteMap(assginmentID);
                        GPSTracker gpsTracker = new GPSTracker(activity);
                        double latitude = gpsTracker.getLatitude();
                        double longitutde = gpsTracker.getLongitude();
                        String str_origin = "&origin=" + latitude + "," + longitutde;
                        String str_dest = "&destination=" + sortedOrdersModel.get(sortedOrdersModel.size() - 1).getLat() + "," + sortedOrdersModel.get(sortedOrdersModel.size() - 1).getLng();
                        String wayPoints = "";
                        String sensor = "sensor=false";
                        String output = "json";
                        for (int j = 0; j < sortedOrdersModel.size() - 1; j++) {
                            SortedOrdersModel point = (SortedOrdersModel) sortedOrdersModel.get(j);
                           *//* wayPoints = wayPoints + (wayPoints.equals("") ? "" : "%7C") + sortedOrdersModel.get(j).getLat() + "," + sortedOrdersModel.get(j).getLng();*//*
                            wayPoints += point.getLat() + "," + point.getLng() + "|";
                        }
                        wayPoints = "&waypoints=" + wayPoints;
                        String parameters = str_origin + "&" + str_dest + "&"  + sensor + "&" + wayPoints;
                        String link="https://www.google.com/maps/dir/?api=1"+parameters+"&travelmode=driving";
                          System.out.println("link  "+link);
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                          startActivity(intent);*/
                    }
                    else{
                        Utils.setToast(activity,"Data Not Found");
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

    @Override
    public void onResume() {
        super.onResume();
        if (bundle != null) {
            type = bundle.getString("Type");
            if (type.equalsIgnoreCase("RejectAssignment")){
                if (!Utils.checkInternetConnection(activity)) {
                    Utils.setToast(activity, getResources().getString(R.string.network_error));
                } else {
                    acceptedAssignmentViewModel.hitGetRejectedAssignment(peopleId);
                }
            }
            else{

                CallAPI();
            }
        }
        else{
            if (!Utils.checkInternetConnection(activity)) {
                Utils.setToast(activity, getResources().getString(R.string.network_error));
            } else {
                acceptedAssignmentViewModel.hitAcceptedAssignmentApi(peopleId);
            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}