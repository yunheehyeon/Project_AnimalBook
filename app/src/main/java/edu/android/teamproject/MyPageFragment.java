package edu.android.teamproject;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

/**

 * A simple {@link Fragment} subclass.
 */
public class MyPageFragment extends Fragment
        implements MyPageDao.DataCallback, ComItemDao.ComItemCallback, DiaryItemDao.DiaryItemCallback {

    interface MyPageCallback{
        void BookMarkDataCallback(String diaryId);
    }

    private MyPageCallback myPageCallback;

    private MyPageDao dao;
    private ComItemDao comItemDao;
    private DiaryItemDao diaryItemDao;

    public static final String KEY = "msg";

    private Button btnProfileChange, btnMyPosting, btnDiaryBM, btnHospitalBM;

    public MyPageFragment() {
        // Required empty public constructor
    }

    myPosting myPosting;
    myDiaryBM myDiaryBM;
    LinearLayout lLPostingView, lLDiaryBMView;

    boolean b1,b2,b3 = false;

    private List<ComItem> comItems = new ArrayList<>();
    private List<DiaryItem> diaryItems = new ArrayList<>();

    @Override
    public void onAttach(Context context) {
        if(context instanceof MyPageCallback){
            myPageCallback = (MyPageCallback) context;
        }
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_page, container, false);

        return view;
    }
    @Override
    public void onStart() {
        super.onStart();

        btnProfileChange = getView().findViewById(R.id.btnProfileChange);

        btnProfileChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyPageEditActivity.class);
                startActivity(intent);
            }
        });
        btnMyPosting = getView().findViewById(R.id.btnMyPosting);
        btnMyPosting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickMyPosting();
            }
        });

        btnDiaryBM = getView().findViewById(R.id.btnDiaryBM);
        btnDiaryBM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickDiaryBM();
            }
        });

        btnHospitalBM = getView().findViewById(R.id.btnHospitalBM);
        btnHospitalBM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickHospitalBM();
            }
        });

        dao = MyPageDao.getMyPageInstance(this);
        if(dao.update() != null) {
            showMyProfile(dao.update());
        }
        comItemDao = ComItemDao.getComItemInstance(this);
        diaryItemDao = DiaryItemDao.getDiaryItemInstance(this);

    }

    private void onClickMyPosting() {
        lLPostingView = getView().findViewById(R.id.lLPostingView);

        if (b1 == false) {
            btnMyPosting.setText("-");
            b1 = true;
            comItems = comItemDao.myComItemUpdate();

            for (int i = 0; i < comItems.size(); i++) {
                myPosting = new myPosting(getActivity());
                TextView textTitle = myPosting.findViewById(R.id.comItemTitle);
                TextView textUserId = myPosting.findViewById(R.id.textUserId);
                TextView textDate = myPosting.findViewById(R.id.textDate);
                TextView textViewCount = myPosting.findViewById(R.id.textViewCount);
                TextView textCommentCount = myPosting.findViewById(R.id.textCommentCount);
                TextView textTag = myPosting.findViewById(R.id.textTag);
                ImageView imageView = myPosting.findViewById(R.id.isImage);

                textTitle.setText(comItems.get(i).getTitle());
                textUserId.setText("작성자 : " + comItems.get(i).getUserEmail());
                textViewCount.setText("조회수 : " + String.valueOf(comItems.get(i).getViewCount()));
                textDate.setText("등록일 : " + comItems.get(i).getDate());
                textCommentCount.setText("댓글 : " + String.valueOf(comItems.get(i).getCommentCount()));
                if(comItems.get(i).getItemId() != null){
                    imageView.setImageResource(R.drawable.isimage);
                }
                StringBuilder builder = new StringBuilder();
                builder.append("Tag : ");
                for(String s : comItems.get(i).getTag()) {
                    if(s != null) {
                        builder.append(s);
                        textTag.setText(builder);
                    }
                    builder.append(", ");
                }

                final int temp = i;
                myPosting.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = ComItemDetailActivity.newIntent(getActivity(), comItems.get(temp).getItemId());
                        startActivity(intent);
                    }
                });

                lLPostingView.addView(myPosting);
            }
        } else {
            b1 = false;
            lLPostingView.removeAllViews();
            btnMyPosting.setText("+");
        }
    }

    private void onClickDiaryBM() {

        lLDiaryBMView = getView().findViewById(R.id.lLDiaryBMView);
        if (b2 == false) {
            btnDiaryBM.setText("-");
            b2 = true;

            diaryItems = diaryItemDao.updateMyDiaryList();

            for (int i = 0; i < diaryItems.size(); i++) {
                myDiaryBM = new myDiaryBM(getActivity());
                TextView textTitle = myDiaryBM.findViewById(R.id.diaryBmTitle);
                TextView textDate = myDiaryBM.findViewById(R.id.diaryBmDate);
                TextView textTag = myDiaryBM.findViewById(R.id.diaryBmTag);

                textTitle.setText("제목 : " + diaryItems.get(i).getDiaryTitle());
                textDate.setText("등록일 : " + diaryItems.get(i).getDiaryDate());

                if(diaryItems.get(i).getDiaryTag() != null) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("Tag : ");
                    for(String s : diaryItems.get(i).getDiaryTag()) {
                        if(s != null) {
                            builder.append(s);
                            textTag.setText(builder);
                        }
                        builder.append(", ");
                    }
                }
                final int temp = i;
                myDiaryBM.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myPageCallback.BookMarkDataCallback(diaryItems.get(temp).getDiaryId());
                    }
                });

                lLDiaryBMView.addView(myDiaryBM);
            }
        } else {
            b2 = false;
            lLDiaryBMView.removeAllViews();
            btnDiaryBM.setText("+");
        }
    }


    private void onClickHospitalBM() {

    }

    @Override
    public void proFileCallback() {
        showMyProfile(dao.update());
    }

    @Override
    public void proFileImageCallback() {
        View view = getView();
        if (view != null) {
            ImageView myProfileImage = view.findViewById(R.id.imageProfile);
            myProfileImage.setImageBitmap(dao.updateImage());
        } else {
        }
    }

    @Override
    public void dateCallback() {
        comItems = comItemDao.myComItemUpdate();
    }

    @Override
    public void itemCallback() {
            diaryItems = diaryItemDao.updateMyDiaryList();
    }

    private void showMyProfile(MyPageProfile myPageProfile) {

        LinearLayout layout = getView().findViewById(R.id.myProfileLayout);
        layout.removeAllViews();
        for(MyPageProfile.ProfileItem p : myPageProfile.getProfileItems()){
            MyProfileItemLayout myProfileItemLayout = new MyProfileItemLayout(getActivity());
            TextView myProfileItem = myProfileItemLayout.findViewById(R.id.myProfileItem);
            TextView myProfileText = myProfileItemLayout.findViewById(R.id.myProfileText);
            myProfileItem.setText(p.getProfileItemName());
            myProfileText.setText(p.getProfileItemText());
            layout.addView(myProfileItemLayout);

            dao.photoDownload(myPageProfile.getPhotoUri());
        }
    }

    class MyProfileItemLayout extends LinearLayout {

        public MyProfileItemLayout(Context context) {
            super(context);
            LayoutInflater inflater = getLayoutInflater();
            inflater.inflate(R.layout.myprofile_item, this, true);
        }
    }
    class myDiaryBM extends CardView {
        public myDiaryBM(Context context) {
            super(context);
            LayoutInflater inflater1 = getLayoutInflater();
            inflater1.inflate(R.layout.mypage_diary_item, this, true);
        }
    }
    class myPosting extends CardView {
        public myPosting(Context context) {
            super(context);
            LayoutInflater inflater = getLayoutInflater();
            inflater.inflate(R.layout.comlist_item, this, true);
        }
    }
}

