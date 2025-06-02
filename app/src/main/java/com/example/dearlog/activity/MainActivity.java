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

    private ImageButton darkModeBtn, settingsBtn;
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 뷰 연결
        darkModeBtn = findViewById(R.id.dark_mode_button);
        settingsBtn = findViewById(R.id.settings_button);
        bottomNav = findViewById(R.id.bottom_navigation);

        // 다크모드 버튼 클릭 이벤트
        // MainActivity.java 안 onCreate()에서, 람다(->) 대신 익명 클래스로 작성한 다크모드 토글 부분만

        darkModeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 현재 저장된 모드 불러오기
                String current = ThemeUtil.modLoad(MainActivity.this);

                // 반대 모드로 적용
                if (ThemeUtil.DARK_MODE.equals(current)) {
                    ThemeUtil.applyTheme(MainActivity.this, ThemeUtil.LIGHT_MODE);
                } else {
                    ThemeUtil.applyTheme(MainActivity.this, ThemeUtil.DARK_MODE);
                }
            }
        });

        // 설정 버튼 클릭 이벤트
        settingsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        // 하단 네비게이션 메뉴 클릭 이벤트
        bottomNav.setOnItemSelectedListener(item -> {
            // 프래그먼트 전환 또는 기능 연결 (구현 예정)
            return true;
        });
    }
}
