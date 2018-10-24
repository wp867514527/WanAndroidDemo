package com.aloe.zxlib.decode;

import android.os.Handler;
import android.os.Looper;

import com.aloe.zxlib.activity.CaptureActivity;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * @author Aloe.Zheng
 * @time 2018/5/10 16:25
 * @email aloe200@163.com
 */
public class DecodeThread extends Thread {
  public static final String BARCODE_BITMAP = "barcode_bitmap";
  public static final int ALL_MODE = 0X300;
  private static final int BARCODE_MODE = 0X100;
  private static final int QRCODE_MODE = 0X200;
  private final CaptureActivity activity;
  private final Map<DecodeHintType, Object> hints;
  private final CountDownLatch handlerInitLatch;
  private Handler handler;

  public DecodeThread(final CaptureActivity activity, final int decodeMode) {

    this.activity = activity;
    handlerInitLatch = new CountDownLatch(1);

    hints = new EnumMap<>(DecodeHintType.class);
    Collection<BarcodeFormat> decodeFormats = new ArrayList<>();
    decodeFormats.addAll(EnumSet.of(BarcodeFormat.AZTEC));
    decodeFormats.addAll(EnumSet.of(BarcodeFormat.PDF_417));
    switch (decodeMode) {
      case BARCODE_MODE:
        decodeFormats.addAll(DecodeFormatManager.getBarCodeFormats());
        break;
      case QRCODE_MODE:
        decodeFormats.addAll(DecodeFormatManager.getQrCodeFormats());
        break;
      case ALL_MODE:
        decodeFormats.addAll(DecodeFormatManager.getBarCodeFormats());
        decodeFormats.addAll(DecodeFormatManager.getQrCodeFormats());
        break;
      default:
        break;
    }
    hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);
  }

  public final Handler getHandler() {
    try {
      handlerInitLatch.await();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return handler;
  }

  @Override
  public final void run() {
    Looper.prepare();
    handler = new DecodeHandler(activity, hints);
    handlerInitLatch.countDown();
    Looper.loop();
  }
}
