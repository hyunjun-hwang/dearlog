package com.example.dearlog.activity;

import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import com.example.dearlog.R;
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
        darkModeBtn.setOnClickListener(v -> {
            // 다크모드 전환 기능 (구현 예정)
        });

        // 설정 버튼 클릭 이벤트
        settingsBtn.setOnClickListener(v -> {
            // 설정 화면 이동 기능 (구현 예정)
        });

        // 하단 네비게이션 메뉴 클릭 이벤트
        bottomNav.setOnItemSelectedListener(item -> {
            // 프래그먼트 전환 또는 기능 연결 (구현 예정)
            return true;
        });
    }
}
