package com.sk.ziladelivery.utilities;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.sk.ziladelivery.R;
import com.sk.ziladelivery.data.api.CommonClassForAPI;
import com.sk.ziladelivery.data.localdatabase.UserLatLngModel;
import com.sk.ziladelivery.data.localdatabase.UserRepository;
import com.sk.ziladelivery.data.model.BackgroundServiceModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class Utils {
    private static Context context;
    public static Dialog customDialog;
    public static String myFormat = "yyyy-MM-dd'T'HH:mm:ss";
    public static LatLng temp = null;
    public static GPSTracker gpsTracker;
    public static String lat, lag;
    public static double latitude, longitude;
    public static Activity activity;
    private static FusedLocationProviderClient fusedLocationClient;
    private static UserRepository userRepository;


    public Utils(Context _mContext) {
        context = _mContext;
        activity = (Activity) _mContext;
        fusedLocationClient = getFusedLocationProviderClient(context);
        userRepository = new UserRepository(context);
        startLocation();

    }

    public static void setToast(Context _mContext, String str) {
        Toast toast = Toast.makeText(_mContext, str, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();
    }

    public static boolean checkInternetConnection(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo networkInfo : info) {
                    if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static String calculateTime(int seconds) {
        int day = (int) TimeUnit.SECONDS.toDays(seconds);
        long hours = TimeUnit.SECONDS.toHours(seconds) - (day * 24);
        long minute = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds) * 60);
        long second = TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) * 60);
        String time = hours + ":" + minute + ":" + second;
        System.out.println(time);
        return time;
    }

    public static String getCustMobile() {
        return SharePrefs.getInstance(context).getString(SharePrefs.MOBILE);
    }

    public static String getToken() {
        return SharePrefs.getInstance(context).getString(SharePrefs.TOKEN);
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }



    public static void hideProgressDialog(Activity activity) {
        if (customDialog != null) {
            //if (customDialog != null) {
            customDialog.dismiss();
        }
    }

    public static void HideKeyBoard(Activity activity, EditText searchTxt) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchTxt.getWindowToken(), 0);
    }

    public static void hideProgressDialog() {
        if (customDialog != null) {
            customDialog.dismiss();
        }
    }

    public static boolean isJSONValid(String json) {
        try {
            new JSONObject(json);
        } catch (Exception ex) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                new JSONArray(json);
            } catch (Exception ex1) {
                return false;
            }
        }
        return true;
    }

    public static String getDate(String dateTime) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "dd/MM/yyyy";


        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern, Locale.ENGLISH);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern, Locale.ENGLISH);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(dateTime);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String getTime(String dateTime) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "HH:mm a";


        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern, Locale.ENGLISH);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern, Locale.ENGLISH);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(dateTime);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String getSimpleDateFormat(String ServerDate) {
        // 2018-12-24T15:48:15.707+05:30
        if (!ServerDate.equalsIgnoreCase("") && !ServerDate.equalsIgnoreCase(null)) {
            DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);//These format come to server
            originalFormat.setTimeZone(TimeZone.getDefault());

            DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);  //change to new format here  //dd-MM-yyyy HH:mm:ss

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

    public static String getSimpleDateFormatforTrip(String ServerDate) {
        // 2018-12-24T15:48:15.707+05:30
        if (!ServerDate.equalsIgnoreCase("") && !ServerDate.equalsIgnoreCase(null)) {
            DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);//These format come to server
            originalFormat.setTimeZone(TimeZone.getDefault());

            DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);  //change to new format here  //dd-MM-yyyy HH:mm:ss

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

    public static String getLocalDate(String ServerDate) {
        // 2018-12-24T15:48:15.707+05:30
        if (!ServerDate.equalsIgnoreCase(null) && !ServerDate.equalsIgnoreCase("")) {
            DateFormat originalFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);//These format come to server
            originalFormat.setTimeZone(TimeZone.getDefault());

            DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);  //change to new format here  //dd-MM-yyyy HH:mm:ss

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


    public static String getDateFormat(String ServerDate) {
        // 2018-12-24T15:48:15.707+05:30
        if (!ServerDate.equalsIgnoreCase(null) && !ServerDate.equalsIgnoreCase("")) {
            DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);//These format come to server
            originalFormat.setTimeZone(TimeZone.getDefault());

            DateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);  //change to new format here  //dd-MM-yyyy HH:mm:ss

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

    public static String getDayMonthFormat(String ServerDate) {
        if (ServerDate != null && !ServerDate.equalsIgnoreCase("")) {
            DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);//These format come to server
            originalFormat.setTimeZone(TimeZone.getDefault());
            DateFormat targetFormat = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);  //change to new format here  //dd-MM-yyyy HH:mm:ss
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
            return "";
        }
    }

    public static long getTimer(String startDateTime) throws ParseException {
        long currMillis = System.currentTimeMillis();
        SimpleDateFormat sdf1 = new SimpleDateFormat(Utils.myFormat, Locale.getDefault());
        sdf1.setTimeZone(TimeZone.getDefault());

        Date startTime = sdf1.parse(startDateTime);
        long startEpoch = startTime.getTime();
        long millse = currMillis - startEpoch;

        return millse;

    }

    public static LatLng getTemp(Context context) {
        try {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return new LatLng(0, 0);
            }
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();

                            }
                        }
                    });
            temp = new LatLng(latitude, longitude);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }


    public static Double getDoubleLat(AppCompatActivity context) {
        try {
            gpsTracker = new GPSTracker(context);
            if (gpsTracker != null) {
                latitude = gpsTracker.getLatitude();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        //  Log.d("TAG", "getLat:" + gpsTracker.getLatitude());
        return latitude;
    }

    public static Double getDoubleLag(AppCompatActivity context) {
        try {
            gpsTracker = new GPSTracker(context);
            if (gpsTracker != null) {
                longitude = gpsTracker.getLongitude();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return longitude;
    }

    public static float Distance(double lat1, double lon1, double lat2, double lon2) {
        Location mylocation = new Location("");
        Location dest_location = new Location("");
        dest_location.setLatitude(lat2);
        dest_location.setLongitude(lon2);
        mylocation.setLatitude(lat1);
        mylocation.setLongitude(lon1);
        return mylocation.distanceTo(dest_location);
    }

    public static Double getLat(AppCompatActivity context) {
        try {
            /*if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return "";
            }
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                latitude=location.getLatitude();
                                longitude=location.getLongitude();

                            }
                        }
                    });*/
            gpsTracker = new GPSTracker(context);
            if (gpsTracker != null) {
                latitude = gpsTracker.getLatitude();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        // Log.d("TAG", "getLat:" + lat);
        return latitude;
    }

    public static Double getLog(AppCompatActivity context) {
        try {
            gpsTracker = new GPSTracker(context);
            if (gpsTracker != null) {
                longitude = gpsTracker.getLongitude();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return longitude;
    }

    public static String convertToDaysHoursMinutes(long minutes) {
        int day = (int) TimeUnit.MINUTES.toDays(minutes);
        long hours = TimeUnit.MINUTES.toHours(minutes) - (day * 24);
        long minute = TimeUnit.MINUTES.toMinutes(minutes) - (TimeUnit.MINUTES.toHours(minutes) * 60);
        String result = "";
        if (day != 0) {
            result += day;
            if (day == 1) {
                result += " day ";
            } else {
                result += " days ";
            }
            return result;
        }

        if (hours != 0) {
            result += hours;

            if (hours == 1) {
                result += " hr ";
            } else {
                result += " hrs ";
            }
        }

        if (minute != 0) {
            result += minute;

            if (minute == 1) {
                result += " min";
            } else {
                result += " mins";
            }
        }
        return result;
    }

    public static String formatToYesterdayOrToday(String date) {
        Date dateTime = null;
        try {
            dateTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTime);
        Calendar today = Calendar.getInstance();
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);
        DateFormat timeFormatter = new SimpleDateFormat("hh:mma", Locale.ENGLISH);

        if (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
            return "Today ";
        } else if (calendar.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == yesterday.get(Calendar.DAY_OF_YEAR)) {
            return "Yesterday ";
        } else {
            return getChangeDateFormatInProfile(date);
        }
    }

    public static String getChangeDateFormatInProfile(String ServerDate) {
        // 2018-12-24T15:48:15.707+05:30
        if (!TextUtils.isNullOrEmpty(ServerDate)) {
            DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);//These format come to server
            originalFormat.setTimeZone(TimeZone.getDefault());
            DateFormat targetFormat = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);  //change to new format here  //dd-MM-yyyy HH:mm:ss
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

    public static String getTimeForChat(String ServerDate) {
        // 2018-12-24T15:48:15.707+05:30
        if (!ServerDate.equalsIgnoreCase(null) && !ServerDate.equalsIgnoreCase("")) {
            DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);//These format come to server
            DateFormat targetFormat = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);  //change to new format here  //dd-MM-yyyy HH:mm:ss
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

    public static void leftTransaction(Activity activity) {
        activity.overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
    }

    public static void rightTransaction(Activity activity) {
        activity.overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }

    public static double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    public static void startLocation() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();

                        }
                    }
                });

    }

    public static void postBackgroundDData(Context contextS, double latitude, double longitude) {
        String response = SharePrefs.getCashmanagmentSharedPreferences(contextS, SharePrefs.BACKGROUND_DATA);
        BackgroundServiceModel backgroundServiceModel = new Gson().fromJson(response, BackgroundServiceModel.class);
        int distance = SharePrefs.getInstance(contextS).getInt(SharePrefs.LogDboyLoctionMeter);
        //converting miles into meter
        if (SharePrefs.getInstance(contextS).getString(SharePrefs.Previous_lat) != null && !SharePrefs.getInstance(contextS).getString(SharePrefs.Previous_lat).isEmpty()) {
            double previouslat = Double.parseDouble(SharePrefs.getInstance(contextS).getString(SharePrefs.Previous_lat));
            double previouslag = Double.parseDouble(SharePrefs.getInstance(contextS).getString(SharePrefs.Previous_lag));
            double difference = Math.round(Utils.distance(previouslat, previouslag, latitude, longitude) * 1609.34);
            SharePrefs.getInstance(contextS).putString(SharePrefs.Previous_lat, String.valueOf(latitude));
            SharePrefs.getInstance(contextS).putString(SharePrefs.Previous_lag, String.valueOf(longitude));
            if (difference >= distance) {
                if (!Utils.checkInternetConnection(contextS)) {
                    Toast.makeText(contextS, contextS.getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                    if (userRepository == null) {
                        userRepository = new UserRepository(contextS);
                    }

                    if (userRepository != null)
                        Utils.setToast(contextS, "Data Insert");
                    userRepository.insertUser(new UserLatLngModel(backgroundServiceModel.getTripPlannerVehicleId(), latitude, longitude, 0, backgroundServiceModel.getRecordStatus(), getLocalDate(new Date().toString()), backgroundServiceModel.getTripPlannerConfirmedDetailId(), Math.round(difference), true));
                } else {
                    if (userRepository != null && userRepository.getAllUsers().size() > 0) {
                        new CommonClassForAPI().postLocalBackgroundDData(null, userRepository.getAllUsers());
                        userRepository.deleteUserAll();
                        Utils.setToast(contextS, "Upload Data");
                    }
                    new CommonClassForAPI().postBackgroundDData(null, new BackgroundServiceModel(backgroundServiceModel.getTripPlannerVehicleId(), latitude, longitude, backgroundServiceModel.getRecordStatus(), getLocalDate(new Date().toString()), backgroundServiceModel.getTripPlannerConfirmedDetailId(), Math.round(difference)));
                }
            }
        } else {
            SharePrefs.getInstance(contextS).putString(SharePrefs.Previous_lat, String.valueOf(latitude));
            SharePrefs.getInstance(contextS).putString(SharePrefs.Previous_lag, String.valueOf(longitude));
            double previouslat = Double.parseDouble(SharePrefs.getInstance(contextS).getString(SharePrefs.Previous_lat));
            double previouslag = Double.parseDouble(SharePrefs.getInstance(contextS).getString(SharePrefs.Previous_lag));
            double difference = Math.round(Utils.distance(previouslat, previouslag, latitude, longitude) * 1609.34);
            SharePrefs.getInstance(contextS).putString(SharePrefs.START_DATE, new Date().toString());

            if (!Utils.checkInternetConnection(contextS)) {

                if (userRepository == null) {
                    userRepository = new UserRepository(contextS);
                }
                userRepository.insertUser(new UserLatLngModel(backgroundServiceModel.getTripPlannerVehicleId(), latitude, longitude, 0, backgroundServiceModel.getRecordStatus(), getDateFormat(new Date().toString()), backgroundServiceModel.getTripPlannerConfirmedDetailId(), Math.round(difference), true));
                Toast.makeText(contextS, contextS.getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            } else {
                if (userRepository != null && userRepository.getAllUsers().size() > 0) {
                    new CommonClassForAPI().postLocalBackgroundDData(null, userRepository.getAllUsers());
                    userRepository.deleteUserAll();
                }
                new CommonClassForAPI().postBackgroundDData(null, new BackgroundServiceModel(backgroundServiceModel.getTripPlannerVehicleId(), latitude, longitude, backgroundServiceModel.getRecordStatus(), getLocalDate(new Date().toString()), backgroundServiceModel.getTripPlannerConfirmedDetailId(), Math.round(difference)));
            }
        }

    }

    public static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    public static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public static String getUrl(Context context, LatLng origin, LatLng dest, String waypointsLatLong) {
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String sensor = "sensor=false";
        String waypoint = "waypoints=" + waypointsLatLong;
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + waypoint;
        String url = "https://maps.googleapis.com/maps/api/directions/json" + "?" + parameters + "&key=" + context.getResources().getString(R.string.google_maps_key);
        Log.e("WAY_URLLL", url);
        //getDistance(origin,dest);
        return url;
    }

    public static String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();

            inputStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);

            }

            data = sb.toString();
            Log.d("downloadUrl", data);

            br.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null)
                inputStream.close();
            urlConnection.disconnect();
        }

        Log.d("data downlaod", data);
        return data;
    }

    public static void showProgressDialog(Activity activity) {
        try {
            if (customDialog != null)
                customDialog.dismiss();
            if (customDialog == null) {
                customDialog = new Dialog(activity, R.style.CustomDialog);
            }
            LayoutInflater inflater = LayoutInflater.from(activity);
            if (inflater != null) {
                View mView = inflater.inflate(R.layout.progress_dialog, null);
                customDialog.setContentView(mView);
                if (!customDialog.isShowing() && customDialog != null && !activity.isFinishing()) {
                    customDialog.show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
