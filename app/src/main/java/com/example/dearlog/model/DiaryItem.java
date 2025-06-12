package com.example.dearlog.model;

public class DiaryItem {
    private int entryId;  // ✅ 추가
    private String date;
    private String question;
    private String writer;
    private String color;

    // 생성자 수정
    public DiaryItem(int entryId, String date, String question, String writer, String color) {
        this.entryId = entryId;
        this.date = date;
        this.question = question;
        this.writer = writer;
        this.color = color;
    }

    // getter 추가
    public int getEntryId() {
        return entryId;
    }

    public String getDate() {
        return date;
    }

    public String getQuestion() {
        return question;
    }

    public String getWriter() {
        return writer;
    }

    public String getColor() {
        return color;
    }
}
