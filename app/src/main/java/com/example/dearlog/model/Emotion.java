package com.example.dearlog.model;

public class Emotion {
    private String emoji;
    private String name;
    private String color;

    public Emotion(String emoji, String name, String color) {
        this.emoji = emoji;
        this.name = name;
        this.color = color;
    }

    public String getEmoji() {
        return emoji;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
}
