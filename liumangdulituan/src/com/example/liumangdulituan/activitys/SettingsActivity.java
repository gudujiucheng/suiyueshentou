package com.example.liumangdulituan.activitys;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import com.example.liumangdulituan.R;


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
                Toast.makeText(SettingsActivity.this, "暂不稳定，下一版本添加",1).show();
//                new UpdateTask(getApplicationContext(), true).update();
                return false;
            }
        });

        // Open issue
        Preference issuePref = findPreference("pref_etc_issue");
        issuePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                Toast.makeText(SettingsActivity.this, "暂不稳定，下一版本添加",1).show();
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
