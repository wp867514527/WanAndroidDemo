package com.zuo.wanandroidjava.base;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.zuo.wanandroidjava.R;

import butterknife.ButterKnife;
import me.yokeyword.fragmentation.SupportActivity;

public abstract class BaseActivity <P extends BasePresenter> extends SupportActivity implements IBase,BaseView {
    protected P presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
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
}
