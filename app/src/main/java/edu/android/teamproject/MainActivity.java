package edu.android.teamproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

public class MainActivity extends AppCompatActivity{
    GoogleMap mMap;
    SupportMapFragment mapFragment;
    public static final int REQ_CODE = 1000;

    private DiaryItemFragment diaryFragment;
    private ComItemListFragment comItemListFragment;
    private GmapFragment gmapFragment;
    private MyPageFragment fragmentMyPage;
    private FragmentManager fragmentManager;

    private BottomNavigationView.OnNavigationItemSelectedListener itemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                        fragmentManager.beginTransaction().replace(R.id.container, diaryFragment).commit();
                    return true;
                case R.id.navigation_com:
                        fragmentManager.beginTransaction().replace(R.id.container, comItemListFragment).commit();
                    return true;
                case R.id.navigation_map:
                        fragmentManager.beginTransaction().replace(R.id.container, gmapFragment).commit();
                    return true;
                case R.id.navigation_my:
                        fragmentManager.beginTransaction().replace(R.id.container, fragmentMyPage).commit();
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

        fragmentManager = getSupportFragmentManager();

        diaryFragment = new DiaryItemFragment();
        if (diaryFragment != null) {
            fragmentManager.beginTransaction().replace(R.id.container, diaryFragment).commit();
        }
        comItemListFragment = new ComItemListFragment();
        gmapFragment = new GmapFragment();
        fragmentMyPage = new MyPageFragment();


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)) {
                Toast.makeText(this, "카메라 권한이 필요합니다.", Toast.LENGTH_LONG).show();
            } else {

            }

            // 권한요청 다이얼로그 띄움
            String[] permission = {Manifest.permission.CAMERA};
            ActivityCompat.requestPermissions(
                    this, // 권한을 요청하는 액티비티 주소
                    permission, // 요청을 하는 관한들의 배열
                    REQ_CODE);  // 권한요청 후 결과처리에 필요한 코드
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();

        switch (navigation.getSelectedItemId()) {
            case R.id.navigation_home:
                diaryFragment = new DiaryItemFragment();
                fragmentManager.beginTransaction().replace(R.id.container, diaryFragment).commit();
                break;
            case R.id.navigation_com:
                comItemListFragment = new ComItemListFragment();
                fragmentManager.beginTransaction().replace(R.id.container, comItemListFragment).commit();
                break;
            case R.id.navigation_map:
                gmapFragment = new GmapFragment();
                fragmentManager.beginTransaction().replace(R.id.container, gmapFragment).commit();
                break;
            case R.id.navigation_my:
                fragmentMyPage = new MyPageFragment();
                fragmentManager.beginTransaction().replace(R.id.container, fragmentMyPage).commit();
                break;
        }


    }
}
