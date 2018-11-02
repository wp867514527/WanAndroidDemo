package com.zuo.wanandroidjava.di.component;

import com.zuo.wanandroidjava.di.module.ActModule;
import com.zuo.wanandroidjava.di.scope.ActivityScope;
import com.zuo.wanandroidjava.ui.act.Login_RegistActivity;
import com.zuo.wanandroidjava.ui.act.SearchActivity;
import com.zuo.wanandroidjava.ui.act.ShoucangActivity;
import dagger.Component;

@ActivityScope
@Component(dependencies = AppComponent.class, modules = ActModule.class)
public interface ActivityComponent {
//    @Inject
    void inject(SearchActivity searchActivity);

    void inject(Login_RegistActivity login_registActivity);

    void inject(ShoucangActivity shoucangActivity);

}
