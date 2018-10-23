package com.zuo.wanandroidjava.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jaeger.library.StatusBarUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener;
import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.zuo.wanandroidjava.R;
import com.zuo.wanandroidjava.base.BaseFragment;
import com.zuo.wanandroidjava.bean.Article;
import com.zuo.wanandroidjava.bean.Banner;
import com.zuo.wanandroidjava.presenter.contract.HomeContract;
import com.zuo.wanandroidjava.presenter.HomePresenter;
import com.zuo.wanandroidjava.ui.adapter.HomeAdapter;
import com.zuo.wanandroidjava.util.GlideImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.Unbinder;
import io.reactivex.annotations.NonNull;

public class HomeFragment extends BaseFragment<HomePresenter> implements HomeContract.IView {
    @BindView(R.id.v_top_0)
    View vTop0;
    @BindView(R.id.v_top_1)
    View vTop1;
    @BindView(R.id.img_search)
    ImageView imgSearch;
    @BindView(R.id.ll_top)
    LinearLayout llTop;
    @BindView(R.id.v_top_2)
    View vTop2;
    @BindView(R.id.img_a)
    ImageView imgA;
    @BindView(R.id.rv_home)
    RecyclerView rvHome;
    @BindView(R.id.srl_home)
    SmartRefreshLayout srlHome;
    Unbinder unbinder;
    private HomeAdapter homeAdapter;
    private List<Article.DatasBean> mItemDatas = new ArrayList<>();
    private com.youth.banner.Banner banner;
    private String TAG = "HomeFragment";
    public boolean isLight = true;
    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void initInject() {
        getComponent().inject(this);



    }

    @Override
    public void init() {
        initTitle();
        initView();
        srlHome.setOnMultiPurposeListener(new SimpleMultiPurposeListener() {
            @Override
            public void onRefresh(@android.support.annotation.NonNull RefreshLayout refreshLayout) {
                presenter.onRefresh();
            }

            @Override
            public void onLoadMore(@android.support.annotation.NonNull RefreshLayout refreshLayout) {
                presenter.loadMore();
            }
        });
        homeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ARouter.getInstance()
                        .build("/zuo/webActivity")
                        .withString("url",mItemDatas.get(position).getLink())
                        .navigation();


            }
        });
    }

    private void initView() {
        homeAdapter = new HomeAdapter(R.layout.item_home, mItemDatas);
        rvHome.setLayoutManager(new LinearLayoutManager(getContext()));
        rvHome.setAdapter(homeAdapter);
        View headView = LayoutInflater.from(getContext()).inflate(R.layout.item_home_banner, null);
        banner = headView.findViewById(R.id.banner);
        homeAdapter.addHeaderView(headView);
        presenter.getBannerDatas();
        presenter.getHomeDatas(0);
        rvHome.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int mDistance = 0;
            int maxDistance = DensityUtil.dp2px(200) - getStatusBarHeight();
            //float percent = mDistance * 1f / maxDistance;//百分比

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                mDistance += dy;
                float percent = mDistance * 1f / maxDistance;//百分比
                Log.d(TAG, "百分比是多少==" + percent);
                if (percent > 0.2) {
                    isLight = true;
                    StatusBarUtil.setLightMode(getActivity());
                }else {
                    isLight = false;
                    StatusBarUtil.setDarkMode(getActivity());

                }
                llTop.setAlpha(percent);
            }
        });
    }

    private void initTitle() {
        // 设置
        fitsStatusBarView(vTop0, vTop1, vTop2);
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
    public void setBannerData(List<Banner> bannerData) {
        List<String> images = new ArrayList<>();
        for (int i = 0; i < bannerData.size(); i++) {
            images.add(bannerData.get(i).getImagePath());
        }
        //设置 banner
        banner.setImages(images)
                .setImageLoader(new GlideImageLoader())
                .start();

    }

    @Override
    public void setHomeDatas(Article article, boolean isRefresh) {
        if (isRefresh) {
            homeAdapter.replaceData(article.getDatas());
        } else {
            homeAdapter.addData(article.getDatas());
        }
    }

    @Override
    public void onUpdated(boolean refresh, boolean success) {
        if (refresh) {
            srlHome.finishRefresh(success);
            if (success) {
                showContentView();
            }else {
                showErrorView();
            }
        } else {
            srlHome.finishLoadMore(success);
        }

    }


}
