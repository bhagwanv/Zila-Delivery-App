package com.sk.ziladelivery.utilities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;

import com.google.android.material.navigation.NavigationView;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class CommonMethods {

    public static String FORMAT_1_MONDAY_yyyy = "yyyy-MM-dd hh:mm:ss.sss";
    public static String DATE_FORMAT_DD_MM_yyyy = "dd-MM-yyyy";
    public static String Time_fomate = "mm:ss.sss";
    public static String getTag(Object object)
    {
        try
        {
            return object.getClass().getName();
        }catch (Exception e)
        {
            Logger.e("Common","getTag : "+e.toString());
        }
        return "";
    }


    public static String getOsVersion() {
        String version = Build.VERSION.RELEASE;
        return version;
    }

    public static void hideKeyboard(Activity context, View view) {
        // Then just use the following:
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static String changeLocalDateFormat(Date receivedDate, String requiredFormat)
    {
        Date date = receivedDate;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(requiredFormat);
            return dateFormat.format(date);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return "";
    }


    public static void hideKeyboardAfterShare(final Activity context, final View view) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager inputManager = (InputMethodManager) context
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(
                        view.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, 300);
    }

    public static void showKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }


    public static String checkNull(String str) {
        String totalFormated = "";
        if (str != null && !str.equalsIgnoreCase("null")) {
            return str;
        }
        return totalFormated;
    }

    public static String replaceNull(String str) {
        String totalFormated = "";
        if (str != null && !str.equalsIgnoreCase("null")
                && !str.contains("null")) {
            return str.trim();
        } else if (str != null) {
            str = str.replace("null", "");
            return str.trim();
        }
        return totalFormated;
    }

    public static String firstLetterCapString(String string) {
        if (string != null && string.length() > 0) {
            string = string.toLowerCase();
            String firstLetter = string.substring(0, 1);
            string = firstLetter.toUpperCase() + string.substring(1, string.length());
            return string;
        }
        return "";
    }



    public static void saveBitmap(Bitmap mBitmap, File destinationPath) {

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(destinationPath);
            if (mBitmap.hasAlpha()) {
                mBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            } else {
                mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (Throwable ignore) {
            }
        }
    }



    public static Bitmap base64StringToBitmap(String encodedImage) {
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

    public static String imageToBase64String(String imagePath) {
        imagePath = imagePath.replace("file:/", "");
        Bitmap bm = BitmapFactory.decodeFile(imagePath);
        //bm = Bitmap.createScaledBitmap(bm, 100, 100, true);
        //bm = getResizedBitmap(bm, 100, 100);
        bm = scaleBitmap(bm, 100, 100);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] byteArrayImage = baos.toByteArray();
        String encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
        return encodedImage;
    }

    private static Bitmap scaleBitmap(Bitmap bm, int maxWidth, int maxHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();

        Log.v("Pictures", "Width and height are " + width + "--" + height);

        if (width > height) {
            // landscape
            float ratio = (float) width / maxWidth;
            width = maxWidth;
            height = (int) (height / ratio);
        } else if (height > width) {
            // portrait
            float ratio = (float) height / maxHeight;
            height = maxHeight;
            width = (int) (width / ratio);
        } else {
            // square
            height = maxHeight;
            width = maxWidth;
        }

        Log.v("Pictures", "after scaling Width and height are " + width + "--" + height);

        bm = Bitmap.createScaledBitmap(bm, width, height, true);
        return bm;
    }

    public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);
        // RECREATE THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }

    public static void changeNavigationViewText(AppCompatActivity activity, NavigationView navigationView) {
        Menu m = navigationView.getMenu();
        for (int i = 0; i < m.size(); i++) {
            MenuItem mi = m.getItem(i);
            //for aapplying a font to subMenu ...
            //SubMenu subMenu = mi.getSubMenu();
            //if (subMenu!=null && subMenu.size() >0 ) {
            //for (int j=0; j <subMenu.size();j++) {
            //MenuItem subMenuItem = subMenu.getItem(j);
            //applyFontToMenuItem(activity,subMenuItem);
            //}
            //}
            //if(i==2)
            //mi.setVisible(false);
            //the method we have create in activity
            applyFontToMenuItem(activity, mi);
        }
    }

    public static void applyFontToMenuItem(AppCompatActivity activity, MenuItem mi) {
        Typeface font = Typeface.createFromAsset(activity.getAssets(), "Roboto-Regular.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    public static String getCurrentDateTime(){
        String mCurrentDateTime="";
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd - hh:mm:ss a");
        mCurrentDateTime= df.format(c.getTime());
        return mCurrentDateTime;
    }


}