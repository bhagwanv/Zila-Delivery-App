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

public class AssginmentSettleViewModel extends ViewModel {
    private final CompositeDisposable disposable = new CompositeDisposable();
    private MutableLiveData<ApiResponse> AssginmenmtLiveData;
    private MutableLiveData<ApiResponse> HistoryAssginmenmtLiveData;


    public LiveData<ApiResponse> getAssignment() {
        if (AssginmenmtLiveData == null) {
            AssginmenmtLiveData = new MutableLiveData<>();
        }
        if (AssginmenmtLiveData.getValue() != null && AssginmenmtLiveData.getValue().data != null) {
            AssginmenmtLiveData.setValue(null);
        }
        return AssginmenmtLiveData;
    }

    public LiveData<ApiResponse> getAssginmentHistory() {
        if (HistoryAssginmenmtLiveData == null) {
            HistoryAssginmenmtLiveData = new MutableLiveData<>();
        }
        if (HistoryAssginmenmtLiveData.getValue() != null && HistoryAssginmenmtLiveData.getValue().data != null) {
            HistoryAssginmenmtLiveData.setValue(null);
        }
        return HistoryAssginmenmtLiveData;
    }

    public void hitAssignmentApi(int dbid) {
        DisposableObserver<JsonElement> observerDes = RestClient.getInstance().getService().GetAssginmentToSettle(dbid)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> AssginmenmtLiveData.setValue(ApiResponse.loading()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonElement>() {
                    @Override
                    public void onNext(JsonElement result) {
                        AssginmenmtLiveData.setValue(ApiResponse.success(result));
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        AssginmenmtLiveData.setValue(ApiResponse.error(throwable));
                        AssginmenmtLiveData = null;
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        disposable.add(observerDes);
    }

    public void hitAssignmentHistoryApi(int SlipId, int AssignmentId, int DboyId, String StartDate, String EndDate, int PageNumber, int PageSize) {
        DisposableObserver<JsonElement> observerDes = RestClient.getInstance().getService().GetAssginmentHistoryToSettle(SlipId, AssignmentId, DboyId, StartDate, EndDate, PageNumber, PageSize)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> HistoryAssginmenmtLiveData.setValue(ApiResponse.loading()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonElement>() {
                    @Override
                    public void onNext(JsonElement result) {
                        HistoryAssginmenmtLiveData.setValue(ApiResponse.success(result));
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        HistoryAssginmenmtLiveData.setValue(ApiResponse.error(throwable));
                        HistoryAssginmenmtLiveData = null;
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        disposable.add(observerDes);
    }

    @Override
    protected void onCleared() {
        disposable.dispose();
        disposable.clear();
    }
}
