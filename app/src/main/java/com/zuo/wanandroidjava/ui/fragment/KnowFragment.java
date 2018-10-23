package com.zuo.wanandroidjava.ui.fragment;

import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zuo.wanandroidjava.R;
import com.zuo.wanandroidjava.base.BaseFragment;
import com.zuo.wanandroidjava.bean.Tree;
import com.zuo.wanandroidjava.presenter.contract.KnowContract;
import com.zuo.wanandroidjava.presenter.KnowPresenter;
import com.zuo.wanandroidjava.ui.adapter.KnowAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.annotations.NonNull;

public class KnowFragment extends BaseFragment<KnowPresenter> implements KnowContract.IView {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_know)
    RecyclerView rvKnow;
    @BindView(R.id.coordinatorlayout)
    CoordinatorLayout coordinatorlayout;
    @BindView(R.id.v_top_0)
    View vTop0;
    private List<Tree.DataBean> itemDatas = new ArrayList<>();
    private KnowAdapter knowAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_know;
    }

    @Override
    public void initInject() {
        getComponent().inject(this);
    }

    @Override
    public void init() {
        fitsStatusBarView(vTop0);
        initView();
    }

    private void initView() {

        knowAdapter = new KnowAdapter(R.layout.item_knowledge, itemDatas);
        rvKnow.setAdapter(knowAdapter);
        rvKnow.setLayoutManager(new LinearLayoutManager(getContext()));
        knowAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Tree.DataBean dataBean = itemDatas.get(position);
                List<String> mTitle = new ArrayList<>();
                List<Integer> ids = new ArrayList<>();
                for (Tree.DataBean.ChildrenBean childrenBean : dataBean.getChildren()) {
                    mTitle.add(childrenBean.getName());
                    ids.add(childrenBean.getId());
                }
                ARouter.getInstance().build("/zuo/treeActivity")
                        .withInt("pos",position)
                        .withObject("datas",mTitle)
                        .navigation();

            }
        });

        presenter.getDatas();
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
    public void setDatas(Tree tree, boolean isRefresh) {
        knowAdapter.addData(tree.getData());
    }

    @Override
    public void onUpdate(boolean isRefresh, boolean success) {
        if (success) {
            showContentView();
        } else {
            showErrorView();
        }
    }


}
