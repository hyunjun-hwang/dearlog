package com.example.dearlog.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.dearlog.R;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput;
    private Button loginButton, signupButton;
    private TextView findInfo;
    private RequestQueue queue;

    // 로컬 테스트용 서버 주소
    private String url = "http://10.0.2.2:8080/test.json"; // AVD 기준 로컬 호스트

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

        // 로그인 버튼 클릭 이벤트
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();

                // 이메일 & 비밀번호 유효성 체크
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

                // 서버 요청 (GET 방식)
                JsonObjectRequest request = new JsonObjectRequest(
                        Request.Method.GET,
                        url,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    String serverId = response.getString("email");
                                    String serverPw = response.getString("password");

                                    if (email.equals(serverId) && password.equals(serverPw)) {
                                        // ✅ 로그인 성공 시 MainActivity로 이동
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish(); // 뒤로가기 방지용
                                    } else {
                                        Toast.makeText(LoginActivity.this, "아이디 또는 비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(LoginActivity.this, "JSON 파싱 오류", Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(LoginActivity.this, "서버 통신 오류", Toast.LENGTH_SHORT).show();
                            }
                        }
                );

                // 요청 추가
                queue.add(request);
            }
        });

        // 회원가입 버튼 클릭 시 RegisterActivity 이동
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        // 아이디/비밀번호 찾기 안내 (미구현 안내)
        findInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "아이디/비밀번호 찾기 기능은 추후 구현 예정입니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 이메일 유효성 검사 함수
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }
}
