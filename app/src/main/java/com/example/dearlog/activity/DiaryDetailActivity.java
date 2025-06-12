package com.example.dearlog.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
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

public class DiaryDetailActivity extends AppCompatActivity {

    private ImageButton btnBack, btnMenu;
    private ImageView imgEmotionColor;
    private TextView tvDate, tvQuestion, tvContent;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_detail);

        // View 초기화
        btnBack = findViewById(R.id.btn_back);
        btnMenu = findViewById(R.id.btn_menu);
        imgEmotionColor = findViewById(R.id.img_emotion_color);
        tvDate = findViewById(R.id.tv_diary_date);
        tvQuestion = findViewById(R.id.tv_question);
        tvContent = findViewById(R.id.tv_diary_content);
        queue = Volley.newRequestQueue(this);

        // 뒤로가기 버튼 이벤트
        btnBack.setOnClickListener(v -> finish());

        // 1. entry_id가 넘어온 경우: 서버에서 직접 조회
        int entryId = getIntent().getIntExtra("entry_id", -1);
        if (entryId != -1) {
            loadEntryDetailFromServer(entryId);
            return;
        }

        // 2. 기존 방식: 인텐트로 전달받은 값 추출
        String emotion = getIntent().getStringExtra("emotion"); // 예: "yellow"
        String date = getIntent().getStringExtra("date");       // 예: "2025.06.04"
        String question = getIntent().getStringExtra("question");
        String content = getIntent().getStringExtra("content");

        // 값 세팅
        tvDate.setText(date + " 작성됨");
        tvQuestion.setText(question);
        tvContent.setText(content);

        // 감정 색상에 따라 이미지 변경
        int emotionResId = R.drawable.color_circle_yellow; // 기본값
        if ("orange".equals(emotion)) emotionResId = R.drawable.color_circle_orange;
        else if ("blue".equals(emotion)) emotionResId = R.drawable.color_circle_blue;
        else if ("red".equals(emotion)) emotionResId = R.drawable.color_circle_red;
        imgEmotionColor.setBackgroundResource(emotionResId);
    }

    // 서버에서 entry_id 기반 일기 상세 불러오기
    private void loadEntryDetailFromServer(int entryId) {
        String url = "http://10.0.2.2:8080/getEntryById.jsp?entry_id=" + entryId;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if (response.length() > 0) {
                            JSONObject obj = response.getJSONObject(0);
                            String date = obj.getString("date");
                            String question = obj.getString("question");
                            String content = obj.getString("content");
                            String color = obj.getString("color");

                            tvDate.setText(date + " 작성됨");
                            tvQuestion.setText(question);
                            tvContent.setText(content);

                            try {
                                imgEmotionColor.setColorFilter(Color.parseColor(color));
                            } catch (IllegalArgumentException e) {
                                imgEmotionColor.setColorFilter(Color.LTGRAY);
                            }
                        }
                    } catch (JSONException e) {
                        Toast.makeText(this, "데이터 파싱 오류", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "서버 요청 실패", Toast.LENGTH_SHORT).show()
        );

        queue.add(request);
    }
}
