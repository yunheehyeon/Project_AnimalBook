package edu.android.teamproject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

public class MainActivity extends AppCompatActivity implements MyPageFragment.MyPageCallback, MyPageDao.DataCallback {

    GoogleMap mMap;
    SupportMapFragment mapFragment;
    public static final int REQ_CODE = 1000;

    private DiaryItemFragment diaryFragment;
    private ComItemListFragment comItemListFragment;
    private GmapFragment gmapFragment;
    private MyPageFragment fragmentMyPage;
    private FragmentManager fragmentManager;
    private MyPageProfile myPageProfile;

    private MyPageDao dao;

    private BottomNavigationView.OnNavigationItemSelectedListener itemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    setDiaryFragment();
                    return true;
                case R.id.navigation_com:
                    setComItemListFragment();
                    return true;
                case R.id.navigation_map:
                    setGmapFragment();
                    return true;
                case R.id.navigation_my:
                    setFragmentMyPage();
                    return true;
            }
            return false;
        }

    };

    private BottomNavigationView navigation;

    int[][] states = new int[][] {
            new int[] { android.R.attr.state_enabled}, // enabled
            new int[] {-android.R.attr.state_enabled}, // disabled
            new int[] {-android.R.attr.state_checked}, // unchecked
            new int[] { android.R.attr.state_pressed}  // pressed
    };

    int[] colors = new int[] {
            Color.WHITE,
            Color.GRAY,
            Color.GRAY,
            Color.WHITE,
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        dao = MyPageDao.getMyPageInstance(this);


        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(itemSelectedListener);
        //navigation.setItemIconTintList(new ColorStateList( states, colors));

        fragmentManager = getSupportFragmentManager();



        if (diaryFragment == null) {
            diaryFragment = new DiaryItemFragment();
        }
        fragmentManager.beginTransaction().replace(R.id.container, diaryFragment).commit();

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

        fragmentManager = getSupportFragmentManager();

        switch (navigation.getSelectedItemId()) {
            case R.id.navigation_home:
                setDiaryFragment();
                break;
            case R.id.navigation_com:
                setComItemListFragment();
                break;
            case R.id.navigation_map:
                setGmapFragment();
                break;
            case R.id.navigation_my:
                setFragmentMyPage();
                break;
        }
    }

    private void setDiaryFragment(){
        diaryFragment = new DiaryItemFragment();
        fragmentManager.beginTransaction().replace(R.id.container, diaryFragment).commitAllowingStateLoss();
    }
    private void setComItemListFragment(){
        comItemListFragment = new ComItemListFragment();
        fragmentManager.beginTransaction().replace(R.id.container, comItemListFragment).commitAllowingStateLoss();

    }
    private void setGmapFragment(){
        gmapFragment = new GmapFragment();
        fragmentManager.beginTransaction().replace(R.id.container, gmapFragment).commitAllowingStateLoss();


    }
    private void setFragmentMyPage(){
        if(fragmentMyPage == null) {
            fragmentMyPage = new MyPageFragment();
        }
        fragmentManager.beginTransaction().replace(R.id.container, fragmentMyPage).commitAllowingStateLoss();

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_profile:
                Context context = getApplicationContext();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);

                View view = inflater.inflate(R.layout.simple_profile, (ViewGroup)findViewById(R.id.simpleProfileLayout));
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                LinearLayout layout = view.findViewById(R.id.simpleProfileLayout);

                myPageProfile = dao.update();

                layout.removeAllViews();

                for(MyPageProfile.ProfileItem p : myPageProfile.getProfileItems()){
                    MyProfileItemLayout myProfileItemLayout = new MyProfileItemLayout(this);
                    TextView myProfileItem = myProfileItemLayout.findViewById(R.id.myProfileItem);
                    TextView myProfileText = myProfileItemLayout.findViewById(R.id.myProfileText);
                    myProfileItem.setText(p.getProfileItemName());
                    myProfileText.setText(p.getProfileItemText());

                    layout.addView(myProfileItemLayout);
                }

                builder.setView(view);

                AlertDialog ad = builder.create();
                ad.show();
        }

        return super.onOptionsItemSelected(item);
    }

    public static String DIARY_ID = "diaryid";

    @Override
    public void BookMarkDataCallback(String diaryId) {
        diaryFragment = new DiaryItemFragment();
        fragmentManager.beginTransaction().replace(R.id.container, diaryFragment).commitAllowingStateLoss();
        navigation.getMenu().getItem(0).setChecked(true);

        Bundle args = new Bundle();
        args.putString(DIARY_ID, diaryId);
        diaryFragment.setArguments(args);
    }

    @Override
    public void proFileCallback() {
        myPageProfile = dao.update();
    }

    @Override
    public void proFileImageCallback() {

    }

    class MyProfileItemLayout extends LinearLayout {

        public MyProfileItemLayout(Context context) {
            super(context);
            LayoutInflater inflater = getLayoutInflater();
            inflater.inflate(R.layout.myprofile_item, this, true);
        }
    }

}
