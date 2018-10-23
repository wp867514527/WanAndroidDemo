package com.zuo.wanandroidjava.da.component;

import com.zuo.wanandroidjava.da.module.FragmentModule;
import com.zuo.wanandroidjava.da.scope.FragmentScope;
import com.zuo.wanandroidjava.ui.fragment.HomeFragment;
import com.zuo.wanandroidjava.ui.fragment.KnowFragment;
import com.zuo.wanandroidjava.ui.fragment.ProjectFragment;
import com.zuo.wanandroidjava.ui.fragment.ProjectListFragment;
import com.zuo.wanandroidjava.ui.fragment.TreeListFragment;

import javax.inject.Inject;
import javax.inject.Scope;

import dagger.Component;
@FragmentScope
@Component(dependencies = AppComponent.class,modules = FragmentModule.class)
public interface FragmentComponent {
    void inject(HomeFragment homeFragment);

    void inject(KnowFragment knowFragment);

    void inject(ProjectFragment projectFragment);

    void inject(ProjectListFragment projectListFragment);

    void inject(TreeListFragment treeListFragment);
}
