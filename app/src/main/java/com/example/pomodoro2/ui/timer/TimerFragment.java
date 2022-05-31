package com.example.pomodoro2.ui.timer;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pomodoro2.R;
import com.example.pomodoro2.TimerExpiredReciver;
import com.example.pomodoro2.databinding.TimerFragmentBinding;
import com.example.pomodoro2.util.NotificationUtil;
import com.example.pomodoro2.util.PrefUtil;

import java.util.Calendar;

public class TimerFragment extends Fragment {
    public enum TimerState {
        Stopped,
        Paused,
        Running
    }

    private TimerFragmentBinding binding;
    private CountDownTimer timer;
    private static long timerLengthSeconds = 0;
    private TimerState timerState = TimerState.Stopped;
    private static long secondsRemaining = 0;
    private TextView textView;
    private ProgressBar progressBar;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
            manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        binding = com.example.pomodoro2.databinding.TimerFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        textView = binding.getRoot().findViewById(R.id.textViewCountDown);
        progressBar = binding.getRoot().findViewById(R.id.progressCountDown);

        binding.fabPlay.setOnClickListener(view -> {
            if (timerState != TimerState.Paused) {
                if (PrefUtil.getCountOfTimer(requireContext()) > PrefUtil.getCountOfRest(requireContext()) && PrefUtil.getCountOfTimer(requireContext()) != 4) {
                    Toast.makeText(requireContext(), "Время для отдыха!", Toast.LENGTH_SHORT).show();
                } else if (PrefUtil.getCountOfTimer(requireContext()) == PrefUtil.getCountOfRest(requireContext()) && PrefUtil.getCountOfTimer(requireContext()) != 4){
                    Toast.makeText(requireContext(), "Время для работы!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "Время для большого отдыха!", Toast.LENGTH_SHORT).show();
                }
                if (PrefUtil.getCountOfTimer(getContext()) > PrefUtil.getCountOfRest(getContext())) {
                    PrefUtil.setCountOfRest(PrefUtil.getCountOfRest(getContext()) + 1, getContext());
                } else {
                    PrefUtil.setCountOfTimer(PrefUtil.getCountOfTimer(getContext()) + 1, getContext());
                }
            }
            startTimer();
            timerState = TimerState.Running;
            updateButtons();
        });

        binding.fabPause.setOnClickListener(view -> {
            timer.cancel();
            timerState = TimerState.Paused;
            updateButtons();
        });

        binding.fabStop.setOnClickListener(view -> {
            timer.cancel();
            onTimerFinished();
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        initTimer();

        removeAlarm(getContext());
        NotificationUtil.hideTimerNotification(requireContext());
    }

    private void initTimer() {
        timerState = PrefUtil.getTimerState(getContext());

        setNewTimerLength();

        if (timerState == TimerState.Stopped) {
            setNewTimerLength();
        } else {
            setPreviousTimerLength();
        }
        if (timerState == TimerState.Running || timerState == TimerState.Paused) {
            secondsRemaining = PrefUtil.getSecondsRemaining(getContext());
        } else {
            secondsRemaining = timerLengthSeconds;
        }

        long alarmSetTime = PrefUtil.getAlarmSetTime(getContext());
        if (alarmSetTime > 0) {
            secondsRemaining -= getNowSeconds() - alarmSetTime;
        }

        if (secondsRemaining <= 0) {
            onTimerFinished();
        } else if(timerState == TimerState.Running) {
            startTimer();
        }
        updateButtons();
        updateCountdownUI();
    }

    private void onTimerFinished() {
        if (PrefUtil.getCountOfTimer(getContext()) == 4 && PrefUtil.getCountOfRest(getContext()) == 4) {
            PrefUtil.setCountOfTimer(0, getContext());
            PrefUtil.setCountOfRest(0, getContext());
        }
        timerState = TimerState.Stopped;
        setNewTimerLength();

        progressBar.setProgress(0);

        PrefUtil.setSecondsRemaining(timerLengthSeconds, getContext());
        secondsRemaining = timerLengthSeconds;
        updateButtons();
        updateCountdownUI();
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(requireContext(), notification);
        r.play();
    }

    private void startTimer() {
        timerState = TimerState.Running;
        timer = new CountDownTimer(secondsRemaining * 1000, 1000) {
            @Override
            public void onTick(long l) {
                secondsRemaining = l / 1000;
                updateCountdownUI();
            }

            @Override
            public void onFinish() {
                onTimerFinished();
            }
        }.start();
    }

    private void setNewTimerLength() {
        int lengthInMinutes;
        if (PrefUtil.getCountOfTimer(getContext()) > PrefUtil.getCountOfRest(getContext()) && PrefUtil.getCountOfTimer(getContext()) != 4) {
            lengthInMinutes = PrefUtil.getRestLength(getContext());
        } else if (PrefUtil.getCountOfTimer(getContext()) == PrefUtil.getCountOfRest(getContext()) && PrefUtil.getCountOfTimer(getContext()) != 4){
            lengthInMinutes = PrefUtil.getTimerLength(getContext());
        } else {
            lengthInMinutes = PrefUtil.getLongRestLength(getContext());
        }
        timerLengthSeconds = lengthInMinutes * 60L;

        progressBar.setMax((int)timerLengthSeconds);
    }

    private void setPreviousTimerLength() {
        timerLengthSeconds = PrefUtil.getPreviousTimerLengthSeconds(getContext());
        progressBar.setMax((int)timerLengthSeconds);
    }

    @SuppressLint("DefaultLocale")
    private void updateCountdownUI() {
        long minutesUntilFinished = secondsRemaining / 60;
        long secondsInMinuteUntilFinished = secondsRemaining - minutesUntilFinished * 60;
        String secondsStr = String.format("" + secondsInMinuteUntilFinished);
        textView.setText(String.format("%d:%s", minutesUntilFinished, secondsStr.length() == 2 ? (secondsStr) : ("0" + secondsStr)));
        progressBar.setProgress((int)(timerLengthSeconds - secondsRemaining));
    }

    private void updateButtons() {
        switch(timerState) {
            case Running:
                binding.fabPlay.setEnabled(false);
                binding.fabStop.setEnabled(true);
                binding.fabPause.setEnabled(true);
                break;
            case Stopped:
                binding.fabPlay.setEnabled(true);
                binding.fabStop.setEnabled(false);
                binding.fabPause.setEnabled(false);
                break;
            case Paused:
                binding.fabPlay.setEnabled(true);
                binding.fabStop.setEnabled(false);
                binding.fabPause.setEnabled(true);
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onPause() {
        super.onPause();
        if (timerState == TimerState.Running) {
            timer.cancel();
            long wakeUpTime = setAlarm(getContext(), getNowSeconds(), secondsRemaining);
            NotificationUtil.showTimerRunning(getContext(), wakeUpTime);
        } else if(timerState == TimerState.Paused) {
            NotificationUtil.showTimerPaused(getContext());
        }

        PrefUtil.setPreviousTimerLengthSeconds(timerLengthSeconds, getContext());
        PrefUtil.setSecondsRemaining(secondsRemaining, getContext());
        PrefUtil.setTimerState(timerState, getContext());
    }


    public static long setAlarm(Context context, long nowSeconds, long secondsRemaining) {
        long wakeUpTime = (nowSeconds + secondsRemaining) * 1000;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, TimerExpiredReciver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, wakeUpTime, pendingIntent);
        PrefUtil.setAlarmSetTime(nowSeconds, context);
        return wakeUpTime;
    }

    public static void removeAlarm(Context context) {
        Intent intent = new Intent(context, TimerExpiredReciver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        PrefUtil.setAlarmSetTime(0, context);
    }

    private static long nowSeconds;

    public static long getNowSeconds() {
        return Calendar.getInstance().getTimeInMillis() / 1000;
    }
}