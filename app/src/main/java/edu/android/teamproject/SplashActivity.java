package edu.android.teamproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            Thread.sleep(1000); // 대기 초 설정
        } catch (Exception e) {
            e.printStackTrace();
        }
        startActivity(new Intent(this,LoginActivity.class));
        finish();
    }
}
