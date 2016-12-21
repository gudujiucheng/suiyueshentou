package com.suiyueshentou.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.suiyueshentou.common.dialog.LoadingDialog;
import com.suiyueshentou.utils.AppManager;
import com.suiyueshentou.utils.DebugLog;

/**
 * activity基类
 */
public class BaseActivity extends Activity implements BaseImpMethod {

    private LoadingDialog mLoadingDialog;

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
        //防止 WindowManager$BadTokenException
        dismissLoadingDialog();
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


    public void showLoadingDialog() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this);
        }
        if (!mLoadingDialog.isShowing()) {
            mLoadingDialog.show();
        }
    }

    public void dismissLoadingDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }
}
