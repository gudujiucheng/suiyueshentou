package com.suiyueshentou.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import com.suiyueshentou.base.baseinterface.BaseResponseListener;
import com.suiyueshentou.common.net.MyUrl;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by zc on 2016/6/23.
 * 更新工具类
 */
public class UpdateUtils {

//    private static UpdateUtils instance = null;
//
//    public static UpdateUtils getInstance() {
//        if (instance == null) {
//            instance = new UpdateUtils();
//        }
//        return instance;
//    }
//
//
//    /**
//     * 检查更新
//     *
//     * @param context  上下文
//     * @param listener 回调
//     */
//    public void requestUpdate(final Context context, final BaseResponseListener<JSONObject> listener) {
//        listener.OnRequestStart();
//
//        //创建okHttpClient对象
//        OkHttpClient mOkHttpClient = new OkHttpClient();
//        //创建一个Request
//        final Request request = new Request.Builder()
//                .url(MyUrl.UPDATE_URL)
//                .build();
//        //new call
//        Call call = mOkHttpClient.newCall(request);
//        //请求加入调度
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                listener.OnRequestError(e);
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                try {
//                    String resp = response.body().string();
//                    DebugLog.e(DebugLog.TAG, resp);
//                    String s = new String(resp.getBytes(), "UTF-8");
//                    JSONObject release = new JSONObject(s);
//                    listener.OnRequestSuccess(release);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//
//    }
//
//
//    /**
//     * 执行下载 app
//     *
//     * @param downloadUrl //下载url
//     */
//    public void downloadApp(final Context context, String downloadUrl) {
//        //创建okHttpClient对象
//        OkHttpClient mOkHttpClient = new OkHttpClient();
//        //创建一个Request
//        Request request = new Request.Builder()
//                .url(downloadUrl)
//                .build();
//        mOkHttpClient.newCall(request).enqueue(new Callback() {
//                                                   @Override
//                                                   public void onFailure(Call call, IOException e) {
//
//                                                   }
//
//                                                   @Override
//                                                   public void onResponse(Call call, Response response) throws IOException {
//                                                       InputStream is = null;
//                                                       byte[] buf = new byte[2048];
//                                                       int len = 0;
//                                                       FileOutputStream fos = null;
//                                                       //获取存储卡路径
//                                                       String SDPath = Environment.getExternalStorageDirectory().getAbsolutePath();
//                                                       String mSavePath = SDPath + "/" + "download";
//                                                       File file = new File(mSavePath);
//                                                       // 判断文件目录是否存在
//                                                       if (!file.exists()) {
//                                                           file.mkdir();
//                                                       }
//
//                                                       try {
//                                                           is = response.body().byteStream();
//                                                           // 获取文件大小
//                                                           long total = response.body().contentLength();
//                                                           long freeSize = Utils.getSDFreeSize();
//                                                           if (freeSize < total) {
//                                                               //TODO  SD 卡存储空间不足   给提示
//                                                               return;
//                                                           }
//                                                           File apkFile = new File(mSavePath, "new_suiyueshentou.apk");
//                                                           fos = new FileOutputStream(apkFile);
//                                                           long sum = 0;
//                                                           while ((len = is.read(buf)) != -1) {
//                                                               fos.write(buf, 0, len);
//                                                               sum += len;
//                                                               int progress = (int) (sum * 1.0f / total * 100);
//                                                               DebugLog.e(DebugLog.TAG, "progress=" + progress);
//                                                           }
//                                                           fos.flush();
//                                                           installApk(context, apkFile);
//                                                           DebugLog.e(DebugLog.TAG, "文件下载成功");
//                                                       } catch (Exception e) {
//                                                           e.printStackTrace();
//                                                           DebugLog.e(DebugLog.TAG, "文件下载失败");
//                                                       } finally {
//                                                           try {
//                                                               if (is != null)
//                                                                   is.close();
//                                                           } catch (IOException e) {
//                                                           }
//                                                           try {
//                                                               if (fos != null)
//                                                                   fos.close();
//                                                           } catch (IOException e) {
//                                                           }
//                                                       }
//                                                   }
//
//
//                                               }
//
//        );
//
//    }
//
//
//    /**
//     * 安装 APK 文件
//     */
//    private void installApk(Context mContext, File apkfile) {
//
//        if (!apkfile.exists()) {
//            return;
//        }
//        // 通过Intent安装APK文件
//        Intent i = new Intent(Intent.ACTION_VIEW);
//        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
//        mContext.startActivity(i);
//    }
}
