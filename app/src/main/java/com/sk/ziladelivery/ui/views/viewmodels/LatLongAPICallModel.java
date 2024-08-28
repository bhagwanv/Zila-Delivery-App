package com.sk.ziladelivery.ui.views.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;
import com.sk.ziladelivery.data.api.RestClient;
import com.sk.ziladelivery.data.model.LatLongModel;
import com.sk.ziladelivery.utilities.ApiResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class LatLongAPICallModel extends ViewModel {
    private final CompositeDisposable disposablesa = new CompositeDisposable();
    private MutableLiveData<ApiResponse> startTimerMutableLiveData;


    public LiveData<ApiResponse> getLatLong() {
        if (startTimerMutableLiveData == null) {
            startTimerMutableLiveData = new MutableLiveData<>();
        }
        if (startTimerMutableLiveData.getValue() != null && startTimerMutableLiveData.getValue().data != null) {
            startTimerMutableLiveData.setValue(null);
        }

        return startTimerMutableLiveData;
    }


    public void Postlatlong( LatLongModel model) {
        DisposableObserver<JsonObject> observerDes = RestClient.getInstance().getService().postlatlong(model)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> startTimerMutableLiveData.setValue(ApiResponse.loading()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonObject>() {
                    @Override
                    public void onNext(JsonObject result) {
                        startTimerMutableLiveData.setValue(ApiResponse.success(result));
                        disposablesa.clear();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        startTimerMutableLiveData.setValue(ApiResponse.error(throwable));
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
