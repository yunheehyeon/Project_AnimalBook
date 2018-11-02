package edu.android.teamproject;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class GoogleMapFragment extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    public static final String ACCESS_FINE_LOCATION = "android.permission.ACCESS_FINE_LOCATION";
    MapView mMapView;
    private GoogleMap googleMap;
    private Fragment mapFrag;

    private static final int MY_PERMISSION_REQUEST_CODE = 0;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 1;

    private GoogleApiClient mgoogleApiClient;
    private LocationRequest mlocationRequest;
    private Location mlastlocation;
    double longitude, latitude;

    Marker myCurrentMarker;

    private static int UPDATE_INTERVAL = 1000;
    private static int FASTEST_INTERVAL = 3000;
    private static int DISPLACEMENT = 10;

    public GoogleMapFragment() {
        // Required
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_google_map, container, false);

        setUpLocation();



        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);


        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                displayLocation();


                if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling

                }

                try {
                    googleMap.setMyLocationEnabled(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                mlastlocation = LocationServices.FusedLocationApi.getLastLocation(mgoogleApiClient);


                if (mlastlocation != null) {
                    final double latitude = mlastlocation.getLatitude();
                    final double longitute = mlastlocation.getLongitude();

                    final LatLng latLng = new LatLng(latitude, longitute);

                    if (myCurrentMarker != null) {
                        myCurrentMarker.remove();
                    }

                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.title("Current Location");
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

                    myCurrentMarker = googleMap.addMarker(markerOptions);


                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    googleMap.animateCamera(CameraUpdateFactory.zoomBy(10));
                }
            }
        });
        return rootView;
    }

    private void displayLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(),
                        android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mlastlocation = LocationServices.FusedLocationApi.getLastLocation(mgoogleApiClient);
        if (mlastlocation != null) {
            final double latitude = mlastlocation.getLatitude();
            final double longitute = mlastlocation.getLongitude();

            final LatLng latLng = new LatLng(latitude, longitute);

            if (myCurrentMarker != null) {
                myCurrentMarker.remove();
            }

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Current Location");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

            myCurrentMarker = googleMap.addMarker(markerOptions);


            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.animateCamera(CameraUpdateFactory.zoomBy(10));


        }
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private void setUpLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(),
                        android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
            }, MY_PERMISSION_REQUEST_CODE);
        } else {
            if (checkPlaySercives()) {
                buildGoogleApiClient();
                creatLocationRequest();
//        displayLocation();
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION:
                if (grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    if (checkPlaySercives()) {
                        buildGoogleApiClient();
                        creatLocationRequest();
//            displayLocation();
                    }
                }
                break;
        }
    }

    private boolean checkPlaySercives() {
        int resultCode =
                GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode))
                GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(), PLAY_SERVICES_RESOLUTION_REQUEST).show();
            else {
                Toast.makeText(getActivity(), "This device is not supported",
                        Toast.LENGTH_LONG).show();
            }
            return false;
        }
        return true;

    }

    private void buildGoogleApiClient() {
        mgoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mgoogleApiClient.connect();
    }

    private void creatLocationRequest() {
        mlocationRequest = new LocationRequest();
        mlocationRequest.setInterval(UPDATE_INTERVAL);
        mlocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mlocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mlocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}

