package com.zuo.wanandroidjava.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener;
import com.zuo.wanandroidjava.R;
import com.zuo.wanandroidjava.base.BaseFragment;
import com.zuo.wanandroidjava.bean.TreeList;
import com.zuo.wanandroidjava.helper.ARouterHelper;
import com.zuo.wanandroidjava.presenter.TreeListPresenter;
import com.zuo.wanandroidjava.presenter.contract.TreeListContract;
import com.zuo.wanandroidjava.ui.act.TreeActivity;
import com.zuo.wanandroidjava.ui.adapter.TreeListAdapter;
import com.zuo.wanandroidjava.weight.MultiStateView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class TreeListFragment extends BaseFragment<TreeListPresenter> implements TreeListContract.IView {
    @BindView(R.id.rv_tree_list)
    RecyclerView rvTreeList;
    @BindView(R.id.srl_tree_list)
    SmartRefreshLayout srlTreeList;
    private List<TreeList.DataBean.DatasBean> mItemDatas = new ArrayList<>();
    private TreeListAdapter adapter;
    private int id;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_tree_list;
    }

    @Override
    public void initInject() {
        getComponent().inject(this);
    }

    @Override
    public void init() {
        id = getArguments().getInt("id");
        adapter = new TreeListAdapter(R.layout.item_tree_list, mItemDatas);
        rvTreeList.setAdapter(adapter);
        rvTreeList.setLayoutManager(new LinearLayoutManager(getContext()));
        presenter.getDatas(id);
        srlTreeList.setOnMultiPurposeListener(new SimpleMultiPurposeListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                presenter.onRefresh(id);
            }

            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                presenter.onLoadMore(id);
            }
        });
        adapter.setOnItemClickListener((adapter, itemView, pos) -> {
            ARouterHelper.jumpWeb(mItemDatas.get(pos).getLink());

        });
        ((TreeActivity) getActivity()).getAppbarlayout().addOnOffsetChangedListener(((appBarLayout, verticalOffset) -> {
            if (verticalOffset == 0) {
                srlTreeList.setEnableRefresh(true);
            } else {
                srlTreeList.setEnableRefresh(false);
            }

        }));
    }


    public static TreeListFragment instance(int id) {
        Bundle args = new Bundle();
        TreeListFragment treeListFragment = new TreeListFragment();
        args.putInt("id", id);
        treeListFragment.setArguments(args);
        return treeListFragment;
    }

    @Override
    public void setDatas(TreeList.DataBean data, boolean b) {
        List<TreeList.DataBean.DatasBean> datas = data.getDatas();
        if (b) {
            adapter.replaceData(datas);
        } else {
            adapter.addData(datas);
        }
        if (datas.size() < 10) {
            srlTreeList.setEnableLoadMore(false);
        }else{
            srlTreeList.setEnableLoadMore(true);
        }
    }

    @Override
    public void onUpdate(boolean isRefresh, boolean success) {
        if (isRefresh) {
            srlTreeList.finishRefresh(success);
        } else {
            srlTreeList.finishLoadMore(success);
        }
    }


}
