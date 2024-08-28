package com.sk.ziladelivery.ui.views.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.JsonElement;
import com.sk.ziladelivery.data.api.RestClient;
import com.sk.ziladelivery.data.model.BackgroundServiceModel;
import com.sk.ziladelivery.utilities.ApiResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class BackgroundServiceViewModel {

    private MutableLiveData<ApiResponse> BackgroundServiceLiveData;
    private final CompositeDisposable disposables = new CompositeDisposable();

    public LiveData<ApiResponse> getBackgroundService() {
        if (BackgroundServiceLiveData == null) {
            BackgroundServiceLiveData = new MutableLiveData<>();
        }
        return BackgroundServiceLiveData;
    }

    public void hitBackgroundAPI(BackgroundServiceModel model) {
        disposables.add(RestClient.getInstance().getService().postBackgroundData(model)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        BackgroundServiceLiveData.setValue(ApiResponse.loading());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<JsonElement>() {
                            @Override
                            public void accept(JsonElement jsonElement) throws Exception {
                                BackgroundServiceLiveData.setValue(ApiResponse.success(jsonElement));

                            }

                        },
                        throwable -> BackgroundServiceLiveData.setValue(ApiResponse.error(throwable))
                ));

    }
}


