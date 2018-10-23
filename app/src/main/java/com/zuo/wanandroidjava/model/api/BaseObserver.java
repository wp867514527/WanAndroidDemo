package com.zuo.wanandroidjava.model.api;

import io.reactivex.MaybeObserver;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

/**
 * @param <T>
 * @author Aloe.Zheng
 * @time 2018/5/13 0:46
 * @email aloe200@163.com
 * desc: 默认Observer
 */
public abstract class BaseObserver<T> implements SingleObserver<T>, MaybeObserver<T>,
        Observer<T> {
  @Override
  public void onComplete() {

  }

  @Override
  public void onSubscribe(final Disposable d) {

  }

  @Override
  public void onSuccess(final T t) {
  }

  @Override
  public final void onNext(final T t) {
    onSuccess(t);
  }

  @Override
  public void onError(final Throwable e) {

  }
}
