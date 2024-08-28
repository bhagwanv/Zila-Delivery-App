package com.sk.ziladelivery.ui.views.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonElement;
import com.sk.ziladelivery.data.api.RestClient;
import com.sk.ziladelivery.data.model.SelectAllResponceModel;
import com.sk.ziladelivery.ui.views.fragment.unloadReturnItem.GenerateOTPForReturnOrder;
import com.sk.ziladelivery.ui.views.fragment.unloadReturnItem.PickedReturnOrderByDBoyRequestModel;
import com.sk.ziladelivery.ui.views.fragment.unloadReturnItem.ReturnOrderCreditNoteRequestModel;
import com.sk.ziladelivery.utilities.ApiResponse;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;

public class UnloadItemViewModel extends ViewModel {
    private MutableLiveData<ApiResponse> unloadItemMutableLiveDat;
    private MutableLiveData<ApiResponse> CheckUnloadItemMutableLiveDat;
    private MutableLiveData<ApiResponse>unloadItemImageMutableLiveDat;
    private MutableLiveData<ApiResponse> returnOrderCreditNoteLiveDat;
    private MutableLiveData<ApiResponse> pickedReturnOrderByDBoyLiveDat;
    private MutableLiveData<ApiResponse>returnItemListLiveDat;
    private MutableLiveData<ApiResponse>generateOTPForReturnOrderLiveDat;


    public LiveData<ApiResponse> getUnloadItemsDataLiveData()
    {
        if (unloadItemMutableLiveDat == null)
        {
            unloadItemMutableLiveDat = new MutableLiveData<>();
        }
        if (unloadItemMutableLiveDat.getValue() != null && unloadItemMutableLiveDat.getValue().data != null)
        {
            unloadItemMutableLiveDat.setValue(null);
        }
        return unloadItemMutableLiveDat;
    }

    public LiveData<ApiResponse> getCheckUnloadItemsLiveData()
    {
        if (CheckUnloadItemMutableLiveDat == null)
        {
            CheckUnloadItemMutableLiveDat = new MutableLiveData<>();
        }
        if (CheckUnloadItemMutableLiveDat.getValue() != null && CheckUnloadItemMutableLiveDat.getValue().data != null)
        {
            CheckUnloadItemMutableLiveDat.setValue(null);
        }
        return CheckUnloadItemMutableLiveDat;
    }

    public LiveData<ApiResponse> getUnloadItemsImageDataLiveData()
    {
        if (unloadItemImageMutableLiveDat == null)
        {
            unloadItemImageMutableLiveDat = new MutableLiveData<>();
        }
        if (unloadItemImageMutableLiveDat.getValue() != null && unloadItemImageMutableLiveDat.getValue().data != null)
        {
            unloadItemImageMutableLiveDat.setValue(null);
        }
        return unloadItemImageMutableLiveDat;
    }

    public LiveData<ApiResponse>  returnOrderCreditNoteLiveData()
    {
        if ( returnOrderCreditNoteLiveDat == null)
        {
            returnOrderCreditNoteLiveDat = new MutableLiveData<>();
        }
        if ( returnOrderCreditNoteLiveDat.getValue() != null &&  returnOrderCreditNoteLiveDat.getValue().data != null)
        {
            returnOrderCreditNoteLiveDat.setValue(null);
        }
        return  returnOrderCreditNoteLiveDat;
    }

    public LiveData<ApiResponse>  pickedReturnOrderByDBoyLiveData()
    {
        if ( pickedReturnOrderByDBoyLiveDat == null)
        {
            pickedReturnOrderByDBoyLiveDat = new MutableLiveData<>();
        }
        if ( pickedReturnOrderByDBoyLiveDat.getValue() != null &&  pickedReturnOrderByDBoyLiveDat.getValue().data != null)
        {
            pickedReturnOrderByDBoyLiveDat.setValue(null);
        }
        return  pickedReturnOrderByDBoyLiveDat;
    }

    public LiveData<ApiResponse> returnItemListLiveData()
    {
        if (returnItemListLiveDat == null)
        {
            returnItemListLiveDat = new MutableLiveData<>();
        }
        if (returnItemListLiveDat.getValue() != null && returnItemListLiveDat.getValue().data != null)
        {
            returnItemListLiveDat.setValue(null);
        }
        return returnItemListLiveDat;
    }

    public LiveData<ApiResponse> generateOTPForReturnOrderLiveData()
    {
        if (generateOTPForReturnOrderLiveDat == null)
        {
            generateOTPForReturnOrderLiveDat = new MutableLiveData<>();
        }
        if (generateOTPForReturnOrderLiveDat.getValue() != null && generateOTPForReturnOrderLiveDat.getValue().data != null)
        {
            generateOTPForReturnOrderLiveDat.setValue(null);
        }
        return generateOTPForReturnOrderLiveDat;
    }
    public void getUnloadItemeObserver(int id) {
        DisposableObserver<JsonElement> observerDes = RestClient.getInstance().getService().GetUnloadItemListPage(id)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        unloadItemMutableLiveDat.setValue(ApiResponse.loading());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonElement>() {
                    @Override
                    public void onNext(JsonElement result) {
                        unloadItemMutableLiveDat.setValue(ApiResponse.success(result));
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        unloadItemMutableLiveDat.setValue(ApiResponse.error(throwable));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        disposables.add(observerDes);
    }

    public void getCheckUnloadItemeObserver(SelectAllResponceModel model) {
        DisposableObserver<JsonElement> observerDes = RestClient.getInstance().getService().GetCheckUnloadItemListPage(model)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        CheckUnloadItemMutableLiveDat.setValue(ApiResponse.loading());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonElement>() {
                    @Override
                    public void onNext(JsonElement result) {
                        CheckUnloadItemMutableLiveDat.setValue(ApiResponse.success(result));
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        CheckUnloadItemMutableLiveDat.setValue(ApiResponse.error(throwable));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        disposables.add(observerDes);
    }

    public void getUnloadItemeItemImageObserver(MultipartBody.Part body) {
        DisposableObserver<JsonElement> observerDes = RestClient.getInstance().getService().DeliveryCancelledDraftUploadImage(body)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        unloadItemImageMutableLiveDat.setValue(ApiResponse.loading());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonElement>() {
                    @Override
                    public void onNext(JsonElement result) {
                        unloadItemImageMutableLiveDat.setValue(ApiResponse.success(result));
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        unloadItemImageMutableLiveDat.setValue(ApiResponse.error(throwable));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        disposables.add(observerDes);
    }

    public void returnOrderCreditNoteObserver(List<ReturnOrderCreditNoteRequestModel> model) {
        DisposableObserver<JsonElement> observerDes = RestClient.getInstance().getService().returnOrderCreditNote(model)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        returnOrderCreditNoteLiveDat.setValue(ApiResponse.loading());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonElement>() {
                    @Override
                    public void onNext(JsonElement result) {
                        returnOrderCreditNoteLiveDat.setValue(ApiResponse.success(result));
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        returnOrderCreditNoteLiveDat.setValue(ApiResponse.error(throwable));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        disposables.add(observerDes);
    }

    public void pickedReturnOrderByDBoyObserver(PickedReturnOrderByDBoyRequestModel model) {
        DisposableObserver<JsonElement> observerDes = RestClient.getInstance().getService().pickedReturnOrderByDBoy(model)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        pickedReturnOrderByDBoyLiveDat.setValue(ApiResponse.loading());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonElement>() {
                    @Override
                    public void onNext(JsonElement result) {
                        pickedReturnOrderByDBoyLiveDat.setValue(ApiResponse.success(result));
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        pickedReturnOrderByDBoyLiveDat.setValue(ApiResponse.error(throwable));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        disposables.add(observerDes);
    }

    public void returnItemList(int id) {
        DisposableObserver<JsonElement> observerDes = RestClient.getInstance().getService().returnItemList(id)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        returnItemListLiveDat.setValue(ApiResponse.loading());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonElement>() {
                    @Override
                    public void onNext(JsonElement result) {
                        returnItemListLiveDat.setValue(ApiResponse.success(result));
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        returnItemListLiveDat.setValue(ApiResponse.error(throwable));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        disposables.add(observerDes);
    }

    public void generateOTPForReturnOrder(GenerateOTPForReturnOrder model) {
        DisposableObserver<JsonElement> observerDes = RestClient.getInstance().getService().generateOTPForReturnOrder(model)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        generateOTPForReturnOrderLiveDat.setValue(ApiResponse.loading());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonElement>() {
                    @Override
                    public void onNext(JsonElement result) {
                        generateOTPForReturnOrderLiveDat.setValue(ApiResponse.success(result));
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        generateOTPForReturnOrderLiveDat.setValue(ApiResponse.error(throwable));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        disposables.add(observerDes);
    }

    private final CompositeDisposable disposables = new CompositeDisposable();

}
