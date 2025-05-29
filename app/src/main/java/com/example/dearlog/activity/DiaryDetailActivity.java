package com.example.dearlog.activity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dearlog.R;

public class DiaryDetailActivity extends AppCompatActivity {

    private ImageButton btnBack, btnMenu;
    private ImageView imgEmotionColor;
    private TextView tvDate, tvQuestion, tvContent;

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

        // 뒤로가기 버튼 이벤트
        btnBack.setOnClickListener(v -> finish());

        // 인텐트로 전달받은 값 추출
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
}
