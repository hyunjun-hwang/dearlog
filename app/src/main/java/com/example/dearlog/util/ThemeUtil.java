package com.example.dearlog.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.appcompat.app.AppCompatDelegate;

public class ThemeUtil {
    private static final String TAG = "ThemeUtil";
    private static final String PREF_NAME = "theme_prefs";
    private static final String KEY_THEME = "theme_color";

    public static final String LIGHT_MODE   = "light";
    public static final String DARK_MODE    = "dark";
    public static final String DEFAULT_MODE = LIGHT_MODE;

    public static void applyTheme(Context context, String themeColor) {
        switch (themeColor) {
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
                Log.d(TAG, "시스템 설정 모드 적용");
                themeColor = DEFAULT_MODE;
                break;
        }
        modSave(context, themeColor);
    }


    public static void modSave(Context context, String select_mod) {
        SharedPreferences sp;
        sp = context.getSharedPreferences("mod", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("mod", select_mod);
        editor.commit();
    }


    public static String modLoad(Context context) {
        SharedPreferences sp;
        sp = context.getSharedPreferences("mod", Context.MODE_PRIVATE);
        String load_mod = sp.getString("mod", "light");
        return load_mod;
    }
}