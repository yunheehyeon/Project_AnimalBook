package edu.android.teamproject;

import android.Manifest;
import android.app.FragmentManager;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;


public class GmapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Geocoder coder;

    private static final String TAG = "edu.android.and39";
    private static final int REQ_FINE_LOCATION = 100;

    // 위치 정보(최근 위치, 주기적 업데이트 시작/취소)와 관련된 클래스
    private FusedLocationProviderClient locationClient;
    // 주기적 위치 업데이트를 요청할 때 설정 정보를 저장하는 클래스
    private LocationRequest locationRequest;
    // 주기적 위치 정보를 처리하는 콜백
    private LocationCallback locationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gmap);

        locationClient = LocationServices.getFusedLocationProviderClient(this);
        createLocationRequest();

        checkLocationPermission();

        SupportMapFragment fragment = (SupportMapFragment)getSupportFragmentManager()
                .findFragmentById(R.id.map);
        fragment.getMapAsync(this);

    }

    private void checkLocationPermission() {
        int check = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (check == PackageManager.PERMISSION_GRANTED) {
            // 최근 위치 -> 지도 업데이트
            getLastLocation();

            // 위치 정보 업데이트 요청
            requestLocationUpdate();
        } else {
            requestLocationPermission();

        }

    }

    private void requestLocationPermission() {
        String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION};
        ActivityCompat.requestPermissions(this, permission, REQ_FINE_LOCATION);


    }

    private void requestLocationUpdate() {
        locationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQ_FINE_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 가장 최근 위치 정보를 확인 -> 그 위치로 지도 이동
                getLastLocation();
                // 주기적 위치 업데이트 요청
                requestLocationUpdate();

            } else {
                Toast.makeText(this, "위치 권한이 있어야 앱을 사용할 수 있습니다.", Toast.LENGTH_SHORT).show();
                finish(); // Activity 종료
            }
        }

    }

    private void getLastLocation() {
        Task<Location> task = locationClient.getLastLocation();
        task.addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location == null) {
                    return;
                }
                double lat = location.getLatitude();
                double lng = location.getLongitude();
                LatLng latLng = new LatLng(lat, lng);
                updateGoogleMap(latLng);
            }
        });


    }

    private void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Location location = locationResult.getLastLocation();
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                LatLng latLng = new LatLng(latitude, longitude);
                updateGoogleMap(latLng);
            }
        };

    }


    /**
     * Manipulates the map once available.
     *
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        
    }

    private void updateGoogleMap(LatLng latLng) {
        if (mMap == null) {
            // GoogleMap 객체가 생성되어 있지않을 때 화면 업데이트

            return;
        }

        LatLng latLng1 = new LatLng(37.56, 126.97);
        mMap.addMarker(new MarkerOptions().position(latLng1));
        mMap.setMinZoomPreference(15);
        mMap.setMaxZoomPreference(20);
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng1));
    }

    @Override
    protected void onPause() {
        super.onPause();

        locationClient.removeLocationUpdates(locationCallback);
    }
}
