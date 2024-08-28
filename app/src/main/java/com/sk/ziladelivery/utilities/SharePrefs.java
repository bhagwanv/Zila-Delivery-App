package com.sk.ziladelivery.utilities;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by User on 13-11-2018.
 */

public class SharePrefs {
    public static final String TOKEN_NAME = "token_name";
    public static final String TOKEN_PASSWORD = "token_pass";
    public static String LANGUAGE = "LANGUAGE";
    public static String SHARED_PREF_NAME = "fcmsharedprefname";
    public static String KEY_ACCESS_TOKEN = "token";
    public static String END_LAT = "end_lat";
    public static String END_LAG = "end_lag";
    public static String PEOPLE_ID = "people_id";
    public static String NOT_LAST_MILE_APP = "is_not_last_mile_app";
    public static String TRIPPLANERMASTERID = "tripPlanerMasterID";
    public static String LOGGED = "logged";
    public static String EMAILID = "emailid";
    public static String SK_CODE = "sk_code";
    public static String ASSIGNMENT_ID = "assignment_id";
    public static String ROLE = "role";
    public static String WAREHOUSE_ID = "warehouse_id";
    public static String COMPANY_ID = "company_id";
    public static String PEAOPLE_FIRST_NAME = "people_first_name";
    public static String PEAOPLE_LAST_NAME = "people_last_name";
    public static String PEAOPLE_EMAIL = "people_email";
    public static String PEAOPLE_IMAGE = "people_image";
    public static String MOBILE = "mobile";
    public static String TOKEN = "token";
    public static String VEHICLE_NUMBER = "vehicle_number";
    public static String VEHICLE_NAME = "vehicle_name";
    public static String PREFERENCE = "skDelivery";
    public static String CASHMANAGMENT_PREFERENCE = "cash_managmentpref";
    public static String MAP_ROUTE = "map_route";
    public static String MY_TASK_DATA = "My_task_data";
    public static String OTP_NUMBER = "Otp_Numbers";
    public static String CASH_AMOUNT_JSON = "Cash_amount_json";
    public static String EDIT_AMOUNT_JSON = "Edit_amount_json";
    public static String CASH_AMOUNT = "Cash_amount";
    public static String CURRENCY_MODEL = "CURRENCY_MODEL ";
    public static String CHEQUE_AMOUNT = "Cheque_amount";
    public static String ONLINE_AMOUNT = "Online_amount";
    public static String ASSIGN_ID = "Assign_ID";
    public static String ASSIGN_MODEL = "Assign_model";
    public static String HISTORY_MODEL = "History_model";
    public static String BACKGROUND_DATA = "background";
    public static String UPDATE_DATA = "Update_data";
    public static String ISRAZORPAYENABLE = "israzorpayenable";
    public static String SecondaryAPIUrl = "SecondaryAPIUrl";
    public static String VIDEOUPLOADBASEURL = "VideoUploadBaseUrl";
    public static String LogDboyLoctionMeter = "LogDboyLoctionMeter";
    public static String BASEURL = "base_url";
    public static String IsQRCodeEnable = "IsQRCodeEnable";
    public static String FLAG = "flag";
    public static String TripPlannerVehicleId = "TripPlannerVehicleId";
    public static String TRIP_ID = "TripId";
    public static String RecordStatus = "RecordStatus";
    public static String TRIP_MASTER_ID = "trip_id";
    public static String ALL_TRIP_SLECTED = "trip_slected";
    public static String TripPlannerConfirmedDetailId = "TripPlannerConfirmedDetailId";
    public static final String Previous_lat = "Previous_lat";
    public static final String Previous_lag = "Previous_lag";
    public static final String Previous_recode_status = "previoues_recode_status";
    public static final String Previous_detailsID = "detail_id";
    public static final String START_DATE = "Start_date";
    public static final String TripPlannerConfirmedMasterId = "tripPlannerConfirmedMasterId";
    // HDFC credentials
    public static final String GATWAY_URL = "gatway_url";
    public static final String HAS_RETURN_ORDER = "cancel_url";
    private static final String PREF_NAME = "DeliveryApp";
    public static String ASSIGN_ID_Notifaction = "Assign_ID_notifcation";

    public static final String ISORDERCANCELED = "IsOrderCanceled";
    public static String REMAININGTIME = "RemainingTime";
    public static String ORDERID = "OrderId";
    public static String ISREATTEMPT = "isReattempt";
    public static String SELECTED_DATE = "SelectedDate";
    public static String REASON = "Reason";
    public static String SELECTED_STATUS = "SelectedStatus";
    public static String OTP_TIME = "OtpTimer";
    public static String OTP_TIME_REMAING = "OTPSentRemaningTimeInSec";

    public static final String last_lat = "last_lat";
    public static final String last_lag = "last_lag";

    public static String LOGIN_PASSWORD = "login_password";

    public static String VIDEO_URL = "video_url";


    private Context ctx;
    private SharedPreferences sharedPreferences;
    private SharedPreferences sharedPreferencesComplex;
    private static SharePrefs instance;


    public SharePrefs(Context context) {
        ctx = context;
        sharedPreferences = ctx.getSharedPreferences(PREF_NAME, 0);
    }

    public static SharePrefs getInstance(Context ctx) {
        if (instance == null) {
            instance = new SharePrefs(ctx);
        }
        return instance;
    }


    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(LOGGED, false);
    }

    public void putString(String key, String val) {
        sharedPreferences.edit().putString(key, val).apply();
    }

    public void putFloat(String key, float val) {
        sharedPreferences.edit().putFloat(key, val).apply();
    }

    public float getFloat(String key) {
        return sharedPreferences.getFloat(key, 0f);
    }

    public void putComplexData(String key, String val) {
        sharedPreferencesComplex.edit().putString(key, val).apply();
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, "");
    }

    public void putInt(String key, Integer val) {
        sharedPreferences.edit().putInt(key, val).apply();
    }

    public void putLong(String key, Long val) {
        sharedPreferences.edit().putLong(key, val).apply();
    }

    public long getLong(String key) {
        return sharedPreferences.getLong(key, 0);
    }

    public int getInt(String key) {
        return sharedPreferences.getInt(key, 0);
    }


    public void putBoolean(String key, Boolean val) {
        sharedPreferences.edit().putBoolean(key, val).apply();
    }

    public Boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }


    public void clearSharePrefs() {
        sharedPreferences.edit().clear().commit();
    }


    public static String getStringSharedPreferences(Context context, String name) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE, 0);
        return settings.getString(name, "");
    }

    // for username string preferences
    public static void setStringSharedPreference(Context context, String name, String value) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(name, value);
        editor.apply();
    }

    public static void setCashmanagmentSharedPreference(Context context, String name, String value) {
        SharedPreferences settings = context.getSharedPreferences(CASHMANAGMENT_PREFERENCE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(name, value);
        editor.apply();
    }

    public static String getCashmanagmentSharedPreferences(Context context, String name) {
        SharedPreferences settings = context.getSharedPreferences(CASHMANAGMENT_PREFERENCE, 0);
        return settings.getString(name, "");
    }

    public static void setMapRouteSharedPreference(Context context, String name, String value) {
        SharedPreferences settings = context.getSharedPreferences(MAP_ROUTE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(name, value);
        editor.apply();
    }

    public static String getMapRouteSharedPreferences(Context context, String name) {
        SharedPreferences settings = context.getSharedPreferences(MAP_ROUTE, 0);
        return settings.getString(name, "");
    }


    public boolean storeToken(String Token) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ACCESS_TOKEN, Token);
        editor.apply();
        return true;
    }


    public String getToken() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_ACCESS_TOKEN, null);
    }
}
