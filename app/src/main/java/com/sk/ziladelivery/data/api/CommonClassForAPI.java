package com.sk.ziladelivery.data.api;

import android.app.Activity;

import com.google.gson.JsonElement;
import com.sk.ziladelivery.data.localdatabase.UserLatLngModel;
import com.sk.ziladelivery.data.model.BackgroundServiceModel;
import com.sk.ziladelivery.data.model.TokenResponse;
import com.sk.ziladelivery.utilities.MyApplication;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


public class CommonClassForAPI {
    private Activity mActivity;
    private static CommonClassForAPI CommonClassForAPI;


    public static CommonClassForAPI getInstance(Activity activity) {
        MyApplication.getInstance().activity = activity;
        if (CommonClassForAPI == null) {
            CommonClassForAPI = new CommonClassForAPI(activity);
        }
        return CommonClassForAPI;
    }

    public CommonClassForAPI() {

    }

    public CommonClassForAPI(Activity activity) {
        mActivity = activity;
        MyApplication.getInstance().activity = activity;
    }
    public void postBackgroundDData(DisposableObserver<JsonElement> observer, BackgroundServiceModel model) {
        BackgroundServiceClient.getInstance().getService().postBackgroundData(model)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonElement>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull JsonElement s) {
                       //// observer.onNext(s);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                       // observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }
    public void postLocalBackgroundDData(DisposableObserver<JsonElement> observer, List<UserLatLngModel> model) {
       BackgroundServiceClient.getInstance().getService().postLocalBackgroundData(model)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonElement>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull JsonElement s) {
                       //// observer.onNext(s);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                       // observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    public void getToken(DisposableObserver<TokenResponse> fetchTokenDes, String password, String username, String Password) {
        RestClient.getInstance().getService().getCommonToken(password, username, Password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TokenResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull TokenResponse object) {
                        fetchTokenDes.onNext(object);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        fetchTokenDes.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        fetchTokenDes.onComplete();
                    }
                });
    }


}