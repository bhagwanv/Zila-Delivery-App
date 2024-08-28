package com.sk.ziladelivery.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import androidx.annotation.Nullable;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * Created by fabio on 30/01/2016.
 */
public class TimerService extends Service {
    public static String str_receiver = "com.countdowntimerservice.receiver";
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    SharedPreferences mpref;
    SharedPreferences.Editor mEditor;
    public static final long NOTIFY_INTERVAL = 1000;
    Intent intent;
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    private static final String TAG = TimerService.class.getSimpleName();
    @Override
    public void onCreate() {
        super.onCreate();

        mpref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mEditor = mpref.edit();
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        intent = new Intent(str_receiver);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        stoptimertask();
        startTimer();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("EXIT", "ondestroy!");
      /*  Intent broadcastIntent = new Intent("uk.ac.shef.oak.ActivityRecognition.RestartSensor");
        sendBroadcast(broadcastIntent);*/
        stoptimertask();
    }

    private Timer timer;
    private TimerTask timerTask;
    long oldTime = 0;

    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, to wake up every 1 second
        timer.schedule(timerTask, 5, 1000); //
    }

    /**
     * it sets the timer to print the counter every x seconds
     */
    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                elapsedTime();
            }
        };
    }

    /**
     * not needed
     */
    public  void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void fn_update(String str_time) {

        intent.putExtra("time", str_time);

        sendBroadcast(intent);
    }
    public String elapsedTime() {
        long int_hours = mpref.getLong("START_TIME", 0);
        timeInMilliseconds = System.currentTimeMillis() - int_hours;
        updatedTime = timeSwapBuff + timeInMilliseconds;
        int secs = (int) (updatedTime / 1000);
        int mins = secs / 60;
        secs = secs % 60;

        int milliseconds = (int) (updatedTime % 1000);
        // timerValue.setText();
        // If the timer is running, the end time will be zero
        long millis = updatedTime;


        String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)),
                TimeUnit.MILLISECONDS.toMillis(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toSeconds(millis)));
        // System.out.println(hms);
       // Log.v(TAG, "Timmer::" + hms);
        fn_update(hms);
        return hms;

    }

}