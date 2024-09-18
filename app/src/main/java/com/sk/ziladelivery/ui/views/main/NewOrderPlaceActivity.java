package com.sk.ziladelivery.ui.views.main;

import static androidx.constraintlayout.widget.Constraints.TAG;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.sk.ziladelivery.R;
import com.sk.ziladelivery.data.api.RestClient;
import com.sk.ziladelivery.databinding.OrderPlaceFragBinding;
import com.sk.ziladelivery.listener.MultiMposListener;
import com.sk.ziladelivery.listener.MultiRtgsListener;
import com.sk.ziladelivery.listener.OrderPlacedListener;
import com.sk.ziladelivery.listener.OrderchequeimageListener;
import com.sk.ziladelivery.data.model.BankNameModel;
import com.sk.ziladelivery.data.model.ChequePermissionModel;
import com.sk.ziladelivery.data.model.CollectPaymentRequestModel;
import com.sk.ziladelivery.data.model.DeliveryPayments;
import com.sk.ziladelivery.data.model.OrderResponsesModel;
import com.sk.ziladelivery.data.model.PaymentRequestModel;
import com.sk.ziladelivery.data.model.PaymentSubmitResponce;
import com.sk.ziladelivery.data.model.VerifyQrCodeResponseModel;
import com.sk.ziladelivery.utilities.ApiResponse;
import com.sk.ziladelivery.utilities.CommonMethods;
import com.sk.ziladelivery.utilities.Constant;
import com.sk.ziladelivery.utilities.CustomRunnable;
import com.sk.ziladelivery.utilities.Logger;
import com.sk.ziladelivery.utilities.RxBus;
import com.sk.ziladelivery.utilities.SharePrefs;
import com.sk.ziladelivery.utilities.TextUtils;
import com.sk.ziladelivery.utilities.Utils;
import com.sk.ziladelivery.ui.views.viewmodels.OrderPlaceViewModel;
import com.sk.ziladelivery.ui.views.adapter.MultiMposAdapter;
import com.sk.ziladelivery.ui.views.adapter.MultiRtgsAdapter;
import com.sk.ziladelivery.ui.views.adapter.MultipleChequeAdapter;
import com.sk.ziladelivery.ui.views.adapter.PaidAmountAdapter;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import id.zelory.compressor.Compressor;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class NewOrderPlaceActivity extends AppCompatActivity implements  OrderchequeimageListener, MultiMposListener, MultiRtgsListener {
    private ArrayList<DeliveryPayments> chequePaymentList = new ArrayList<>();
    private ArrayList<DeliveryPayments> mposPaymentList = new ArrayList<>();
    private ArrayList<DeliveryPayments> rtgsPaymentList = new ArrayList<>();
    private final ArrayList<String> BankNameList = new ArrayList<>();
    private ArrayList<DeliveryPayments> postPaymentList;
    private MultipleChequeAdapter adapter;
    private MultiMposAdapter mposadapter;
    private MultiRtgsAdapter rtgsadapter;

    private Dialog qrCodeDialog;
    private OrderPlaceFragBinding mBinding;
    private OrderPlaceViewModel orderPlaceViewModel;
    private int orderId;
    private EditText etCaseAmount;
    private TextView tvReceivedTotalAmt;
    private Button btnPayment, btnUpdate;
    private int deliveryIssuanceId, Customerid, TripPlannerConfirmedOrderId, position, chequeLimit;
    private final String trupay = "false";
    private String sCaseAmount = "", sChequeAmount = "", sUPIAmount = "", sRTGSAmount = "";
    private double sServiceText;
    private String fCheckName, uploadFilePath, colorCode, msg, type, from, time;
    private boolean isCheckDetilsFlag = true;
    private boolean isMposDetailFlag = true;
    private boolean isrtgsDetailFlag = true;
    private boolean isPaidAmountFlag = true;
    private LinearLayout checkDetails;
    private boolean paymentBtnFlag = false;
    private boolean isOtpVerified;
    private final boolean isRefeNo = false;
    private String paymentThrough = "";
    private double lat, lg;
    private CustomRunnable customRunnable;
    private Handler handler;
    private Uri mCropImageUri;
    private BottomSheetDialog dialog;
    private ImageView chequeimage;
    private ProgressDialog progressDialog;
    private boolean ischeque = false;
    private boolean IsQREnabled = false;
    PaidAmountAdapter paidAmountAdapter;

    double amount = 0.0, prePaidAmount = 0.0, cashAmt = 0.0, totalBillAmount = 0.0, cheque_Amount = 0.0, mpos_Amount = 0.0, vanRtgsAmt, rtgsBalanceAmount,sQRAmount = 0.0;
    public static final int REQUEST_IMAGE = 100;
    public static final int GALLERY_REQUST = 200;
    private double remaningAmount, onlineAmount;

    private ArrayList<CollectPaymentRequestModel> selectedOrderList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.order_place_frag);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("submitting");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        if (getIntent().getExtras() != null) {
            lat = getIntent().getDoubleExtra("lat", 0);
            lg = getIntent().getDoubleExtra("lg", 0);
            orderId = getIntent().getIntExtra("ORDER_ID", 0);
            deliveryIssuanceId = getIntent().getIntExtra("DeliveryIssuanceId", 0);
            time = getIntent().getStringExtra("time");
            Customerid = getIntent().getIntExtra("CustomerID", 0);
            IsQREnabled = getIntent().getBooleanExtra("IsQREnabled", false);
            remaningAmount = getIntent().getDoubleExtra("RemingAmount", 0);
            onlineAmount = getIntent().getDoubleExtra("onlineAmount", 0);
            totalBillAmount = remaningAmount;
            TripPlannerConfirmedOrderId = getIntent().getIntExtra(Constant.TRIP_PLANNER_CONFIRMED_DETAIL_Id, 0);
            colorCode = getIntent().getStringExtra("colorCode");
            type = getIntent().getStringExtra("type");
            from = getIntent().getStringExtra("from");
            selectedOrderList = (ArrayList<CollectPaymentRequestModel>) getIntent().getSerializableExtra("orderList");
            mBinding.totalPaidamt.setText("" + onlineAmount);

        }
        intView();

        //ll_QR_payment
        orderPlaceViewModel = ViewModelProviders.of(this).get(OrderPlaceViewModel.class);
        mBinding.setOrderPlaceViewModel(orderPlaceViewModel);
        setActionBarConfiguration();
        /***
         * listener
         * **/
        mBinding.setOrderPlacedListener(new OrderPlacedListener() {
            /***
             * check lay hide and un-hide
             * **/
            @Override
            public void checkLayHideUnHideClicked() {
                if (ischeque) {
                    if (isCheckDetilsFlag) {
                        checkDetails.setVisibility(View.VISIBLE);
                        isCheckDetilsFlag = false;
                    } else {
                        checkDetails.setVisibility(View.GONE);
                        isCheckDetilsFlag = true;
                    }
                } else {
                    if (msg != null && !(msg.equalsIgnoreCase(""))) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(NewOrderPlaceActivity.this)
                                .setCancelable(true)
                                .setMessage(msg);
                        builder.show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(NewOrderPlaceActivity.this)
                                .setCancelable(true)
                                .setMessage("This Customer is Not Eligible for Cheque Payment");
                        builder.show();
                    }

                }
            }

            @Override
            public void checkUriHideUnHideClicked() {
                if (isMposDetailFlag) {
                    mBinding.UTRDetail.setVisibility(View.VISIBLE);
                    isMposDetailFlag = false;
                } else {
                    mBinding.UTRDetail.setVisibility(View.GONE);
                    isMposDetailFlag = true;
                }
            }

            @Override
            public void rtgsCicked() {
                if (isrtgsDetailFlag) {
                    mBinding.rtgsDetail.setVisibility(View.VISIBLE);
                    isrtgsDetailFlag = false;
                } else {
                    mBinding.rtgsDetail.setVisibility(View.GONE);
                    isrtgsDetailFlag = true;
                }
            }

            @Override
            public void updateBtnClicked() {

                if (trupay.equalsIgnoreCase("true")) {
                    resetValue("prepaidAmt");
                    WSCallforOrderPlace(true);
                } else {
                    // generate OTP for Order
                    btnUpdate.setClickable(false);
                    resetValue("orderCancel");
                    UpdateAPICall(false);
                }
            }

            @Override
            public void UPIBtnClicked() {
                RestClient.getInstance().getService().getQRCodeUPI(orderId, Customerid, (int) cashAmt)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<String>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                progressDialog = new ProgressDialog(NewOrderPlaceActivity.this);
                                progressDialog.setCancelable(false);
                                progressDialog.setMessage("Loading");
                                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                progressDialog.show();
                            }

                            @Override
                            public void onNext(String result) {
                                progressDialog.dismiss();
                                if (result != null && !result.equals("")) {
                                    QRCOdePopup(result);
                                } else {
                                    Utils.setToast(getApplicationContext(), "Unable to Upload image");
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Unable to Uploaded Image", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }

            @Override
            public void PaidAmountClicked() {
                if (isPaidAmountFlag) {
                    mBinding.llPaidamtDetail.setVisibility(View.VISIBLE);
                    isPaidAmountFlag = false;
                } else {
                    mBinding.llPaidamtDetail.setVisibility(View.GONE);
                    isPaidAmountFlag = true;
                }
            }

            @Override
            public void QRCodeClicked() {
                List<Integer> orderList = new ArrayList<Integer>();
                for (int j = 0; j < selectedOrderList.size(); j++) {
                    orderList.add(selectedOrderList.get(j).getOrderId());
                }
                startActivity(new Intent(NewOrderPlaceActivity.this, QRCodeActivity.class).putExtra("OrderID", (Serializable) orderList).putExtra("sCaseAmount", remaningAmount));
                finish();
            }

        });


        orderPlaceViewModel.getverifyqrcodeData().observe(this, apiResponse -> {
            if (apiResponse != null) {
                consumeResponseverifyqrcode(apiResponse);
            }
        });


        FetchCustinfo(getCustInfo, Customerid);

        /***
         * Order Placed response Getting
         * **/
        orderPlaceViewModel.getOrderPlacedResponseData().observe(this, apiResponse -> {
            if (apiResponse != null) {
                consumeResponseForOrderPlace(apiResponse);
            }
        });

        orderPlaceViewModel.getchequepermission().observe(this, apiResponse -> {
            if (apiResponse != null) {
                consumeResponseForchequePermission(apiResponse);
            }
        });


        orderPlaceViewModel.getBankname().observe(this, apiResponse -> {
            if (apiResponse != null) {
                consumeResponseForBankname(apiResponse);
            }
        });
        orderPlaceViewModel.hitBankName();

        orderPlaceViewModel.generateOrderOTP().observe(this, apiResponse -> {
            if (apiResponse != null) {
                consumeOrderOtpGenerateResponse(apiResponse);
            }
        });
        orderPlaceViewModel.validateOrderOTP().observe(this, apiResponse -> {
            if (apiResponse != null) {
                consumeVerifyOrderOtpResponse(apiResponse);
            }
        });

        orderPlaceViewModel.submitPayment().observe(this, apiResponse -> {
            if (apiResponse != null) {
                consumeSubmitPayment(apiResponse);
            }
        });

        orderPlaceViewModel.getCustomerRTGSAmount().observe(this, apiResponse -> {
            if (apiResponse != null) {
                consumeResponseCustomerRTGSAmount(apiResponse);
            }
        });

/***
 * Order details API call
 * **/

        CallAPI();
        getCustomerRTGSAmount();

    }

    private void getCustomerRTGSAmount() {
        if (!Utils.checkInternetConnection(this)) {
            Utils.setToast(this, getResources().getString(R.string.network_error));
        } else {
            orderPlaceViewModel.customerRTGSAmountObserver(Customerid);
        }
    }

    private void PaymentCheckValidation(boolean ischeque, boolean isMpos, boolean isRTGS) {
        ArrayList<String> newList = new ArrayList<>();
        boolean ischequeboolean = false;
        boolean ismposboolean = false;
        boolean isrtgsboolean = false;
        if (ischeque && isMpos && isRTGS) {
            for (int i = 0; i < chequePaymentList.size(); i++) {
                if (!newList.contains(chequePaymentList.get(i).getTransId())) {
                    newList.add(chequePaymentList.get(i).getTransId());
                } else {
                    ischequeboolean = false;
                    Utils.setToast(NewOrderPlaceActivity.this, "Cheque Number should not be same");
                    break;
                }
                if (chequePaymentList.get(i).getChequeImageUrl() == null || chequePaymentList.get(i).getChequeImageUrl().equalsIgnoreCase("")) {
                    ischequeboolean = false;
                    Utils.setToast(NewOrderPlaceActivity.this, getString(R.string.plz_upload_check_images));
                    break;
                } else if (chequePaymentList.get(i).getTransId() == null || chequePaymentList.get(i).getTransId().equalsIgnoreCase("")) {
                    ischequeboolean = false;
                    Utils.setToast(NewOrderPlaceActivity.this, getString(R.string.plz_enter_chequeno));
                    break;
                } else if (chequePaymentList.get(i).getChequeBankName() == null || chequePaymentList.get(i).getChequeBankName().equalsIgnoreCase("") || chequePaymentList.get(i).getChequeBankName().equalsIgnoreCase("Select Bank Name")) {
                    ischequeboolean = false;
                    Utils.setToast(NewOrderPlaceActivity.this, getString(R.string.plz_select_Bank_name));
                    break;
                } else if (chequePaymentList.get(i).getAmount() == 0) {
                    Utils.setToast(NewOrderPlaceActivity.this, "Enter Cheque Amount");
                    ischequeboolean = false;
                    break;
                } else if (chequePaymentList.get(i).getPaymentDate() == null || chequePaymentList.get(i).getPaymentDate().equalsIgnoreCase("")) {
                    ischequeboolean = false;
                    Utils.setToast(NewOrderPlaceActivity.this, getString(R.string.enter_check_date));
                    break;
                } else {
                    ischequeboolean = true;
                }
            }
            if (ischequeboolean) {
                ArrayList<String> newlist = new ArrayList<String>();
                ismposboolean = false;
                for (int i = 0; i < mposPaymentList.size(); i++) {
                    if (!newlist.contains(mposPaymentList.get(i).getTransId())) {
                        newlist.add(mposPaymentList.get(i).getTransId());
                    } else {
                        ismposboolean = false;
                        Utils.setToast(NewOrderPlaceActivity.this, "Mpos Number should not be same");
                        break;
                    }
                    if (mposPaymentList.get(i).getTransId() == null || mposPaymentList.get(i).getTransId().equalsIgnoreCase("")) {
                        ismposboolean = false;
                        Utils.setToast(NewOrderPlaceActivity.this, getString(R.string.enter_reference_id));
                        break;
                    } else if (mposPaymentList.get(i).getTransId().length() < 8) {
                        ismposboolean = false;
                        Utils.setToast(NewOrderPlaceActivity.this, getString(R.string.enter_reference_mpos_id));
                        break;
                    } else if (mposPaymentList.get(i).getAmount() == 0) {
                        ismposboolean = false;
                        Utils.setToast(NewOrderPlaceActivity.this, "Please Enter Mpos Amount");
                        break;
                    } else {
                        ismposboolean = true;
                    }

                }
            }
            if (ismposboolean) {
                isrtgsboolean = false;
                ArrayList<String> newlist = new ArrayList<String>();
                for (int i = 0; i < rtgsPaymentList.size(); i++) {
                    if (!newlist.contains(rtgsPaymentList.get(i).getTransId())) {
                        newlist.add(rtgsPaymentList.get(i).getTransId());
                    } else {
                        isrtgsboolean = false;
                        Utils.setToast(NewOrderPlaceActivity.this, "RTGS/NEFT Number should not be same");
                        break;
                    }
                    if (rtgsPaymentList.get(i).getTransId() == null || rtgsPaymentList.get(i).getTransId().equalsIgnoreCase("")) {
                        isrtgsboolean = false;
                        Utils.setToast(NewOrderPlaceActivity.this, getString(R.string.enter_rtgs));
                        break;
                    } else if (rtgsPaymentList.get(i).getTransId().length() < 8) {
                        isrtgsboolean = false;
                        Utils.setToast(NewOrderPlaceActivity.this, getString(R.string.enter_valid_rtgs_id));
                        break;
                    } else if (rtgsPaymentList.get(i).getAmount() == 0) {
                        isrtgsboolean = false;
                        Utils.setToast(NewOrderPlaceActivity.this, "Please Enter RTGS/NEFT Amount");
                        break;
                    } else {
                        isrtgsboolean = true;
                    }
                }

            }
            if (isrtgsboolean) {
                WSCallforOrderPlace(false);
            }
        } else if (ischeque && isMpos) {
            ischequeboolean = false;
            newList.clear();
            for (int i = 0; i < chequePaymentList.size(); i++) {
                if (!newList.contains(chequePaymentList.get(i).getTransId())) {
                    newList.add(chequePaymentList.get(i).getTransId());
                } else {
                    ischequeboolean = false;
                    Utils.setToast(NewOrderPlaceActivity.this, "Cheque Number should not be same");
                    break;
                }

                if (chequePaymentList.get(i).getChequeImageUrl() == null || chequePaymentList.get(i).getChequeImageUrl().equalsIgnoreCase("")) {
                    ischequeboolean = false;
                    Utils.setToast(NewOrderPlaceActivity.this, getString(R.string.plz_upload_check_images));
                    break;
                } else if (chequePaymentList.get(i).getTransId() == null || chequePaymentList.get(i).getTransId().equalsIgnoreCase("")) {
                    ischequeboolean = false;
                    Utils.setToast(NewOrderPlaceActivity.this, getString(R.string.plz_enter_chequeno));
                    break;
                } else if (chequePaymentList.get(i).getChequeBankName() == null || chequePaymentList.get(i).getChequeBankName().equalsIgnoreCase("") || chequePaymentList.get(i).getChequeBankName().equalsIgnoreCase("Select Bank Name")) {
                    ischequeboolean = false;
                    Utils.setToast(NewOrderPlaceActivity.this, getString(R.string.plz_select_Bank_name));
                    break;
                } else if (chequePaymentList.get(i).getAmount() == 0) {
                    Utils.setToast(NewOrderPlaceActivity.this, "Enter Cheque Amount");
                    ischequeboolean = false;
                    break;
                } else if (chequePaymentList.get(i).getPaymentDate() == null || chequePaymentList.get(i).getPaymentDate().equalsIgnoreCase("")) {
                    ischequeboolean = false;
                    Utils.setToast(NewOrderPlaceActivity.this, getString(R.string.enter_check_date));
                    break;
                } else {
                    ischequeboolean = true;
                }

            }
            if (ischequeboolean) {
                ismposboolean = false;
                ArrayList<String> newlist = new ArrayList<String>();
                for (int i = 0; i < mposPaymentList.size(); i++) {
                    if (!newlist.contains(mposPaymentList.get(i).getTransId())) {
                        newlist.add(mposPaymentList.get(i).getTransId());
                    } else {
                        ismposboolean = false;
                        Utils.setToast(NewOrderPlaceActivity.this, "Mpos Number should not be same");
                        break;
                    }
                    if (mposPaymentList.get(i).getTransId() == null || mposPaymentList.get(i).getTransId().equalsIgnoreCase("")) {
                        ismposboolean = false;
                        Utils.setToast(NewOrderPlaceActivity.this, getString(R.string.enter_reference_id));
                        break;
                    } else if (mposPaymentList.get(i).getTransId().length() < 8) {
                        ismposboolean = false;
                        Utils.setToast(NewOrderPlaceActivity.this, getString(R.string.enter_reference_mpos_id));
                        break;
                    } else if (mposPaymentList.get(i).getAmount() == 0) {
                        ismposboolean = false;
                        Utils.setToast(NewOrderPlaceActivity.this, "Please Enter Mpos Amount");
                        break;
                    } else {
                        ismposboolean = true;
                    }
                }
            }
            if (ismposboolean) {
                WSCallforOrderPlace(false);
            }
        } else if (ischeque && isRTGS) {
            ischequeboolean = false;
            for (int i = 0; i < chequePaymentList.size(); i++) {
                if (!newList.contains(chequePaymentList.get(i).getTransId())) {
                    newList.add(chequePaymentList.get(i).getTransId());
                } else {
                    ischequeboolean = false;
                    Utils.setToast(NewOrderPlaceActivity.this, "Cheque Number should not be same");
                    break;
                }
                if (chequePaymentList.get(i).getChequeImageUrl() == null || chequePaymentList.get(i).getChequeImageUrl().equalsIgnoreCase("")) {
                    ischequeboolean = false;
                    Utils.setToast(NewOrderPlaceActivity.this, getString(R.string.plz_upload_check_images));
                    break;
                } else if (chequePaymentList.get(i).getTransId() == null || chequePaymentList.get(i).getTransId().equalsIgnoreCase("")) {
                    ischequeboolean = false;
                    Utils.setToast(NewOrderPlaceActivity.this, getString(R.string.plz_enter_chequeno));
                    break;
                } else if (chequePaymentList.get(i).getChequeBankName() == null || chequePaymentList.get(i).getChequeBankName().equalsIgnoreCase("") || chequePaymentList.get(i).getChequeBankName().equalsIgnoreCase("Select Bank Name")) {
                    ischequeboolean = false;
                    Utils.setToast(NewOrderPlaceActivity.this, getString(R.string.plz_select_Bank_name));
                    break;
                } else if (chequePaymentList.get(i).getAmount() == 0) {
                    Utils.setToast(NewOrderPlaceActivity.this, "Enter Cheque Amount");
                    ischequeboolean = false;
                    break;
                } else if (chequePaymentList.get(i).getPaymentDate() == null || chequePaymentList.get(i).getPaymentDate().equalsIgnoreCase("")) {
                    ischequeboolean = false;
                    Utils.setToast(NewOrderPlaceActivity.this, getString(R.string.enter_check_date));
                    break;
                } else {
                    ischequeboolean = true;
                }
            }
            if (ischequeboolean) {
                isrtgsboolean = false;
                ArrayList<String> newlist = new ArrayList<String>();
                for (int i = 0; i < rtgsPaymentList.size(); i++) {
                    if (!newlist.contains(rtgsPaymentList.get(i).getTransId())) {
                        newlist.add(rtgsPaymentList.get(i).getTransId());
                    } else {
                        isrtgsboolean = false;
                        Utils.setToast(NewOrderPlaceActivity.this, "RTGS/NEFT Number should not be same");
                        break;
                    }
                    if (rtgsPaymentList.get(i).getTransId() == null || rtgsPaymentList.get(i).getTransId().equalsIgnoreCase("")) {
                        isrtgsboolean = false;
                        Utils.setToast(NewOrderPlaceActivity.this, getString(R.string.enter_rtgs));
                        break;
                    } else if (rtgsPaymentList.get(i).getTransId().length() < 8) {
                        isrtgsboolean = false;
                        Utils.setToast(NewOrderPlaceActivity.this, getString(R.string.enter_valid_rtgs_id));
                        break;
                    } else if (rtgsPaymentList.get(i).getAmount() == 0) {
                        isrtgsboolean = false;
                        Utils.setToast(NewOrderPlaceActivity.this, "Please Enter RTGS/NEFT Amount");
                        break;
                    } else {
                        isrtgsboolean = true;
                    }
                }
            }
            if (isrtgsboolean) {
                WSCallforOrderPlace(false);
            }
        } else if (isMpos && isRTGS) {
            ArrayList<String> newlist = new ArrayList<String>();
            ismposboolean = false;
            for (int i = 0; i < mposPaymentList.size(); i++) {
                if (!newlist.contains(mposPaymentList.get(i).getTransId())) {
                    newlist.add(mposPaymentList.get(i).getTransId());
                } else {
                    ismposboolean = false;
                    Utils.setToast(NewOrderPlaceActivity.this, "Mpos Number should not be same");
                    break;
                }
                if (mposPaymentList.get(i).getTransId() == null || mposPaymentList.get(i).getTransId().equalsIgnoreCase("")) {
                    ismposboolean = false;
                    Utils.setToast(NewOrderPlaceActivity.this, getString(R.string.enter_reference_id));
                    break;
                } else if (mposPaymentList.get(i).getTransId().length() < 8) {
                    ismposboolean = false;
                    Utils.setToast(NewOrderPlaceActivity.this, getString(R.string.enter_reference_mpos_id));
                    break;
                } else if (mposPaymentList.get(i).getAmount() == 0) {
                    ismposboolean = false;
                    Utils.setToast(NewOrderPlaceActivity.this, "Please Enter Mpos Amount");
                    break;
                } else {
                    ismposboolean = true;
                }
            }
            if (ismposboolean) {
                isrtgsboolean = false;
                newlist.clear();
                for (int i = 0; i < rtgsPaymentList.size(); i++) {
                    if (!newlist.contains(rtgsPaymentList.get(i).getTransId())) {
                        newlist.add(rtgsPaymentList.get(i).getTransId());
                    } else {
                        isrtgsboolean = false;
                        Utils.setToast(NewOrderPlaceActivity.this, "RTGS/NEFT Number should not be same");
                        break;
                    }
                    if (rtgsPaymentList.get(i).getTransId() == null || rtgsPaymentList.get(i).getTransId().equalsIgnoreCase("")) {
                        isrtgsboolean = false;
                        Utils.setToast(NewOrderPlaceActivity.this, getString(R.string.enter_rtgs));
                        break;
                    } else if (rtgsPaymentList.get(i).getTransId().length() < 8) {
                        isrtgsboolean = false;
                        Utils.setToast(NewOrderPlaceActivity.this, getString(R.string.enter_valid_rtgs_id));
                        break;
                    } else if (rtgsPaymentList.get(i).getAmount() == 0) {
                        isrtgsboolean = false;
                        Utils.setToast(NewOrderPlaceActivity.this, "Please Enter RTGS/NEFT Amount");
                        break;
                    } else {
                        isrtgsboolean = true;
                    }
                }

            }
            if (isrtgsboolean) {
                WSCallforOrderPlace(false);
            }
        } else if (isMpos) {
            ArrayList<String> newlist = new ArrayList<String>();
            ismposboolean = false;
            for (int i = 0; i < mposPaymentList.size(); i++) {
                if (!newlist.contains(mposPaymentList.get(i).getTransId())) {
                    newlist.add(mposPaymentList.get(i).getTransId());
                } else {
                    ismposboolean = false;
                    Utils.setToast(NewOrderPlaceActivity.this, "Mpos Number should not be same");
                    break;
                }
                if (mposPaymentList.get(i).getTransId() == null || mposPaymentList.get(i).getTransId().equalsIgnoreCase("")) {
                    ismposboolean = false;
                    Utils.setToast(NewOrderPlaceActivity.this, getString(R.string.enter_reference_id));
                    break;
                } else if (mposPaymentList.get(i).getTransId().length() < 8) {
                    ismposboolean = false;
                    Utils.setToast(NewOrderPlaceActivity.this, getString(R.string.enter_reference_mpos_id));
                    break;
                } else if (mposPaymentList.get(i).getAmount() == 0) {
                    ismposboolean = false;
                    Utils.setToast(NewOrderPlaceActivity.this, "Please Enter Mpos Amount");
                    break;
                } else {
                    ismposboolean = true;
                }
            }
            if (ismposboolean) {
                WSCallforOrderPlace(false);
            }
        } else if (isRTGS) {
            isrtgsboolean = false;
            ArrayList<String> newlist = new ArrayList<String>();
            for (int i = 0; i < rtgsPaymentList.size(); i++) {
                if (!newlist.contains(rtgsPaymentList.get(i).getTransId())) {
                    newlist.add(rtgsPaymentList.get(i).getTransId());
                } else {
                    isrtgsboolean = false;
                    Utils.setToast(NewOrderPlaceActivity.this, "RTGS/NEFT Number should not be same");
                    break;
                }
                if (rtgsPaymentList.get(i).getTransId() == null || rtgsPaymentList.get(i).getTransId().equalsIgnoreCase("")) {
                    isrtgsboolean = false;
                    Utils.setToast(NewOrderPlaceActivity.this, getString(R.string.enter_rtgs));
                    break;
                } else if (rtgsPaymentList.get(i).getTransId().length() < 8) {
                    isrtgsboolean = false;
                    Utils.setToast(NewOrderPlaceActivity.this, getString(R.string.enter_valid_rtgs_id));
                    break;
                } else if (rtgsPaymentList.get(i).getAmount() == 0) {
                    isrtgsboolean = false;
                    Utils.setToast(NewOrderPlaceActivity.this, "Please Enter RTGS/NEFT Amount");
                    break;
                } else {
                    isrtgsboolean = true;
                }
            }
            if (isrtgsboolean) {
                WSCallforOrderPlace(false);
            }
        } else if (ischeque) {
            ischequeboolean = false;
            for (int i = 0; i < chequePaymentList.size(); i++) {
                if (!newList.contains(chequePaymentList.get(i).getTransId())) {
                    newList.add(chequePaymentList.get(i).getTransId());
                } else {
                    ischequeboolean = false;
                    Utils.setToast(NewOrderPlaceActivity.this, "Cheque Number should not be same");
                    break;
                }
                if (chequePaymentList.get(i).getChequeImageUrl() == null || chequePaymentList.get(i).getChequeImageUrl().equalsIgnoreCase("")) {
                    ischequeboolean = false;
                    Utils.setToast(NewOrderPlaceActivity.this, getString(R.string.plz_upload_check_images));
                    break;
                } else if (chequePaymentList.get(i).getTransId() == null || chequePaymentList.get(i).getTransId().equalsIgnoreCase("")) {
                    ischequeboolean = false;
                    Utils.setToast(NewOrderPlaceActivity.this, getString(R.string.plz_enter_chequeno));
                    break;
                } else if (chequePaymentList.get(i).getChequeBankName() == null || chequePaymentList.get(i).getChequeBankName().equalsIgnoreCase("") || chequePaymentList.get(i).getChequeBankName().equalsIgnoreCase("Select Bank Name")) {
                    ischequeboolean = false;
                    Utils.setToast(NewOrderPlaceActivity.this, getString(R.string.plz_select_Bank_name));
                    break;
                } else if (chequePaymentList.get(i).getAmount() == 0) {
                    Utils.setToast(NewOrderPlaceActivity.this, "Enter Cheque Amount");
                    ischequeboolean = false;
                    break;
                } else if (chequePaymentList.get(i).getPaymentDate() == null || chequePaymentList.get(i).getPaymentDate().equalsIgnoreCase("")) {
                    ischequeboolean = false;
                    Utils.setToast(NewOrderPlaceActivity.this, getString(R.string.enter_check_date));
                    break;
                } else {
                    ischequeboolean = true;
                }
            }
            if (ischequeboolean) {
                WSCallforOrderPlace(false);
            }
        }

    }

    private void CallAPI() {
        if (!Utils.checkInternetConnection(this)) {
            Utils.setToast(this, getResources().getString(R.string.network_error));
        } else {
            orderPlaceViewModel.verifyChequePermission(Customerid);
        }
    }

    /**
     * Give capture permission
     **/
    private void requestWritePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_MEDIA_IMAGES}, 0);
            } else {
                CapturePhotoPopup();
            }
        }else{
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            } else {
                CapturePhotoPopup();
            }
        }

    }

    private void consumeResponseForBankname(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                mBinding.proRelatedItem.setVisibility(View.VISIBLE);
                break;
            case SUCCESS:
                mBinding.proRelatedItem.setVisibility(View.GONE);
                assert apiResponse.data != null;
                renderSuccessResponseForBankname(apiResponse.data);
                break;
            case ERROR:
                mBinding.proRelatedItem.setVisibility(View.GONE);
                Utils.setToast(this, getResources().getString(R.string.errorString));
                break;
            default:
                break;
        }
    }


    private void renderSuccessResponseForBankname(JsonElement response) {
        if (Utils.isJSONValid(response.toString())) {
            if (!response.isJsonNull()) {
                Logger.d(CommonMethods.getTag(this), response.toString());
                try {
                    JSONObject obj = new JSONObject(response.toString());
                    BankNameModel bankNameModel = new Gson().fromJson(obj.toString(), BankNameModel.class);
                    if (bankNameModel.isStatus()) {
                        BankNameList.add("Select Bank Name");
                        for (int i = 0; i < bankNameModel.getBankNameDc().size(); i++) {
                            String bankname = bankNameModel.getBankNameDc().get(i).getBankName();
                            BankNameList.add(bankname);
                        }
                        adapter = new MultipleChequeAdapter(NewOrderPlaceActivity.this, BankNameList, chequePaymentList, orderId, this);
                        mBinding.rvCheque.setAdapter(adapter);
                    } else {
                        Utils.setToast(this, obj.getString("Message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, getResources().getString(R.string.errorString), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, response.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void resetValue(String resetType) {
        if (resetType.equalsIgnoreCase("prepaidAmt")) {
            sCaseAmount = "0";
            sUPIAmount = "0";
            sServiceText = 0;
            sRTGSAmount = "0";
            paymentThrough = "";
        } else if (resetType.equalsIgnoreCase("orderCancel")) {
            sCaseAmount = "0";
            sUPIAmount = "0";
            sRTGSAmount = "0";
            sServiceText = 0;
        }
    }

    private void setPaymentBtn() {
        // set listeners
        etCaseAmount.addTextChangedListener(mTextWatcher);
        mBinding.etRtgsAmount.addTextChangedListener(vanTextWatcher);


        // run once to disable if empty
        checkFieldsForEmptyValues();
    }

    private final TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }


        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            // check Fields For Empty Values
            checkFieldsForEmptyValues();
        }
    };


    private final TextWatcher vanTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }


        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (mBinding.etRtgsAmount.length() > 0 && !mBinding.etRtgsAmount.equals(".") && rtgsBalanceAmount < Double.parseDouble(mBinding.etRtgsAmount.getText().toString())) {
                mBinding.etRtgsAmount.setText("" + new DecimalFormat("##.##").format(rtgsBalanceAmount));
            } else {
                checkFieldsForEmptyValues();
            }
        }
    };

    private void checkFieldsForEmptyValues() {
        try {
            double cashAmnt = 0;
            double chequeAmt = 0;
            double upiAmt = 0;
            double rtgsAmt = 0;
            sCaseAmount = etCaseAmount.getText().toString();
            if (!TextUtils.isNullOrEmpty(mBinding.etRtgsAmount.getText().toString()))
                vanRtgsAmt = Double.parseDouble(mBinding.etRtgsAmount.getText().toString());
            else
                vanRtgsAmt = 0;
            if (!sCaseAmount.equals("") && sCaseAmount.length() > 0) {
                cashAmnt = Double.parseDouble(sCaseAmount);
            } else {
                cashAmnt = 0;
            }
            if (!sChequeAmount.equals("") && sChequeAmount.length() > 0) {
                chequeAmt = Double.parseDouble(sChequeAmount);
            } else {
                chequeAmt = 0;
            }

            if (!sUPIAmount.equals("") && sUPIAmount.length() > 0) {
                upiAmt = Double.parseDouble(sUPIAmount);
            } else {
                upiAmt = 0;
            }
            if (!sRTGSAmount.equals("") && sRTGSAmount.length() > 0) {
                rtgsAmt = Double.parseDouble(sRTGSAmount);
            } else {
                rtgsAmt = 0;
            }

            cashAmnt = (totalBillAmount - (prePaidAmount + chequeAmt + upiAmt + rtgsAmt + vanRtgsAmt));
            if (cashAmnt > 0 || cashAmnt == 0) {
                etCaseAmount.removeTextChangedListener(mTextWatcher);
                etCaseAmount.setText("" + (int) cashAmnt);
                etCaseAmount.addTextChangedListener(mTextWatcher);
                double totalenteredAmt = cashAmnt + chequeAmt + upiAmt + rtgsAmt + vanRtgsAmt;
                amount = prePaidAmount + totalenteredAmt;
                tvReceivedTotalAmt.setText(String.valueOf(totalenteredAmt));

                if (amount != 0) {
                    if (amount == totalBillAmount) {
                        btnPayment.setEnabled(true);
                        btnPayment.setBackgroundColor(Color.parseColor("#38a561"));
                        paymentBtnFlag = true;
                    } else {
                        paymentBtnFlag = false;
                        btnPayment.setEnabled(false);
                        btnPayment.setBackgroundColor(Color.parseColor("#828282"));
                    }
                } else {
                    paymentBtnFlag = false;
                    btnPayment.setEnabled(false);
                    btnPayment.setBackgroundColor(Color.parseColor("#828282"));
                }
            } else {
                paymentBtnFlag = false;
                btnPayment.setEnabled(false);
                btnPayment.setBackgroundColor(Color.parseColor("#828282"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void WSCallforOrderPlace(boolean paymentType) {
        UpdateAPICall(paymentType);
    }

    private void intView() {
        try {
            getNotificationEvent();
            tvReceivedTotalAmt = mBinding.tvReceivedTotalAmt;
            etCaseAmount = mBinding.etCaseAmount;
            btnPayment = mBinding.paymentBtn;
            btnUpdate = mBinding.updateBtn;
            checkDetails = mBinding.checkDetails;
            mposadapter = new MultiMposAdapter(this, mposPaymentList, orderId, deliveryIssuanceId, Customerid, this);
            mBinding.rvMpos.setAdapter(mposadapter);
            rtgsadapter = new MultiRtgsAdapter(this, rtgsPaymentList, orderId, deliveryIssuanceId, Customerid, this);
            mBinding.rvRtgs.setAdapter(rtgsadapter);
            if (IsQREnabled) {
                mBinding.llQRPayment.setVisibility(View.VISIBLE);
            } else {
                mBinding.llQRPayment.setVisibility(View.GONE);
            }
            etCaseAmount.setText("" + remaningAmount);
            setPaymentBtn();

            if (onlineAmount > 0) {
                mBinding.llPrePaidImage.setVisibility(View.GONE);
            } else {
            }

            mBinding.btnAdd.setOnClickListener(v -> {
                chequePaymentList = adapter.getListFromAdapter();
                boolean isboolean = false;
                if (chequePaymentList.size() > 0) {
                    ArrayList<String> newList = new ArrayList<String>();
                    for (int i = 0; i < chequePaymentList.size(); i++) {
                        if (!newList.contains(chequePaymentList.get(i).getTransId())) {
                            newList.add(chequePaymentList.get(i).getTransId());
                        } else {
                            isboolean = false;
                            Utils.setToast(NewOrderPlaceActivity.this, "Cheque Number should not be same");
                            break;
                        }
                        if (chequePaymentList.get(i).getAmount() == 0) {
                            Utils.setToast(NewOrderPlaceActivity.this, "Enter Cheque Amount");
                            isboolean = false;
                            break;
                        } else if (chequePaymentList.get(i).getChequeBankName() == null || chequePaymentList.get(i).getChequeBankName().equalsIgnoreCase("") || chequePaymentList.get(i).getChequeBankName().equalsIgnoreCase("Select Bank Name")) {
                            isboolean = false;
                            Utils.setToast(NewOrderPlaceActivity.this, getString(R.string.plz_select_Bank_name));
                            break;
                        } else if (chequePaymentList.get(i).getChequeImageUrl() == null || chequePaymentList.get(i).getChequeImageUrl().equalsIgnoreCase("")) {
                            isboolean = false;
                            Utils.setToast(NewOrderPlaceActivity.this, getString(R.string.plz_upload_check_images));
                            break;

                        } else if (chequePaymentList.get(i).getPaymentDate() == null || chequePaymentList.get(i).getPaymentDate().equalsIgnoreCase("")) {
                            isboolean = false;
                            Utils.setToast(NewOrderPlaceActivity.this, getString(R.string.enter_check_date));
                            break;
                        } else if (chequePaymentList.get(i).getTransId() == null || chequePaymentList.get(i).getTransId().equalsIgnoreCase("")) {
                            isboolean = false;
                            Utils.setToast(NewOrderPlaceActivity.this, getString(R.string.plz_enter_chequeno));
                            break;

                        } else {
                            isboolean = true;
                        }
                    }
                    if (isboolean) {
                        chequePaymentList.add(new DeliveryPayments(0, "", 0, "Cheque", "", "", "", false));
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    chequePaymentList.add(new DeliveryPayments(0, "", 0, "Cheque", "", "", "", false));
                    adapter.notifyDataSetChanged();
                }

            });
            mBinding.btnMposadd.setOnClickListener(v -> {
                mposPaymentList = mposadapter.getListFromAdapter();
                boolean ismposboolean = false;
                if (mposPaymentList.size() > 0) {
                    ArrayList<String> newList = new ArrayList<String>();
                    for (int i = 0; i < mposPaymentList.size(); i++) {
                        if (!newList.contains(mposPaymentList.get(i).getTransId())) {
                            newList.add(mposPaymentList.get(i).getTransId());
                        } else {
                            ismposboolean = false;
                            Utils.setToast(NewOrderPlaceActivity.this, "Mpos Number should not be same");
                            break;
                        }
                        if (mposPaymentList.get(i).getTransId() == null || mposPaymentList.get(i).getTransId().equalsIgnoreCase("")) {
                            ismposboolean = false;
                            Utils.setToast(NewOrderPlaceActivity.this, getString(R.string.enter_reference_id));
                            break;
                        } else if (mposPaymentList.get(i).getTransId().length() < 8) {
                            ismposboolean = false;
                            Utils.setToast(NewOrderPlaceActivity.this, getString(R.string.enter_reference_mpos_id));
                            break;
                        } else if (mposPaymentList.get(i).getAmount() == 0) {
                            ismposboolean = false;
                            Utils.setToast(NewOrderPlaceActivity.this, "Please Enter Mpos Amount");
                            break;
                        } else {
                            ismposboolean = true;
                        }
                    }

                    if (ismposboolean) {
                        mposPaymentList.add(new DeliveryPayments(0, "",
                                0, "mPos", "", "", "", false));
                        mposadapter.notifyDataSetChanged();
                    }

                } else {
                    mposPaymentList.add(new DeliveryPayments(0, "", 0, "mPos", "", "", "", false));
                    mposadapter.notifyDataSetChanged();
                }
            });
            mBinding.btnRtgsadd.setOnClickListener(v -> {
                rtgsPaymentList = rtgsadapter.getListFromAdapter();
                boolean isrtgsboolean = false;
                if (rtgsPaymentList.size() > 0) {
                    ArrayList<String> newList = new ArrayList<String>();
                    for (int i = 0; i < rtgsPaymentList.size(); i++) {
                        if (!newList.contains(rtgsPaymentList.get(i).getTransId())) {
                            newList.add(rtgsPaymentList.get(i).getTransId());
                        } else {
                            isrtgsboolean = false;
                            Utils.setToast(NewOrderPlaceActivity.this, "Rtgs Number should not be same");
                            break;
                        }
                        if (rtgsPaymentList.get(i).getTransId() == null || rtgsPaymentList.get(i).getTransId().equalsIgnoreCase("")) {
                            isrtgsboolean = false;
                            Utils.setToast(NewOrderPlaceActivity.this, getString(R.string.enter_rtgs));
                            break;
                        } else if (rtgsPaymentList.get(i).getTransId().length() < 8) {
                            isrtgsboolean = false;
                            Utils.setToast(NewOrderPlaceActivity.this, getString(R.string.enter_valid_rtgs_id));
                            break;
                        } else if (rtgsPaymentList.get(i).getAmount() == 0) {
                            isrtgsboolean = false;
                            Utils.setToast(NewOrderPlaceActivity.this, "Please Enter RTGS/NEFT Amount");
                            break;
                        } else {
                            isrtgsboolean = true;
                        }
                    }

                    if (isrtgsboolean) {
                        rtgsPaymentList.add(new DeliveryPayments(0, "", 0, "RTGS/NEFT", "", "", "", false));
                        rtgsadapter.notifyDataSetChanged();
                    }
                } else {
                    rtgsPaymentList.add(new DeliveryPayments(0, "", 0, "RTGS/NEFT", "", "", "", false));
                    rtgsadapter.notifyDataSetChanged();
                }
            });
            mBinding.rvCheque.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            mBinding.rvMpos.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            mBinding.rvRtgs.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

            if (colorCode != null && !colorCode.isEmpty()) {
                mBinding.tvCustName.setTextColor(Color.parseColor("" + colorCode));
            } else {
                mBinding.tvCustName.setTextColor(Color.BLACK);
            }

            mBinding.paymentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!Utils.checkInternetConnection(NewOrderPlaceActivity.this)) {
                        Utils.setToast(NewOrderPlaceActivity.this, getResources().getString(R.string.network_error));
                    } else {
                        // getting et value
                        postPaymentList = new ArrayList<>();
                        sCaseAmount = etCaseAmount.getText().toString();
                        if (adapter != null && adapter.getListFromAdapter() != null) {
                            chequePaymentList = adapter.getListFromAdapter();
                            mposPaymentList = mposadapter.getListFromAdapter();
                            rtgsPaymentList = rtgsadapter.getListFromAdapter();
                        }

                        System.out.println("valueee>>>>" + rtgsPaymentList.size());

                        boolean isRtgsPaymentDone = true;
                        boolean isMPOSPaymentDone = true;
                        if (rtgsPaymentList.size() > 0) {
                            for (int i = 0; i < rtgsPaymentList.size(); i++) {
                                if (rtgsPaymentList.get(i).getAmount() != 0) {
                                    if (rtgsPaymentList.get(i).getTransId() != null) {
                                        if (!rtgsPaymentList.get(i).isVeryfied()) {
                                            isRtgsPaymentDone = false;
                                            break;
                                        }
                                    }
                                } else {
                                    isRtgsPaymentDone = true;
                                }
                            }
                        }
                        if (mposPaymentList.size() > 0) {
                            for (int i = 0; i < mposPaymentList.size(); i++) {
                                if (mposPaymentList.get(i).getAmount() != 0) {
                                    if (!mposPaymentList.get(i).isVeryfied()) {
                                        isMPOSPaymentDone = false;
                                        break;
                                    }
                                } else {
                                    isMPOSPaymentDone = true;
                                }
                            }
                        }
                        if (chequePaymentList.size() == 0 && mposPaymentList.size() == 0 && rtgsPaymentList.size() == 0) {
                            WSCallforOrderPlace(false);
                        } else {
                            if (isRtgsPaymentDone && isMPOSPaymentDone) {
                                if (amount == totalBillAmount) {
                                    if (sCaseAmount.length() > 0 && !(sCaseAmount.equalsIgnoreCase("0"))) {
                                        if (Double.parseDouble(sCaseAmount) > 200000) {
                                            Utils.setToast(NewOrderPlaceActivity.this, "Cash Amount should not Be Greater than 2 lakh");
                                        } else {
                                            if (chequePaymentList.size() != 0) {
                                                if (Double.parseDouble(sChequeAmount) > chequeLimit) {
                                                    Utils.setToast(NewOrderPlaceActivity.this, "Cheque Amount should not be greater than " + chequeLimit);
                                                } else {
                                                    if (!sUPIAmount.equals("") && sUPIAmount.length() > 0 && sUPIAmount.equalsIgnoreCase("0")) {

                                                        if (!sRTGSAmount.equals("") && sRTGSAmount.length() > 0 && sRTGSAmount.equalsIgnoreCase("0")) {
                                                            if (chequePaymentList.size() > 0 && mposPaymentList.size() > 0 && rtgsPaymentList.size() > 0) {
                                                                PaymentCheckValidation(true, true, true);
                                                            }
                                                        } else {
                                                            if (chequePaymentList.size() > 0 && mposPaymentList.size() > 0) {
                                                                PaymentCheckValidation(true, true, false);
                                                            }
                                                        }

                                                    } else if (!sRTGSAmount.equals("") && sRTGSAmount.length() > 0 && sRTGSAmount.equalsIgnoreCase("0")) {
                                                        PaymentCheckValidation(true, false, true);
                                                    } else if (chequePaymentList.size() != 0) {
                                                        PaymentCheckValidation(true, false, false);
                                                    } else {
                                                        WSCallforOrderPlace(false);
                                                    }
                                                }
                                            } else if (!sUPIAmount.equals("") && sUPIAmount.length() > 0 && sUPIAmount.equalsIgnoreCase("0")) {
                                                PaymentCheckValidation(false, true, !sRTGSAmount.equals("") && sRTGSAmount.length() > 0 && sRTGSAmount.equalsIgnoreCase("0"));
                                            } else if (!sRTGSAmount.equals("") && sRTGSAmount.length() > 0 && !sRTGSAmount.equalsIgnoreCase("0")) {
                                                PaymentCheckValidation(false, false, true);
                                            } else {
                                                WSCallforOrderPlace(false);
                                            }
                                        }
                                    } else {
                                        if (chequePaymentList.size() != 0) {
                                            if (Double.parseDouble(sChequeAmount) > chequeLimit) {
                                                Utils.setToast(NewOrderPlaceActivity.this, "Cheque Amount should not be greater than " + chequeLimit);
                                            } else {
                                                if (!sUPIAmount.equals("") && sUPIAmount.length() > 0 && sUPIAmount.equalsIgnoreCase("0")) {

                                                    if (!sRTGSAmount.equals("") && sRTGSAmount.length() > 0 && sRTGSAmount.equalsIgnoreCase("0")) {
                                                        if (chequePaymentList.size() > 0 && mposPaymentList.size() > 0 && rtgsPaymentList.size() > 0) {
                                                            PaymentCheckValidation(true, true, true);
                                                        }
                                                    } else {
                                                        if (chequePaymentList.size() > 0 && mposPaymentList.size() > 0) {
                                                            PaymentCheckValidation(true, true, false);
                                                        }
                                                    }

                                                } else if (!sRTGSAmount.equals("") && sRTGSAmount.length() > 0 && sRTGSAmount.equalsIgnoreCase("0")) {

                                                    PaymentCheckValidation(true, false, true);
                                                } else if (chequePaymentList.size() != 0) {
                                                    PaymentCheckValidation(true, false, false);
                                                } else {
                                                    WSCallforOrderPlace(false);
                                                }
                                            }
                                        } else if (!sUPIAmount.equals("") && sUPIAmount.length() > 0 && sUPIAmount.equalsIgnoreCase("0")) {
                                            PaymentCheckValidation(false, true, !sRTGSAmount.equals("") && sRTGSAmount.length() > 0 && sRTGSAmount.equalsIgnoreCase("0"));
                                        } else if (!sRTGSAmount.equals("") && sRTGSAmount.length() > 0 && sRTGSAmount.equalsIgnoreCase("0")) {
                                            PaymentCheckValidation(false, false, true);
                                        } else {
                                            WSCallforOrderPlace(false);
                                        }
                                    }
                                } else {
                                    Utils.setToast(NewOrderPlaceActivity.this, getString(R.string.enter_total_amt));
                                }

                            } else {
                                Utils.setToast(NewOrderPlaceActivity.this, "Please Verify Reference Number First");
                            }
                        }
                    }
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
        if (SharePrefs.getInstance(getApplicationContext()).getBoolean(SharePrefs.ISRAZORPAYENABLE)) {
            mBinding.paymentBtnUpi.setVisibility(View.VISIBLE);
        } else {
            mBinding.paymentBtnUpi.setVisibility(View.GONE);
        }

    }

    private void getNotificationEvent() {
        RxBus.getInstance().getEvent().observeOn(AndroidSchedulers.mainThread()).subscribe(new DisposableObserver<Object>() {
            @Override
            public void onNext(@NotNull Object o) {
                if (o instanceof Boolean) {
                    boolean str = (boolean) o;
                    if (str) {
                        if (qrCodeDialog != null) {
                            qrCodeDialog.dismiss();
                        }
                        CallAPI();
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


    private void consumeResponseverifyqrcode(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                mBinding.proRelatedItem.setVisibility(View.VISIBLE);
                break;

            case SUCCESS:
                mBinding.proRelatedItem.setVisibility(View.GONE);
                assert apiResponse.data != null;
                renderSuccessResponseForverifyqrcode(apiResponse.data);
                break;

            case ERROR:
                mBinding.proRelatedItem.setVisibility(View.GONE);
                Utils.setToast(this, getResources().getString(R.string.errorString));
                break;

            default:
                break;
        }
    }


    /*
     * method to handle response
     * */
    private void consumeResponseForOrderPlace(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                progressDialog.show();
                break;
            case SUCCESS:
                progressDialog.dismiss();
                assert apiResponse.data != null;
                renderSuccessResponseForOrderPlaced(apiResponse.data);
                break;
            case ERROR:
                progressDialog.dismiss();
                Utils.setToast(this, getResources().getString(R.string.errorString));
                break;
            default:
                break;
        }
    }


    private void consumeResponseForchequePermission(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                mBinding.proRelatedItem.setVisibility(View.VISIBLE);
                break;
            case SUCCESS:
                mBinding.proRelatedItem.setVisibility(View.GONE);
                assert apiResponse.data != null;
                renderSuccessResponseForchequePermission(apiResponse.data);
                break;
            case ERROR:
                mBinding.proRelatedItem.setVisibility(View.GONE);
                Utils.setToast(this, getResources().getString(R.string.errorString));
                break;
            default:
                break;
        }
    }

    private void consumeResponseCustomerRTGSAmount(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                mBinding.proRelatedItem.setVisibility(View.VISIBLE);
                break;
            case SUCCESS:
                mBinding.proRelatedItem.setVisibility(View.GONE);
                assert apiResponse.data != null;
                renderSuccessResponseCustomerRTGS(apiResponse.data);
                break;
            case ERROR:
                mBinding.proRelatedItem.setVisibility(View.GONE);
                Utils.setToast(this, getResources().getString(R.string.errorString));
                break;
            default:
                break;
        }
    }

    private void renderSuccessResponseForchequePermission(JsonElement response) {
        mBinding.proRelatedItem.setVisibility(View.GONE);
        if (Utils.isJSONValid(response.toString())) {
            if (!response.isJsonNull()) {
                try {
                    JSONObject obj = new JSONObject(response.toString());
                    ChequePermissionModel model = new Gson().fromJson(obj.toString(), ChequePermissionModel.class);
                    ischeque = model.isChequeAccepted();
                    msg = model.getMsg();
                    chequeLimit = (int) model.getChequeLimit();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, getResources().getString(R.string.errorString), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, response.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void renderSuccessResponseCustomerRTGS(JsonElement response) {
        mBinding.proRelatedItem.setVisibility(View.GONE);
        rtgsBalanceAmount = Double.parseDouble(response.toString());
        if (!TextUtils.isNullOrEmpty(response.toString()) && rtgsBalanceAmount > 0 && prePaidAmount != totalBillAmount) {
            mBinding.etRtgsAmount.setText("0");
            mBinding.llrtgsbalanceamount.setVisibility(View.VISIBLE);
            mBinding.llAddRtgs.setVisibility(View.GONE);
            mBinding.rtgsDetail.setVisibility(View.GONE);
            mBinding.cheque.setBackgroundColor(Color.parseColor("#5CD3D3D3"));
            mBinding.llmainRTGS.setEnabled(false);
            mBinding.cheque.setEnabled(false);
            mBinding.tvrefrance.setText("Available Balance : ₹ " + new DecimalFormat("##.##").format(rtgsBalanceAmount));
            if (rtgsBalanceAmount >= (totalBillAmount - prePaidAmount)) {
                mBinding.etRtgsAmount.setText("" + new DecimalFormat("##.##").format((totalBillAmount - prePaidAmount)));
            } else {
                if (prePaidAmount > 0) {
                    mBinding.etRtgsAmount.setText("" + new DecimalFormat("##.##").format(rtgsBalanceAmount));
                } else {
                    mBinding.etRtgsAmount.setText("" + new DecimalFormat("##.##").format(rtgsBalanceAmount));
                }
            }
        } else {
            mBinding.llmainRTGS.setEnabled(true);
            mBinding.cheque.setEnabled(true);
            mBinding.llAddRtgs.setVisibility(View.VISIBLE);
            mBinding.etRtgsAmount.setVisibility(View.GONE);
            mBinding.etRtgsAmount.setText("0");
        }

    }


    /*
     * method to handle success response
     * */
    private void renderSuccessResponseForOrderPlaced(JsonElement response) {
        if (Utils.isJSONValid(response.toString())) {
            if (!response.isJsonNull()) {
                mBinding.proRelatedItem.setVisibility(View.GONE);
                Logger.d(CommonMethods.getTag(this), response.toString());
                try {
                    OrderResponsesModel orderResponsesModel = new Gson().fromJson(response, OrderResponsesModel.class);
                    if (orderResponsesModel.isStatus()) {
                        if (from.equalsIgnoreCase("DashBoardFragment")) {
                            startActivity(new Intent(NewOrderPlaceActivity.this, MainActivity.class));
                            finish();
                        } else {
                            Utils.setToast(this, orderResponsesModel.getMessage());
                            Intent intent = new Intent();
                            setResult(401, intent);
                            finish();
                        }
                    } else {
                        Utils.setToast(this, orderResponsesModel.getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, getResources().getString(R.string.errorString), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, response.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void renderSuccessResponseForverifyqrcode(JsonElement response) {
        if (Utils.isJSONValid(response.toString())) {
            if (!response.isJsonNull()) {
                mBinding.proRelatedItem.setVisibility(View.GONE);
                Logger.d(CommonMethods.getTag(this), response.toString());
                try {
                    VerifyQrCodeResponseModel ResponsesModel = new Gson().fromJson(response, VerifyQrCodeResponseModel.class);
                    if (ResponsesModel.isCaptured()) {
                        CallAPI();
                        qrCodeDialog.dismiss();
                    } else {
                        Utils.setToast(this, "Payment Not Done Yet");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, getResources().getString(R.string.errorString), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, response.toString(), Toast.LENGTH_SHORT).show();
        }
    }


    // CustomerInfoAPI
    private void FetchCustinfo(DisposableObserver observer, int CustomerId) {
        RestClient.getInstance().getService().getcustInfo(CustomerId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(@NotNull String object) {
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

    private void CustInfoPopup(String response) {
        LayoutInflater inflater = getLayoutInflater();
        final View mView = inflater.inflate(R.layout.cust_info_popup, null);
        Dialog dialog = new Dialog(this, R.style.CustomDialog);
        dialog.setContentView(mView);
        TextView okBtn = mView.findViewById(R.id.tv_ok);
        TextView msg = mView.findViewById(R.id.tv_msg);
        msg.setText(response);
        okBtn.setOnClickListener(v -> dialog.dismiss());
        //dialog.show();
    }


    private void QRCOdePopup(String response) {
        LayoutInflater inflater = getLayoutInflater();
        final View mView = inflater.inflate(R.layout.qrcode_popup, null);
        qrCodeDialog = new Dialog(this, R.style.CustomDialog);
        qrCodeDialog.setContentView(mView);
        qrCodeDialog.setCancelable(true);
        TextView okBtn = mView.findViewById(R.id.btn_verify);
        ImageView ivqrcode = mView.findViewById(R.id.iv_qrcode);
        if (!response.isEmpty())
            Picasso.get().load(response).into(ivqrcode);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderPlaceViewModel.hitVerifyQrCode(orderId, (int) cashAmt);
                qrCodeDialog.dismiss();
            }
        });
        qrCodeDialog.show();
    }


    //custinfo response
    private final DisposableObserver<String> getCustInfo = new DisposableObserver<String>() {
        @Override
        public void onNext(@NotNull String response) {
            try {
                Utils.hideProgressDialog(NewOrderPlaceActivity.this);
                if (!TextUtils.isNullOrEmpty(response)) {
                    CustInfoPopup(response);
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            Utils.hideProgressDialog(NewOrderPlaceActivity.this);
        }

        @Override
        public void onComplete() {
            Utils.hideProgressDialog(NewOrderPlaceActivity.this);
        }
    };


    // timerMethods
    @Override
    public void onStart() {
        super.onStart();
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            Log.v(TAG, "Starting and binding service");
        }
    }


    private void UpdateAPICall(boolean paymentType) {
        List<Integer> orderIDList = new ArrayList<Integer>();
        if (sCaseAmount.equalsIgnoreCase("0")) {
            paymentThrough = "";
        } else {
            paymentThrough = "cash";
        }
        if (vanRtgsAmt > 0) {
            if (rtgsPaymentList.size() == 0)
                rtgsPaymentList.add(new DeliveryPayments(0, "", 0, "RTGS/NEFT", "", "", "", true));
            rtgsPaymentList.get(0).setAmount(vanRtgsAmt);
        }
        postPaymentList.addAll(chequePaymentList);
        postPaymentList.addAll(mposPaymentList);
        postPaymentList.addAll(rtgsPaymentList);

        for (int j = 0; j < selectedOrderList.size(); j++) {
            orderIDList.add(selectedOrderList.get(j).getOrderId());
        }


        PaymentRequestModel paymentRequestModel = new PaymentRequestModel(TripPlannerConfirmedOrderId, 0, orderIDList, Double.parseDouble(sCaseAmount), postPaymentList);
        Gson gson = new Gson();
        Log.e("ChequeJson", "ChequeJson" + gson.toJson(paymentRequestModel));

        if (!Utils.checkInternetConnection(this)) {
            Utils.setToast(this, getResources().getString(R.string.network_error));
        } else {
            orderPlaceViewModel.submitPaymentObserver(paymentRequestModel);
        }


    }


    @Override
    public void onImageClick(ImageView imageView, int position) {
        this.chequeimage = imageView;
        this.position = position;
        fCheckName = orderId + "_" + System.currentTimeMillis() + "CheckNo.jpg";
        requestWritePermission();
        //CapturePhotoPopup();

    }


    @Override
    public void onAmountChange(ArrayList<DeliveryPayments> payment) {
        double chequeAmount = 0.0;
        for (int i = 0; i < payment.size(); i++) {
            if (payment.get(i).getPaymentFrom().equalsIgnoreCase("Cheque")) {
                chequeAmount = chequeAmount + payment.get(i).getAmount();
            }
        }
        sChequeAmount = String.valueOf(chequeAmount);
        checkFieldsForEmptyValues();
    }

    public void timer(String time, TextView timer) {
        long millse = 0;
        try {
            long currMillis = System.currentTimeMillis();
            SimpleDateFormat sdf1 = new SimpleDateFormat(Utils.myFormat, Locale.getDefault());
            sdf1.setTimeZone(TimeZone.getDefault());
            if (time != null) {
                Date startTime = sdf1.parse(time);
                assert startTime != null;
                long startEpoch = startTime.getTime();
                millse = currMillis - startEpoch;
            }
            if (handler == null) {
                handler = new Handler();
            }
            if (customRunnable != null) {
                customRunnable = null;
            }
            customRunnable = new CustomRunnable(handler, timer, 10000);
            handler.removeCallbacks(customRunnable);
            customRunnable.holder = timer;
            customRunnable.millisUntilFinished = millse;
            handler.postDelayed(customRunnable, 1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        //mBinding.CropImageView.setOnSetImageUriCompleteListener(null);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("onResult-ActivityMain", String.valueOf(requestCode));
        String barcoderesult;
        if (requestCode == 2) {
            try {
                barcoderesult = data.getStringExtra("result");
                if (barcoderesult.equalsIgnoreCase(String.valueOf(orderId))) {
                    WSCallforOrderPlace(false);
                } else {

                    Toast.makeText(this, "Barcode Does not Match with Order ID", Toast.LENGTH_SHORT).show();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_IMAGE && resultCode == Activity.RESULT_OK) {
            uploadMultipart();
        } else if ((requestCode == GALLERY_REQUST && resultCode == Activity.RESULT_OK && null != data)) {
            Uri selectedImageUri = data.getData();
            String[] projection = {MediaStore.MediaColumns.DATA};
            Cursor cursor = managedQuery(selectedImageUri, projection, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            cursor.moveToFirst();
            String selectedImagePath = cursor.getString(column_index);
            Bitmap bm;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(selectedImagePath, options);
            final int REQUIRED_SIZE = 200;
            int scale = 1;
            while (options.outWidth / scale / 2 >= REQUIRED_SIZE && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;
            options.inSampleSize = scale;
            options.inJustDecodeBounds = false;
            bm = BitmapFactory.decodeFile(selectedImagePath, options);
            uploadFilePath = SavedImages(bm);
            uploadMultipart();

        }

    }

    private void CapturePhotoPopup() {
        LayoutInflater inflater = getLayoutInflater();
        final View mView = inflater.inflate(R.layout.capture_photo_view, null);
        Dialog dialog = new Dialog(this, R.style.CustomDialog);
        dialog.setContentView(mView);
        TextView takePhoto = mView.findViewById(R.id.take_photo);
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickFromCamera();
                dialog.dismiss();
            }
        });
        TextView gallery = mView.findViewById(R.id.gallery);
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, GALLERY_REQUST);
                dialog.dismiss();
            }
        });
        TextView cancel = mView.findViewById(R.id.Cancel);
        cancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
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
            Uri photoUri = FileProvider.getUriForFile(this, this.getPackageName() + ".provider", photoFile);
            pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(pictureIntent, REQUEST_IMAGE);
    }

    private File createImageFile() {
        fCheckName = orderId + "_" + System.currentTimeMillis() + "CheckNo.jpg";
        File storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File myDir = new File(Environment.getExternalStorageDirectory() + "/ShopKirana");
        myDir.mkdirs();
        File file = new File(storageDir, fCheckName);
        uploadFilePath = file.getAbsolutePath();
        return file;
    }

    private String SavedImages(Bitmap bm) {
        Uri imageUri = null;
        OutputStream fos;
        String root = "";
        String directory = Environment.DIRECTORY_PICTURES;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            try {
                root = String.valueOf(Environment.getExternalStorageDirectory());

                ContentResolver resolver = this.getContentResolver();
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fCheckName);
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image");
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, directory);
                imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                fos = resolver.openOutputStream(imageUri);
                boolean saved = bm.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
                fos.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

            Log.d(TAG, "SavedImages:" + root + File.separator + directory + File.separator + fCheckName);
            return uploadFilePath = root + File.separator + directory + File.separator + fCheckName;

        } else {
            try {
                String roott = getExternalFilesDir(null).toString();

                File myDir = new File(getExternalFilesDir(null).getAbsolutePath() + "/ShopKirana");
                myDir.mkdirs();
                File file = new File(myDir, fCheckName);
                if (file.exists()) file.delete();
                FileOutputStream out = new FileOutputStream(file);
                bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
                uploadFilePath = roott + "/ShopKirana/" + fCheckName;
                // uploadMultipart();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return uploadFilePath;
        }
    }

    /**
     * Upload images
     **/
    public void uploadMultipart() {
        try {
            final File fileToUpload = new File(uploadFilePath);
            Disposable subscribe = new Compressor(this)
                    .compressToFileAsFlowable(fileToUpload)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(file -> {
                        RequestBody fileReqBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                        MultipartBody.Part part = MultipartBody.Part.createFormData("file", fileToUpload.getName(), fileReqBody);
                        UploadChequeImage(getimageResponse, part);
                    }, throwable -> {
                        throwable.printStackTrace();
                        showError(throwable.getMessage());
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    private void UploadChequeImage(DisposableObserver observer, MultipartBody.Part image) {
        RestClient.getInstance().getService().UploadCheque(image)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NotNull String object) {
                        observer.onNext(object);
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        Log.d("TAG", "Error:" + e);
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();

                    }
                });
    }

    private final DisposableObserver<String> getimageResponse = new DisposableObserver<String>() {
        @Override
        public void onNext(@NotNull String response) {
            try {
                Utils.hideProgressDialog(NewOrderPlaceActivity.this);
                if (response != null) {
                    String chequeimgUrl = response.replace("\"", "");
                    adapter.paymentList.get(position).setChequeImageUrl(chequeimgUrl);
                    adapter.notifyDataSetChanged();
                    Log.e("Success", chequeimgUrl);

                } else {
                    Log.e("Failed", "Failed ####  " + response);
                    Picasso.get().load(R.drawable.ic_add_image_icon).placeholder(R.drawable.ic_add_image_icon).into(chequeimage);
                    Toast.makeText(NewOrderPlaceActivity.this, "Image Not Uploaded", Toast.LENGTH_SHORT).show();

                }

            } catch (Exception e) {
                e.printStackTrace();

            }
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            Utils.hideProgressDialog(NewOrderPlaceActivity.this);
        }

        @Override
        public void onComplete() {
            Utils.hideProgressDialog(NewOrderPlaceActivity.this);
        }
    };




    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            // customRunnable.stop();
            handler.removeCallbacks(customRunnable);
            customRunnable = null;
            handler = null;
        }
    }

    private void setActionBarConfiguration() {
        if (type == null) {
            mBinding.tvTimmer.setVisibility(View.GONE);
            timer(time, mBinding.tvTimmer);

        } else {
            mBinding.tvTimmer.setVisibility(View.GONE);
        }
        mBinding.tvOrderno.setText("Bill Total " + remaningAmount);
        mBinding.ivBack.setOnClickListener(view -> onBackPressed());
    }

    private void consumeOrderOtpGenerateResponse(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                mBinding.proRelatedItem.setVisibility(View.VISIBLE);
                break;
            case SUCCESS:
                mBinding.proRelatedItem.setVisibility(View.GONE);
                Utils.setToast(this, "OTP sent successfully!");
                break;
            case ERROR:
                mBinding.proRelatedItem.setVisibility(View.GONE);
                Utils.setToast(this, getResources().getString(R.string.errorString));
                break;
            default:
                break;
        }
    }

    private void consumeVerifyOrderOtpResponse(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                mBinding.proRelatedItem.setVisibility(View.VISIBLE);
                break;
            case SUCCESS:
                mBinding.proRelatedItem.setVisibility(View.GONE);
                assert apiResponse.data != null;
                if (apiResponse.data.getAsBoolean()) {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    isOtpVerified = true;
                    if (mBinding.updateBtn.getVisibility() == View.VISIBLE) {
                        mBinding.updateBtn.callOnClick();
                    }
                    if (mBinding.paymentBtn.getVisibility() == View.VISIBLE) {
                        WSCallforOrderPlace(false);
                    }
                } else {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    Utils.setToast(this, getResources().getString(R.string.enter_correct_otp));
                }
                break;
            case ERROR:
                mBinding.proRelatedItem.setVisibility(View.GONE);
                Utils.setToast(this, getResources().getString(R.string.errorString));
                break;
            default:
                break;
        }
    }

    private void consumeSubmitPayment(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                progressDialog.setMessage("Doing something, please wait.");
                progressDialog.show();
                break;
            case SUCCESS:
                progressDialog.dismiss();
                renderSuccessResponseSubmitPayment(apiResponse.data);
                break;
            case ERROR:
                progressDialog.dismiss();
                Utils.setToast(this, getResources().getString(R.string.errorString));
                break;
            default:
                break;
        }
    }

    @Override
    public void onMposAmountChange(ArrayList<DeliveryPayments> payment) {
        double mposAmount = 0;
        for (int i = 0; i < payment.size(); i++) {
            if (payment.get(i).getPaymentFrom().equalsIgnoreCase("mPos")) {
                mposAmount = mposAmount + payment.get(i).getAmount();
            }
        }
        sUPIAmount = String.valueOf(mposAmount);
        checkFieldsForEmptyValues();
    }

    @Override
    public void onRtgsAmountChange(ArrayList<DeliveryPayments> payment) {
        double rtgsAmount = 0;
        for (int i = 0; i < payment.size(); i++) {
            if (payment.get(i).getPaymentFrom().equalsIgnoreCase("RTGS/NEFT")) {
                rtgsAmount = rtgsAmount + payment.get(i).getAmount();
            }
        }
        sRTGSAmount = String.valueOf(rtgsAmount);
        checkFieldsForEmptyValues();
    }

    private void renderSuccessResponseSubmitPayment(JsonElement response) {
        if (Utils.isJSONValid(response.toString())) {
            if (!response.isJsonNull()) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    PaymentSubmitResponce submitResponce = new Gson().fromJson(String.valueOf(jsonObject), PaymentSubmitResponce.class);
                    if (submitResponce.isStatus()) {
                        Intent intent = new Intent();
                        intent.putExtra(Constant.TRIP_PLANNER_CONFIRMED_DETAIL_Id, TripPlannerConfirmedOrderId);
                        setResult(RESULT_OK, intent);
                       onBackPressed();
                    } else {
                        Toast.makeText(this, submitResponce.getMessage(), Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, getResources().getString(R.string.errorString), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, response.toString(), Toast.LENGTH_SHORT).show();

        }
    }
}