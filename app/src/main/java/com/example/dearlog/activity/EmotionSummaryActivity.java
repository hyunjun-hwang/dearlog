package com.example.dearlog.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dearlog.R;
import com.example.dearlog.util.ThemeUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class EmotionSummaryActivity extends AppCompatActivity {

    private TextView tvMainTitle;
    private ImageButton btnDarkMode, btnSettings;
    private LinearLayout btnEmotionSummary, emotionFilterMenu;
    private TextView menuMyEmotions, menuFriendsEmotions;
    private GridLayout colorGrid;
    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtil.applyTheme(this, ThemeUtil.loadMode(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emotion_summary);

        // View 연결
        tvMainTitle = findViewById(R.id.tv_main_title);
        btnDarkMode = findViewById(R.id.btn_dark_mode);
        btnSettings = findViewById(R.id.btn_settings);
        btnEmotionSummary = findViewById(R.id.btn_emotion_summary);
        colorGrid = findViewById(R.id.color_grid);
        bottomNavigation = findViewById(R.id.bottom_navigation);

        // 감정 필터 메뉴 관련
        emotionFilterMenu = findViewById(R.id.emotion_filter_menu);
        menuMyEmotions = findViewById(R.id.menu_my_emotions);
        menuFriendsEmotions = findViewById(R.id.menu_friends_emotions);

        // 다크모드 토글
        btnDarkMode.setOnClickListener(v -> {
            String current = ThemeUtil.loadMode(this);
            if (ThemeUtil.DARK_MODE.equals(current)) {
                ThemeUtil.applyTheme(this, ThemeUtil.LIGHT_MODE);
            } else {
                ThemeUtil.applyTheme(this, ThemeUtil.DARK_MODE);
            }
            recreate(); // 테마 적용
        });

        // 설정 이동
        btnSettings.setOnClickListener(v -> {
            startActivity(new Intent(this, SettingsActivity.class));
        });

        // 감정별 모아보기 버튼 클릭 시 메뉴 토글
        btnEmotionSummary.setOnClickListener(v -> {
            if (emotionFilterMenu.getVisibility() == View.VISIBLE) {
                emotionFilterMenu.setVisibility(View.GONE);
            } else {
                emotionFilterMenu.setVisibility(View.VISIBLE);
            }
        });

        // 메뉴 항목 클릭 처리
        menuMyEmotions.setOnClickListener(v -> {
            emotionFilterMenu.setVisibility(View.GONE);
            Toast.makeText(this, "나의 감정만 보기", Toast.LENGTH_SHORT).show();
            // TODO: 필터링 로직 구현
        });

        menuFriendsEmotions.setOnClickListener(v -> {
            emotionFilterMenu.setVisibility(View.GONE);
            Toast.makeText(this, "친구 감정 보기", Toast.LENGTH_SHORT).show();
            // TODO: 필터링 로직 구현
        });

        setupColorGridClickListeners();
        setupBottomNavigation();

    }

    private void setupColorGridClickListeners() {
        for (int i = 0; i < colorGrid.getChildCount(); i++) {
            View colorCircle = colorGrid.getChildAt(i);
            colorCircle.setOnClickListener(v -> {
                String selectedColor = getColorHexFromView(v);
                Toast.makeText(this, "감정 색상 선택: " + selectedColor, Toast.LENGTH_SHORT).show();
                // 선택된 감정 색상만 표시, 화면 이동 없음
            });
        }
    }

    private String getColorHexFromView(View view) {
        if (view.getBackground() instanceof ColorDrawable) {
            int color = ((ColorDrawable) view.getBackground()).getColor();
            return String.format("#%06X", (0xFFFFFF & color));
        }
        return "#FFFFFF";
    }

    private void setupBottomNavigation() {
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                startActivity(new Intent(this, MainActivity.class));
                return true;
            } else if (itemId == R.id.nav_book) {
                Toast.makeText(this, "디자인 기능은 아직 미구현입니다", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.nav_emotion) {
                recreate(); // 현재 페이지 새로고침
                return true;
            } else if (itemId == R.id.nav_calendar) {
                Toast.makeText(this, "캘린더 기능은 아직 미구현입니다", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });
    }
}