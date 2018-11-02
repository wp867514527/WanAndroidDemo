package com.zuo.wanandroidjava.ui.act;

import android.annotation.SuppressLint;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.zuo.wanandroidjava.R;
import com.zuo.wanandroidjava.base.BaseActivity;
import com.zuo.wanandroidjava.bean.Article;
import com.zuo.wanandroidjava.helper.ARouterHelper;
import com.zuo.wanandroidjava.presenter.SearchPresneter;
import com.zuo.wanandroidjava.presenter.contract.SearchContract;
import com.zuo.wanandroidjava.ui.adapter.HomeAdapter;
import com.zuo.wanandroidjava.util.ToastUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;

//https://mp.weixin.qq.com/s?__biz=MzAxMTI4MTkwNQ==&mid=2650823105&idx=1&sn=3989787daac29f098525f124a2cbb94d&chksm=80b78d5fb7c00449a26980f199d69aa83b40fc3b5e1a2f11bad08ba914fca451decc7ed68ffa&scene=38#wechat_redirect
//搜素框
@Route(path = ARouterHelper.SEARCH_PAGE)
public class SearchActivity extends BaseActivity<SearchPresneter> implements SearchContract.IView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbarlayout)
    AppBarLayout appbarlayout;
    @BindView(R.id.rv_search)
    RecyclerView rvSearch;
    private String TAG = "SearchActivity";
    private List<Article.DatasBean> mItemDatas = new ArrayList<>();
    private HomeAdapter adapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    public void initInject() {

        getComponent().inject(this);
    }

    @Override
    public void init() {
        initToolbar();
        adapter = new HomeAdapter(R.layout.item_home, mItemDatas);
        rvSearch.setAdapter(adapter);
        rvSearch.setLayoutManager(new LinearLayoutManager(this));
    }

    @SuppressLint("RestrictedApi")
    private void initToolbar() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressedSupport();


            }
        });
    }

    @SuppressLint("CheckResult")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.menu_search));
        searchView.setQueryHint("搜索");
        searchView.setIconified(false);
        searchView.onActionViewExpanded();
        //searchView.setIconifiedByDefault(false);
        Class<? extends SearchView> aClass = searchView.getClass();
        try {
            Field mSearchSrcTextView = aClass.getDeclaredField("mSearchSrcTextView");
            mSearchSrcTextView.setAccessible(true); //取消 java 访问权限检查
            SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete) mSearchSrcTextView.get(searchView);
            RxTextView.textChanges(searchAutoComplete).debounce(1, TimeUnit.SECONDS)  //坑比框架，既然不是主线程
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(s -> {
                                Log.d(TAG, "数据==" + s);
                                if (TextUtils.isEmpty(s)) {
                                    return;
                                }
                                presenter.getDatas(s.toString());
                            }
                    );

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


        return super.onCreateOptionsMenu(menu);
    }

    long time = 0;

    @Override
    public void onBackPressedSupport() {
        long millis = System.currentTimeMillis();
        if (millis - time > 2000) {
            time = millis;
            ToastUtils.showCenterToast("长得丑的需要点俩次");
            return;
        }
        super.onBackPressedSupport();
    }

    @Override
    public void setDatas(List<Article.DatasBean> datas) {
        adapter.setNewData(datas);
    }
}
