package edu.android.teamproject;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


/**
 * A simple {@link Fragment} subclass.
 */
public class ImageFragment extends Fragment {

    private static final String POSITION = "position";
    private static final String DIARY_NUMBER = "diaryNumber";
    public ImageFragment() {
        // Required empty public constructor
    }

    public static ImageFragment newInstance(String fileRef,  int sectionNumber) {
        ImageFragment fragment = new ImageFragment();
        Bundle args = new Bundle();
        args.putInt(POSITION, sectionNumber);
        args.putString(DIARY_NUMBER, fileRef);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_image, container, false);
        final ImageView imageView = rootView.findViewById(R.id.imageView);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.
                getReferenceFromUrl("gs://timproject-14aaa.appspot.com").child("diary/" + getArguments().getString(DIARY_NUMBER));

        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
//                GlideApp.with(getActivity())
//                        .load(uri)
//                        .into(imageView);
            }
        });

//        switch (getArguments().getInt(POSITION)) {
//            case 1:
//                imageView.setImageResource(R.drawable.n1);
//                break;
//            case 2:
//                imageView.setImageResource(R.drawable.n2);
//                break;
//            case 3:
//                imageView.setImageResource(R.drawable.n3);
//                break;
//        }

        return rootView;
    }



}
