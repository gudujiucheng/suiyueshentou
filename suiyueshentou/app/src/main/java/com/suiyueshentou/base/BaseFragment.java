package com.suiyueshentou.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Fragement 基类
 * Created by xz on 2016/4/6.
 */
public class BaseFragment extends Fragment {

    // 登录广播
//    protected LoginSuccessReceiver loginReceiver;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();
        initBundle();
        initView();
        initData();
    }


    /**
     * 初始化
     */
    protected void init() {
    }

    /**
     * 获取传递数据
     */
    protected void initBundle() {

    }

    /**
     * 初始化视图
     */
    protected void initView() {
        registerReceiver();
    }

    /**
     * 初始化数据
     */
    protected void initData() {

    }


    /**
     * 注册广播
     */
    private void registerReceiver() {
//        IntentFilter loginFilter = new IntentFilter();
//        loginFilter.addAction(Const.ACTION_LOGIN_SUCCESS);
//        loginReceiver = new LoginSuccessReceiver();
//        getContext().getApplicationContext().registerReceiver(loginReceiver, loginFilter);
    }

    /**
     * 通知我的界面 刷新的广播
     */
//    class LoginSuccessReceiver extends BroadcastReceiver {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            initData();
//        }
//    }
    @Override
    public void onDestroy() {
        super.onDestroy();
//        getContext().getApplicationContext().unregisterReceiver(loginReceiver);
    }
}
