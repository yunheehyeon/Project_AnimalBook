package edu.android.teamproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import static com.facebook.FacebookSdk.getApplicationContext;

public class PhotoFirebaseStorageUtil {

    private PhotoFirebaseStorageUtil() {}

    public static String PhotoUpload(Context context, Uri uri) {
        //storage
        FirebaseStorage storage = FirebaseStorage.getInstance();

        //Unique한 파일명을 만들자.
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMHH_mmss");
        Date now = new Date();
        String filename = formatter.format(now) + ".png";

        //storage 주소와 폴더 파일명을 지정해 준다.
        StorageReference storageRef = storage.getReferenceFromUrl("gs://timproject-14aaa.appspot.com").child("images/" + filename);
        //올라가거라...
        storageRef.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getApplicationContext(), "업로드 완료!", Toast.LENGTH_SHORT).show();
                    }
                })
                //실패시
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "업로드 실패!", Toast.LENGTH_SHORT).show();
                    }
                });

        return filename;
    }

    public static List<String> PhotoUpload(Context context, List<Uri> uris) {
        List<String> filenames = new ArrayList<>();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        int minTerm = 0;
        StorageReference storageRef = storage.getReferenceFromUrl("gs://timproject-14aaa.appspot.com");
        for(Uri uri : uris) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_mmss");
            Date now = new Date();
            String filename = formatter.format(now)+ minTerm + ".png";
            minTerm++;
            filenames.add(filename);
            storageRef.child("diary/" + filename).putFile(uri)
                   .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                       @Override
                       public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                           Toast.makeText(getApplicationContext(), "업로드 완료!", Toast.LENGTH_SHORT).show();
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
