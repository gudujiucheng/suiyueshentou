package com.suiyueshentou.fragment;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.Toast;

import com.suiyueshentou.R;
import com.suiyueshentou.activity.MyHelpActivity;
import com.suiyueshentou.activity.SettingsActivity;
import com.suiyueshentou.base.BaseFragment;
import com.suiyueshentou.utils.UpdateUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;


public class HomeFragment extends BaseFragment implements View.OnClickListener {

    //打开设置
    private final Intent mAccessibleIntent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
    //qq服务
    private Button mBtQQ;
    //wx服务
    private Button mBtWX;
    //电源管理 屏幕常亮相关
    private PowerManager powerManager;
    //电源管理 屏幕常亮相关
    private PowerManager.WakeLock wakeLock;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        mBtQQ = (Button) view.findViewById(R.id.bt_qq);
        mBtWX = (Button) view.findViewById(R.id.bt_wx);

        handleMIUIStatusBar();
        updateServiceStatus();
        explicitlyLoadPreferences();

        view.findViewById(R.id.bt_wx).setOnClickListener(this);
        view.findViewById(R.id.bt_qq).setOnClickListener(this);
        view.findViewById(R.id.bt_screen).setOnClickListener(this);
        view.findViewById(R.id.tv_more_msg).setOnClickListener(this);
        view.findViewById(R.id.bt_help).setOnClickListener(this);
        view.findViewById(R.id.iv_setting).setOnClickListener(this);


        return view;
    }

    /**
     * @Description 初始化默认配置
     * @author zhangcan
     */
    private void explicitlyLoadPreferences() {
        PreferenceManager.setDefaultValues(getActivity(), R.xml.preferences, false);
    }

    /**
     * 适配MIUI沉浸状态栏
     */
    private void handleMIUIStatusBar() {
        Window window = getActivity().getWindow();

        Class clazz = window.getClass();
        try {
            int tranceFlag = 0;
            Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");

            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_TRANSPARENT");
            tranceFlag = field.getInt(layoutParams);

            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(window, tranceFlag, tranceFlag);
        } catch (Exception e) {
            // 考虑到大多数非MIUI ROM都会打印出错误栈,不太优雅,而且一点卵用也没有,于是删了
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateServiceStatus();
    }


    /**
     * @Description 检查服务状态
     * @author zhangcan
     */
    private void updateServiceStatus() {
        boolean mQQServiceEnabled = false;
        boolean mWXServiceEnabled = false;

        AccessibilityManager accessibilityManager = (AccessibilityManager) getActivity().getSystemService(Context.ACCESSIBILITY_SERVICE);
        List<AccessibilityServiceInfo> accessibilityServices = accessibilityManager
                .getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
        for (AccessibilityServiceInfo info : accessibilityServices) {
            if (info.getId().equals(getActivity().getPackageName() + "/.services.MQQService")) {// 状态注意包名
                mQQServiceEnabled = true;
            }
            if (info.getId().equals(getActivity().getPackageName() + "/.services.MWXService")) {
                mWXServiceEnabled = true;
            }
        }

        if (mQQServiceEnabled) {
            mBtQQ.setText("关闭QQ辅助");
            mBtQQ.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_circle));
            // Prevent screen from dimming
        } else {
            mBtQQ.setText("开启QQ辅助");
            mBtQQ.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_circle_gray));
        }

        if (mWXServiceEnabled) {
            mBtWX.setText("关闭微信辅助");
            mBtWX.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_circle));
            // Prevent screen from dimming
        } else {
            mBtWX.setText("开启微信辅助");
            mBtWX.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_circle_gray));
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_qq://qq
                onButtonClicked();
                break;
            case R.id.bt_wx://wx
                onButtonClicked();
                break;
            case R.id.bt_screen://屏幕常亮
                keepScreenLight();
                break;
            case R.id.tv_more_msg://更多信息
                openGithub();
                break;
            case R.id.bt_help://帮助
                openHelp();
                break;
            case R.id.iv_setting://设置
                openSettings();
                break;
        }
    }


    public void onButtonClicked() {
        startActivity(mAccessibleIntent);
    }

    public void openGithub() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://github.com/geeeeeeeeek/WeChatLuckyMoney"));
        startActivity(browserIntent);
    }

    /**
     * 开启屏幕常亮
     */
    public void keepScreenLight() {
        //开启常亮1
        powerManager = (PowerManager) getActivity().getSystemService(getActivity().POWER_SERVICE);
        wakeLock = this.powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Lock");
        wakeLock.setReferenceCounted(false);// 是否需计算锁的数量
        wakeLock.acquire();
        //开启常亮2
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Toast.makeText(getActivity(), "开启成功", Toast.LENGTH_SHORT).show();


    }

    /**
     * 关闭屏幕常亮
     */
    public void closeScreenLight() {
        //关闭常亮1
        if (wakeLock != null) {
            wakeLock.release();
        }
        //关闭常亮2
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Toast.makeText(getActivity(), "关闭成功", Toast.LENGTH_SHORT).show();
    }

    public void openHelp() {
        startActivity(new Intent(getActivity(), MyHelpActivity.class));
    }

    public void openSettings() {
        Intent settingsIntent = new Intent(getActivity(), SettingsActivity.class);
        startActivity(settingsIntent);
    }
}
