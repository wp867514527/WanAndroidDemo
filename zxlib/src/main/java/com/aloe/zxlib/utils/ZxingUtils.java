package com.aloe.zxlib.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Aloe.Zheng
 * @time 2018/5/10 16:26
 * @email aloe200@163.com
 * @desc 二维码生成工具类
 */
public final class ZxingUtils {
  /**
   * 二维码宽度.
   */
  public static final String QR_WIDTH = "width";
  /**
   * 二维码高度.
   */
  public static final String QR_HEIGHT = "height";
  /**
   * 二维码数据.
   */
  public static final String QR_RESULT = "result";

  private ZxingUtils() {
  }

  /**
   * 创建带Logo的二维码.
   *
   * @param content   二维码内容
   * @param widthPix  二维码宽度
   * @param heightPix 二维码高度
   * @param logoBm    Logo
   * @return 带Logo的二维码
   */
  public static Bitmap createQRCode(final String content, final int widthPix, final int heightPix,
                                    final Bitmap logoBm) {
    try {
      if (content == null || "".equals(content)) {
        return null;
      }
      // 配置参数
      Map<EncodeHintType, Object> hints = new HashMap<>(2);
      hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
      // 容错级别
      hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
      // 图像数据转换，使用了矩阵转换
      BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, widthPix,
          heightPix, hints);
      int[] pixels = new int[widthPix * heightPix];
      // 下面这里按照二维码的算法，逐个生成二维码的图片，
      // 两个for循环是图片横列扫描的结果
      for (int y = 0; y < heightPix; y++) {
        for (int x = 0; x < widthPix; x++) {
          if (bitMatrix.get(x, y)) {
            pixels[y * widthPix + x] = Color.BLACK;
          } else {
            pixels[y * widthPix + x] = Color.WHITE;
          }
        }
      }
      // 生成二维码图片的格式，使用ARGB_8888
      Bitmap bitmap = Bitmap.createBitmap(widthPix, heightPix, Bitmap.Config.ARGB_8888);
      bitmap.setPixels(pixels, 0, widthPix, 0, 0, widthPix, heightPix);
      if (logoBm != null) {
        bitmap = addLogo(bitmap, logoBm);
      }
      //必须使用compress方法将bitmap保存到文件中再进行读取。直接返回的bitmap是没有任何压缩的，内存消耗巨大！
      return bitmap;
    } catch (WriterException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 在二维码中间添加Logo图案.
   *
   * @param src  二维码
   * @param logo Logo
   * @return 带Logo的二维码
   */
  private static Bitmap addLogo(final Bitmap src, final Bitmap logo) {
    if (src == null || logo == null) {
      return null;
    }
    //获取图片的宽高
    int srcWidth = src.getWidth();
    int srcHeight = src.getHeight();
    int logoWidth = logo.getWidth();
    int logoHeight = logo.getHeight();
    if (srcWidth == 0 || srcHeight == 0) {
      return null;
    }
    if (logoWidth == 0 || logoHeight == 0) {
      return src;
    }
    //logo大小为二维码整体大小的1/5
    float scaleFactor = srcWidth * 1.0f / 5 / logoWidth;
    Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
    try {
      Canvas canvas = new Canvas(bitmap);
      canvas.drawBitmap(src, 0, 0, null);
      canvas.scale(scaleFactor, scaleFactor, srcWidth / 2.0F, srcHeight / 2.0F);
      canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2.0F, (srcHeight - logoHeight) / 2.0F, null);
      canvas.save();
      canvas.restore();
    } catch (Exception e) {
      Log.d("aloe", e.getMessage());
    }
    return bitmap;
  }
}
