package com.example.dearlog.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.dearlog.R;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // fragment_home.xml 인플레이션
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // 뷰 요소 초기화 (필요 시 리스너도 여기에 작성)
        ImageButton darkModeBtn = view.findViewById(R.id.dark_mode_button);
        ImageButton settingsBtn = view.findViewById(R.id.settings_button);
        TextView connectText = view.findViewById(R.id.connect_text);
        TextView exclamationIcon = view.findViewById(R.id.exclamation_icon);

        // 클릭 이벤트 예시 (추후 구현 예정)
        settingsBtn.setOnClickListener(v -> {
            // 설정 화면 이동 (예정)
        });

        return view;
    }
}
