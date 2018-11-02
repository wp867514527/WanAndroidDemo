package com.zuo.wanandroidjava.presenter;

import com.zuo.wanandroidjava.base.BasePresenter;
import com.zuo.wanandroidjava.bean.User;
import com.zuo.wanandroidjava.model.api.BaseObserver;
import com.zuo.wanandroidjava.presenter.contract.Login_RegistContract;

import javax.inject.Inject;

public class Login_RegistPresneter extends BasePresenter<Login_RegistContract.IView> implements Login_RegistContract.IPresenter {
    @Inject
    public Login_RegistPresneter() {

    }

    @Override
    public void login(String userName, String psw) {
        getHttpHelper().ogin(userName, psw)
                .as(getDisposable())
                .subscribe(new BaseObserver<User>() {
                    @Override
                    public void onSuccess(User user) {
                        getView().login("success");
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().login("error");
                    }
                });
    }

    @Override
    public void regist(String userName, String psw, String repassword) {
        getHttpHelper().register(userName, psw, repassword)
                .as(getDisposable())
                .subscribe(new BaseObserver<User>() {
                    @Override
                    public void onSuccess(User user) {
                        getView().register("success");
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().register("error");
                    }
                });
    }
}
