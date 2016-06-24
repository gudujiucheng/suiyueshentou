package com.suiyueshentou.base;

import android.app.Application;

import com.suiyueshentou.handler.CustomNotificationHandler;
import com.umeng.message.PushAgent;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;


public class MyApplication extends Application {
    private PushAgent mPushAgent;

    @Override
    public void onCreate() {
        super.onCreate();
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();

        OkHttpUtils.initClient(okHttpClient);


        mPushAgent = PushAgent.getInstance(this);
        mPushAgent.setDebugMode(true);
        /**
         * 自定义通知点击回调
         * */
        CustomNotificationHandler notificationClickHandler = new CustomNotificationHandler();
        mPushAgent.setNotificationClickHandler(notificationClickHandler);
    }


}
