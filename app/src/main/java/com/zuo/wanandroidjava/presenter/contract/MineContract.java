package com.zuo.wanandroidjava.presenter.contract;

import com.zuo.wanandroidjava.base.BaseView;

public interface MineContract {

    interface IPresenter {
        void logOut();
    }

    interface IView extends BaseView {
        void logoutCallBack(String isSucess);
    }
}
