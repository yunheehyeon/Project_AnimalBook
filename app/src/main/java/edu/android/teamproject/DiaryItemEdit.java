package edu.android.teamproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import java.util.Map;
import java.util.TreeMap;

public class  DiaryItemEdit extends AppCompatActivity implements View.OnClickListener {

    private static final int PIC_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;

    private static final int MAX_TAG_ITEM = 6;
    private static final int MAX_PHOTO = 3;
    private static final int DIARY_ADD = -1;

    private int id_view;
    private String imageFilePath;
    private Uri photoUri;
    private int photoNum = 0;

    private int tagNum = 0;
    private ArrayList<TagItem> tagItems = new ArrayList<>();
    private ArrayList<PhotoItemLayout> photoItemLayouts = new ArrayList<>();
    private Map<Integer, Uri> photoItems = new TreeMap<>();

    private ArrayList<String> photoUris = new ArrayList<>();
    private ArrayList<String> tagTexts = new ArrayList<>();

    private DiaryItemDao dao;

    public static final String DIARY_POSITION = "diaryPosition";

    private EditText editTag, editText, editTitle;
    private Button btnInsert, btnPlusTag, btnConfirm, btnCancel;


    public static Intent newIntent(Context context, int diaryPosition) {
        Intent intent = new Intent(context, DiaryItemEdit.class);
        intent.putExtra(DIARY_POSITION, diaryPosition);

        return  intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("다이어리 글쓰기");
        setContentView(R.layout.activity_diary_item_edit);

        dao = DiaryItemDao.getDiaryItemInstance(this);

        editTitle = findViewById(R.id.editTitle);
        editText = findViewById(R.id.editText);

        btnInsert = findViewById(R.id.btnInsert);
        btnInsert.setOnClickListener(this);

        btnPlusTag = findViewById(R.id.btnPlusTag);
        editTag = findViewById(R.id.editTag);

        btnConfirm = findViewById(R.id.btnConfirm);
        btnCancel = findViewById(R.id.btnCancel);

        //TedPermission 라이브러리 -> 카메라 권한 획득 추가

        btnPlusTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTagItem();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diaryItemUpdate();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        int diaryPosition = getIntent().getIntExtra(DIARY_POSITION, DIARY_ADD);
        if(diaryPosition != DIARY_ADD){
            setDiaryItem(diaryPosition);
        }

    }

    private void setDiaryItem(int diaryPosition) {
        List<DiaryItem> diaryItems = dao.upDate();
        DiaryItem diaryItem = diaryItems.get(diaryPosition);

    }

    private void diaryItemUpdate() {
        DiaryItem diaryItem = new DiaryItem();
        diaryItem.setDiaryTitle(editTitle.getText().toString());
        diaryItem.setDiaryText(editText.getText().toString());
        Log.i("aaa", photoItems.toString());
        List<Uri> temp = new ArrayList<>();
        for(int i = 0; i < photoItemLayouts.size(); i++) {
            temp.add(photoItems.get(photoItemLayouts.get(i).getId()));
        }
        //photoUris.add(PhotoFirebaseStorageUtil.PhotoUpload(this, temp));
        diaryItem.setDiaryImages(PhotoFirebaseStorageUtil.PhotoUpload(this, temp));
        diaryItem.setDiaryTag(tagTexts);

        dao.insert(diaryItem);
    }


    private void addTagItem() {
        if(tagNum < MAX_TAG_ITEM) {
            TagItem tagItem = new TagItem(DiaryItemEdit.this);
            tagItem.setId(tagNum+100);
            tagItems.add(tagItem);
            TextView textTag = tagItem.findViewById(R.id.textTag);
            textTag.setText(editTag.getText().toString());
            tagTexts.add(editTag.getText().toString());

            LinearLayout tagLayout = findViewById(R.id.tagItemLayout);
            tagLayout.addView(tagItem);
            editTag.setText("");
            tagNum++;
        }else {
            Toast.makeText(this, "태그는 6개까지 추가가능", Toast.LENGTH_SHORT).show();
        }

        for(final TagItem t : tagItems){
            Button btnDeleteTag = t.findViewById(R.id.btnDeleteTag);
            btnDeleteTag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LinearLayout tagLayout = findViewById(R.id.tagItemLayout);
                    TextView textView = findViewById(R.id.textTag);
                    tagTexts.remove(textView.getText().toString());
                    tagLayout.removeView(t);
                    tagItems.remove(t);
                    tagNum--;
                }
            });
        }

    }

    @Override
    public void onClick(View v) {
        id_view = v.getId();

        if (id_view == R.id.btnInsert && photoNum < MAX_PHOTO) {
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

            new AlertDialog.Builder(this)
                    .setTitle("업로드할 이미지 선택")
                    .setPositiveButton("사진촬영", cameraListener)
                    .setNeutralButton("앨범선택", albumListener)
                    .setNegativeButton("취소", cancelListener)
                    .show();
        }else {
            Toast.makeText(this, "사진 최대 3장", Toast.LENGTH_SHORT).show();
        }

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
            PhotoItemLayout photoItemLayout = new PhotoItemLayout(DiaryItemEdit.this);
            photoItemLayout.setId(photoNum+1000);
            photoItemLayouts.add(photoItemLayout);
            LinearLayout tagLayout = findViewById(R.id.imageItemLayout);
            tagLayout.addView(photoItemLayout);

            ImageView photo = photoItemLayout.findViewById(R.id.photo);
            photo.setImageURI(resultUri);

            photoItems.put(photoItemLayout.getId(), resultUri);
            photoNum++;

        for(final PhotoItemLayout p : photoItemLayouts){
            Button btnDeletePhoto = p.findViewById(R.id.btnDeletePhoto);
            btnDeletePhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LinearLayout photoLayout = findViewById(R.id.imageItemLayout);
                    photoItems.remove(p.getId());
                    photoLayout.removeView(p);
                    photoItemLayouts.remove(p);
                    photoNum --;
                }
            });
        }
    }


    class TagItem extends LinearLayout {

        public TagItem(Context context) {
            super(context);
            LayoutInflater inflater = getLayoutInflater();
            inflater.inflate(R.layout.tag_item, this, true);
        }
    }

    class PhotoItemLayout extends LinearLayout {

        public PhotoItemLayout (Context context) {
            super(context);
            LayoutInflater inflater = getLayoutInflater();
            inflater.inflate(R.layout.photo_item, this, true);
        }
    }

}
