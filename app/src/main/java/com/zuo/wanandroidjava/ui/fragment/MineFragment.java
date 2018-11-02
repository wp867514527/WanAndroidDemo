package com.zuo.wanandroidjava.ui.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zuo.wanandroidjava.R;
import com.zuo.wanandroidjava.base.BaseFragment;
import com.zuo.wanandroidjava.bean.EventMessage;
import com.zuo.wanandroidjava.helper.ARouterHelper;
import com.zuo.wanandroidjava.presenter.MinePresenter;
import com.zuo.wanandroidjava.presenter.contract.MineContract;
import com.zuo.wanandroidjava.util.ToastUtils;
import com.zuo.wanandroidjava.weight.HeadZoomScrollView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MineFragment extends BaseFragment<MinePresenter> implements MineContract.IView {

    @BindView(R.id.dzsv)
    HeadZoomScrollView mDzsv;
    Unbinder unbinder;
    @BindView(R.id.iv)
    ImageView mIv;
    @BindView(R.id.login)
    TextView mLogin;
    @BindView(R.id.shoucang)
    TextView mShoucang;
    @BindView(R.id.about)
    TextView mAbout;
    @BindView(R.id.linearlayout)
    LinearLayout mLinearlayout;
    private String TAG = "HomeFragment";
    public boolean isLight = true;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    public void initInject() {
        getComponent().inject(this);

    }

    @Override
    public void init() {
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginSuccess(EventMessage message) {
        if (message.isSuccess()) {
            mLogin.setText("退出");
        } else {
            mLogin.setText("登陆");
        }
    }


    @OnClick({R.id.login, R.id.shoucang, R.id.about})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login:
                if (mLogin.getText().toString().equals("登陆")) {
                    //登陆
                    ARouterHelper.jumpLogin();
                } else {
                    //退出登陆
                    presenter.logOut();
                }
                break;
            case R.id.shoucang:
                ARouterHelper.jumpShoucang();
                break;
            case R.id.about:
                ARouterHelper.jumpAbout();
                break;
        }
    }

    @Override
    public void logoutCallBack(String isSucess) {
        switch (isSucess) {
            case "success":
                ToastUtils.showToast("退出登陆");
                mLogin.setText("登陆");
                break;
            case "error":
                ToastUtils.showToast("退出失败");
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
