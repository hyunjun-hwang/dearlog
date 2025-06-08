package com.example.dearlog.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dearlog.R;

/**
 * 익명 일기 열람 화면 프래그먼트
 * RecyclerView를 이용해 다른 사용자의 교환일기를 열람할 수 있음
 */
public class AnonymousDiaryFragment extends Fragment {

    private RecyclerView recyclerView;

    public AnonymousDiaryFragment() {
        // 기본 생성자
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_anonymous_diary, container, false);
        recyclerView = view.findViewById(R.id.recyclerAnonymousDiary);

        // RecyclerView 초기 설정 (데이터는 나중에 서버에서 받아올 예정)
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // recyclerView.setAdapter(...); → 추후 연결 예정

        return view;
    }
}
