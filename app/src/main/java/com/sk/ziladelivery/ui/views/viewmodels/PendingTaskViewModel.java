package com.sk.ziladelivery.ui.views.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sk.ziladelivery.data.api.RestClient;
import com.sk.ziladelivery.data.model.AcceptModel;
import com.sk.ziladelivery.utilities.ApiResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class PendingTaskViewModel extends ViewModel {
    private final CompositeDisposable disposables = new CompositeDisposable();
    private MutableLiveData<ApiResponse> myTaskModelMutableLiveDat;
    private MutableLiveData<ApiResponse> acceptMyTaskMutableLiveDat;
    private MutableLiveData<ApiResponse> acceptAssignmentMutableLiveDat;
    private MutableLiveData<ApiResponse> cancellationMutableLiveData;


    public LiveData<ApiResponse> getCancellationData() {
        if (cancellationMutableLiveData == null) {
            cancellationMutableLiveData = new MutableLiveData<>();
        }
        return cancellationMutableLiveData;
    }
    public LiveData<ApiResponse> getPendingTaskData() {
        if (myTaskModelMutableLiveDat == null) {
            myTaskModelMutableLiveDat = new MutableLiveData<>();
        }
        return myTaskModelMutableLiveDat;
    }

    public LiveData<ApiResponse> getAcceptMyTaskData() {
        if (acceptMyTaskMutableLiveDat == null) {
            acceptMyTaskMutableLiveDat = new MutableLiveData<>();
        }
        return acceptMyTaskMutableLiveDat;
    }

    public LiveData<ApiResponse> getAcceptAssignmentDistanceData() {
        if (acceptAssignmentMutableLiveDat == null) {
            acceptAssignmentMutableLiveDat = new MutableLiveData<>();
        }
        return acceptAssignmentMutableLiveDat;
    }

    /*
     * My task API call
     * */
    public void pendingMyTaskAdi(int peopleID) {
        disposables.add(RestClient.getInstance().getService().getPepoleMyTaskData(String.valueOf(peopleID))
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> myTaskModelMutableLiveDat.setValue(ApiResponse.loading()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> myTaskModelMutableLiveDat.setValue(ApiResponse.success(result)),
                        throwable -> myTaskModelMutableLiveDat.setValue(ApiResponse.error(throwable))
                ));
    }
    public void CancellationAssignment(int whId,String mobNo) {
        disposables.add(RestClient.getInstance().getService().getAssignmentCancelationData(whId,mobNo)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> cancellationMutableLiveData.setValue(ApiResponse.loading()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> cancellationMutableLiveData.setValue(ApiResponse.success(result)),
                        throwable -> cancellationMutableLiveData.setValue(ApiResponse.error(throwable))
                ));
    }
    /*
     * Accept API call
     * */
    public void AcceptPendingMyTaskAdi(AcceptModel acceptModel) {
        disposables.add(RestClient.getInstance().getService().acceptMyPendingTask(acceptModel)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> acceptMyTaskMutableLiveDat.setValue(ApiResponse.loading()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> acceptMyTaskMutableLiveDat.setValue(ApiResponse.success(result)),
                        throwable -> acceptMyTaskMutableLiveDat.setValue(ApiResponse.error(throwable))
                ));
    }

    public void getAcceptAssignmentDistance() {
        disposables.add(RestClient.getInstance().getService().getAcceptAssignmentDistance()
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> acceptAssignmentMutableLiveDat.setValue(ApiResponse.loading()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> acceptAssignmentMutableLiveDat.setValue(ApiResponse.success(result)),
                        throwable -> acceptAssignmentMutableLiveDat.setValue(ApiResponse.error(throwable))
                ));
    }
}
