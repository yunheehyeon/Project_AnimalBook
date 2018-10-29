package edu.android.teamproject;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * A simple {@link Fragment} subclass.
 */

public class GMapFragment extends Fragment {

    private GoogleMap googleMap;
    private MapView mapView;

    public GMapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_gmap, container, false);

//        MapView mapView = view.findViewById(R.id.map);
////        mapView.onCreate(savedInstanceState);
////        mapView.onResume();
//        mapView.getMapAsync(this);
//        mapView.onStart();
        return view;
    }

//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//       googleMap = googleMap;
//
////        LatLng SEOUL = new LatLng(37.56, 126.97);
////        MarkerOptions markerOptions = new MarkerOptions();
////        markerOptions.position(SEOUL);
////        markerOptions.title("서울");
////        markerOptions.snippet("한국의 수도");
////        googleMap.addMarker(markerOptions);
////
////        googleMap.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));
////        googleMap.animateCamera(CameraUpdateFactory.zoomTo(10));
//    }

}

