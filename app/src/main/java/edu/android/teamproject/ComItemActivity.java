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
import android.widget.LinearLayout;
import android.widget.TextView;

public class ComItemActivity extends AppCompatActivity {

    public static final String ITEM_ID = "comItemId";

    private Button btncommentAdd;

    public static Intent newIntent(Context context, String comid){
        Intent intent = new Intent(context, ComItemActivity.class);
        intent.putExtra(ITEM_ID, comid);

        return  intent;
    }

    private int commentCount;
    private EditText commentEdText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_com_item);


        btncommentAdd = findViewById(R.id.btnCommentWrite);

        commentCount = 3;

        for(int i = 0; i < commentCount; i++){
            CommentItem commentItem = new CommentItem(this);
            LinearLayout comLayout = (LinearLayout) findViewById(R.id.comItemLayout);
            comLayout.addView(commentItem);
        }

        for(int i = 0; i < commentCount; i++){
            CommentMasterItem commentMasterItem = new CommentMasterItem(this);
            LinearLayout comLayout = (LinearLayout) findViewById(R.id.comItemLayout);
            comLayout.addView(commentMasterItem);
        }

        final CommentAddMenu commentAddMenu = new CommentAddMenu(this);
        LinearLayout comLayout = (LinearLayout) findViewById(R.id.comItemLayout);
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
