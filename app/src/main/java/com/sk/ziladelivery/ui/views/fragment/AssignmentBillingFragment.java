package com.sk.ziladelivery.ui.views.fragment;

import static android.view.View.GONE;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sk.ziladelivery.R;
import com.sk.ziladelivery.data.api.RestClient;
import com.sk.ziladelivery.data.model.AssignmentBillingModel;
import com.sk.ziladelivery.data.model.AssignmentIdDataModel;
import com.sk.ziladelivery.data.model.AssignmentOrderModel;
import com.sk.ziladelivery.data.model.AssignmentSetModel;
import com.sk.ziladelivery.data.model.DeliveryIssuanceM;
import com.sk.ziladelivery.data.model.DeliveryIssuanceModel;
import com.sk.ziladelivery.data.model.GanrateCodeModel;
import com.sk.ziladelivery.data.model.OrderModel;
import com.sk.ziladelivery.data.model.PaymentDetails;
import com.sk.ziladelivery.data.model.ShortItemeModel;
import com.sk.ziladelivery.databinding.FragmentAssignmentBillingBinding;
import com.sk.ziladelivery.databinding.RejectResonPopupBinding;
import com.sk.ziladelivery.ui.views.adapter.AssignmentAdapter;
import com.sk.ziladelivery.ui.views.adapter.AssignmentBillingAdapter;
import com.sk.ziladelivery.ui.views.adapter.ShortItemAdapter;
import com.sk.ziladelivery.ui.views.main.MainActivity;
import com.sk.ziladelivery.ui.views.viewmodels.AssignmentBillingViewModel;
import com.sk.ziladelivery.utilities.ApiResponse;
import com.sk.ziladelivery.utilities.CommonMethods;
import com.sk.ziladelivery.utilities.Logger;
import com.sk.ziladelivery.utilities.MarshmallowPermissions;
import com.sk.ziladelivery.utilities.RxBus;
import com.sk.ziladelivery.utilities.SharePrefs;
import com.sk.ziladelivery.utilities.TextUtils;
import com.sk.ziladelivery.utilities.Utils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import id.zelory.compressor.Compressor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class AssignmentBillingFragment extends Fragment implements View.OnClickListener {
    private static final int CAPTURE_IMAGE_CAMERA = 1111;
    public static final int REQUEST_IMAGE = 100;
    private MainActivity activity;
    private FragmentAssignmentBillingBinding mBinding;
    private LinearLayout cameraCallLL, serialNoLL, mainViewLL;
    private RelativeLayout shortitemlistRl, showOrderListRL;
    private String imageFilePath, CustomerDocUpload, DeliveryIssuanceId;
    private Spinner AssignmentSP;
    private TextView totalDeliveredOrderTV, totalCancelOrderTV, totalRedispatchOrderTV, caseAmountTV, onlineAmountTV, chequeAmount, deliveredTotalAmountTV;
    private CheckBox termsConditionCB, imageUploadCB;
    private Button submitBT, rejectBT, ganrateCodedBT;
    private RecyclerView assignmentRV, shortAssignmentRV;
    private AssignmentBillingAdapter assignmentBillingAdapter;
    private AssignmentBillingViewModel assignmentBillingViewModel;
    private ArrayList<DeliveryIssuanceModel> deliveryIssuanceModelArrayList;
    private ArrayList<OrderModel> OrderModel;
    private ArrayList<ShortItemeModel> shortIssuanceArrayList;
    private ArrayList<PaymentDetails> PaymentDetails;
    private AssignmentAdapter assignmentAdapter;
    private Dialog customDialog;
    private File compressedImage;
    private CountDownTimer cTimer = null;
    private Dialog dialog;

    private int peopleId;
    private int warehouseId;
    private Boolean isShowShortItemList = true;
    private Boolean isShowOrderItemList = true;

    boolean isImageUploaded = false;

    public AssignmentBillingFragment() {
    }


    @Override
    public void onAttach(Context context) {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_assignment_billing, container, false);
        getDataViewModel();
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setActionBarConfiguration();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (dialog!=null&& dialog.isShowing()){
            dialog.dismiss();
            Utils.hideProgressDialog(getActivity());
        }
    }

    private void getDataViewModel() {

        assignmentBillingViewModel = ViewModelProviders.of(this).get(AssignmentBillingViewModel.class);
        mBinding.setAssignmentBillingViewModel(assignmentBillingViewModel);
        mBinding.setLifecycleOwner(this);
        initView();
        getNotificationEvent();


        assignmentBillingViewModel.getAssignmentBilling().observe(activity, new Observer<ApiResponse>() {
            @Override
            public void onChanged(@Nullable ApiResponse apiResponse) {

                if (apiResponse != null) {
                    consumeResponse(apiResponse);
                }
            }
        });
        assignmentBillingViewModel.getAssignmentAllData().observe(activity, new Observer<ApiResponse>() {
            @Override
            public void onChanged(@Nullable ApiResponse apiResponse) {

                if (apiResponse != null) {
                    consumeResponseData(apiResponse);
                }

            }
        });

        assignmentBillingViewModel.getAssignmentSendAllData().observe(activity, new Observer<ApiResponse>() {
            @Override
            public void onChanged(@Nullable ApiResponse apiResponse) {

                if (apiResponse != null) {
                    consumeResponseSendData(apiResponse);
                }

            }
        });

        assignmentBillingViewModel.getAssignmentIDResponse().observe(this, apiResponse -> {
            if (apiResponse != null) {
                consumeResponseList(apiResponse);
            }
        });

        assignmentBillingViewModel.getRejectAssignment().observe(activity, new Observer<ApiResponse>() {
            @Override
            public void onChanged(@Nullable ApiResponse apiResponse) {

                if (apiResponse != null) {
                    consumeResponseReject(apiResponse);
                }

            }
        });

        assignmentBillingViewModel.getGanrateCode().observe(activity, new Observer<ApiResponse>() {
            @Override
            public void onChanged(@Nullable ApiResponse apiResponse) {

                if (apiResponse != null) {
                    consumeResponseGanrateCode(apiResponse);
                }

            }
        });

        /***
         * API call
         * **/
        if (!Utils.checkInternetConnection(getActivity())) {
            Utils.setToast(getActivity(), getResources().getString(R.string.network_error));
        } else {
            ///assignmentBillingViewModel.AssignmentBillingID(SharePrefs.getInstance(getActivity()).getInt(SharePrefs.PEOPLE_ID));
            assignmentBillingViewModel.AssignmentBillingID(SharePrefs.getInstance(activity).getInt(SharePrefs.PEOPLE_ID));
        }

    }

    private void getNotificationEvent() {
        RxBus.getInstance().getEvent().observeOn(AndroidSchedulers.mainThread()).subscribe(new DisposableObserver<Object>() {
            @Override
            public void onNext(@NotNull Object o) {
                if (o instanceof String) {
                    String str = (String) o;
                    if (str.equals("CashOTPApproval")) {
                        if (dialog!=null&& dialog.isShowing()){
                            dialog.dismiss();
                            Utils.hideProgressDialog(getActivity());
                            assignmentBillingViewModel.hitAssignmentId(peopleId, warehouseId);
                        }
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void initView() {
        //Long tsLong = System.currentTimeMillis() / 1000;
        cameraCallLL = mBinding.llCameraCall;
        AssignmentSP = mBinding.spAssignmentList;
        totalDeliveredOrderTV = mBinding.tvTotalOrderDeliverd;
        totalCancelOrderTV = mBinding.tvTotalCanceleOrder;
        totalRedispatchOrderTV = mBinding.tvTotalRedispatchOrder;
        caseAmountTV = mBinding.tvCaseAmount;
        onlineAmountTV = mBinding.tvOnlineAmount;
        chequeAmount = mBinding.tvChecqueAmount;
        deliveredTotalAmountTV = mBinding.tvDeliveredTotalAmount;
        termsConditionCB = mBinding.cbTermsConditions;
        submitBT = mBinding.rlSubmit;
        rejectBT = mBinding.btReject;
        assignmentRV = mBinding.rvAssignment;
        shortAssignmentRV = mBinding.rvShortAssinment;
        showOrderListRL = mBinding.llShowOrderList;
        shortitemlistRl = mBinding.llSortList;
        serialNoLL = mBinding.llSerialNo;
        mainViewLL = mBinding.llMainView;
        imageUploadCB = mBinding.cbImageUpload;
        mainViewLL.setVisibility(GONE);
        deliveryIssuanceModelArrayList = new ArrayList<>();

        peopleId = SharePrefs.getInstance(activity).getInt(SharePrefs.PEOPLE_ID);
        warehouseId = SharePrefs.getInstance(activity).getInt(SharePrefs.WAREHOUSE_ID);

        /*assignment Drop down listView*/
        assignmentRV.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        assignmentRV.setHasFixedSize(true);

        /*Short Items listView*/
        shortAssignmentRV.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        shortAssignmentRV.setHasFixedSize(true);
        shortAssignmentRV.setNestedScrollingEnabled(false);



        /* ClickListener */
        cameraCallLL.setOnClickListener(this);
        termsConditionCB.setOnClickListener(this);
        submitBT.setOnClickListener(this);
        showOrderListRL.setOnClickListener(this);
        shortitemlistRl.setOnClickListener(this);
        rejectBT.setOnClickListener(this);
        mBinding.rlGenrateCode.setOnClickListener(this);


        AssignmentSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    DeliveryIssuanceId = deliveryIssuanceModelArrayList.get(position).getDeliveryIssuanceId();

                    if (!Utils.checkInternetConnection(getActivity())) {
                        Utils.setToast(getActivity(), getResources().getString(R.string.network_error));
                    } else {
                        /**
                         * @param DeliveryIssuanceId
                         *
                         */
                        if (DeliveryIssuanceId.equalsIgnoreCase("Select Assignment ID")) {
                            Toast.makeText(activity, "Select Assignment", Toast.LENGTH_SHORT).show();
                            mBinding.allLayout.setVisibility(View.GONE);

                        } else {
                            assignmentBillingViewModel.AssignmentAllData(DeliveryIssuanceId);
                            imageFilePath = null;
                            imageUploadCB.setVisibility(GONE);
                            termsConditionCB.setChecked(true);

                        }
                    }
                } catch (NullPointerException e) {
                    Logger.e(CommonMethods.getTag(this), e.toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

    }

    private void setActionBarConfiguration() {
        final DrawerLayout drawer = getActivity().findViewById(R.id.container);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        TextView tittleTextView = getActivity().findViewById(R.id.toolbar_title);
        tittleTextView.setText(getString(R.string.assignment_billing));
        tittleTextView.setVisibility(View.VISIBLE);
        tittleTextView.setBackgroundDrawable(null);
        TextView tv_assignmentid = getActivity().findViewById(R.id.assignmentid);
        tv_assignmentid.setVisibility(GONE);
        TextView starttimer = getActivity().findViewById(R.id.start_timer);
        starttimer.setVisibility(View.GONE);
        TextView timmer = getActivity().findViewById(R.id.tv_timmer);
        timmer.setVisibility(View.GONE);
        activity.startBreak.setVisibility(View.GONE);
        TextView timer = getActivity().findViewById(R.id.tv_timer);
        timer.setVisibility(View.GONE);
        TextView tvHistory = getActivity().findViewById(R.id.tv_history);
        tvHistory.setVisibility(View.GONE);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });
        ((MainActivity) getActivity()).toggle.syncState();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_camera_call: {
                callForImage(CAPTURE_IMAGE_CAMERA);
                break;
            }

            case R.id.bt_reject: {
                RejectPopup();
                break;
            }

            case R.id.ll_show_order_list: {
                if (isShowOrderItemList) {
                    assignmentRV.setVisibility(View.VISIBLE);
                    isShowOrderItemList = false;
                } else {
                    assignmentRV.setVisibility(View.GONE);
                    isShowOrderItemList = true;
                }
                break;
            }

            case R.id.ll_Sort_list: {
                try {
                    if (shortIssuanceArrayList.size() > 0) {
                        if (isShowShortItemList) {
                            shortAssignmentRV.setVisibility(View.VISIBLE);
                            serialNoLL.setVisibility(View.VISIBLE);
                            isShowShortItemList = false;
                        } else {
                            shortAssignmentRV.setVisibility(View.GONE);
                            serialNoLL.setVisibility(View.GONE);
                            isShowShortItemList = true;
                        }
                    } else {
                        serialNoLL.setVisibility(View.GONE);
                        Toast.makeText(activity, "Short Item Is Not Available.", Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                    Logger.e(CommonMethods.getTag(this), e.toString());
                    Toast.makeText(activity, "Short Item Is Not Available.", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.cb_terms_conditions: {

                break;
            }
            case R.id.rl_genrate_code: {
                if (!Utils.checkInternetConnection(getActivity())) {
                    Utils.setToast(getActivity(), getResources().getString(R.string.network_error));
                } else {
                    Utils.showProgressDialog(getActivity());
                    assignmentBillingViewModel.genrateCode(new GanrateCodeModel(SharePrefs.getInstance(activity).getInt(SharePrefs.PEOPLE_ID),DeliveryIssuanceId));
                }


               /* if (isImageUploaded) {
                    Intent i = new Intent(getActivity(), ScanCodeActivity.class);
                    i.putExtra("DeliveryIssuanceId", Integer.parseInt(DeliveryIssuanceId));
                    i.putExtra("type", "AssignmentBilling");
                    startActivityForResult(i, 2);
                    getActivity().overridePendingTransition(0, 0);
                } else {
                    Toast.makeText(getActivity(), "Please Upload Assignment copy.", Toast.LENGTH_SHORT).show();
                }*/
                break;
            }
        }
    }

    private void RejectPopup() {
        LayoutInflater inflater = getLayoutInflater();
        RejectResonPopupBinding rejectResonPopupBinding = DataBindingUtil.inflate(inflater, R.layout.reject_reson_popup, null, false);
        //  final View mView = inflater.inflate(R.layout.reject_reson_popup, null);
        customDialog = new Dialog(getActivity(), R.style.CustomDialog);
        customDialog.setContentView(rejectResonPopupBinding.getRoot());
        Button submit = rejectResonPopupBinding.submit;
        ImageView dissmiss = rejectResonPopupBinding.dissmiss;
        Spinner spReason = rejectResonPopupBinding.spReason;
        ArrayAdapter statusAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.reason, android.R.layout.simple_spinner_dropdown_item);
        spReason.setAdapter(statusAdapter);
        submit.setOnClickListener(v -> {
            String sReason = spReason.getSelectedItem().toString();
            /***
             * Accept API call
             * **/
            if (sReason.equalsIgnoreCase("Select Reason")) {
                Utils.setToast(getActivity(), getResources().getString(R.string.plz_select_reason));
            } else {
                customDialog.dismiss();
                if (!Utils.checkInternetConnection(getActivity())) {
                    Utils.setToast(getActivity(), getResources().getString(R.string.network_error));
                } else {
                    Utils.showProgressDialog(getActivity());

                    DeliveryIssuanceM deliveryIssuance = new DeliveryIssuanceM(Integer.parseInt(DeliveryIssuanceId), sReason, SharePrefs.getInstance(activity).getInt(SharePrefs.PEOPLE_ID));
                    assignmentBillingViewModel.rejectAssignmentData(deliveryIssuance);
                }
            }

        });
        dissmiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.dismiss();
            }
        });
        customDialog.show();
    }

    private void SubmitAllData() {
        if (isImageUploaded) {

            if (termsConditionCB.isChecked()) {

                if (!Utils.checkInternetConnection(getActivity())) {
                    Utils.setToast(getActivity(), getResources().getString(R.string.network_error));
                } else {

                    /**
                     * @param PeopleID
                     * @param DeliveryIssuanceId
                     * @param CustomerDocUpload
                     *
                     */
                    mBinding.proRelatedItem.setVisibility(View.VISIBLE);

                    assignmentBillingViewModel.sendAllAssignmentData(SharePrefs.getInstance(activity).getInt(SharePrefs.PEOPLE_ID), Integer.parseInt(DeliveryIssuanceId), CustomerDocUpload);
                }
            } else {
                Toast.makeText(getActivity(), "Please select terms and condition.", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            Toast.makeText(getActivity(), "Please Upload Assignment copy.", Toast.LENGTH_SHORT).show();
            return;
        }


    }

    private void consumeResponseList(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                mBinding.proRelatedItem.setVisibility(View.VISIBLE);
                break;
            case SUCCESS:
                mBinding.proRelatedItem.setVisibility(View.GONE);
                renderSuccessResponseList(apiResponse.data);
                break;
            case ERROR:
                mBinding.proRelatedItem.setVisibility(View.GONE);
                Utils.setToast(activity, getResources().getString(R.string.errorString));
                break;
            default:
                break;
        }
    }

    private void renderSuccessResponseList(JsonElement response) {
        if (Utils.isJSONValid(response.toString())) {
            if (!response.isJsonNull()) {
                mBinding.proRelatedItem.setVisibility(View.GONE);

                Log.d("response=", response.toString());
                try {
                    JSONObject obj = new JSONObject(response.toString());
                    AssignmentIdDataModel  assignmentIdDataModel = new Gson().fromJson(obj.toString(), AssignmentIdDataModel.class);
                    if (dialog!=null&& dialog.isShowing()){
                        dialog.dismiss();
                        Utils.hideProgressDialog(getActivity());
                    }
                    Gson gson = new Gson();
                    SharePrefs.setCashmanagmentSharedPreference(activity, SharePrefs.ASSIGN_MODEL, gson.toJson(assignmentIdDataModel.getDeliveryIssuanceDcs()));
                    SharePrefs.getInstance(activity).putString(SharePrefs.ASSIGN_ID,String.valueOf(SharePrefs.getInstance(getActivity()).getInt(SharePrefs.ASSIGN_ID_Notifaction)));
                    SharePrefs.getInstance(activity).putBoolean(SharePrefs.FLAG, true);
                    Fragment fragment = new AssignmentCashManagment();
                    Bundle args = new Bundle();
                    args.putString("comeFrom", "AssignmentBillingFragment");
                    fragment.setArguments(args);
                    activity.switchContentWithStack(fragment);
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

    public void callForImage(int imageResource) {
        try {
            if (Build.VERSION.SDK_INT >= 23) {

                if (MarshmallowPermissions.checkPermissionCamera(getActivity())) {
                    if (MarshmallowPermissions.checkPermissionWriteExternalStorage(getActivity())) {
                        if (imageResource == CAPTURE_IMAGE_CAMERA) {

                            pickFromCamera();
                        }
                    } else {
                        MarshmallowPermissions.requestPermissionWriteExternalStorage(getActivity(), MarshmallowPermissions.PERMISSION_REQUEST_CODE_WRITE_EXTERNAL_STORAGE);
                    }
                } else {
                    MarshmallowPermissions.requestPermissionCameraAndWriteExternalStorage(getActivity(), MarshmallowPermissions.PERMISSION_REQUEST_CODE_CAMERA_AND_STORAGE);
                }
            } else {
                if (imageResource == CAPTURE_IMAGE_CAMERA) {
                    pickFromCamera();
                }
            }
        } catch (Exception e) {
            Logger.e(CommonMethods.getTag(this), e.toString());

        }
    }

    public void pickFromCamera() {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File photoFile;
            try {
                photoFile = createImageFile();
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            Uri photoUri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".provider", photoFile);
            pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(pictureIntent, REQUEST_IMAGE);
    }

    private File createImageFile() {
        CustomerDocUpload = "Assignment" + DeliveryIssuanceId + "image" + ".jpg";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File myDir = new File(Environment.getExternalStorageDirectory() + "/ShopKirana");
        myDir.mkdirs();
        File file = new File(storageDir, CustomerDocUpload);
        imageFilePath = file.getAbsolutePath();
        return file;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE && resultCode == Activity.RESULT_OK) {
            uploadMultipart();
        } else if (requestCode == 2) {
            try {

                if (DeliveryIssuanceId.equalsIgnoreCase(data.getStringExtra("result"))) {
                    SubmitAllData();
                } else {
                    Toast.makeText(activity, "Barcode Does not Match with Order ID", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    /*
     * method to handle response
     * */
    private void consumeResponse(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                Utils.showProgressDialog(getActivity());
                break;
            case SUCCESS:
                Utils.hideProgressDialog(getActivity());
                renderSuccessResponse(apiResponse.data);
                break;

            case ERROR:
                Utils.hideProgressDialog(getActivity());
//                Utils.setToast(getActivity(), getResources().getString(R.string.errorString));
                break;

            default:
                break;
        }
    }

    /*
     * method to handle response
     * */
    private void consumeResponseData(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                Utils.showProgressDialog(getActivity());
                break;
            case SUCCESS:
                Utils.hideProgressDialog(getActivity());
                renderSuccessResponseData(apiResponse.data);
                break;

            case ERROR:
                Utils.hideProgressDialog(getActivity());
                Utils.setToast(getActivity(), getResources().getString(R.string.errorString));
                break;

            default:
                break;
        }
    }

    /*
     * method to handle response
     * */
    private void consumeResponseSendData(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                mBinding.proRelatedItem.setVisibility(View.VISIBLE);
                break;
            case SUCCESS:
                mBinding.proRelatedItem.setVisibility(GONE);
                renderSuccessResponseSendData(apiResponse.data);
                break;

            case ERROR:
                mBinding.proRelatedItem.setVisibility(GONE);
                Utils.setToast(getActivity(), getResources().getString(R.string.errorString));
                break;

            default:
                break;
        }
    }

    /*
     * method to handle response
     * */
    private void consumeResponseGanrateCode(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                //mBinding.proRelatedItem.setVisibility(View.VISIBLE);
                break;
            case SUCCESS:
                // mBinding.proRelatedItem.setVisibility(GONE);
                renderSuccessResponseGanrateCode(apiResponse.data);
                break;

            case ERROR:
                // mBinding.proRelatedItem.setVisibility(GONE);
                Utils.setToast(getActivity(), getResources().getString(R.string.errorString));
                break;

            default:
                break;
        }
    }


    /*Reject Assignments Data */
    private void consumeResponseReject(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                Utils.showProgressDialog(getActivity());
                break;
            case SUCCESS:
                Utils.hideProgressDialog(getActivity());
                renderSuccessResponseReject(apiResponse.data);
                break;

            case ERROR:
                Utils.hideProgressDialog(getActivity());
                Utils.setToast(getActivity(), getResources().getString(R.string.errorString));
                break;

            default:
                break;
        }
    }

    /*
     * method to handle success response
     * @param response
     * */
    private void renderSuccessResponse(JsonElement response) {
        if (Utils.isJSONValid(response.toString())) {
            if (!response.isJsonNull()) {
                deliveryIssuanceModelArrayList.clear();
                Logger.e(CommonMethods.getTag(this), response.toString());
                try {
                    JSONObject obj = new JSONObject(response.toString());
                    AssignmentBillingModel assignmentBillingModel = new Gson().fromJson(obj.toString(), AssignmentBillingModel.class);

                    if (assignmentBillingModel.isStatus()) {
                        if (assignmentBillingModel.getDeliveryIssuanceAL().size() > 0) {
                            mainViewLL.setVisibility(View.VISIBLE);
                            mBinding.tvNoDataFound.setVisibility(View.GONE);
                            deliveryIssuanceModelArrayList.add(new DeliveryIssuanceModel("Select Assignment ID"));
                            deliveryIssuanceModelArrayList.addAll(assignmentBillingModel.getDeliveryIssuanceAL());
                            assignmentAdapter = new AssignmentAdapter(getActivity(), deliveryIssuanceModelArrayList);
                            AssignmentSP.setAdapter(assignmentAdapter);
                            assignmentAdapter.notifyDataSetChanged();

                        } else {
                            mainViewLL.setVisibility(GONE);
                            mBinding.tvNoDataFound.setVisibility(View.VISIBLE);
                        }
                    } else {
                        mainViewLL.setVisibility(GONE);
                        mBinding.tvNoDataFound.setVisibility(View.VISIBLE);
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

    /**
     * @param response
     */
    private void renderSuccessResponseData(JsonElement response) {
        if (Utils.isJSONValid(response.toString())) {
            if (!response.isJsonNull()) {
                Logger.e(CommonMethods.getTag(this), response.toString());
                try {

                    JSONObject obj = new JSONObject(response.toString());
                    AssignmentOrderModel assignmentOrderModel = new Gson().fromJson(obj.toString(), AssignmentOrderModel.class);
                    OrderModel = assignmentOrderModel.getOrderDelMastAssignments();
                    assignmentBillingAdapter = new AssignmentBillingAdapter(getActivity(), OrderModel);
                    assignmentRV.setAdapter(assignmentBillingAdapter);
                    mBinding.allLayout.setVisibility(View.VISIBLE);
                    for (int i = 0; i < OrderModel.size(); i++) {
                        shortIssuanceArrayList = assignmentOrderModel.getOrderDelMastAssignments().get(i).getShortItemeAl();
                    }

                    ShortItemAdapter shortItemAdapter = new ShortItemAdapter(getActivity(), shortIssuanceArrayList);
                    shortAssignmentRV.setAdapter(shortItemAdapter);

                    /*SetData in View*/
                    setDataInView(OrderModel);


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

    private void setDataInView(ArrayList<OrderModel> orderModel) {

        int numberOfDelivered = 0, deliveryCanceled = 0, deliveryRedispatch = 0;
        double totalDeliveredAmount = 0, caseAmount = 0, ElectronicAmount = 0, CheckAmount = 0;

        /*for (int j = 0; j <orderModel.size(); j++)
        {*/

        //}

        for (int i = 0; i < orderModel.size(); i++) {
            if (orderModel.get(i).getStatus().equalsIgnoreCase("Delivered")) {
                numberOfDelivered++;

            } else if (orderModel.get(i).getStatus().equals("Delivery Canceled")) {
                deliveryCanceled++;
            } else if (orderModel.get(i).getStatus().equals("Delivery Redispatch")) {
                deliveryRedispatch++;
            }
            for (int j = 0; j < orderModel.get(i).getPaymentDetails().size(); j++) {
                PaymentDetails = orderModel.get(i).getPaymentDetails();

                if (PaymentDetails.get(j).getPaymentFrom().equalsIgnoreCase("Cash") && orderModel.get(i).getStatus().equalsIgnoreCase("Delivered")) {
                    caseAmount = caseAmount + PaymentDetails.get(j).getAmount();
                }
                if (PaymentDetails.get(j).getPaymentFrom().equalsIgnoreCase("Cheque") && orderModel.get(i).getStatus().equalsIgnoreCase("Delivered")) {
                    CheckAmount = CheckAmount + PaymentDetails.get(j).getAmount();
                }
                if (!PaymentDetails.get(j).getPaymentFrom().equalsIgnoreCase("Cheque") && !PaymentDetails.get(j).getPaymentFrom().equalsIgnoreCase("Cash") && orderModel.get(i).getStatus().equalsIgnoreCase("Delivered")) {
                    ElectronicAmount = ElectronicAmount + PaymentDetails.get(j).getAmount();
                }
            }


        }
        caseAmountTV.setText(String.valueOf(caseAmount));
        onlineAmountTV.setText(String.valueOf(ElectronicAmount));
        chequeAmount.setText(String.valueOf(CheckAmount));
        totalDeliveredAmount = totalDeliveredAmount + caseAmount + ElectronicAmount + CheckAmount;

        deliveredTotalAmountTV.setText(String.valueOf(totalDeliveredAmount));
        totalDeliveredOrderTV.setText(String.valueOf(numberOfDelivered));
        totalCancelOrderTV.setText(String.valueOf(deliveryCanceled));
        totalRedispatchOrderTV.setText(String.valueOf(deliveryRedispatch));

    }

    /*
     * method to handle success response
     * */
    private void renderSuccessResponseSendData(JsonElement response) {
        if (Utils.isJSONValid(response.toString())) {
            if (!response.isJsonNull()) {
                Logger.e(CommonMethods.getTag(this), response.toString());
                try {
                    JSONObject obj = new JSONObject(response.toString());
                    AssignmentSetModel assignmentOrderModel = new Gson().fromJson(obj.toString(), AssignmentSetModel.class);
                    if (assignmentOrderModel.isStatus()) {
                        startActivity(new Intent(activity, MainActivity.class));
                        Utils.setToast(activity, getResources().getString(R.string.order_placed_done));

                    } else {
                        Toast.makeText(getActivity(), assignmentOrderModel.getMessage(), Toast.LENGTH_SHORT).show();

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


    /*
     * method to handle success response
     * */
    private void renderSuccessResponseGanrateCode(JsonElement response) {
        if (!TextUtils.isNullOrEmpty(response.toString())) {
            Logger.e(CommonMethods.getTag(this), response.toString());
            String newCode= response.toString().replaceAll("\"", "");
            showDialog(newCode);
        }

    }


    /*
     * method to handle success response
     * */
    private void renderSuccessResponseReject(JsonElement response) {
        if (Utils.isJSONValid(response.toString())) {
            if (!response.isJsonNull()) {
                Logger.e(CommonMethods.getTag(this), response.toString());
                try {
                    JSONObject obj = new JSONObject(response.toString());
                    // RejectAssignmentModel assignmentSetModel = new Gson().fromJson(obj.toString(), RejectAssignmentModel.class);

                    if (!Utils.checkInternetConnection(getActivity())) {
                        Utils.setToast(getActivity(), getResources().getString(R.string.network_error));
                    } else {
                        ///assignmentBillingViewModel.AssignmentBillingID(SharePrefs.getInstance(getActivity()).getInt(SharePrefs.PEOPLE_ID));
                        assignmentBillingViewModel.AssignmentBillingID(SharePrefs.getInstance(activity).getInt(SharePrefs.PEOPLE_ID));
                    }

                    Toast.makeText(activity, "Rejected Successfully.", Toast.LENGTH_SHORT).show();


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


    public void uploadMultipart() {
        final File fileToUpload = new File(imageFilePath);
        final Disposable subscribe = new Compressor(getActivity())
                .compressToFileAsFlowable(fileToUpload)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<File>() {
                    @Override
                    public void accept(File file) {
                        compressedImage = file;
                        RequestBody fileReqBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                        MultipartBody.Part part = MultipartBody.Part.createFormData("file", fileToUpload.getName(), fileReqBody);
                        UploadChequeImage(getimageResponse, part);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        throwable.printStackTrace();
                        showError(throwable.getMessage());
                    }
                });

    }

    public void showError(String errorMessage) {
        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
    }

    private void UploadChequeImage(DisposableObserver observer, MultipartBody.Part image) {
        RestClient.getInstance().getService().AssignmentCopyUpload(image)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(JsonObject object) {
                        observer.onNext(object);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("TAG", "Error:" + e.toString());
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();

                    }
                });
    }

    private DisposableObserver<JsonObject> getimageResponse = new DisposableObserver<JsonObject>() {
        @Override
        public void onNext(JsonObject response) {
            try {
                Utils.hideProgressDialog(activity);

                if (response != null) {
                    String status = String.valueOf(response.getAsJsonObject().get("Status").getAsBoolean());
                    if (status.equalsIgnoreCase("true")) {
                        Log.e("Success", "Success" + response);
                        imageUploadCB.setVisibility(View.VISIBLE);
                        imageUploadCB.setChecked(true);
                        isImageUploaded = true;
                        mBinding.rlSubmit.setBackground(getContext().getResources().getDrawable(R.drawable.ic_circle_green_shape));

                    } else {
                        Log.e("Failed", "Failed ####  " + status);
                        mBinding.rlSubmit.setBackground(getContext().getResources().getDrawable(R.drawable.ic_circle_green_shape));

                    }
                }


            } catch (Exception e) {
                e.printStackTrace();

            }
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            Utils.hideProgressDialog(activity);
        }

        @Override
        public void onComplete() {
            Utils.hideProgressDialog(activity);
        }
    };

    public void showDialog(String msg) {
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custome_layout_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView timmerTv = dialog.findViewById(R.id.tv_timmer);
        TextView code = dialog.findViewById(R.id.tv_genrated_code);
        code.setText(msg);
        startTimer(timmerTv);

        dialog.show();
    }

    private void startTimer(TextView tvResendOtpTimer/* TextView resendotp*/) {

        cTimer = new CountDownTimer(60*10*1000, 1000) {
            public void onTick(long millisUntilFinished) {
                tvResendOtpTimer.setText(""+String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));

              //  tvResendOtpTimer.setText("Resend Code" + ":" + "" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                if (dialog!=null&& dialog.isShowing()) {
                    dialog.dismiss();
                    Utils.hideProgressDialog(getActivity());
                    mBinding.rlGenrateCode.setText("Regenerate Code");
                }
            }
        };
        cTimer.start();
    }


}

