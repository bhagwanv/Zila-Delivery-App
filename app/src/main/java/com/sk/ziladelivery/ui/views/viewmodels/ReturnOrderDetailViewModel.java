package com.sk.ziladelivery.ui.views.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sk.ziladelivery.data.api.RestClient;
import com.sk.ziladelivery.utilities.ApiResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ReturnOrderDetailViewModel extends ViewModel {
    private MutableLiveData<ApiResponse> returnOrderLiveData;
    private MutableLiveData<ApiResponse> returnRequestStatusLiveData;
    private final CompositeDisposable disposables = new CompositeDisposable();

    public ReturnOrderDetailViewModel() {

    }

    public LiveData<ApiResponse> returnOrderItemListResponse() {
        if (returnOrderLiveData == null) {
            returnOrderLiveData = new MutableLiveData<>();
        }
        return returnOrderLiveData;
    }

    public LiveData<ApiResponse> returnRequestStatusResponse() {
        if (returnRequestStatusLiveData == null) {
            returnRequestStatusLiveData = new MutableLiveData<>();
        }
        return returnRequestStatusLiveData;
    }

    // login API calling
    public void hitReturnOrderItemListAPI(int dBoyId) {
        disposables.add(RestClient.getInstance().getService().getReturnReplaceItemList(dBoyId)
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

    public void hitUpdateReturnOrderStatusAPI(int kkRequestId, String status, int dBoyId, String pickerComment, String returnReplaceImage) {
        disposables.add(RestClient.getInstance().getService().updateReturnRequestStatus(kkRequestId, status, dBoyId, pickerComment, returnReplaceImage)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> returnRequestStatusLiveData.setValue(ApiResponse.loading()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> returnRequestStatusLiveData.setValue(ApiResponse.success(result)),
                        (Throwable throwable) -> {
                            returnRequestStatusLiveData.setValue(ApiResponse.error(throwable));
                            System.out.println("Error" + throwable);
                        }
                ));
    }


    @Override
    protected void onCleared() {
        disposables.clear();
    }
}