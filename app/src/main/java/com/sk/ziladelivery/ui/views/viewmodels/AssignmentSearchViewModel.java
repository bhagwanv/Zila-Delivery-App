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
import io.reactivex.schedulers.Schedulers;

public class AssignmentSearchViewModel extends ViewModel {
    private MutableLiveData<ApiResponse> responseLiveData;
    private MutableLiveData<ApiResponse> OrderIDresponseLiveData;
    private final CompositeDisposable disposables = new CompositeDisposable();


    public LiveData<ApiResponse> searchResponse() {
        if (responseLiveData == null) {
            responseLiveData = new MutableLiveData<>();
        }
        return responseLiveData;
    }

    public LiveData<ApiResponse> OrderIDsearchResponse() {
        if (OrderIDresponseLiveData == null) {
            OrderIDresponseLiveData = new MutableLiveData<>();
        }
        return OrderIDresponseLiveData;
    }

    /*
     * Search API calling
     * */
    public void hitSearchApi(String AssignmentID, int DBoyId) {
        disposables.add(RestClient.getInstance().getService().searchAssignmentIDResponse(AssignmentID, DBoyId)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        responseLiveData.setValue(ApiResponse.loading());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<JsonElement>() {
                            @Override
                            public void accept(JsonElement jsonElement) throws Exception {
                                responseLiveData.setValue(ApiResponse.success(jsonElement));

                            }

                        },
                        throwable -> responseLiveData.setValue(ApiResponse.error(throwable))
                ));

    }

    public void hitOrderIDSearchApi(String OrderID, int DBoyId) {
        disposables.add(RestClient.getInstance().getService().searchOrderIDResponse(OrderID, DBoyId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<JsonElement>() {
                            @Override
                            public void accept(JsonElement jsonElement) throws Exception {
                                OrderIDresponseLiveData.setValue(ApiResponse.success(jsonElement));

                            }

                        },
                        throwable -> OrderIDresponseLiveData.setValue(ApiResponse.error(throwable))
                ));

    }

    @Override
    protected void onCleared() {
        disposables.clear();
    }
}
