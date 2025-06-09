package com.example.dearlog.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.dearlog.R;

public class SelectEmotionDialog extends Dialog {

    public interface OnEmotionSelectedListener {
        void onEmotionSelected(String colorHex); // 선택된 색상 코드 전달
    }

    private OnEmotionSelectedListener listener;

    public void setOnEmotionSelectedListener(OnEmotionSelectedListener listener) {
        this.listener = listener;
    }

    private ImageButton colorYellow, colorOrange, colorBlue, colorRed;
    private Button btnComplete;
    private ImageView btnClose;

    private String selectedColor = null;

    public SelectEmotionDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_select_emotion);

        colorYellow = findViewById(R.id.color_yellow);
        colorOrange = findViewById(R.id.color_orange);
        colorBlue = findViewById(R.id.color_blue);
        colorRed = findViewById(R.id.color_red);
        btnClose = findViewById(R.id.btn_close);
        btnComplete = findViewById(R.id.btn_select_complete);

        // 클릭 시 색상 hex 저장
        colorYellow.setOnClickListener(v -> selectColor("#FFD700"));
        colorOrange.setOnClickListener(v -> selectColor("#FFA500"));
        colorBlue.setOnClickListener(v -> selectColor("#42A5F5"));
        colorRed.setOnClickListener(v -> selectColor("#FF6347"));

        // 완료 버튼
        btnComplete.setOnClickListener(v -> {
            if (listener != null && selectedColor != null) {
                listener.onEmotionSelected(selectedColor);
                dismiss();
            }
        });

        // 닫기 버튼
        btnClose.setOnClickListener(v -> dismiss());
    }

    private void selectColor(String colorHex) {
        this.selectedColor = colorHex;

        // 시각적 피드백 주고 싶으면 여기서 배경 테두리 등 변경 가능
        // 예: 선택된 버튼 강조, 나머지 초기화
        // 선택 상태에 따라 background drawable 바꾸기 등 구현 가능
    }
}
