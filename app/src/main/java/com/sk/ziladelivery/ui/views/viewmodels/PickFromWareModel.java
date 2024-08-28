package com.sk.ziladelivery.ui.views.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sk.ziladelivery.data.api.RestClient;
import com.sk.ziladelivery.utilities.ApiResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class PickFromWareModel extends ViewModel {
    private MutableLiveData<ApiResponse> returnOrderLiveData;
    private final CompositeDisposable disposables = new CompositeDisposable();

    public PickFromWareModel() {

    }

    public LiveData<ApiResponse> returnOrderListResponse() {
        if (returnOrderLiveData == null) {
            returnOrderLiveData = new MutableLiveData<>();
        }
        return returnOrderLiveData;
    }

    /*
     * Return Order List API calling
     * */
    public void hitReturnOrderListAPI(int dBoyId) {
        disposables.add(RestClient.getInstance().getService().getWarehouseRejectOrderList(dBoyId)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> returnOrderLiveData.setValue(ApiResponse.loading()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> returnOrderLiveData.setValue(ApiResponse.success(result)),
                        (Throwable throwable) -> {
                            returnOrderLiveData.setValue(ApiResponse.error(throwable));
                            System.out.println("Error" + throwable);
                        }
                ));
    }

    @Override
    protected void onCleared() {
        disposables.clear();
    }
}