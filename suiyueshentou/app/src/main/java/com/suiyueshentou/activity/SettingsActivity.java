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
import com.suiyueshentou.utils.UpdateUtils;

import org.json.JSONObject;

import okhttp3.Request;


public class SettingsActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadUI();
        setPrefListeners();
    }

    private void setPrefListeners() {
        // Check for updates
        Preference updatePref = findPreference("pref_etc_check_update");
        updatePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                UpdateUtils.getInstance().requestUpdate(SettingsActivity.this, new BaseResponseListener<JSONObject>() {
                    @Override
                    public void OnRequestStart() {

                    }

                    @Override
                    public void OnRequestSuccess(JSONObject release) {
                        try {
                            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                            String version = pInfo.versionName;

                            final String latestVersion = release.getString("tag_name");
                            boolean isPreRelease = release.getBoolean("prerelease");
                            if (!isPreRelease && version.compareToIgnoreCase(latestVersion) >= 0) {
                                Toast.makeText(SettingsActivity.this, R.string.update_already_latest, Toast.LENGTH_SHORT).show();
                            } else {//需要更新
                                String downloadUrl = release.getJSONArray("assets").getJSONObject(0).getString("browser_download_url");
                                //开启下载

                                // 判断SD卡是否存在，并且是否具有读写权限
                                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                                    UpdateUtils.getInstance().downloadApp(SettingsActivity.this,downloadUrl);
                                }else{

                                }
//                                // Give up on the fucking DownloadManager. The downloaded apk
//                                // got renamed and unable to install. Fuck.
//                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(downloadUrl));
//                                browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(browserIntent);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void OnRequestInterfaceError(String errorMsg) {

                    }

                    @Override
                    public void OnRequestOtherError(Exception error) {

                    }
                });//检查是否有更新
                return false;
            }
        });

        // Open issue
        Preference issuePref = findPreference("pref_etc_issue");
        issuePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                Toast.makeText(SettingsActivity.this, "暂不稳定，下一版本添加", Toast.LENGTH_LONG).show();
//                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri
//                        .parse("https://github.com/geeeeeeeeek/WeChatLuckyMoney/issues"));
//                browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                SettingsActivity.this.startActivity(browserIntent);
                return false;
            }
        });
    }

    private void loadUI() {
        addPreferencesFromResource(R.xml.preferences);

        // Get rid of the fucking additional padding
        getListView().setPadding(0, 0, 0, 0);
        getListView().setBackgroundColor(0xfffaf6f1);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void performBack(View view) {
        super.onBackPressed();
    }

    public void enterAccessibilityPage(View view) {
        Intent mAccessibleIntent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        startActivity(mAccessibleIntent);
    }


}
