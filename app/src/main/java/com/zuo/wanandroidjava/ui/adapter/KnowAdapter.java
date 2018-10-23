package com.zuo.wanandroidjava.ui.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zuo.wanandroidjava.R;
import com.zuo.wanandroidjava.bean.Tree;

import java.util.List;

public class KnowAdapter extends BaseQuickAdapter<Tree.DataBean,BaseViewHolder> {
    public KnowAdapter(int layoutResId, @Nullable List<Tree.DataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Tree.DataBean item) {

       /* tvTitle
                tvContent*/
        helper.setText(R.id.tvTitle, item.getName());
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < item.getChildren().size(); i++) {
            String name = item.getChildren().get(i).getName();
            if (!TextUtils.isEmpty(name)) {
                stringBuilder.append(name);
                stringBuilder.append("  ");
            }

        }
        helper.setText(R.id.tvContent, stringBuilder.toString());
    }
}
