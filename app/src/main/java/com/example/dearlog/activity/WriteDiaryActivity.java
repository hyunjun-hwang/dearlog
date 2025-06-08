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
import com.android.volley.toolbox.Volley;
import com.example.dearlog.R;
import com.example.dearlog.dialog.SelectEmotionDialog;
import com.example.dearlog.model.Emotion;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WriteDiaryActivity extends AppCompatActivity {

    private TextView tvDate, tvQuestion, tvDiaryTitle;
    private EditText etDiaryContent;
    private LinearLayout emotionSelector;
    private ImageButton btnBack, btnMenu;
    private Button btnFinish;

    private Emotion selectedEmotion; // 선택된 감정 저장용 변수

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_diary);

        // 1. XML 요소 연결
        tvDate = findViewById(R.id.tvDate);
        tvQuestion = findViewById(R.id.tvQuestion);
        tvDiaryTitle = findViewById(R.id.tvDiaryTitle);
        etDiaryContent = findViewById(R.id.etDiaryContent);
        emotionSelector = findViewById(R.id.emotionSelector);
        btnBack = findViewById(R.id.btnBack);
        btnMenu = findViewById(R.id.btnMenu);
        btnFinish = findViewById(R.id.btnFinish); // 작성 완료 버튼

        // 2. 인텐트에서 제목, 색상 값 받아오기
        String title = getIntent().getStringExtra("title");
        String color = getIntent().getStringExtra("color");

        if (tvDiaryTitle != null && title != null) {
            tvDiaryTitle.setText("📒 " + title);
        }
        if (emotionSelector != null && color != null) {
            try {
                emotionSelector.setBackgroundColor(Color.parseColor(color));
            } catch (IllegalArgumentException e) {
                emotionSelector.setBackgroundColor(Color.LTGRAY);
            }
        }

        // 3. 오늘 날짜 표시
        String today = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault()).format(new Date());
        tvDate.setText(today + " 작성일");

        // 4. 오늘의 질문 불러오기
        loadTodayQuestion();

        // 5. 감정 선택 다이얼로그 연결
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

                TextView tvSelectedEmotion = findViewById(R.id.tvSelectedEmotion);
                if (tvSelectedEmotion != null) {
                    tvSelectedEmotion.setText(emotion.getEmoji() + " " + emotion.getName());
                }
            });
            dialog.show();
        });

        // 6. 뒤로가기 버튼
        btnBack.setOnClickListener(v -> finish());

        // 7. 메뉴 버튼 (미구현 안내)
        btnMenu.setOnClickListener(v -> {
            Toast.makeText(this, "메뉴 기능은 추후 구현 예정", Toast.LENGTH_SHORT).show();
        });

        // 8. 작성 완료 버튼 → 다이얼로그 → 저장
        btnFinish.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("일기 저장")
                    .setMessage("일기를 저장할까요?")
                    .setPositiveButton("네", (dialog, which) -> saveDiary())
                    .setNegativeButton("아니요", null)
                    .show();
        });
    }

    /**
     * 오늘의 질문 서버에서 불러오기
     */
    private void loadTodayQuestion() {
        String url = "http://10.0.2.2:8080/getQuestion.jsp";

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        String question = response.getString("question");
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

    /**
     * 일기 저장 처리
     */
    private void saveDiary() {
        String content = etDiaryContent.getText().toString().trim();
        String date = tvDate.getText().toString().replace(" 작성일", "");
        String question = tvQuestion.getText().toString().replace("오늘의 질문:\n", "");
        String emotionCode = (selectedEmotion != null) ? selectedEmotion.getCode() : "NONE";

        if (content.isEmpty()) {
            Toast.makeText(this, "일기 내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO: 서버로 POST 요청 보내서 저장할 수 있음

        Toast.makeText(this, "일기가 저장되었습니다!", Toast.LENGTH_SHORT).show();

        // 이동
        Intent intent = new Intent(WriteDiaryActivity.this, DiaryListActivity.class);
        startActivity(intent);
        finish();
    }
}
