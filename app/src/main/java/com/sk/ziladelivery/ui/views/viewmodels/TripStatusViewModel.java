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

public class TripStatusViewModel extends ViewModel {
    private final CompositeDisposable disposables = new CompositeDisposable();
    private MutableLiveData<ApiResponse> TripCurrentStatusLiveData;

    public LiveData<ApiResponse> getCurrentStatusResponse()
    {
        if (TripCurrentStatusLiveData == null)
        {
            TripCurrentStatusLiveData = new MutableLiveData<>();
        }
        if (TripCurrentStatusLiveData.getValue() != null && TripCurrentStatusLiveData.getValue().data != null)
        {
            TripCurrentStatusLiveData.setValue(null);
        }
        return TripCurrentStatusLiveData;
    }
    public void GetTripCurrentStatusAPI(int id) {
        DisposableObserver<JsonElement> observerDes = RestClient.getInstance().getService().getCheckStripStatus(id)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        TripCurrentStatusLiveData.setValue(ApiResponse.loading());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonElement>() {
                    @Override
                    public void onNext(JsonElement result) {
                        TripCurrentStatusLiveData.setValue(ApiResponse.success(result));
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        TripCurrentStatusLiveData.setValue(ApiResponse.error(throwable));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        disposables.add(observerDes);
    }

}
