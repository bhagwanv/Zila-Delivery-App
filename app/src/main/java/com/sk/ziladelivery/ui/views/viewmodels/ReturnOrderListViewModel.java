package com.sk.ziladelivery.ui.views.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sk.ziladelivery.data.api.RestClient;
import com.sk.ziladelivery.utilities.ApiResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ReturnOrderListViewModel extends ViewModel {
    private MutableLiveData<ApiResponse> returnOrderLiveData;
    private final CompositeDisposable disposables = new CompositeDisposable();

    public ReturnOrderListViewModel() {

    }

    public LiveData<ApiResponse> returnOrderListResponse() {
        if (returnOrderLiveData == null) {
            returnOrderLiveData = new MutableLiveData<>();
        }
        return returnOrderLiveData;
    }

    /*
     * login API calling
     * */
    public void hitReturnOrderListAPI(int dBoyId) {
        disposables.add(RestClient.getInstance().getService().getReturnOrderList(dBoyId)
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