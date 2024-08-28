package com.sk.ziladelivery.utilities;

import android.os.Handler;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class CustomRunnable implements Runnable {
    public long millisUntilFinished = 0;
    public TextView holder;
    private Handler handler;
    private final String FORMAT = "%02d:%02d:%02d";
    private static String hms;
    private static CustomRunnable customRunnable;


    public static CustomRunnable getInstance(Handler handler, TextView holder, long millisUntilFinished) {
        if (customRunnable == null) {
            customRunnable = new CustomRunnable(handler, holder, millisUntilFinished);
        }
        return customRunnable;
    }

    public CustomRunnable(Handler handler, TextView holder, long millisUntilFinished) {
        this.handler = handler;
        this.holder = holder;
        this.millisUntilFinished = millisUntilFinished;
    }

    @Override
    public void run() {
        long mills = Math.abs(millisUntilFinished);
        hms = String.format(FORMAT,
                TimeUnit.MILLISECONDS.toHours(mills),
                TimeUnit.MILLISECONDS.toMinutes(mills) - TimeUnit.HOURS.toMinutes(
                        TimeUnit.MILLISECONDS.toHours(mills)),
                TimeUnit.MILLISECONDS.toSeconds(mills) - TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(mills)));
        holder.setText("" + hms);
        millisUntilFinished += 1000;
        handler.postDelayed(this, 1000);
    }

    public void stop() {
        if (handler != null) {
            handler.removeCallbacks(customRunnable);
            handler.removeCallbacks(this);
            handler = null;
        }
    }
}