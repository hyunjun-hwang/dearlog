package com.example.dearlog.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dearlog.R;

/**
 * 캘린더 보기 화면 프래그먼트
 * calendar.xml과 연결되도록 구성됨
 */
public class CalendarFragment extends Fragment {

    public CalendarFragment() {
        // 기본 생성자
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.calendar, container, false); // 기존 XML calendar.xml과 연결
    }
}
