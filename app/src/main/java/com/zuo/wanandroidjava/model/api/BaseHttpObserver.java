package com.zuo.wanandroidjava.model.api;

import android.util.Log;
import com.zuo.wanandroidjava.base.BaseView;

import io.reactivex.disposables.Disposable;

/**
 *
 * time   : 2018/4/25 10:50
 * email  : aloe200@163.com
 * desc: Observer
 * @author Aloe.Zheng
 * @param <T> 泛型
 */
public abstract class BaseHttpObserver<T> extends BaseObserver<T> {
  /**
   * 是否显示正在加载.
   */
  private boolean showLoading;
  /**
   * 当前View视图.
   */
  private  BaseView baseView;
  private String TAG = "BaseHttpObserver";
  public BaseHttpObserver(final BaseView baseView) {
    this.baseView = baseView;
  }

  public BaseHttpObserver() {

  }

  public BaseHttpObserver(final BaseView baseView, final boolean showLoading) {
    this.baseView = baseView;
    this.showLoading = showLoading;
  }

  @Override
  public final void onSubscribe(final Disposable d) {
    Log.d(TAG, "onSubscribe");
    if (baseView != null && showLoading) {

    }
  }

  @Override
  public final void onError(final Throwable e) {
    Log.d(TAG, "onError");
    onFail(e);
    if (baseView == null) {
      return;
    }

  }

  @Override
  public final void onComplete() {
    Log.d(TAG, "onComplete()");
    if (baseView != null) {

    }
  }

  /**
   * 请求失败回调方法.
   * @param e 失败原因
   */
  protected abstract void onFail(Throwable e);
}
