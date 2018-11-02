package com.zuo.wanandroidjava;

import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;

import com.google.common.collect.ImmutableList;
import com.jaeger.library.StatusBarUtil;
import com.zuo.wanandroidjava.base.BaseActivity;
import com.zuo.wanandroidjava.base.BaseFragment;
import com.zuo.wanandroidjava.ui.fragment.HomeFragment;
import com.zuo.wanandroidjava.ui.fragment.KnowFragment;
import com.zuo.wanandroidjava.ui.fragment.MineFragment;
import com.zuo.wanandroidjava.ui.fragment.ProjectFragment;
import com.zuo.wanandroidjava.weight.SlideViewPager;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.slide_view_pager)
    SlideViewPager slideViewPager;
    @BindView(R.id.bottom_navigation_view)
    BottomNavigationView bottomNavigationView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initInject() {

    }

    @Override
    public void init() {
        StatusBarUtil.setTransparentForImageViewInFragment(this,null);
        //不可变集合,当成数组即可
        ImmutableList<BaseFragment> fragments = ImmutableList.of(
                new HomeFragment(),
                new KnowFragment(),
                new ProjectFragment(),
                new MineFragment()
        );
        slideViewPager.setAdapter(new StatePageAdapter(getSupportFragmentManager(), fragments));
        ImmutableList<Integer> itemIds = ImmutableList.of(R.id.tab_main_home, R.id.tab_main_know, R.id.tab_main_project, R.id.tab_main_mine);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            slideViewPager.setCurrentItem(itemIds.indexOf(item.getItemId()));
            return true;
        });
        slideViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (i == 0) {
                    HomeFragment fragment = (HomeFragment) fragments.get(0);
                    StatusBarUtil.setTransparentForImageViewInFragment(MainActivity.this,null);
                    if (fragment.isLight) {
                        StatusBarUtil.setLightMode(MainActivity.this);
                    }
                }else{
                    StatusBarUtil.setColor(MainActivity.this, getResources().getColor(R.color.colorPrimary));
                    StatusBarUtil.setDarkMode(MainActivity.this);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }

}
