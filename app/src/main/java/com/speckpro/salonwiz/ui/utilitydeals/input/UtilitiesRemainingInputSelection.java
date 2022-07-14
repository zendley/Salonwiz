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
import com.speckpro.salonwiz.models.UtilityInputSelectionModel;
import com.speckpro.salonwiz.newmodels.ApiResponse;
import com.speckpro.salonwiz.newmodels.UserUtilitiesModel;
import com.speckpro.salonwiz.newmodels.UtilitiesModel;
import com.speckpro.salonwiz.retrofit.ApiClient;
import com.speckpro.salonwiz.retrofit.ApiService;
import com.speckpro.salonwiz.ui.utilitydeals.UtilityDealsMain;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UtilitiesRemainingInputSelection extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private UtilitiesInputAdapter inputAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<UtilityInputSelectionModel> studentList;
    private Button btnSelection;
    private ArrayList<UtilityInputSelectionModel> data;
    private final ArrayList<String> remainingtitles= new ArrayList<String>();
    private final ArrayList<String> totaltitles= new ArrayList<String>();
    private final ArrayList<String> usertitles= new ArrayList<String>();

    private ArrayList<UtilitiesModel> utilitiesArrayList, reaminingUtilitiesArray;
    private ArrayList<UserUtilitiesModel> userUtilitiesArrayList;
    private ProgressBar progressBar;
    private ApiService apiService;
    private TextView textViewNoData;
    SharedPreferences sharedpreferences;
    String id, token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utilities_remaininginputselection);

        apiService = ApiClient.getClient().create(ApiService.class);
        progressBar = findViewById(R.id.progressBar_remainingutilitesInput);
        textViewNoData = findViewById(R.id.textView_noRemainingUtility);
        mRecyclerView = findViewById(R.id.recyclerView_remainingUtility);
        btnSelection = findViewById(R.id.util_submitbtn);

        sharedpreferences =  getSharedPreferences("MySalonSharedPref", MODE_PRIVATE);
        token = sharedpreferences.getString("token", "");
        id = sharedpreferences.getString("id", "");

        mRecyclerView = findViewById(R.id.recyclerView_remainingUtility);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));

        utilitiesArrayList = new ArrayList<>();
        userUtilitiesArrayList = new ArrayList<>();
        reaminingUtilitiesArray = new ArrayList<>();

        getUserUtilities();

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
                    progressBar.setVisibility(View.GONE); // + response.body().toString()
                    Toast.makeText(UtilitiesRemainingInputSelection.this, "Utility Added Successfully!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(new Intent(UtilitiesRemainingInputSelection.this, UtilityDealsMain.class)));
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(UtilitiesRemainingInputSelection.this, "" + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
//                Log.d("TAG",""+t.toString());
                Toast.makeText(UtilitiesRemainingInputSelection.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getAllUtilities(){
        utilitiesArrayList.clear();
        progressBar.setVisibility(View.VISIBLE);
        textViewNoData.setVisibility(View.GONE);
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
                    }

                    for(int v=0; v<utilitiesArrayList.size(); v++){
                        for (int b=0; b<usertitles.size(); b++){
                            if(utilitiesArrayList.get(v).getTitle().equals(usertitles.get(b))){
                                utilitiesArrayList.remove(v);
                            }
                        }
                    }

                    if (utilitiesArrayList!=null && utilitiesArrayList.isEmpty()) {
                        progressBar.setVisibility(View.GONE);
                        textViewNoData.setVisibility(View.VISIBLE);
                        textViewNoData.setText("No Remaining Utilities Found!");
                    } else {
                        progressBar.setVisibility(View.GONE);
                        textViewNoData.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);

                        inputAdapter = new UtilitiesInputAdapter(UtilitiesRemainingInputSelection.this, utilitiesArrayList);
                        mRecyclerView.setAdapter(inputAdapter);
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    textViewNoData.setVisibility(View.VISIBLE);
                    textViewNoData.setText("Something went Wrong, Couldn't load Remaining Utilities!");
                    //Toast.makeText(KnowledgeArticlesActivity.this, "" + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<UtilitiesModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                textViewNoData.setVisibility(View.VISIBLE);
                textViewNoData.setText("Check your internet connection, Load Remaining Utilities Failed!");
                //Toast.makeText(UtilitiesInputSelection.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getUserUtilities(){
        userUtilitiesArrayList.clear();
        progressBar.setVisibility(View.VISIBLE);
        textViewNoData.setVisibility(View.GONE);
        Call<ArrayList<UserUtilitiesModel>> call = apiService.getUserAllUtilities("application/json", "Bearer "+token);
        call.enqueue(new Callback<ArrayList<UserUtilitiesModel>>() {
            @Override
            public void onResponse(Call<ArrayList<UserUtilitiesModel>> call, Response<ArrayList<UserUtilitiesModel>> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    for (int i = 0; i < response.body().size(); i++) {
                        userUtilitiesArrayList.add(new UserUtilitiesModel(
                                response.body().get(i).getId(),
                                response.body().get(i).getUtiltity(),
                                response.body().get(i).getSupplier(),
                                response.body().get(i).getExpirationdate(),
                                response.body().get(i).getBillpaid(),
                                response.body().get(i).getLoaForm(),
                                response.body().get(i).getLastBill(),
                                response.body().get(i).getUserId(),
                                response.body().get(i).getIsAccepted(),
                                response.body().get(i).getDealRequested(),
                                response.body().get(i).getIsFilled(),
                                response.body().get(i).getCreatedAt(),
                                response.body().get(i).getUpdatedAt()));

                        usertitles.add(response.body().get(i).getUtiltity());
                    }

                    if (userUtilitiesArrayList != null && userUtilitiesArrayList.isEmpty()) {
                        progressBar.setVisibility(View.VISIBLE);
                        textViewNoData.setVisibility(View.GONE);
                        textViewNoData.setText("No Deals Found!");
                    } else {
                        progressBar.setVisibility(View.VISIBLE);
                        textViewNoData.setVisibility(View.GONE);

                        getAllUtilities();
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    textViewNoData.setVisibility(View.VISIBLE);
                    textViewNoData.setText("Something went Wrong, Couldn't load Deals!");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<UserUtilitiesModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                textViewNoData.setVisibility(View.VISIBLE);
                textViewNoData.setText("Check your internet connection, Load Deals Failed!");
            }
        });
    }
}