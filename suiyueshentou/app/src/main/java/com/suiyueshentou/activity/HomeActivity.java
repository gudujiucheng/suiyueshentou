package com.suiyueshentou.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.suiyueshentou.R;
import com.suiyueshentou.base.BaseActivity;
import com.suiyueshentou.base.BaseFragmentActivity;
import com.suiyueshentou.fragment.CartFragment;
import com.suiyueshentou.fragment.HomeFragment;
import com.suiyueshentou.fragment.MineFragment;
import com.suiyueshentou.fragment.StoreFragment;
import com.suiyueshentou.utils.DebugLog;
import com.suiyueshentou.utils.UpdateUtils;
import com.suiyueshentou.view.MenuBarView;



public class HomeActivity extends BaseFragmentActivity {
    private int showIndex = 0;
    //相关模块
    private Fragment[] mFrags = new Fragment[4];

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        MenuBarView menuBarView = (MenuBarView) findViewById(R.id.main_menu_bar);
        menuBarView.setOnMenuItemClickListener(menuListener);
        menuBarView.setCurrentMenuItem(0);


    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        DebugLog.e("MyTest", "首页 onRestoreInstanceState 执行");
        //异常或者其他情况导致activity回收，恢复
        showFragment(getSupportFragmentManager().beginTransaction(), savedInstanceState.getInt("index"));
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        DebugLog.e("MyTest", "首页 onSaveInstanceState 执行");
        //记录当前的position
        outState.putInt("index", showIndex);
    }

    /**
     * 显示/隐藏Fragment
     *
     * @param trans
     * @param showIndex
     */
    private void showFragment(FragmentTransaction trans, int showIndex) {
        // 遍历,隐藏其他,只显示当前的
        for (int i = 0; i < 4; i++) {
            if (i == showIndex) {
                trans.show(mFrags[i]);        // 显示当前显示的
            } else if (mFrags[i] != null) {
                trans.hide(mFrags[i]);        // 隐藏当前不显示并且不为空的
            }
        }
    }


    /**
     * 底部menu监听器
     */
    private MenuBarView.OnMenuItemClickListener menuListener = new MenuBarView.OnMenuItemClickListener() {
        @Override
        public void onMenuItemClick(int viewId) {
            FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
            switch (viewId) {

                case R.id.home_menu_iv:
                    showIndex = 0;
                    if (mFrags[0] == null) {
                        mFrags[0] = new HomeFragment();
                        trans.add(R.id.layout_menu, mFrags[showIndex]);
                    }
                    break;

                case R.id.store_menu_iv:
                    showIndex = 1;
                    if (mFrags[1] == null) {
                        mFrags[1] = new StoreFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("welcomePic", "");
                        mFrags[1].setArguments(bundle);
                        trans.add(R.id.layout_menu, mFrags[showIndex]);
                    }
                    break;

                case R.id.cart_menu_iv:
                    showIndex = 2;
                    if (mFrags[2] == null) {
                        mFrags[2] = new CartFragment();
                        trans.add(R.id.layout_menu, mFrags[showIndex]);
                    }
                    break;

                case R.id.mine_menu_iv:
                    showIndex = 3;
                    if (mFrags[3] == null) {
                        mFrags[3] = new MineFragment();
                        trans.add(R.id.layout_menu, mFrags[showIndex]);
                    }
                    break;
            }

            showFragment(trans, showIndex);
            trans.commit();
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
