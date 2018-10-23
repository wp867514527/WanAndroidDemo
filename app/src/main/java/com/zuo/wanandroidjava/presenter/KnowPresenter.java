package com.zuo.wanandroidjava.presenter;

import com.zuo.wanandroidjava.base.BasePresenter;
import com.zuo.wanandroidjava.bean.Tree;
import com.zuo.wanandroidjava.presenter.contract.KnowContract;
import com.zuo.wanandroidjava.model.api.BaseHttpObserver;

import javax.inject.Inject;

public class KnowPresenter extends BasePresenter<KnowContract.IView> implements KnowContract.IPresenter{
    @Inject
    public KnowPresenter() {

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void loadMore() {

    }

    @Override
    public void getDatas() {
        getHttpHelper().getTree()

                .as(getDisposable())
                .subscribe(new BaseHttpObserver<Tree>() {
                    @Override
                    public void onSuccess(Tree tree) {
                        if (tree != null && tree.getErrorCode()==0) {
                            getView().setDatas(tree,true);
                        }
                        getView().onUpdate(true,true);
                    }

                    @Override
                    protected void onFail(Throwable e) {
                        getView().onUpdate(true,false);
                    }
                });
    }
}
