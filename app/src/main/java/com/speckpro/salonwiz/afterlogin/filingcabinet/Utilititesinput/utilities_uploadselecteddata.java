package com.speckpro.salonwiz.afterlogin.filingcabinet.Utilititesinput;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.speckpro.salonwiz.R;

import java.util.ArrayList;

public class utilities_uploadselecteddata extends AppCompatActivity {
    private RecyclerView courseRV;
    public static final int REQUEST_CODE = 101;
    private cardadapter mAdapter;
    private ArrayList<card_model> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utilities_uploadselecteddata);

        boolean gasStatus = getIntent().getExtras().getBoolean("gasStatus");
        boolean wasteStatus = getIntent().getExtras().getBoolean("wasteStatus");
        boolean electricStatus = getIntent().getExtras().getBoolean("electricStatus");
        boolean insurenceStatus = getIntent().getExtras().getBoolean("insurenceStatus");
        boolean telephoneStatus = getIntent().getExtras().getBoolean("telephoneStatus");
        boolean broadbandStatus = getIntent().getExtras().getBoolean("broadbandStatus");
        boolean waterStatus = getIntent().getExtras().getBoolean("waterStatus");
        boolean cardtermStatus = getIntent().getExtras().getBoolean("cardtermStatus");
      //  boolean gasStatus =((intent.extras.get("gasStatus")).toString()).toBoolean();

        courseRV = findViewById(R.id.idRVCourse);
        data = new ArrayList<>();
        cardadapter courseAdapter = new cardadapter(this, data);
        courseRV.setLayoutManager(new GridLayoutManager(this,2));
        courseRV.setAdapter(courseAdapter);

    }
    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {

        }
    }
}