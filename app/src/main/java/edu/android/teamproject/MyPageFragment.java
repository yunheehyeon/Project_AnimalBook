package edu.android.teamproject;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**

 * A simple {@link Fragment} subclass.
 */
public class MyPageFragment extends Fragment {


    private MyPageDao dao;

    public static final String KEY = "msg";

    private Button btnProfileChange;

    public MyPageFragment() {
        // Required empty public constructor
    }



    @Override
    public void onStart() {
        super.onStart();

        btnProfileChange = getView().findViewById(R.id.btnProfileChange);

        btnProfileChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyPageFragmentActivity.class);
//                String msg = null;
//                intent.putExtra(KEY, msg);
                startActivity(intent);
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

}
