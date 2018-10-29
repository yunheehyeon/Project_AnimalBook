package edu.android.teamproject;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**

 * A simple {@link Fragment} subclass.
 */
public class MyPageFragment extends Fragment {


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
        
        dao = MyPageDao.getMyPageInstance();

        dao.insert(new MyPageProfile("연습", 15));


        btnProfileChange = getView().findViewById(R.id.btnProfileChange);

        btnProfileChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyPageFragmentActivity.class);
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
}
