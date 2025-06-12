package com.example.dearlog.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.dearlog.R;
import com.example.dearlog.request.FindIdRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class FindIdActivity extends AppCompatActivity {

    private EditText nicknameInput;
    private Button findIdButton;
    private TextView resultText;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_id);

        nicknameInput = findViewById(R.id.nickname_input);
        findIdButton = findViewById(R.id.find_id_button);
        resultText = findViewById(R.id.result_text); // 결과 표시용 텍스트뷰 추가 필요
        // 뒤로가기 버튼
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        findIdButton.setOnClickListener(v -> {
            String nickname = nicknameInput.getText().toString().trim();

            if (nickname.isEmpty()) {
                Toast.makeText(this, "닉네임을 입력하세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            Response.Listener<String> responseListener = response -> {
                try {
                    JSONObject json = new JSONObject(response);
                    if (json.getBoolean("success")) {
                        String userId = json.getString("user_id");
                        resultText.setText("찾은 아이디: " + userId);
                    } else {
                        resultText.setText("해당 닉네임으로 등록된 아이디가 없습니다.");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "서버 오류", Toast.LENGTH_SHORT).show();
                }
            };

            // 서버 요청 전송
            FindIdRequest request = new FindIdRequest(nickname, responseListener);
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(request);
        });
    }
}