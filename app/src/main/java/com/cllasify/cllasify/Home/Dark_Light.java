package com.cllasify.cllasify.Home;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.cllasify.cllasify.R;

public class Dark_Light extends AppCompatActivity {

    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dark_light);


        Button btnDefault,btnDark,btnLight;

        btnDefault = findViewById(R.id.btnDefault);
        btnDark = findViewById(R.id.btnDark);
        btnLight = findViewById(R.id.btnLight);


        RadioGroup rg_theme = findViewById(R.id.rg_Theme);


        SharedPreferences sharedPreferencesLightDark = getSharedPreferences("night",0);

        int booleanValue = sharedPreferencesLightDark.getInt("THEME_MODE", 2);
        Log.d(TAG, "onCreate: "+booleanValue);
        if (booleanValue == 0){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        if (booleanValue == 1){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        if (booleanValue == 2){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }

        rg_theme.setOnCheckedChangeListener((radioGroup, i) -> {
            switch (i){
                case R.id.btnDefault:
                    Log.d(TAG, "onCreate: "+btnDefault);
                    int themeDefault = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
                    AppCompatDelegate.setDefaultNightMode(themeDefault);
                    SharedPreferences.Editor editorDefault = sharedPreferencesLightDark.edit();
                    editorDefault.putInt("THEME_MODE",2);
                    editorDefault.apply();
                    break;

                case R.id.btnDark:
                    Log.d(TAG, "onCreate: "+btnDark);
                    int themeDark = AppCompatDelegate.MODE_NIGHT_YES;
                    AppCompatDelegate.setDefaultNightMode(themeDark);
                    SharedPreferences.Editor editorDark = sharedPreferencesLightDark.edit();
                    editorDark.putInt("THEME_MODE",0);
                    editorDark.apply();
                    break;

                case R.id.btnLight:
                    Log.d(TAG, "onCreate: "+btnLight);
                    int themeLight = AppCompatDelegate.MODE_NIGHT_NO;
                    AppCompatDelegate.setDefaultNightMode(themeLight);
                    SharedPreferences.Editor editorLight = sharedPreferencesLightDark.edit();
                    editorLight.putInt("THEME_MODE",1);
                    editorLight.apply();
                    break;
            }
        });

    }
}