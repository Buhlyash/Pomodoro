package com.example.pomodoro2.util;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.pomodoro2.ui.timer.AppConstants;
import com.example.pomodoro2.MainActivity;
import com.example.pomodoro2.R;
import com.example.pomodoro2.TimerNotificationActionReceiver;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NotificationUtil {
    private static final String CHANNEL_ID_TIMER = "menu_timer";
    private static final String CHANNEL_NAME_TIMER = "Pomodoro App";
    private static final int TIMER_ID = 0;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void showTimerExpired(Context context) {
        Intent startIntent = new Intent(context, TimerNotificationActionReceiver.class);
        startIntent.setAction(AppConstants.ACTION_START);
        PendingIntent startPendingIntent = PendingIntent.getBroadcast(context,
                0, startIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder nBuilder = getBasicNotificationBuilder(context, CHANNEL_ID_TIMER, true);
        nBuilder.setContentTitle("Timer Expired!")
                .setContentText("Start next?")
                .setContentIntent(getPendingIntentWithStack(context, MainActivity.class))
                .addAction(R.drawable.ic_play, "Start", startPendingIntent);

        NotificationManager nManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.createNotificationChannel(createNotificationChannelExpired(CHANNEL_ID_TIMER, CHANNEL_NAME_TIMER, true));
        nManager.notify(TIMER_ID, nBuilder.build());

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void showTimerRunning(Context context, long wakeUpTime) {
        Intent stopIntent = new Intent(context, TimerNotificationActionReceiver.class);
        stopIntent.setAction(AppConstants.ACTION_STOP);
        PendingIntent stopPendingIntent = PendingIntent.getBroadcast(context,
                0, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent pauseIntent = new Intent(context, TimerNotificationActionReceiver.class);
        pauseIntent.setAction(AppConstants.ACTION_PAUSE);
        PendingIntent pausePendingIntent = PendingIntent.getBroadcast(context,
                0, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        DateFormat df = SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT);

        Date date = new Date(wakeUpTime);
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("hh:mm:ss");
        NotificationCompat.Builder nBuilder = getBasicNotificationBuilder(context, CHANNEL_ID_TIMER, true);
        nBuilder.setContentTitle("Timer is Running.")
                .setContentText(String.format(Locale.getDefault(),"End: %s", formatForDateNow.format(date)))
                .setContentIntent(getPendingIntentWithStack(context, MainActivity.class))
                .setOngoing(true)
                .addAction(R.drawable.ic_stop, "Stop", stopPendingIntent)
                .addAction(R.drawable.ic_pause, "Pause", pausePendingIntent);

        NotificationManager nManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.createNotificationChannel(createNotificationChannelExpired(CHANNEL_ID_TIMER, CHANNEL_NAME_TIMER, true));
        nManager.notify(TIMER_ID, nBuilder.build());

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void showTimerPaused(Context context) {
        Intent resumeIntent = new Intent(context, TimerNotificationActionReceiver.class);
        resumeIntent.setAction(AppConstants.ACTION_RESUME);
        PendingIntent resumePendingIntent = PendingIntent.getBroadcast(context,
                0, resumeIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder nBuilder = getBasicNotificationBuilder(context, CHANNEL_ID_TIMER, true);
        nBuilder.setContentTitle("Timer is paused.")
                .setContentText("Resume?")
                .setContentIntent(getPendingIntentWithStack(context, MainActivity.class))
                .setOngoing(true)
                .addAction(R.drawable.ic_play, "Resume", resumePendingIntent);

        NotificationManager nManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel ch = createNotificationChannelExpired(CHANNEL_ID_TIMER, CHANNEL_NAME_TIMER, true);
        nManager.createNotificationChannel(ch);
        nManager.notify(TIMER_ID, nBuilder.build());

    }

    private static NotificationCompat.Builder getBasicNotificationBuilder(Context context, String channelId, boolean playSound) {
        Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_timer)
                .setAutoCancel(true)
                .setDefaults(0);
        if (playSound) {
            nBuilder.setSound(notificationSound);
        }
        return nBuilder;
    }

    private static <T> PendingIntent getPendingIntentWithStack(Context context, Class<T> javaClass) {
        Intent resultIntent = new Intent(context, javaClass);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(javaClass);
        stackBuilder.addNextIntent(resultIntent);

        return stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @TargetApi(26)
    private static NotificationChannel createNotificationChannelExpired(String channelId, String channelName, boolean playSound) {
        NotificationChannel nChannel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int channelImportance;
            if (playSound) {
                channelImportance = NotificationManager.IMPORTANCE_DEFAULT;
            } else {
                channelImportance = NotificationManager.IMPORTANCE_LOW;
            }
            nChannel = new NotificationChannel(channelId, channelName, channelImportance);
            nChannel.enableLights(true);
            nChannel.setLightColor(Color.RED);
        }
        return nChannel;
    }

    public static void hideTimerNotification(Context context) {
        NotificationManager nManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.cancel(TIMER_ID);
    }
}
