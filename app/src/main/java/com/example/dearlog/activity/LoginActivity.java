package com.example.dearlog.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.dearlog.activity.MainActivity;
import com.example.dearlog.R;
import com.example.dearlog.request.LoginRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput;
    private Button loginButton, signupButton;
    private TextView findInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 뷰 초기화
        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        loginButton = findViewById(R.id.login_button);
        signupButton = findViewById(R.id.signup_button);
        findInfo = findViewById(R.id.find_info);

        // 로그인 버튼 클릭 처리
        loginButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "이메일과 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            Response.Listener<String> responseListener = response -> {
                try {
                    JSONObject json = new JSONObject(response);
                    if (json.getBoolean("success")) {
                        // 로그인 성공 → MainActivity 이동
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "서버 응답 오류", Toast.LENGTH_SHORT).show();
                }
            };

            LoginRequest request = new LoginRequest(email, password, responseListener);
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(request);
        });

        // 회원가입 화면 이동
        signupButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });

        // 아이디/비번 찾기 안내
        findInfo.setOnClickListener(v -> {
            Toast.makeText(this, "아이디/비밀번호 찾기 기능은 추후 구현 예정입니다.", Toast.LENGTH_SHORT).show();
        });
    }
}
