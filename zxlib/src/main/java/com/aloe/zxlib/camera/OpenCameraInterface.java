package com.aloe.zxlib.camera;

import android.hardware.Camera;

/**
 * time   : 2018/5/10 16:30
 * email  : aloe200@163.com
 * @author Aloe.Zheng
 */
public final class OpenCameraInterface {
  private OpenCameraInterface() {
  }

  public static Camera open(final int cameraId) {
    int numCameras = Camera.getNumberOfCameras();
    if (numCameras == 0) {
      return null;
    }
    boolean explicitRequest = cameraId >= 0;
    int id = cameraId;
    if (!explicitRequest) {
      int index = 0;
      while (index < numCameras) {
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(index, cameraInfo);
        if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
          break;
        }
        index++;
      }
      id = index;
    }
    Camera camera;
    if (id < numCameras) {
      camera = Camera.open(id);
    } else {
      if (explicitRequest) {
        camera = null;
      } else {
        camera = Camera.open(0);
      }
    }
    return camera;
  }

  public static Camera open() {
    return open(-1);
  }
}
