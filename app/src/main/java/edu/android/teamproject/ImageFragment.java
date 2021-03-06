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
                getReferenceFromUrl("gs://timproject-14aaa.appspot.com").child(DiaryItemDao.diary + getArguments().getString(DIARY_NUMBER));

        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                if(imageView != null && getActivity() != null) {
                    GlideApp.with(getActivity())
                            .load(uri)
                            .into(imageView);
                }
            }
        });

        return rootView;
    }

    @Override
    public void onDestroy() {
        // GlideApp 터지는거 처리
        super.onDestroy();
    }
}
