package com.sk.ziladelivery.ui.views.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonElement;
import com.sk.ziladelivery.data.api.RestClient;
import com.sk.ziladelivery.data.model.CurrentEndTimeModel;
import com.sk.ziladelivery.data.model.TimerDetailsModel;
import com.sk.ziladelivery.utilities.ApiResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MyTaskViewModel extends ViewModel {
    private String timmer;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final CompositeDisposable disposablesMyTask = new CompositeDisposable();
    private MutableLiveData<ApiResponse> myTaskModelMutableLiveDat;
    private MutableLiveData<ApiResponse> timerStartStopPostMutableLiveData;
    private MutableLiveData<ApiResponse> timerGetResponseMutableLiveData;
    private MutableLiveData<ApiResponse> rejectedrderLiveData;


    public LiveData<ApiResponse> getMyTaskData() {
        if (myTaskModelMutableLiveDat == null) {
            myTaskModelMutableLiveDat = new MutableLiveData<>();
        }
        if (myTaskModelMutableLiveDat.getValue() != null && myTaskModelMutableLiveDat.getValue().data != null) {
            myTaskModelMutableLiveDat.setValue(null);
        }

        return myTaskModelMutableLiveDat;
    }
    public LiveData<ApiResponse> getRejectedOrder() {
        if (rejectedrderLiveData == null) {
            rejectedrderLiveData = new MutableLiveData<>();
        }
        if (rejectedrderLiveData.getValue() != null && rejectedrderLiveData.getValue().data != null) {
            rejectedrderLiveData.setValue(null);
        }

        return rejectedrderLiveData;
    }

    public LiveData<ApiResponse> getStartStopTimerDetails() {
        if (timerStartStopPostMutableLiveData == null) {
            timerStartStopPostMutableLiveData = new MutableLiveData<>();
        }
        if (timerStartStopPostMutableLiveData.getValue() != null && timerStartStopPostMutableLiveData.getValue().data != null) {
            timerStartStopPostMutableLiveData.setValue(null);
        }
        return timerStartStopPostMutableLiveData;
    }

    public LiveData<ApiResponse> getTimerDetails() {
        if (timerGetResponseMutableLiveData == null) {
            timerGetResponseMutableLiveData = new MutableLiveData<>();
        }
        if (timerGetResponseMutableLiveData.getValue() != null && timerGetResponseMutableLiveData.getValue().data != null) {
            timerGetResponseMutableLiveData.setValue(null);
        }
        return timerGetResponseMutableLiveData;
    }

    /*
     * My task API call
     * */
    public void hitMyTaskApi(int DeliveryIssuanceId, String mobileNumber,int page,int List,int orderid) {
        DisposableObserver<JsonElement> observerDes = RestClient.getInstance().getService().getMyTaskData(DeliveryIssuanceId, mobileNumber,page,List,orderid)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> myTaskModelMutableLiveDat.setValue(ApiResponse.loading()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonElement>() {
                    @Override
                    public void onNext(JsonElement result) {
                        myTaskModelMutableLiveDat.setValue(ApiResponse.success(result));

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        myTaskModelMutableLiveDat.setValue(ApiResponse.error(throwable));

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        disposablesMyTask.add(observerDes);

    }
    public void hitRejectedOrderApi(int DeliveryIssuanceId, String mobileNumber) {
        DisposableObserver<JsonElement> observerDes = RestClient.getInstance().getService().GetDeclineAssignment(DeliveryIssuanceId, mobileNumber)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> rejectedrderLiveData.setValue(ApiResponse.loading()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonElement>() {
                    @Override
                    public void onNext(JsonElement result) {
                        rejectedrderLiveData.setValue(ApiResponse.success(result));

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        rejectedrderLiveData.setValue(ApiResponse.error(throwable));

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        disposablesMyTask.add(observerDes);

    }

    public void hitPostTimerApi(CurrentEndTimeModel CurrentEndTimeModel) {
        DisposableObserver<JsonElement> observerDes = RestClient.getInstance().getService().postCurentTime(CurrentEndTimeModel)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        myTaskModelMutableLiveDat.setValue(ApiResponse.loading());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonElement>() {
                    @Override
                    public void onNext(JsonElement result) {
                        timerStartStopPostMutableLiveData.setValue(ApiResponse.success(result));

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        timerStartStopPostMutableLiveData.setValue(ApiResponse.error(throwable));
                        throwable.printStackTrace();

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        disposables.add(observerDes);
    }


    public void hitgetTimeApi(TimerDetailsModel timerDetailsModel) {
        DisposableObserver<JsonElement> observerDes = RestClient.getInstance().getService().getCurentTime(timerDetailsModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonElement>() {
                    @Override
                    public void onNext(JsonElement result) {
                        timerGetResponseMutableLiveData.setValue(ApiResponse.success(result));

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        timerGetResponseMutableLiveData.setValue(ApiResponse.error(throwable));
                        throwable.printStackTrace();

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
        disposablesMyTask.dispose();
        disposables.clear();
        disposablesMyTask.clear();
    }

    public String getTimmer() {
        return timmer;
    }

    public void setTimmer(String timmer) {
        this.timmer = timmer;
    }
}