package com.aloe.zxlib.camera;

import android.graphics.Point;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;

/**
 * @author Aloe.Zheng
 * @time 2018/5/10 16:28
 * @email aloe200@163.com
 */
public class PreviewCallback implements Camera.PreviewCallback {

  private final CameraConfigurationManager configManager;
  private Handler previewHandler;
  private int previewMessage;

  public PreviewCallback(final CameraConfigurationManager configManager) {
    this.configManager = configManager;
  }

  public final void setHandler(final Handler previewHandler, final int previewMessage) {
    this.previewHandler = previewHandler;
    this.previewMessage = previewMessage;
  }

  @Override
  public final void onPreviewFrame(final byte[] data, final Camera camera) {
    Point cameraResolution = configManager.getCameraResolution();
    Handler thePreviewHandler = previewHandler;
    if (cameraResolution != null && thePreviewHandler != null) {
      Message message = thePreviewHandler.obtainMessage(previewMessage, cameraResolution.x,
          cameraResolution.y, data);
      message.sendToTarget();
      previewHandler = null;
    }
  }
}
