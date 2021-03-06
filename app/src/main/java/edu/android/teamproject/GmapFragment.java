package edu.android.teamproject;


import android.content.Context;
import android.content.Intent;


import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;


import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


import com.bumptech.glide.util.LogTime;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class GmapFragment extends Fragment {

    private static final String TAG = "edu.android.teamproject";

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
//
//                Collections.sort(itemList, new Comparator<GmapListItem>() {
//                    @Override
//                    public int compare(GmapListItem o1, GmapListItem o2) {
//                        List<Address> list1, list2 = null;
//                        int ret = 0;
//                        double lat1 = 0;
//                        double lng1 = 0;
//                        double lat2 = 0;
//                        double lng2 = 0;
//                        try {
//                            list1 = geocoder.getFromLocationName(o1.getAddr(), 1);
//                            lat1 = list1.get(0).getLatitude();
//                            lng1 = list1.get(0).getLongitude();
//                            list2 = geocoder.getFromLocationName(o2.getAddr(), 1);
//                            lat2 = list2.get(0).getLatitude();
//                            lng2 = list2.get(0).getLongitude();
//                        } catch (Exception e) {
//                            try {
//                                list1 = geocoder.getFromLocationName(o1.getAddr_old(), 1);
//                                lat1 = list1.get(0).getLatitude();
//                                lng1 = list1.get(0).getLongitude();
//                                list2 = geocoder.getFromLocationName(o2.getAddr_old(), 1);
//                                lat2 = list2.get(0).getLatitude();
//                                lng2 = list2.get(0).getLongitude();
//                                e.printStackTrace();
//                            } catch (Exception e1) {
//                                e1.printStackTrace();
//                            }
//
//                        }
//                        double num1 = distanceByDegreeAndroid(myLat, myLng, lat1, lng1);
//
//                        double num2 = distanceByDegreeAndroid(myLat, myLng, lat2, lng2);
//                        Log.i(TAG, num1 + "|" + num2);
//                        if(num1 < num2){
//                            ret =  1;
//                        }else if(num1 > num2){
//                            ret = -1;
//                        }
//                        return ret;
//                    }
//
//                });

            gmapListAdapter.notifyDataSetChanged();
        }
    };

    Button btn, btn_Asc;
    private ListView listView;
    private TextView tv;
    private Request request;
    String name, addr_old, addr, lsind_Type, tel, mapx, mapy;
    double myLat, myLng;
    Geocoder geocoder;

    public GmapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
       geocoder = new Geocoder(getActivity());

        super.onStart();
    }

    private GmapListAdapter gmapListAdapter;
    ArrayList<GmapListItem> itemList = new ArrayList<GmapListItem>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_gmap, container, false);

        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        // GPS 프로바이더 사용가능여부
        locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 네트워크 프로바이더 사용가능여부
        locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double lat = location.getLatitude();
                double lng = location.getLongitude();

                myLat = lat;
                myLng = lng;
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 1, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1, locationListener);


        listView = (ListView) view.findViewById(R.id.list1);
        gmapListAdapter = new GmapListAdapter(itemList);
        listView.setAdapter(gmapListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startGmapActivity(position);
            }
        });


        try {
            run();
        } catch (Exception e) {
            e.getMessage();
        }

        btn_Asc = view.findViewById(R.id.btn_Asc);

        btn_Asc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Comparator<GmapListItem> textAsc = new Comparator<GmapListItem>() {
                    @Override
                    public int compare(GmapListItem listItem1, GmapListItem listItem2) {
                        return listItem1.getName().compareTo(listItem2.getName());
                    }
                };

//                Comparator comparator = Collections.reverseOrder(); //역순
                Collections.sort(itemList, textAsc);
                handler.sendEmptyMessage(0);

            }


        });
        return view;
    }

    //안드로이드 - 두지점(위도,경도) 사이의 거리
    public double distanceByDegreeAndroid(double _latitude1, double _longitude1, double _latitude2, double _longitude2) {
        Location startPos = new Location("PointA");
        Location endPos = new Location("PointB");

        startPos.setLatitude(_latitude1);
        startPos.setLongitude(_longitude1);
        endPos.setLatitude(_latitude2);
        endPos.setLongitude(_longitude2);

        double distance = startPos.distanceTo(endPos);

        return distance;
    }

    private void startGmapActivity(int position) {
        Intent intent = new Intent(getActivity(), GmapActivity.class);
        Geocoder geocoder = new Geocoder(getActivity());
        List<Address> list = null;
        try {
            list = geocoder.getFromLocationName(itemList.get(position).getAddr(), 1);

            double lat = list.get(0).getLatitude();
            double lng = list.get(0).getLongitude();
            Object[] objects = {lat, lng, itemList.get(position).getName(), myLat, myLng};

            intent.putExtra("1", objects);

        } catch (Exception e) {
            try {
                list = geocoder.getFromLocationName(itemList.get(position).getAddr_old(), 1);

                double lat = list.get(0).getLatitude();
                double lng = list.get(0).getLongitude();

                Object[] objects = {lat, lng, itemList.get(position).getName(), myLat, myLng};

                intent.putExtra("1", objects);

            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        startActivity(intent);
    }

    private final OkHttpClient client = new OkHttpClient();


    public void run() throws Exception {

        request = new Request.Builder()
                .url("http://openapi.seoul.go.kr:8088/554a7a6b426b6a773731616b426a52/json/vtrHospitalInfo/1/100/")
                .get()
                .addHeader("cache-control", "no-cache")
                .addHeader("postman-token", "6e96ef76-aa93-c287-0959-a2dc6d9035e2")
                .build();
//        Log.i(TAG, "requset=" + request);

        Call call = client.newCall(request);
//        Log.i(TAG, "newCall=" + call);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
//                Log.i(TAG, "failed: " + e.getMessage());
            }

            @Override
            public void onResponse(Response response) throws IOException {
//                Log.i(TAG, "ok:" + response.body().string());

                response = client.newCall(request).execute();
//                Log.i(TAG, "response=" + response);

                if (!response.isSuccessful()) {
//                    Log.i(TAG, "failed");
                }
                String jsonString = response.body().string();
                JsonParser jsonParser = new JsonParser();

                try {
                    JsonObject jsonObject = (JsonObject) jsonParser.parse(jsonString);
                    JsonObject hos_Info = jsonObject.getAsJsonObject("vtrHospitalInfo");

                    JsonArray row = hos_Info.getAsJsonArray("row");
//                    Log.i(TAG, "row size = " + row.size());
                    for (int i = 0; i < row.size(); i++) {
                        JsonObject rowInfo = (JsonObject) row.get(i);
//                        Log.i(TAG, rowInfo.toString());
//                        List<Address> list = null;
                        name = (String) rowInfo.get("NM").toString();
                        addr = (String) rowInfo.get("ADDR").toString();
                        addr_old = rowInfo.get("ADDR_OLD").toString();
                        lsind_Type = (String) rowInfo.get("LSIND_TYPE").toString();
                        tel = (String) rowInfo.get("TEL").toString();
                        mapx = (String) rowInfo.get("XCODE").toString();
                        mapy = (String) rowInfo.get("YCODE").toString();
                        double x = 0.0;
                        double y = 0.0;
//                        try {
//                            list = geocoder.getFromLocationName(addr, 1);
//                            Log.i(TAG,"" + list.get(0).getLatitude());
//                            x = list.get(0).getLatitude();
//                            y = list.get(0).getLongitude();
//                        } catch (IOException e) {
//                            list = geocoder.getFromLocationName(addr_old, 1);
//                            Log.i(TAG,"" + list.get(0).getLatitude());
//                            x = list.get(0).getLatitude();
//                            y = list.get(0).getLongitude();
//                        }
                        gmapListAdapter.addItem(name, addr_old, addr, lsind_Type, tel, x, y);
                    }
                    handler.sendEmptyMessage(0);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
