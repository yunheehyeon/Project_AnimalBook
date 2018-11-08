package edu.android.teamproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class ComItemDetailActivity extends AppCompatActivity implements CommentDown.DataCallback {

    private static final String ITEM_ID = "comItemId";

    public static Intent newIntent(Context context, String comId){
        Intent intent = new Intent(context, ComItemDetailActivity.class);
        intent.putExtra(ITEM_ID, comId);

        return  intent;
    }

    private ComItemDao dao;

    private Button btncommentAdd, btnComItemDelete;
    private int commentCount;
    private EditText commentEdText;
    private TextView textTitle, textUserId, textDate, textCommentCount, textTag, textViewCount;
    private TextView textView;
    private ComItem comItem;

    private CommentDown commentDown;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_com_item);

        textTitle = findViewById(R.id.comItemTitle);
        textUserId = findViewById(R.id.comItemUserId);
        textDate = findViewById(R.id.comItemDate);
        textView = findViewById(R.id.comItemText);
        textTag = findViewById(R.id.comItemTag);
        textCommentCount = findViewById(R.id.itemCommentCount);
        textViewCount = findViewById(R.id.comItemViewCount);
        btncommentAdd = findViewById(R.id.btnCommentWrite);
        btnComItemDelete = findViewById(R.id.btnComItemDelete);

        String comId = getIntent().getStringExtra(ITEM_ID);
        dao = ComItemDao.getComItemInstance(this);

        for(ComItem c : dao.update()){
            if(c.getItemId().equals(comId)){
                comItem = c;
            }
        }
        if(comItem == null){
            finish();
        }

        dao.viewCountUpdate(comItem);

        commentDown = new CommentDown(comItem.getItemId(), this);

        textTitle.setText(comItem.getTitle());
        textUserId.setText("작성자 : " + comItem.getUserEmail());
        textDate.setText("등록일 : " + comItem.getDate());
        textView.setText(comItem.getText());
        textViewCount.setText("조회수 : " + String.valueOf(comItem.getViewCount()));
        textCommentCount.setText("댓글 : " + String.valueOf(comItem.getCommentCount()));
        StringBuilder builder = new StringBuilder();
        builder.append("Tag : ");
        if(comItem.getTag() != null){
            for(String s : comItem.getTag()) {
                if(s != null) {
                    builder.append(s);
                    textTag.setText(builder);
                }
                builder.append(", ");
            }
        }
        if(comItem.getUserEmail().equals(commentDown.userEmail())){
            btnComItemDelete.setVisibility(View.VISIBLE);
            btnComItemDelete.setEnabled(true);
            btnComItemDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ComItemDetailActivity.this);
                    builder.setTitle("삭제 확인");
                    builder.setMessage("삭제할까요?");
                    builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dao.delete(comItem);
                            finish();
                        }
                    });
                    builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog dlg = builder.create();
                    dlg.show();
                }
            });
        }

        for(String fileName : comItem.getImages()){

            final ImageView imageView = new ImageView(this);
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.
                    getReferenceFromUrl("gs://timproject-14aaa.appspot.com").child(ComItemDao.community + fileName);

            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    if(imageView != null && ComItemDetailActivity.this != null) {
                        GlideApp.with(ComItemDetailActivity.this)
                                .load(uri)
                                .into(imageView);
                    }
                }
            });
            LinearLayout imageLayout = findViewById(R.id.comImageLayout);
            imageLayout.addView(imageView);
            imageView.setPadding(8, 8, 8, 8);
        }

        final CommentAddMenu commentAddMenu = new CommentAddMenu(this);
        LinearLayout comLayout = findViewById(R.id.commentMenuLayout);
        comLayout.addView(commentAddMenu);

        commentEdText = commentAddMenu.findViewById(R.id.commentEdText);
        Button btnCommentUpdate = commentAddMenu.findViewById(R.id.btnCommentUpdateM);
        btnCommentUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentUpdate();
                commentEdText.setText("");
            }
        });

        btncommentAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentEdText.post(new Runnable() {
                    @Override
                    public void run() {
                        commentEdText.setFocusableInTouchMode(true);
                        commentEdText.requestFocus();
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(commentEdText,0);
                    }
                });
            }
        });

    }

    private void commentUpdate() {
        CommentItem commentItem = new CommentItem();
        commentItem.setCommentText(commentEdText.getText().toString());
        commentDown.insert(commentItem);
    }

    @Override
    public void CommentCallback() {
        LinearLayout comLayout = findViewById(R.id.commentItemLayout);
        comLayout.removeAllViews();

        List<CommentItem> commentItemList = commentDown.update();
        commentCount = commentItemList.size();
        comItem.setCommentCount(commentCount);
        dao.commentCountUpdate(comItem);
        textCommentCount.setText("댓글 : " + String.valueOf(commentCount));

        for(int i = 0; i < commentCount; i++){
            if(commentItemList.get(i).getCommentUserId().equals(commentDown.userEmail())){
                CommentMasterItem commentMasterItem = new CommentMasterItem(this);
                TextView textUserEmail = commentMasterItem.findViewById(R.id.commentUserIdM);
                TextView textDate = commentMasterItem.findViewById(R.id.commentDateM);
                TextView textView = commentMasterItem.findViewById(R.id.commentTextM);
                Button btnCommentDelete = commentMasterItem.findViewById(R.id.btnCommentDeleteM);
                final CommentItem commentItem = commentItemList.get(i);
                textUserEmail.setText("작성자 : " + commentItem.getCommentUserId());
                textDate.setText("날짜 : " + commentItem.getCommentDate());
                textView.setText(commentItemList.get(i).getCommentText());
                comLayout.addView(commentMasterItem);

                btnCommentDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ComItemDetailActivity.this);
                        builder.setTitle("삭제 확인");
                        builder.setMessage("삭제할까요?");
                        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                commentDown.delete(commentItem);
                            }
                        });
                        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        AlertDialog dlg = builder.create();
                        dlg.show();

                    }
                });

            }else {
                CommentItemLayout commentItem = new CommentItemLayout(this);
                TextView textUserEmail = commentItem.findViewById(R.id.commentUserId);
                TextView textDate = commentItem.findViewById(R.id.commentDate);
                TextView textView = commentItem.findViewById(R.id.commentText);
                textUserEmail.setText(commentItemList.get(i).getCommentUserId());
                textDate.setText(commentItemList.get(i).getCommentDate());
                textView.setText(commentItemList.get(i).getCommentText());
                comLayout.addView(commentItem);
            }
        }

    }

    class CommentItemLayout extends CardView {

        public CommentItemLayout(Context context) {
            super(context);
            LayoutInflater inflater = getLayoutInflater();
            inflater.inflate(R.layout.comment_item, this, true);
        }
    }

    class CommentMasterItem extends CardView {

        public CommentMasterItem(Context context) {
            super(context);
            LayoutInflater inflater = getLayoutInflater();
            inflater.inflate(R.layout.comment_master_item, this, true);
        }
    }

    class CommentAddMenu extends LinearLayout {

        public CommentAddMenu(Context context) {
            super(context);
            LayoutInflater inflater = getLayoutInflater();
            inflater.inflate(R.layout.comment_menu, this, true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
