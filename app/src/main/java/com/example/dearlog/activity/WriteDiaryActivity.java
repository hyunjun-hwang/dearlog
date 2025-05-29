package com.example.dearlog.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import com.example.dearlog.R;
import com.example.dearlog.dialog.SelectEmotionDialog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WriteDiaryActivity extends AppCompatActivity {

    private TextView tvDate, tvQuestion;
    private EditText etDiaryContent;
    private LinearLayout emotionSelector;
    private ImageButton btnBack, btnMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_diary);

        // 1. XML 요소 연결
        tvDate = findViewById(R.id.tvDate);
        tvQuestion = findViewById(R.id.tvQuestion);
        etDiaryContent = findViewById(R.id.etDiaryContent);
        emotionSelector = findViewById(R.id.emotionSelector);
        btnBack = findViewById(R.id.btnBack);
        btnMenu = findViewById(R.id.btnMenu);

        // 2. 오늘 날짜 표시
        String today = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault()).format(new Date());
        tvDate.setText(today + " 작성일");

        // 3. 질문 불러오기
        loadTodayQuestion();

        // 4. 감정 선택 클릭 리스너 (임시 토스트 처리)
        emotionSelector.setOnClickListener(v -> {
            SelectEmotionDialog dialog = new SelectEmotionDialog(this, selectedColor -> {
                // 선택된 색상 처리
                View colorView = findViewById(R.id.emotionSelector).findViewById(R.id.color_circle);
                if (colorView != null) {
                    switch (selectedColor) {
                        case "yellow":
                            colorView.setBackgroundResource(R.drawable.color_circle_yellow);
                            break;
                        case "orange":
                            colorView.setBackgroundResource(R.drawable.color_circle_orange);
                            break;
                        case "blue":
                            colorView.setBackgroundResource(R.drawable.color_circle_blue);
                            break;
                        case "red":
                            colorView.setBackgroundResource(R.drawable.color_circle_red);
                            break;
                    }
                }
            });
            dialog.show();
        });


        // 5. 뒤로가기 버튼 처리
        btnBack.setOnClickListener(v -> finish());

        // 6. 메뉴 버튼 처리 (추후 구현 가능)
        btnMenu.setOnClickListener(v -> {
            Toast.makeText(this, "메뉴 기능은 추후 구현 예정", Toast.LENGTH_SHORT).show();
        });
    }

    private void loadTodayQuestion() {
        String url = "http://YOUR_SERVER_ADDRESS/getQuestion.jsp"; // 👉 서버 주소로 변경해야 함

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


}
