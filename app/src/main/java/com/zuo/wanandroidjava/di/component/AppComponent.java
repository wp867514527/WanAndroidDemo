package com.zuo.wanandroidjava.di.component;

import com.zuo.wanandroidjava.di.module.AppModule;
import com.zuo.wanandroidjava.di.module.HttpModule;
import com.zuo.wanandroidjava.model.api.HttpHelper;

import javax.inject.Singleton;

import dagger.Component;
@Singleton
@Component(modules = {AppModule.class,HttpModule.class})
public interface AppComponent {
    HttpHelper getHttpHelper();
}
