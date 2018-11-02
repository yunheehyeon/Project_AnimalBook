package edu.android.teamproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyPageEditActivity extends AppCompatActivity {

    private static final int PIC_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private int id_view;
    private Uri photoUri;
    private String imageFilePath;
    private ImageView photo;

    private Uri uploadPhoto;
    private EditText editProfileItemName;
    private Button btnProfileItemAdd;

    private MyPageDao dao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page_fragment);

        Intent intent = getIntent();

        dao = MyPageDao.getMyPageInstance(this);

        editProfileItemName = findViewById(R.id.editProfileItemName);

        btnProfileItemAdd = findViewById(R.id.btnProfileItemAdd);
        btnProfileItemAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProfileItem();
            }
        });

        photo = findViewById(R.id.profileImageView);
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMyDialog();
            }
        });

    }

    private static final int MAX_PROFILE_ITEM = 6;
    private int profileItemNum = 0;
    private List<ProfileItemLayout> profileItemLayouts = new ArrayList<>();
    private void addProfileItem() {

        if(profileItemNum < MAX_PROFILE_ITEM){
            ProfileItemLayout profileItemLayout = new ProfileItemLayout(this);
            TextView textView = profileItemLayout.findViewById(R.id.profileItem);
            textView.setText(editProfileItemName.getText().toString());
            profileItemLayouts.add(profileItemLayout);

            LinearLayout layout = findViewById(R.id.profileLayout);
            layout.addView(profileItemLayout);
            profileItemNum ++;

            editProfileItemName.setText("");

            for(final ProfileItemLayout p : profileItemLayouts){
                Button button = p.findViewById(R.id.btnProfileItemDelete);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LinearLayout layout = findViewById(R.id.profileLayout);
                        layout.removeView(p);
                        profileItemLayouts.remove(p);
                        profileItemNum--;
                    }
                });
            }

        }else {
            Toast.makeText(this, "최대 6개까지", Toast.LENGTH_SHORT).show();
        }

    }

    private void showMyDialog() {

        DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                doTakePhotoAction();
            }
        };

        DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                doTakeAlbumAction();
            }
        };

        DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        };
        new AlertDialog.Builder(MyPageEditActivity.this)
                .setTitle("업로드할 이미지 선택")
                .setPositiveButton("사진촬영", cameraListener)
                .setNeutralButton("앨범선택", albumListener)
                .setNegativeButton("취소", cancelListener)
                .show();

    }

    private void doTakePhotoAction() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }

            if (photoFile != null) {
                photoUri = FileProvider.getUriForFile(this, getPackageName(), photoFile);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, PIC_FROM_CAMERA);
            }
        }
    }

    private void doTakeAlbumAction() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "TEST_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,      /* prefix */
                ".jpg",         /* suffix */
                storageDir          /* directory */
        );
        imageFilePath = image.getAbsolutePath();
        return image;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == PICK_FROM_ALBUM) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                try {
                    photoUri = data.getData();
                    CropImage.activity(photoUri)
                            .start(this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if(requestCode == PIC_FROM_CAMERA){
            if (resultCode == RESULT_OK) {
                // start cropping activity for pre-acquired image saved on the device
                CropImage.activity(photoUri)
                        .start(this);
            }
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                addImage(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void addImage(Uri resultUri) {
        photo.setImageURI(resultUri);
        uploadPhoto = resultUri;
    }

    public void profileUpdateCancel(View view) {
        finish();
    }

    public void profileUpdate(View view) {

        String photoUri = PhotoFirebaseStorageUtil.PhotoUpload(this, uploadPhoto);
        List<MyPageProfile.ProfileItem> profileItems = new ArrayList<>();

        for(ProfileItemLayout p : profileItemLayouts) {
            TextView textView = p.findViewById(R.id.profileItem);
            EditText editText = p.findViewById(R.id.editProfileItem);
            MyPageProfile.ProfileItem profileItem = new MyPageProfile.ProfileItem(textView.getText().toString(), editText.getText().toString());
            profileItems.add(profileItem);
        }

        MyPageProfile myPageProfile = new MyPageProfile(photoUri, profileItems);
        dao.insert(myPageProfile);

    }


    class ProfileItemLayout extends ConstraintLayout {

        public ProfileItemLayout(Context context) {
            super(context);
            LayoutInflater inflater = getLayoutInflater();
            inflater.inflate(R.layout.profile_item, this, true);
        }
    }

}
