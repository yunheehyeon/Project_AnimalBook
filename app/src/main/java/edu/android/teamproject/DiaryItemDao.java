package edu.android.teamproject;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class DiaryItemDao implements ChildEventListener {

    interface DiaryItemCallback{
        void itemCallback();
    }

    private FirebaseDatabase database;
    private DatabaseReference reference;
    private ArrayList<DiaryItem> diaryList = new ArrayList<>();
    private FirebaseAuth mAuth;
    private String providerId,uid,name,email;
    private Uri photoUrl;
    private UserInfo profile;


    private DiaryItemCallback callback;

    private static DiaryItemDao diaryItemInstance;

    public static DiaryItemDao getDiaryItemInstance(Object object){
        if(diaryItemInstance == null){
            diaryItemInstance = new DiaryItemDao(object);
        }

        return diaryItemInstance;
    }
    private DiaryItemDao(Object object){
        if(object instanceof DiaryItemCallback){
            callback = (DiaryItemCallback) object;
        }


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
        reference = database.getReference().child("DiaryItem").child(uid);
        reference.addChildEventListener(this);

    }
    public ArrayList upDate(){
        return diaryList;
    }

    public void insert(DiaryItem diaryItem) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy 년 MM 월 dd일");
        Date now = new Date();
        String date = formatter.format(now);
        diaryItem.setDiaryDate(date);

        reference.push().setValue(diaryItem);
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        Log.i("aaa", dataSnapshot.getKey());
        DiaryItem diaryItem = dataSnapshot.getValue(DiaryItem.class);
        diaryItem.setDiaryId(dataSnapshot.getKey());
        diaryList.add(diaryItem);
        callback.itemCallback();
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




}
