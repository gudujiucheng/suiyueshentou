package com.suiyueshentou.base;

import android.app.Application;

import com.suiyueshentou.handler.CustomNotificationHandler;
import com.umeng.message.PushAgent;


public class MyApplication extends Application {
    private PushAgent mPushAgent;

    @Override
    public void onCreate() {
        super.onCreate();


        mPushAgent = PushAgent.getInstance(this);
        mPushAgent.setDebugMode(true);
        /**
         * 自定义通知点击回调
         * */
        CustomNotificationHandler notificationClickHandler = new CustomNotificationHandler();
        mPushAgent.setNotificationClickHandler(notificationClickHandler);
    }


}
