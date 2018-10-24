package com.aloe.zxlib.camera;

import android.annotation.SuppressLint;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.SystemClock;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.RejectedExecutionException;

/**
 * @author Aloe.Zheng
 * @time 2018/5/10 16:30
 * @email aloe200@163.com
 */
public class AutoFocusManager implements Camera.AutoFocusCallback {
  private static final long AUTO_FOCUS_INTERVAL_MS = 2000L;
  private static final Collection<String> FOCUS_MODES_CALLING_AF;

  static {
    FOCUS_MODES_CALLING_AF = new ArrayList<>(2);
    FOCUS_MODES_CALLING_AF.add(Camera.Parameters.FOCUS_MODE_AUTO);
    FOCUS_MODES_CALLING_AF.add(Camera.Parameters.FOCUS_MODE_MACRO);
  }

  private final boolean useAutoFocus;
  private final Camera camera;
  private boolean stopped;
  private boolean focusing;
  private AsyncTask<?, ?, ?> outstandingTask;

  AutoFocusManager(final Camera camera) {
    this.camera = camera;
    String currentFocusMode = camera.getParameters().getFocusMode();
    useAutoFocus = FOCUS_MODES_CALLING_AF.contains(currentFocusMode);
    start();
  }

  @Override
  public final synchronized void onAutoFocus(final boolean success, final Camera theCamera) {
    focusing = false;
    autoFocusAgainLater();
  }

  @SuppressLint("NewApi")
  private synchronized void autoFocusAgainLater() {
    if (!stopped && outstandingTask == null) {
      AutoFocusTask newTask = new AutoFocusTask(this);
      try {
        newTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        outstandingTask = newTask;
      } catch (RejectedExecutionException e) {
        e.printStackTrace();
      }
    }
  }

  public final synchronized void start() {
    if (useAutoFocus) {
      outstandingTask = null;
      if (!stopped && !focusing) {
        try {
          camera.autoFocus(this);
          focusing = true;
        } catch (RuntimeException e) {
          e.printStackTrace();
          autoFocusAgainLater();
        }
      }
    }
  }

  private synchronized void cancelOutstandingTask() {
    if (outstandingTask != null) {
      if (outstandingTask.getStatus() != AsyncTask.Status.FINISHED) {
        outstandingTask.cancel(true);
      }
      outstandingTask = null;
    }
  }

  public final synchronized void stop() {
    stopped = true;
    if (useAutoFocus) {
      cancelOutstandingTask();
      try {
        camera.cancelAutoFocus();
      } catch (RuntimeException e) {
        e.printStackTrace();
      }
    }
  }

  private static final class AutoFocusTask extends AsyncTask<Object, Object, Object> {
    private WeakReference<AutoFocusManager> reference;

    private AutoFocusTask(final AutoFocusManager manager) {
      reference = new WeakReference<>(manager);
    }

    @Override
    protected Object doInBackground(final Object... voids) {
      SystemClock.sleep(AUTO_FOCUS_INTERVAL_MS);
      AutoFocusManager manager = reference.get();
      if (manager != null) {
        manager.start();
      }
      return null;
    }
  }
}
