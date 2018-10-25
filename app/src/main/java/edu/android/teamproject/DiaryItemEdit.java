package edu.android.teamproject;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DiaryItemEdit extends AppCompatActivity {

    public static final String DIARY_ID = "diaryId";

    public static Intent newIntent(Context context, String diaryid) {
        Intent intent = new Intent(context, DiaryItemEdit.class);
        intent.putExtra(DIARY_ID, diaryid);

        return  intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_item_edit);
    }
}
