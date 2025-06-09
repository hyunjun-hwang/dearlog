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
 * 감정 통계 화면 프래그먼트
 * 사용자 일기의 감정 기록을 통계로 시각화하는 화면 (차트 등)
 */
public class EmotionStatsFragment extends Fragment {

    public EmotionStatsFragment() {
        // 기본 생성자
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_emotion_stats, container, false);
    }
}
