package com.sk.ziladelivery.ui.views.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonElement;
import com.sk.ziladelivery.data.api.RestClient;
import com.sk.ziladelivery.data.model.GenerateOTPofSalesPersonforReattemptRequestModel;
import com.sk.ziladelivery.data.model.ReDispatchModel;
import com.sk.ziladelivery.data.model.ReDispatchOTPModelforMyTrip;
import com.sk.ziladelivery.utilities.ApiResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MyTripViewModel extends ViewModel {
    private final CompositeDisposable disposables = new CompositeDisposable();
    private MutableLiveData<ApiResponse> myTripOrderMutableLiveDat;
    private MutableLiveData<ApiResponse> notifyReDispatchReAttemptMutableLiveData;
    private MutableLiveData<ApiResponse> notifyALLReDispatchAndReAttemptLiveData;
    private MutableLiveData<ApiResponse> myTripRecodingMutableLiveDat;
    private MutableLiveData<ApiResponse> confirmOrderLiveData;
    private MutableLiveData<ApiResponse> skipAllLiveData;
    private MutableLiveData<ApiResponse> generateOrderOtpLiveData;
    private MutableLiveData<ApiResponse> mySingleTripMapOrderMutableLiveDat;
    private MutableLiveData<ApiResponse> orderCountMutableLiveDat;
    private MutableLiveData<ApiResponse> holidayOrderMutableLiveDat;
    private MutableLiveData<ApiResponse> GenerateOTPofSalesPersonforReattemptMutableLiveDat;
    private MutableLiveData<ApiResponse> validateOrderOtpLiveData;


    public LiveData<ApiResponse> getmyTripOrderResponseData() {
        if (myTripOrderMutableLiveDat == null) {
            myTripOrderMutableLiveDat = new MutableLiveData<>();
        }
        if (myTripOrderMutableLiveDat.getValue() != null && myTripOrderMutableLiveDat.getValue().data != null) {
            myTripOrderMutableLiveDat.setValue(null);
        }
        return myTripOrderMutableLiveDat;
    }

    public LiveData<ApiResponse> cofirmOrder() {
        if (confirmOrderLiveData == null) {
            confirmOrderLiveData = new MutableLiveData<>();
        }
        if (confirmOrderLiveData.getValue() != null && confirmOrderLiveData.getValue().data != null) {
            confirmOrderLiveData.setValue(null);
        }
        return confirmOrderLiveData;
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


    public LiveData<ApiResponse> getNotifyReDispatchReAttempt()
    {
        if (notifyReDispatchReAttemptMutableLiveData == null)
        {
            notifyReDispatchReAttemptMutableLiveData = new MutableLiveData<>();
        }
        if (notifyReDispatchReAttemptMutableLiveData.getValue() != null && notifyReDispatchReAttemptMutableLiveData.getValue().data != null)
        {
            notifyReDispatchReAttemptMutableLiveData.setValue(null);
        }
        return notifyReDispatchReAttemptMutableLiveData;
    }

    public LiveData<ApiResponse> getAllNotifyReDispatchReAttempt()
    {
        if (notifyALLReDispatchAndReAttemptLiveData == null)
        {
            notifyALLReDispatchAndReAttemptLiveData = new MutableLiveData<>();
        }
        if (notifyALLReDispatchAndReAttemptLiveData.getValue() != null && notifyALLReDispatchAndReAttemptLiveData.getValue().data != null)
        {
            notifyALLReDispatchAndReAttemptLiveData.setValue(null);
        }
        return notifyALLReDispatchAndReAttemptLiveData;
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


    public MutableLiveData<ApiResponse> getSkipAll(int id) {
        if (skipAllLiveData == null) {
            skipAllLiveData = new MutableLiveData<>();
        }
        if (skipAllLiveData.getValue() != null && skipAllLiveData.getValue().data != null) {
            skipAllLiveData.setValue(null);
        }
        callSkipAllApi(id);
        return skipAllLiveData;
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


    public LiveData<ApiResponse> getHolidayReAttempt() {
        if (holidayOrderMutableLiveDat == null) {
            holidayOrderMutableLiveDat = new MutableLiveData<>();
        }
        if (holidayOrderMutableLiveDat.getValue() != null && holidayOrderMutableLiveDat.getValue().data != null) {
            holidayOrderMutableLiveDat.setValue(null);
        }
        return holidayOrderMutableLiveDat;
    }

    public LiveData<ApiResponse> GenerateOTPofSalesPersonforReattempt() {
        if (GenerateOTPofSalesPersonforReattemptMutableLiveDat == null) {
            GenerateOTPofSalesPersonforReattemptMutableLiveDat = new MutableLiveData<>();
        }
        if (GenerateOTPofSalesPersonforReattemptMutableLiveDat.getValue() != null && GenerateOTPofSalesPersonforReattemptMutableLiveDat.getValue().data != null) {
            GenerateOTPofSalesPersonforReattemptMutableLiveDat.setValue(null);
        }
        return GenerateOTPofSalesPersonforReattemptMutableLiveDat;
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

    public void generateOrderOTPforAll( int orderid,String status, double lat, double lg) {
        DisposableObserver<JsonElement> observerDes = RestClient.getInstance().getService().getOtpForOrderTwo(orderid, status, lat, lg)
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


    public void getNotifyALLReDispatchAndReAttempt(ReDispatchModel model) {
        DisposableObserver<JsonElement> observerDes = RestClient.getInstance().getService().notifyALLReDispatchAndReAttempt(model)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        notifyALLReDispatchAndReAttemptLiveData.setValue(ApiResponse.loading());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonElement>() {
                    @Override
                    public void onNext(JsonElement result) {
                        notifyALLReDispatchAndReAttemptLiveData.setValue(ApiResponse.success(result));
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        notifyALLReDispatchAndReAttemptLiveData.setValue(ApiResponse.error(throwable));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        disposables.add(observerDes);
    }
    public void getNotifyReDispatchReAttemptMethod(ReDispatchModel model) {
        DisposableObserver<JsonElement> observerDes = RestClient.getInstance().getService().notifyReDispatchAndReAttemptAll(model)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> notifyReDispatchReAttemptMutableLiveData.setValue(ApiResponse.loading()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonElement>() {
                    @Override
                    public void onNext(JsonElement result) {
                        notifyReDispatchReAttemptMutableLiveData.setValue(ApiResponse.success(result));
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        notifyReDispatchReAttemptMutableLiveData.setValue(ApiResponse.error(throwable));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        disposables.add(observerDes);
    }
    public void GetMyTripOrderModel(int id) {
        DisposableObserver<JsonElement> observerDes = RestClient.getInstance().getService().getMytripOrder(id)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> myTripOrderMutableLiveDat.setValue(ApiResponse.loading()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonElement>() {
                    @Override
                    public void onNext(JsonElement result) {
                        myTripOrderMutableLiveDat.setValue(ApiResponse.success(result));
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        myTripOrderMutableLiveDat.setValue(ApiResponse.error(throwable));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        disposables.add(observerDes);
    }


    public void callSkipAllApi(int id) {
        DisposableObserver<JsonElement> observerDes = RestClient.getInstance().getService().
                getCallSkipAllTwo(id)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> skipAllLiveData.setValue(ApiResponse.loading()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonElement>() {
                    @Override
                    public void onNext(JsonElement result) {
                        skipAllLiveData.setValue(ApiResponse.success(result));
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        skipAllLiveData.setValue(ApiResponse.error(throwable));
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
                .doOnSubscribe(disposable -> myTripRecodingMutableLiveDat.setValue(ApiResponse.loading()))
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
    public void confirmOrderNotifyMethod(ReDispatchOTPModelforMyTrip model) {
        DisposableObserver<JsonElement> observerDes = RestClient.getInstance().getService().confirmOtpRedispatchReattemptAll(model)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> confirmOrderLiveData.setValue(ApiResponse.loading()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonElement>() {
                    @Override
                    public void onNext(JsonElement result) {
                        confirmOrderLiveData.setValue(ApiResponse.success(result));
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        confirmOrderLiveData.setValue(ApiResponse.error(throwable));
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
    public void getReAttempt(int id) {
        DisposableObserver<JsonElement> observerDes = RestClient.getInstance().getService().getHolidayOnReAttempt(id)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe((Consumer<Disposable>) disposable -> holidayOrderMutableLiveDat.setValue(ApiResponse.loading()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonElement>() {
                    @Override
                    public void onNext(JsonElement result) {
                        holidayOrderMutableLiveDat.setValue(ApiResponse.success(result));
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        holidayOrderMutableLiveDat.setValue(ApiResponse.error(throwable));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        disposables.add(observerDes);
    }
    public void generateOTPofSalesPersonforReattempt(GenerateOTPofSalesPersonforReattemptRequestModel model) {
        DisposableObserver<JsonElement> observerDes = RestClient.getInstance().getService().GenerateOTPofSalesPersonforReattempt(model)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe((Consumer<Disposable>) disposable -> GenerateOTPofSalesPersonforReattemptMutableLiveDat.setValue(ApiResponse.loading()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonElement>() {
                    @Override
                    public void onNext(JsonElement result) {
                        GenerateOTPofSalesPersonforReattemptMutableLiveDat.setValue(ApiResponse.success(result));
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        GenerateOTPofSalesPersonforReattemptMutableLiveDat.setValue(ApiResponse.error(throwable));
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
}
