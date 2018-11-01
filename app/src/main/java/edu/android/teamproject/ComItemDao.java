package edu.android.teamproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.List;

import static edu.android.teamproject.ComItemListFragment.*;

public class ComItemDao {

    interface ComDateCallback {
        void comItemCallback(ComItemEdit comItemEdit);
        void comItemCallback(Bitmap bitmap);
    }

    private ComDateCallback callback;

    private List<ComItem> comItems;

    private FirebaseDatabase database;
    private DatabaseReference reference;

    private static ComItemDao comItemInstance;

    private String providerId,uid,name,email;
    private Uri photoUrl;

    public static ComItemDao getComItemInstance() {
        if (comItemInstance == null) {
            comItemInstance = new ComItemDao();
        }
        return comItemInstance;
    }

    private ComItemDao() {

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

    public void insert(ComItemEdit comItem) {
        FirebaseTask firebaseTask = new FirebaseTask();
        firebaseTask.execute(comItem);
    }

    private ComItemEdit comItemEdit;
    private Context context;

    class FirebaseTask extends AsyncTask<ComItemEdit, ComItemEdit, ComItemEdit> implements ChildEventListener {

        @Override
        protected ComItemEdit doInBackground(ComItemEdit... comItemEdits) {
            database = FirebaseDatabase.getInstance();
            reference = database.getReference().child("ComItem");
            reference.addChildEventListener(this);

            if (comItemEdits.length != 0) {
                reference.child(uid).setValue(comItemEdits[0]);
            } else {

            }

            return null;
        }

        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            Log.i("aaa", dataSnapshot.getKey());

            if (uid.equals(dataSnapshot.getKey())) {
                comItemEdit = dataSnapshot.getValue(ComItemEdit.class);
                callback.comItemCallback(comItemEdit);
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
    }
}
