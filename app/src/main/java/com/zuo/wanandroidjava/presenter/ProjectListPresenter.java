package com.zuo.wanandroidjava.presenter;

import com.zuo.wanandroidjava.base.BasePresenter;
import com.zuo.wanandroidjava.bean.ProjectList;
import com.zuo.wanandroidjava.bean.Tree;
import com.zuo.wanandroidjava.model.api.BaseHttpObserver;
import com.zuo.wanandroidjava.presenter.contract.ProjectListContract;

import javax.inject.Inject;

import retrofit2.http.Path;

public class ProjectListPresenter extends BasePresenter<ProjectListContract.IView> implements ProjectListContract.IPresenter {
    protected int page = 1;

    @Inject
    public ProjectListPresenter() {

    }


    @Override
    public void onRefresh(int id) {
        page = 1;
        getHttpHelper().getProjectList(page, id)
                .as(getDisposable())
                .subscribe(new BaseHttpObserver<ProjectList>() {
                    @Override
                    public void onSuccess(ProjectList projectList) {
                        getView().showContentView();
                        getView().setDatas(projectList.getData(), true);
                        getView().onUpdate(true, true);
                    }

                    @Override
                    protected void onFail(Throwable e) {
                        getView().showErrorView();
                        getView().onUpdate(true, false);
                    }
                });

    }

    @Override
    public void loadMore(int id) {
        page++;
        getHttpHelper().getProjectList(page, id)
                .as(getDisposable())
                .subscribe(new BaseHttpObserver<ProjectList>() {
                    @Override
                    public void onSuccess(ProjectList projectList) {
                        getView().showContentView();
                        getView().setDatas(projectList.getData(), false);
                        getView().onUpdate(false, true);
                    }

                    @Override
                    protected void onFail(Throwable e) {
                        //getView().showErrorView();
                        getView().onUpdate(false, false);
                    }
                });
    }
}
