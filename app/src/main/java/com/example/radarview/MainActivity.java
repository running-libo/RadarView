package com.example.radarview;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private String[] titleArray  = new String[] {"击杀", "生存", "助攻", "物理", "魔法", "防御", "金钱"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RadarView radarView = findViewById(R.id.radarview);
        radarView.setTitleArray(titleArray);
    }
}