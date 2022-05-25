package com.example.pomodoro2.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;

import com.example.pomodoro2.ui.timer.TimerFragment;

public class PrefUtil {

    private static final String PREVIOUS_TIMER_LENGTH_SECONDS_ID = "com.example.timer.previous_timer_length";
    private static final String TIMER_STATE_ID = "com.example.timer.timer_state";

    private static final String TIMER_LENGTH_ID = "com.example.timer.timer_length";
    private static final String REST_LENGTH_ID = "com.example.timer.rest_length";
    private static final String LONG_REST_LENGTH_ID = "com.example.timer.long_rest_length";


    private static final String COUNT_OF_TIMER_ID = "com.example.timer.count_of_timer";
    private static final String COUNT_OF_REST_ID = "com.example.timer.count_of_rest";

    public static int getTimerLength(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(TIMER_LENGTH_ID, 25);
    }

    public static int getRestLength(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(REST_LENGTH_ID, 5);
    }

    public static int getLongRestLength(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(LONG_REST_LENGTH_ID, 15);
    }

    public static int getCountOfTimer (Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return  preferences.getInt(COUNT_OF_TIMER_ID, 0);
    }

    public static void setCountOfTimer(int count, Context context) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putInt(COUNT_OF_TIMER_ID, count);
        editor.apply();
    }

    public static int getCountOfRest (Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return  preferences.getInt(COUNT_OF_REST_ID, 0);
    }

    public static void setCountOfRest(int count, Context context) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putInt(COUNT_OF_REST_ID, count);
        editor.apply();
    }

    public static long getPreviousTimerLengthSeconds(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID, 0);
    }

    public static void setPreviousTimerLengthSeconds(long seconds, Context context) {
       SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
       editor.putLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID, seconds);
       editor.apply();
    }

    public static TimerFragment.TimerState getTimerState(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int ordinal = preferences.getInt(TIMER_STATE_ID, 0);
        return TimerFragment.TimerState.values()[ordinal];
    }

    public static void setTimerState(TimerFragment.TimerState state, Context context) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        int ordinal = state.ordinal();
        editor.putInt(TIMER_STATE_ID, ordinal);
        editor.apply();
    }

    private static final String SECONDS_REMAINING_ID = "com.example.timer.seconds_remaining";

    public static long getSecondsRemaining(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getLong(SECONDS_REMAINING_ID, 0);
    }

    public static void setSecondsRemaining(long seconds, Context context) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putLong(SECONDS_REMAINING_ID, seconds);
        editor.apply();
    }

    private static final String ALARM_SET_TIME_ID = "com.example.timer.backgrounded_timer";

    public static long getAlarmSetTime(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getLong(ALARM_SET_TIME_ID, 0);
    }

    public static void setAlarmSetTime(long time, Context context) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putLong(ALARM_SET_TIME_ID, time);
        editor.apply();
    }

}
