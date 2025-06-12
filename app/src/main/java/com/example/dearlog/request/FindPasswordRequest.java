package com.example.dearlog.request;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class FindPasswordRequest extends StringRequest {

    private static final String URL = "http://10.0.2.2:8080/find_password.jsp";

    private final Map<String, String> params;

    public FindPasswordRequest(String userId, String nickname, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        params = new HashMap<>();
        params.put("user_id", userId);
        params.put("nickname", nickname);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params;
    }
}