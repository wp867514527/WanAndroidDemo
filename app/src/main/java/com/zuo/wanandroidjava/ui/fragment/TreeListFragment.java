package com.zuo.wanandroidjava.ui.fragment;

import android.os.Bundle;

import com.zuo.wanandroidjava.R;
import com.zuo.wanandroidjava.base.BaseFragment;

public class TreeListFragment extends BaseFragment {
    @Override
    public int getLayoutId() {
        return R.layout.fragment_tree_list;
    }

    @Override
    public void initInject() {

    }

    @Override
    public void init() {

    }


    public static TreeListFragment instance(int id) {
        Bundle args = new Bundle();
        TreeListFragment treeListFragment = new TreeListFragment();
        args.putInt("id",id);
        treeListFragment.setArguments(args);
        return treeListFragment;
    }
}
