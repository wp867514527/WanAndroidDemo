package com.zuo.wanandroidjava.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener;
import com.zuo.wanandroidjava.R;
import com.zuo.wanandroidjava.base.BaseFragment;
import com.zuo.wanandroidjava.bean.ProjectList;
import com.zuo.wanandroidjava.presenter.ProjectListPresenter;
import com.zuo.wanandroidjava.presenter.contract.ProjectListContract;
import com.zuo.wanandroidjava.ui.adapter.ProjectListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ProjectListFragment extends BaseFragment<ProjectListPresenter> implements ProjectListContract.IView {
    @BindView(R.id.rv_project)
    RecyclerView rvProject;
    @BindView(R.id.srl_project)
    SmartRefreshLayout srlProject;
    private List<ProjectList.DataBean.DatasBean> mItemDatas = new ArrayList<>();
    private ProjectListAdapter adapter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_project_list;
    }

    @Override
    public void initInject() {
        getComponent().inject(this);
    }

    @Override
    public void init() {
        Bundle bundle = getArguments();
        int id = bundle.getInt("id");
        presenter.onRefresh(id);
        adapter = new ProjectListAdapter(R.layout.item_project_list, mItemDatas);
        rvProject.setAdapter(adapter);
        rvProject.setLayoutManager(new LinearLayoutManager(getContext()));
        srlProject.setOnMultiPurposeListener(new SimpleMultiPurposeListener(){

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                presenter.onRefresh(id);
            }

            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                presenter.loadMore(id);
            }
        });
        FragmentManager fragmentManager = getFragmentManager();
        ProjectFragment fragment = (ProjectFragment) getParentFragment();
        fragment.appbarlayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {
                    srlProject.setEnableRefresh(true);
                }else{
                    srlProject.setEnableRefresh(false);
                }
            }
        });

    }

    public static ProjectListFragment instance(int id) {
        ProjectListFragment projectListFragment = new ProjectListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        projectListFragment.setArguments(bundle);
        return projectListFragment;
    }


    @Override
    public void setDatas(ProjectList.DataBean data, boolean b) {
        if (b){
            adapter.replaceData(data.getDatas());
        }else{
            adapter.addData(data.getDatas());
        }


    }

    @Override
    public void onUpdate(boolean isRefresh, boolean success) {
        if (isRefresh) {
            srlProject.finishRefresh(success);
        } else {
            srlProject.finishLoadMore(success);
        }
    }


}
