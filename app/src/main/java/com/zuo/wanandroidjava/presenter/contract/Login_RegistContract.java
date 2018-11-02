package com.zuo.wanandroidjava.presenter.contract;

import com.zuo.wanandroidjava.base.BaseView;

public interface Login_RegistContract {

    interface IView extends BaseView {
        void register(String isSucess);

        void login(String isSucess);
    }

    interface IPresenter {
        void login(String userName, String psw);

        void regist(String userName, String psw, String repassword);
    }

}
