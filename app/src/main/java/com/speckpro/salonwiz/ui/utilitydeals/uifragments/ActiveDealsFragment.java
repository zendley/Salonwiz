package com.speckpro.salonwiz.ui.utilitydeals.uifragments;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.speckpro.salonwiz.R;
import com.speckpro.salonwiz.adapters.ActiveUtilityDealsAdapter;
import com.speckpro.salonwiz.newmodels.UserUtilitiesModel;
import com.speckpro.salonwiz.retrofit.ApiClient;
import com.speckpro.salonwiz.retrofit.ApiService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActiveDealsFragment extends Fragment {
    private RecyclerView courseRV;
    public static final int REQUEST_CODE = 101;
    private RecyclerView recyclerViewActiveDeals;
    private ActiveUtilityDealsAdapter utilityDealsAdapter;
    private SharedPreferences sharedPreferences;
    private ArrayList<UserUtilitiesModel> utilitiesArrayList;
    private ProgressBar progressBar;
    private ApiService apiService;
    private TextView txtNoDeals;
    String token;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_active, container, false);
        SharedPreferences sh = getActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        String id = sh.getString("id", "");

        apiService = ApiClient.getClient().create(ApiService.class);
        progressBar = rootView.findViewById(R.id.progressBar_activedeals);
        txtNoDeals = rootView.findViewById(R.id.textView_noActiveDeals);
        recyclerViewActiveDeals = rootView.findViewById(R.id.recyclerView_activeDeals);

        sharedPreferences = getContext().getSharedPreferences("MySalonSharedPref", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", null);

        recyclerViewActiveDeals.setHasFixedSize(true);
        recyclerViewActiveDeals.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        getActiveUserDeals();

        return rootView;
    }

    private void getActiveUserDeals(){
        utilitiesArrayList = new ArrayList();
        recyclerViewActiveDeals.setVisibility(View.GONE);
        txtNoDeals.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        Call<ArrayList<UserUtilitiesModel>> call = apiService.getUserActiveUtilities("application/json", "Bearer "+token);
        call.enqueue(new Callback<ArrayList<UserUtilitiesModel>>() {
            @Override
            public void onResponse(Call<ArrayList<UserUtilitiesModel>> call, Response<ArrayList<UserUtilitiesModel>> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    for (int i = 0; i < response.body().size(); i++) {
                        utilitiesArrayList.add(new UserUtilitiesModel(
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
                                response.body().get(i).getUpdatedAt(),
                                response.body().get(i).getImage()));
                    }

                    if (utilitiesArrayList != null && utilitiesArrayList.isEmpty()) {
                        recyclerViewActiveDeals.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        txtNoDeals.setVisibility(View.VISIBLE);
                        txtNoDeals.setText("No Active Deals Found!");
//                        Toast.makeText(KnowledgeArticlesActivity.this, "No Articles Found!", Toast.LENGTH_SHORT).show();
                    } else {
                        recyclerViewActiveDeals.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        txtNoDeals.setVisibility(View.GONE);
                        //call adapter here
                        utilityDealsAdapter = new ActiveUtilityDealsAdapter(getActivity(), utilitiesArrayList);
                        recyclerViewActiveDeals.setAdapter(utilityDealsAdapter);
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    recyclerViewActiveDeals.setVisibility(View.GONE);
                    txtNoDeals.setVisibility(View.VISIBLE);
                    txtNoDeals.setText("Something went Wrong, Couldn't load Active Deals!");
                    //Toast.makeText(KnowledgeArticlesActivity.this, "" + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<UserUtilitiesModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                recyclerViewActiveDeals.setVisibility(View.GONE);
                txtNoDeals.setVisibility(View.VISIBLE);
                txtNoDeals.setText("Check your internet connection, Load Active Deals Failed!");
//                Log.d("TAG",""+t.toString());
//                Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}