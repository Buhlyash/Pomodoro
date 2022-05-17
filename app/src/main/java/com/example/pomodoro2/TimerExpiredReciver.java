package com.example.pomodoro2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.pomodoro2.ui.timer.TimerFragment;
import com.example.pomodoro2.util.NotificationUtil;
import com.example.pomodoro2.util.PrefUtil;

public class TimerExpiredReciver extends BroadcastReceiver {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationUtil.showTimerExpired(context);

        PrefUtil.setTimerState(TimerFragment.TimerState.Stopped, context);
        PrefUtil.setAlarmSetTime(0, context);
    }
}