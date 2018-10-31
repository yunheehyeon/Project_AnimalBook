package edu.android.teamproject;

import android.net.Uri;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyPageDao implements ChildEventListener {

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

        database = FirebaseDatabase.getInstance();
        messageReference = database.getReference().child("MyPage");
        messageReference.addChildEventListener(this);
    }

    public void insert(MyPageProfile myPageProfile){
        messageReference.child(uid).setValue(myPageProfile);
    }

    private MyPageProfile myPageProfile;

    public MyPageProfile Update(){

        return myPageProfile;
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        if(uid.equals(dataSnapshot.getKey())){
            myPageProfile = dataSnapshot.getValue(MyPageProfile.class);
        }

    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            myPageProfile = dataSnapshot.getValue(MyPageProfile.class);
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
