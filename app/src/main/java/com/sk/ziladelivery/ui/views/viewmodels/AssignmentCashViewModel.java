package com.sk.ziladelivery.ui.views.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonElement;
import com.sk.ziladelivery.data.api.RestClient;
import com.sk.ziladelivery.data.model.CurrencyPostDataModel;
import com.sk.ziladelivery.utilities.ApiResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class AssignmentCashViewModel extends ViewModel {
     private final CompositeDisposable disposables = new CompositeDisposable();

    private MutableLiveData<ApiResponse> CurrencyPostData;




    public LiveData<ApiResponse> getPostCurrencyData() {
        if (CurrencyPostData == null) {
            CurrencyPostData = new MutableLiveData<>();
        }
        if (CurrencyPostData.getValue() != null && CurrencyPostData.getValue().data != null) {
            CurrencyPostData.setValue(null);
        }
        return CurrencyPostData;
    }
    /*
     * Assignment Id List API CALL
     * */

    public void hitcurrencyPostData(CurrencyPostDataModel currencyPostDataModel) {

        DisposableObserver<JsonElement> observerDes = RestClient.getInstance().getService().postCurrencyData(currencyPostDataModel)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        CurrencyPostData.setValue(ApiResponse.loading());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonElement>() {
                    @Override
                    public void onNext(JsonElement result) {
                        CurrencyPostData.setValue(ApiResponse.success(result));
                        disposables.clear();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        CurrencyPostData.setValue(ApiResponse.error(throwable));
                        disposables.clear();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
             disposables.add(observerDes);
    }

    @Override
    protected void onCleared() {
        disposables.dispose();
        disposables.clear();
    }

}
