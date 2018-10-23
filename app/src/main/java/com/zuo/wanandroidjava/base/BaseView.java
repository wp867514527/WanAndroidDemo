package com.zuo.wanandroidjava.base;

import android.arch.lifecycle.LifecycleOwner;

import com.zuo.wanandroidjava.bean.ProjectList;

public interface BaseView extends LifecycleOwner{

    void showLoadingView();

    void showEmptyView();

    void showErrorView();

    void showContentView();


    //void setDatas(ProjectList.DataBean data, boolean b);
}
