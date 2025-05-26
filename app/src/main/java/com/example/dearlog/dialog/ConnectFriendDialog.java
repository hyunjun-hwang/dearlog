package com.example.dearlog.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.dearlog.R;

public class ConnectFriendDialog extends Dialog {

    private EditText etFriendId;
    private ImageButton btnBack;
    private Button btnConnect;
    private OnConnectListener connectListener;

    public interface OnConnectListener {
        void onConnect(String friendId);
    }

    public ConnectFriendDialog(@NonNull Context context, OnConnectListener listener) {
        super(context);
        this.connectListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_connect_friend, null);
        setContentView(view);

        // 뷰 연결
        etFriendId = view.findViewById(R.id.et_friend_id);
        btnBack = view.findViewById(R.id.btn_back);
        btnConnect = view.findViewById(R.id.btn_connect);

        // 뒤로가기 버튼 → 다이얼로그 종료
        btnBack.setOnClickListener(v -> dismiss());

        // 연결 버튼 클릭 시 처리
        btnConnect.setOnClickListener(v -> {
            String friendId = etFriendId.getText().toString().trim();
            if (friendId.isEmpty()) {
                Toast.makeText(getContext(), "친구 ID를 입력해주세요", Toast.LENGTH_SHORT).show();
            } else {
                connectListener.onConnect(friendId);
                dismiss();
            }
        });
    }
}
