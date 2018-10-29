package edu.android.teamproject;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyPageDao implements ChildEventListener {

    private FirebaseDatabase database;
    private DatabaseReference messageReference; // 테이블 주소
    private ChildEventListener childEventListener;


    private static MyPageDao myPageinstance;

    public static MyPageDao getMyPageinstance(){
        if(myPageinstance == null){
            myPageinstance = new MyPageDao();
        }

        return myPageinstance;
    }

    private MyPageDao() {
        database = FirebaseDatabase.getInstance();
        messageReference = database.getReference().child("MyPage");
        messageReference.addChildEventListener(childEventListener);
    }

    public void insert(MyPageProfile myPageProfile){
        MyPageProfile profile = new MyPageProfile("예시", 15);
        messageReference.push().setValue(profile);
    }



    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

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