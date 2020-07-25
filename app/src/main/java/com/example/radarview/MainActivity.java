package com.example.radarview;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private String[] titleArray  = new String[] {"击杀", "生存", "助攻", "物理", "魔法", "金钱"};
    private int[] scoreArray = new int[] {60, 92, 94, 30, 98, 68, 76};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RadarView radarView = findViewById(R.id.radarview);
        radarView.setTitleArray(titleArray);
        radarView.setScoreArray(scoreArray);
    }
}