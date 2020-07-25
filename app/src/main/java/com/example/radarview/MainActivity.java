package com.example.radarview;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RadarView radarView = findViewById(R.id.radarview);

//        final int[] side = {3};
//        new CountDownTimer(15000, 500) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                radarView.setSideCount(side[0]);
//                side[0]++;
//            }
//
//            @Override
//            public void onFinish() {
//
//            }
//        }.start();
    }
}