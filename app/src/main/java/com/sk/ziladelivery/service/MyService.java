package com.sk.ziladelivery.service;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonObject;
import com.sk.ziladelivery.data.api.RestClient;
import com.sk.ziladelivery.data.model.LatLongModel;
import com.sk.ziladelivery.utilities.RxBus;
import com.sk.ziladelivery.utilities.SharePrefs;
import com.sk.ziladelivery.utilities.Utils;
import com.sk.ziladelivery.ui.views.main.MainActivity;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MyService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private static final String LOGSERVICE = "#######";
    Utils utils;
    MainActivity activity;

    @Override
    public void onCreate() {
        super.onCreate();
        buildGoogleApiClient();
        utils = new Utils(getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!mGoogleApiClient.isConnected())
            mGoogleApiClient.connect();
        return Service.START_STICKY;
    }


    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location l = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (l != null) {
            Log.i(LOGSERVICE, "lat " + l.getLatitude());
            Log.i(LOGSERVICE, "lng " + l.getLongitude());

        }
        startLocationUpdate();
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i(LOGSERVICE, "lat " + location.getLatitude());
        Log.i(LOGSERVICE, "lng " + location.getLongitude());
        RxBus.getInstance().sendEvent(new LatLng(location.getLatitude(),location.getLongitude()));
        API(location.getLatitude(), location.getLongitude());
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(LOGSERVICE, "onConnectionFailed ");

    }

    private void initLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(100000);
        mLocationRequest.setFastestInterval(100000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void startLocationUpdate() {
        initLocationRequest();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    private void stopLocationUpdate() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
    }

    private void API(double latitude, double longitude) {
        if (Utils.isNetworkAvailable(getApplicationContext())) {
            int peopleId = SharePrefs.getInstance(activity).getInt(SharePrefs.PEOPLE_ID);
            postlatlong(new LatLongModel(latitude, longitude, peopleId));
        } else {
            Utils.setToast(MyService.this, "Please Connect to Internet");
        }
    }

    public void postlatlong(LatLongModel model) {
        RestClient.getInstance().getService().postlatlong(model)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonObject>() {
                    @Override
                    public void onNext(JsonObject result) {

                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
