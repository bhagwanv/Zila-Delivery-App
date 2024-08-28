package com.sk.ziladelivery.ui.views.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
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
import com.google.gson.JsonObject;
import com.sk.ziladelivery.R;
import com.sk.ziladelivery.databinding.FragmentMultipleAssignmentBinding;
import com.sk.ziladelivery.listener.MultipleAssignmentInterface;
import com.sk.ziladelivery.data.model.AcceptedAssginmentListModel;
import com.sk.ziladelivery.data.model.AcceptedAssginmentModel;
import com.sk.ziladelivery.data.model.SortOrderModel;
import com.sk.ziladelivery.utilities.ApiResponse;
import com.sk.ziladelivery.utilities.ApiResponseObj;
import com.sk.ziladelivery.utilities.CommonMethods;
import com.sk.ziladelivery.utilities.Logger;
import com.sk.ziladelivery.utilities.SharePrefs;
import com.sk.ziladelivery.utilities.Utils;
import com.sk.ziladelivery.ui.views.viewmodels.AcceptedAssignmentViewModel;
import com.sk.ziladelivery.ui.views.adapter.RejectAssignmentAdapter;
import com.sk.ziladelivery.ui.views.main.MainActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import io.reactivex.disposables.CompositeDisposable;

public class RejectAssginmentFragment extends Fragment implements MultipleAssignmentInterface {
    private FragmentMultipleAssignmentBinding mBinding;
    private MainActivity activity;
    private RejectAssignmentAdapter adapter;
    private AcceptedAssignmentViewModel acceptedAssignmentViewModel;
    private int peopleId;
    private AcceptedAssginmentModel acceptedAssginmentModel;
    private ArrayList<AcceptedAssginmentListModel> assignmentAcceptPending;
    private MutableLiveData<ApiResponse> tokenLiveData;
    private CompositeDisposable disposablesa;
    private boolean isTimerStart = true;
    private String type = "";
    private boolean isRefresh = false;


    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        this.activity = (MainActivity) context;

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_multiple_assignment, container, false);
        initialization();
        acceptedAssignmentViewModel.getrejectedAssignment().observe(this, apiResponseObj -> {
            if (apiResponseObj != null) {
                consumeResponseRejectedAssignment(apiResponseObj);
            }
        });


        if (!Utils.checkInternetConnection(activity)) {
            Utils.setToast(activity, getResources().getString(R.string.network_error));
        } else {
            acceptedAssignmentViewModel.hitGetRejectedAssignment(peopleId);
        }


        return mBinding.getRoot();
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
        mBinding.rvMyTask.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mBinding.rvMyTask.setHasFixedSize(true);
        mBinding.SearchBar.setVisibility(View.GONE);
        adapter = new RejectAssignmentAdapter(activity, assignmentAcceptPending, this);


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
        tittleTextView.setText("Rejected Assignment");
        TextView starttimer = getActivity().findViewById(R.id.start_timer);
        activity.startBreak.setVisibility(View.GONE);
        starttimer.setVisibility(View.GONE);
        TextView timer = getActivity().findViewById(R.id.tv_timer);
        timer.setVisibility(View.GONE);
        TextView tvHistory = getActivity().findViewById(R.id.tv_history);
        tvHistory.setVisibility(View.GONE);
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

    }

    @Override
    public void Details(int deliveryIssuanceId, boolean isTimerstart, int i) {
        Bundle bundle = new Bundle();
        Fragment fragment = new AssignmentDetailFragment();
        bundle.putInt("deliveryIssuanceId", deliveryIssuanceId);
        bundle.putString("type", "rejectAssign");
        fragment.setArguments(bundle);
        activity.switchContentWithStack(fragment);
    }

    @Override
    public void shortPathData(SortOrderModel sortOrderModel, int postion, int id) {

    }
}