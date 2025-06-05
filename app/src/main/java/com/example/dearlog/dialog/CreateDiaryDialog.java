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
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.dearlog.R;

import java.util.HashMap;
import java.util.Map;

public class CreateDiaryDialog extends Dialog {

    private EditText etDiaryTitle;
    private ImageButton btnBack;
    private Button btnDone;
    private View selectedColorView;
    private String selectedColorHex = null;

    private final OnDiaryCreateListener diaryCreateListener;

    // 콜백 인터페이스 정의
    public interface OnDiaryCreateListener {
        void onDiaryCreate(String title, String colorHex);
    }

    public CreateDiaryDialog(@NonNull Context context, OnDiaryCreateListener listener) {
        super(context);
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

            diaryCreateListener.onDiaryCreate(title, selectedColorHex); // 콜백 실행
            dismiss();
        });
    }
}