package com.sk.ziladelivery.ui.views.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonElement;
import com.sk.ziladelivery.data.api.RestClient;
import com.sk.ziladelivery.data.model.OrderPlacedModel;
import com.sk.ziladelivery.data.model.PostUnloadingModel;
import com.sk.ziladelivery.data.model.UnloadLocationModel;
import com.sk.ziladelivery.utilities.ApiResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MySingleTripMapViewModel extends ViewModel {
    private final CompositeDisposable disposables = new CompositeDisposable();
    private MutableLiveData<ApiResponse> mySingleTripMapOrderMutableLiveDat;
    private MutableLiveData<ApiResponse> mySingleTripMapOrderUpdateStatusMutableLiveDat;
    private MutableLiveData<ApiResponse> myTripMapOrderMutableLiveDat;
    private MutableLiveData<ApiResponse> unloadingOrderMutableLiveDat;
    private MutableLiveData<ApiResponse> myOrderDetailsMutableLiveData;
    private MutableLiveData<ApiResponse> myOrderPlacedResponseMutableLiveData;
    private MutableLiveData<ApiResponse> checkOrderOtpExistLiveData;
    private MutableLiveData<ApiResponse> generateOrderOtpLiveData;
    private MutableLiveData<ApiResponse> validateOrderOtpLiveData;
    private MutableLiveData<ApiResponse> myTripRecodingMutableLiveDat;
    private MutableLiveData<ApiResponse> completeTripMutableLiveDat;
    private MutableLiveData<ApiResponse> notifayCutomerNotictionML;
    private MutableLiveData<ApiResponse> orderCountMutableLiveDat;
    private MutableLiveData<ApiResponse> customerUnloadMutableLiveData;

    public LiveData<ApiResponse> sendNotifications() {
        if (notifayCutomerNotictionML == null) {
            notifayCutomerNotictionML = new MutableLiveData<>();
        }
        if (notifayCutomerNotictionML.getValue() != null && notifayCutomerNotictionML.getValue().data != null) {
            notifayCutomerNotictionML.setValue(null);
        }
        return notifayCutomerNotictionML;
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


    public LiveData<ApiResponse> getCompletetrip() {
        if (completeTripMutableLiveDat == null) {
            completeTripMutableLiveDat = new MutableLiveData<>();
        }
        if (completeTripMutableLiveDat.getValue() != null && completeTripMutableLiveDat.getValue().data != null) {
            completeTripMutableLiveDat.setValue(null);
        }
        return completeTripMutableLiveDat;
    }

    public LiveData<ApiResponse> getOrderCountStatus() {
        if (orderCountMutableLiveDat == null) {
            orderCountMutableLiveDat = new MutableLiveData<>();
        }
        if (orderCountMutableLiveDat.getValue() != null && orderCountMutableLiveDat.getValue().data != null) {
            orderCountMutableLiveDat.setValue(null);
        }
        return orderCountMutableLiveDat;
    }
    public LiveData<ApiResponse> postCustomerUnloadApi() {
        if (customerUnloadMutableLiveData == null) {
            customerUnloadMutableLiveData = new MutableLiveData<>();
        }
        if (customerUnloadMutableLiveData.getValue() != null && customerUnloadMutableLiveData.getValue().data != null) {
            customerUnloadMutableLiveData.setValue(null);
        }
        return customerUnloadMutableLiveData;
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
    public LiveData<ApiResponse> getOrderDetailsData() {
        if (myOrderDetailsMutableLiveData == null) {
            myOrderDetailsMutableLiveData = new MutableLiveData<>();
        }
        if (myOrderDetailsMutableLiveData.getValue() != null && myOrderDetailsMutableLiveData.getValue().data != null) {
            myOrderDetailsMutableLiveData.setValue(null);
        }
        return myOrderDetailsMutableLiveData;
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

    public LiveData<ApiResponse> getunloadingOrderResponseData()
    {
        if (unloadingOrderMutableLiveDat == null)
        {
            unloadingOrderMutableLiveDat = new MutableLiveData<>();
        }
        if (unloadingOrderMutableLiveDat.getValue() != null && unloadingOrderMutableLiveDat.getValue().data != null)
        {
            unloadingOrderMutableLiveDat.setValue(null);
        }
        return unloadingOrderMutableLiveDat;
    }


    public LiveData<ApiResponse> getmyTripOrderResponseData()
    {
        if (myTripMapOrderMutableLiveDat == null)
        {
            myTripMapOrderMutableLiveDat = new MutableLiveData<>();
        }
        if (myTripMapOrderMutableLiveDat.getValue() != null && myTripMapOrderMutableLiveDat.getValue().data != null)
        {
            myTripMapOrderMutableLiveDat.setValue(null);
        }
        return myTripMapOrderMutableLiveDat;
    }
    public LiveData<ApiResponse> getmySinngleTripOrderResponseData()
    {
        if (mySingleTripMapOrderMutableLiveDat == null)
        {
            mySingleTripMapOrderMutableLiveDat = new MutableLiveData<>();
        }
        if (mySingleTripMapOrderMutableLiveDat.getValue() != null && mySingleTripMapOrderMutableLiveDat.getValue().data != null)
        {
            mySingleTripMapOrderMutableLiveDat.setValue(null);
        }
        return mySingleTripMapOrderMutableLiveDat;
    }
    public LiveData<ApiResponse> getmySingleTripMapOrderUpdateStatus()
    {
        if (mySingleTripMapOrderUpdateStatusMutableLiveDat == null)
        {
            mySingleTripMapOrderUpdateStatusMutableLiveDat = new MutableLiveData<>();
        }
        if (mySingleTripMapOrderUpdateStatusMutableLiveDat.getValue() != null && mySingleTripMapOrderUpdateStatusMutableLiveDat.getValue().data != null)
        {
            mySingleTripMapOrderUpdateStatusMutableLiveDat.setValue(null);
        }
        return mySingleTripMapOrderUpdateStatusMutableLiveDat;
    }

    public LiveData<ApiResponse> getTripRecodingResponce() {
        if (myTripRecodingMutableLiveDat == null) {
            myTripRecodingMutableLiveDat = new MutableLiveData<>();
        }
        if (myTripRecodingMutableLiveDat.getValue() != null && myTripRecodingMutableLiveDat.getValue().data != null) {
            myTripRecodingMutableLiveDat.setValue(null);
        }

        return myTripRecodingMutableLiveDat;
    }

    public void GetUnloadingOrderAPI(PostUnloadingModel model) {
        DisposableObserver<JsonElement> observerDes = RestClient.getInstance().getService().getUnloadingAPI(model)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        unloadingOrderMutableLiveDat.setValue(ApiResponse.loading());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonElement>() {
                    @Override
                    public void onNext(JsonElement result) {
                        unloadingOrderMutableLiveDat.setValue(ApiResponse.success(result));
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        unloadingOrderMutableLiveDat.setValue(ApiResponse.error(throwable));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        disposables.add(observerDes);
    }

    public void getCompletTripStatus(int id,double lat,double lng) {
        DisposableObserver<JsonElement> observerDes = RestClient.getInstance().getService().getCompleteTripStatusChange(id,lat,lng)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        completeTripMutableLiveDat.setValue(ApiResponse.loading());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonElement>() {
                    @Override
                    public void onNext(JsonElement result) {
                        completeTripMutableLiveDat.setValue(ApiResponse.success(result));
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        completeTripMutableLiveDat.setValue(ApiResponse.error(throwable));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        disposables.add(observerDes);
    }


    public void getOderCountObserver(int id,double lat,double lng) {
        DisposableObserver<JsonElement> observerDes = RestClient.getInstance().getService().getCompleteTripStatusChange(id,lat,lng)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        orderCountMutableLiveDat.setValue(ApiResponse.loading());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonElement>() {
                    @Override
                    public void onNext(JsonElement result) {
                        orderCountMutableLiveDat.setValue(ApiResponse.success(result));
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        orderCountMutableLiveDat.setValue(ApiResponse.error(throwable));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        disposables.add(observerDes);
    }

    public void GetMySingleTripMapOrderModel(int id,double lat,double lng) {
        DisposableObserver<JsonElement> observerDes = RestClient.getInstance().getService().getSingleMapViewOrderList(id,lat,lng)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mySingleTripMapOrderMutableLiveDat.setValue(ApiResponse.loading());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonElement>() {
                    @Override
                    public void onNext(JsonElement result) {
                        mySingleTripMapOrderMutableLiveDat.setValue(ApiResponse.success(result));
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        mySingleTripMapOrderMutableLiveDat.setValue(ApiResponse.error(throwable));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        disposables.add(observerDes);
    }
    public void GetMySingleTripMapOrderForUpdateSttaus(int id) {
        DisposableObserver<JsonElement> observerDes = RestClient.getInstance().getService().getTripOrderForUpdateStatusTwo(id)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mySingleTripMapOrderUpdateStatusMutableLiveDat.setValue(ApiResponse.loading());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonElement>() {
                    @Override
                    public void onNext(JsonElement result) {
                        mySingleTripMapOrderUpdateStatusMutableLiveDat.setValue(ApiResponse.success(result));
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        mySingleTripMapOrderUpdateStatusMutableLiveDat.setValue(ApiResponse.error(throwable));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        disposables.add(observerDes);
    }
    public void GetMyTripMapOrderModel(int id) {
        DisposableObserver<JsonElement> observerDes = RestClient.getInstance().getService().getMapViewOrderList(id)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable)  {
                        myTripMapOrderMutableLiveDat.setValue(ApiResponse.loading());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonElement>() {
                    @Override
                    public void onNext(JsonElement result) {
                        myTripMapOrderMutableLiveDat.setValue(ApiResponse.success(result));
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        myTripMapOrderMutableLiveDat.setValue(ApiResponse.error(throwable));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        disposables.add(observerDes);
    }
    public void hitOrderDetailsApi(int OrderId) {
        DisposableObserver<JsonElement> observerDes = RestClient.getInstance().getService().getOrderDetails(OrderId)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonElement>() {
                    @Override
                    public void onNext(JsonElement result) {
                        myOrderDetailsMutableLiveData.setValue(ApiResponse.success(result));

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        myOrderDetailsMutableLiveData.setValue(ApiResponse.error(throwable));

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        disposables.add(observerDes);
    }
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

    public void getVoiceRecodeObserver(int tripID, int customerID) {
        DisposableObserver<JsonElement> observerDes = RestClient.getInstance().getService().TripCustVoiceRecord(tripID, customerID)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        myTripRecodingMutableLiveDat.setValue(ApiResponse.loading());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonElement>() {
                    @Override
                    public void onNext(JsonElement result) {
                        myTripRecodingMutableLiveDat.setValue(ApiResponse.success(result));
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        myTripRecodingMutableLiveDat.setValue(ApiResponse.error(throwable));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        disposables.add(observerDes);
    }

    public void sendNotificationsObserver(int customerID) {
        DisposableObserver<JsonElement> observerDes = RestClient.getInstance().getService().NotifyCustomer(customerID)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        notifayCutomerNotictionML.setValue(ApiResponse.loading());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonElement>() {
                    @Override
                    public void onNext(JsonElement result) {
                        notifayCutomerNotictionML.setValue(ApiResponse.success(result));
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        notifayCutomerNotictionML.setValue(ApiResponse.error(throwable));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        disposables.add(observerDes);
    }
    public void customerUnloadLocation(UnloadLocationModel unloadLocationModel) {
        DisposableObserver<JsonElement> observerDes = RestClient.getInstance().getService().customerUnloadLocationApi(unloadLocationModel)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        customerUnloadMutableLiveData.setValue(ApiResponse.loading());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonElement>() {
                    @Override
                    public void onNext(JsonElement result) {
                        customerUnloadMutableLiveData.setValue(ApiResponse.success(result));
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        customerUnloadMutableLiveData.setValue(ApiResponse.error(throwable));
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
    public void verifyOrderOTP(int orderId, String status, String otp) {
        DisposableObserver<JsonElement> observerDes = RestClient.getInstance().getService().validateOrderOtp(orderId, status, otp)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> validateOrderOtpLiveData.setValue(ApiResponse.loading()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonElement>() {
                    @Override
                    public void onNext(JsonElement result) {
                        validateOrderOtpLiveData.setValue(ApiResponse.success(result));
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        validateOrderOtpLiveData.setValue(ApiResponse.error(throwable));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        disposables.add(observerDes);
    }
    @Override
    protected void onCleared() {
        disposables.clear();
    }
}
