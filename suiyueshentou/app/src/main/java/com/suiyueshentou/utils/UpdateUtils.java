package com.suiyueshentou.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.widget.Toast;

import com.suiyueshentou.R;
import com.suiyueshentou.base.BaseActivity;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by zc on 2016/6/23.
 * 更新工具类
 */
public class UpdateUtils {
    public static final String updateUrl = "https://api.github.com/repos/gudujiucheng/suiyueshentou/releases/latest";

    private static UpdateUtils instance = null;

    public static UpdateUtils getInstance() {
        if (instance == null) {
            instance = new UpdateUtils();
        }
        return instance;
    }


    /**
     * 检查更新
     *
     * @param context           上下文
     * @param isUpdateOnRelease 只是提示，还是走更新逻辑   true ：走更新逻辑   false：仅仅提示
     */
    public void requestUpdate(final Context context, final boolean isUpdateOnRelease) {
        ((BaseActivity) context).showLoadingDialog();
        //创建okHttpClient对象
        OkHttpClient mOkHttpClient = new OkHttpClient();
//创建一个Request
        final Request request = new Request.Builder()
                .url("https://github.com/hongyangAndroid")
                .build();
//new call
        Call call = mOkHttpClient.newCall(request);
//请求加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ((BaseActivity) context).dismissLoadingDialog();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ((BaseActivity) context).dismissLoadingDialog();
                try {
                    JSONObject release = new JSONObject(response.body().string());

                    // Get current version
                    PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                    String version = pInfo.versionName;

                    String latestVersion = release.getString("tag_name");
                    boolean isPreRelease = release.getBoolean("prerelease");
                    if (!isPreRelease && version.compareToIgnoreCase(latestVersion) >= 0) {
                        // Your version is ahead of or same as the latest.
                        if (isUpdateOnRelease)
                            Toast.makeText(context, R.string.update_already_latest, Toast.LENGTH_SHORT).show();
                    } else {
                        if (!isUpdateOnRelease) {
                            Toast.makeText(
                                    context,
                                    context.getString(R.string.update_new_seg1) + latestVersion
                                            + context.getString(R.string.update_new_seg3), Toast.LENGTH_LONG).show();
                            return;
                        }
                        // Need update.
                        String downloadUrl = release.getJSONArray("assets").getJSONObject(0).getString("browser_download_url");

                        // Give up on the fucking DownloadManager. The downloaded apk
                        // got renamed and unable to install. Fuck.
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(downloadUrl));
                        browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(browserIntent);
                        Toast.makeText(
                                context,
                                context.getString(R.string.update_new_seg1) + latestVersion
                                        + context.getString(R.string.update_new_seg2), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (isUpdateOnRelease)
                        Toast.makeText(context, R.string.update_error, Toast.LENGTH_LONG).show();
                }
            }
        });


    }
}
