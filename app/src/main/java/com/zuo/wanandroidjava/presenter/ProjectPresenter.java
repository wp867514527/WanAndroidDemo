package com.zuo.wanandroidjava.presenter;

import com.zuo.wanandroidjava.base.BasePresenter;
import com.zuo.wanandroidjava.bean.ProjectTab;
import com.zuo.wanandroidjava.presenter.contract.ProjectContract;
import com.zuo.wanandroidjava.model.api.BaseHttpObserver;

import javax.inject.Inject;

public class ProjectPresenter extends BasePresenter<ProjectContract.IView> implements ProjectContract.IPresenter {
    @Inject
    public ProjectPresenter() {

    }


    @Override
    public void getData() {
        getHttpHelper().getProjectTab()
                .as(getDisposable())
                .subscribe(new BaseHttpObserver<ProjectTab>() {
                    @Override
                    public void onSuccess(ProjectTab projectTab) {
                        if (projectTab!=null &&projectTab.getData()!=null ) {
                            getView().setDatas(projectTab.getData());
                        }
                        getView().showContentView();
                    }

                    @Override
                    protected void onFail(Throwable e) {
                        getView().showErrorView();
                    }
                });
    }
}
