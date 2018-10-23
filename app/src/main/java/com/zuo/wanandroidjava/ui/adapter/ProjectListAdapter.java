package com.zuo.wanandroidjava.ui.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zuo.wanandroidjava.R;
import com.zuo.wanandroidjava.bean.ProjectList;

import java.util.List;

public class ProjectListAdapter extends BaseQuickAdapter<ProjectList.DataBean.DatasBean,BaseViewHolder> {



    public ProjectListAdapter(int layoutResId, @Nullable List<ProjectList.DataBean.DatasBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ProjectList.DataBean.DatasBean item) {
        /*tv_title
            tv_desc
    tv_author
            tvTime
    iv_image*/
        helper.setText(R.id.tv_title,item.getTitle());
        helper.setText(R.id.tv_desc, item.getDesc());
        helper.setText(R.id.tv_author, item.getAuthor());
        Glide.with(mContext).load(item.getEnvelopePic()).into((ImageView) helper.getView(R.id.iv_image));

    }
}
