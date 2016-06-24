package com.suiyueshentou.utils;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Utils {

    /**
     * 吐司
     *
     * @param mToast
     * @param mActivity
     * @param content
     */
    public static void showToast(Toast mToast, Activity mActivity, String content) {
        if (mToast == null) {
            mToast = Toast.makeText(mActivity, content, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(content);
        }
        mToast.show();
    }

    public static void showToast(Context context, String content) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }

    /**
     * dp转px
     *
     * @param context
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }


    /**
     * px转dp
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * sp转dp
     *
     * @param context
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 获取应用版本名
     *
     * @param context
     * @return
     */
    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (TextUtils.isEmpty(versionName)) {
                return "";
            }
        } catch (Exception e) {
            DebugLog.e(DebugLog.TAG, "Version Exception ==== " + e);
        }
        return versionName;
    }

    /**
     * 验证手机号是否正确
     *
     * @param mobiles
     * @return
     */
    public static boolean checkMobileNO(String mobiles) {
//        String str = "^1[3|4|5|7|8][0-9]\\d{8}$";
        String regex = "^(13[0-9]|15[012356789]|17[013678]|18[0-9]|14[57])[0-9]{8}$";
        Pattern ps = Pattern.compile(regex);
        Matcher ms = ps.matcher(mobiles);
        return ms.matches();
    }


    /**
     * String 类型的 加法运算
     *
     * @param v1
     * @param v2
     * @return
     */
    public static String add(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);

        return b1.add(b2).toString();
    }

    /**
     * String类型 的乘法
     *
     * @param v1
     * @param v2
     * @return
     */
    public static String multiply(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);

        return b1.multiply(b2).toString();
    }

    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels; // 屏幕宽度（像素）
    }

    /**
     * 获取屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels; // 屏幕高度（像素）
    }

    /**
     * 将double精确到小数点后两位
     *
     * @param value
     * @return
     */
    public static String formatDouble2(double value) {
        BigDecimal decimal = new BigDecimal(value);
        decimal = decimal.setScale(2, BigDecimal.ROUND_HALF_UP);
        return "" + decimal;
    }

    /**
     * 用户昵称正则匹配
     *
     * @param content
     * @return
     */
    public static boolean nicknameRegex(String content) {
        String regex = "^([^\\x00-\\xff]|\\d|\\w|[-_]){4,20}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        return matcher.matches();
    }

    /**
     * 密码正则匹配
     *
     * @param password
     * @return
     */
    public static boolean passwordRegex(String password) {
        String regex = "^(\\d|[a-zA-Z]){6,18}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    /**
     * 应用程序是否已安装
     *
     * @param context
     * @param packageName 应用程序的包名
     * @return
     */
    public static boolean isInstalled(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> mPacks = pm.getInstalledPackages(0);
        for (PackageInfo info : mPacks) {
            if ((info.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 0) {
                if (packageName.equals(info.packageName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 设置webview属性
     */
    public static void setWebView(WebView webView) {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setUseWideViewPort(true);
        settings.setDomStorageEnabled(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(true);
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    /**
     * 获取 yyyy-MM-dd 格式的日期
     *
     * @param date
     * @return
     */
    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    /**
     * 身份证号校验的正则匹配
     *
     * @param number
     * @return true 表示合法
     * false 表示不合法
     */
    public static boolean checkIDcard(String number) {
        String regex = "\\d{17}[\\d|X]|\\d{15}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(number);
        return matcher.matches();
    }

    /**
     * 是否有网络
     *
     * @param context
     * @return true 有
     */
    public static boolean isConn(Context context) {
        boolean bisConnFlag = false;
        ConnectivityManager conManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = conManager.getActiveNetworkInfo();
        if (network != null) {
            bisConnFlag = conManager.getActiveNetworkInfo().isAvailable()
                    && conManager.getActiveNetworkInfo().isConnected();
        }
        return bisConnFlag;
    }

    /**
     * 获取状态栏高度
     *
     * @param mContext
     * @return
     */
    public static int getSysBarHeight(Context mContext) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0;
        int sbar = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = mContext.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return sbar;
    }

    /**
     * 计算折扣
     *
     * @param str1 现价
     * @param str2 原价
     * @return
     */
    public static String getDiscount(Double str1, Double str2) {
        double i = str1 /  str2 ;

        BigDecimal decimal = new BigDecimal(i * 10);
        decimal = decimal.setScale(1, BigDecimal.ROUND_HALF_UP);
        return "" + decimal + "折";
    }

    public static String getDiscount(Double str1) {

        BigDecimal decimal = new BigDecimal(str1);
        decimal = decimal.setScale(2, BigDecimal.ROUND_HALF_UP);
        return decimal + "";
    }

    public static String getDiscount2(Double str1) {

        BigDecimal decimal = new BigDecimal(str1);
        decimal = decimal.setScale(0, BigDecimal.ROUND_HALF_UP);
        return decimal + "";
    }

    /**
     * 接口返回的字符串数据是否为空的判断
     *
     * @param sequence
     * @return
     */
    public static boolean isEmpty(CharSequence sequence) {
        return TextUtils.isEmpty(sequence) || sequence.equals("null");
    }

    /**
     * 格式化id
     * 传递参数时，多个id间以‘，’分隔
     *
     * @param ids
     * @return
     */
    public static String formatIds(List<String> ids) {
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < ids.size(); i++) {
            sb.append(ids.get(i)).append(",");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }


    /**
     * 获取设备唯一标识码
     *
     * @param context
     * @return
     */
    public static String getUUID(Context context) {
        //注意需要添加权限 <uses-permission android:name="android.permission.READ_PHONE_STATE"/>
        String uniqueId = "";
        final TelephonyManager tm = (TelephonyManager) ((ContextWrapper) context).getBaseContext().getSystemService(
                Context.TELEPHONY_SERVICE);
        String tmDevice = tm.getDeviceId();//获取IME标识
        String tmSerial = tm.getSimSerialNumber();//获取sim卡的序号
        String androidId = android.provider.Settings.Secure.getString(((ContextWrapper) context).getContentResolver(),
                android.provider.Settings.Secure.ANDROID_ID);
        if (tmDevice == null) {
            tmDevice = "";
        }
        if (tmSerial == null) {
            tmSerial = "";
        }
        if (androidId == null) {
            androidId = "";
        }
        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        uniqueId = deviceUuid.toString();
        return uniqueId;
    }

    /**
     * 判断是否有中文
     *
     * @param str
     * @return
     */
    public static boolean isContainsChinese(String str) {
        String regEx = "[\u4e00-\u9fa5]";
        Pattern pat = Pattern.compile(regEx);
        Matcher matcher = pat.matcher(str);
        boolean flg = false;
        if (matcher.find()) {
            flg = true;
        }
        return flg;
    }

    /**
     * 将时间戳转为 yyyy-MM-dd 格式的字符串
     *
     * @param timestamp
     * @return
     */
    public static String getTimeFormat(long timestamp) {
        Date date = new Date(timestamp * 1000);
        return getTime(date);
    }

    /**
     * yyyy-MM-dd HH:mm:ss
     *
     * @param timestamp
     * @return
     */
    public static String getTimeFormat2(long timestamp) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String times = sdr.format(new Date(timestamp * 1000L));
        return times;

    }


    /**
     * 去除特殊字符或将所有中文标号替换为英文标号
     *
     * @param str
     * @return
     */
    public static String stringFilter(String str) {
        str = str.replaceAll("【", "[").replaceAll("】", "]")
                .replaceAll("！", "!").replaceAll("：", ":")
                .replace("《", "[").replace("》", "]");// 替换中文标号
        String regEx = "[『』]"; // 清除掉特殊字符
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    /**
     * 给字符串添加零前缀
     */
    public static String addZeroToStr(String string) {
        String result;
        int length = string.length();
        if (length == 0) {
            result = "00";
        } else if (length == 1) {
            result = "0" + string;
        } else {
            result = string;
        }
        return result;
    }


    /**
     * Drawable--->Bitmap
     *
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitamp(Drawable drawable) {
        Bitmap bitmap;
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        System.out.println("Drawable转Bitmap");
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
        bitmap = Bitmap.createBitmap(w, h, config);
        //注意，下面三行代码要用到，否在在View或者surfaceview里的canvas.drawBitmap会看不到图
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }
}
