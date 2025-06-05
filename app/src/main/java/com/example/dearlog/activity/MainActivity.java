package com.example.dearlog.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.TextView;
import com.example.dearlog.dialog.ConnectFriendDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dearlog.R;
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
        ThemeUtil.applyTheme(this, ThemeUtil.loadMode(this));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 뷰 연결
        darkModeBtn = findViewById(R.id.dark_mode_button);
        settingsBtn = findViewById(R.id.settings_button);
        bottomNav = findViewById(R.id.bottom_navigation);
        connectText = findViewById(R.id.connect_text);
        exclamationIcon = findViewById(R.id.exclamation_icon);

        connectText.setOnClickListener(v -> {
            if (!isFriendConnected) {
                // 친구 연결 다이얼로그 띄우기
                ConnectFriendDialog dialog = new ConnectFriendDialog(this, friendId -> {
                    // 친구 ID 받았을 때 UI 변경
                    connectText.setText("교환일기를\n만들어주세요!");
                    exclamationIcon.setText("?");

                    isFriendConnected = true; // 친구 연결 상태 저장
                });
                dialog.show();
            } else {
                // 친구 연결되어 있으면 교환일기 생성 다이얼로그 띄우기
                CreateDiaryDialog diaryDialog = new CreateDiaryDialog(this, (title, colorHex) ->{
                    // 교환일기 생성 후 처리 (ex: 저장, 이동 등)
                    Toast.makeText(this, "제목: " + title + "\n색상: " + colorHex, Toast.LENGTH_LONG).show();
                });
                diaryDialog.show();
            }
        });
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
        setupBottomNavigation();

    }
    // 하단 네비게이션 클릭 이벤트
    private void setupBottomNavigation() {
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                recreate(); // 현재 페이지 새로고침
                return true;
            } else if (itemId == R.id.nav_book) {
                Toast.makeText(this, "디자인 기능은 아직 미구현입니다", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.nav_emotion) {
                startActivity(new Intent(this, EmotionSummaryActivity.class));
                return true;
            } else if (itemId == R.id.nav_calendar) {
                Toast.makeText(this, "캘린더 기능은 아직 미구현입니다", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });
    }
}