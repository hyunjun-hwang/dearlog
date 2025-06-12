package com.example.dearlog.request;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class FindIdRequest extends StringRequest {

    // 서버에서 닉네임으로 user_id를 찾는 JSP 경로
    private static final String URL = "http://10.0.2.2:8080/find_id.jsp";

    private final Map<String, String> params;

    public FindIdRequest(String nickname, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        params = new HashMap<>();
        params.put("nickname", nickname); // JSP에서 request.getParameter("nickname") 으로 받게
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params;
    }
}