package edu.android.teamproject;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ComItemListFragment extends Fragment implements ComItemDao.ComItemCallback {

    private ComItemDao dao;
    private List<ComItem> comItems = new ArrayList<>();

    public static final String TAG = "comitemfragment";
    private ComItemListAdapter adapter;

    private Spinner spinner;

    public ComItemListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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

    @Override
    public void onStart() {
        super.onStart();

        View view = getView();

        RecyclerView recyclerView = view.findViewById(R.id.comRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ComItemListAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        spinner = view.findViewById(R.id.spinner);

        dao = ComItemDao.getComItemInstance(this);
        dateCallback();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    dateCallback();
                }
                if(position == 1){
                    comItems = dao.update();
                    Collections.sort(comItems, new Comparator<ComItem>() {
                        @Override
                        public int compare(ComItem o1, ComItem o2) {
                            int num1 = o1.getViewCount();
                            int num2 = o2.getViewCount();
                            if(num1 < num2){
                                return 1;
                            }else if(num1 > num2){
                                return -1;
                            }
                            return 0;
                        }
                    });

                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void dateCallback() {
        comItems = dao.update();
        Collections.sort(comItems, new Comparator<ComItem>() {
            @Override
            public int compare(ComItem o1, ComItem o2) {
                String str1 = stringParsing(o1.getDate());
                String str2 = stringParsing(o2.getDate());
                return str2.compareTo(str1);
            }
        });

        adapter.notifyDataSetChanged();
    }

    private String stringParsing(String string){
        String[] strs = string.split("/");
        StringBuilder builder = new StringBuilder();
        for(String s : strs){
            builder.append(s);
        }
        strs = builder.toString().split(":");
        builder = new StringBuilder();
        for(String s : strs){
            builder.append(s);
        }
        return builder.toString();
    }


    class ComItemListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        class ComItemListViewHolder extends RecyclerView.ViewHolder{

            private TextView textTitle;
            private TextView textUserId;
            private TextView textDate;
            private TextView textCommentCount;
            private TextView textTag;
            private TextView textViewCount;
            private ImageView imageView;

            public ComItemListViewHolder(@NonNull View itemView) {
                super(itemView);
                textTitle = itemView.findViewById(R.id.comItemTitle);
                textUserId = itemView.findViewById(R.id.textUserId);
                textDate = itemView.findViewById(R.id.textDate);
                textViewCount = itemView.findViewById(R.id.textViewCount);
                textCommentCount = itemView.findViewById(R.id.textCommentCount);
                textTag = itemView.findViewById(R.id.textTag);
                imageView = itemView.findViewById(R.id.isImage);
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

            holder.textTitle.setText(comItems.get(i).getTitle());
            holder.textUserId.setText("작성자 : " + comItems.get(i).getUserEmail());
            holder.textViewCount.setText("조회수 : " + String.valueOf(comItems.get(i).getViewCount()));
            holder.textDate.setText("등록일 : " + comItems.get(i).getDate());
            holder.textCommentCount.setText("댓글 : " + String.valueOf(comItems.get(i).getCommentCount()));
            if(comItems.get(i).getItemId() != null){
                holder.imageView.setImageResource(R.drawable.isimage);
            }
            StringBuilder builder = new StringBuilder();
            builder.append("Tag : ");
            if(comItems.get(i).getTag() != null) {
                for (String s : comItems.get(i).getTag()) {
                    if (s != null) {
                        builder.append(s);
                        holder.textTag.setText(builder);
                    }
                    builder.append(", ");
                }
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startComItemActivity(comItems.get(i).getItemId());
                }
            });

        }

        @Override
        public int getItemCount() {
            return comItems.size();
        }

    }

    private void startComItemActivity(String comId){
        Intent intent = ComItemDetailActivity.newIntent(getActivity(), comId);
        startActivity(intent);
    }

}
