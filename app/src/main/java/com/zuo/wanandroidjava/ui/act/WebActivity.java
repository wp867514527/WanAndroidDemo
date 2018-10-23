package com.zuo.wanandroidjava.ui.act;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.just.agentweb.AgentWeb;
import com.zuo.wanandroidjava.R;
import com.zuo.wanandroidjava.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

@Route(path = "/zuo/webActivity")
public class WebActivity extends BaseActivity {
    @Autowired
    public String url;
    @BindView(R.id.ll_content)
    LinearLayout llContent;

    @Override
    public int getLayoutId() {
        return R.layout.activity_web;
    }

    @Override
    public void initInject() {
        ARouter.getInstance().inject(this);
    }

    @Override
    public void init() {
        AgentWeb.with(this)
                .setAgentWebParent(llContent, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .setMainFrameErrorView(R.layout.item_fail,-1)
                .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
                .interceptUnkownUrl()
                .createAgentWeb()
                .ready()
                .go(url);
    }


}
