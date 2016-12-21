package com.suiyueshentou.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import com.suiyueshentou.R;
import com.suiyueshentou.base.baseinterface.BaseResponseListener;
import com.suiyueshentou.common.dialog.LoadingDialog;
import com.suiyueshentou.common.dialog.WarnDiaLog;
import com.suiyueshentou.utils.UpdateUtils;

import org.json.JSONObject;




public class SettingsActivity extends PreferenceActivity {
//    //是否下载提示窗
//    private WarnDiaLog mWarnDialog;
//    //加载框
//    private LoadingDialog mLoadingDialog;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        loadUI();
//        setPrefListeners();
//    }
//
//    private void setPrefListeners() {
//        // Check for updates
//        Preference updatePref = findPreference("pref_etc_check_update");
//        updatePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//            public boolean onPreferenceClick(Preference preference) {
//                UpdateUtils.getInstance().requestUpdate(SettingsActivity.this, new BaseResponseListener<JSONObject>() {
//                    @Override
//                    public void OnRequestStart() {
//                        showLoadingDialog();
//                    }
//
//                    @Override
//                    public void OnRequestSuccess(JSONObject release) {//注意这里不是UI线程，应该按照hongyang的逻辑进行封装一下
//                        dismissLoadingDialog();
//                        try {
//                            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
//                            String version = pInfo.versionName;
//
//                            final String latestVersion = release.getString("tag_name");
//                            boolean isPreRelease = release.getBoolean("prerelease");
//                            if (!isPreRelease && version.compareToIgnoreCase(latestVersion) >= 0) {
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        Toast.makeText(SettingsActivity.this, R.string.update_already_latest, Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//                            } else {//需要更新
//                                final String downloadUrl = release.getJSONArray("assets").getJSONObject(0).getString("browser_download_url");
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        mWarnDialog = new WarnDiaLog(SettingsActivity.this, "检测到新版本", "取消", "立即下载", new WarnDiaLog.ClickListenerInterface() {
//                                            @Override
//                                            public void doLeft() {
//                                                mWarnDialog.dismiss();
//                                            }
//
//                                            @Override
//                                            public void doRight() { //开启下载
//                                                // 判断SD卡是否存在，并且是否具有读写权限
//                                                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//                                                    UpdateUtils.getInstance().downloadApp(SettingsActivity.this, downloadUrl);
//                                                } else {
//                                                    Toast.makeText(SettingsActivity.this, "读取sd卡失败", Toast.LENGTH_SHORT).show();
//                                                }
//
//                                            }
//                                        });
//                                        if (mWarnDialog != null && !mWarnDialog.isShowing()) {
//                                            mWarnDialog.show();
//                                        }
//                                    }
//                                });
//
//                                //原来采取的浏览器下载  体验不好
////                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(downloadUrl));
////                                browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                                startActivity(browserIntent);
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//
//                    }
//
//                    @Override
//                    public void OnRequestError(Exception error) {
//                        dismissLoadingDialog();
//                    }
//                });//检查是否有更新
//                return false;
//            }
//        });
//
//        // Open issue
//        Preference issuePref = findPreference("pref_etc_issue");
//        issuePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//            public boolean onPreferenceClick(Preference preference) {
//                Toast.makeText(SettingsActivity.this, "暂不稳定，下一版本添加", Toast.LENGTH_LONG).show();
//                return false;
//            }
//        });
//    }
//
//    private void loadUI() {
//        addPreferencesFromResource(R.xml.preferences);
//        // Get rid of the fucking additional padding
//        getListView().setPadding(0, 0, 0, 0);
//        getListView().setBackgroundColor(0xfffaf6f1);
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//    }
//
//    public void performBack(View view) {
//        super.onBackPressed();
//    }
//
//    public void enterAccessibilityPage(View view) {
//        Intent mAccessibleIntent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
//        startActivity(mAccessibleIntent);
//    }
//
//
//    public void showLoadingDialog() {
//        if (mLoadingDialog == null) {
//            mLoadingDialog = new LoadingDialog(this);
//        }
//        if (!mLoadingDialog.isShowing()) {
//            mLoadingDialog.show();
//        }
//    }
//
//    public void dismissLoadingDialog() {
//        if (mLoadingDialog != null) {
//            mLoadingDialog.dismiss();
//        }
//    }

}
