package com.aloe.zxlib.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.aloe.zxlib.R;
import com.aloe.zxlib.activity.CaptureActivity;
import com.aloe.zxlib.camera.CameraManager;
import com.aloe.zxlib.decode.DecodeThread;
import com.google.zxing.Result;

/**
 * @author Aloe.Zheng
 * @time 2018/5/10 16:23
 * @email aloe200@163.com
 */
public class CaptureActivityHandler extends Handler {
  private final CaptureActivity activity;
  private final DecodeThread decodeThread;
  private final CameraManager cameraManager;
  private State state;

  public CaptureActivityHandler(final CaptureActivity activity, final CameraManager cameraManager,
                                final int decodeMode) {
    this.activity = activity;
    decodeThread = new DecodeThread(activity, decodeMode);
    decodeThread.start();
    state = State.SUCCESS;
    this.cameraManager = cameraManager;
    cameraManager.startPreview();
    restartPreviewAndDecode();
  }

  @Override
  public final void handleMessage(final Message message) {
    if (message.what == R.id.restart_preview) {
      restartPreviewAndDecode();
    } else if (message.what == R.id.decode_succeeded) {
      state = State.SUCCESS;
      Bundle bundle = message.getData();
      activity.handleDecode((Result) message.obj, bundle);
    } else if (message.what == R.id.decode_failed) {
      state = State.PREVIEW;
      cameraManager.requestPreviewFrame(decodeThread.getHandler(), R.id.decode);
    } else if (message.what == R.id.return_scan_result) {
      activity.setResult(Activity.RESULT_OK, (Intent) message.obj);
      activity.finish();
    }
  }

  public final void quitSynchronously() {
    state = State.DONE;
    cameraManager.stopPreview();
    Message quit = Message.obtain(decodeThread.getHandler(), R.id.quit);
    quit.sendToTarget();
    try {
      decodeThread.join(500L);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    removeMessages(R.id.decode_succeeded);
    removeMessages(R.id.decode_failed);
  }

  private void restartPreviewAndDecode() {
    if (state == State.SUCCESS) {
      state = State.PREVIEW;
      cameraManager.requestPreviewFrame(decodeThread.getHandler(), R.id.decode);
    }
  }

  private enum State {
    /**
     * 预览.
     */
    PREVIEW,
    /**
     * 成功.
     */
    SUCCESS,
    /**
     * 完成.
     */
    DONE
  }
}
