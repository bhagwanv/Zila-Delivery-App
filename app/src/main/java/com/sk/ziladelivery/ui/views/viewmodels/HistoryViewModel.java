package com.sk.ziladelivery.ui.views.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;
import com.sk.ziladelivery.data.api.RestClient;
import com.sk.ziladelivery.utilities.ApiResponseObj;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class HistoryViewModel extends ViewModel {
    private final CompositeDisposable disposablesa = new CompositeDisposable();
    private MutableLiveData<String> startTimerMutableLiveData;
    private MutableLiveData<String> stopTimerMutableLiveData;
    private MutableLiveData<ApiResponseObj> myOrderDetailsMutableLiveData;

    public LiveData<ApiResponseObj> getOrderDetailsData() {
        if (myOrderDetailsMutableLiveData == null) {
            myOrderDetailsMutableLiveData = new MutableLiveData<>();
        }
        if (myOrderDetailsMutableLiveData.getValue() != null && myOrderDetailsMutableLiveData.getValue().data != null) {
            myOrderDetailsMutableLiveData.setValue(null);
        }

        return myOrderDetailsMutableLiveData;
    }

    public HistoryViewModel() {

    }

    public void HistoryDetailsApi(String mob, String Startdate, String enddate, String dbid) {
        DisposableObserver<JsonObject> observerDes = RestClient.getInstance().getService().getPepoleMyTaskData(mob, Startdate, enddate, dbid)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        myOrderDetailsMutableLiveData.setValue(ApiResponseObj.loading());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonObject>() {
                    @Override
                    public void onNext(JsonObject result) {
                        myOrderDetailsMutableLiveData.setValue(ApiResponseObj.success(result));
                        disposablesa.clear();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        myOrderDetailsMutableLiveData.setValue(ApiResponseObj.error(throwable));
                        disposablesa.clear();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        disposablesa.add(observerDes);
    }

    @Override
    protected void onCleared() {
        // myOrderDetailsMutableLiveData.postValue(null);
        disposablesa.clear();
    }
}