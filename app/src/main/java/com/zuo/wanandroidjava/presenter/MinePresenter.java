package com.zuo.wanandroidjava.presenter;

import com.zuo.wanandroidjava.base.BasePresenter;
import com.zuo.wanandroidjava.bean.User;
import com.zuo.wanandroidjava.model.api.BaseObserver;
import com.zuo.wanandroidjava.presenter.contract.MineContract;

import javax.inject.Inject;

public class MinePresenter extends BasePresenter<MineContract.IView> implements MineContract.IPresenter {
    private int page = 0;

    @Inject
    public MinePresenter() {

    }

    @Override
    public void logOut() {
        getHttpHelper().logout()
                .as(getDisposable())
                .subscribe(new BaseObserver<User>() {
                    @Override
                    public void onSuccess(User user) {
                        getView().logoutCallBack("success");
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().logoutCallBack("error");
                    }
                });
    }
}
