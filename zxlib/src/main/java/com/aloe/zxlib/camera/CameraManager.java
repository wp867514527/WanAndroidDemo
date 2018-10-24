package com.aloe.zxlib.camera;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera;
import android.os.Handler;
import android.view.SurfaceHolder;

import java.io.IOException;

/**
 * @author Aloe.Zheng
 * @time 2018/5/10 16:29
 * @email aloe200@163.com
 */
public class CameraManager {
  private final CameraConfigurationManager configManager;
  /**
   * Preview frames are delivered here, which we pass on to the registered
   * handler. Make sure to clear the handler so it will only receive one
   * message.
   */
  private final PreviewCallback previewCallback;
  private Camera camera;
  private AutoFocusManager autoFocusManager;
  private boolean initialized;
  private boolean previewing;
  private int requestedCameraId = -1;

  public CameraManager(final Context context) {
    this.configManager = new CameraConfigurationManager(context);
    previewCallback = new PreviewCallback(configManager);
  }

  public final synchronized void openDriver(final SurfaceHolder holder) throws IOException {
    Camera theCamera = camera;
    if (theCamera == null) {
      if (requestedCameraId >= 0) {
        theCamera = OpenCameraInterface.open(requestedCameraId);
      } else {
        theCamera = OpenCameraInterface.open();
      }
      if (theCamera == null) {
        throw new IOException();
      }
      camera = theCamera;
    }
    theCamera.setPreviewDisplay(holder);
    if (!initialized) {
      initialized = true;
      configManager.initFromCameraParameters(theCamera);
    }
    Camera.Parameters parameters = theCamera.getParameters();
    String parametersFlattened = parameters == null ? null : parameters.flatten();
    try {
      configManager.setDesiredCameraParameters(theCamera);
    } catch (RuntimeException re) {
      if (parametersFlattened != null) {
        parameters = theCamera.getParameters();
        parameters.unflatten(parametersFlattened);
        try {
          theCamera.setParameters(parameters);
          configManager.setDesiredCameraParameters(theCamera);
        } catch (RuntimeException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public final synchronized boolean isOpen() {
    return camera != null;
  }

  public final synchronized void closeDriver() {
    if (camera != null) {
      camera.release();
      camera = null;
    }
  }

  public final synchronized void startPreview() {
    Camera theCamera = camera;
    if (theCamera != null && !previewing) {
      theCamera.startPreview();
      previewing = true;
      autoFocusManager = new AutoFocusManager(camera);
    }
  }

  public final synchronized void stopPreview() {
    if (autoFocusManager != null) {
      autoFocusManager.stop();
      autoFocusManager = null;
    }
    if (camera != null && previewing) {
      camera.stopPreview();
      previewCallback.setHandler(null, 0);
      previewing = false;
    }
  }

  public final synchronized void requestPreviewFrame(final Handler handler, final int message) {
    Camera theCamera = camera;
    if (theCamera != null && previewing) {
      previewCallback.setHandler(handler, message);
      theCamera.setOneShotPreviewCallback(previewCallback);
    }
  }

  /**
   * 获取相机分辨率.
   */
  public Point getCameraResolution() {
    return configManager.getCameraResolution();
  }

  public final synchronized Camera.Size getPreviewSize() {
    if (null != camera) {
      return camera.getParameters().getPreviewSize();
    }
    return null;
  }
}
