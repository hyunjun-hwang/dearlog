package com.example.dearlog.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dearlog.R;
import com.example.dearlog.dialog.SelectEmotionDialog;
import com.example.dearlog.model.Emotion;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class WriteDiaryActivity extends AppCompatActivity {

    private TextView tvDate, tvQuestion, tvDiaryTitle, tvSelectedEmotion;
    private EditText etDiaryContent;
    private LinearLayout emotionSelector;
    private ImageButton btnBack, btnMenu;
    private Button btnFinish;

    private Emotion selectedEmotion; // 감정 선택 결과
    private int questionId = -1;     // 질문 ID 저장

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_diary);

        // 뷰 바인딩
        tvDate = findViewById(R.id.tvDate);
        tvQuestion = findViewById(R.id.tvQuestion);
        tvDiaryTitle = findViewById(R.id.tvDiaryTitle);
        tvSelectedEmotion = findViewById(R.id.tvSelectedEmotion);
        etDiaryContent = findViewById(R.id.etDiaryContent);
        emotionSelector = findViewById(R.id.emotionSelector);
        btnBack = findViewById(R.id.btnBack);
        btnMenu = findViewById(R.id.btnMenu);
        btnFinish = findViewById(R.id.btnFinish);

        // 인텐트에서 다이어리 제목/색상 받아오기
        String title = getIntent().getStringExtra("title");
        String color = getIntent().getStringExtra("color");

        if (title != null) {
            tvDiaryTitle.setText("📒 " + title);
        }
        if (color != null) {
            try {
                emotionSelector.setBackgroundColor(Color.parseColor(color));
            } catch (IllegalArgumentException e) {
                emotionSelector.setBackgroundColor(Color.LTGRAY);
            }
        }

        // 날짜 출력
        String today = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault()).format(new Date());
        tvDate.setText(today + " 작성일");

        // 질문 불러오기
        loadTodayQuestion();

        // 감정 선택 다이얼로그
        emotionSelector.setOnClickListener(v -> {
            SelectEmotionDialog dialog = new SelectEmotionDialog(this);
            dialog.setOnEmotionSelectedListener(emotion -> {
                selectedEmotion = emotion;

                View colorCircle = findViewById(R.id.color_circle);
                if (colorCircle != null) {
                    try {
                        colorCircle.setBackgroundColor(Color.parseColor(emotion.getColor()));
                    } catch (IllegalArgumentException e) {
                        colorCircle.setBackgroundColor(Color.LTGRAY);
                    }
                }

                if (tvSelectedEmotion != null) {
                    tvSelectedEmotion.setText(emotion.getEmoji() + " " + emotion.getName());
                }
            });
            dialog.show();
        });

        // 뒤로가기
        btnBack.setOnClickListener(v -> finish());

        // 메뉴 버튼 안내
        btnMenu.setOnClickListener(v -> {
            Toast.makeText(this, "메뉴 기능은 추후 구현 예정입니다", Toast.LENGTH_SHORT).show();
        });

        // 작성 완료
        btnFinish.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("일기 저장")
                    .setMessage("일기를 저장할까요?")
                    .setPositiveButton("네", (dialog, which) -> saveDiary())
                    .setNegativeButton("아니요", null)
                    .show();
        });
    }

    private void loadTodayQuestion() {
        String url = "http://10.0.2.2:8080/DearlogServer/getQuestion.jsp"; // 개발환경 주소

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        String question = response.getString("question_text");
                        questionId = response.getInt("question_id");
                        tvQuestion.setText("오늘의 질문:\n" + question);
                    } catch (JSONException e) {
                        tvQuestion.setText("질문을 불러오는 데 실패했습니다.");
                    }
                },
                error -> {
                    tvQuestion.setText("서버 연결 실패. 인터넷 상태를 확인해주세요.");
                });

        queue.add(request);
    }

    private void saveDiary() {
        String content = etDiaryContent.getText().toString().trim();
        String date = tvDate.getText().toString().replace(" 작성일", "");
        String title = tvDiaryTitle.getText().toString().replace("📒 ", "");
        String emotionCode = (selectedEmotion != null) ? selectedEmotion.getCode() : "NONE";
        String userId = "test_user"; // 추후 SharedPreferences로 대체

        if (content.isEmpty()) {
            Toast.makeText(this, "일기 내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "http://10.0.2.2:8080/DearlogServer/insertDiary.jsp";

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    if (response.contains("success")) {
                        Toast.makeText(this, "일기가 저장되었습니다!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, DiaryListActivity.class));
                        finish();
                    } else {
                        Toast.makeText(this, "서버 저장 실패", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Toast.makeText(this, "서버 연결 오류", Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", userId);
                params.put("title", title);
                params.put("question_id", String.valueOf(questionId));
                params.put("emotion_code", emotionCode);
                params.put("content", content);
                return params;
            }
        };

        queue.add(request);
    }
}
