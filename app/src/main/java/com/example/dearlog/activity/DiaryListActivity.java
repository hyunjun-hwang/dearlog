package com.example.dearlog.activity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.dearlog.R;
import com.example.dearlog.adapter.DiaryAdapter;
import com.example.dearlog.model.DiaryItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DiaryListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DiaryAdapter adapter;
    private ArrayList<DiaryItem> diaryList;
    private ImageButton btnBack, btnMenu;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_list);

        recyclerView = findViewById(R.id.recycler_diary_list);
        btnBack = findViewById(R.id.btn_back);
        btnMenu = findViewById(R.id.btn_menu);
        queue = Volley.newRequestQueue(this);

        diaryList = new ArrayList<>();
        adapter = new DiaryAdapter(diaryList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        btnBack.setOnClickListener(v -> finish());
        btnMenu.setOnClickListener(v -> {
            // 메뉴 기능 추후 구현 예정
        });

        // 인텐트로부터 entry_ids 받아오기
        ArrayList<Integer> entryIds = (ArrayList<Integer>) getIntent().getSerializableExtra("entry_ids");

        if (entryIds == null || entryIds.isEmpty()) {
            Toast.makeText(this, "불러올 일기가 없습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 서버에서 entry 데이터 불러오기
        for (int entryId : entryIds) {
            String entryUrl = "http://10.0.2.2:8080/getEntryById.jsp?entry_id=" + entryId;

            JsonArrayRequest entryRequest = new JsonArrayRequest(
                    Request.Method.GET,
                    entryUrl,
                    null,
                    response -> {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject entry = response.getJSONObject(i);
                                String date = entry.getString("date");
                                String question = entry.getString("question");
                                String writer = entry.getString("writer");
                                String color = entry.getString("color");

                                diaryList.add(new DiaryItem(date, question, writer, color));
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            Toast.makeText(this, "일기 데이터 파싱 오류", Toast.LENGTH_SHORT).show();
                        }
                    },
                    error -> Toast.makeText(this, "일기 데이터 로딩 실패", Toast.LENGTH_SHORT).show()
            );
            queue.add(entryRequest);
        }
    }
}