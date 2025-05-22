package com.example.dearlog.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.dearlog.R;
import com.example.dearlog.request.RegisterRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput, passwordConfirmInput, nicknameInput;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // 뷰 연결
        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        passwordConfirmInput = findViewById(R.id.password_confirm_input);
        nicknameInput = findViewById(R.id.nickname_input);
        registerButton = findViewById(R.id.register_button);

        // 가입 버튼 클릭 처리
        registerButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
            String confirm = passwordConfirmInput.getText().toString().trim();
            String nickname = nicknameInput.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty() || confirm.isEmpty() || nickname.isEmpty()) {
                Toast.makeText(this, "모든 정보를 입력하세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirm)) {
                Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            Response.Listener<String> responseListener = response -> {
                try {
                    JSONObject json = new JSONObject(response);
                    if (json.getBoolean("success")) {
                        Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                        finish(); // 이전(Login) 화면으로 돌아가기
                    } else {
                        Toast.makeText(this, "회원가입 실패", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "서버 오류", Toast.LENGTH_SHORT).show();
                }
            };

            RegisterRequest request = new RegisterRequest(email, password, nickname, responseListener);
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(request);
        });
    }
}
