package com.example.dearlog.request;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * 로그인 요청을 위한 Volley Request 클래스
 */
public class LoginRequest extends StringRequest {

    // 서버 URL (로그인 PHP 또는 JSP 위치)
    private static final String URL = "http://yourserver.com/Login.jsp";

    // 요청에 함께 보낼 데이터
    private final Map<String, String> params;

    public LoginRequest(String email, String password, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);  // null → 에러 리스너는 생략 가능

        params = new HashMap<>();
        params.put("userEmail", email);
        params.put("userPassword", password);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params;
    }
}
