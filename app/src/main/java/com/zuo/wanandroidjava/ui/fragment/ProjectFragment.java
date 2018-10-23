package com.zuo.wanandroidjava.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyco.tablayout.SlidingTabLayout;
import com.zuo.wanandroidjava.R;
import com.zuo.wanandroidjava.base.BaseFragment;
import com.zuo.wanandroidjava.bean.ProjectTab;
import com.zuo.wanandroidjava.presenter.ProjectPresenter;
import com.zuo.wanandroidjava.presenter.contract.ProjectContract;
import com.zuo.wanandroidjava.weight.SlideViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.annotations.NonNull;

//项目先不做吧
public class ProjectFragment extends BaseFragment<ProjectPresenter> implements ProjectContract.IView {
    @BindView(R.id.slide_view_pager)
    SlidingTabLayout slideViewPager;
    @BindView(R.id.viewpager)
    SlideViewPager viewpager;
    Unbinder unbinder;
    @BindView(R.id.v_top_0)
    View vTop0;
    @BindView(R.id.appbarlayout)
    public AppBarLayout appbarlayout;
    Unbinder unbinder1;
    private List<String> mTitle;
    private List<Integer> ids;
    private String TAG = "ProjectFragment";

    @Override
    public int getLayoutId() {
        return R.layout.fragment_project;
    }

    @Override
    public void initInject() {
        getComponent().inject(this);
    }

    @Override
    public void init() {
        fitsStatusBarView(vTop0);
        appbarlayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                Log.d(TAG, "verticalOffset==" + verticalOffset);
                if (verticalOffset == 0) {

                } else {

                }

            }
        });

        presenter.getData();

    }
    public void fitsStatusBarView(@NonNull View... view) {
        int statusBarHeight = getStatusBarHeight();
        for (int i = 0; i < view.length; i++) {
            ViewGroup.LayoutParams layoutParams = view[i].getLayoutParams();
            layoutParams.height = statusBarHeight;
            view[i].setLayoutParams(layoutParams);
        }
    }

    @Override
    public void setDatas(List<ProjectTab.DataBean> data) {
        mTitle = new ArrayList<>();
        ids = new ArrayList<>();
        for (ProjectTab.DataBean datum : data) {
            mTitle.add(datum.getName());
            ids.add(datum.getId());
        }
        String[] titles = mTitle.toArray(new String[mTitle.size()]);
        ProjectAdapter adapter = new ProjectAdapter(getChildFragmentManager());
        viewpager.setAdapter(adapter);
        slideViewPager.setViewPager(viewpager, titles);

    }


    class ProjectAdapter extends FragmentPagerAdapter {


        public ProjectAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return ProjectListFragment.instance(ids.get(i));
        }

        @Override
        public int getCount() {
            return mTitle.size();
        }
    }


}
