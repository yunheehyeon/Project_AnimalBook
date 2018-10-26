package edu.android.teamproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {



    private BottomNavigationView.OnNavigationItemSelectedListener itemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    FragmentManager diaryManager = getSupportFragmentManager();
                    DiaryItemFragment diaryFragment = new DiaryItemFragment();
                    if (diaryFragment != null) {
                        diaryManager.beginTransaction().replace(R.id.container, diaryFragment).commit();
                    }

                    return true;
                case R.id.navigation_com:
                    FragmentManager manager = getSupportFragmentManager();
                    ComItemListFragment fragment = new ComItemListFragment();
                    if(fragment != null){
                        manager.beginTransaction().replace(R.id.container, fragment).commit();
                    }
                    return true;
                case R.id.navigation_map:

                    return true;
                case R.id.navigation_my:
                    FragmentManager manager1 = getSupportFragmentManager();
                    MyPageFragment fragment1 = new MyPageFragment();
                    if(fragment1 != null){
                        manager1.beginTransaction().replace(R.id.container, fragment1).commit();
                    }
                    return true;

            }
            return false;
        }

    };

    private BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();



        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(itemSelectedListener);

        FragmentManager diaryManager = getSupportFragmentManager();
        DiaryItemFragment diaryFragment = new DiaryItemFragment();
        if (diaryFragment != null) {
            diaryManager.beginTransaction().replace(R.id.container, diaryFragment).commit();
        }

    }


}
