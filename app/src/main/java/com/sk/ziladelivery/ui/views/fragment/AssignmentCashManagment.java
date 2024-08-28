package com.sk.ziladelivery.ui.views.fragment;

import static com.sk.ziladelivery.R.drawable;
import static com.sk.ziladelivery.R.id;
import static com.sk.ziladelivery.R.string;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.sk.ziladelivery.R;
import com.sk.ziladelivery.data.model.AssignmentID;
import com.sk.ziladelivery.data.model.CurrencyPostDataModel;
import com.sk.ziladelivery.data.model.DeliveryIssuanceDetail;
import com.sk.ziladelivery.data.model.PostCashCollection;
import com.sk.ziladelivery.data.model.PostChequeCollectionModel;
import com.sk.ziladelivery.data.model.PostOnlineCollectionModel;
import com.sk.ziladelivery.databinding.AssignmentcashshowingBinding;
import com.sk.ziladelivery.ui.views.main.ChequeCollectionActivity;
import com.sk.ziladelivery.ui.views.main.MainActivity;
import com.sk.ziladelivery.ui.views.viewmodels.AssignmentCashViewModel;
import com.sk.ziladelivery.utilities.ApiResponse;
import com.sk.ziladelivery.utilities.SharePrefs;
import com.sk.ziladelivery.utilities.TextUtils;
import com.sk.ziladelivery.utilities.Utils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class AssignmentCashManagment extends Fragment implements View.OnClickListener {

    private MainActivity activity;
    private AssignmentcashshowingBinding mBinding;
    private AssignmentCashViewModel assignmentCashViewModel;
    private final ArrayList<Integer> OrderIDArray = new ArrayList<>();

    private int peopleId;
    private int warehouseId;
    private String selectedAssignmentId;
    private double TotalAmount;
    private Bundle bundle;
    private double totalCollectedAmount;
    private double totalUsedCheckAmnt, totalusedCashAmnt, totalUsedOnlineAmnt;
    private double cashAmount, ChequeAmount, OnlineAmount;
    private ArrayList<PostChequeCollectionModel> postChequeCollectionModelArrayList;
    private ArrayList<PostCashCollection> postCashCollectionArrayList;
    private ArrayList<PostOnlineCollectionModel> postOnlineCollectionModelArrayList;
    private int ID = 0;
    private Dialog customDialog;
    private boolean flag;
    private boolean isRefresh = false;
    String backPressMangeValue;

    public AssignmentCashManagment() {

    }


    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        this.activity = (MainActivity) context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.assignmentcashshowing, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            backPressMangeValue = bundle.getString("comeFrom");
        }

        // init
        initView();
        assignmentCashViewModel = ViewModelProviders.of(this).get(AssignmentCashViewModel.class);
        mBinding.setLifecycleOwner(this);
        assignmentCashViewModel.getPostCurrencyData().observe(getViewLifecycleOwner(), apiResponse -> {
            if (apiResponse != null) {
                ResponseofPostData(apiResponse);
            }
        });
        flag = SharePrefs.getInstance(activity).getBoolean(SharePrefs.FLAG);
        if (flag) {
            String assignModel = SharePrefs.getCashmanagmentSharedPreferences(activity, SharePrefs.ASSIGN_MODEL);
            selectedAssignmentId = SharePrefs.getInstance(activity).getString(SharePrefs.ASSIGN_ID);
            ArrayList<AssignmentID> DeliveryIssuanceDcs = new Gson().fromJson(assignModel, new TypeToken<ArrayList<AssignmentID>>() {
            }.getType());
            mBinding.assignId.setText(selectedAssignmentId);
            setData(selectedAssignmentId, DeliveryIssuanceDcs);

        } else {
            String historyModel = SharePrefs.getCashmanagmentSharedPreferences(activity, SharePrefs.HISTORY_MODEL);
            setHistoryData(historyModel);
        }

        RetrieveData();
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
        if (isRefresh) {
            isRefresh = false;
           /* if (flag) {
                String Assignmodel = SharePrefs.getCashmanagmentSharedPreferences(activity, SharePrefs.ASSIGN_MODEL);
                selectedAssignmentId = SharePrefs.getInstance(activity).getString(SharePrefs.ASSIGN_ID);
                ArrayList<AssignmentID> DeliveryIssuanceDcs = new Gson().fromJson(Assignmodel, new TypeToken<ArrayList<AssignmentID>>() {
                }.getType());
                mBinding.assignId.setText(selectedAssignmentId);
                setData(selectedAssignmentId, DeliveryIssuanceDcs);

            } else {
                String historyModel = SharePrefs.getCashmanagmentSharedPreferences(activity, SharePrefs.HISTORY_MODEL);
                setHistoryData(historyModel);
            }*/
            RetrieveData();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.cash_layout:
                Fragment fragment;
                if (cashAmount > 0) {
                    fragment = new CurrencyFragment();
                    bundle.putDouble("cashAmount", cashAmount);
                    fragment.setArguments(bundle);
                    activity.switchContentWithStack(fragment);
                } else {
                    Toast.makeText(activity, R.string.cash_0, Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.cheque_layout:
                if (OrderIDArray.size() > 0) {
                    isRefresh = true;
                  /*  fragment = new ChequeCollectionFragment();
                    fragment.setArguments(bundle);
                    activity.switchContentWithStack(fragment);*/
                    startActivity(new Intent(activity, ChequeCollectionActivity.class));
                } else {
                    Toast.makeText(activity, R.string.Cheque_0, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.online_layout:
                if (OnlineAmount > 0) {
                    fragment = new OnlinePaymentFragment();
                    bundle.putDouble("OnlineAmount", OnlineAmount);
                    fragment.setArguments(bundle);
                    activity.switchContentWithStack(fragment);
                } else {
                    Toast.makeText(activity, string.online_0, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.payment_button:
                if (cashAmount > 0 && totalusedCashAmnt == 0.0) {
                    Toast.makeText(activity, "Please Verify Cash Amount", Toast.LENGTH_SHORT).show();
                } else if (ChequeAmount > 0 && totalUsedCheckAmnt == 0.0) {
                    Toast.makeText(activity, "Please Verify Cheque Amount", Toast.LENGTH_SHORT).show();
                } else if (OnlineAmount > 0 && totalUsedOnlineAmnt == 0.0) {
                    Toast.makeText(activity, "Please Verify Online Amount", Toast.LENGTH_SHORT).show();
                } else if (TotalAmount != totalCollectedAmount) {
                    Toast.makeText(activity, "Submitted Amount should be equals to " + TotalAmount, Toast.LENGTH_SHORT).show();
                } else {
                    SubmitPopup();
                }
                break;
            case R.id.back:
                if (!TextUtils.isNullOrEmpty(backPressMangeValue)) {
                    getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
                } else {
                    activity.onBackPressed();
                }
                break;
            default:
                break;
        }
    }


    private void initView() {
        try {
            totalUsedCheckAmnt = 0.0;
            totalusedCashAmnt = 0.0;
            totalUsedOnlineAmnt = 0.0;
            cashAmount = 0.0;
            ChequeAmount = 0.0;
            OnlineAmount = 0.0;
            totalCollectedAmount = 0.0;

            peopleId = SharePrefs.getInstance(activity).getInt(SharePrefs.PEOPLE_ID);
            warehouseId = SharePrefs.getInstance(activity).getInt(SharePrefs.WAREHOUSE_ID);
            bundle = new Bundle();
            mBinding.cashLayout.setOnClickListener(this);
            mBinding.chequeLayout.setOnClickListener(this);
            mBinding.paymentButton.setOnClickListener(this);
            mBinding.back.setOnClickListener(this);
            mBinding.onlineLayout.setOnClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setHistoryData(String historyModel) {
        try {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<PostChequeCollectionModel>>() {
            }.getType();
            Type type1 = new TypeToken<ArrayList<PostCashCollection>>() {
            }.getType();
            Type type2 = new TypeToken<ArrayList<PostOnlineCollectionModel>>() {
            }.getType();
            JSONObject object = new JSONObject(historyModel);

            String Cashamount = SharePrefs.getCashmanagmentSharedPreferences(activity, SharePrefs.CASH_AMOUNT_JSON);
            String CheckAmount = SharePrefs.getCashmanagmentSharedPreferences(activity, SharePrefs.CHEQUE_AMOUNT);
            String onlineAmount = SharePrefs.getCashmanagmentSharedPreferences(activity, SharePrefs.ONLINE_AMOUNT);

            postChequeCollectionModelArrayList = gson.fromJson(CheckAmount, type);
            postCashCollectionArrayList = gson.fromJson(Cashamount, type1);
            postOnlineCollectionModelArrayList = gson.fromJson(onlineAmount, type2);

            if (postChequeCollectionModelArrayList.size() > 0 && postChequeCollectionModelArrayList != null) {
                for (int i = 0; i < postChequeCollectionModelArrayList.size(); i++) {
                    String Orderid = postChequeCollectionModelArrayList.get(i).getOrderid();
                    OrderIDArray.add(Integer.valueOf(Orderid));
                }
            }


            mBinding.assignId.setText(String.valueOf(object.get("Deliveryissueid")));
            TotalAmount = object.getDouble("TotalDeliveryissueAmt");
            selectedAssignmentId = String.valueOf(object.get("Deliveryissueid"));
            ID = object.getInt("Id");
            cashAmount = object.getDouble("TotalCollectionCashAmt");
            ChequeAmount = object.getDouble("TotalCollectionCheckAmt");
            OnlineAmount = object.getDouble("TotalCollectionOnlineAmt");

            totalusedCashAmnt = object.getDouble("TotalCashAmt");
            totalUsedCheckAmnt = object.getDouble("TotalCheckAmt");
            totalUsedOnlineAmnt = object.getDouble("TotalOnlineAmt");


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setData(String assignmentID, ArrayList<AssignmentID> assignmentIdDataModel) {
        OrderIDArray.clear();
        try {
            for (int i = 0; i < assignmentIdDataModel.size(); i++) {
                String AssignmentID = String.valueOf(assignmentIdDataModel.get(i).getDeliveryIssuanceId());
                if (AssignmentID.equalsIgnoreCase(assignmentID)) {
                    ArrayList<DeliveryIssuanceDetail> deliveryIssuanceDetailsList = assignmentIdDataModel.get(i).getDeliveryIssuanceDetailDcs();
                    for (int j = 0; j < deliveryIssuanceDetailsList.size(); j++) {
                        for (int k = 0; k < deliveryIssuanceDetailsList.get(j).getPaymentDetails().size(); k++) {
                            if (deliveryIssuanceDetailsList.get(j).getPaymentDetails().get(k).getPaymentFrom().equalsIgnoreCase("Cash")) {
                                cashAmount = cashAmount + deliveryIssuanceDetailsList.get(j).getPaymentDetails().get(k).getAmount();
                            }
                            if (deliveryIssuanceDetailsList.get(j).getPaymentDetails().get(k).getPaymentFrom().equalsIgnoreCase("Cheque")) {
                                int Orderid = deliveryIssuanceDetailsList.get(j).getOrderId();
                                OrderIDArray.add(Orderid);
                                ChequeAmount = ChequeAmount + deliveryIssuanceDetailsList.get(j).getPaymentDetails().get(k).getAmount();
                            }
                            if (!deliveryIssuanceDetailsList.get(j).getPaymentDetails().get(k).getPaymentFrom().equalsIgnoreCase("Cheque") && !deliveryIssuanceDetailsList.get(j).getPaymentDetails().get(k).getPaymentFrom().equalsIgnoreCase("Cash")) {
                                OnlineAmount = OnlineAmount + deliveryIssuanceDetailsList.get(j).getPaymentDetails().get(k).getAmount();
                            }
                        }
                        /*if (deliveryIssuanceDetailsList.get(j).getCheckAmount() != 0) {
                            int Orderid = deliveryIssuanceDetailsList.get(j).getOrderId();
                            OrderIDArray.add(Orderid);
                        }*/
                    }
                }
            }
            TotalAmount = cashAmount + ChequeAmount + OnlineAmount;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void SubmitPopup() {
        LayoutInflater inflater = getLayoutInflater();
        if (activity != null) {
            final View mView = inflater.inflate(R.layout.confirmation_popup, null);
            customDialog = new Dialog(activity, R.style.CustomDialog);
            customDialog.setContentView(mView);
            TextView okBtn = mView.findViewById(R.id.btn_yes);
            TextView cancelBtn = mView.findViewById(R.id.btn_no);
            TextView textMesaage = mView.findViewById(R.id.tv_txt);
            textMesaage.setText("Please Confirm to Submit ");
            okBtn.setOnClickListener(v -> {
                customDialog.dismiss();
                if (postData() != null) {
                    mBinding.proRelatedItem.setVisibility(View.VISIBLE);
                    assignmentCashViewModel.hitcurrencyPostData(postData());

                } else {
                    Toast.makeText(activity, "No  Data", Toast.LENGTH_SHORT).show();
                }
            });
            cancelBtn.setOnClickListener(v -> customDialog.dismiss());
            customDialog.show();
        }
    }

    public CurrencyPostDataModel postData() {
        return new CurrencyPostDataModel(postOnlineCollectionModelArrayList == null ? new ArrayList<>() : postOnlineCollectionModelArrayList, postChequeCollectionModelArrayList == null ? new ArrayList<PostChequeCollectionModel>() : postChequeCollectionModelArrayList, postCashCollectionArrayList == null ? new ArrayList<PostCashCollection>() : postCashCollectionArrayList, peopleId, TotalAmount, totalUsedCheckAmnt, totalUsedOnlineAmnt, totalusedCashAmnt, selectedAssignmentId, peopleId, warehouseId, ID);
    }

    public void RetrieveData() {
        try {
            //setData
            if (flag) {
                totalUsedCheckAmnt = 0.0;
                totalusedCashAmnt = 0.0;
                totalUsedOnlineAmnt = 0.0;
                mBinding.assignAmnt.setText(String.format("₹ %s", TotalAmount));
                mBinding.etCaseAmount.setText(String.format("₹ %s", cashAmount));
                mBinding.etChequeAmount.setText(String.format("₹ %s", ChequeAmount));
                mBinding.etOnlineAmount.setText(String.format("₹ %s", OnlineAmount));

                mBinding.collectedCashamnt.setText(String.format("₹ %s", cashAmount));
                mBinding.collectedOnlineamnt.setText(String.format("₹ %s", OnlineAmount));
                mBinding.collectedChequeamnt.setText(String.format("₹ %s", ChequeAmount));
                //retriveData From sahrepref

                String Cashamount = SharePrefs.getCashmanagmentSharedPreferences(activity, SharePrefs.CASH_AMOUNT_JSON);
                String CheckAmount = SharePrefs.getCashmanagmentSharedPreferences(activity, SharePrefs.CHEQUE_AMOUNT);
                String onlineAmount = SharePrefs.getCashmanagmentSharedPreferences(activity, SharePrefs.ONLINE_AMOUNT);

                //  totalusedCashAmnt = SharePrefs.getInstance(activity).getInt(SharePrefs.CASH_AMOUNT);
                postChequeCollectionModelArrayList = new Gson().fromJson(CheckAmount, new TypeToken<ArrayList<PostChequeCollectionModel>>() {
                }.getType());
                postCashCollectionArrayList = new Gson().fromJson(Cashamount, new TypeToken<ArrayList<PostCashCollection>>() {
                }.getType());
                postOnlineCollectionModelArrayList = new Gson().fromJson(onlineAmount, new TypeToken<ArrayList<PostOnlineCollectionModel>>() {
                }.getType());

                if (postOnlineCollectionModelArrayList != null) {
                    for (int i = 0; i < postOnlineCollectionModelArrayList.size(); i++) {
                        totalUsedOnlineAmnt = totalUsedOnlineAmnt + postOnlineCollectionModelArrayList.get(i).getPaymentGetwayAmt() + postOnlineCollectionModelArrayList.get(i).getMposAmt();
                    }
                }
                if (postCashCollectionArrayList != null) {
                    for (int i = 0; i < postCashCollectionArrayList.size(); i++) {
                        totalusedCashAmnt = totalusedCashAmnt + (Integer.parseInt(postCashCollectionArrayList.get(i).getValue()) * postCashCollectionArrayList.get(i).getCurrencyCountByDBoy());
                    }
                }
                if (postChequeCollectionModelArrayList != null) {
                    for (int i = 0; i < postChequeCollectionModelArrayList.size(); i++) {
                        totalUsedCheckAmnt = totalUsedCheckAmnt + postChequeCollectionModelArrayList.get(i).getUsedChequeAmt();
                    }
                }
                totalCollectedAmount = totalUsedCheckAmnt + totalusedCashAmnt + totalUsedOnlineAmnt;
                mBinding.totalSubmittedAmnt.setText(String.format("₹ %s", totalCollectedAmount));

                mBinding.collectedDepositeamnt.setText(String.format("₹ %s", totalusedCashAmnt));
                mBinding.collectedChequedepositeamnt.setText(String.format("₹ %s", totalUsedCheckAmnt));
                mBinding.collectedOnlinedepositeamnt.setText(String.format("₹ %s", totalUsedOnlineAmnt));

                // Apply Conditions to set Check Icon
                if (Double.parseDouble(mBinding.etCaseAmount.getText().toString().replace("₹ ", "")) == totalusedCashAmnt && totalusedCashAmnt > 0) {
                    mBinding.cashImg.setImageResource(drawable.ic_check_green);
                } else {
                    mBinding.cashImg.setImageResource(drawable.ic_check_grey);

                }
                if (Double.parseDouble(mBinding.etChequeAmount.getText().toString().replace("₹ ", "")) == totalUsedCheckAmnt && totalUsedCheckAmnt > 0) {
                    mBinding.checkImg.setImageResource(drawable.ic_check_green);
                } else {
                    mBinding.checkImg.setImageResource(drawable.ic_check_grey);

                }
                if (Double.parseDouble(mBinding.etOnlineAmount.getText().toString().replace("₹ ", "")) == totalUsedOnlineAmnt && totalUsedOnlineAmnt > 0) {
                    mBinding.onlineImg.setImageResource(drawable.ic_check_green);
                } else {
                    mBinding.onlineImg.setImageResource(drawable.ic_check_grey);
                }

            } else {
                if (SharePrefs.getInstance(activity).getBoolean(SharePrefs.UPDATE_DATA)) {
                    totalUsedCheckAmnt = 0.0;
                    totalusedCashAmnt = 0.0;
                    totalUsedOnlineAmnt = 0.0;

                    mBinding.assignAmnt.setText(String.format("₹ %s", TotalAmount));
                    mBinding.etCaseAmount.setText(String.format("₹ %s", cashAmount));
                    mBinding.etChequeAmount.setText(String.format("₹ %s", ChequeAmount));
                    mBinding.etOnlineAmount.setText(String.format("₹ %s", OnlineAmount));

                    mBinding.collectedCashamnt.setText(String.format("₹ %s", cashAmount));
                    mBinding.collectedOnlineamnt.setText(String.format("₹ %s", OnlineAmount));
                    mBinding.collectedChequeamnt.setText(String.format("₹ %s", ChequeAmount));

                    String Cashamount = SharePrefs.getCashmanagmentSharedPreferences(activity, SharePrefs.CASH_AMOUNT_JSON);
                    String CheckAmount = SharePrefs.getCashmanagmentSharedPreferences(activity, SharePrefs.CHEQUE_AMOUNT);
                    String onlineAmount = SharePrefs.getCashmanagmentSharedPreferences(activity, SharePrefs.ONLINE_AMOUNT);

                    //  totalusedCashAmnt = SharePrefs.getInstance(activity).getInt(SharePrefs.CASH_AMOUNT);
                    postChequeCollectionModelArrayList = new Gson().fromJson(CheckAmount, new TypeToken<ArrayList<PostChequeCollectionModel>>() {
                    }.getType());
                    postCashCollectionArrayList = new Gson().fromJson(Cashamount, new TypeToken<ArrayList<PostCashCollection>>() {
                    }.getType());
                    postOnlineCollectionModelArrayList = new Gson().fromJson(onlineAmount, new TypeToken<ArrayList<PostOnlineCollectionModel>>() {
                    }.getType());

                    if (postOnlineCollectionModelArrayList != null) {
                        for (int i = 0; i < postOnlineCollectionModelArrayList.size(); i++) {
                            totalUsedOnlineAmnt = totalUsedOnlineAmnt + postOnlineCollectionModelArrayList.get(i).getPaymentGetwayAmt() + postOnlineCollectionModelArrayList.get(i).getMposAmt();
                        }
                    }
                    if (postCashCollectionArrayList != null) {
                        for (int i = 0; i < postCashCollectionArrayList.size(); i++) {
                            totalusedCashAmnt = totalusedCashAmnt + (Integer.parseInt(postCashCollectionArrayList.get(i).getValue()) * postCashCollectionArrayList.get(i).getCurrencyCountByDBoy());
                        }
                    }
                    if (postChequeCollectionModelArrayList != null) {
                        for (int i = 0; i < postChequeCollectionModelArrayList.size(); i++) {
                            totalUsedCheckAmnt = totalUsedCheckAmnt + postChequeCollectionModelArrayList.get(i).getUsedChequeAmt();
                        }
                    }
                    totalCollectedAmount = totalUsedCheckAmnt + totalusedCashAmnt + totalUsedOnlineAmnt;
                    mBinding.totalSubmittedAmnt.setText(String.format("₹ %s", totalCollectedAmount));

                    mBinding.collectedDepositeamnt.setText(String.format("₹ %s", totalusedCashAmnt));
                    mBinding.collectedChequedepositeamnt.setText(String.format("₹ %s", totalUsedCheckAmnt));
                    mBinding.collectedOnlinedepositeamnt.setText(String.format("₹ %s", totalUsedOnlineAmnt));


                    //Apply Conditions to set Check Icon
                    if (Double.parseDouble(mBinding.etCaseAmount.getText().toString().replace("₹ ", "")) == totalusedCashAmnt && totalusedCashAmnt > 0) {
                        mBinding.cashImg.setImageResource(drawable.ic_check_green);
                    } else {
                        mBinding.cashImg.setImageResource(drawable.ic_check_grey);

                    }
                    if (Double.parseDouble(mBinding.etChequeAmount.getText().toString().replace("₹ ", "")) == totalUsedCheckAmnt && totalUsedCheckAmnt > 0) {
                        mBinding.checkImg.setImageResource(drawable.ic_check_green);
                    } else {
                        mBinding.checkImg.setImageResource(drawable.ic_check_grey);

                    }
                    if (Double.parseDouble(mBinding.etOnlineAmount.getText().toString().replace("₹ ", "")) == totalUsedOnlineAmnt && totalUsedOnlineAmnt > 0) {
                        mBinding.onlineImg.setImageResource(drawable.ic_check_green);
                    } else {
                        mBinding.onlineImg.setImageResource(drawable.ic_check_grey);

                    }
                } else {
                    mBinding.assignAmnt.setText(String.format("₹ %s", TotalAmount));
                    mBinding.etCaseAmount.setText(String.format("₹ %s", cashAmount));
                    mBinding.etChequeAmount.setText(String.format("₹ %s", ChequeAmount));
                    mBinding.etOnlineAmount.setText(String.format("₹ %s", OnlineAmount));

                    mBinding.collectedCashamnt.setText(String.format("₹ %s", cashAmount));
                    mBinding.collectedOnlineamnt.setText(String.format("₹ %s", OnlineAmount));
                    mBinding.collectedChequeamnt.setText(String.format("₹ %s", ChequeAmount));


                    mBinding.collectedDepositeamnt.setText(String.format("₹ %s", totalusedCashAmnt));
                    mBinding.collectedChequedepositeamnt.setText(String.format("₹ %s", totalUsedCheckAmnt));
                    mBinding.collectedOnlinedepositeamnt.setText(String.format("₹ %s", totalUsedOnlineAmnt));

                    totalCollectedAmount = totalUsedCheckAmnt + totalusedCashAmnt + totalUsedOnlineAmnt;
                    mBinding.totalSubmittedAmnt.setText(String.format("₹ %s", totalCollectedAmount));


                    //Apply Conditions to set Check Icon
                    if (Double.parseDouble(mBinding.etCaseAmount.getText().toString().replace("₹ ", "")) == totalusedCashAmnt && totalusedCashAmnt > 0) {
                        mBinding.cashImg.setImageResource(drawable.ic_check_green);
                    } else {
                        mBinding.cashImg.setImageResource(drawable.ic_check_grey);

                    }
                    if (Double.parseDouble(mBinding.etChequeAmount.getText().toString().replace("₹ ", "")) == totalUsedCheckAmnt && totalUsedCheckAmnt > 0) {
                        mBinding.checkImg.setImageResource(drawable.ic_check_green);
                    } else {
                        mBinding.checkImg.setImageResource(drawable.ic_check_grey);

                    }
                    if (Double.parseDouble(mBinding.etOnlineAmount.getText().toString().replace("₹ ", "")) == totalUsedOnlineAmnt && totalUsedOnlineAmnt > 0) {
                        mBinding.onlineImg.setImageResource(drawable.ic_check_green);
                    } else {
                        mBinding.onlineImg.setImageResource(drawable.ic_check_grey);

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ResponseofPostData(ApiResponse apiResponse) {

        switch (apiResponse.status) {
            case LOADING:
                mBinding.proRelatedItem.setVisibility(View.VISIBLE);
                break;
            case SUCCESS:
                mBinding.proRelatedItem.setVisibility(View.GONE);
                SuccessResponseofPostData(apiResponse.data);
                break;

            case ERROR:
                mBinding.proRelatedItem.setVisibility(View.GONE);
                Utils.setToast(activity, getResources().getString(string.errorString));
                break;

            default:
                break;
        }
    }

    private void SuccessResponseofPostData(JsonElement response) {
        if (Utils.isJSONValid(response.toString())) {
            if (!response.isJsonNull()) {

                Log.d("response=", response.toString());
                try {

                    JSONObject obj = new JSONObject(response.toString());
                    if (obj.get("status").equals(true)) {

                        Toast.makeText(activity, obj.get("Message").toString(), Toast.LENGTH_SHORT).show();
                        SharePrefs.setCashmanagmentSharedPreference(activity, SharePrefs.ASSIGN_MODEL, null);
                        SharePrefs.getInstance(activity).putString(SharePrefs.ASSIGN_ID, null);
                        SharePrefs.setCashmanagmentSharedPreference(activity, SharePrefs.CASH_AMOUNT_JSON, null);
                        SharePrefs.setCashmanagmentSharedPreference(activity, SharePrefs.CHEQUE_AMOUNT, null);
                        SharePrefs.setCashmanagmentSharedPreference(activity, SharePrefs.ONLINE_AMOUNT, null);
                        SharePrefs.getInstance(activity).putInt(SharePrefs.CASH_AMOUNT, 0);
                        //activity.onBackPressed();
                        startActivity(new Intent(activity, MainActivity.class));

                    } else {
                        Toast.makeText(activity, obj.get("Message").toString(), Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(activity, getResources().getString(string.errorString), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(activity, response.toString(), Toast.LENGTH_SHORT).show();
        }


    }

    private void setActionBarConfiguration() {
        final DrawerLayout drawer = getActivity().findViewById(id.container);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        Toolbar toolbar = getActivity().findViewById(id.toolbar);
        toolbar.setVisibility(View.GONE);
        activity.toggle.syncState();
        activity.startBreak.setVisibility(View.GONE);

    }
}