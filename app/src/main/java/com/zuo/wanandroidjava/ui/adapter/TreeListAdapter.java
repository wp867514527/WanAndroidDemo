package com.zuo.wanandroidjava.ui.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zuo.wanandroidjava.R;
import com.zuo.wanandroidjava.bean.TreeList;

import java.util.List;

public class TreeListAdapter extends BaseQuickAdapter<TreeList.DataBean.DatasBean,BaseViewHolder> {
    public TreeListAdapter(int layoutResId, @Nullable List<TreeList.DataBean.DatasBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TreeList.DataBean.DatasBean item) {
     /*   tv_title
                tv_content
        tv_author
                tv_time*/
        helper.setText(R.id.tv_title, item.getTitle());
        helper.setText(R.id.tv_content, item.getDesc());
        helper.setText(R.id.tv_author, item.getAuthor());
        helper.setText(R.id.tv_time, item.getPublishTime()+"");
    }
}
