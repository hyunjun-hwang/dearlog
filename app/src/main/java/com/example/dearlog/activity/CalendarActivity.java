package com.example.dearlog.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.dearlog.R;
import com.example.dearlog.util.ThemeUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class CalendarActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private ImageButton darkModeBtn, settingsBtn;
    private BottomNavigationView bottomNav;
    private int diaryId = -1;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);

        calendarView = findViewById(R.id.calendarView);
        darkModeBtn = findViewById(R.id.btn_dark_mode);
        settingsBtn = findViewById(R.id.btn_settings);
        bottomNav = findViewById(R.id.bottom_navigation);

        queue = Volley.newRequestQueue(this);

        // SharedPreferences에서 user_id 꺼내기
        SharedPreferences prefs = getSharedPreferences("DearlogPrefs", MODE_PRIVATE);
        String userId = prefs.getString("user_id", null);

        if (userId == null) {
            Toast.makeText(this, "로그인 정보가 없습니다.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // diary_id 조회
        String diaryUrl = "http://10.0.2.2:8080/getDiaryId.jsp?user_id=" + userId;
        JsonObjectRequest diaryRequest = new JsonObjectRequest(
                Request.Method.GET,
                diaryUrl,
                null,
                response -> {
                    try {
                        if (response.getString("status").equals("success")) {
                            diaryId = response.getInt("diary_id");
                        } else {
                            Toast.makeText(this, "다이어리 정보를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(this, "JSON 파싱 오류", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "다이어리 ID 조회 실패", Toast.LENGTH_SHORT).show()
        );
        queue.add(diaryRequest);

        // 날짜 클릭 이벤트
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            if (diaryId == -1) {
                Toast.makeText(this, "다이어리 정보를 불러오는 중입니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            String selectedDate = String.format(Locale.KOREA, "%04d-%02d-%02d", year, month + 1, dayOfMonth);
            String entryUrl = "http://10.0.2.2:8080/getCalendarEntryId.jsp?diary_id=" + diaryId + "&date=" + selectedDate;

            JsonObjectRequest entryRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    entryUrl,
                    null,
                    entryResponse -> {
                        try {
                            if (entryResponse.getString("status").equals("success")) {
                                JSONArray entryArray = entryResponse.getJSONArray("entries");
                                ArrayList<Integer> entryIds = new ArrayList<>();
                                for (int i = 0; i < entryArray.length(); i++) {
                                    int entryId = entryArray.getJSONObject(i).getInt("entry_id");
                                    entryIds.add(entryId);
                                }

                                // DiaryListActivity로 entry_id 배열 넘김
                                Intent intent = new Intent(CalendarActivity.this, DiaryListActivity.class);
                                intent.putExtra("entry_ids", entryIds);
                                startActivity(intent);
                            } else {
                                Toast.makeText(this, "해당 날짜의 일기가 없습니다.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(this, "일기 ID 파싱 오류", Toast.LENGTH_SHORT).show();
                        }
                    },
                    error -> Toast.makeText(this, "엔트리 ID 조회 실패", Toast.LENGTH_SHORT).show()
            );
            queue.add(entryRequest);
        });

        // 다크모드 전환
        darkModeBtn.setOnClickListener(v -> {
            String current = ThemeUtil.loadMode(CalendarActivity.this);
            if (ThemeUtil.DARK_MODE.equals(current)) {
                ThemeUtil.applyTheme(CalendarActivity.this, ThemeUtil.LIGHT_MODE);
            } else {
                ThemeUtil.applyTheme(CalendarActivity.this, ThemeUtil.DARK_MODE);
            }
            recreate();
        });

        // 설정 화면 이동
        settingsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(CalendarActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        // 하단 네비게이션 설정
        setupBottomNavigation();
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
                recreate();
                return true;
            }

            return false;
        });
    }
}