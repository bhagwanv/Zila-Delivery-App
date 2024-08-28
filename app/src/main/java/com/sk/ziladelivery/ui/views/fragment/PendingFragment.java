package com.sk.ziladelivery.ui.views.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.sk.ziladelivery.R;
import com.sk.ziladelivery.data.model.AcceptModel;
import com.sk.ziladelivery.data.model.CancellationModel;
import com.sk.ziladelivery.data.model.PendingTaskModel;
import com.sk.ziladelivery.databinding.FragmentPenddingBinding;
import com.sk.ziladelivery.databinding.RejectResonPopupBinding;
import com.sk.ziladelivery.listener.AcceptClickInterface;
import com.sk.ziladelivery.ui.views.main.MainActivity;
import com.sk.ziladelivery.ui.views.viewmodels.PendingTaskViewModel;
import com.sk.ziladelivery.utilities.ApiResponse;
import com.sk.ziladelivery.utilities.CommonMethods;
import com.sk.ziladelivery.utilities.GPSTracker;
import com.sk.ziladelivery.utilities.Logger;
import com.sk.ziladelivery.utilities.SharePrefs;
import com.sk.ziladelivery.utilities.Utils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class PendingFragment extends Fragment implements AcceptClickInterface {
    private FragmentPenddingBinding mBinding;

    private MainActivity activity;
    private RecyclerView rvPendingRackRV;
    private PendingTaskViewModel pendingTaskViewModel;
    private Dialog customDialog;
    private boolean ARFlag;
    private ProgressDialog dialog;
    private PendingTaskModel pendingTaskModel;
    private CancellationModel cancellationModel;
    private int distanceCal = 5000;
    TextView starttimer;


    public PendingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_pendding, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setData();
    }


    private void initView() {
        rvPendingRackRV = mBinding.rvPenddingTask;
        dialog = new ProgressDialog(getActivity());
        rvPendingRackRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvPendingRackRV.setHasFixedSize(true);
        starttimer = getActivity().findViewById(R.id.start_timer);
        starttimer.setVisibility(View.VISIBLE);
    }

    private void setData() {
        pendingTaskViewModel = ViewModelProviders.of(this).get(PendingTaskViewModel.class);
        mBinding.setPendingTaskViewModel(pendingTaskViewModel);
        mBinding.setLifecycleOwner(this);
        initView();
        pendingTaskViewModel.getPendingTaskData().observe(getActivity(), apiResponse -> consumeResponse(apiResponse));
        pendingTaskViewModel.getAcceptMyTaskData().observe(getActivity(), apiResponse -> AcceptRejectconsumeResponse(apiResponse));
        pendingTaskViewModel.getAcceptAssignmentDistanceData().observe(getActivity(), apiResponse -> acceptAssignmentDistanceConsumeResponse(apiResponse));
        pendingTaskViewModel.getCancellationData().observe(getActivity(), apiResponse -> cancellationconsumeResponse(apiResponse));
        /***
         * Pending Task API call
         * **/
        if (!Utils.checkInternetConnection(getActivity())) {
            Utils.setToast(getActivity(), getResources().getString(R.string.network_error));
        } else {
            pendingTaskViewModel.pendingMyTaskAdi(SharePrefs.getInstance(getActivity()).getInt(SharePrefs.PEOPLE_ID));
            pendingTaskViewModel.CancellationAssignment(SharePrefs.getInstance(getActivity()).getInt(SharePrefs.WAREHOUSE_ID), SharePrefs.getInstance(getActivity()).getString(SharePrefs.MOBILE));        }
        /***
         * Accept response
         * **/

     /*   pendingTaskViewModel.getAcceptMyTaskData().observe(this, value -> {
            mBinding.proRelatedItem.setVisibility(View.GONE);
            if (value) {
                if (ARFlag) {
                    Utils.setToast(getActivity(), getString(R.string.accept));
                } else {
                    Utils.setToast(getActivity(), getString(R.string.reject));
                }
                startActivity(new Intent(getActivity(), MainActivity.class));
                if (!Utils.checkInternetConnection(getActivity())) {
                    Utils.setToast(getActivity(), getResources().getString(R.string.network_error));
                } else {
                    pendingTaskViewModel.pendingMyTaskAdi(SharePrefs.getInstance(getActivity()).getInt(SharePrefs.PEOPLE_ID));
                }
            } else {
                Utils.setToast(getActivity(), getString(R.string.Error));
            }
        });*/
    }

    /*
     * method to handle response
     * */
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
                Utils.setToast(getActivity(), getResources().getString(R.string.errorString));
                break;

            default:
                break;
        }
    }


    private void AcceptRejectconsumeResponse(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                dialog.setMessage("Doing something, please wait.");
                dialog.show();
                break;
            case SUCCESS:
                dialog.dismiss();
                AcceptRejectrenderSuccessResponse(apiResponse.data);
                break;

            case ERROR:
                dialog.dismiss();
                Utils.setToast(getActivity(), getResources().getString(R.string.errorString));
                break;

            default:
                break;
        }
    }

    private void acceptAssignmentDistanceConsumeResponse(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                dialog.setMessage("Doing something, please wait.");
                dialog.show();
                break;
            case SUCCESS:
                dialog.dismiss();
                acceptAssignmentDistanceConsumeResponse(apiResponse.data);
                break;
            case ERROR:
                dialog.dismiss();
                Utils.setToast(getActivity(), getResources().getString(R.string.errorString));
                break;
            default:
                break;
        }
    }



    private void CancellationRenderSuccessResponse(JsonElement response) {
        JSONObject obj = null;
        try {
            if(response!=null){
                obj = new JSONObject(response.toString());
                cancellationModel = new Gson().fromJson(obj.toString(), CancellationModel.class);
                mBinding.cancelAmt.setText("₹"+cancellationModel.getCancelamount()+"");
                mBinding.cancelamtDiff.setText(String.format("%.2f", cancellationModel.getCompareamountpercent())+" %\n"+"("+cancellationModel.getCancelamountdiff()+")");
                mBinding.tvCancelcountDiff.setText(String.format("%.2f", cancellationModel.getComparecountpercent())+" %\n"+"("+cancellationModel.getCancelcountdiff()+")");
                mBinding.tvCancelcount.setText(cancellationModel.getCancelcount()+"");mBinding.tvCancellationper.setText(cancellationModel.getCancellationpercant()+"%");
                mBinding.tvComparecancelation.setText(cancellationModel.getComparecancellationpercant()+"%");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    starttimer.setText("YES" + "(" + cancellationModel.getWarningCount() + ")");
                    starttimer.setTextColor(Color.BLACK);
                    starttimer.setBackgroundColor(Color.parseColor(cancellationModel.getBackgroundcolor()));
                }



                if(cancellationModel.getComparecancellationpercant()>0){
                    mBinding.ivCancellation.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_down_arrow_green));
                }
                else{
                    mBinding.ivCancellation.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_down_arrow_red));
                }
                if(cancellationModel.getComparecountpercent()>0){
                    mBinding.ivCancelcount.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_down_arrow_green));
                }
                else{
                    mBinding.ivCancelcount.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_down_arrow_red));
                }
                if(cancellationModel.getCompareamountpercent()>0){
                    mBinding.ivCancelamt.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_down_arrow_green));
                }
                else{
                    mBinding.ivCancelamt.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_down_arrow_red));
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void cancellationconsumeResponse(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                dialog.setMessage("Doing something, please wait.");
                dialog.show();
                break;
            case SUCCESS:
                dialog.dismiss();
                CancellationRenderSuccessResponse(apiResponse.data);
                break;

            case ERROR:
                dialog.dismiss();
                Utils.setToast(getActivity(), getResources().getString(R.string.errorString));
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
                mBinding.proRelatedItem.setVisibility(View.GONE);

                Logger.e(CommonMethods.getTag(this), response.toString());
                try {
                    JSONObject obj = new JSONObject(response.toString());
                    pendingTaskModel = new Gson().fromJson(obj.toString(), PendingTaskModel.class);
                    if (pendingTaskModel.isStatus()) {
                        mBinding.rvPenddingTask.setVisibility(View.VISIBLE);
                    } else {
                        // Utils.setToast(getActivity(), getResources().getString(R.string.no_data_error));
                        mBinding.tvMyTask.setVisibility(View.VISIBLE);
                        mBinding.rvPenddingTask.setVisibility(View.GONE);
                    }

                } catch (JSONException e) {
                    Logger.e(CommonMethods.getTag(this), e.toString());
                }
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.errorString), Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();

        }
    }

    private void AcceptRejectrenderSuccessResponse(JsonElement response) {
        if (response != null && response.getAsJsonObject().get("Status").getAsBoolean()) {
            Toast.makeText(getActivity(), response.getAsJsonObject().get("Message").getAsString(), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getActivity(), MainActivity.class));

        } else {
            Toast.makeText(getActivity(), response.getAsJsonObject().get("Message").getAsString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void acceptAssignmentDistanceConsumeResponse(JsonElement response) {
        if (response != null) {
            System.out.println("des" + response);
            distanceCal = response.getAsInt();
        }
    }


    @Override
    public void acceptClicked(int deliveryIssuanceId, String arValue) {
        /***
         * Accept API call
         * **/
        GPSTracker gpsTracker = new GPSTracker(activity);
        double distance = 0.0;
        for (int i = 0; i < pendingTaskModel.getDI().size(); i++) {
            if (deliveryIssuanceId == pendingTaskModel.getDI().get(i).getDeliveryIssuanceId()) {
                distance = Math.round(Utils.Distance(gpsTracker.getLatitude(), gpsTracker.getLongitude(), pendingTaskModel.getDI().get(i).getLatitude(), pendingTaskModel.getDI().get(i).getLongitude()));
                break;
            }
        }
       // if (BuildConfig.DEBUG) {

        //} else {
            /*System.out.println("distance"+distance);
            if (distance <= 200) {
                ARFlag = true;
                if (!Utils.checkInternetConnection(getActivity())) {
                    Utils.setToast(getActivity(), getResources().getString(R.string.network_error));
                } else {
                    AcceptModel acceptModel = new AcceptModel(deliveryIssuanceId, arValue, "");
                    pendingTaskViewModel.AcceptPendingMyTaskAdi(acceptModel);
                }
            } else {
                Utils.setToast(getActivity(), "You are not  to allowed Accept Assginment at this Location");
            }*/
       // }
        ARFlag = true;
        if (!Utils.checkInternetConnection(getActivity())) {
            Utils.setToast(getActivity(), getResources().getString(R.string.network_error));
        } else {
            AcceptModel acceptModel = new AcceptModel(deliveryIssuanceId, arValue, "");
            pendingTaskViewModel.AcceptPendingMyTaskAdi(acceptModel);
        }
    }

    @Override

    public void rejectClicked(int deliveryIssuanceId, String aTrue) {
        RejectPopup(deliveryIssuanceId, aTrue);
    }


    /**
     * Reject popup
     *
     * @param deliveryIssuanceId
     * @param aTrue
     */
    private void RejectPopup(int deliveryIssuanceId, String aTrue) {
        RejectResonPopupBinding rejectResonPopupBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.reject_reson_popup, null, false);
        customDialog = new Dialog(getActivity(), R.style.CustomDialog);
        customDialog.setContentView(rejectResonPopupBinding.getRoot());
        Button submit = rejectResonPopupBinding.submit;
        ImageView dissmiss = rejectResonPopupBinding.dissmiss;
        Spinner spReason = rejectResonPopupBinding.spReason;
        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.reason, android.R.layout.simple_spinner_dropdown_item);
        spReason.setAdapter(statusAdapter);
        submit.setOnClickListener(v -> {
            String sReason = spReason.getSelectedItem().toString();
            // Accept API call
            if (sReason.equalsIgnoreCase("Select Reason")) {
                Utils.setToast(getActivity(), getResources().getString(R.string.plz_select_reason));
            } else {
                customDialog.dismiss();
                ARFlag = false;
                if (!Utils.checkInternetConnection(getActivity())) {
                    Utils.setToast(getActivity(), getResources().getString(R.string.network_error));
                } else {
                    Utils.showProgressDialog(getActivity());
                    AcceptModel acceptModel = new AcceptModel(deliveryIssuanceId, aTrue, sReason);
                    pendingTaskViewModel.AcceptPendingMyTaskAdi(acceptModel);
                }
            }
        });
        dissmiss.setOnClickListener(v -> customDialog.dismiss());
        customDialog.show();
    }
}