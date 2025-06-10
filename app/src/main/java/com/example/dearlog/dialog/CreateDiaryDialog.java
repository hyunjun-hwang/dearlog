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

import java.util.HashMap;
import java.util.Map;

public class CreateDiaryDialog extends Dialog {

    private EditText etDiaryTitle;
    private ImageButton btnBack;
    private Button btnDone;
    private View selectedColorView;
    private String selectedColorHex = null;
    private final Context context;  // <- 추가
    private final OnDiaryCreateListener diaryCreateListener;

    // 콜백 인터페이스 정의
    public interface OnDiaryCreateListener {
        void onDiaryCreate(String title, String colorHex);
    }

    public CreateDiaryDialog(@NonNull Context context, OnDiaryCreateListener listener) {
        super(context);
        this.context = context;
        this.diaryCreateListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_create_diary, null);
        setContentView(view);

        // 뷰 초기화
        etDiaryTitle = view.findViewById(R.id.et_diary_title);
        btnBack = view.findViewById(R.id.btn_back);
        btnDone = view.findViewById(R.id.btn_done);

        btnBack.setOnClickListener(v -> dismiss());

        int[] colorIds = new int[] {
                R.id.color_red, R.id.color_orange, R.id.color_yellow,
                R.id.color_green, R.id.color_blue, R.id.color_navy,
                R.id.color_purple, R.id.color_black, R.id.color_sky,
                R.id.color_pink, R.id.color_brown, R.id.color_lavender
        };

        Map<Integer, String> colorHexMap = new HashMap<>();
        colorHexMap.put(R.id.color_red, "#FF0000");
        colorHexMap.put(R.id.color_orange, "#FFA500");
        colorHexMap.put(R.id.color_yellow, "#FFFF00");
        colorHexMap.put(R.id.color_green, "#008000");
        colorHexMap.put(R.id.color_blue, "#0000FF");
        colorHexMap.put(R.id.color_navy, "#000080");
        colorHexMap.put(R.id.color_purple, "#800080");
        colorHexMap.put(R.id.color_black, "#000000");
        colorHexMap.put(R.id.color_sky, "#00CFFF");
        colorHexMap.put(R.id.color_pink, "#FF00FF");
        colorHexMap.put(R.id.color_brown, "#A52A2A");
        colorHexMap.put(R.id.color_lavender, "#E6E6FA");

        for (int id : colorIds) {
            View colorView = view.findViewById(id);
            colorView.setOnClickListener(v -> {
                if (selectedColorView != null) {
                    selectedColorView.setBackgroundResource(R.drawable.color_circle_selector);
                }
                selectedColorView = v;
                selectedColorHex = colorHexMap.get(id);
                v.setBackgroundResource(R.drawable.color_circle_selector); // 선택 표시

                Toast.makeText(getContext(), "선택한 색상 코드: " + selectedColorHex, Toast.LENGTH_SHORT).show();
            });
        }

        btnDone.setOnClickListener(v -> {
            String title = etDiaryTitle.getText().toString().trim();

            if (title.isEmpty()) {
                Toast.makeText(getContext(), "제목을 입력해주세요", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedColorHex == null) {
                Toast.makeText(getContext(), "표지 색상을 선택해주세요", Toast.LENGTH_SHORT).show();
                return;
            }

            // SharedPreferences에서 user_id와 friend_id 가져오기
            SharedPreferences prefs = context.getSharedPreferences("DearlogPrefs", Context.MODE_PRIVATE);
            String userId = prefs.getString("user_id", null);
            String friendId = prefs.getString("friend_id", null);

            if (userId == null || friendId == null) {
                Toast.makeText(getContext(), "사용자 정보가 없습니다", Toast.LENGTH_SHORT).show();
                return;
            }

            // JSP URL로 요청 전송
            String url = "http://10.0.2.2:8080/createDiary.jsp"
                    + "?title=" + title
                    + "&user1_id=" + userId
                    + "&user2_id=" + friendId
                    + "&color_hex=" + selectedColorHex;

            RequestQueue queue = Volley.newRequestQueue(context);
            StringRequest request = new StringRequest(Request.Method.GET, url,
                    response -> {
                        if (response.contains("success")) {
                            Toast.makeText(getContext(), "다이어리 생성 성공", Toast.LENGTH_SHORT).show();
                            diaryCreateListener.onDiaryCreate(title, selectedColorHex);
                            dismiss();
                        } else {
                            Toast.makeText(getContext(), "서버 오류: " + response, Toast.LENGTH_SHORT).show();
                        }
                    },
                    error -> Toast.makeText(getContext(), "요청 실패", Toast.LENGTH_SHORT).show()
            );

            queue.add(request);
        });
    }
}