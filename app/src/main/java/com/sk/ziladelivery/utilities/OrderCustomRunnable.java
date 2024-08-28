package com.sk.ziladelivery.utilities;

import android.os.Handler;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;


public class OrderCustomRunnable implements Runnable {
    public long millisUntilFinished = 0;
    public TextView holder;
    Handler handler;
    final String FORMAT = "%02d:%02d:%02d";
    private static String hms;

    public OrderCustomRunnable(Handler handler, TextView holder, long millisUntilFinished) {
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
            if(millisUntilFinished<0){
                holder.setText("-" + hms);
            }
            else
                {
                holder.setText("" + hms);

            }
            // holder.setText("  "+time);
            millisUntilFinished -= 1000;
            handler.postDelayed(this, 1000);


    }

}