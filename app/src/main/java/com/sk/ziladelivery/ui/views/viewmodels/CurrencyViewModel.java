package com.sk.ziladelivery.ui.views.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonElement;
import com.sk.ziladelivery.data.api.RestClient;
import com.sk.ziladelivery.data.model.CurrencyModel;
import com.sk.ziladelivery.utilities.ApiResponse;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class CurrencyViewModel extends ViewModel {
    private final CompositeDisposable disposables = new CompositeDisposable();

    private MutableLiveData<ApiResponse> myCurrencyMutableLiveDat;
    private MutableLiveData<ApiResponse> CurrencyDataList;

    private ArrayList<CurrencyModel> currencyList;

    public LiveData<ApiResponse> getCurrencyResponseData()
    {
        if (myCurrencyMutableLiveDat == null)
        {
            myCurrencyMutableLiveDat = new MutableLiveData<>();
        }
        if (myCurrencyMutableLiveDat.getValue() != null && myCurrencyMutableLiveDat.getValue().data != null)
        {
            myCurrencyMutableLiveDat.setValue(null);
        }
        return myCurrencyMutableLiveDat;
    }
     public LiveData<ApiResponse> getCurrencyData()
     {
        if (CurrencyDataList == null)
        {
            CurrencyDataList = new MutableLiveData<>();
        }
        if (CurrencyDataList.getValue() != null && CurrencyDataList.getValue().data != null)
        {
            CurrencyDataList.setValue(null);
        }
        return CurrencyDataList;
    }





    public void hitgetCurrencyData() {
        DisposableObserver<JsonElement> observerDes = RestClient.getInstance().getService().getCurrency()
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {

                        CurrencyDataList.setValue(ApiResponse.loading());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonElement>() {
                    @Override
                    public void onNext(JsonElement result) {
                        CurrencyDataList.setValue(ApiResponse.success(result));
                        disposables.clear();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        CurrencyDataList.setValue(ApiResponse.error(throwable));
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