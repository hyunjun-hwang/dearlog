package com.example.dearlog.model;

public class Emotion {
    private String code;
    private String name;
    private String color;

    public Emotion(String code, String name, String color) {
        this.code = code;
        this.name = name;
        this.color = color;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
}
