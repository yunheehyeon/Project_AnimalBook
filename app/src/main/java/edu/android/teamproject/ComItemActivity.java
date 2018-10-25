package edu.android.teamproject;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ComItemActivity extends AppCompatActivity {

    public static final String ITEM_ID = "comItemId";

    public static Intent newIntent(Context context, String comId){
        Intent intent = new Intent(context, ComItemActivity.class);
        intent.putExtra(ITEM_ID, comId);

        return  intent;
    }

    private int commentCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_com_item);

        commentCount = 5;

        for(int i = 0; i < commentCount; i++){
            CommentItem commentItem = new CommentItem(this);
            LinearLayout comLayout = (LinearLayout) findViewById(R.id.comItemLayout);
            comLayout.addView(commentItem);
        }



    }

    class CommentItem extends CardView {

        private TextView comUserId;

        public CommentItem(Context context) {
            super(context);
            LayoutInflater inflater = getLayoutInflater();
            inflater.inflate(R.layout.comment_item, this, true);
        }
    }


}
