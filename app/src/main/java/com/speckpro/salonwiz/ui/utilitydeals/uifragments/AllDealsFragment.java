package com.speckpro.salonwiz.ui.utilitydeals.uifragments;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.speckpro.salonwiz.R;
import com.speckpro.salonwiz.adapters.UtilityCardsAdapter;
import com.speckpro.salonwiz.adapters.UtilityDealsAdapter;
import com.speckpro.salonwiz.models.UtilityCardsModel;
import com.speckpro.salonwiz.newmodels.UserUtilitiesModel;
import com.speckpro.salonwiz.retrofit.ApiClient;
import com.speckpro.salonwiz.retrofit.ApiService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllDealsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final int REQUEST_CODE = 101;
    private UtilityCardsAdapter mAdapter;
    private ArrayList<UtilityCardsModel> data;
    private String id;

    View rootView;
    private RecyclerView recyclerViewAllDeals;
    private UtilityDealsAdapter utilityDealsAdapter;
    private SharedPreferences sharedPreferences;
    private ArrayList<UserUtilitiesModel> utilitiesArrayList;
    private ProgressBar progressBar;
    private ApiService apiService;
    private TextView txtNoDeals;
    String token;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_all, container, false);

        apiService = ApiClient.getClient().create(ApiService.class);
        progressBar = rootView.findViewById(R.id.progressBar_alldeals);

        sharedPreferences = getContext().getSharedPreferences("MySalonSharedPref", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", null);

        txtNoDeals = rootView.findViewById(R.id.textView_noAllDeals);
        recyclerViewAllDeals = rootView.findViewById(R.id.recyclerView_allDeals);
        recyclerViewAllDeals.setHasFixedSize(true);
        recyclerViewAllDeals.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        utilitiesArrayList = new ArrayList();
        getAllUserDeals();

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (Build.VERSION.SDK_INT >= 26) {
            ft.setReorderingAllowed(false);
        }
        ft.detach(this).attach(this).commit();
        return rootView;
    }

    @Override
    public void onResume(){
        super.onResume();
        if(utilitiesArrayList.size()>0){
            utilitiesArrayList.clear();
            getAllUserDeals();
        }
    }

    private void getAllUserDeals(){
        utilitiesArrayList.clear();
        recyclerViewAllDeals.setVisibility(View.GONE);
        txtNoDeals.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        Call<ArrayList<UserUtilitiesModel>> call = apiService.getUserAllUtilities("application/json", "Bearer "+token);
        call.enqueue(new Callback<ArrayList<UserUtilitiesModel>>() {
            @Override
            public void onResponse(Call<ArrayList<UserUtilitiesModel>> call, Response<ArrayList<UserUtilitiesModel>> response) {
                if (response.isSuccessful()) {
                    utilitiesArrayList.clear();
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
                        recyclerViewAllDeals.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        txtNoDeals.setVisibility(View.VISIBLE);
                        txtNoDeals.setText("No Deals Found!");
//                        Toast.makeText(KnowledgeArticlesActivity.this, "No Articles Found!", Toast.LENGTH_SHORT).show();
                    } else {
                        recyclerViewAllDeals.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        txtNoDeals.setVisibility(View.GONE);
                        //call adapter here
                        utilityDealsAdapter = new UtilityDealsAdapter(getActivity(), utilitiesArrayList);
                        recyclerViewAllDeals.setAdapter(utilityDealsAdapter);
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    recyclerViewAllDeals.setVisibility(View.GONE);
                    txtNoDeals.setVisibility(View.VISIBLE);
                    txtNoDeals.setText("Something went Wrong, Couldn't load Deals!");
                    //Toast.makeText(KnowledgeArticlesActivity.this, "" + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<UserUtilitiesModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                recyclerViewAllDeals.setVisibility(View.GONE);
                txtNoDeals.setVisibility(View.VISIBLE);
                txtNoDeals.setText("Check your internet connection, Load Deals Failed!");
//                Log.d("TAG",""+t.toString());
//                Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}