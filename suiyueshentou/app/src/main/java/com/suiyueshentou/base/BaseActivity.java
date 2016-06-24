package com.suiyueshentou.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.suiyueshentou.common.dialog.LoadingDialog;
import com.suiyueshentou.utils.AppManager;
import com.suiyueshentou.utils.DebugLog;
import com.umeng.message.PushAgent;

/**
 * activity基类
 */
public class BaseActivity extends Activity {

    private LoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //友盟统计应用启动数据
        PushAgent.getInstance(this).onAppStart();

        init();
        initBundle();
        initView();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        DebugLog.i("BaseActivity", "当前所在的activity：" + getClass().getSimpleName());
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //防止 WindowManager$BadTokenException
        dismissLoadingDialog();
        // 结束Activity&从堆栈中移除
        AppManager.getAppManager().finishActivity(this);

    }

    /**
     * 初始化
     */
    private void init() {
        // 添加Activity到堆栈
        AppManager.getAppManager().addActivity(this);
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

    }

    /**
     * 初始化数据
     */
    protected void initData() {

    }

    /**
     * 设置页面标题
     *
     * @param resId
     * @param title
     */
    protected void setHeadView(int resId, String title) {
        ((TextView) findViewById(resId)).setText(title);
    }


    /**
     * 设置返回按钮监听
     */
    protected void setBack(int backResId) {
        findViewById(backResId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 展示加载弹窗
     */
    public void showLoadingDialog() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this);
        }
        if (!mLoadingDialog.isShowing()) {
            mLoadingDialog.show();
        }
    }

    /**
     * 消失加载弹窗
     */
    public void dismissLoadingDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }
}
