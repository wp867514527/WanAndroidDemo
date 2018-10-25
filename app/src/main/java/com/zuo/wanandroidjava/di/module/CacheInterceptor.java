package com.zuo.wanandroidjava.di.module;

import android.os.SystemClock;
import android.util.Log;


import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author Aloe.Zheng
 * @time 2018/5/7 19:38
 * @email aloe200@163.com
 */
public class CacheInterceptor implements Interceptor {
  protected static String TAG = "cache";
  /**
   * 无网络时，模拟网络请求.
   */
  private static final long NO_NET_TIME = 1000L;
  /**
   * 缓存头部.
   */
  private static final String CACHE_HEAD = "Cache-Control";
  @Override
  public final Response intercept(final Chain chain) throws IOException {
    Request request = chain.request();
    if (isNetWorkAvailable()) {
      String cacheControl = request.cacheControl().toString();
      //request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
      Log.d(TAG, "cacheControl==" + cacheControl);
      return chain.proceed(request).newBuilder().removeHeader("pragma")  // pragama  Cache-Control之前的设置无缓存头部
          .removeHeader(CACHE_HEAD)
          .header(CACHE_HEAD, "public, max-age=0")  // max-age <=0 时 请求浏览器 ，否则请求缓存
          .build();
    } else {
      SystemClock.sleep(NO_NET_TIME);
      request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
      return chain.proceed(request).newBuilder()
          .header(CACHE_HEAD, "public, only-if-cached")  //只获取缓存没有 缓存 返回 504
          .removeHeader("Pragma").build();
    }
  }


  public static boolean isNetWorkAvailable() {
    try {
      Process process = Runtime.getRuntime().exec("/system/bin/ping -c 1 www.baidu.com");
      return process.waitFor() == 0;
    } catch (Exception e) {

    }
    return false;
  }
}
