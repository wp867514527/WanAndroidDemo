package com.zuo.wanandroidjava.di.component;

import com.zuo.wanandroidjava.di.module.ActModule;
import com.zuo.wanandroidjava.di.scope.ActivityScope;
import com.zuo.wanandroidjava.ui.act.SearchActivity;

import javax.inject.Inject;

import dagger.Component;

@ActivityScope
@Component(dependencies = AppComponent.class, modules = ActModule.class)
public interface ActivityComponent {
   /* @Inject*/
    void inject(SearchActivity searchActivity);
}
