package edu.android.teamproject;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyProfile extends Fragment {

    private ImageView myImage;
    private LinearLayout myLinear;

    private MyPageDao dao;


    public MyProfile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);

        myImage = view.findViewById(R.id.myImage);
        myLinear = view.findViewById(R.id.myLinear);

        dao.update();

        return view;
    }

}
