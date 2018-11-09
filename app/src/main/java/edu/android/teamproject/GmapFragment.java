package edu.android.teamproject;


import android.content.Intent;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;


import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


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
            gmapListAdapter.notifyDataSetChanged();
        }
    };

    Button btn, btn_Asc;
    private ListView listView;
    private TextView tv;
    private Request request;
    private List<GmapListItem> listItems;
    String name, addr, lsind_Type, tel, mapx, mapy;


    public GmapFragment() {
        // Required empty public constructor
    }

    private GmapListAdapter gmapListAdapter;
    ArrayList<GmapListItem> itemList = new ArrayList<GmapListItem>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_gmap, container, false);

        listView = (ListView) view.findViewById(R.id.list1);
        gmapListAdapter = new GmapListAdapter(itemList);
        listView.setAdapter(gmapListAdapter);

        btn = view.findViewById(R.id.btnStartGmapActivity);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGmapActivity();
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
                Collections.sort(itemList,textAsc);
                handler.sendEmptyMessage(0);

            }


        });
        return view;
    }

        private void startGmapActivity () {
            Intent intent = new Intent(getActivity(), GmapActivity.class);

            startActivity(intent);
        }

        private final OkHttpClient client = new OkHttpClient();

        public void run () throws Exception {
            request = new Request.Builder()
                    .url("http://openapi.seoul.go.kr:8088/554a7a6b426b6a773731616b426a52/json/vtrHospitalInfo/1/1000/")
                    .get()
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "6e96ef76-aa93-c287-0959-a2dc6d9035e2")
                    .build();
            Log.i(TAG, "requset=" + request);

            Call call = client.newCall(request);
            Log.i(TAG, "newCall=" + call);


            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    Log.i(TAG, "failed: " + e.getMessage());
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    Log.i(TAG, "ok:" + response.body().string());

                    response = client.newCall(request).execute();
                    Log.i(TAG, "response=" + response);

                    if (!response.isSuccessful()) {
                        Log.i(TAG, "failed");

                    }


                    String jsonString = response.body().string();
                    JsonParser jsonParser = new JsonParser();

                    try {
                        JsonObject jsonObject = (JsonObject) jsonParser.parse(jsonString);
                        JsonObject hos_Info = jsonObject.getAsJsonObject("vtrHospitalInfo");

                        JsonArray row = hos_Info.getAsJsonArray("row");
                        Log.i(TAG, "row size = " + row.size());
                        for (int i = 0; i < row.size(); i++) {
                            JsonObject rowInfo = (JsonObject) row.get(i);
                            Log.i(TAG, rowInfo.toString());

                            name = (String) rowInfo.get("NM").toString();
                            addr = (String) rowInfo.get("ADDR").toString();
                            lsind_Type = (String) rowInfo.get("LSIND_TYPE").toString();
                            tel = (String) rowInfo.get("TEL").toString();
                            mapx = (String) rowInfo.get("XCODE").toString();
                            mapy = (String) rowInfo.get("YCODE").toString();

                            gmapListAdapter.addItem(name, addr, lsind_Type, tel);
                        }

                        handler.sendEmptyMessage(0);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            });


        }


    }
