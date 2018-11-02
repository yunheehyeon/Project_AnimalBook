package edu.android.teamproject;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DiaryItemFragment extends Fragment implements DiaryItemDao.DiaryItemCallback {

    private FloatingActionButton btnInsert;
    private ArrayList<DiaryItem> diaryList = new ArrayList<>();
    private RecyclerView recyclerView;
    private DiaryItemAdapter adapter;

    private static HashMap<Integer, Integer> mViewPagerState = new HashMap<>();

    private DiaryItemDao dao;

    public DiaryItemFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_diary_item, container, false);

        btnInsert = view.findViewById(R.id.btnInsert);
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDiaryItemEdit(-1);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String getTime = simpleDateFormat.format(date);


        dao = DiaryItemDao.getDiaryItemInstance(this);
        diaryList = dao.upDate();
        View view = getView();
        recyclerView = view.findViewById(R.id.diaryRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new DiaryItemAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void itemCallback() {
        diaryList = dao.upDate();
        Log.i("aaa", diaryList.toString());
        adapter.notifyDataSetChanged();
    }

    class DiaryItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        class DiaryItemViewHolder extends RecyclerView.ViewHolder {

            private ViewPager viewPager;
            private TextView text, textDate, textTag;
            private Button btnFavorites, btnShare, btnUpdate, btnDelete ;


            public DiaryItemViewHolder(@NonNull View itemview) {
                super(itemview);
                viewPager = itemview.findViewById(R.id.imageContainer);
                text = itemview.findViewById(R.id.comItemTag);
                textDate = itemview.findViewById(R.id.textDate);
                textTag = itemview.findViewById(R.id.textTag);

                btnFavorites = itemview.findViewById(R.id.btnFavorites);
                btnUpdate = itemview.findViewById(R.id.btnUpdate);
                btnShare = itemview.findViewById(R.id.btnShare);
                btnDelete = itemview.findViewById(R.id.btnDelete);

//                btnDelete.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        diaryList.remove(getAdapterPosition());
//                        notifyItemRemoved(getAdapterPosition());
//                        notifyItemRangeChanged(getAdapterPosition(), diaryList.size());
//                    }
//                });
            }
        }

        
        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View diaryView = inflater.inflate(R.layout.diary_item,viewGroup, false);
            DiaryItemViewHolder holder = new DiaryItemViewHolder(diaryView);

            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
            DiaryItemViewHolder holder = (DiaryItemViewHolder) viewHolder;
            ImagesPagerAdapter pagerAdapter = new ImagesPagerAdapter(getActivity().getSupportFragmentManager(), i);
            holder.viewPager.setId(i+1000);
            holder.viewPager.setAdapter(pagerAdapter);
            holder.text.setText(diaryList.get(i).getDiaryText());
            holder.textDate.setText(diaryList.get(i).getDiaryDate());
            if(diaryList.get(i).getDiaryTag() != null) {
                holder.textTag.setText(diaryList.get(i).getDiaryTag().toString());
            }

            holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startDiaryItemEdit(i);
                }
            });

            holder.btnShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            if (mViewPagerState.containsKey(i)) {
                holder.viewPager.setCurrentItem(mViewPagerState.get(i));
            }

        }

        @Override
        public int getItemCount() {
            return diaryList.size();
        }

        @Override
        public void onViewRecycled(RecyclerView.ViewHolder viewHolder) {
            DiaryItemViewHolder holder = (DiaryItemViewHolder) viewHolder;
            mViewPagerState.put(holder.getAdapterPosition(), holder.viewPager.getCurrentItem());
            super.onViewRecycled(holder);
        }

    }

    private void startDiaryItemEdit(int i) {
        Intent intent = DiaryItemEdit.newIntent(getActivity(), i);
        startActivity(intent);
    }


    public class ImagesPagerAdapter extends FragmentPagerAdapter {

        private int diaryNumber;

        public ImagesPagerAdapter(FragmentManager fm, int diaryNumber) {
            super(fm);
            this.diaryNumber = diaryNumber;
        }

        @Override
        public Fragment getItem(int position) {
            String fileRef = diaryList.get(diaryNumber).getDiaryImages().get(position);

            return ImageFragment.newInstance(fileRef,position + 1);
        }

        @Override
        public int getCount() {
            return diaryList.get(diaryNumber).getDiaryImages().size();
        }
    }




}
