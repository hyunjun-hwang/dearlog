package com.example.dearlog.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dearlog.R;
import com.example.dearlog.adapter.DiaryAdapter;
import com.example.dearlog.model.DiaryItem;

import java.util.ArrayList;

public class DiaryListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DiaryAdapter adapter;
    private ArrayList<DiaryItem> diaryList;
    private ImageButton btnBack, btnMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_list);

        recyclerView = findViewById(R.id.recycler_diary_list);
        btnBack = findViewById(R.id.btn_back);
        btnMenu = findViewById(R.id.btn_menu);

        // RecyclerView 설정
        diaryList = new ArrayList<>();
        adapter = new DiaryAdapter(diaryList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // 버튼 이벤트
        btnBack.setOnClickListener(v -> finish());
        btnMenu.setOnClickListener(v -> {
            // TODO: 메뉴 기능 구현 예정
        });

        // 임시 데이터 (서버 연동 전 테스트용)
        loadTestData();
    }

    private void loadTestData() {
        diaryList.add(new DiaryItem("2025.06.09", "오늘 제일 좋아하는 시간은?", "user1", "yellow"));
        diaryList.add(new DiaryItem("2025.06.08", "기억에 남는 장소는?", "user2", "blue"));
        adapter.notifyDataSetChanged();
    }
}
