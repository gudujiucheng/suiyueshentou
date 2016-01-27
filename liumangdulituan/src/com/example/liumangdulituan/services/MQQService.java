package com.example.liumangdulituan.services;

import java.util.ArrayList;
import java.util.List;

import com.example.liumangdulituan.utils.DrawerToast;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.ImageView;
import android.widget.Toast;

public class MQQService extends AccessibilityService {

    private boolean mNeedBack;

    private AccessibilityNodeInfo rootNodeInfo;

    private static final String WECHAT_VIEW_OTHERS_CH = "点击拆开";

    private static final String WECHAT_VIEW_DETAILS = "查看详情";

    private static final String KL_WECHAT_VIEW_OTHERS_CH = "口令红包";

    private static final String TX_WECHAT_VIEW_OTHERS_CH = "点击输入口令";

    private static final String FS_WECHAT_VIEW_OTHERS_CH = "发送";

    private static final int MAX_CACHE_TOLERANCE = 5000;

    private long mLogtime;

    private SharedPreferences pref;

    private String lastFetchedHongbaoId;// 最后一个

    private long lastFetchedTime;// 最后时间

    /**
     * AccessibilityEvent的回调方法
     * 
     * @param event
     *            事件
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

            accessibilityNodeInfo.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);

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
                                parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
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
                if ((hbid != null && !hbid.equals(lastFetchedHongbaoId)) || (now - lastFetchedTime) > MAX_CACHE_TOLERANCE) {
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

        List<AccessibilityNodeInfo> nodes3 = this.findAccessibilityNodeInfosByTexts(this.rootNodeInfo, new String[] {
                "红包记录", "来晚一步，红包被领完了", "已存入余额" });
        if (!nodes3.isEmpty()) {
            mNeedBack = true;
        }

        if (mNeedBack) {
            performGlobalAction(GLOBAL_ACTION_BACK);
            mNeedBack = false;
        }

        showToast("实时监控中...");

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

    /**
     * 将节点对象的id和hb上的内容合并 用于表示一个唯一的hb
     */
    private String getHBText(AccessibilityNodeInfo node) {
        /* 获取hb上的文本 */
        String content = null;
        try {
            AccessibilityNodeInfo i = node.getParent();
            for (int j = 0; j < i.getChildCount(); j++) {
                Log.i("MyError", "content");
                content += i.getChild(j).getText().toString();
            }
            Log.i("MyError", content);
        } catch (NullPointerException npe) {
            Log.i("MyError", "异常null");
            return null;
        }

        return content;
    }

}
