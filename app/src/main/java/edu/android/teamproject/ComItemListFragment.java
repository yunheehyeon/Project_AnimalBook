package edu.android.teamproject;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ComItemListFragment extends Fragment {

    private ComItemDao dao;

    public static final String TAG = "comitemfragment";

    public ComItemListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_com_item_list, container, false);

        Button btnInsert = view.findViewById(R.id.btnInsert);
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startComItemEdit();
            }
        });

        return view;
    }

    private void startComItemEdit() {
        Intent intent = ComItemEditActivity.newIntent(getActivity(), "1");
        startActivity(intent);
    }

    long now = System.currentTimeMillis();
    Date date = new Date(now);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    String getTime = sdf.format(date);
    List<String> tag = new ArrayList<>();
    


    @Override
    public void onStart() {
        super.onStart();

//        dao = ComItemDao.getComItemInstance();
//        tag.add("새해");
//        dao.insert(new ComItem("아이디", "연습", "anay", date, 1, tag, "연습", tag, "연습"));

        View view = getView();

        RecyclerView recyclerView = view.findViewById(R.id.comRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ComItemListAdapter adapter = new ComItemListAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

    }

    class ComItemListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        class ComItemListViewHolder extends RecyclerView.ViewHolder{

            private TextView textTitle;
            private TextView textUserId;
            private TextView textData;
            private TextView textCommentCount;
            private TextView textTag;
            private TextView textViewCount;
            private ImageView imageView;
            public ComItemListViewHolder(@NonNull View itemView) {
                super(itemView);
                textTitle = itemView.findViewById(R.id.comItemTitle);
                textUserId = itemView.findViewById(R.id.textUserId);
                textData = itemView.findViewById(R.id.textDate);
                textViewCount = itemView.findViewById(R.id.textViewCount);
                textCommentCount = itemView.findViewById(R.id.textCommentCount);
                textTag = itemView.findViewById(R.id.textTag);
                imageView = itemView.findViewById(R.id.imageView);
            }
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View itemView = inflater.inflate(R.layout.comlist_item, viewGroup, false);
            ComItemListViewHolder holder = new ComItemListViewHolder(itemView);

            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
            ComItemListViewHolder holder = (ComItemListViewHolder) viewHolder;



            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startComItemActivity(i);
                }
            });

        }

        @Override
        public int getItemCount() {
            return 15;
        }

    }

    private void startComItemActivity(int i){
        Intent intent = ComItemDetailActivity.newIntent(getActivity(), "1");
        startActivity(intent);
    }

}
