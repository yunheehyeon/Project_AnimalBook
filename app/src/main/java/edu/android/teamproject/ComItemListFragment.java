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
public class ComItemListFragment extends Fragment implements ComItemDao.ComItemCallback {

    private ComItemDao dao;
    private List<ComItem> comItems = new ArrayList<>();

    public static final String TAG = "comitemfragment";
    private ComItemListAdapter adapter;


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

        dao = ComItemDao.getComItemInstance(this);
        dateCallback();
    }

    @Override
    public void dateCallback() {
        comItems = dao.update();
        adapter.notifyDataSetChanged();
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
            for(String s : comItems.get(i).getTag()) {
                if(s != null) {
                    builder.append(s);
                    holder.textTag.setText(builder);
                }
                builder.append(", ");
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startComItemActivity(i);
                }
            });

        }

        @Override
        public int getItemCount() {
            return comItems.size();
        }

    }

    private void startComItemActivity(int i){
        Intent intent = ComItemDetailActivity.newIntent(getActivity(), i);
        startActivity(intent);
    }

}
