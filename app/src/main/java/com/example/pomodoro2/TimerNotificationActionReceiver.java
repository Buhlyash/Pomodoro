package com.example.pomodoro2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.pomodoro2.ui.timer.AppConstants;
import com.example.pomodoro2.ui.timer.TimerFragment;
import com.example.pomodoro2.util.NotificationUtil;
import com.example.pomodoro2.util.PrefUtil;

public class TimerNotificationActionReceiver extends BroadcastReceiver {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case AppConstants.ACTION_STOP:
                TimerFragment.removeAlarm(context);
                PrefUtil.setTimerState(TimerFragment.TimerState.Stopped, context);
                NotificationUtil.hideTimerNotification(context);
                break;
            case AppConstants.ACTION_PAUSE:
                long secondsRemaining = PrefUtil.getSecondsRemaining(context);
                long alarmSetTime = PrefUtil.getAlarmSetTime(context);
                long nowSeconds = TimerFragment.getNowSeconds();

                secondsRemaining -= nowSeconds - alarmSetTime;
                PrefUtil.setSecondsRemaining(secondsRemaining, context);

                TimerFragment.removeAlarm(context);
                PrefUtil.setTimerState(TimerFragment.TimerState.Paused, context);
                NotificationUtil.showTimerPaused(context);
                break;
            case AppConstants.ACTION_RESUME:
                long secondsRemaining2 = PrefUtil.getSecondsRemaining(context);
                long wakeUpTime = TimerFragment.setAlarm(context, TimerFragment.getNowSeconds(), secondsRemaining2);
                PrefUtil.setTimerState(TimerFragment.TimerState.Running, context);
                NotificationUtil.showTimerRunning(context, wakeUpTime);
                break;
            case AppConstants.ACTION_START:
                int minutesRemaining;
                if (PrefUtil.getCountOfTimer(context) > PrefUtil.getCountOfRest(context) && PrefUtil.getCountOfTimer(context) != 4) {
                    minutesRemaining = PrefUtil.getRestLength(context);
                } else if (PrefUtil.getCountOfTimer(context) == PrefUtil.getCountOfRest(context) && PrefUtil.getCountOfTimer(context) != 4){
                    minutesRemaining = PrefUtil.getTimerLength(context);
                } else {
                    minutesRemaining = PrefUtil.getLongRestLength(context);
                }
                long secondsRemaining3 = minutesRemaining * 60L;
                long wakeUpTime2 = TimerFragment.setAlarm(context, TimerFragment.getNowSeconds(), secondsRemaining3);
                PrefUtil.setTimerState(TimerFragment.TimerState.Running, context);
                PrefUtil.setSecondsRemaining(secondsRemaining3, context);
                NotificationUtil.showTimerRunning(context, wakeUpTime2);
                break;
        }
    }
}