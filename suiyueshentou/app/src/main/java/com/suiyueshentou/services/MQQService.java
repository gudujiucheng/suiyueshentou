package com.suiyueshentou.services;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Parcelable;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.suiyueshentou.utils.DrawerToast;

import java.util.ArrayList;
import java.util.List;

public class MQQService extends AccessibilityService {

    private boolean mNeedBack;

    private AccessibilityNodeInfo rootNodeInfo;

    private static final String WECHAT_VIEW_OTHERS_CH = "点击拆开";

    private static final String WECHAT_VIEW_DETAILS = "查看详情";

    private static final String KL_WECHAT_VIEW_OTHERS_CH = "口令红包";

    private static final String TX_WECHAT_VIEW_OTHERS_CH = "点击输入口令";

    private static final String FS_WECHAT_VIEW_OTHERS_CH = "发送";

    private static final String BACK_TEXT_01 = "红包记录";

    private static final String BACK_TEXT_02 = "来晚一步，红包被领完了";

    private static final String BACK_TEXT_03 = "已存入余额";

    private static final String QQ_NOTIFICATION_TIP = "[QQ红包]";

    private static final int MAX_CACHE_TOLERANCE = 5000;

    private long mLogtime;

    private SharedPreferences pref;

    private String lastFetchedHongbaoId;// 最后一个

    private long lastFetchedTime;// 最后时间

    private long getKLTime;

    private long getYQTime;

    /**
     * AccessibilityEvent的回调方法
     *
     * @param event 事件
     */
    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        this.rootNodeInfo = getRootInActiveWindow();
        if (rootNodeInfo == null)
            return;

        /** ------------------yunqichai------------------------- */
        List<AccessibilityNodeInfo> nodes1 = rootNodeInfo.findAccessibilityNodeInfosByText(WECHAT_VIEW_OTHERS_CH);
        for (AccessibilityNodeInfo accessibilityNodeInfo : nodes1) {
            if (System.currentTimeMillis() - getYQTime > 200) {// 加个0.2秒限制防止频繁点击
                accessibilityNodeInfo.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                getYQTime = System.currentTimeMillis();
            }
        }

        /** -----------------koulingchai-------------------------- */

        List<AccessibilityNodeInfo> KLnodes = rootNodeInfo.findAccessibilityNodeInfosByText(KL_WECHAT_VIEW_OTHERS_CH);

        if (KLnodes != null && !KLnodes.isEmpty()) {
            for (AccessibilityNodeInfo accessibilityNodeInfo : KLnodes) {

                AccessibilityNodeInfo parent = accessibilityNodeInfo.getParent();
                if (parent != null) {
                    if (parent.getClassName().equals("android.widget.RelativeLayout")
                            && accessibilityNodeInfo.getClassName().equals("android.widget.TextView")) {
                        CharSequence nodetext = accessibilityNodeInfo.getText();
                        if (nodetext != null) {
                            String text = nodetext.toString();
                            if (!text.contains("已拆开")) {
                                if (System.currentTimeMillis() - getKLTime > 200) {// 加个0.2秒限制防止频繁点击
                                    parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                    getKLTime = System.currentTimeMillis();
                                }

                            }
                        }
                    }
                }

            }
        }

        List<AccessibilityNodeInfo> KLText = rootNodeInfo.findAccessibilityNodeInfosByText(TX_WECHAT_VIEW_OTHERS_CH);
        if (KLText != null && !KLText.isEmpty()) {
            for (AccessibilityNodeInfo textnode : KLText) {
                textnode.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                List<AccessibilityNodeInfo> Btnodes = rootNodeInfo
                        .findAccessibilityNodeInfosByText(FS_WECHAT_VIEW_OTHERS_CH);
                for (AccessibilityNodeInfo btnode : Btnodes) {
                    if (btnode != null && btnode.getClassName().equals("android.widget.Button")) {
                        btnode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    }

                }
            }

        }

        /** -----------------putongchai-------------------------- */
        List<AccessibilityNodeInfo> PTnodes = rootNodeInfo.findAccessibilityNodeInfosByText(WECHAT_VIEW_DETAILS);
        if (PTnodes != null && !PTnodes.isEmpty()) {
            for (AccessibilityNodeInfo accessibilityNodeInfo : PTnodes) {
                String hbid = getHBText(accessibilityNodeInfo);
                AccessibilityNodeInfo parent = accessibilityNodeInfo.getParent();
                long now = System.currentTimeMillis();
                if ((hbid != null && !hbid.equals(lastFetchedHongbaoId))
                        || (now - lastFetchedTime) > MAX_CACHE_TOLERANCE) {
                    if (parent != null) {
                        if (parent.getClassName().equals("android.widget.RelativeLayout")
                                && accessibilityNodeInfo.getClassName().equals("android.widget.TextView")) {
                            CharSequence nodetext = accessibilityNodeInfo.getText();
                            if (nodetext != null) {
                                String text = nodetext.toString();
                                if (!text.contains("查看领取")) {
                                    parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                    lastFetchedHongbaoId = hbid;
                                    lastFetchedTime = now;
                                }
                            }
                        }
                    }

                }

            }
        }

        /** ------------------tongzhi------------------------- */
        if (watchNotifications(event))
            return;
        /** ------------------back------------------------- */
        List<AccessibilityNodeInfo> nodes3 = this.findAccessibilityNodeInfosByTexts(this.rootNodeInfo, new String[]{
                BACK_TEXT_01, BACK_TEXT_02, BACK_TEXT_03});
        if (!nodes3.isEmpty()) {
            mNeedBack = true;
        }

        if (mNeedBack) {
            mNeedBack = false;
            performGlobalAction(GLOBAL_ACTION_BACK);

        }

        showToast("QQ实时监控中...");

    }

    @Override
    public void onInterrupt() {

    }

    private List<AccessibilityNodeInfo> findAccessibilityNodeInfosByTexts(AccessibilityNodeInfo nodeInfo, String[] texts) {
        for (String text : texts) {
            if (text == null)
                continue;

            List<AccessibilityNodeInfo> nodes = nodeInfo.findAccessibilityNodeInfosByText(text);

            if (!nodes.isEmpty()) {
                return nodes;
            }
        }
        return new ArrayList<AccessibilityNodeInfo>();
    }

    private DrawerToast toast;

    public void showToast(final String s) {

        if ((System.currentTimeMillis() - mLogtime) < 1000) {// 禁止频繁输出监控日志
            return;
        }

        toast = DrawerToast.getInstance(getApplicationContext());
        toast.show(s);
        mLogtime = System.currentTimeMillis();

    }

    private String textContent = "";

    /**
     * 将节点对象的id和hb上的内容合并 用于表示一个唯一的hb
     */
    private synchronized String getHBText(AccessibilityNodeInfo node) {
        /* 获取hb上的文本 */
        textContent = "";
        try {
            getTextView(node);
        } catch (NullPointerException npe) {
            Log.i("MyError", "异常null");
            return null;
        }
        return textContent;
    }

    /**
     * @param node
     * @Description 遍历累加textview的值 作为标记
     * @author zhangcan
     */
    public void getTextView(AccessibilityNodeInfo node) {

        for (int i = 0; i < node.getChildCount(); i++) {
            // 获得该布局的所有子布局
            AccessibilityNodeInfo subView = node.getChild(i);
            // 判断子布局属性，如果还是ViewGroup类型，递归回收
            if (subView.getClassName().equals("android.widget.RelativeLayout")) {
                // 递归
                getTextView(subView);
            } else {
                if (subView.getClassName().equals("android.widget.TextView")) {
                    textContent += subView.getText().toString();
                }

            }
        }
    }

    private boolean watchNotifications(AccessibilityEvent event) {
        // Not a notification
        if (event.getEventType() != AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED)
            return false;

        // Not a hongbao
        String tip = event.getText().toString();
        if (!tip.contains(QQ_NOTIFICATION_TIP))
            return true;

        Parcelable parcelable = event.getParcelableData();
        if (parcelable instanceof Notification) {
            Notification notification = (Notification) parcelable;
            try {
                notification.contentIntent.send();
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

}
