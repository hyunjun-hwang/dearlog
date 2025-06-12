package com.example.dearlog.request;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * 이메일 중복 확인을 위한 Request 클래스
 */
public class ValidateRequest extends StringRequest {

    private static final String URL = "http://10.0.2.2:8080/Validate.jsp";

    private final Map<String, String> params;

    public ValidateRequest(String email, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        params = new HashMap<>();
        params.put("userEmail", email);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params;
    }
}
