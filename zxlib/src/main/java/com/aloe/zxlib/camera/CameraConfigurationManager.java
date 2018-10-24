package com.aloe.zxlib.camera;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera;
import android.view.Display;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author Aloe.Zheng
 * @time 2018/5/10 16:29
 * @email aloe200@163.com
 */
public class CameraConfigurationManager {
  private static final int MIN_PREVIEW_PIXELS = 480 * 320;
  private static final double MAX_ASPECT_DISTORTION = 0.15;
  private final Context context;
  /**
   * 相机分辨率.
   */
  private Point cameraResolution;

  public CameraConfigurationManager(final Context context) {
    this.context = context;
  }

  public final void initFromCameraParameters(final Camera camera) {
    Camera.Parameters parameters = camera.getParameters();
    WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    if (manager == null) {
      return;
    }
    Point screenResolution = getDisplaySize(manager.getDefaultDisplay());
    //因为换成了竖屏显示，所以不替换屏幕宽高得出的预览图是变形的
    Point screenResolutionForCamera = new Point();
    screenResolutionForCamera.x = screenResolution.x;
    screenResolutionForCamera.y = screenResolution.y;
    if (screenResolution.x < screenResolution.y) {
      screenResolutionForCamera.x = screenResolution.y;
      screenResolutionForCamera.y = screenResolution.x;
    }
    cameraResolution = findBestPreviewSizeValue(parameters, screenResolutionForCamera);
  }

  @SuppressWarnings("deprecation")
  @SuppressLint("NewApi")
  private Point getDisplaySize(final Display display) {
    final Point point = new Point();
    try {
      display.getSize(point);
    } catch (NoSuchMethodError ignore) {
      point.x = display.getWidth();
      point.y = display.getHeight();
    }
    return point;
  }

  public final void setDesiredCameraParameters(final Camera camera) {
    Camera.Parameters parameters = camera.getParameters();
    if (parameters == null) {
      return;
    }
    parameters.setPreviewSize(cameraResolution.x, cameraResolution.y);
    camera.setParameters(parameters);

    Camera.Parameters afterParameters = camera.getParameters();
    Camera.Size afterSize = afterParameters.getPreviewSize();
    if (afterSize != null && (cameraResolution.x != afterSize.width || cameraResolution.y != afterSize
        .height)) {
      cameraResolution.x = afterSize.width;
      cameraResolution.y = afterSize.height;
    }
    //设置相机预览为竖屏
    camera.setDisplayOrientation(90);
  }

  public final Point getCameraResolution() {
    return cameraResolution;
  }

  /**
   * 从相机支持的分辨率中计算出最适合的预览界面尺寸.
   */
  private Point findBestPreviewSizeValue(final Camera.Parameters parameters, final Point screenResolution) {
    List<Camera.Size> rawSupportedSizes = parameters.getSupportedPreviewSizes();
    if (rawSupportedSizes == null) {
      Camera.Size defaultSize = parameters.getPreviewSize();
      return new Point(defaultSize.width, defaultSize.height);
    }
    List<Camera.Size> supportedPreviewSizes = new ArrayList<>(rawSupportedSizes);
    Collections.sort(supportedPreviewSizes, (a, b) -> {
      int aPixels = a.height * a.width;
      int bPixels = b.height * b.width;
      return Integer.compare(bPixels, aPixels);
    });
    double screenAspectRatio = (double) screenResolution.x / (double) screenResolution.y;
    Iterator<Camera.Size> it = supportedPreviewSizes.iterator();
    while (it.hasNext()) {
      Camera.Size supportedPreviewSize = it.next();
      int realWidth = supportedPreviewSize.width;
      int realHeight = supportedPreviewSize.height;
      if (realWidth * realHeight < MIN_PREVIEW_PIXELS) {
        it.remove();
        continue;
      }
      boolean isCandidatePortrait = realWidth < realHeight;
      int maybeFlippedWidth = isCandidatePortrait ? realHeight : realWidth;
      int maybeFlippedHeight = isCandidatePortrait ? realWidth : realHeight;
      double aspectRatio = (double) maybeFlippedWidth / (double) maybeFlippedHeight;
      double distortion = Math.abs(aspectRatio - screenAspectRatio);
      if (distortion > MAX_ASPECT_DISTORTION) {
        it.remove();
        continue;
      }
      if (maybeFlippedWidth == screenResolution.x && maybeFlippedHeight == screenResolution.y) {
        return new Point(realWidth, realHeight);
      }
    }
    if (!supportedPreviewSizes.isEmpty()) {
      Camera.Size largestPreview = supportedPreviewSizes.get(0);
      return new Point(largestPreview.width, largestPreview.height);
    }
    Camera.Size defaultPreview = parameters.getPreviewSize();
    return new Point(defaultPreview.width, defaultPreview.height);
  }
}
