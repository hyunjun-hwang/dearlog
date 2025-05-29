package com.example.dearlog.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dearlog.R;

public class SettingsActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private Switch switchAlarm;
    private LinearLayout profileSetting;
    private LinearLayout alarmSetting;
    private LinearLayout themeSetting;
    private LinearLayout textStyleSetting;
    private LinearLayout screenLockSetting;
    private LinearLayout backupRestoreSetting;
    private LinearLayout pdfExport;
    private LinearLayout languageSetting;
    private LinearLayout friendManagementSetting;
    private TextView deleteAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // 뒤로가기 버튼
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 일기알림 스위치
        switchAlarm = findViewById(R.id.switch_alarm);
        switchAlarm.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(android.widget.CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(SettingsActivity.this, "일기알림 켜짐", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SettingsActivity.this, "일기알림 꺼짐", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 각 설정 항목
        profileSetting          = findViewById(R.id.profile_setting);
        alarmSetting            = findViewById(R.id.alarm_setting);
        themeSetting            = findViewById(R.id.theme_setting);
        textStyleSetting        = findViewById(R.id.text_style_setting);
        screenLockSetting       = findViewById(R.id.screen_lock_setting);
        backupRestoreSetting    = findViewById(R.id.backup_restore_setting);
        pdfExport               = findViewById(R.id.pdf_export);
        languageSetting         = findViewById(R.id.language_setting);
        friendManagementSetting = findViewById(R.id.friend_management_setting);
        deleteAccount           = findViewById(R.id.delete_account);

        profileSetting.setOnClickListener(itemClickListener("프로필 및 계정 관리"));
        alarmSetting.setOnClickListener(itemClickListener("일기알림"));
        themeSetting.setOnClickListener(itemClickListener("테마 설정"));
        textStyleSetting.setOnClickListener(itemClickListener("글자 스타일"));
        screenLockSetting.setOnClickListener(itemClickListener("화면 잠금"));
        backupRestoreSetting.setOnClickListener(itemClickListener("백업/복원"));
        pdfExport.setOnClickListener(itemClickListener("PDF 내보내기"));
        languageSetting.setOnClickListener(itemClickListener("언어 설정"));
        friendManagementSetting.setOnClickListener(itemClickListener("친구 관리"));
        deleteAccount.setOnClickListener(itemClickListener("계정 탈퇴"));
    }

    /**
     * 공통 클릭 리스너 생성 메서드.
     * @param label 표시할 텍스트
     * @return View.OnClickListener
     */
    private View.OnClickListener itemClickListener(final String label) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingsActivity.this, label + " 클릭됨", Toast.LENGTH_SHORT).show();
            }
        };
    }
}