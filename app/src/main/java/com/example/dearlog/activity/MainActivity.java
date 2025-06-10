package com.example.dearlog.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dearlog.R;
import com.example.dearlog.dialog.ConnectFriendDialog;
import com.example.dearlog.dialog.CreateDiaryDialog;
import com.example.dearlog.util.ThemeUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private ImageButton darkModeBtn;
    private ImageButton settingsBtn;
    private BottomNavigationView bottomNav;
    private TextView connectText;
    private TextView exclamationIcon;
    private boolean isFriendConnected = false;
    private boolean isDiaryCreated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtil.applyTheme(this, ThemeUtil.loadMode(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        darkModeBtn = findViewById(R.id.dark_mode_button);
        settingsBtn = findViewById(R.id.settings_button);
        bottomNav = findViewById(R.id.bottom_navigation);
        connectText = findViewById(R.id.connect_text);
        exclamationIcon = findViewById(R.id.exclamation_icon);

        SharedPreferences prefs = getSharedPreferences("DearlogPrefs", MODE_PRIVATE);
        String friendId = prefs.getString("friend_id", null);
        isFriendConnected = (friendId != null);

        checkDiaryExists();

        connectText.setOnClickListener(v -> {
            if (!isFriendConnected) {
                showFriendConnectDialog();
            } else if (!isDiaryCreated) {
                showCreateDiaryDialog();
            } else {
                Toast.makeText(this, "이미 교환일기가 생성되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        darkModeBtn.setOnClickListener(v -> {
            String current = ThemeUtil.loadMode(MainActivity.this);
            ThemeUtil.applyTheme(MainActivity.this,
                    ThemeUtil.DARK_MODE.equals(current) ? ThemeUtil.LIGHT_MODE : ThemeUtil.DARK_MODE);
            recreate();
        });

        settingsBtn.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        });

        setupBottomNavigation();
    }

    private void checkDiaryExists() {
        SharedPreferences prefs = getSharedPreferences("DearlogPrefs", MODE_PRIVATE);
        String userId = prefs.getString("user_id", null);

        if (userId == null) return;

        String url = "http://10.0.2.2:8080/checkDiaryExists.jsp?user_id=" + userId;

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONObject json = new JSONObject(response);
                        boolean exists = json.getBoolean("exists");

                        if (exists) {
                            connectText.setText("교환일기가\n만들어졌어요!");
                            exclamationIcon.setText("!?");
                            isDiaryCreated = true;
                        } else if (isFriendConnected) {
                            connectText.setText("교환일기를\n만들어주세요!");
                            exclamationIcon.setText("?");
                        }
                    } catch (Exception e) {
                        Toast.makeText(this, "서버 응답 오류", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "서버 요청 실패", Toast.LENGTH_SHORT).show()
        );

        queue.add(request);
    }

    public void onFriendConnected() {
        connectText.setText("교환일기를\n만들어주세요!");
        exclamationIcon.setText("?");
        isFriendConnected = true;
        showCreateDiaryDialog();
    }

    private void showFriendConnectDialog() {
        ConnectFriendDialog dialog = new ConnectFriendDialog(this, this::onFriendConnected);
        dialog.show();
    }

    private void showCreateDiaryDialog() {
        CreateDiaryDialog diaryDialog = new CreateDiaryDialog(this, (title, colorHex) -> {
            Intent intent = new Intent(MainActivity.this, WriteDiaryActivity.class);
            intent.putExtra("title", title);
            intent.putExtra("color", colorHex);
            startActivity(intent);
            onDiaryCreated();
        });
        diaryDialog.show();
    }

    public void onDiaryCreated() {
        connectText.setText("교환일기가\n만들어졌어요!");
        exclamationIcon.setText("!?");
        isDiaryCreated = true;
    }

    private void setupBottomNavigation() {
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                recreate();
                return true;
            } else if (itemId == R.id.nav_book) {
                startActivity(new Intent(this, DiaryActivity.class));
                return true;
            } else if (itemId == R.id.nav_emotion) {
                startActivity(new Intent(this, EmotionSummaryActivity.class));
                return true;
            } else if (itemId == R.id.nav_calendar) {
                startActivity(new Intent(this, CalendarActivity.class));
                return true;
            }
            return false;
        });
    }
}