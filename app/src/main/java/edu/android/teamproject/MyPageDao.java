package edu.android.teamproject;

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

import static com.facebook.FacebookSdk.getApplicationContext;

public class MyPageDao implements ChildEventListener {

    interface DataCallback{
        void proFileCallback();
        void proFileImageCallback();
    }

    private DataCallback callback;

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

        myPageInstance.callback = (DataCallback) object;
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

    static Bitmap bitmap = null;

    public String photoUpload(Context context, Uri uri){
        //storage
        FirebaseStorage storage = FirebaseStorage.getInstance();

        //Unique한 파일명을 만들자.
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMHH_mmss");
        Date now = new Date();
        String filename = formatter.format(now) + ".png";

        //storage 주소와 폴더 파일명을 지정해 준다.
        StorageReference storageRef = storage.getReferenceFromUrl("gs://timproject-14aaa.appspot.com").child("images/" + filename);
        //올라가거라...
        storageRef.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getApplicationContext(), "업로드 완료!", Toast.LENGTH_SHORT).show();
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
        StorageReference storageRef = storage.getReferenceFromUrl("gs://timproject-14aaa.appspot.com").child("images/" + fileRef);

        //Url을 다운받기
        final long ONE_MEGABYTE = 1024 * 1024;
        storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray( bytes, 0, bytes.length );
                myPageImage = bitmap;
                Log.i("aaa", "callback:" + callback);
                callback.proFileImageCallback();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });
    }











}
