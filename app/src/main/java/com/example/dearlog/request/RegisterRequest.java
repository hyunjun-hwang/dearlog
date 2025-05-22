package com.example.dearlog.request;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * 회원가입 요청을 위한 Volley Request 클래스
 */
public class RegisterRequest extends StringRequest {

    private static final String URL = "http://yourserver.com/Register.jsp";

    private final Map<String, String> params;

    public RegisterRequest(String email, String password, String nickname, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        params = new HashMap<>();
        params.put("userEmail", email);
        params.put("userPassword", password);
        params.put("userNickname", nickname);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params;
    }
}
