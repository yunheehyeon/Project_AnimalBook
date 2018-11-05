package edu.android.teamproject;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Comment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentDown implements ChildEventListener {

    interface DataCallback{
        void CommentCallback();
    }

    private  DataCallback callback;

    private String commentTableId;

    private FirebaseDatabase database;
    private DatabaseReference reference;

    private FirebaseAuth mAuth;
    private String providerId,uid,name,email;
    private Uri photoUrl;
    private List<CommentItem> commentItemList = new ArrayList<>();

    private int commentCount = 0;

    public CommentDown(String comItemId, DataCallback callback) {

        this.callback = callback;

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {

                providerId = profile.getProviderId();
                uid = profile.getUid();

                name = profile.getDisplayName();
                email =  profile.getEmail();
                photoUrl = profile.getPhotoUrl();
            }

        }

        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Comment/").child(comItemId);
        reference.addChildEventListener(this);

    }

    public String userEmail(){
        return email.split("@")[0];
    }

    public void insert(CommentItem commentItem){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();
        String date = formatter.format(now);

        commentItem.setCommentDate(date);
        commentItem.setCommentUserId(email.split("@")[0]);

        reference.push().setValue(commentItem);
    }

    public List<CommentItem> update(){
        return commentItemList;
    }



    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        CommentItem commentItem = dataSnapshot.getValue(CommentItem.class);
        commentItem.setCommentItemId(dataSnapshot.getKey());
        commentItemList.add(commentItem);
        commentCount++;
        callback.CommentCallback();
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
