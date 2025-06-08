package com.example.dearlog.model;

public class DiaryItem {
    private String date;
    private String question;
    private String writer;
    private String color;

    public DiaryItem(String date, String question, String writer, String color) {
        this.date = date;
        this.question = question;
        this.writer = writer;
        this.color = color;
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
