package edu.android.teamproject;

import android.net.Uri;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class DiaryItemDao implements ChildEventListener {

    private FirebaseDatabase database;
    private DatabaseReference reference;

    private FirebaseAuth mAuth;
    private String providerId,uid,name,email;
    private Uri photoUrl;
    private UserInfo profile;



    private static DiaryItemDao diaryItemInstance;

    public static DiaryItemDao getDiaryItemInstance(){
        if(diaryItemInstance == null){
            diaryItemInstance = new DiaryItemDao();
        }

        return diaryItemInstance;
    }

    private DiaryItemDao(){
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
        reference = database.getReference().child("DiaryItem");
        reference.addChildEventListener(this);




    }
    public void insert(DiaryItem diaryItem){
        diaryItem.setUid(providerId  + "/" + email);
        reference.push().setValue(diaryItem);


    }
    @Override
    public void onChildAdded( DataSnapshot dataSnapshot,  String s) {

    }

    @Override
    public void onChildChanged( DataSnapshot dataSnapshot,  String s) {

    }

    @Override
    public void onChildRemoved( DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved( DataSnapshot dataSnapshot,  String s) {

    }

    @Override
    public void onCancelled( DatabaseError databaseError) {

    }
}
