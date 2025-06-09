package com.example.dearlog.model;

import android.graphics.Color;

public class Emotion {
    public static final String YELLOW = "Y";
    public static final String BLUE = "B";
    public static final String RED = "R";

    public static int getColor(String code) {
        switch (code) {
            case YELLOW: return Color.parseColor("#FDD835"); // 노란색
            case BLUE: return Color.parseColor("#42A5F5");   // 파랑
            case RED: return Color.parseColor("#EF5350");    // 빨강
            default: return Color.GRAY;                      // 예외 처리
        }
    }
}

