package com.example.dearlog.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.dearlog.R;
import com.example.dearlog.request.FindPasswordRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class FindPasswordActivity extends AppCompatActivity {

    private EditText emailInput, nicknameInput;
    private Button findPasswordButton;
    private ImageButton btnBack;
    private TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);

        emailInput = findViewById(R.id.email_input);
        nicknameInput = findViewById(R.id.nickname_input);
        findPasswordButton = findViewById(R.id.find_password_button);
        resultText = findViewById(R.id.result_text);
        // 뒤로가기 버튼
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        findPasswordButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String nickname = nicknameInput.getText().toString().trim();

            if (email.isEmpty() || nickname.isEmpty()) {
                Toast.makeText(this, "모든 정보를 입력하세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            Response.Listener<String> responseListener = response -> {
                try {
                    JSONObject json = new JSONObject(response);
                    if (json.getBoolean("success")) {
                        String password = json.getString("password");
                        resultText.setText("찾은 비밀번호: " + password);
                    } else {
                        resultText.setText("일치하는 정보가 없습니다.");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "서버 오류", Toast.LENGTH_SHORT).show();
                }
            };

            FindPasswordRequest request = new FindPasswordRequest(email, nickname, responseListener);
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(request);
        });
    }
}