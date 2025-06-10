package com.example.dearlog.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.dearlog.R;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput;
    private Button loginButton, signupButton;
    private TextView findInfo;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // View 연결
        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        loginButton = findViewById(R.id.login_button);
        signupButton = findViewById(R.id.signup_button);
        findInfo = findViewById(R.id.find_info);
        queue = Volley.newRequestQueue(this);

        // 로그인 버튼 클릭
        loginButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
            String url = "http://10.0.2.2:8080/loginCheck.jsp?email=" + email + "&password=" + password;

            // 유효성 검사
            if (email.isEmpty()) {
                Toast.makeText(LoginActivity.this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show();
                return;
            } else if (!isValidEmail(email)) {
                Toast.makeText(LoginActivity.this, "이메일 형식이 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
                return;
            } else if (password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            // 서버 요청
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    null,
                    response -> {
                        try {
                            String status = response.getString("status");
                            if (status.equals("success")) {
                                String serverId = response.getString("user_id");
                                String nickname = response.getString("nickname");

                                // SharedPreferences에 저장
                                SharedPreferences prefs = getSharedPreferences("DearlogPrefs", MODE_PRIVATE);
                                prefs.edit().putString("user_id", serverId).apply();
                                prefs.edit().putString("nickname", nickname).apply();

                                // 메인화면 이동
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "아이디 또는 비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this, "JSON 파싱 오류", Toast.LENGTH_SHORT).show();
                        }
                    },
                    error -> Toast.makeText(LoginActivity.this, "서버 통신 오류", Toast.LENGTH_SHORT).show()
            );

            queue.add(request);
        });

        // 회원가입 버튼 클릭 시 이동
        signupButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        // 아이디/비밀번호 찾기 (미구현 안내)
        findInfo.setOnClickListener(v -> {
            Toast.makeText(LoginActivity.this, "아이디/비밀번호 찾기 기능은 추후 구현 예정입니다.", Toast.LENGTH_SHORT).show();
        });
    }

    // 이메일 형식 유효성 검사
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }
}