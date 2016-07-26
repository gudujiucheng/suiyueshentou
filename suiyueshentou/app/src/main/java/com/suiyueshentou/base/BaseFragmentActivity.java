package com.suiyueshentou.base;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

import com.suiyueshentou.common.dialog.LoadingDialog;
import com.suiyueshentou.utils.AppManager;
import com.suiyueshentou.utils.DebugLog;


public class BaseFragmentActivity extends FragmentActivity implements BaseImpMethod {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 添加Activity到堆栈
        AppManager.getAppManager().addActivity(this);
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

        // 结束Activity&从堆栈中移除
        AppManager.getAppManager().finishActivity(this);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void setHeadView(int resId, String title) {
        ((TextView) findViewById(resId)).setText(title);
    }

    @Override
    public void setBack(int backResId) {
        findViewById(backResId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private LoadingDialog mLoadingDialog;

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
