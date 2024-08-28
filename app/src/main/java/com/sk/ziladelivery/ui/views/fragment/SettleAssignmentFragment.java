package com.sk.ziladelivery.ui.views.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.sk.ziladelivery.R;
import com.sk.ziladelivery.databinding.SettleAssignmentBinding;
import com.sk.ziladelivery.data.model.AssignmentIdDataModel;
import com.sk.ziladelivery.utilities.ApiResponse;
import com.sk.ziladelivery.utilities.SharePrefs;
import com.sk.ziladelivery.utilities.Utils;
import com.sk.ziladelivery.ui.views.viewmodels.SettleAssignmentViewModel;
import com.sk.ziladelivery.ui.views.main.MainActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SettleAssignmentFragment extends Fragment {
    private SettleAssignmentBinding mBinding;
    private AssignmentIdDataModel assignmentIdDataModel;

    private ArrayList<String> AssignmentIDArray = new ArrayList<>();
    private int peopleId;
    private int warehouseId;
    private String selectedAssignmentid;
    private MainActivity activity;

    public SettleAssignmentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.settle_assignment, container, false);
        // init
        initView();
        if (!Utils.checkInternetConnection(activity)) {
            Utils.setToast(activity, getResources().getString(R.string.network_error));
        } else {
            SettleAssignmentViewModel settleAssignmentViewModel = ViewModelProviders.of(this).get(SettleAssignmentViewModel.class);
            mBinding.setLifecycleOwner(this);
            settleAssignmentViewModel.getAssignmentIDResponse().observe(getViewLifecycleOwner(), apiResponse -> {
                if (apiResponse != null) {
                    consumeResponse(apiResponse);
                }
            });
            mBinding.proRelatedItem.setVisibility(View.VISIBLE);
            settleAssignmentViewModel.hitAssignmentId(peopleId, warehouseId);
            mBinding.spAssignmentList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        selectedAssignmentid = AssignmentIDArray.get(position);
                        if (position != 0) {
                            System.out.println("selectedAssignmentid" + selectedAssignmentid);
                            Gson gson = new Gson();
                            SharePrefs.setCashmanagmentSharedPreference(activity, SharePrefs.ASSIGN_MODEL, gson.toJson(assignmentIdDataModel.getDeliveryIssuanceDcs()));
                            SharePrefs.getInstance(activity).putString(SharePrefs.ASSIGN_ID, selectedAssignmentid);
                            SharePrefs.getInstance(activity).putBoolean(SharePrefs.FLAG, true);
                            Fragment fragment = new AssignmentCashManagment();
                            activity.switchContentWithStack(fragment);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setActionBarConfiguration();

    }

    @Override
    public void onResume() {
        super.onResume();
        SharePrefs.setCashmanagmentSharedPreference(activity, SharePrefs.ASSIGN_MODEL, null);
        SharePrefs.getInstance(activity).putString(SharePrefs.ASSIGN_ID, null);
        SharePrefs.setCashmanagmentSharedPreference(activity, SharePrefs.CASH_AMOUNT_JSON, null);
        SharePrefs.setCashmanagmentSharedPreference(activity, SharePrefs.EDIT_AMOUNT_JSON, null);
        SharePrefs.setCashmanagmentSharedPreference(activity, SharePrefs.CHEQUE_AMOUNT, null);
        SharePrefs.setCashmanagmentSharedPreference(activity, SharePrefs.ONLINE_AMOUNT, null);
        SharePrefs.getInstance(activity).putInt(SharePrefs.CASH_AMOUNT, 0);
        SharePrefs.getInstance(activity).putBoolean(SharePrefs.FLAG, true);
        SharePrefs.getInstance(activity).putBoolean(SharePrefs.UPDATE_DATA, false);
    }


    private void initView() {
        peopleId = SharePrefs.getInstance(activity).getInt(SharePrefs.PEOPLE_ID);
        warehouseId = SharePrefs.getInstance(activity).getInt(SharePrefs.WAREHOUSE_ID);
    }

    private void consumeResponse(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                mBinding.proRelatedItem.setVisibility(View.VISIBLE);
                break;
            case SUCCESS:
                mBinding.proRelatedItem.setVisibility(View.GONE);
                renderSuccessResponse(apiResponse.data);
                break;
            case ERROR:
                mBinding.proRelatedItem.setVisibility(View.GONE);
                Utils.setToast(activity, getResources().getString(R.string.errorString));
                break;
            default:
                break;
        }
    }

    private void renderSuccessResponse(JsonElement response) {
        if (Utils.isJSONValid(response.toString())) {
            if (!response.isJsonNull()) {
                mBinding.proRelatedItem.setVisibility(View.GONE);
                AssignmentIDArray.clear();
                Log.d("response=", response.toString());
                try {
                    JSONObject obj = new JSONObject(response.toString());
                    assignmentIdDataModel = new Gson().fromJson(obj.toString(), AssignmentIdDataModel.class);
                    if (assignmentIdDataModel.getStatus()) {
                        AssignmentIDArray.add(getContext().getResources().getString(R.string.select_assignment));
                        for (int i = 0; i < assignmentIdDataModel.getDeliveryIssuanceDcs().size(); i++) {
                            String AssignmentID = String.valueOf(assignmentIdDataModel.getDeliveryIssuanceDcs().get(i).getDeliveryIssuanceId());
                            AssignmentIDArray.add(AssignmentID);
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, AssignmentIDArray);
                        mBinding.spAssignmentList.setAdapter(adapter);
                    } else {
                        Toast.makeText(activity, assignmentIdDataModel.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void setActionBarConfiguration() {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        //actionBar.setLogo(R.drawable.ic_action_bar_logo);
        final DrawerLayout drawer = getActivity().findViewById(R.id.container);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        TextView tittleTextView = getActivity().findViewById(R.id.toolbar_title);
        TextView starttimer = getActivity().findViewById(R.id.start_timer);
        TextView tv_assignmentid = getActivity().findViewById(R.id.assignmentid);
        tv_assignmentid.setVisibility(View.GONE);
        TextView timer = getActivity().findViewById(R.id.tv_timer);
        timer.setVisibility(View.GONE);
        TextView tvHistory = getActivity().findViewById(R.id.tv_history);
        starttimer.setVisibility(View.GONE);
        tvHistory.setVisibility(View.VISIBLE);
        tvHistory.setText(R.string.history);
        activity.startBreak.setVisibility(View.GONE);
        tittleTextView.setVisibility(View.VISIBLE);
        tittleTextView.setBackgroundResource(0);
        tittleTextView.setClickable(false);
        tittleTextView.setFocusable(false);
        tittleTextView.setText(R.string.settle_assignment);
        ImageView menu = getActivity().findViewById(R.id.drawer_menu);
        menu.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_menu_three_dot));
        starttimer.setEnabled(true);


        tvHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.switchContentWithStack(new CashManagmentHistory());
                SharePrefs.getInstance(activity).putBoolean(SharePrefs.FLAG, false);

            }
        });
        toolbar.setNavigationOnClickListener(v -> {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        ((MainActivity) getActivity()).toggle.syncState();
    }
}