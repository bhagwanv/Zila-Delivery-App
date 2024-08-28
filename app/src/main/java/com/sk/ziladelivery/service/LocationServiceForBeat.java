package com.sk.ziladelivery.service;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.sk.ziladelivery.data.api.RestClient;
import com.sk.ziladelivery.data.model.MySingleTripOrderResponseModel;
import com.sk.ziladelivery.utilities.SharePrefs;

import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LocationServiceForBeat extends Service implements LocationListener {
    private final long UPDATE_INTERVAL = 2 * 10000;  /* 10 secs */
    private final long FASTEST_INTERVAL = 2000; /* 2 sec */
    LatLng latLng;
    private LocationRequest mLocationRequest;
    int count = 0;
    private FusedLocationProviderClient fusedLocationClient;
    Geocoder geocoder;

    @Override
    public void onCreate() {
        super.onCreate();
        fusedLocationClient = getFusedLocationProviderClient(this);

       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startMyOwnForeground();
        } else {
            startForeground(1, new Notification());
        }*/
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void startMyOwnForeground() {
        String NOTIFICATION_CHANNEL_ID = "example.permanence";
        String channelName = "Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MAX)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();

        //startForeground(2, notification);


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        if (intent != null && intent.getAction() != null) {
            if (intent.getAction().equals("StopService")) {
                stopForeground(true);
                stopSelf();
            }
        } else {
            startLocation();
            Log.e("start Service", "started");
        }


        return START_STICKY;
    }


    public void startLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            latLng = new LatLng(location.getLatitude(), location.getLongitude());
                            startLocationUpdates();

                        } /*else {
                            GPSTracker gpsTracker = new GPSTracker(LocationServiceForBeat.this);
                            latLng = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
                            startLocationUpdate();
                        }*/
                    }
                });

    }

    // Trigger new location updates at interval
    protected void startLocationUpdates() {

        // Create the location request to start receiving updates
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setSmallestDisplacement(200);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);
        count = 0;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        // do work
                        onLocationChanged(locationResult.getLastLocation());
                    }
                },
                Looper.myLooper());


    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

/*    public void onLocationChanged(Location location) {
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        GetSingleMapview(SharePrefs.getInstance(this).getInt(SharePrefs.TripPlannerConfirmedMasterId));

    }*/

    public void GetSingleMapview(int id,double lat,double lng) {
        RestClient.getInstance().getService().getSingleMapViewOrderList(id,lat,lng)
                .subscribeOn(Schedulers.io())
//                .doOnSubscribe(disposable -> Utils.showProgressDialog(mActivity))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonElement>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(JsonElement o) {
                        setData(o);
                    }


                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        //customDialog.dismiss();

                    }
                });
    }

    private void setData(JsonElement response) {
        try {
            JSONObject jsonObject = new JSONObject(response.toString());
            MySingleTripOrderResponseModel mySingleTripOrderResponseModel = new Gson().fromJson(String.valueOf(jsonObject), MySingleTripOrderResponseModel.class);
            SharePrefs.setMapRouteSharedPreference(this, SharePrefs.MAP_ROUTE, new Gson().toJson(mySingleTripOrderResponseModel));
            Location startPoint = new Location("locationA");
            startPoint.setLatitude(latLng.latitude);
            startPoint.setLongitude(latLng.longitude);
            Location endPoint = new Location("locationB");
            double distance = 0;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());
                endPoint.setLatitude(mySingleTripOrderResponseModel.getCustomerOrderinfoDc().getLat());
                endPoint.setLongitude(mySingleTripOrderResponseModel.getCustomerOrderinfoDc().getLng());
                distance = startPoint.distanceTo(endPoint);
                System.out.println("distance - set data -  "+ distance);
                if (distance <= 200) {

                    addresses = geocoder.getFromLocation(mySingleTripOrderResponseModel.getCustomerOrderinfoDc().getLat(),
                            mySingleTripOrderResponseModel.getCustomerOrderinfoDc().getLng(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    String address = addresses.get(0).getAddressLine(0);
                    SharePrefs.setMapRouteSharedPreference(this, SharePrefs.MAP_ROUTE, new Gson().toJson(mySingleTripOrderResponseModel));
                    Intent intent = new Intent("LocationFound");
                    intent.putExtra("address",address);
                    //extra.putDouble("lat", RouteorderResponseModel.getCustomerOrderinfoDc().getLat());
                    //extra.putDouble("lag", RouteorderResponseModel.getCustomerOrderinfoDc().getLng());
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                    Log.e("Distance is", "Distance" + distance);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void savedData(String response) {
        try {
            // JSONObject jsonObject = new JSONObject(response);
            System.out.println("response" + response);
            MySingleTripOrderResponseModel mySingleTripOrderResponseModel = new Gson().fromJson(response, MySingleTripOrderResponseModel.class);
            Location startPoint = new Location("locationA");
            startPoint.setLatitude(latLng.latitude);
            startPoint.setLongitude(latLng.longitude);
            Location endPoint = new Location("locationB");
            double distance = 0;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());
                endPoint.setLatitude(mySingleTripOrderResponseModel.getCustomerOrderinfoDc().getLat());
                endPoint.setLongitude(mySingleTripOrderResponseModel.getCustomerOrderinfoDc().getLng());
                distance = startPoint.distanceTo(endPoint);
                System.out.println("distance - saved data -  "+ distance);
                if (distance <= 200) {
                    addresses = geocoder.getFromLocation(mySingleTripOrderResponseModel.getCustomerOrderinfoDc().getLat(),
                            mySingleTripOrderResponseModel.getCustomerOrderinfoDc().getLng(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    String address = addresses.get(0).getAddressLine(0);
                    SharePrefs.setMapRouteSharedPreference(this, SharePrefs.MAP_ROUTE, new Gson().toJson(mySingleTripOrderResponseModel));
                    Intent intent = new Intent("LocationFound");
                   /* Bundle extra = new Bundle();
                    extra.putString("address", address);*/
                    intent.putExtra("address", address);
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                    Log.e("Distance is", "Distance" + distance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        String DataSaved = SharePrefs.getMapRouteSharedPreferences(this, SharePrefs.MAP_ROUTE);
        if (DataSaved != null && !DataSaved.equals("")) {
            savedData(DataSaved);
        } else {
            GetSingleMapview(SharePrefs.getInstance(this).getInt(SharePrefs.TripPlannerConfirmedMasterId),latLng.latitude,latLng.longitude);
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {


    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }
}
