package com.sk.ziladelivery.ui.views.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonElement;
import com.sk.ziladelivery.data.api.RestClient;
import com.sk.ziladelivery.data.model.OrderConfirmModel;
import com.sk.ziladelivery.utilities.ApiResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class CollectPaymentViewModel extends ViewModel {
    private MutableLiveData<ApiResponse> collectPaymentMD;
    private MutableLiveData<ApiResponse> collectionOrderListMutableLiveDat;
    private MutableLiveData<ApiResponse>deliveryGenrateOtpML;
    private MutableLiveData<ApiResponse>DeliverdConfirmOML;
    private MutableLiveData<ApiResponse>removeOrderML;
    private MutableLiveData<ApiResponse>CheckRemaingOrderML;
    private MutableLiveData<ApiResponse> completeTripMutableLiveDat;

    public LiveData<ApiResponse> getCollectPayment() {
        if (collectPaymentMD == null) {
            collectPaymentMD = new MutableLiveData<>();
        }
        if (collectPaymentMD.getValue() != null && collectPaymentMD.getValue().data != null) {
            collectPaymentMD.setValue(null);
        }
        return collectPaymentMD;
    }

    public LiveData<ApiResponse> getDeliveryConfrm() {
        if (DeliverdConfirmOML == null) {
            DeliverdConfirmOML = new MutableLiveData<>();
        }
        if (DeliverdConfirmOML.getValue() != null && DeliverdConfirmOML.getValue().data != null) {
            DeliverdConfirmOML.setValue(null);
        }
        return DeliverdConfirmOML;
    }

    public LiveData<ApiResponse> getDeliveryOtp() {
        if (deliveryGenrateOtpML == null) {
            deliveryGenrateOtpML = new MutableLiveData<>();
        }
        if (deliveryGenrateOtpML.getValue() != null && deliveryGenrateOtpML.getValue().data != null) {
            deliveryGenrateOtpML.setValue(null);
        }
        return deliveryGenrateOtpML;
    }


    public LiveData<ApiResponse> getCheckRemaingOrder() {
        if (CheckRemaingOrderML == null) {
            CheckRemaingOrderML = new MutableLiveData<>();
        }
        if (CheckRemaingOrderML.getValue() != null && CheckRemaingOrderML.getValue().data != null) {
            CheckRemaingOrderML.setValue(null);
        }
        return CheckRemaingOrderML;
    }

    public LiveData<ApiResponse> getCollectionOrderList() {
        if (collectionOrderListMutableLiveDat == null) {
            collectionOrderListMutableLiveDat = new MutableLiveData<>();
        }
        if (collectionOrderListMutableLiveDat.getValue() != null && collectionOrderListMutableLiveDat.getValue().data != null) {
            collectionOrderListMutableLiveDat.setValue(null);
        }
        return collectionOrderListMutableLiveDat;
    }

    public LiveData<ApiResponse> removeOrderData() {
        if (removeOrderML == null) {
            removeOrderML = new MutableLiveData<>();
        }
        if (removeOrderML.getValue() != null && removeOrderML.getValue().data != null) {
            removeOrderML.setValue(null);
        }
        return removeOrderML;
    }

    public LiveData<ApiResponse> getCompletetrip() {
        if (completeTripMutableLiveDat == null) {
            completeTripMutableLiveDat = new MutableLiveData<>();
        }
        if (completeTripMutableLiveDat.getValue() != null && completeTripMutableLiveDat.getValue().data != null) {
            completeTripMutableLiveDat.setValue(null);
        }
        return completeTripMutableLiveDat;
    }
    public void getCollectionOrderListObserver(int id) {
        DisposableObserver<JsonElement> observerDes = RestClient.getInstance().getService().GetCollectPaymentOrderList(id)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        collectionOrderListMutableLiveDat.setValue(ApiResponse.loading());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonElement>() {
                    @Override
                    public void onNext(JsonElement result) {
                        collectionOrderListMutableLiveDat.setValue(ApiResponse.success(result));
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        collectionOrderListMutableLiveDat.setValue(ApiResponse.error(throwable));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        disposables.add(observerDes);
    }
    public void getDeliveryConfirmObserver(OrderConfirmModel model) {
        DisposableObserver<JsonElement> observerDes = RestClient.getInstance().getService().getDeliverdConfirmOtp(model)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        DeliverdConfirmOML.setValue(ApiResponse.loading());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonElement>() {
                    @Override
                    public void onNext(JsonElement result) {
                        DeliverdConfirmOML.setValue(ApiResponse.success(result));
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        DeliverdConfirmOML.setValue(ApiResponse.error(throwable));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        disposables.add(observerDes);
    }
    public void getDeliveryOTPObserver(int tripPlannerConfirmedDetailId, String status, double latitude, double longitude) {
        DisposableObserver<JsonElement> observerDes = RestClient.getInstance().getService().getDeliveredGenerateOtp(tripPlannerConfirmedDetailId,status,latitude,longitude)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        deliveryGenrateOtpML.setValue(ApiResponse.loading());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonElement>() {
                    @Override
                    public void onNext(JsonElement result) {
                        deliveryGenrateOtpML.setValue(ApiResponse.success(result));
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        deliveryGenrateOtpML.setValue(ApiResponse.error(throwable));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        disposables.add(observerDes);
    }

    public void getCollectPaymentObserver(int id) {
        DisposableObserver<JsonElement> observerDes = RestClient.getInstance().getService().getCollectPayment(id)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> collectPaymentMD.setValue(ApiResponse.loading()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonElement>() {
                    @Override
                    public void onNext(JsonElement result) {
                        collectPaymentMD.setValue(ApiResponse.success(result));
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        collectPaymentMD.setValue(ApiResponse.error(throwable));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        disposables.add(observerDes);
    }

    public void getRemoveOrderObserver(int id,boolean IsPaymentDone) {
        DisposableObserver<JsonElement> observerDes = RestClient.getInstance().getService().getRemoveOrder(id,IsPaymentDone)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        removeOrderML.setValue(ApiResponse.loading());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonElement>() {
                    @Override
                    public void onNext(JsonElement result) {
                        removeOrderML.setValue(ApiResponse.success(result));
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        removeOrderML.setValue(ApiResponse.error(throwable));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        disposables.add(observerDes);
    }

    public void getCheckOrderRemaingObserver(int id) {
        DisposableObserver<JsonElement> observerDes = RestClient.getInstance().getService().getCheckRemaingOrderStatus(id)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        CheckRemaingOrderML.setValue(ApiResponse.loading());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonElement>() {
                    @Override
                    public void onNext(JsonElement result) {
                        CheckRemaingOrderML.setValue(ApiResponse.success(result));
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        CheckRemaingOrderML.setValue(ApiResponse.error(throwable));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        disposables.add(observerDes);
    }


    public void getCompletTripStatus(int id,double lat,double lng) {
        DisposableObserver<JsonElement> observerDes = RestClient.getInstance().getService().getCompleteTripStatusChange(id,lat,lng)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        completeTripMutableLiveDat.setValue(ApiResponse.loading());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonElement>() {
                    @Override
                    public void onNext(JsonElement result) {
                        completeTripMutableLiveDat.setValue(ApiResponse.success(result));
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        completeTripMutableLiveDat.setValue(ApiResponse.error(throwable));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        disposables.add(observerDes);
    }


    private final CompositeDisposable disposables = new CompositeDisposable();

}
