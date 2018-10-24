package android.edu.myapplication;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spinner;
    private ImageView ivPhoto;
    private TextView tvName;
    private Button btnBack;

    ArrayAdapter<String> myAdapter;
    Integer[] photos = {0, R.drawable.lupy, R.drawable.joro, R.drawable.sangdi};
    String[] names = {"이곳을 클릭하여 선택하세요.", "루피", "조로", "상디"};

    private Activity myActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myActivity = MainActivity.this;
        myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, names);

        spinner = findViewById(R.id.spinner);
        spinner.setAdapter(myAdapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position > 0)
            showBigPhoto(position);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void showBigPhoto(int position) {
        setContentView(R.layout.ani_source);
        tvName = findViewById(R.id.tvName);
        ivPhoto = findViewById(R.id.ivPhoto);

        tvName.setText("좋아하는 인물은: " + names[position]);
        ivPhoto.setImageResource(photos[position]);

        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myActivity.recreate();
            }
        });
    }

}
