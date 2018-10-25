package com.zuo.wanandroidjava.base;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.zuo.wanandroidjava.BaseApp;
import com.zuo.wanandroidjava.R;
import com.zuo.wanandroidjava.di.component.ActivityComponent;
import com.zuo.wanandroidjava.di.component.DaggerActivityComponent;
import com.zuo.wanandroidjava.di.module.ActModule;
import com.zuo.wanandroidjava.util.ToastUtils;

import javax.inject.Inject;

import butterknife.ButterKnife;
import me.yokeyword.fragmentation.SupportActivity;

public abstract class BaseActivity<P extends BasePresenter> extends SupportActivity implements IBase, BaseView {
    @Inject
    protected P presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ButterKnife.bind(this);
        getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat
                .getColor(this, R.color.act_bg)));
        initInject();
        if (presenter != null) {
            getLifecycle().addObserver(presenter);
            presenter.attachView(this);
        }
        init();
    }

    public static ActivityComponent getComponent() {
        return DaggerActivityComponent.builder()
                .appComponent(BaseApp.getAppComponent())
                .actModule(new ActModule())
                .build();
    }

    @Override
    public void showLoadingView() {

    }

    @Override
    public void showEmptyView() {

    }

    @Override
    public void showErrorView() {

    }

    @Override
    public void showContentView() {

    }

    long time = 0;

    @Override
    public void onBackPressedSupport() {
        long millis = System.currentTimeMillis();
        if (millis - time > 2000) {
            time = millis;
            ToastUtils.showCenterToast("长得丑的需要点俩次");
            return;
        }
        super.onBackPressedSupport();
    }

}
