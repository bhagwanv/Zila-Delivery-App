package com.sk.ziladelivery.utilities;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.sk.ziladelivery.BuildConfig;
import com.sk.ziladelivery.R;

import java.util.Timer;

public class YourService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private Context context;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private MediaPlayer mp;
    private Timer timer;
    private Runnable r;
    private Handler handler = new Handler();

    @Override
    public void onCreate() {
        super.onCreate();
        this.context = this;

        buildGoogleApiClient();

       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startMyOwnForeground();
        } else {
            startForeground(1, new Notification());
        }*/

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        if (!mGoogleApiClient.isConnected())
            mGoogleApiClient.connect();
        startTimer();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

        mGoogleApiClient.disconnect();
        handler.removeCallbacks(r);
        handler = null;
        if (mp != null && mp.isPlaying()) {
            mp.stop();
        }
        stoptimertask();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        int distance = SharePrefs.getInstance(getApplicationContext()).getInt(SharePrefs.LogDboyLoctionMeter);
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(10);
        if (BuildConfig.DEBUG) {
            mLocationRequest.setSmallestDisplacement(20);
        } else {
            mLocationRequest.setSmallestDisplacement(distance);
        }
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        if (!SharePrefs.getInstance(context).getBoolean(SharePrefs.NOT_LAST_MILE_APP)) {
            if (location.getLatitude() != 0.0 && location.getLongitude() != 0.0) {
                Utils.postBackgroundDData(context, location.getLatitude(), location.getLongitude());
            }

        }
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
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
       // startForeground(2, notification);
    }

    public void startTimer() {
        r = new Runnable() {
            public void run() {
                // periodicallyLocation();
                gpsAlarmAlert();
                handler.postDelayed(this, 30000);
            }
        };
        handler.postDelayed(r, 30000);
    }

    public void stoptimertask() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        if (!mGoogleApiClient.isConnected())
            mGoogleApiClient.connect();
    }

    private void gpsAlarmAlert() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 100, 0);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            playRingTone();
        }
    }

    private void playRingTone() {
        try {
            Uri notification = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.delivery_ring_tone);
            mp = new MediaPlayer();
            mp.setDataSource(getApplicationContext(), notification);
            mp.prepare();
            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                }
            });
            setNotification("जीपीएस सक्षम करें", "कृपया हमें स्थान डेटा तक पहुँचने के लिए GPS की अनुमति दें। कृपया अतिरिक्त कार्यक्षमता के लिए ऐप सेटिंग में अनुमति दें।");
            new Handler().postDelayed(() -> {
                mp.stop();
            }, 100000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setNotification(String messageBody, String title) {
        try {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel mChannel = new NotificationChannel(getResources().getString(R.string.app_name),
                        getResources().getString(R.string.app_name),
                        NotificationManager.IMPORTANCE_HIGH);
                mChannel.enableLights(true);
                mChannel.enableVibration(true);
                mChannel.setLightColor(Color.YELLOW);
                mChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                notificationManager.createNotificationChannel(mChannel);
            }
            Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), getResources().getString(R.string.app_name))
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title)
                    .setContentText(messageBody)
                    .setContentInfo(messageBody)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent)
                    .setChannelId(getResources().getString(R.string.app_name));

            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            notificationManagerCompat.notify(99, builder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}