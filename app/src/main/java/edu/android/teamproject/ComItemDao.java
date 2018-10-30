package edu.android.teamproject;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.List;

import static edu.android.teamproject.ComItemListFragment.*;

public class ComItemDao implements ChildEventListener{

    private List<ComItem> comItems;

    private FirebaseDatabase database;
    private DatabaseReference reference;

    private static ComItemDao comItemInstance;

    public static ComItemDao getComItemInstance() {
        if (comItemInstance == null) {
            comItemInstance = new ComItemDao();
        }
        return comItemInstance;
    }

    private ComItemDao() {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("ComItem");
        reference.addChildEventListener(this);
        
    }

    public void insert(ComItem comItem) {
        reference.push().setValue(comItem);
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
