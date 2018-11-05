package edu.android.teamproject;

import android.content.Context;
import android.content.Intent;
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

public class ComItemDetailActivity extends AppCompatActivity {

    private static final String ITEM_ID = "comItemId";
    private static final int ITEM_ERROE = -1;

    public static Intent newIntent(Context context, int comId){
        Intent intent = new Intent(context, ComItemDetailActivity.class);
        intent.putExtra(ITEM_ID, comId);

        return  intent;
    }

    private ComItemDao dao;

    private Button btncommentAdd;
    private int commentCount;
    private EditText commentEdText;
    private TextView textTitle, textUserId, textDate, textCommentCount, textTag, textViewCount;
    private TextView textView;
    private ImageView imageView1, imageView2, imageView3;
    private ComItem comItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_com_item);

        textTitle = findViewById(R.id.comItemTitle);
        textUserId = findViewById(R.id.comItemUserId);
        textDate = findViewById(R.id.comItemDate);
        textView = findViewById(R.id.comItemTitle);
        textTag = findViewById(R.id.comItemTag);
        textCommentCount = findViewById(R.id.itemCommentCount);
        btncommentAdd = findViewById(R.id.btnCommentWrite);
        imageView1 = findViewById(R.id.comImage1);
        imageView2 = findViewById(R.id.comImage2);
        imageView3 = findViewById(R.id.comImage3);

        int position = getIntent().getIntExtra(ITEM_ID, ITEM_ERROE);
        if(position != ITEM_ERROE){
            dao = ComItemDao.getComItemInstance(this);
            comItem = dao.update().get(position);
        }

        textTitle.setText(comItem.getTitle());
        textUserId.setText(comItem.getUserId());
        textDate.setText(comItem.getDate());
        textView.setText(comItem.getText());
        textViewCount.setText("조회수 : " + String.valueOf(comItem.getViewCount()));
        textCommentCount.setText("댓글 : " + String.valueOf(comItem.getCommentCount()));
        StringBuilder builder = new StringBuilder();
        builder.append("Tag : ");
        for(String s : comItem.getTag()) {
            if(s != null) {
                builder.append(s);
                textTag.setText(builder);
            }
            builder.append(", ");
        }

        

        commentCount = 3;

        for(int i = 0; i < commentCount; i++){
            CommentItem commentItem = new CommentItem(this);
            LinearLayout comLayout = findViewById(R.id.comItemLayout);
            comLayout.addView(commentItem);
        }

        for(int i = 0; i < commentCount; i++){
            CommentMasterItem commentMasterItem = new CommentMasterItem(this);
            LinearLayout comLayout = findViewById(R.id.comItemLayout);
            comLayout.addView(commentMasterItem);
        }

        final CommentAddMenu commentAddMenu = new CommentAddMenu(this);
        LinearLayout comLayout = findViewById(R.id.comItemLayout);
        comLayout.addView(commentAddMenu);
        commentEdText = commentAddMenu.findViewById(R.id.commentEdText);


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

    class CommentItem extends CardView {

        private TextView comUserId;

        public CommentItem(Context context) {
            super(context);
            LayoutInflater inflater = getLayoutInflater();
            inflater.inflate(R.layout.comment_item, this, true);
        }
    }

    class CommentMasterItem extends CardView {

        private TextView comUserId;

        public CommentMasterItem(Context context) {
            super(context);
            LayoutInflater inflater = getLayoutInflater();
            inflater.inflate(R.layout.comment_master_item, this, true);
        }
    }

    class CommentAddMenu extends LinearLayout {

        private TextView comUserId;

        public CommentAddMenu(Context context) {
            super(context);
            LayoutInflater inflater = getLayoutInflater();
            inflater.inflate(R.layout.comment_menu, this, true);
        }
    }



}
