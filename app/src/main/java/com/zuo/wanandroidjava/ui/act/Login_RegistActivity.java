package com.zuo.wanandroidjava.ui.act;

import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.zuo.wanandroidjava.R;
import com.zuo.wanandroidjava.base.BaseActivity;
import com.zuo.wanandroidjava.bean.EventMessage;
import com.zuo.wanandroidjava.helper.ARouterHelper;
import com.zuo.wanandroidjava.presenter.Login_RegistPresneter;
import com.zuo.wanandroidjava.presenter.contract.Login_RegistContract;
import com.zuo.wanandroidjava.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

//https://mp.weixin.qq.com/s?__biz=MzAxMTI4MTkwNQ==&mid=2650823105&idx=1&sn=3989787daac29f098525f124a2cbb94d&chksm=80b78d5fb7c00449a26980f199d69aa83b40fc3b5e1a2f11bad08ba914fca451decc7ed68ffa&scene=38#wechat_redirect
//登陆注册
@Route(path = ARouterHelper.LOGIN_REGIST)
public class Login_RegistActivity extends BaseActivity<Login_RegistPresneter> implements Login_RegistContract.IView {

    @BindView(R.id.et_username)
    EditText mEtUsername;
    @BindView(R.id.til_username)
    TextInputLayout mTilUsername;
    @BindView(R.id.et_psw)
    EditText mEtPsw;
    @BindView(R.id.til_psw)
    TextInputLayout mTilPsw;
    @BindView(R.id.btn_login)
    Button mBtnLogin;
    @BindView(R.id.btn_login_regist)
    Button mBtnLoginRegist;
    private String TAG = "Login_RegistActivity";

    @Override
    public int getLayoutId() {
        return R.layout.activity_login_regist;
    }

    @Override
    public void initInject() {
        getComponent().inject(this);
    }

    @Override
    public void init() {
    }


    @OnClick({R.id.btn_login, R.id.btn_login_regist})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                presenter.login(mEtUsername.getText().toString(), mEtPsw.getText().toString());
                break;
            case R.id.btn_login_regist:
                presenter.regist(mEtUsername.getText().toString(), mEtPsw.getText().toString(), mEtPsw.getText().toString());
                presenter.login(mEtUsername.getText().toString(), mEtPsw.getText().toString());
                break;
        }
    }


    @Override
    public void register(String isSucess) {
        switch (isSucess) {
            case "success":
                ToastUtils.showToast("注册成功");
                break;
            case "error":
                ToastUtils.showToast("注册失败");
                break;

        }
    }

    @Override
    public void login(String isSucess) {
        switch (isSucess) {
            case "success":
                ToastUtils.showToast("登陆成功");
                EventBus.getDefault().post(new EventMessage(true));
                finish();
                break;
            case "error":
                ToastUtils.showToast("登陆失败");
                break;

        }
    }
}
