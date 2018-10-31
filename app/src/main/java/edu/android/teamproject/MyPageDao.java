package edu.android.teamproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class MyPageDao {

    private FirebaseDatabase database;
    private DatabaseReference messageReference; // 테이블 주소
    private static MyPageDao myPageInstance;

    private String uid;
    private String email;
    private String providerId;
    private String name;
    private Uri photoUrl;


    public static MyPageDao getMyPageInstance(){
        if(myPageInstance == null){
            myPageInstance = new MyPageDao();
        }

        return myPageInstance;
    }

    private MyPageDao() {

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

    }
    public void insert(MyPageProfile myPageProfile){
        FirebaseTask firebaseTask = new FirebaseTask();
        firebaseTask.execute(myPageProfile);
    }

    private MyPageProfile myPageProfile;
    private Context context;
    public MyPageProfile update(Context context){
        this.context = context;
        FirebaseTask firebaseTask = new FirebaseTask();
        firebaseTask.execute();
        return myPageProfile;
    }

    class FirebaseTask extends AsyncTask<MyPageProfile, MyPageProfile, MyPageProfile> implements ChildEventListener{

        @Override
        protected MyPageProfile doInBackground(MyPageProfile... myPageProfiles) {
            database = FirebaseDatabase.getInstance();
            messageReference = database.getReference().child("MyPage");
            messageReference.addChildEventListener(this);

            if(myPageProfiles.length != 0) {
                messageReference.child(uid).setValue(myPageProfiles[0]);
            }else {

            }
            return null;
        }
        @Override
        protected void onPostExecute(MyPageProfile myPageProfile) {

        }

        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            Log.i("aaa", dataSnapshot.getKey());
            if(uid.equals(dataSnapshot.getKey())){
                myPageProfile = dataSnapshot.getValue(MyPageProfile.class);

                Uri uri = PhotoFirebaseStorageUtil.photoDownload(myPageProfile.getPhotoUri(), context);

                ConnectivityManager manager =
                        (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
                NetworkInfo info = manager.getActiveNetworkInfo();

                if(info != null && info.isConnected()){
                    ImageDownload imageDownload = new ImageDownload();
                    //imageDownload.execute(String.valueOf(uri));
                    Toast.makeText(context, "" + String.valueOf(uri), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "사용가능한 네트워크가 없습니다.", Toast.LENGTH_SHORT).show();
                }

            }
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            Log.i("aaa", dataSnapshot.getKey());
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
    }

    class ImageDownload extends AsyncTask<String, String, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bitmap = null;

            HttpURLConnection connection = null;
            InputStream in = null;
            BufferedInputStream bis = null;
            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                connection.setRequestMethod("GET");

                connection.connect();

                int responseCode = connection.getResponseCode();
                if(responseCode == HttpURLConnection.HTTP_OK){
                    in = connection.getInputStream();
                    bis = new BufferedInputStream(in);
                    bitmap = BitmapFactory.decodeStream(bis);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    bis.close();
                    connection.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
        }
    }











}
