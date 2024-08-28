package com.sk.ziladelivery.ui.views.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sk.ziladelivery.R;
import com.sk.ziladelivery.data.api.RestClient;
import com.sk.ziladelivery.data.model.AssignmentID;
import com.sk.ziladelivery.data.model.DeliveryIssuanceDetail;
import com.sk.ziladelivery.data.model.PostOnlineCollectionModel;
import com.sk.ziladelivery.databinding.OnlinePaymentBinding;
import com.sk.ziladelivery.listener.OnlinePaymentListener;
import com.sk.ziladelivery.listener.RTGSEditListener;
import com.sk.ziladelivery.ui.views.adapter.MposPaymentAdapter;
import com.sk.ziladelivery.ui.views.adapter.OnlinepaymentAdapter;
import com.sk.ziladelivery.ui.views.main.MainActivity;
import com.sk.ziladelivery.utilities.SharePrefs;
import com.sk.ziladelivery.utilities.TextUtils;
import com.sk.ziladelivery.utilities.Utils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class OnlinePaymentFragment extends Fragment implements RTGSEditListener {
    private MainActivity activity;
    private OnlinePaymentBinding mBinding;

    private OnlinepaymentAdapter adapter;
    private MposPaymentAdapter mposPaymentAdapter;

    private int AssignmentID;
    private double AllAmount = 0.0;
    private double mposAmount = 0.0;
    private double onlineAmount = 0.0;
    private ArrayList<PostOnlineCollectionModel> onlineModellist;
    private ArrayList<PostOnlineCollectionModel> mposModellist;
    private ArrayList<PostOnlineCollectionModel> saveList;
    private ArrayList<DeliveryIssuanceDetail> deliveryIssuanceDetailsList;

    private String historyModel, TransRefNo;
    private Dialog customDialog;
    boolean isFlag = true;
    AlertDialog dialogRTGS;


    public OnlinePaymentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        this.activity = (MainActivity) context;
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            mBinding = DataBindingUtil.inflate(inflater, R.layout.online_payment, container, false);
            Bundle bundle = this.getArguments();
            if (bundle != null) {
                AssignmentID = bundle.getInt("AssignmentID");
                AllAmount = bundle.getDouble("OnlineAmount");
            }
            initView();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBinding.setOnlinePaymentListener(new OnlinePaymentListener() {
            boolean isCheckDetilsFlag;
            boolean isMposDetilsFlag;

            @Override
            public void onlineLayHideUnHideClicked() {
                if (isCheckDetilsFlag) {
                    mBinding.recyclerOnline.setVisibility(View.VISIBLE);
                    isCheckDetilsFlag = false;
                } else {
                    mBinding.recyclerOnline.setVisibility(View.GONE);
                    isCheckDetilsFlag = true;
                }
            }

            @Override
            public void mposLayHideUnHideClicked() {
                if (isMposDetilsFlag) {
                    mBinding.recyclerMpos.setVisibility(View.VISIBLE);
                    isMposDetilsFlag = false;
                } else {
                    mBinding.recyclerMpos.setVisibility(View.GONE);
                    isMposDetilsFlag = true;
                }
            }
        });
        mBinding.submit.setOnClickListener(v -> SubmitPopup());
        mBinding.back.setOnClickListener(v -> activity.onBackPressed());
    }


    @Override
    public void isEditRTGS(String paymentReferenceNO, int orderid, int deliveryIssuanceId, int paymentResponseRetailerAppId) {
        editRTGSDialog(paymentReferenceNO, orderid, deliveryIssuanceId, paymentResponseRetailerAppId);
    }


    private void initView() {
        onlineModellist = new ArrayList<>();
        mposModellist = new ArrayList<>();
        deliveryIssuanceDetailsList = new ArrayList<>();

        mBinding.recyclerMpos.addItemDecoration(new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL));
        mBinding.recyclerOnline.addItemDecoration(new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL));

        boolean flag = SharePrefs.getInstance(activity).getBoolean(SharePrefs.FLAG);
        if (flag) {
            mBinding.onlinePaidamnt.setText(String.format("₹ %s", AllAmount));
            String Assignmodel = SharePrefs.getCashmanagmentSharedPreferences(activity, SharePrefs.ASSIGN_MODEL);
            String selectedAssignmentId = SharePrefs.getInstance(activity).getString(SharePrefs.ASSIGN_ID);
            ArrayList<AssignmentID> DeliveryIssuanceDcs = new Gson().fromJson(Assignmodel, new TypeToken<ArrayList<AssignmentID>>() {
            }.getType());

            String OnlineAmount = SharePrefs.getCashmanagmentSharedPreferences(activity, SharePrefs.ONLINE_AMOUNT);
            if (OnlineAmount != null) {
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<PostOnlineCollectionModel>>() {
                }.getType();
                saveList = gson.fromJson(OnlineAmount, type);
                if (saveList != null) {
                    for (int j = 0; j < saveList.size(); j++) {
                        if (/*!saveList.get(j).getPaymentFrom().equalsIgnoreCase("mPos") &&*/ !saveList.get(j).getPaymentFrom().equalsIgnoreCase("Cheque") && !saveList.get(j).getPaymentFrom().equalsIgnoreCase("Cash")) {
                            onlineAmount = onlineAmount + saveList.get(j).getPaymentGetwayAmt();
                            onlineModellist.add(new PostOnlineCollectionModel(saveList.get(j).getOrderid(), saveList.get(j).getPaymentGetwayAmt(), 0.0, 0, saveList.get(j).getOrderid(), "", saveList.get(j).getPaymentReferenceNO(), saveList.get(j).getPaymentFrom(), saveList.get(j).isEditRTGS(), saveList.get(j).getDeliveryIssuanceId(), saveList.get(j).paymentResponseRetailerAppId));
                        }
                    }
                } else {
                    setData(selectedAssignmentId + "", DeliveryIssuanceDcs);
                    for (int j = 0; j < deliveryIssuanceDetailsList.size(); j++) {
                        for (int i = 0; i < deliveryIssuanceDetailsList.get(j).getPaymentDetails().size(); i++) {
                            if (/*!deliveryIssuanceDetailsList.get(j).getPaymentDetails().get(i).getPaymentFrom().equalsIgnoreCase("mPos") && */!deliveryIssuanceDetailsList.get(j).getPaymentDetails().get(i).getPaymentFrom().equalsIgnoreCase("Cheque") && !deliveryIssuanceDetailsList.get(j).getPaymentDetails().get(i).getPaymentFrom().equalsIgnoreCase("Cash")) {
                                onlineAmount = onlineAmount + deliveryIssuanceDetailsList.get(j).getPaymentDetails().get(i).getAmount();
                                double onlineAmount = deliveryIssuanceDetailsList.get(j).getPaymentDetails().get(i).getAmount();
                                TransRefNo = deliveryIssuanceDetailsList.get(j).getPaymentDetails().get(i).getTransRefNo();
                                onlineModellist.add(new PostOnlineCollectionModel(deliveryIssuanceDetailsList.get(j).getOrderId(), onlineAmount, 0.0, 0, deliveryIssuanceDetailsList.get(j).getOrderId(), "", TransRefNo, deliveryIssuanceDetailsList.get(j).getPaymentDetails().get(i).getPaymentFrom(), deliveryIssuanceDetailsList.get(j).getPaymentDetails().get(i).isEditRTGS(), deliveryIssuanceDetailsList.get(j).getPaymentDetails().get(i).getDeliveryIssuanceId(), deliveryIssuanceDetailsList.get(j).getPaymentDetails().get(i).paymentResponseRetailerAppId));
                            }
                        }
                    }
                }
            } else {
                setData(selectedAssignmentId + "", DeliveryIssuanceDcs);
                for (int j = 0; j < deliveryIssuanceDetailsList.size(); j++) {
                    for (int i = 0; i < deliveryIssuanceDetailsList.get(j).getPaymentDetails().size(); i++) {
                        if (/*!deliveryIssuanceDetailsList.get(j).getPaymentDetails().get(i).getPaymentFrom().equalsIgnoreCase("mPos") &&*/ !deliveryIssuanceDetailsList.get(j).getPaymentDetails().get(i).getPaymentFrom().equalsIgnoreCase("Cheque") && !deliveryIssuanceDetailsList.get(j).getPaymentDetails().get(i).getPaymentFrom().equalsIgnoreCase("Cash")) {
                            onlineAmount = onlineAmount + deliveryIssuanceDetailsList.get(j).getPaymentDetails().get(i).getAmount();
                            double onlineAmount = deliveryIssuanceDetailsList.get(j).getPaymentDetails().get(i).getAmount();
                            TransRefNo = deliveryIssuanceDetailsList.get(j).getPaymentDetails().get(i).getTransRefNo();
                            onlineModellist.add(new PostOnlineCollectionModel(deliveryIssuanceDetailsList.get(j).getOrderId(), onlineAmount, 0.0, 0, deliveryIssuanceDetailsList.get(j).getOrderId(), "", TransRefNo, deliveryIssuanceDetailsList.get(j).getPaymentDetails().get(i).getPaymentFrom(), deliveryIssuanceDetailsList.get(j).getPaymentDetails().get(i).isEditRTGS(), deliveryIssuanceDetailsList.get(j).getPaymentDetails().get(i).getDeliveryIssuanceId(), deliveryIssuanceDetailsList.get(j).getPaymentDetails().get(i).paymentResponseRetailerAppId));
                        }
                    }
                }
            }
        } else {
            mBinding.onlinePaidamnt.setText(String.format("₹ %s", AllAmount));
            historyModel = SharePrefs.getCashmanagmentSharedPreferences(activity, SharePrefs.HISTORY_MODEL);
            setHistoryData(historyModel);
        }

        mBinding.onlinePaidaamnt.setText(String.format("₹ %s", onlineAmount));
        mBinding.mposPaidaamnt.setText(String.format("₹ %s", mposAmount));

        if (onlineModellist.size() > 0) {
            adapter = new OnlinepaymentAdapter(activity, onlineModellist, this);
            mBinding.recyclerOnline.setAdapter(adapter);
        }
        if (mposModellist.size() > 0) {
            mposPaymentAdapter = new MposPaymentAdapter(activity, mposModellist);
            mBinding.recyclerMpos.setAdapter(mposPaymentAdapter);
        }
    }

    private void setHistoryData(String historyModel) {
        try {
            String OnlineAmount = SharePrefs.getCashmanagmentSharedPreferences(activity, SharePrefs.ONLINE_AMOUNT);
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<PostOnlineCollectionModel>>() {
            }.getType();
            if (OnlineAmount != null) {
                saveList = gson.fromJson(OnlineAmount, type);
                if (saveList != null) {
                    for (int j = 0; j < saveList.size(); j++) {
                        if (saveList.get(j).getMposAmt() != 0) {
                            mposAmount = mposAmount + saveList.get(j).getMposAmt();
                            mposModellist.add(new PostOnlineCollectionModel(saveList.get(j).getOrderid(), 0.0, saveList.get(j).getMposAmt(), 0, saveList.get(j).getOrderid(), saveList.get(j).getMposReferenceNo(), "", saveList.get(j).getPaymentFrom(), saveList.get(j).isEditRTGS(), saveList.get(j).getDeliveryIssuanceId(), saveList.get(j).paymentResponseRetailerAppId));
                        } else {
                            onlineAmount = onlineAmount + saveList.get(j).getPaymentGetwayAmt();
                            onlineModellist.add(new PostOnlineCollectionModel(saveList.get(j).getOrderid(), saveList.get(j).getPaymentGetwayAmt(), 0.0, 0, saveList.get(j).getOrderid(), "", saveList.get(j).getPaymentReferenceNO(), saveList.get(j).getPaymentFrom(), saveList.get(j).isEditRTGS(), saveList.get(j).getDeliveryIssuanceId(), saveList.get(j).paymentResponseRetailerAppId));
                        }
                    }
                } else {
                    JSONObject obj = new JSONObject(historyModel);
                    ArrayList<PostOnlineCollectionModel> CollectionArrayList = gson.fromJson(obj.get("OnlineCollections").toString(), type);
                    for (int i = 0; i < CollectionArrayList.size(); i++) {
                        if (CollectionArrayList.get(i).getMposAmt() != 0) {
                            mposAmount = mposAmount + CollectionArrayList.get(i).getMposAmt();
                            mposModellist.add(new PostOnlineCollectionModel(CollectionArrayList.get(i).getOrderid(), 0.0, CollectionArrayList.get(i).getMposAmt(), 0, CollectionArrayList.get(i).getOrderid(), CollectionArrayList.get(i).getMposReferenceNo(), "", CollectionArrayList.get(i).getPaymentFrom(), CollectionArrayList.get(i).isEditRTGS(), CollectionArrayList.get(i).getDeliveryIssuanceId(), CollectionArrayList.get(i).paymentResponseRetailerAppId));
                        } else {
                            onlineAmount = onlineAmount + CollectionArrayList.get(i).getPaymentGetwayAmt();
                            onlineModellist.add(new PostOnlineCollectionModel(CollectionArrayList.get(i).getOrderid(), CollectionArrayList.get(i).getPaymentGetwayAmt(), 0.0, 0, CollectionArrayList.get(i).getOrderid(), "", CollectionArrayList.get(i).getPaymentReferenceNO(), CollectionArrayList.get(i).getPaymentFrom(), CollectionArrayList.get(i).isEditRTGS(), CollectionArrayList.get(i).getDeliveryIssuanceId(), CollectionArrayList.get(i).paymentResponseRetailerAppId));
                        }
                    }
                }
            } else {
                JSONObject obj = new JSONObject(historyModel);
                ArrayList<PostOnlineCollectionModel> CollectionArrayList = gson.fromJson(obj.get("OnlineCollections").toString(), type);
                for (int i = 0; i < CollectionArrayList.size(); i++) {
                    if (CollectionArrayList.get(i).getMposAmt() != 0) {
                        mposAmount = mposAmount + CollectionArrayList.get(i).getMposAmt();
                        mposModellist.add(new PostOnlineCollectionModel(CollectionArrayList.get(i).getOrderid(), 0.0, CollectionArrayList.get(i).getMposAmt(), 0, CollectionArrayList.get(i).getOrderid(), CollectionArrayList.get(i).getMposReferenceNo(), "", CollectionArrayList.get(i).getPaymentFrom(), CollectionArrayList.get(i).isEditRTGS(), CollectionArrayList.get(i).getDeliveryIssuanceId(), CollectionArrayList.get(i).paymentResponseRetailerAppId));
                    } else {
                        onlineAmount = onlineAmount + CollectionArrayList.get(i).getPaymentGetwayAmt();
                        onlineModellist.add(new PostOnlineCollectionModel(CollectionArrayList.get(i).getOrderid(), CollectionArrayList.get(i).getPaymentGetwayAmt(), 0.0, 0, CollectionArrayList.get(i).getOrderid(), "", CollectionArrayList.get(i).getPaymentReferenceNO(), CollectionArrayList.get(i).getPaymentFrom(), CollectionArrayList.get(i).isEditRTGS(), CollectionArrayList.get(i).getDeliveryIssuanceId(), CollectionArrayList.get(i).paymentResponseRetailerAppId));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setData(String assignmentID, ArrayList<AssignmentID> deliveryIssuanceDcs) {
        deliveryIssuanceDetailsList.clear();
        try {
            for (int i = 0; i < deliveryIssuanceDcs.size(); i++) {
                String AssignmentID = String.valueOf(deliveryIssuanceDcs.get(i).getDeliveryIssuanceId());
                if (AssignmentID.equalsIgnoreCase(assignmentID)) {
                    deliveryIssuanceDetailsList = deliveryIssuanceDcs.get(i).getDeliveryIssuanceDetailDcs();


                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void SubmitPopup() {

        LayoutInflater inflater = getLayoutInflater();
        if (activity != null && inflater != null) {
            final View mView = inflater.inflate(R.layout.confirmation_popup, null);
            customDialog = new Dialog(activity, R.style.CustomDialog);
            customDialog.setContentView(mView);
            TextView okBtn = mView.findViewById(R.id.btn_yes);
            TextView cancelBtn = mView.findViewById(R.id.btn_no);
            TextView textMesaage = mView.findViewById(R.id.tv_txt);
            textMesaage.setText(getContext().getResources().getString(R.string.online_summary));
            okBtn.setOnClickListener(v -> {
                ArrayList<PostOnlineCollectionModel> list = new ArrayList<>();
                ArrayList<PostOnlineCollectionModel> mposlist = new ArrayList<>();

                if (onlineModellist.size() > 0 && onlineModellist != null) {
                    if (adapter.getList() != null) {
                        list = adapter.getList();
                    }
                }

                if (mposModellist.size() > 0 && mposModellist != null) {
                    if (mposPaymentAdapter.getList().size() > 0) {
                        mposlist = mposPaymentAdapter.getList();

                    }
                }
                for (int i = 0; i < mposlist.size(); i++) {
                    if (TextUtils.isNullOrEmpty(mposlist.get(i).getMposReferenceNo())) {
                        isFlag = false;
                        break;
                    } else {
                        isFlag = true;
                    }
                }
                if (isFlag) {
                    mposlist.addAll(list);
                    Gson gson = new Gson();
                    SharePrefs.setCashmanagmentSharedPreference(activity, SharePrefs.ONLINE_AMOUNT, gson.toJson(mposlist));
                    System.out.println("json" + SharePrefs.getCashmanagmentSharedPreferences(activity, SharePrefs.ONLINE_AMOUNT));
                    activity.onBackPressed();
                    customDialog.dismiss();
                } else {
                    Toast.makeText(activity, R.string.enter_reference_id, Toast.LENGTH_SHORT).show();

                }
            });
            cancelBtn.setOnClickListener(v -> customDialog.dismiss());
            customDialog.show();
        }
    }

    private void editRTGSDialog(String paymentReferenceNO, int orderid, int deliveryIssuanceId, int paymentResponseRetailerAppId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Update RTGS Number");
        final View customLayout = getLayoutInflater().inflate(R.layout.rtgs_update_layout, null);
        EditText editText = customLayout.findViewById(R.id.etupdatertgs);
        editText.setText(paymentReferenceNO);
        builder.setView(customLayout);
        builder.setCancelable(false);

        builder.setPositiveButton("OK",
                (dialog, which) -> {
                    if (!Utils.checkInternetConnection(getActivity())) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                    } else {
                        if (TextUtils.isNullOrEmpty(editText.getText().toString())) {
                            Utils.setToast(getActivity(), "Please Enter Reference Number");
                        } else {
                            VerifyRefNo(deliveryIssuanceId, editText.getText().toString().trim(), orderid, paymentResponseRetailerAppId);
                        }
                    }
                });

        dialogRTGS = builder.create();
        dialogRTGS.show();
    }

    private void VerifyRefNo(int assginId, String refno, int orderId, int paymentResponseRetailerAppId) {
        RestClient.getInstance().getService().checkRefNo(assginId, refno, orderId)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> Utils.showProgressDialog(getActivity()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NotNull Boolean response) {
                        Utils.hideProgressDialog();
                        if (response) {
                            Utils.setToast(getActivity(), "This Reference Number is Already Used");
                        } else {
                            updateRTGSApi(orderId, refno, paymentResponseRetailerAppId);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Utils.hideProgressDialog();
                    }

                    @Override
                    public void onComplete() {
                        Utils.hideProgressDialog();
                    }
                });
    }

    private void updateRTGSApi(int orderId, String refno, int paymentResponseRetailerAppId) {
        RestClient.getInstance().getService().UpdateRefNumberForOrder(orderId, refno, paymentResponseRetailerAppId)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> Utils.showProgressDialog(getActivity()))

                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NotNull Boolean response) {
                        Utils.hideProgressDialog();
                        if (response) {
                            if (!Utils.checkInternetConnection(getActivity())) {
                                Utils.setToast(getActivity(), getResources().getString(R.string.network_error));
                            } else {
                                activity.switchContentWithStack(new SettleAssignmentFragment());
                                dialogRTGS.dismiss();
                            }
                        } else {

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Utils.hideProgressDialog();
                        Log.d("TAG", "Error:" + e.toString());

                    }

                    @Override
                    public void onComplete() {
                        Utils.hideProgressDialog();
                    }
                });
    }
}