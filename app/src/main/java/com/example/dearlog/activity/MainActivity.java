package com.example.dearlog.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dearlog.R;
import com.example.dearlog.dialog.ConnectFriendDialog;
import com.example.dearlog.dialog.CreateDiaryDialog;
import com.example.dearlog.util.ThemeUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private ImageButton darkModeBtn;
    private ImageButton settingsBtn;
    private BottomNavigationView bottomNav;
    private TextView connectText;
    private TextView exclamationIcon;
    private boolean isFriendConnected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 테마 적용
        ThemeUtil.applyTheme(this, ThemeUtil.loadMode(this));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 뷰 연결
        darkModeBtn = findViewById(R.id.dark_mode_button);
        settingsBtn = findViewById(R.id.settings_button);
        bottomNav = findViewById(R.id.bottom_navigation);
        connectText = findViewById(R.id.connect_text);
        exclamationIcon = findViewById(R.id.exclamation_icon);

        // 친구 연결 텍스트 클릭 시 다이얼로그 연결
        connectText.setOnClickListener(v -> {
            if (!isFriendConnected) {
                showFriendConnectDialog();
            } else {
                showCreateDiaryDialog();
            }
        });

        // 다크모드 전환
        darkModeBtn.setOnClickListener(v -> {
            String current = ThemeUtil.loadMode(MainActivity.this);
            if (ThemeUtil.DARK_MODE.equals(current)) {
                ThemeUtil.applyTheme(MainActivity.this, ThemeUtil.LIGHT_MODE);
            } else {
                ThemeUtil.applyTheme(MainActivity.this, ThemeUtil.DARK_MODE);
            }
            recreate();
        });

        // 설정화면 이동
        settingsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        // 하단 네비게이션 설정
        setupBottomNavigation();

        // 앱 실행 시 자동으로 친구 연결 다이얼로그 표시
        if (!isFriendConnected) {
            showFriendConnectDialog();
        }
    }

    private void showFriendConnectDialog() {
        ConnectFriendDialog dialog = new ConnectFriendDialog(this, friendId -> {
            connectText.setText("교환일기를\n만들어주세요!");
            exclamationIcon.setText("?");
            isFriendConnected = true;

            // 친구 연결 후 바로 교환일기 생성 다이얼로그 띄우기
            showCreateDiaryDialog();
        });
        dialog.show();
    }

    private void showCreateDiaryDialog() {
        CreateDiaryDialog diaryDialog = new CreateDiaryDialog(this, (title, colorHex) -> {
            // 일기 작성 화면으로 전환
            Intent intent = new Intent(MainActivity.this, WriteDiaryActivity.class);
            intent.putExtra("title", title);
            intent.putExtra("color", colorHex);
            startActivity(intent);
        });
        diaryDialog.show();
    }

    private void setupBottomNavigation() {
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                recreate(); // 홈 새로고침
                return true;
            } else if (itemId == R.id.nav_book) {
                Toast.makeText(this, "디자인 기능은 아직 미구현입니다", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.nav_emotion) {
                startActivity(new Intent(this, EmotionSummaryActivity.class));
                return true;
            } else if (itemId == R.id.nav_calendar) {
                Intent intent = new Intent(this, CalendarActivity.class);
                startActivity(intent);
                return true;
            }

            return false;
        });
    }
}
