package edu.android.teamproject;


import android.content.Intent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class DiaryItemFragment extends Fragment {

    private ViewPager viewPager;
    private ImageView imageView;
    private TextView text, textDate, textTag;
    private Button btnFavorites, btnShare, btnUpdate, btnDelete ;
    private FloatingActionButton btnInsert;
    private ArrayList<DiaryItem> diaryList;



    HashMap<Integer, Integer> mViewPagerState = new HashMap<>();

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
                startDiaryItemEdit();
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

        List<String> tag = new ArrayList<>();

        dao = DiaryItemDao.getDiaryItemInstance();


         dao.insert(new DiaryItem(null,"안녕",date,tag,0,null));






        View view = getView();

        RecyclerView recyclerView = view.findViewById(R.id.diaryRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        DiaryItemAdapter adapter = new DiaryItemAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);


    }

    class DiaryItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        class DiaryItemViewHolder extends RecyclerView.ViewHolder {

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


                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startDiaryItemEdit();
                    }
                });

//                btnDelete.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        diaryList.remove(getAdapterPosition());
//                        notifyItemRemoved(getAdapterPosition());
//                        notifyItemRangeChanged(getAdapterPosition(), diaryList.size());
//                    }
//                });

                btnShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
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
            ImagesPagerAdapter pagerAdapter = new ImagesPagerAdapter(getActivity().getSupportFragmentManager());
            viewPager.setId(i+1000);
            viewPager.setAdapter(pagerAdapter);

            if (mViewPagerState.containsKey(i)) {
                viewPager.setCurrentItem(mViewPagerState.get(i));
            }

        }

        @Override
        public int getItemCount() {
            return 3;
        }

        @Override
        public void onViewRecycled(RecyclerView.ViewHolder holder) {
            mViewPagerState.put(holder.getAdapterPosition(), viewPager.getCurrentItem());
            super.onViewRecycled(holder);
        }

    }

    private void startDiaryItemEdit() {
        Intent intent = DiaryItemEdit.newIntent(getActivity(), "1");
        startActivity(intent);
    }


    public class ImagesPagerAdapter extends FragmentPagerAdapter {

        public ImagesPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ImageFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 3;
        }
    }


}
