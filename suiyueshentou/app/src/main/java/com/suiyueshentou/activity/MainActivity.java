package com.suiyueshentou.activity;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.Toast;

import com.suiyueshentou.R;
import com.suiyueshentou.base.BaseActivity;
import com.suiyueshentou.databasebean.Person;
import com.suiyueshentou.utils.DebugLog;
import com.suiyueshentou.utils.UpdateUtils;
import com.suiyueshentou.utils.Utils;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class MainActivity extends BaseActivity {

    private final Intent mAccessibleIntent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);

    private Button switchPlugin;

    private Button WXswitchPlugin;

    private PowerManager powerManager;

    private PowerManager.WakeLock wakeLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Bomb：自v3.4.7版本开始,设置BmobConfig,允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)，
        BmobConfig config =new BmobConfig.Builder(this)
        //设置appkey
        .setApplicationId("9c90bbc7dd6638e88a00da0c9d9f23a7")
        //请求超时时间（单位为秒）：默认15s
        .setConnectTimeout(30)
        //文件分片上传时每片的大小（单位字节），默认512*1024
        .setUploadBlockSize(1024*1024)
        //文件的过期时间(单位为秒)：默认1800s
        .setFileExpiration(2500)
        .build();
        Bmob.initialize(config);

        switchPlugin = (Button) findViewById(R.id.button_accessible);
        WXswitchPlugin = (Button) findViewById(R.id.button_accessible_wx);

        handleMIUIStatusBar();
        updateServiceStatus();
        explicitlyLoadPreferences();


        //友盟
        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.enable();

        //开启推送并设置注册的回调处理
        mPushAgent.enable(new IUmengRegisterCallback() {
            @Override
            public void onRegistered(final String registrationId) {
                //handler.post(r)。r是要执行的任务代码。意思就是说r的代码实际是在UI线程执行的。可以写更新UI的代码。
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        //onRegistered方法的参数registrationId即是device_token
                        DebugLog.e(DebugLog.TAG, "友盟推送 device_token:" + registrationId);
                    }
                });
            }
        });



        Person p2 = new Person();
        p2.setName("lucky");
        p2.setAddress("北京海淀");
        p2.save(new SaveListener<String>() {
            @Override
            public void done(String objectId,BmobException e) {
                if(e==null){
                    Utils.showToast(MainActivity.this,"添加数据成功，返回objectId为："+objectId);
                }else{
                    Utils.showToast(MainActivity.this,"创建数据失败：" + e.getMessage());
                }
            }
        });
    }


    /**
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
        UpdateUtils.getInstance().requestUpdate(this, false);//检查是否有更新
        updateServiceStatus();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
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
     * @param v
     * @Description 打开消息列表
     * @author zhangcan
     */
    public void openMSGList(View v) {
//        startActivity(new Intent(this, MSGListActivity.class));
    }

    public void openHelp(View v) {
        startActivity(new Intent(this, MyHelpActivity.class));
    }

    public void openSettings(View view) {
        Intent settingsIntent = new Intent(this, SettingsActivity.class);
        startActivity(settingsIntent);
    }
}
