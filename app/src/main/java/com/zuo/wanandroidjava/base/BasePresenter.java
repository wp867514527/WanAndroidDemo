package com.zuo.wanandroidjava.base;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;

import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.AutoDisposeConverter;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;
import com.zuo.wanandroidjava.BaseApp;
import com.zuo.wanandroidjava.model.api.HttpHelper;

import javax.inject.Singleton;

public class BasePresenter<V extends BaseView> implements LifecycleObserver {
    private V view;
    private HttpHelper httpHelper;
    protected String TAG = "Presenter";
    public void attachView(final V v){
        view = v;
        httpHelper = BaseApp.getAppComponent().getHttpHelper();
    }



    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    protected void onStart() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    protected void onResume() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    protected void onPause() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    protected void onStop() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    protected void onDestroy() {

    }

    public V getView() {
        return view;
    }

    public HttpHelper getHttpHelper() {
        return httpHelper;
    }


    /**
     * 获取RxJava生命周期管理类对象.
     *
     * @param <T> RxJava生命周期管理类
     * @return RxJava生命周期管理类对象
     */
    protected final <T> AutoDisposeConverter<T> getDisposable() {
        return AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(view));
    }

}
