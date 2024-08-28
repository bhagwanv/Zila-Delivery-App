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

public class SettleAssignmentViewModel extends ViewModel {
    private final CompositeDisposable disposables = new CompositeDisposable();
    private MutableLiveData<ApiResponse> AssignmentList;
    private MutableLiveData<ApiResponse> bankNameResponseLiveData;

    public LiveData<ApiResponse> getAssignmentIDResponse() {
        if (AssignmentList == null) {
            AssignmentList = new MutableLiveData<>();
        }
        if (AssignmentList.getValue() != null && AssignmentList.getValue().data != null) {
            AssignmentList.setValue(null);
        }
        return AssignmentList;
    }
    public LiveData<ApiResponse> getBankname()
    {
        if (bankNameResponseLiveData == null) {
            bankNameResponseLiveData = new MutableLiveData<>();
        }
        if (bankNameResponseLiveData.getValue() != null && bankNameResponseLiveData.getValue().data != null) {
            bankNameResponseLiveData.setValue(null);
        }
        return bankNameResponseLiveData;
    }
    public void hitBankName() {
        DisposableObserver<JsonElement> observerDes = RestClient.getInstance().getService().getBankName()
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> bankNameResponseLiveData.setValue(ApiResponse.loading()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonElement>() {
                    @Override
                    public void onNext(JsonElement result) {
                        bankNameResponseLiveData.setValue(ApiResponse.success(result));
                        disposables.clear();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        bankNameResponseLiveData.setValue(ApiResponse.error(throwable));
                        disposables.clear();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        disposables.add(observerDes);
    }
    public void hitAssignmentId(int DBID,int WID) {

        DisposableObserver<JsonElement> observerDes = RestClient.getInstance().getService().getAssignmentIDDeatil(DBID,WID)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        AssignmentList.setValue(ApiResponse.loading());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonElement>() {
                    @Override
                    public void onNext(JsonElement result) {
                        AssignmentList.setValue(ApiResponse.success(result));
                        disposables.clear();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        AssignmentList.setValue(ApiResponse.error(throwable));
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
