package com.aloe.zxlib.utils;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;

import java.lang.ref.WeakReference;

/**
 * @author Aloe.Zheng
 * @time 2018/5/10 16:24
 * @email aloe200@163.com
 */
public class InactivityTimer {
  private static final long INACTIVITY_DELAY_MS = 5 * 60 * 1000L;
  private AppCompatActivity activity;
  private BroadcastReceiver powerStatusReceiver;
  private boolean registered;
  private AsyncTask<Object, Object, Object> inactivityTask;

  public InactivityTimer(final AppCompatActivity activity) {
    this.activity = activity;
    powerStatusReceiver = new PowerStatusReceiver();
    registered = false;
    onActivity();
  }

  @SuppressLint("NewApi")
  public final synchronized void onActivity() {
    cancel();
    inactivityTask = new InactivityAsyncTask(activity);
    inactivityTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
  }

  public final synchronized void onPause() {
    cancel();
    if (registered) {
      activity.unregisterReceiver(powerStatusReceiver);
      registered = false;
    }
  }

  public final synchronized void onResume() {
    if (!registered) {
      activity.registerReceiver(powerStatusReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
      registered = true;
    }
    onActivity();
  }

  private synchronized void cancel() {
    AsyncTask<?, ?, ?> task = inactivityTask;
    if (task != null) {
      task.cancel(true);
      inactivityTask = null;
    }
  }

  public final void shutdown() {
    cancel();
  }

  private class PowerStatusReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, final Intent intent) {
      if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {
        boolean onBatteryNow = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1) <= 0;
        if (onBatteryNow) {
          InactivityTimer.this.onActivity();
        } else {
          InactivityTimer.this.cancel();
        }
      }
    }
  }

  private static final class InactivityAsyncTask extends AsyncTask<Object, Object, Object> {
    private WeakReference<AppCompatActivity> weakReference;

    private InactivityAsyncTask(final AppCompatActivity activity) {
      weakReference = new WeakReference<>(activity);
    }

    @Override
    protected Object doInBackground(final Object... objects) {
      SystemClock.sleep(INACTIVITY_DELAY_MS);
      AppCompatActivity activity = weakReference.get();
      if (activity != null) {
        activity.finish();
      }
      return null;
    }
  }
}
