package com.example.dearlog.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.Window;
import android.widget.Toast;

import com.example.dearlog.R;

public class CreateDiaryDialog extends Dialog {

    private EditText etTitle;
    private GridLayout colorPalette;
    private Button btnDone;
    private ImageView btnClose;

    private int selectedColorResId = -1; // 선택된 색상 리소스 ID

    public interface OnDiaryCreateListener {
        void onDiaryCreated(String title, int colorResId);
    }

    private OnDiaryCreateListener listener;

    public CreateDiaryDialog(Context context, OnDiaryCreateListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀 제거
        setContentView(R.layout.dialog_create_diary);
        setCancelable(false);

        etTitle = findViewById(R.id.et_diary_title);
        colorPalette = findViewById(R.id.color_palette);
        btnDone = findViewById(R.id.btn_done);
        btnClose = findViewById(R.id.btn_close);

        btnDone.setEnabled(false);

        // 색상 선택 핸들링
        for (int i = 0; i < colorPalette.getChildCount(); i++) {
            final View colorView = colorPalette.getChildAt(i);
            colorView.setOnClickListener(v -> {
                selectedColorResId = ((GradientDrawable) colorView.getBackground()).getColor().getDefaultColor();
                highlightSelectedColor(colorView);
                updateDoneButtonState();
            });
        }

        // 제목 입력 변화 감지
        etTitle.setOnFocusChangeListener((v, hasFocus) -> updateDoneButtonState());

        // 닫기 버튼
        btnClose.setOnClickListener(v -> dismiss());

        // 완료 버튼
        btnDone.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            if (title.isEmpty() || selectedColorResId == -1) {
                Toast.makeText(getContext(), "제목과 색상을 선택해주세요", Toast.LENGTH_SHORT).show();
                return;
            }
            if (listener != null) {
                listener.onDiaryCreated(title, selectedColorResId);
            }
            dismiss();
        });
    }

    private void updateDoneButtonState() {
        String title = etTitle.getText().toString().trim();
        btnDone.setEnabled(!title.isEmpty() && selectedColorResId != -1);
    }

    private void highlightSelectedColor(View selectedView) {
        for (int i = 0; i < colorPalette.getChildCount(); i++) {
            View colorView = colorPalette.getChildAt(i);
            colorView.setAlpha(1f); // 기본 불투명
        }
        selectedView.setAlpha(0.5f); // 선택된 색상은 반투명 처리
    }
}
