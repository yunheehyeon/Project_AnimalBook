package edu.android.teamproject;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**

 * A simple {@link Fragment} subclass.
 */
public class MyPageFragment extends Fragment implements MyPageDao.DataCallback {


    private MyPageDao dao;

    public static final String KEY = "msg";

    private Button btnProfileChange, btnMyPosting, btnDiaryBM, btnHospitalBM;

    public MyPageFragment() {
        // Required empty public constructor
    }
    myPosting myPosting;
    myDiaryBM myDiaryBM;
    LinearLayout lLPostingView, lLDiaryBMView;
    private int count;
    boolean b1,b2,b3 = false;
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

        dao = MyPageDao.getMyPageInstance();

        dao.update(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_page, container, false);

        return view;
    }

    class myPosting extends CardView {
        public myPosting(Context context) {
            super(context);
            LayoutInflater inflater = getLayoutInflater();
            inflater.inflate(R.layout.comlist_item, this, true);
        }
    }
    private void onClickMyPosting() {
        if (b1 == false) {
            btnMyPosting.setText("-");
            b1 = true;
            for (int i = 0; i < 5; i++) {
                myPosting= new myPosting(getActivity());
                lLPostingView = (LinearLayout) getView().findViewById(R.id.lLPostingView);
                lLPostingView.addView(myPosting);
            }
        } else {
            b1 = false;
            lLPostingView.removeAllViews();
            btnMyPosting.setText("+");
        }
        if (b1 == false) {
            lLPostingView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });

        }
    }
    class myDiaryBM extends CardView {
        public myDiaryBM(Context context) {
            super(context);
            LayoutInflater inflater1 = getLayoutInflater();
            inflater1.inflate(R.layout.diary_item, this, true);
        }
    }
    private void onClickDiaryBM() {
        if (b2 == false) {
            btnDiaryBM.setText("-");
            b2 = true;
            for (int i = 0; i < 5; i++) {
                myDiaryBM = new myDiaryBM(getActivity());
                lLDiaryBMView = (LinearLayout) getView().findViewById(R.id.lLDiaryBMView);
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
    public void proFileCallback(MyPageProfile myPageProfile) {
        showMyProfile(myPageProfile);
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
        }

        final ImageView myProfileImage = getView().findViewById(R.id.imageProfile);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://timproject-14aaa.appspot.com").child("images/" + myPageProfile.getPhotoUri());

        final long ONE_MEGABYTE = 1024 * 1024;
        storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray( bytes, 0, bytes.length );
                myProfileImage.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });
        
    }

    class MyProfileItemLayout extends LinearLayout {

        public MyProfileItemLayout(Context context) {
            super(context);
            LayoutInflater inflater = getLayoutInflater();
            inflater.inflate(R.layout.myprofile_item, this, true);
        }
    }




}
