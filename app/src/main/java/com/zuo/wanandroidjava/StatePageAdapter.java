package com.zuo.wanandroidjava;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.zuo.wanandroidjava.base.BaseFragment;

import java.util.List;

public class StatePageAdapter<T extends BaseFragment> extends FragmentStatePagerAdapter {

    private final List<T> data;

    public StatePageAdapter(FragmentManager fm, List<T> fragments) {
        super(fm);
        this.data = fragments;
    }

    @Override
    public Fragment getItem(int i) {
        return data.get(i);
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

}
