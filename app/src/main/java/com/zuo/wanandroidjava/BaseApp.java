package com.zuo.wanandroidjava;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
import com.zuo.wanandroidjava.da.component.AppComponent;
import com.zuo.wanandroidjava.da.component.DaggerAppComponent;
import com.zuo.wanandroidjava.da.module.HttpModule;

public class BaseApp extends Application {
    public static BaseApp instance;
    @Override
    public void onCreate() {
        super.onCreate();
        setInstance(this);
        ARouter.openLog();
        ARouter.openLog();
        ARouter.init(this);

    }

    public static AppComponent getAppComponent() {
        return DaggerAppComponent.builder().
                httpModule(new HttpModule(getInstance())).
                build();
    }

    public static void setInstance(BaseApp instance) {
        BaseApp.instance = instance;
    }

    public static BaseApp getInstance() {
        return instance;
    }
}
