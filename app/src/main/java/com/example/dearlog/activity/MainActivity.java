package com.example.dearlog.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dearlog.R;
import com.example.dearlog.util.ThemeUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private ImageButton darkModeBtn;
    private ImageButton settingsBtn;
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // ⬅️ 저장된 테마 먼저 적용
        ThemeUtil.applyTheme(this, ThemeUtil.loadMode(this));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 뷰 연결
        darkModeBtn = findViewById(R.id.dark_mode_button);
        settingsBtn = findViewById(R.id.settings_button);
        bottomNav = findViewById(R.id.bottom_navigation);

        // 다크모드 버튼 클릭
        darkModeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String current = ThemeUtil.loadMode(MainActivity.this);
                if (ThemeUtil.DARK_MODE.equals(current)) {
                    ThemeUtil.applyTheme(MainActivity.this, ThemeUtil.LIGHT_MODE);
                } else {
                    ThemeUtil.applyTheme(MainActivity.this, ThemeUtil.DARK_MODE);
                }
                recreate(); // 테마 적용 후 액티비티 재시작
            }
        });

        // 설정 화면 이동
        settingsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        // 하단 네비게이션 클릭 이벤트
        bottomNav.setOnItemSelectedListener(item -> {
            // TODO: 프래그먼트 전환 또는 액션 처리 예정
            return true;
        });
    }
}