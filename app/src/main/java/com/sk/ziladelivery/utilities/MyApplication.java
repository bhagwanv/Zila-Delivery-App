package com.sk.ziladelivery.utilities;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.sk.ziladelivery.data.api.CommonClassForAPI;
import com.sk.ziladelivery.data.model.TokenResponse;

import org.jetbrains.annotations.NotNull;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.AnalyticPost;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by user on 5/26/2017.
 */
public class MyApplication extends Application {

    private static MyApplication mInstance;
    public Activity activity;

    public FirebaseAnalytics mFirebaseAnalytics;


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        // firebase initialization
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);
        if (SharePrefs.getInstance(this).isLoggedIn()) {
            // firebase
            mFirebaseAnalytics.setUserId(SharePrefs.getInstance(getApplicationContext()).getString(SharePrefs.SK_CODE));
            mFirebaseAnalytics.setUserProperty("SkCode", SharePrefs.getInstance(getApplicationContext()).getString(SharePrefs.SK_CODE));
//        mFirebaseAnalytics.setUserProperty("city", SharePrefs.getInstance(getApplicationContext()).getString(SharePrefs.CITY_NAME));
            mFirebaseAnalytics.setUserProperty("warehouse", "" + SharePrefs.getInstance(getApplicationContext()).getInt(SharePrefs.WAREHOUSE_ID));
        }

    }





    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public void token() {
        CommonClassForAPI.getInstance(activity).getToken(callTokenDes, "password", SharePrefs.getInstance(getApplicationContext()).getString(SharePrefs.TOKEN_NAME), SharePrefs.getInstance(getApplicationContext()).getString(SharePrefs.TOKEN_PASSWORD));
    }

    private final DisposableObserver<TokenResponse> callTokenDes = new DisposableObserver<TokenResponse>() {
        @Override
        public void onNext(@NotNull TokenResponse response) {
            try {
                if (response != null) {
                    SharePrefs.getInstance(getApplicationContext()).putString(SharePrefs.TOKEN, response.getAccess_token());
                    if (activity != null) activity.recreate();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onComplete() {
        }
    };
}