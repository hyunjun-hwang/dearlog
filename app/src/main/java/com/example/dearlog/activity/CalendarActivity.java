package com.example.dearlog.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dearlog.R;

public class CalendarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);

        // 날짜 TextView(1~31) 클릭 시 DiaryDetailActivity로 이동
        for (int day = 1; day <= 31; day++) {
            String viewId = "calendar_day_" + day;
            int resId = getResources().getIdentifier(viewId, "id", getPackageName());

            View view = findViewById(resId);
            if (view instanceof TextView) {
                view.setOnClickListener(v -> {
                    String selectedDay = ((TextView) v).getText().toString();
                    String formattedDate = "2025-06-" + String.format("%02d", Integer.parseInt(selectedDay)); // 예시용
                    Intent intent = new Intent(CalendarActivity.this, DiaryDetailActivity.class);
                    intent.putExtra("date", formattedDate);
                    startActivity(intent);
                });
            } else if (resId == 0) {
                // 누락된 ID가 있을 경우 경고
                Toast.makeText(this, "calendar.xml에 ID 누락: " + viewId, Toast.LENGTH_SHORT).show();
            }
        }

        // 설정 버튼 → SettingsActivity로 이동
        ImageButton btnSettings = findViewById(R.id.btn_settings);
        if (btnSettings != null) {
            btnSettings.setOnClickListener(v -> {
                Intent intent = new Intent(CalendarActivity.this, SettingsActivity.class);
                startActivity(intent);
            });
        }
    }
}
