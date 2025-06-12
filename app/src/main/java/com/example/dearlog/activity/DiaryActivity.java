package com.example.dearlog.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dearlog.R;
import com.example.dearlog.util.ThemeUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DiaryActivity extends AppCompatActivity {
    private BottomNavigationView bottomNav;
    private ImageButton darkModeBtn;
    private ImageButton settingsBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_design);
        bottomNav = findViewById(R.id.bottom_navigation);
        darkModeBtn = findViewById(R.id.dark_mode_button);
        settingsBtn = findViewById(R.id.settings_button);

        // 다크모드 전환
        darkModeBtn.setOnClickListener(v -> {
            String current = ThemeUtil.loadMode(DiaryActivity.this);
            if (ThemeUtil.DARK_MODE.equals(current)) {
                ThemeUtil.applyTheme(DiaryActivity.this, ThemeUtil.LIGHT_MODE);
            } else {
                ThemeUtil.applyTheme(DiaryActivity.this, ThemeUtil.DARK_MODE);
            }
            recreate();
        });

        // 일기목록 전환
        ImageView diaryIcon = findViewById(R.id.diary_icon);
        diaryIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DiaryActivity.this, DiaryListActivity.class);
                startActivity(intent);
            }
        });
        
        // 설정화면 이동
        settingsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(DiaryActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        setupBottomNavigation(); // Fix: initialize bottom navigation
    }
    private void setupBottomNavigation() {
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                startActivity(new Intent(this, MainActivity.class));
                return true;
            } else if (itemId == R.id.nav_book) {
                startActivity(new Intent(this, DiaryActivity.class));
                return true;
            } else if (itemId == R.id.nav_emotion) {
                startActivity(new Intent(this, EmotionSummaryActivity.class));
                return true;
            } else if (itemId == R.id.nav_calendar) {
                startActivity(new Intent(this, DiaryActivity.class));
                return true;
            }

            return false;
        });
    }
}
