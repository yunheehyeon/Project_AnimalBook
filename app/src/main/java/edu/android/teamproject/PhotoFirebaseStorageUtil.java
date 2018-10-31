package edu.android.teamproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static com.facebook.FacebookSdk.getApplicationContext;

public class PhotoFirebaseStorageUtil {

    private PhotoFirebaseStorageUtil() {}

    public static String PhotoUpload(Context context, Uri uri) {

        //업로드 진행 Dialog 보이기
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("업로드중...");
        progressDialog.show();

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
                        progressDialog.dismiss(); //업로드 진행 Dialog 상자 닫기
                        Toast.makeText(getApplicationContext(), "업로드 완료!", Toast.LENGTH_SHORT).show();
                    }
                })
                //실패시
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "업로드 실패!", Toast.LENGTH_SHORT).show();
                    }
                })
                //진행중
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        @SuppressWarnings("VisibleForTests") //이걸 넣어 줘야 아랫줄에 에러가 사라진다. 넌 누구냐?
                                double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        //dialog에 진행률을 퍼센트로 출력해 준다
                        progressDialog.setMessage("Uploaded " + ((int) progress) + "% ...");
                    }
                });


        return filename;
    }

    static Uri downloadPhoto = null;
    static Bitmap download = null;

    public static Bitmap photoDownload(String fileRef, final Context context){

        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference storageRef = storage.getReferenceFromUrl("gs://timproject-14aaa.appspot.com").child("images/" + fileRef);
        //Url을 다운받기
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.i("aaa", uri.toString());
                downloadPhoto = uri;
                downloadImage(context);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "다운로드 실패", Toast.LENGTH_SHORT).show();
            }
        });

        return download;
    }


    public static void downloadImage(Context context) {

        ConnectivityManager manager =
                (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if(info != null && info.isConnected()){
            ImageDownloadTask task = new ImageDownloadTask();
            task.execute(String.valueOf(downloadPhoto));
        }else{
            Toast.makeText(context, "사용가능한 네트워크가 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    static class ImageDownloadTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = null;

            HttpURLConnection connection = null;
            InputStream in = null;
            BufferedInputStream bis = null;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                connection.setRequestMethod("GET");

                connection.connect();

                int responseCode = connection.getResponseCode();
                if(responseCode == HttpURLConnection.HTTP_OK){
                    in = connection.getInputStream();
                    bis = new BufferedInputStream(in);
                    bitmap = BitmapFactory.decodeStream(bis);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    bis.close();
                    connection.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            download = bitmap;
        }
    }


}
