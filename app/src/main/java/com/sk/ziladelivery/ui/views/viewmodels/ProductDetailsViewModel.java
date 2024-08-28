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

public class ProductDetailsViewModel extends ViewModel {
    private MutableLiveData<ApiResponse> productDetailsUnloadItemMutableLiveData;

    public LiveData<ApiResponse> getProductItems()
    {
        if (productDetailsUnloadItemMutableLiveData == null)
        {
            productDetailsUnloadItemMutableLiveData = new MutableLiveData<>();
        }
        if (productDetailsUnloadItemMutableLiveData.getValue() != null && productDetailsUnloadItemMutableLiveData.getValue().data != null)
        {
            productDetailsUnloadItemMutableLiveData.setValue(null);
        }
        return productDetailsUnloadItemMutableLiveData;
    }



    public void GetProductDetailsObserverApi( int orderID) {
        DisposableObserver<JsonElement> observerDes = RestClient.getInstance().getService().getOrderDetails(orderID)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        productDetailsUnloadItemMutableLiveData.setValue(ApiResponse.loading());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonElement>() {
                    @Override
                    public void onNext(JsonElement result) {
                        productDetailsUnloadItemMutableLiveData.setValue(ApiResponse.success(result));
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        productDetailsUnloadItemMutableLiveData.setValue(ApiResponse.error(throwable));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        disposables.add(observerDes);
    }

    private final CompositeDisposable disposables = new CompositeDisposable();

}
