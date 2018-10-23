package com.zuo.wanandroidjava.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zuo.wanandroidjava.BaseApp;
import com.zuo.wanandroidjava.R;
import com.zuo.wanandroidjava.da.component.DaggerFragmentComponent;
import com.zuo.wanandroidjava.da.component.FragmentComponent;
import com.zuo.wanandroidjava.da.module.FragmentModule;
import com.zuo.wanandroidjava.weight.MultiStateView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.yokeyword.fragmentation.SupportFragment;

public abstract class BaseFragment<P extends BasePresenter> extends SupportFragment implements IBase, BaseView {
    @Inject
    protected P presenter;
    private boolean initFinish;
    private View rootView;
    @BindView(R.id.multistateview)
    MultiStateView multistateview;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(getLayoutId(), container, false);
        return rootView;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initFinish = true;
        init();

    }

    protected FragmentComponent getComponent() {
        return DaggerFragmentComponent.builder()
                .appComponent(BaseApp.getAppComponent())
                .fragmentModule(new FragmentModule())
                .build();

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initInject();
        if (presenter != null) {
            getLifecycle().addObserver(presenter);
            presenter.attachView(this);
        }
        initMultiStateView();
    }

    private void initMultiStateView() {
        if (multistateview == null) {
            return;
        }
        multistateview.setEmptyResource(R.layout.item_empty)
                .setLoadingResource(R.layout.item_loading)
                .setFailResource(R.layout.item_fail)
                .build();

    }

    public boolean isInitFinish() {
        return initFinish;
    }
    protected int getStatusBarHeight() {
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        return getResources().getDimensionPixelSize(resourceId);

    }
    @Override
    public void showLoadingView() {
        if (multistateview!=null) {
            multistateview.showLoadingView();
        }
    }

    @Override
    public void showEmptyView() {
        if (multistateview!=null) {
            multistateview.showEmptyView();
        }
    }

    @Override
    public void showErrorView() {
        if (multistateview!=null) {
            multistateview.showErrorView();
        }
    }

    @Override
    public void showContentView() {
        if (multistateview!=null) {
            multistateview.showContent();
        }
    }
}
