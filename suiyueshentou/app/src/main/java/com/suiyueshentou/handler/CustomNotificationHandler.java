package com.suiyueshentou.handler;

import android.content.Context;
import android.widget.Toast;

import com.suiyueshentou.utils.DebugLog;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

import java.util.Map;

/**
 * Created by zc on 2016/6/24.
 * launchApp、openUrl、openActivity这三个方法已经由消息推送SDK完成
 * 而dealWithCustomAction则只是一个空的方法。 若开发者需要处理自定义行为，则可以重写方法dealWithCustomAction();
 * 该Handler是在BroadcastReceiver中被调用。因此若需启动Activity，需为Intent添加Flag：Intent.FLAG_ACTIVITY_NEW_TASK，否则无法启动Activity。
 */
public class CustomNotificationHandler extends UmengNotificationClickHandler {

    @Override
    public void dismissNotification(Context context, UMessage msg) {
        super.dismissNotification(context, msg);
    }

    @Override
    public void launchApp(Context context, UMessage msg) {
        super.launchApp(context, msg);
    }

    @Override
    public void openActivity(Context context, UMessage msg) {
        super.openActivity(context, msg);
    }

    @Override
    public void openUrl(Context context, UMessage msg) {
        super.openUrl(context, msg);
    }

    @Override
    public void dealWithCustomAction(Context context, UMessage msg) {
        super.dealWithCustomAction(context, msg);

        for (Map.Entry<String, String> entry : msg.extra.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            DebugLog.e(DebugLog.TAG, "通知携带的参数：key：" + key + "    value:" + value);
        }
        Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
    }

    @Override
    public void autoUpdate(Context context, UMessage msg) {
        super.autoUpdate(context, msg);
    }

}
