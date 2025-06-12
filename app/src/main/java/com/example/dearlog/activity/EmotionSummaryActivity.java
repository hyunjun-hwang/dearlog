package com.example.dearlog.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.dearlog.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class EmotionSummaryActivity extends AppCompatActivity {

    private TextView tvMainTitle;
    private LinearLayout btnEmotionSummary;
    private TextView menuMyEmotions, menuFriendsEmotions;
    private BottomNavigationView bottomNav;
    private RequestQueue requestQueue;

    private static final String SERVER_URL = "http://10.0.2.2:8080/dearlog/getEmotionStats.jsp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emotion_summary);

        // 1. View 연결
        tvMainTitle = findViewById(R.id.tv_main_title);
        btnEmotionSummary = findViewById(R.id.btn_emotion_summary);
        menuMyEmotions = findViewById(R.id.menu_my_emotions);
        menuFriendsEmotions = findViewById(R.id.menu_friends_emotions);
        bottomNav = findViewById(R.id.bottom_navigation);

        requestQueue = Volley.newRequestQueue(this);

        // 2. SharedPreferences에서 user_id 가져오기
        SharedPreferences prefs = getSharedPreferences("DearlogPrefs", MODE_PRIVATE);
        String userId = prefs.getString("user_id", null);

        if (userId == null) {
            Toast.makeText(this, "로그인 정보가 없습니다.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 3. 감정 통계 불러오기
        loadEmotionStats(userId);

        // 4. 감정 모아보기 토글
        btnEmotionSummary.setOnClickListener(v -> {
            boolean visible = menuMyEmotions.getVisibility() == View.VISIBLE;
            menuMyEmotions.setVisibility(visible ? View.GONE : View.VISIBLE);
            menuFriendsEmotions.setVisibility(visible ? View.GONE : View.VISIBLE);
        });

        menuMyEmotions.setOnClickListener(v ->
                Toast.makeText(this, "나의 감정만 보기", Toast.LENGTH_SHORT).show()
        );

        menuFriendsEmotions.setOnClickListener(v ->
                Toast.makeText(this, "친구 감정 보기", Toast.LENGTH_SHORT).show()
        );

        // 5. 바텀 네비게이션 설정
        setupBottomNavigation();
    }

    private void loadEmotionStats(String userId) {
        String url = SERVER_URL + "?user_id=" + userId;

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET, url, null,
                response -> {
                    String mostEmotion = null;
                    int maxCount = -1;

                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            String emotionCode = obj.getString("emotion_code");
                            int count = obj.getInt("count");

                            if (count > maxCount) {
                                maxCount = count;
                                mostEmotion = emotionCode;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    if (mostEmotion != null) {
                        tvMainTitle.setText("이번달은 '" + mostEmotion + "' 감정이\n제일 많아요");
                    } else {
                        tvMainTitle.setText("이번달 감정 데이터를 찾을 수 없어요.");
                    }
                },
                error -> {
                    Log.e("EmotionStats", "서버 요청 실패: " + error.toString());
                    Toast.makeText(this, "감정 통계를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                });

        requestQueue.add(request);
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
                recreate(); // 현재 화면 새로고침
                return true;
            } else if (itemId == R.id.nav_calendar) {
                startActivity(new Intent(this, CalendarActivity.class));
                return true;
            }
            return false;
        });
    }
}
