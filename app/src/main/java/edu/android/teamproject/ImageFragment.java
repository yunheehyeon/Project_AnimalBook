package edu.android.teamproject;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ImageFragment extends Fragment {

    private static final String POSITION = "position";

    public ImageFragment() {
        // Required empty public constructor
    }

    public static ImageFragment newInstance(int sectionNumber) {
        ImageFragment fragment = new ImageFragment();
        Bundle args = new Bundle();
        args.putInt(POSITION, sectionNumber);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_image, container, false);
        ImageView imageView = rootView.findViewById(R.id.imageView);
        switch (getArguments().getInt(POSITION)) {
            case 1:
                imageView.setImageResource(R.drawable.n1);
                break;
            case 2:
                imageView.setImageResource(R.drawable.n2);
                break;
            case 3:
                imageView.setImageResource(R.drawable.n3);
                break;
        }

        return rootView;
    }



}
