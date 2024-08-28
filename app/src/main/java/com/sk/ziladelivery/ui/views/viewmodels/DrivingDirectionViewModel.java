package com.sk.ziladelivery.ui.views.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonElement;
import com.sk.ziladelivery.data.api.RestClient;
import com.sk.ziladelivery.utilities.ApiResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class DrivingDirectionViewModel extends ViewModel {
    private MutableLiveData<ApiResponse> routemap;
    private final CompositeDisposable disposablesa = new CompositeDisposable();

    public LiveData<ApiResponse> getmapRoute() {
        if (routemap == null) {
            routemap = new MutableLiveData<>();
        }
        if (routemap.getValue() != null && routemap.getValue().data != null) {
            routemap.setValue(null);
        }
        return routemap;
    }

    public void hitRouteMap(int assginId) {
        DisposableObserver<JsonElement> observerDes = RestClient.getInstance().getService().getRouteMap(assginId)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    routemap.setValue(ApiResponse.loading());
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonElement>() {
                    @Override
                    public void onNext(JsonElement result) {
                        routemap.setValue(ApiResponse.success(result));

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        routemap.setValue(ApiResponse.error(throwable));

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        disposablesa.add(observerDes);
    }
}
