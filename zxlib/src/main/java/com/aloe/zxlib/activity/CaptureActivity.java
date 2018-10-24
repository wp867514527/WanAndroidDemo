package com.aloe.zxlib.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.aloe.zxlib.R;
import com.aloe.zxlib.camera.CameraManager;
import com.aloe.zxlib.decode.DecodeThread;
import com.aloe.zxlib.utils.BeepManager;
import com.aloe.zxlib.utils.CaptureActivityHandler;
import com.aloe.zxlib.utils.InactivityTimer;
import com.google.zxing.Result;

import java.io.IOException;

/**
 * @author Aloe.Zheng
 * @time 2018/5/10 16:27
 * @email aloe200@163.com
 */
public class CaptureActivity extends AppCompatActivity implements SurfaceHolder.Callback {
  private CameraManager cameraManager;
  private CaptureActivityHandler handler;
  private InactivityTimer inactivityTimer;
  private BeepManager beepManager;
  private SurfaceView scanPreview;
  private FrameLayout scanCropView;
  private Rect mCropRect;
  private boolean isHasSurface;

  public final Handler getHandler() {
    return handler;
  }

  public final CameraManager getCameraManager() {
    return cameraManager;
  }

  @Override
  public final void onCreate(final Bundle icicle) {
    super.onCreate(icicle);
    Window window = getWindow();
    window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    setContentView(R.layout.activity_capture);
    scanPreview = findViewById(R.id.capture_preview);
    scanCropView = findViewById(R.id.capture_crop_view);
    ImageView scanLine = findViewById(R.id.capture_scan_line);
    inactivityTimer = new InactivityTimer(this);
    beepManager = new BeepManager(this);
    TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation
        .RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT,
        0.9f);
    animation.setDuration(4500);
    animation.setRepeatCount(-1);
    animation.setRepeatMode(Animation.RESTART);
    scanLine.startAnimation(animation);
  }

  @Override
  protected final void onResume() {
    super.onResume();
    cameraManager = new CameraManager(getApplication());
    handler = null;
    if (isHasSurface) {
      initCamera(scanPreview.getHolder());
    } else {
      scanPreview.getHolder().addCallback(this);
    }
    inactivityTimer.onResume();
  }

  @Override
  protected final void onPause() {
    if (handler != null) {
      handler.quitSynchronously();
      handler = null;
    }
    inactivityTimer.onPause();
    beepManager.close();
    cameraManager.closeDriver();
    if (!isHasSurface) {
      scanPreview.getHolder().removeCallback(this);
    }
    super.onPause();
  }

  @Override
  protected final void onDestroy() {
    inactivityTimer.shutdown();
    super.onDestroy();
  }

  @Override
  public final void surfaceCreated(final SurfaceHolder holder) {
    if (!isHasSurface) {
      isHasSurface = true;
      initCamera(holder);
    }
  }

  @Override
  public final void surfaceDestroyed(final SurfaceHolder holder) {
    isHasSurface = false;
  }

  @Override
  public final void surfaceChanged(final SurfaceHolder holder, final int format, final int width,
                                   final int height) {

  }

  public final void handleDecode(final Result rawResult, final Bundle bundle) {
    inactivityTimer.onActivity();
    beepManager.playBeepSoundAndVibrate();
    Intent resultIntent = new Intent();
    bundle.putInt("width", mCropRect.width());
    bundle.putInt("height", mCropRect.height());
    bundle.putString("result", rawResult.getText());
    resultIntent.putExtras(bundle);
    this.setResult(RESULT_OK, resultIntent);
    CaptureActivity.this.finish();
  }

  private void initCamera(final SurfaceHolder surfaceHolder) {
    if (surfaceHolder == null) {
      throw new IllegalStateException("No SurfaceHolder provided");
    }
    if (cameraManager.isOpen()) {
      return;
    }
    try {
      cameraManager.openDriver(surfaceHolder);
      if (handler == null) {
        handler = new CaptureActivityHandler(this, cameraManager, DecodeThread.ALL_MODE);
      }
      initCrop();
    } catch (IOException e) {
      e.printStackTrace();
      displayFrameworkBugMessageAndExit();
    } catch (RuntimeException e) {
      displayFrameworkBugMessageAndExit();
    }
  }

  private void displayFrameworkBugMessageAndExit() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle("zxing");
    builder.setMessage("Camera error");
    builder.setPositiveButton("OK", (dialog, which) -> finish());
    builder.setOnCancelListener(dialog -> finish());
    builder.show();
  }

  public final Rect getCropRect() {
    return mCropRect;
  }

  /**
   * 初始化截取的矩形区域.
   */
  private void initCrop() {
    int cameraWidth = cameraManager.getCameraResolution().y;
    int cameraHeight = cameraManager.getCameraResolution().x;
    //获取布局中扫描框的位置信息
    int[] location = new int[2];
    scanCropView.getLocationInWindow(location);
    int cropLeft = location[0];
    int cropTop = location[1] - getStatusBarHeight();
    int cropWidth = scanCropView.getWidth();
    int cropHeight = scanCropView.getHeight();
    //获取布局容器的宽高
    int containerWidth = scanPreview.getWidth();
    int containerHeight = scanPreview.getHeight();
    //计算最终截取的矩形的左上角顶点x坐标
    int x = cropLeft * cameraWidth / containerWidth;
    //计算最终截取的矩形的左上角顶点y坐标
    int y = cropTop * cameraHeight / containerHeight;
    //计算最终截取的矩形的宽度
    int width = cropWidth * cameraWidth / containerWidth;
    //计算最终截取的矩形的高度
    int height = cropHeight * cameraHeight / containerHeight;
    //生成最终的截取的矩形
    mCropRect = new Rect(x, y, width + x, height + y);
  }

  private int getStatusBarHeight() {
    int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
    if (resourceId > 0) {
      return getResources().getDimensionPixelSize(resourceId);
    } else {
      return 0;
    }
  }
}
