package com.example.dearlog.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.dearlog.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class EmotionSummaryActivity extends AppCompatActivity {

    private TextView tvMainTitle;
    private LinearLayout btnEmotionSummary;
    private TextView menuMyEmotions, menuFriendsEmotions;

    private RequestQueue requestQueue;

    // 실제 프로젝트에서는 로그인 후 받아온 user_id로 대체
    private static final String USER_ID = "2020081031";
    private static final String SERVER_URL = "http://192.168.0.101:8080/dearlog/getEmotionStats.jsp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emotion_summary);

        tvMainTitle = findViewById(R.id.tv_main_title);
        btnEmotionSummary = findViewById(R.id.btn_emotion_summary);
        menuMyEmotions = findViewById(R.id.menu_my_emotions);
        menuFriendsEmotions = findViewById(R.id.menu_friends_emotions);

        requestQueue = Volley.newRequestQueue(this);

        // 감정 통계 불러오기
        loadEmotionStats();

        // 감정 모아보기 토글
        btnEmotionSummary.setOnClickListener(v -> {
            boolean visible = menuMyEmotions.getVisibility() == View.VISIBLE;
            menuMyEmotions.setVisibility(visible ? View.GONE : View.VISIBLE);
            menuFriendsEmotions.setVisibility(visible ? View.GONE : View.VISIBLE);
        });

        // 필터 메뉴 클릭 처리 (기능 확장 예정)
        menuMyEmotions.setOnClickListener(v -> {
            Toast.makeText(this, "나의 감정만 보기", Toast.LENGTH_SHORT).show();
        });

        menuFriendsEmotions.setOnClickListener(v -> {
            Toast.makeText(this, "친구 감정 보기", Toast.LENGTH_SHORT).show();
        });
    }

    private void loadEmotionStats() {
        String url = SERVER_URL + "?user_id=" + USER_ID;

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET, url, null,
                response -> {
                    HashMap<String, Integer> stats = new HashMap<>();
                    String mostEmotion = null;
                    int maxCount = -1;

                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            String emotionCode = obj.getString("emotion_code");
                            int count = obj.getInt("count");

                            stats.put(emotionCode, count);

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
}
