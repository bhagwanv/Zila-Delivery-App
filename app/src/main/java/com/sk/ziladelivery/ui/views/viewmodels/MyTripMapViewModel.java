package com.sk.ziladelivery.ui.views.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonElement;
import com.sk.ziladelivery.data.api.RestClient;
import com.sk.ziladelivery.utilities.ApiResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MyTripMapViewModel extends ViewModel {
    private final CompositeDisposable disposables = new CompositeDisposable();
    private MutableLiveData<ApiResponse> myTripMapOrderMutableLiveDat;
    private MutableLiveData<ApiResponse> myTripRecodingMutableLiveDat;
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

    public LiveData<ApiResponse> getTripRecodingResponce() {
        if (myTripRecodingMutableLiveDat == null) {
            myTripRecodingMutableLiveDat = new MutableLiveData<>();
        }
        if (myTripRecodingMutableLiveDat.getValue() != null && myTripRecodingMutableLiveDat.getValue().data != null) {
            myTripRecodingMutableLiveDat.setValue(null);
        }

        return myTripRecodingMutableLiveDat;
    }
    public void GetMyTripMapOrderModel(int id) {
        DisposableObserver<JsonElement> observerDes = RestClient.getInstance().getService().getMapViewOrderList(id)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
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
}
