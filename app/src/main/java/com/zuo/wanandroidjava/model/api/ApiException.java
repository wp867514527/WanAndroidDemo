package com.zuo.wanandroidjava.model.api;

/**
 * time   : 2018/4/24 10:25
 * email  : aloe200@163.com
 *
 * @author Aloe.Zheng
 */
public class ApiException extends Exception {
  /**
   * 网络请求状态码.
   */
  private final String code;

  public ApiException(final String code) {
    super("http error code: " + code);
    this.code = code;
  }

  public ApiException(final String code, final String message) {
    super(message);
    this.code = code;
  }

  public final String getCode() {
    return code;
  }
}
