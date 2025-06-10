package com.example.dearlog.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dearlog.R;

public class ConnectFriendDialog extends Dialog {

    private EditText etFriendId;
    private ImageButton btnBack;
    private Button btnConnect;
    private RequestQueue queue;
    private Context context;
    private Runnable onSuccessCallback;

    public ConnectFriendDialog(@NonNull Context context, Runnable onSuccessCallback) {
        super(context);
        this.context = context;
        this.queue = Volley.newRequestQueue(context);
        this.onSuccessCallback = onSuccessCallback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_connect_friend, null);
        setContentView(view);

        etFriendId = view.findViewById(R.id.et_friend_id);
        btnBack = view.findViewById(R.id.btn_back);
        btnConnect = view.findViewById(R.id.btn_connect);

        btnBack.setOnClickListener(v -> dismiss());

        btnConnect.setOnClickListener(v -> {
            String friendId = etFriendId.getText().toString().trim();
            if (friendId.isEmpty()) {
                Toast.makeText(getContext(), "친구 ID를 입력해주세요", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences prefs = context.getSharedPreferences("DearlogPrefs", Context.MODE_PRIVATE);
            String userId = prefs.getString("user_id", null);
            if (userId == null) {
                Toast.makeText(getContext(), "로그인 정보가 없습니다", Toast.LENGTH_SHORT).show();
                return;
            }

            String url = "http://10.0.2.2:8080/connectFriend.jsp?user_id=" + userId + "&friend_id=" + friendId;

            StringRequest request = new StringRequest(Request.Method.GET, url,
                    response -> {
                        if (response.contains("success")) {
                            prefs.edit().putString("friend_id", friendId).apply();
                            Toast.makeText(getContext(), "친구 연결 성공", Toast.LENGTH_SHORT).show();
                            dismiss();
                            if (onSuccessCallback != null) onSuccessCallback.run();
                        } else if (response.contains("not_found")) {
                            Toast.makeText(getContext(), "존재하지 않는 친구 ID입니다", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "서버 오류", Toast.LENGTH_SHORT).show();
                        }
                    },
                    error -> Toast.makeText(getContext(), "연결 실패", Toast.LENGTH_SHORT).show());

            queue.add(request);
        });
    }
}
