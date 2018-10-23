package com.zuo.wanandroidjava.da.component;

import com.zuo.wanandroidjava.da.module.AppModule;
import com.zuo.wanandroidjava.da.module.HttpModule;
import com.zuo.wanandroidjava.model.api.HttpHelper;

import javax.inject.Singleton;

import dagger.Component;
@Singleton
@Component(modules = {AppModule.class,HttpModule.class})
public interface AppComponent {
    HttpHelper getHttpHelper();
}
