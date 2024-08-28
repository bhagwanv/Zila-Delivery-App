package com.sk.ziladelivery.ui.views.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonElement;
import com.sk.ziladelivery.data.api.RestClient;
import com.sk.ziladelivery.data.model.OrderPlacedModel;
import com.sk.ziladelivery.data.model.PaymentRequestModel;
import com.sk.ziladelivery.utilities.ApiResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class OrderPlaceViewModel extends ViewModel {
    private final CompositeDisposable disposablesa = new CompositeDisposable();
    private final CompositeDisposable disposables = new CompositeDisposable();
    private MutableLiveData<ApiResponse> myOrderPlacedResponseMutableLiveData;
    private MutableLiveData<ApiResponse> bankNameResponseLiveData;
    private MutableLiveData<ApiResponse> checkOrderOtpExistLiveData;
    private MutableLiveData<ApiResponse> generateOrderOtpLiveData;
    private MutableLiveData<ApiResponse> validateOrderOtpLiveData;
    private MutableLiveData<ApiResponse> validateChequepermissionLiveData;
    private MutableLiveData<ApiResponse> verifyqrcode;
    private MutableLiveData<ApiResponse> submitPaymentLiveData;
    private MutableLiveData<ApiResponse> customerRTGSAmountLiveData;

    public OrderPlaceViewModel() {

    }

    public LiveData<ApiResponse> submitPayment() {
        if (submitPaymentLiveData == null) {
            submitPaymentLiveData = new MutableLiveData<>();
        }
        if (submitPaymentLiveData.getValue() != null && submitPaymentLiveData.getValue().data != null) {
            submitPaymentLiveData.setValue(null);
        }
        return submitPaymentLiveData;
    }


    public LiveData<ApiResponse> getverifyqrcodeData() {
        if (verifyqrcode == null) {
            verifyqrcode = new MutableLiveData<>();
        }
        if (verifyqrcode.getValue() != null && verifyqrcode.getValue().data != null) {
            verifyqrcode.setValue(null);
        }
        return verifyqrcode;
    }



    public LiveData<ApiResponse> getchequepermission() {
        if (validateChequepermissionLiveData == null) {
            validateChequepermissionLiveData = new MutableLiveData<>();
        }
        if (validateChequepermissionLiveData.getValue() != null && validateChequepermissionLiveData.getValue().data != null) {
            validateChequepermissionLiveData.setValue(null);
        }
        return validateChequepermissionLiveData;
    }

    public LiveData<ApiResponse> getCustomerRTGSAmount() {
        if (customerRTGSAmountLiveData == null) {
            customerRTGSAmountLiveData = new MutableLiveData<>();
        }
        if (customerRTGSAmountLiveData.getValue() != null && customerRTGSAmountLiveData.getValue().data != null) {
            customerRTGSAmountLiveData.setValue(null);
        }
        return customerRTGSAmountLiveData;
    }


    public LiveData<ApiResponse> getBankname() {
        if (bankNameResponseLiveData == null) {
            bankNameResponseLiveData = new MutableLiveData<>();
        }
        if (bankNameResponseLiveData.getValue() != null && bankNameResponseLiveData.getValue().data != null) {
            bankNameResponseLiveData.setValue(null);
        }
        return bankNameResponseLiveData;
    }

    public LiveData<ApiResponse> getOrderPlacedResponseData() {
        if (myOrderPlacedResponseMutableLiveData == null) {
            myOrderPlacedResponseMutableLiveData = new MutableLiveData<>();
        }
        if (myOrderPlacedResponseMutableLiveData.getValue() != null && myOrderPlacedResponseMutableLiveData.getValue().data != null) {
            myOrderPlacedResponseMutableLiveData.setValue(null);
        }
        return myOrderPlacedResponseMutableLiveData;
    }



    public void submitPaymentObserver(PaymentRequestModel paymentRequestModel) {
        DisposableObserver<JsonElement> observerDes = RestClient.getInstance().getService().submitPaymentApi(paymentRequestModel)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    submitPaymentLiveData.setValue(ApiResponse.loading());
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonElement>() {
                    @Override
                    public void onNext(JsonElement result) {
                        submitPaymentLiveData.setValue(ApiResponse.success(result));
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        submitPaymentLiveData.setValue(ApiResponse.error(throwable));                    }

                    @Override
                    public void onComplete() {

                    }
                });
        disposablesa.add(observerDes);
    }


    public void hitVerifyQrCode(int OrderId, int cashamt) {
        DisposableObserver<JsonElement> observerDes = RestClient.getInstance().getService().verifyQRCodeUPI(OrderId, cashamt)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    verifyqrcode.setValue(ApiResponse.loading());
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonElement>() {
                    @Override
                    public void onNext(JsonElement result) {
                        verifyqrcode.setValue(ApiResponse.success(result));

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        verifyqrcode.setValue(ApiResponse.error(throwable));

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        disposablesa.add(observerDes);
    }


    /*
     * Order Placed API call
     * */
    public void hitOrderPlacedApi(OrderPlacedModel orderPlacedModel) {
       /* Gson datajson = new Gson();
        String json = datajson.toJson(orderPlacedModel);
        System.out.println(json);*/
        DisposableObserver<JsonElement> observerDes = RestClient.getInstance().getService().postOrderPostData(orderPlacedModel)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> myOrderPlacedResponseMutableLiveData.setValue(ApiResponse.loading()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonElement>() {
                    @Override
                    public void onNext(JsonElement result) {
                        myOrderPlacedResponseMutableLiveData.setValue(ApiResponse.success(result));

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        myOrderPlacedResponseMutableLiveData.setValue(ApiResponse.error(throwable));

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        disposables.add(observerDes);
    }


    public void hitBankName() {
        DisposableObserver<JsonElement> observerDes = RestClient.getInstance().getService().getBankName()
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> bankNameResponseLiveData.setValue(ApiResponse.loading()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonElement>() {
                    @Override
                    public void onNext(JsonElement result) {
                        bankNameResponseLiveData.setValue(ApiResponse.success(result));
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        bankNameResponseLiveData.setValue(ApiResponse.error(throwable));

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        disposables.add(observerDes);
    }


    public void hitOrderOTPExist(int orderId, String status, String comment) {
        DisposableObserver<JsonElement> observerDes = RestClient.getInstance().getService().checkOrderOtpExist(orderId, status, comment)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> checkOrderOtpExistLiveData.setValue(ApiResponse.loading()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonElement>() {
                    @Override
                    public void onNext(JsonElement result) {
                        checkOrderOtpExistLiveData.setValue(ApiResponse.success(result));
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        checkOrderOtpExistLiveData.setValue(ApiResponse.error(throwable));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        disposables.add(observerDes);
    }

    public void generateOrderOTP(int orderId, String status, double lat, double lg) {
        DisposableObserver<JsonElement> observerDes = RestClient.getInstance().getService().getOtpForOrderTwo(orderId, status, lat, lg)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> generateOrderOtpLiveData.setValue(ApiResponse.loading()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonElement>() {
                    @Override
                    public void onNext(JsonElement result) {
                        generateOrderOtpLiveData.setValue(ApiResponse.success(result));
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        generateOrderOtpLiveData.setValue(ApiResponse.error(throwable));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        disposables.add(observerDes);
    }


    public void verifyChequePermission(int custId) {
        DisposableObserver<JsonElement> observerDes = RestClient.getInstance().getService().getChequepermission(custId)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> validateChequepermissionLiveData.setValue(ApiResponse.loading()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonElement>() {
                    @Override
                    public void onNext(JsonElement result) {
                        validateChequepermissionLiveData.setValue(ApiResponse.success(result));
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        validateChequepermissionLiveData.setValue(ApiResponse.error(throwable));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        disposables.add(observerDes);
    }


    public void customerRTGSAmountObserver(int custId) {
        DisposableObserver<JsonElement> observerDes = RestClient.getInstance().getService().GetCustomerRTGSAmount(custId)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> customerRTGSAmountLiveData.setValue(ApiResponse.loading()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonElement>() {
                    @Override
                    public void onNext(JsonElement result) {
                        customerRTGSAmountLiveData.setValue(ApiResponse.success(result));
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        customerRTGSAmountLiveData.setValue(ApiResponse.error(throwable));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        disposables.add(observerDes);
    }


    public LiveData<ApiResponse> checkOrderOTPExist()
    {
        if (checkOrderOtpExistLiveData == null) {
            checkOrderOtpExistLiveData = new MutableLiveData<>();
        }
        if (checkOrderOtpExistLiveData.getValue() != null && checkOrderOtpExistLiveData.getValue().data != null) {
            checkOrderOtpExistLiveData.setValue(null);
        }
        return checkOrderOtpExistLiveData;
    }

    public LiveData<ApiResponse> generateOrderOTP() {
        if (generateOrderOtpLiveData == null) {
            generateOrderOtpLiveData = new MutableLiveData<>();
        }
        if (generateOrderOtpLiveData.getValue() != null && generateOrderOtpLiveData.getValue().data != null) {
            generateOrderOtpLiveData.setValue(null);
        }
        return generateOrderOtpLiveData;
    }

    public LiveData<ApiResponse> validateOrderOTP() {
        if (validateOrderOtpLiveData == null) {
            validateOrderOtpLiveData = new MutableLiveData<>();
        }
        if (validateOrderOtpLiveData.getValue() != null && validateOrderOtpLiveData.getValue().data != null) {
            validateOrderOtpLiveData.setValue(null);
        }
        return validateOrderOtpLiveData;
    }

    @Override
    protected void onCleared() {
        disposablesa.clear();
        disposables.clear();
    }
}