package com.speckpro.salonwiz.ui.utilitydeals.input;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.speckpro.salonwiz.R;
import com.speckpro.salonwiz.adapters.UtilityCardsAdapter;
import com.speckpro.salonwiz.models.UtilityCardsModel;

import java.util.ArrayList;

public class UtilitiesUploadSelectedData extends AppCompatActivity {
    private RecyclerView courseRV;
    public static final int REQUEST_CODE = 101;
    private UtilityCardsAdapter mAdapter;
    private ArrayList<UtilityCardsModel> data;
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
        UtilityCardsAdapter courseAdapter = new UtilityCardsAdapter(this, data);
        courseRV.setLayoutManager(new GridLayoutManager(this,2));
        courseRV.setAdapter(courseAdapter);

    }
    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {

        }
    }
}