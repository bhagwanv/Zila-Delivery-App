package com.sk.ziladelivery.ui.views.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonElement;
import com.sk.ziladelivery.data.api.RestClient;
import com.sk.ziladelivery.data.model.PaymentRequestModel;
import com.sk.ziladelivery.utilities.ApiResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class BillPaymentViewModel extends ViewModel {
    private final CompositeDisposable disposables = new CompositeDisposable();
    private MutableLiveData<ApiResponse> billPaymentML;
    private MutableLiveData<ApiResponse> validateChequepermissionLiveData;


    public LiveData<ApiResponse> getPayment()
    {
        if (billPaymentML == null)
        {
            billPaymentML = new MutableLiveData<>();
        }
        if (billPaymentML.getValue() != null && billPaymentML.getValue().data != null)
        {
            billPaymentML.setValue(null);
        }
        return billPaymentML;
    }
    public LiveData<ApiResponse> getchequepermission() {
        if (validateChequepermissionLiveData == null) {
            validateChequepermissionLiveData = new MutableLiveData<>();
        }
        if (validateChequepermissionLiveData.getValue() != null && validateChequepermissionLiveData.getValue().data != null) {
            validateChequepermissionLiveData.setValue(null);
        }
        return validateChequepermissionLiveData;
    }


    public void getBillObserver(PaymentRequestModel collectPaymentList) {
        DisposableObserver<JsonElement> observerDes = RestClient.getInstance().getService().submitPaymentApi(collectPaymentList)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        billPaymentML.setValue(ApiResponse.loading());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonElement>() {
                    @Override
                    public void onNext(JsonElement result) {
                        billPaymentML.setValue(ApiResponse.success(result));
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        billPaymentML.setValue(ApiResponse.error(throwable));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        disposables.add(observerDes);
    }

    public void verifyChequePermission(int custId) {
        DisposableObserver<JsonElement> observerDes = RestClient.getInstance().getService().getChequepermission(custId)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> validateChequepermissionLiveData.setValue(ApiResponse.loading()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonElement>() {
                    @Override
                    public void onNext(JsonElement result) {
                        validateChequepermissionLiveData.setValue(ApiResponse.success(result));
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        validateChequepermissionLiveData.setValue(ApiResponse.error(throwable));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        disposables.add(observerDes);
    }


}
