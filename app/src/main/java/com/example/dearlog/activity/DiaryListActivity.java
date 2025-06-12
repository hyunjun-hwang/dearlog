package com.example.dearlog.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
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
            Toast.makeText(this, "메뉴 기능은 추후 구현 예정입니다", Toast.LENGTH_SHORT).show();
        });

        // 1. 기존 방식: entry_ids 배열이 있을 경우
        ArrayList<Integer> entryIds = (ArrayList<Integer>) getIntent().getSerializableExtra("entry_ids");

        if (entryIds != null && !entryIds.isEmpty()) {
            Log.d("DiaryListActivity", "받은 entryIds: " + entryIds.toString());

            for (int entryId : entryIds) {
                String entryUrl = "http://10.0.2.2:8080/DearlogServer/getEntryById.jsp?entry_id=" + entryId;

                JsonArrayRequest entryRequest = new JsonArrayRequest(
                        Request.Method.GET,
                        entryUrl,
                        null,
                        response -> {
                            try {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject entry = response.getJSONObject(i);
                                    int id = entry.getInt("entry_id");
                                    String date = entry.getString("date");
                                    String question = entry.getString("question");
                                    String writer = entry.getString("writer");
                                    String color = entry.getString("color");

                                    diaryList.add(new DiaryItem(id, date, question, writer, color));
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

        } else {
            // 2. 새로운 방식: diary_id로 일괄 조회
            String diaryId = getIntent().getStringExtra("diary_id");
            if (diaryId != null) {
                loadEntriesByDiaryId(diaryId);
            } else {
                Toast.makeText(this, "불러올 일기가 없습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadEntriesByDiaryId(String diaryId) {
        String url = "http://10.0.2.2:8080/DearlogServer/getDiaryEntries.jsp?diary_id=" + diaryId;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if (response.getString("status").equals("success")) {
                            JSONArray entries = response.getJSONArray("entries");
                            diaryList.clear();
                            for (int i = 0; i < entries.length(); i++) {
                                JSONObject obj = entries.getJSONObject(i);
                                int entryId = obj.getInt("entry_id");
                                String date = obj.getString("written_at");
                                String question = obj.optString("question_text", "오늘의 질문");
                                String writer = obj.optString("writer", "작성자");
                                String color = obj.getString("emotion_code");

                                diaryList.add(new DiaryItem(entryId, date, question, writer, color));
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(this, "일기 목록 불러오기 실패", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(this, "데이터 파싱 오류", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "서버 연결 실패", Toast.LENGTH_SHORT).show()
        );

        queue.add(request);
    }
}