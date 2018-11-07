package edu.android.teamproject;

import android.app.ProgressDialog;
import android.content.Context;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static com.facebook.FacebookSdk.getApplicationContext;


public class DiaryItemDao implements ChildEventListener {

    interface DiaryItemCallback{
        void itemCallback();
    }
    interface EndCallback{
        void endUpload();
    }

    private FirebaseDatabase database;
    private DatabaseReference reference;

    private FirebaseAuth mAuth;
    private String providerId,uid,name,email;
    private Uri photoUrl;
    private UserInfo profile;


    private DiaryItemCallback callback;
    private EndCallback endCallback;

    private ArrayList<DiaryItem> diaryList = new ArrayList<>();
    private List<DiaryItem> myDiaryList = new ArrayList<>();
    private Map<String, DiaryItem> myDiaryMap = new TreeMap<>();

    private static DiaryItemDao diaryItemInstance;

    public static DiaryItemDao getDiaryItemInstance(Object object){
        if(diaryItemInstance == null){
            diaryItemInstance = new DiaryItemDao();
        }
        if(object instanceof DiaryItemCallback){
            diaryItemInstance.callback = (DiaryItemCallback) object;
        }
        if(object instanceof EndCallback){
            diaryItemInstance.endCallback = (EndCallback) object;
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
        reference = database.getReference().child("DiaryItem").child(uid);
        reference.addChildEventListener(this);

    }
    public ArrayList upDate(){
        diaryList = new ArrayList<DiaryItem>(myDiaryMap.values());
        return diaryList;
    }

    public void insert(DiaryItem diaryItem) {
        diaryList = new ArrayList<>();
        reference = database.getReference().child("DiaryItem").child(uid);
        reference.addChildEventListener(this);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();
        String date = formatter.format(now);
        diaryItem.setDiaryDate(date);

        reference.push().setValue(diaryItem);
    }

    public void delete(DiaryItem diaryItem){

        reference.child(diaryItem.getDiaryId()).removeValue();
    }

    public void updateBookmark(DiaryItem diaryItem){
        Log.i("aaa", diaryItem.getDiaryId());
        reference = database.getReference().child("DiaryItem").child(uid).child(diaryItem.getDiaryId()).child("bookMark");
        reference.addChildEventListener(this);

        reference.setValue(diaryItem.isBookMark());
    }

    public List<DiaryItem> updateMyDiaryList(){
        return myDiaryList;
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        Log.i("aaa", "aaa" + dataSnapshot.getKey());
        DiaryItem diaryItem = dataSnapshot.getValue(DiaryItem.class);
        diaryItem.setDiaryId(dataSnapshot.getKey());
        myDiaryMap.put(dataSnapshot.getKey(), diaryItem);

        if(diaryItem.isBookMark()){
            myDiaryList.add(diaryItem);
        }

        callback.itemCallback();
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        DiaryItem diaryItem = dataSnapshot.getValue(DiaryItem.class);
        diaryItem.setDiaryId(dataSnapshot.getKey());
        diaryList.add(diaryItem);

        if(diaryItem.isBookMark()){
            myDiaryList.add(diaryItem);
        }else {
            for (int i = 0; i < myDiaryList.size(); i++) {
                if (myDiaryList.get(i).getDiaryId().equals(dataSnapshot.getKey())) {
                    myDiaryList.remove(i);
                }
            }
        }

        callback.itemCallback();
    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
        for(int i = 0; i < diaryList.size(); i++){
            if(dataSnapshot.getKey().equals(diaryList.get(i).getDiaryId())){
                diaryList.remove(i);
            }
        }

        for(int i = 0; i < myDiaryList.size(); i++){
            if(myDiaryList.get(i).getDiaryId().equals(dataSnapshot.getKey())){
                myDiaryList.remove(i);
            }
        }
        callback.itemCallback();
    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
    public static final String diary = "Diary/";
    private int minTerm = 0;
    public List<String> photoUpload(Context context, final List<Uri> uris) {

        final ProgressDialog progressDialog = new ProgressDialog(
                context);

        progressDialog.setMessage("저장중입니다.");
        progressDialog.show();

        List<String> filenames = new ArrayList<>();
        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference storageRef = storage.getReferenceFromUrl("gs://timproject-14aaa.appspot.com");

        for(Uri uri : uris) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_mmss");
            Date now = new Date();
            String filename = formatter.format(now)+ minTerm + ".png";
            minTerm++;
            filenames.add(filename);
            storageRef.child(diary + filename).putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            if(uris.size() == minTerm) {
                                progressDialog.dismiss();
                                endCallback.endUpload();
                            }
                        }
                    })
                    //실패시
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "업로드 실패!", Toast.LENGTH_SHORT).show();
                        }
                    });

        }

        return filenames;
    }


}
