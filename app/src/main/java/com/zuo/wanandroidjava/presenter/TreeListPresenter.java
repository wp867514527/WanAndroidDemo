package com.zuo.wanandroidjava.presenter;

import com.zuo.wanandroidjava.base.BasePresenter;
import com.zuo.wanandroidjava.bean.TreeList;
import com.zuo.wanandroidjava.model.api.BaseHttpObserver;
import com.zuo.wanandroidjava.presenter.contract.TreeListContract;

import javax.inject.Inject;

public class TreeListPresenter extends BasePresenter<TreeListContract.IView> implements TreeListContract.IPresenter {
    int page = 1;
    @Inject
    public TreeListPresenter() {

    }
    @Override
    public void getDatas(int cid) {
        getHttpHelper().getTreeList(page,cid)
                .as(getDisposable())
                .subscribe(new BaseHttpObserver<TreeList>() {
                    @Override
                    public void onSuccess(TreeList treeList) {
                        getView().showContentView();
                        if (page==1) {
                            getView().setDatas(treeList.getData(), true);
                        }else{
                            getView().setDatas(treeList.getData(), false);
                        }
                        getView().onUpdate(page==1,true);
                    }

                    @Override
                    protected void onFail(Throwable e) {
                        getView().onUpdate(page == 1, false);
                        getView().showErrorView();

                    }
                });
    }


}
