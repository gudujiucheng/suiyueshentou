package com.suiyueshentou.activity;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.Toast;

import com.suiyueshentou.R;
import com.suiyueshentou.utils.UpdateTask;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final Intent mAccessibleIntent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);

    private Button switchPlugin;

    private Button WXswitchPlugin;

    private PowerManager powerManager;

    private PowerManager.WakeLock wakeLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 开启logcat输出，方便debug，发布时请关闭
//         XGPushConfig.enableDebug(this, true);
        // 如果需要知道注册是否成功，请使用registerPush(getApplicationContext(),
        // XGIOperateCallback)带callback版本
        // 如果需要绑定账号，请使用registerPush(getApplicationContext(),account)版本
        // 具体可参考详细的开发指南
        // 传递的参数为ApplicationContext
        Context context = getApplicationContext();
        XGPushManager.registerPush(context);

        // 2.36（不包括）之前的版本需要调用以下2行代码
        // Intent service = new Intent(context, XGPushService.class);
        // context.startService(service);

        // 其它常用的API：
        // 绑定账号（别名）注册：registerPush(context,account)或registerPush(context,account,
        // XGIOperateCallback)，其中account为APP账号，可以为任意字符串（qq、openid或任意第三方），业务方一定要注意终端与后台保持一致。
        // 取消绑定账号（别名）：registerPush(context,"*")，即account="*"为取消绑定，解绑后，该针对该账号的推送将失效
        // 反注册（不再接收消息）：unregisterPush(context)
        // 设置标签：setTag(context, tagName)
        // 删除标签：deleteTag(context, tagName)

        XGPushConfig.enableDebug(this, true);
        XGPushManager.registerPush(getApplicationContext(), new XGIOperateCallback() {
            @Override
            public void onSuccess(Object o, int code) {
                Log.e("DebugLog", " 信鸽注册成功！设备token ===> " + o + " ,resultCode ===> " + code);

            }

            @Override
            public void onFail(Object o, int code, String msg) {
                Log.e("DebugLog", " 信鸽注册失败！msg ===> " + msg + " ,errorCode ===> " + code);

            }
        });

        switchPlugin = (Button) findViewById(R.id.button_accessible);
        WXswitchPlugin = (Button) findViewById(R.id.button_accessible_wx);

        handleMIUIStatusBar();
        updateServiceStatus();
        explicitlyLoadPreferences();
    }


    /**
     *
     * @Description 初始化默认配置
     * @author zhangcan
     */
    private void explicitlyLoadPreferences() {
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
    }

    /**
     * 适配MIUI沉浸状态栏
     */
    private void handleMIUIStatusBar() {
        Window window = getWindow();

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
    protected void onResume() {
        super.onResume();
        new UpdateTask(this, false).update();//检查是否有更新
        updateServiceStatus();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     *
     * @Description 检查服务状态
     * @author zhangcan
     */
    private void updateServiceStatus() {
        boolean serviceEnabled = false;
        boolean WXserviceEnabled = false;

        AccessibilityManager accessibilityManager = (AccessibilityManager) getSystemService(Context.ACCESSIBILITY_SERVICE);
        List<AccessibilityServiceInfo> accessibilityServices = accessibilityManager
                .getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
        for (AccessibilityServiceInfo info : accessibilityServices) {
            if (info.getId().equals(getPackageName() + "/.services.MQQService")) {// 状态注意包名
                serviceEnabled = true;
            }
            if (info.getId().equals(getPackageName() + "/.services.MWXService")) {
                WXserviceEnabled = true;
            }
        }

        if (serviceEnabled) {
            switchPlugin.setText("关闭QQ辅助");
            switchPlugin.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_green));
            // Prevent screen from dimming
        } else {
            switchPlugin.setText("开启QQ辅助");
            switchPlugin.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_orange));
        }

        if (WXserviceEnabled) {
            WXswitchPlugin.setText("关闭微信辅助");
            WXswitchPlugin.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_green));
            // Prevent screen from dimming
        } else {
            WXswitchPlugin.setText("开启微信辅助");
            WXswitchPlugin.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_orange));
        }
    }

    public void onButtonClicked(View view) {
        startActivity(mAccessibleIntent);
    }

    public void openGithub(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://github.com/geeeeeeeeek/WeChatLuckyMoney"));
        startActivity(browserIntent);
    }

    public void keepOn01(View v) {
        powerManager = (PowerManager) this.getSystemService(this.POWER_SERVICE);
        wakeLock = this.powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Lock");
        wakeLock.setReferenceCounted(false);// 是否需计算锁的数量
        wakeLock.acquire();
        Toast.makeText(this, "方法1开启成功", Toast.LENGTH_SHORT).show();

    }

    public void close01(View v) {
        if (wakeLock != null) {
            wakeLock.release();
        }
        Toast.makeText(this, "方法1关闭成功", Toast.LENGTH_SHORT).show();
    }

    public void keepOn02(View v) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Toast.makeText(this, "方法2开启成功", Toast.LENGTH_SHORT).show();
    }

    public void close02(View v) {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Toast.makeText(this, "方法2关闭成功", Toast.LENGTH_SHORT).show();
    }

    /**
     *
     * @Description 打开消息列表
     * @author zhangcan
     * @param v
     */
    public void openMSGList(View v) {
        startActivity(new Intent(this, MSGListActivity.class));
    }

    public void openHelp(View v) {
        startActivity(new Intent(this, MyHelpActivity.class));
    }

    public void openSettings(View view) {
        Intent settingsIntent = new Intent(this, SettingsActivity.class);
        startActivity(settingsIntent);
    }
}
