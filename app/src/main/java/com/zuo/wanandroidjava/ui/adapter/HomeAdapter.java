package com.zuo.wanandroidjava.ui.adapter;

import android.support.annotation.Nullable;

import com.aloe.zxlib.utils.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zuo.wanandroidjava.R;
import com.zuo.wanandroidjava.bean.Article;

import java.sql.Time;
import java.util.List;

public class HomeAdapter extends BaseQuickAdapter<Article.DatasBean,BaseViewHolder> {
    public HomeAdapter(int layoutResId, @Nullable List<Article.DatasBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Article.DatasBean item) {
  /*      tv_title
                tv_content
        tv_author
                tv_time*/
        helper.setText(R.id.tv_title, item.getTitle());
        helper.setText(R.id.tv_content,item.getDesc());
        helper.setText(R.id.tv_author,item.getAuthor());

        helper.setText(R.id.tv_time, TimeUtils.getTimestampString(item.getPublishTime()));

    }
}
