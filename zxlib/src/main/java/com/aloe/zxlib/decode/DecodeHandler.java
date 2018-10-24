package com.aloe.zxlib.decode;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.aloe.zxlib.R;
import com.aloe.zxlib.activity.CaptureActivity;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.io.ByteArrayOutputStream;
import java.util.Map;

/**
 * @author Aloe.Zheng
 * @time 2018/5/10 16:25
 * @email aloe200@163.com
 */
public class DecodeHandler extends Handler {
  private final CaptureActivity activity;
  private final MultiFormatReader multiFormatReader;
  private boolean running = true;

  DecodeHandler(final CaptureActivity activity, final Map<DecodeHintType, Object> hints) {
    multiFormatReader = new MultiFormatReader();
    multiFormatReader.setHints(hints);
    this.activity = activity;
  }

  private static void bundleThumbnail(final PlanarYUVLuminanceSource source, final Bundle bundle) {
    int[] pixels = source.renderThumbnail();
    int width = source.getThumbnailWidth();
    int height = source.getThumbnailHeight();
    Bitmap bitmap = Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.ARGB_8888);
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
    bundle.putByteArray(DecodeThread.BARCODE_BITMAP, out.toByteArray());
  }

  @Override
  public final void handleMessage(final Message message) {
    if (!running) {
      return;
    }
    if (message.what == R.id.decode) {
      decode((byte[]) message.obj);
    } else if (message.what == R.id.quit) {
      running = false;
      Looper looper = Looper.myLooper();
      if (looper != null) {
        looper.quit();
      }
    }
  }

  private void decode(final byte[] data) {
    Camera.Size size = activity.getCameraManager().getPreviewSize();
    // 这里需要将获取的data翻转一下，因为相机默认拿的的横屏的数据
    byte[] rotatedData = new byte[data.length];
    for (int y = 0; y < size.height; y++) {
      for (int x = 0; x < size.width; x++) {
        rotatedData[x * size.height + size.height - y - 1] = data[x + y * size.width];
      }
    }
    // 宽高也要调整
    int tmp = size.width;
    size.width = size.height;
    size.height = tmp;
    Result rawResult = null;
    PlanarYUVLuminanceSource source = buildLuminanceSource(rotatedData, size.width, size.height);
    if (source != null) {
      BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
      try {
        rawResult = multiFormatReader.decodeWithState(bitmap);
      } catch (ReaderException e) {
        e.printStackTrace();
      } finally {
        multiFormatReader.reset();
      }
    }
    Handler handler = activity.getHandler();
    if (rawResult != null) {
      if (handler != null) {
        Message message = Message.obtain(handler, R.id.decode_succeeded, rawResult);
        Bundle bundle = new Bundle();
        bundleThumbnail(source, bundle);
        message.setData(bundle);
        message.sendToTarget();
      }
    } else {
      if (handler != null) {
        Message message = Message.obtain(handler, R.id.decode_failed);
        message.sendToTarget();
      }
    }
  }

  private PlanarYUVLuminanceSource buildLuminanceSource(final byte[] data, final int width, final int height) {
    Rect rect = activity.getCropRect();
    if (rect == null) {
      return null;
    }
    return new PlanarYUVLuminanceSource(data, width, height, rect.left, rect.top, rect.width(), rect
        .height(), false);
  }
}
