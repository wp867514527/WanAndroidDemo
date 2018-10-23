package com.zuo.wanandroidjava.ui.act;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.Toolbar;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.flyco.tablayout.SlidingTabLayout;
import com.zuo.wanandroidjava.R;
import com.zuo.wanandroidjava.base.BaseActivity;
import com.zuo.wanandroidjava.bean.Data;
import com.zuo.wanandroidjava.bean.Tree;
import com.zuo.wanandroidjava.ui.fragment.TreeListFragment;
import com.zuo.wanandroidjava.weight.SlideViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

@Route(path = "/zuo/treeActivity")
public class TreeActivity extends BaseActivity {
    /* @Autowired
     Tree.DataBean dataBean;*/

    @Autowired
    int pos;
    @Autowired
    List<String> mTitle;
    @BindView(R.id.tablayout)
    SlidingTabLayout tablayout;
    @BindView(R.id.viewpager)
    SlideViewPager viewpager;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
   // private List<String> mTitle = new ArrayList<>();
    protected List<Integer> ids = new ArrayList<>();

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
        //Tree.DataBean dataBean = itemDatas.get(pos);
        System.out.println(mTitle);
       /* toolbar.setTitle(dataBean.getName());
        for (Tree.DataBean.ChildrenBean childrenBean : dataBean.getChildren()) {
            mTitle.add(childrenBean.getName());
            ids.add(childrenBean.getId());
        }

        TreeAdapter treeAdapter = new TreeAdapter(getSupportFragmentManager());
        viewpager.setAdapter(treeAdapter);
        String[] titles = (String[]) mTitle.toArray();
        tablayout.setViewPager(viewpager, titles);*/
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
