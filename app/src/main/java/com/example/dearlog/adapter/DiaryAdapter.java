package com.example.dearlog.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dearlog.R;
import com.example.dearlog.activity.DiaryDetailActivity;
import com.example.dearlog.model.DiaryItem;

import java.util.ArrayList;

public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.ViewHolder> {

    private ArrayList<DiaryItem> diaryList;
    private Context context;

    public DiaryAdapter(ArrayList<DiaryItem> diaryList, Context context) {
        this.diaryList = diaryList;
        this.context = context;
    }

    @NonNull
    @Override
    public DiaryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_diary_entry, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiaryAdapter.ViewHolder holder, int position) {
        DiaryItem item = diaryList.get(position);

        holder.dateView.setText(item.getDate());
        holder.questionView.setText(item.getQuestion());
        holder.writerView.setText(item.getWriter());

        try {
            holder.colorView.setBackgroundColor(Color.parseColor(item.getColor()));
        } catch (IllegalArgumentException e) {
            holder.colorView.setBackgroundColor(Color.GRAY);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DiaryDetailActivity.class);
            intent.putExtra("date", item.getDate());
            intent.putExtra("question", item.getQuestion());
            intent.putExtra("writer", item.getWriter());
            intent.putExtra("color", item.getColor());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return diaryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateView, questionView, writerView;
        View colorView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateView = itemView.findViewById(R.id.text_date);
            questionView = itemView.findViewById(R.id.text_question);
            writerView = itemView.findViewById(R.id.text_writer);
            colorView = itemView.findViewById(R.id.view_emotion_color);
        }
    }
}
