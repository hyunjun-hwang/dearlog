package com.example.dearlog.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.appcompat.app.AppCompatDelegate;

public class ThemeUtil {
    private static final String TAG = "ThemeUtil";

    private static final String PREF_NAME = "theme_prefs";
    private static final String KEY_THEME = "theme_mode";

    public static final String LIGHT_MODE   = "light";
    public static final String DARK_MODE    = "dark";
    public static final String DEFAULT_MODE = LIGHT_MODE;

    // 테마 적용
    public static void applyTheme(Context context, String themeMode) {
        switch (themeMode) {
            case LIGHT_MODE:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                Log.d(TAG, "라이트 모드 적용");
                break;
            case DARK_MODE:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                Log.d(TAG, "다크 모드 적용");
                break;
            default:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                Log.d(TAG, "시스템 기본 모드 적용");
                themeMode = DEFAULT_MODE;
                break;
        }
        saveMode(context, themeMode);
    }

    // 모드 저장
    public static void saveMode(Context context, String mode) {
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        sp.edit().putString(KEY_THEME, mode).apply();
    }

    // 모드 불러오기
    public static String loadMode(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sp.getString(KEY_THEME, DEFAULT_MODE);
    }
}