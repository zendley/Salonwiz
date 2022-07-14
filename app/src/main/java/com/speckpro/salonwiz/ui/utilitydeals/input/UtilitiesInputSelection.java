package com.speckpro.salonwiz.ui.utilitydeals.input;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.speckpro.salonwiz.R;
import com.speckpro.salonwiz.adapters.UtilitiesInputAdapter;
import com.speckpro.salonwiz.newmodels.ApiResponse;
import com.speckpro.salonwiz.newmodels.UtilitiesModel;
import com.speckpro.salonwiz.retrofit.ApiClient;
import com.speckpro.salonwiz.retrofit.ApiService;
import com.speckpro.salonwiz.ui.utilitydeals.UtilityDealsMain;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UtilitiesInputSelection extends AppCompatActivity{

    private Button btnSelection;
    private RecyclerView mRecyclerView;
    private UtilitiesInputAdapter inputAdapter;
    private ArrayList<UtilitiesModel> utilitiesArrayList;
    private ProgressBar progressBar;
    private ApiService apiService;
    private TextView textViewNoData;
    SharedPreferences sharedpreferences;
    String id, token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utilities_inputselection);

        apiService = ApiClient.getClient().create(ApiService.class);
        progressBar = findViewById(R.id.progressBar_utilitesInput);
        textViewNoData = findViewById(R.id.textView_noInputUtility);
        mRecyclerView = findViewById(R.id.recyclerView_inputUtilities);

        sharedpreferences =  getSharedPreferences("MySalonSharedPref", MODE_PRIVATE);
        token = sharedpreferences.getString("token", "");
        id = sharedpreferences.getString("id", "");

        utilitiesArrayList = new ArrayList();
        getAllUtilities();

        btnSelection = findViewById(R.id.button_submitUtility);
        btnSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = "";
                List<String> list = new ArrayList<>();
                List<UtilitiesModel> stList = (inputAdapter).getUtilitiesList();
                for (int i = 0; i < stList.size(); i++) {
                    UtilitiesModel utility = stList.get(i);
                    if (utility.getIsChecked().equals("1")) {
                        if(title.isEmpty()){
                            title = utility.getTitle();
                        } else {
                            title = title+","+utility.getTitle();
                        }
                        list.add(title);
                        //Toast.makeText(UtilitiesInputSelection.this, ""+title, Toast.LENGTH_SHORT).show();
                    }
                }

                addUserUtility(title);

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void addUserUtility(String title){
        progressBar.setVisibility(View.VISIBLE);
        Call<ApiResponse> call = apiService.addUserUtility("application/json", "Bearer "+token, title);
        call.enqueue(new Callback<ApiResponse>() {

            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if(response.isSuccessful()){
                    assert response.body() != null;
                    progressBar.setVisibility(View.GONE);
//                    Toast.makeText(UtilitiesInputSelection.this, "" + response.body().toString(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(new Intent(UtilitiesInputSelection.this, UtilityDealsMain.class)));
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(UtilitiesInputSelection.this, "" + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
//                Log.d("TAG",""+t.toString());
                Toast.makeText(UtilitiesInputSelection.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getAllUtilities(){
        progressBar.setVisibility(View.VISIBLE);
        textViewNoData.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.GONE);

        Call<ArrayList<UtilitiesModel>> call = apiService.getAllUtilities("application/json", "Bearer "+token);
        call.enqueue(new Callback<ArrayList<UtilitiesModel>>() {
            @Override
            public void onResponse(Call<ArrayList<UtilitiesModel>> call, Response<ArrayList<UtilitiesModel>> response) {
                if(response.isSuccessful()){
                    assert response.body() != null;
                    for (int i = 0; i < response.body().size(); i++) {
                        utilitiesArrayList.add(new UtilitiesModel(
                                response.body().get(i).getId(),
                                response.body().get(i).getTitle(),
                                response.body().get(i).getImage(),
                                response.body().get(i).getCreatedAt(),
                                response.body().get(i).getUpdatedAt(),
                                response.body().get(i).getSupplier()));
//                        Toast.makeText(UtilitiesInputSelection.this, ""+response.body().get(i).getTitle(), Toast.LENGTH_SHORT).show();
                    }

                    if (utilitiesArrayList!=null && utilitiesArrayList.isEmpty()) {
                        progressBar.setVisibility(View.GONE);
                        textViewNoData.setVisibility(View.VISIBLE);
                        mRecyclerView.setVisibility(View.GONE);
                        textViewNoData.setText("No Utilities Found!");
                    } else {
                        progressBar.setVisibility(View.GONE);
                        textViewNoData.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);

                        mRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
                        inputAdapter = new UtilitiesInputAdapter(UtilitiesInputSelection.this, utilitiesArrayList);
                        mRecyclerView.setAdapter(inputAdapter);
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    textViewNoData.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                    textViewNoData.setText("Something went Wrong, Couldn't load Utilities!");

                    //Toast.makeText(KnowledgeArticlesActivity.this, "" + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<UtilitiesModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.GONE);
                textViewNoData.setVisibility(View.VISIBLE);
                textViewNoData.setText("Check your internet connection, Load Utilities Failed!");
                //Toast.makeText(UtilitiesInputSelection.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}