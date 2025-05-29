package com.example.dearlog.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import com.example.dearlog.R;

public class SelectEmotionDialog extends Dialog {

    public interface OnEmotionSelectedListener {
        void onEmotionSelected(String color); // 예: "yellow", "orange" 등
    }

    public SelectEmotionDialog(Context context, OnEmotionSelectedListener listener) {
        super(context);
        setContentView(R.layout.dialog_select_emotion);
        setCancelable(true);

        // 닫기 버튼
        ImageButton btnClose = findViewById(R.id.btn_close);
        btnClose.setOnClickListener(v -> dismiss());

        // 색상 버튼들
        @SuppressLint("WrongViewCast") ImageButton yellowBtn = findViewById(R.id.color_yellow);
        @SuppressLint("WrongViewCast") ImageButton orangeBtn = findViewById(R.id.color_orange);
        @SuppressLint("WrongViewCast") ImageButton blueBtn = findViewById(R.id.color_blue);
        @SuppressLint("WrongViewCast") ImageButton redBtn = findViewById(R.id.color_red);

        final String[] selectedColor = {null};

        View.OnClickListener colorClickListener = v -> {
            if (v.getId() == R.id.color_yellow) selectedColor[0] = "yellow";
            else if (v.getId() == R.id.color_orange) selectedColor[0] = "orange";
            else if (v.getId() == R.id.color_blue) selectedColor[0] = "blue";
            else if (v.getId() == R.id.color_red) selectedColor[0] = "red";
        };

        yellowBtn.setOnClickListener(colorClickListener);
        orangeBtn.setOnClickListener(colorClickListener);
        blueBtn.setOnClickListener(colorClickListener);
        redBtn.setOnClickListener(colorClickListener);

        // 완료 버튼
        Button completeBtn = findViewById(R.id.btn_select_complete);
        completeBtn.setOnClickListener(v -> {
            if (selectedColor[0] != null) {
                listener.onEmotionSelected(selectedColor[0]);
                dismiss();
            }
        });
    }
}
