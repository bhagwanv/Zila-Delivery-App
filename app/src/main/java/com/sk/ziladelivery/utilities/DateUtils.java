package com.sk.ziladelivery.utilities;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {

    public static  String DATE_FORMAT_TIME = "yyyy-MM-dd'T'HH:mm:ss";
    public static  String DATE_FORMAT_DATE = "E, MMM dd, yyyy";


    public static String getDateFormat(String ServerDate) {
        if (!ServerDate.equalsIgnoreCase("") && !ServerDate.equalsIgnoreCase(null)) {

            DateFormat originalFormat = new SimpleDateFormat(DATE_FORMAT_TIME);//These format come to server
            originalFormat.setTimeZone(TimeZone.getDefault());
            DateFormat targetFormat = new SimpleDateFormat(DATE_FORMAT_DATE);  //change to new format here  //dd-MM-yyyy HH:mm:ss
            Date date = null;
            String formattedDate = "";
            try {
                date = originalFormat.parse(ServerDate);

                formattedDate = targetFormat.format(date);  //result

            } catch (ParseException e) {
                e.printStackTrace();
            }
            return formattedDate;


        } else {
            return "null";
        }

    }

    public static long getEpochTime(String ServerDate) {
        if (!ServerDate.equalsIgnoreCase("") && !ServerDate.equalsIgnoreCase(null)) {
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            Date epochDate = null;
            try {
                epochDate = formatter.parse(ServerDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return epochDate.getTime();
        } else {
            return 0;
        }
    }


    public static List<String> filterDates(List<String> datesWithTimezone) {
        List<String> dates = new ArrayList<>();
        // SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSXXX", Locale.getDefault());
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        for (String dateString : datesWithTimezone) {
            Log.e("TAG", "filterDates: "+dateString );
            try {
                Date date = inputFormat.parse(dateString);
                String formattedDate = outputFormat.format(date);
                dates.add(formattedDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return dates;
    }

    public static String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String giveDate() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        return sdf.format(cal.getTime());
    }





}
