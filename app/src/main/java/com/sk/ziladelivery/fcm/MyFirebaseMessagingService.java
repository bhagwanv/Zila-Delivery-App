package com.sk.ziladelivery.fcm;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.sk.ziladelivery.R;
import com.sk.ziladelivery.ui.views.main.MainActivity;
import com.sk.ziladelivery.ui.views.main.NewOrderPlaceActivity;
import com.sk.ziladelivery.ui.views.main.WelcomeActivity;
import com.sk.ziladelivery.utilities.RxBus;
import com.sk.ziladelivery.utilities.SharePrefs;

import org.json.JSONObject;

public class MyFirebaseMessagingService extends FirebaseMessagingService {


    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        System.out.println("Message data payload: " + remoteMessage.getData());
        int AssignmentID;
        String body = "";
        String title = "";
        String TxnNo = "";
        boolean IsApproved = false;
        Boolean isCaptured = false;
        try {
            JSONObject object = new JSONObject(remoteMessage.getData());
            if (object.has("notify_type") && !TextUtils.isEmpty(object.getString("notify_type"))) {
                switch (object.getString("notify_type")) {
                    case "return":
                        SharePrefs.getInstance(getApplicationContext()).putBoolean(SharePrefs.HAS_RETURN_ORDER, true);
                        setNotification(isCaptured, body, title);
                        break;
                    case "returnOrder":
                        SharePrefs.getInstance(getApplicationContext()).putBoolean(SharePrefs.HAS_RETURN_ORDER, true);
                        setNotification(isCaptured, body, title);
                        break;
                    case "CashOTPApproval":
                        if (object.has("body")) {
                            body = object.getString("body");
                            title = object.getString("title");
                            AssignmentID = object.getInt("AssignmentID");
                            SharePrefs.getInstance(getApplicationContext()).putInt(SharePrefs.ASSIGN_ID_Notifaction,AssignmentID);
                            RxBus.getInstance().sendEvent("CashOTPApproval");
                            Intent intent = null;
                            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            PendingIntent pendingIntent;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                                 pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
                            }else {
                                 pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                            }
                            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), getResources().getString(R.string.app_name))
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .setContentTitle(title)
                                    .setContentText(body)
                                    .setAutoCancel(true)
                                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                                    .setContentIntent(pendingIntent)
                                    .setChannelId(getResources().getString(R.string.app_name));

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

                            NotificationManagerCompat notificationManagerCompat =
                                    NotificationManagerCompat.from(getApplicationContext());
                            notificationManagerCompat.notify((int) System.currentTimeMillis(), builder.build());
                        }
                        break;
                    case "UPI Callback":
                        if (object.has("body")) {
                            String data = object.getString("body");
                            JSONObject obj = new JSONObject(data);
                            isCaptured = obj.getBoolean("IsCaptured");
                        }
                        setNotification(isCaptured, body, title);
                        break;
                    case "QRCodePayment":
                        if (object.has("body")) {
                            body = object.getString("body");
                            title = object.getString("title");
                            TxnNo = object.getString("UPITxnID");
                            RxBus.getInstance().sendEvent(TxnNo);
                            Intent intent = null;
                            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            intent = new Intent(getApplicationContext(), NewOrderPlaceActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            PendingIntent pendingIntent = null;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                                pendingIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), new Intent(), PendingIntent.FLAG_IMMUTABLE);
                            }else {
                                pendingIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
                            }
                            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), getResources().getString(R.string.app_name))
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .setContentTitle(title)
                                    .setContentText(body)
                                    .setAutoCancel(true)
                                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                                    .setContentIntent(pendingIntent)
                                    .setChannelId(getResources().getString(R.string.app_name));

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

                            NotificationManagerCompat notificationManagerCompat =
                                    NotificationManagerCompat.from(getApplicationContext());
                            notificationManagerCompat.notify((int) System.currentTimeMillis(), builder.build());
                        }
                        break;
                    case "Order Action Updated Notification":
                        if (object.has("body")) {
                            body = object.getString("body");
                            title = object.getString("title");
                            RxBus.getInstance().sendEvent("OrderId");
                            Intent intent = null;
                            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            PendingIntent pendingIntent;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                                 pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
                            }else {
                                pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                            }

                            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), getResources().getString(R.string.app_name))
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .setContentTitle(title)
                                    .setContentText(body)
                                    .setAutoCancel(true)
                                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                                    .setContentIntent(pendingIntent)
                                    .setChannelId(getResources().getString(R.string.app_name));

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

                            NotificationManagerCompat notificationManagerCompat =
                                    NotificationManagerCompat.from(getApplicationContext());
                            notificationManagerCompat.notify((int) System.currentTimeMillis(), builder.build());
                        }
                        break;

                    case "Sarthi App Accpect Notification":
                        if (object.has("body")) {
                            body = object.getString("body");
                            title = object.getString("title");
                            IsApproved = object.getBoolean("IsApproved");
                            RxBus.getInstance().sendEvent(IsApproved);
                            Intent intent = null;
                            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            PendingIntent pendingIntent;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                                pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
                            }else {
                                pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                            }
                            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), getResources().getString(R.string.app_name))
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .setContentTitle(title)
                                    .setContentText(body)
                                    .setAutoCancel(true)
                                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                                    .setContentIntent(pendingIntent)
                                    .setChannelId(getResources().getString(R.string.app_name));

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

                            NotificationManagerCompat notificationManagerCompat =
                                    NotificationManagerCompat.from(getApplicationContext());
                            notificationManagerCompat.notify((int) System.currentTimeMillis(), builder.build());
                        }
                        break;
                    case "Sales Person Order Reject Notification":
                        if (object.has("body")) {
                            body = object.getString("body");
                            title = object.getString("title");
                            RxBus.getInstance().sendEvent("Reject");
                            Intent intent = null;
                            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            PendingIntent pendingIntent;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                                pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
                            }else {
                                pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                            }
                            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), getResources().getString(R.string.app_name))
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .setContentTitle(title)
                                    .setContentText(body)
                                    .setAutoCancel(true)
                                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                                    .setContentIntent(pendingIntent)
                                    .setChannelId(getResources().getString(R.string.app_name));

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                NotificationChannel mChannel = new NotificationChannel(getResources().getString(R.string.app_name), getResources().getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH);
                                mChannel.enableLights(true);
                                mChannel.enableVibration(true);
                                mChannel.setLightColor(Color.YELLOW);
                                mChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                                notificationManager.createNotificationChannel(mChannel);
                            }

                            NotificationManagerCompat notificationManagerCompat =
                                    NotificationManagerCompat.from(getApplicationContext());
                            notificationManagerCompat.notify((int) System.currentTimeMillis(), builder.build());
                        }
                        break;


                }
            } else {
                if (object.has("body")) {
                    body = object.getString("body");
                }
                if (object.has("title")) {
                    title = object.getString("title");
                }
                if (title.equals("true")) {
                    logout();
                    RxBus.getInstance().sendEvent(title);
                } else {
                    setNotification(isCaptured, body, title);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void logout() {
        PreferenceManager.getDefaultSharedPreferences(this).edit().clear().apply();
        SharePrefs.getInstance(getApplicationContext()).putBoolean(SharePrefs.LOGGED, false);
        Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


    private void setNotification(Boolean isCaptured, String messageBody, String title) {
        try {
            if (isCaptured) {
                RxBus.getInstance().sendEvent(isCaptured);
            } else {
                RxBus.getInstance().sendEvent(title);
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
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
                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), getResources().getString(R.string.app_name))
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setContentInfo(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setChannelId(getResources().getString(R.string.app_name));

                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
                notificationManagerCompat.notify((int) System.currentTimeMillis(), builder.build());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}