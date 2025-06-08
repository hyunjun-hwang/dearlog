package com.example.dearlog.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dearlog.R;
import com.example.dearlog.model.Emotion;

import java.util.ArrayList;
import java.util.List;

/**
 * 감정 선택을 위한 커스텀 다이얼로그 클래스
 */
public class SelectEmotionDialog extends Dialog {

    /**
     * 감정 선택 결과를 전달하기 위한 리스너 인터페이스
     */
    public interface OnEmotionSelectedListener {
        void onEmotionSelected(Emotion emotion);
    }

    private OnEmotionSelectedListener listener;

    public void setOnEmotionSelectedListener(OnEmotionSelectedListener listener) {
        this.listener = listener;
    }

    private RecyclerView recyclerView;
    private EmotionAdapter emotionAdapter;
    private List<Emotion> emotionList;
    private ImageView btnClose;
    private TextView tvTitle;

    public SelectEmotionDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_select_emotion);

        // View 초기화
        recyclerView = findViewById(R.id.recyclerViewEmotions);
        btnClose = findViewById(R.id.btnClose);
        tvTitle = findViewById(R.id.tvTitle);

        // 감정 데이터 리스트 초기화
        emotionList = new ArrayList<>();
        emotionList.add(new Emotion("😄", "기쁨", "#FFD700"));
        emotionList.add(new Emotion("😢", "슬픔", "#87CEEB"));
        emotionList.add(new Emotion("😡", "화남", "#FF6347"));
        emotionList.add(new Emotion("😱", "놀람", "#8A2BE2"));
        emotionList.add(new Emotion("😴", "피곤", "#A9A9A9"));
        emotionList.add(new Emotion("😍", "사랑", "#FF69B4"));

        // 어댑터 설정
        emotionAdapter = new EmotionAdapter(emotionList);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setAdapter(emotionAdapter);

        // 감정 클릭 이벤트 처리
        emotionAdapter.setOnItemClickListener(emotion -> {
            if (listener != null) {
                listener.onEmotionSelected(emotion);
            }
            dismiss();
        });

        // 닫기 버튼 처리
        btnClose.setOnClickListener(v -> dismiss());
    }

    /**
     * 감정 리스트를 위한 RecyclerView 어댑터 클래스
     */
    public static class EmotionAdapter extends RecyclerView.Adapter<EmotionAdapter.EmotionViewHolder> {

        /** 클릭 리스너 인터페이스 */
        public interface OnItemClickListener {
            void onItemClick(Emotion emotion);
        }

        private List<Emotion> emotionList;
        private OnItemClickListener itemClickListener;

        public EmotionAdapter(List<Emotion> emotionList) {
            this.emotionList = emotionList;
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.itemClickListener = listener;
        }

        @NonNull
        @Override
        public EmotionViewHolder onCreateViewHolder(@NonNull android.view.ViewGroup parent, int viewType) {
            android.view.View view = android.view.LayoutInflater.from(parent.getContext()).inflate(R.layout.item_emotion, parent, false);
            return new EmotionViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull EmotionViewHolder holder, int position) {
            Emotion emotion = emotionList.get(position);
            holder.bind(emotion);
        }

        @Override
        public int getItemCount() {
            return emotionList.size();
        }

        /**
         * ViewHolder 클래스: 감정 항목 하나를 구성함
         */
        class EmotionViewHolder extends RecyclerView.ViewHolder {
            TextView tvEmoji, tvName;

            public EmotionViewHolder(@NonNull android.view.View itemView) {
                super(itemView);
                tvEmoji = itemView.findViewById(R.id.tvEmoji);
                tvName = itemView.findViewById(R.id.tvName);
            }

            public void bind(final Emotion emotion) {
                tvEmoji.setText(emotion.getEmoji());
                tvName.setText(emotion.getName());

                itemView.setOnClickListener(v -> {
                    if (itemClickListener != null) {
                        itemClickListener.onItemClick(emotion);
                    }
                });
            }
        }
    }
}