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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ComItemDao implements ChildEventListener{

    interface ComItemCallback{
        void dateCallback();
    }

    private ComItemCallback callback;

    private FirebaseDatabase database;
    private DatabaseReference reference;

    private FirebaseAuth mAuth;
    private String providerId,uid,name,email;
    private Uri photoUrl;
    private UserInfo profile;

    private List<ComItem> comItems = new ArrayList<>();

    private static ComItemDao comItemDaoInstance;

    public static ComItemDao getComItemInstance(Object object){
        if(comItemDaoInstance == null){
            comItemDaoInstance = new ComItemDao();
        }
        if(object instanceof ComItemCallback){
            comItemDaoInstance.callback = (ComItemCallback) object;
        }
        return comItemDaoInstance;
    }

    private ComItemDao(){
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
        reference = database.getReference().child("ComItem");
        reference.addChildEventListener(this);
    }

    public void insert(ComItem comItem) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();
        String date = formatter.format(now);
        comItem.setDate(date);
        comItem.setUserEmail(email.split("@")[0]);
        comItem.setUserId(uid);
        comItem.setViewCount(0);

        reference.push().setValue(comItem);
    }

    public List<ComItem> update(){
        return comItems;
    }

    @Override
    public void onChildAdded( DataSnapshot dataSnapshot,  String s) {
        ComItem comItem = dataSnapshot.getValue(ComItem.class);
        comItem.setItemId(dataSnapshot.getKey());
        comItems.add(comItem);

        callback.dateCallback();
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
