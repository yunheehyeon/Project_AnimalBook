package edu.android.teamproject;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DiaryItemFragment extends Fragment implements DiaryItemDao.DiaryItemCallback, View.OnClickListener {


    private Animation fab_open, fab_close;
    private Boolean isFabOpen = false;
    private FloatingActionButton fabInsert;
    private FloatingActionButton fabToTop, fab;


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
        View view = inflater.inflate(R.layout.fragment_diary_item, container, false);

        fab_open = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_close);

        fabInsert = view.findViewById(R.id.fabInsert);
        fabToTop = view.findViewById(R.id.fabToTop);
        fab = view.findViewById(R.id.fab);

        fab.setOnClickListener(this);
        fabToTop.setOnClickListener(this);
        fabInsert.setOnClickListener(this);

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

        if(getArguments() != null) {
            String diaryId = getArguments().getString(MainActivity.DIARY_ID);
            if (diaryId != null) {
                showDiaryItemOfDiaryId(diaryId);
            }
        }
    }

    @Override
    public void itemCallback() {
        diaryList = dao.upDate();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.fab:
                anim();
                break;
            case R.id.fabInsert:
                anim();
                startDiaryItemEdit(null);
                break;
            case R.id.fabToTop:
                anim();
                recyclerView.scrollToPosition(0);
                break;
        }
    }

    public void anim() {

        if (isFabOpen) {
            fabToTop.startAnimation(fab_close);
            fabInsert.startAnimation(fab_close);
            fabInsert.setClickable(false);
            fabToTop.setClickable(false);
            isFabOpen = false;
        } else {
            fabToTop.startAnimation(fab_open);
            fabInsert.startAnimation(fab_open);
            fabToTop.setClickable(true);
            fabInsert.setClickable(true);
            isFabOpen = true;
        }
    }

    class DiaryItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        class DiaryItemViewHolder extends RecyclerView.ViewHolder {

            private ViewPager viewPager;
            private TextView text, textDate, textTag, textTitle;
            private Button btnShare, btnUpdate, btnDelete;
            private ImageView btnFavorites;

            public DiaryItemViewHolder(@NonNull View itemview) {
                super(itemview);
                textTitle = itemview.findViewById(R.id.textDiaryTitle);
                viewPager = itemview.findViewById(R.id.imageContainer);
                text = itemview.findViewById(R.id.textDiary);
                textDate = itemview.findViewById(R.id.textDate);
                textTag = itemview.findViewById(R.id.textTag);

                btnFavorites = itemview.findViewById(R.id.btnFavorites);
                btnUpdate = itemview.findViewById(R.id.btnUpdate);
                btnShare = itemview.findViewById(R.id.btnShare);
                btnDelete = itemview.findViewById(R.id.btnDelete);

            }
        }


        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View diaryView = inflater.inflate(R.layout.diary_item, viewGroup, false);
            DiaryItemViewHolder holder = new DiaryItemViewHolder(diaryView);

            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
            DiaryItemViewHolder holder = (DiaryItemViewHolder) viewHolder;
            if(diaryList.get(i).getDiaryImages() == null){
            }else {
                ImagesPagerAdapter pagerAdapter = new ImagesPagerAdapter(getChildFragmentManager(), i);
                holder.viewPager.setId(i + 1000);
                holder.viewPager.setAdapter(pagerAdapter);
            }
            holder.textTitle.setText("제목 : " + diaryList.get(i).getDiaryTitle());
            holder.text.setText(diaryList.get(i).getDiaryText());
            holder.textDate.setText("등록일 : " + diaryList.get(i).getDiaryDate());

            if (diaryList.get(i).getDiaryTag() != null) {
                StringBuilder builder = new StringBuilder();
                builder.append("Tag : ");
                for (String s : diaryList.get(i).getDiaryTag()) {
                    if (s != null) {
                        builder.append(s);
                        holder.textTag.setText(builder);
                    }
                    builder.append(", ");
                }
            }
            if (diaryList.get(i).isBookMark()) {
                holder.btnFavorites.setBackgroundResource(R.drawable.book_mark_on);
            } else {
                holder.btnFavorites.setBackgroundResource(R.drawable.book_mark_off);
            }
            holder.btnFavorites.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (diaryList.get(i).isBookMark()) {
                        diaryList.get(i).setBookMark(false);
                        Toast.makeText(getActivity(), "북마크 해제", Toast.LENGTH_SHORT).show();
                    } else {
                        diaryList.get(i).setBookMark(true);
                        Toast.makeText(getActivity(), "북마크 설정", Toast.LENGTH_SHORT).show();
                    }
                    dao.updateBookmark(diaryList.get(i));
                }
            });

            holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startDiaryItemEdit(diaryList.get(i).getDiaryId());
                }
            });

            holder.btnShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("삭제 확인");
                    builder.setMessage("삭제할까요?");
                    builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dao.delete(diaryList.get(i));
                        }
                    });

                    builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog dlg = builder.create();
                    dlg.show();
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

    private void startDiaryItemEdit(String diaryId) {
        Intent intent = DiaryItemEdit.newIntent(getActivity(), diaryId);
        startActivity(intent);
    }

    public void showDiaryItemOfDiaryId(String diaryId){
        for(int i = 0; i < diaryList.size(); i++){
            if(diaryList.get(i).getDiaryId().equals(diaryId) ){
                Log.i("aaa", "position"+ i);
                recyclerView.scrollToPosition(i);
            }
        }
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
            Fragment fragment = ImageFragment.newInstance(fileRef, position + 1);
            return fragment;
        }

        @Override
        public int getCount() {
            return diaryList.get(diaryNumber).getDiaryImages().size();
        }
    }


}
