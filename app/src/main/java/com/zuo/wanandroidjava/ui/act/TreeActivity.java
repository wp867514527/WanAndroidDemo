package com.zuo.wanandroidjava.ui.act;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.flyco.tablayout.SlidingTabLayout;
import com.zuo.wanandroidjava.R;
import com.zuo.wanandroidjava.base.BaseActivity;
import com.zuo.wanandroidjava.bean.Tree;
import com.zuo.wanandroidjava.ui.fragment.TreeListFragment;
import com.zuo.wanandroidjava.weight.SlideViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

@Route(path = "/zuo/treeActivity")
public class TreeActivity extends BaseActivity {
    @Autowired
    Tree.DataBean dataBean;
    @BindView(R.id.tablayout)
    SlidingTabLayout tablacyout;
    @BindView(R.id.viewpager)
    SlideViewPager viewpager;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbarlayout)
    AppBarLayout appbarlayout;
    private List<String> mTitle = new ArrayList<>();
    protected List<Integer> ids = new ArrayList<>();
    private String TAG = "TreeActivity";

    @Override
    public int getLayoutId() {
        return R.layout.activity_tree;
    }

    @Override
    public void initInject() {
        ARouter.getInstance().inject(this);
    }

    @Override
    public void init() {
        // String e = getIntent().getStringExtra("dataBean");
        if (dataBean == null) {
            Log.d(TAG, "dataBean==null");
            return;
        }
        toolbar.setTitle(dataBean.getName());
        List<Tree.DataBean.ChildrenBean> children = dataBean.getChildren();
        for (Tree.DataBean.ChildrenBean child : children) {
            mTitle.add(child.getName());
            ids.add(child.getId());
        }
        String[] strings = mTitle.toArray(new String[mTitle.size()]);
        viewpager.setAdapter(new TreeAdapter(getSupportFragmentManager()));
        tablacyout.setViewPager(viewpager, strings);
    }

    public AppBarLayout getAppbarlayout() {
        return appbarlayout;
    }

    class TreeAdapter extends FragmentPagerAdapter {


        public TreeAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return TreeListFragment.instance(ids.get(i));
        }

        @Override
        public int getCount() {
            return mTitle.size();
        }
    }
}
