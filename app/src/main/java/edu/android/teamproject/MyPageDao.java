package edu.android.teamproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class MyPageDao implements ChildEventListener {

    interface DataCallback{
        void proFileCallback();
        void proFileImageCallback();
    }
    interface EndCallback{
        void endUpload();
    }


    private DataCallback callback;
    private EndCallback endCallback;

    private FirebaseDatabase database;
    private DatabaseReference messageReference; // 테이블 주소
    private static MyPageDao myPageInstance;

    private String uid;
    private String email;
    private String providerId;
    private String name;
    private Uri photoUrl;

    private MyPageProfile myPageProfile;
    private Bitmap myPageImage;

    public static MyPageDao getMyPageInstance(Object object){
        if(myPageInstance == null){
            myPageInstance = new MyPageDao(object);
        }

        if(object instanceof DataCallback) {
            myPageInstance.callback = (DataCallback) object;
        }

        if(object instanceof EndCallback){
            myPageInstance.endCallback = (EndCallback) object;
        }

        return myPageInstance;
    }

    private MyPageDao(Object object) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                // Id of the provider (ex: google.com)
                providerId = profile.getProviderId();

                // UID specific to the provider
                uid = profile.getUid();

                // Name, email address, and profile photo Url
                name = profile.getDisplayName();
                email =  profile.getEmail();
                photoUrl = profile.getPhotoUrl();
            }

        }

        database = FirebaseDatabase.getInstance();
        messageReference = database.getReference().child("MyPage");
        messageReference.addChildEventListener(this);

    }
    public void insert(MyPageProfile myPageProfile){
        messageReference.child(uid).setValue(myPageProfile);
    }

    public MyPageProfile update(){
        return myPageProfile;
    }

    public Bitmap updateImage(){
        return myPageImage;
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        if(uid.equals(dataSnapshot.getKey())){
            myPageProfile = dataSnapshot.getValue(MyPageProfile.class);
            callback.proFileCallback();
        }
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        if(uid.equals(dataSnapshot.getKey())){
            myPageProfile = dataSnapshot.getValue(MyPageProfile.class);
            callback.proFileCallback();
        }
    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
    private String myPage = "Profile/";

    public String photoUpload(Context context, Uri uri){
        final ProgressDialog progressDialog = new ProgressDialog(
                context);

        progressDialog.setMessage("저장중입니다.");
        progressDialog.show();
        progressDialog.setCancelable(false);

        FirebaseStorage storage = FirebaseStorage.getInstance();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMHH_mmss");
        Date now = new Date();
        String filename = formatter.format(now) + ".png";


        StorageReference storageRef = storage.getReferenceFromUrl("gs://timproject-14aaa.appspot.com").child(myPage + filename);
        storageRef.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "업로드 완료!", Toast.LENGTH_SHORT).show();
                        endCallback.endUpload();
                    }
                })
                //실패시
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "업로드 실패!", Toast.LENGTH_SHORT).show();
                    }
                });

        return filename;
    }

    public void photoDownload(final String fileRef){

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://timproject-14aaa.appspot.com").child(myPage + fileRef);

        //Url을 다운받기
        final long ONE_MEGABYTE = 1024 * 1024;
        storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray( bytes, 0, bytes.length );
                myPageImage = bitmap;
                callback.proFileImageCallback();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });
    }











}
