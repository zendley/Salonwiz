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
import com.speckpro.salonwiz.adapters.InActiveUtilityDealsAdapter;
import com.speckpro.salonwiz.newmodels.UserUtilitiesModel;
import com.speckpro.salonwiz.retrofit.ApiClient;
import com.speckpro.salonwiz.retrofit.ApiService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InActiveDealsFragment extends Fragment {
    private RecyclerView courseRV;
    public static final int REQUEST_CODE = 101;

    private RecyclerView recyclerViewInActiveDeals;
    private InActiveUtilityDealsAdapter utilityDealsAdapter;
    private SharedPreferences sharedPreferences;
    private ArrayList<UserUtilitiesModel> utilitiesArrayList;
    private ProgressBar progressBar;
    private ApiService apiService;
    private TextView txtNoDeals;
    String token;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inactive, container, false);

        apiService = ApiClient.getClient().create(ApiService.class);
        progressBar = rootView.findViewById(R.id.progressBar_inactivedeals);
        txtNoDeals = rootView.findViewById(R.id.textView_noInActiveDeals);
        recyclerViewInActiveDeals = rootView.findViewById(R.id.recyclerView_inActive);

        sharedPreferences = getContext().getSharedPreferences("MySalonSharedPref", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", null);

        recyclerViewInActiveDeals.setHasFixedSize(true);
        recyclerViewInActiveDeals.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        getInActiveUserDeals();

        return rootView;
    }

    private void getInActiveUserDeals(){
        utilitiesArrayList = new ArrayList();
        recyclerViewInActiveDeals.setVisibility(View.GONE);
        txtNoDeals.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        Call<ArrayList<UserUtilitiesModel>> call = apiService.getUserInActiveUtilities("application/json", "Bearer "+token);
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
                        recyclerViewInActiveDeals.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        txtNoDeals.setVisibility(View.VISIBLE);
                        txtNoDeals.setText("No Active Deals Found!");
//                        Toast.makeText(KnowledgeArticlesActivity.this, "No Articles Found!", Toast.LENGTH_SHORT).show();
                    } else {
                        recyclerViewInActiveDeals.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        txtNoDeals.setVisibility(View.GONE);
                        //call adapter here
                        utilityDealsAdapter = new InActiveUtilityDealsAdapter(getActivity(), utilitiesArrayList);
                        recyclerViewInActiveDeals.setAdapter(utilityDealsAdapter);
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    recyclerViewInActiveDeals.setVisibility(View.GONE);
                    txtNoDeals.setVisibility(View.VISIBLE);
                    txtNoDeals.setText("Something went Wrong, Couldn't load Active Deals!");
                    //Toast.makeText(KnowledgeArticlesActivity.this, "" + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<UserUtilitiesModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                recyclerViewInActiveDeals.setVisibility(View.GONE);
                txtNoDeals.setVisibility(View.VISIBLE);
                txtNoDeals.setText("Check your internet connection, Load Active Deals Failed!");
//                Log.d("TAG",""+t.toString());
//                Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

//    public void getutilities(String User,View rootView){
//        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
//        progressDialog.setCancelable(false);
//        progressDialog.setMessage("Please Wait");
//        progressDialog.show();
//        String url2=baseurl+"api/Userutilities/getUtilitiesOfOneUser";
//        try {
//            JSONObject object1=new JSONObject();
//            object1.put("User",User);
//            RequestQueue mqueue4 = Volley.newRequestQueue(getApplicationContext());
//            JsonObjectRequest jsonObjRequest4 = new JsonObjectRequest(Request.Method.POST,
//                    url2, object1, new com.android.volley.Response.Listener<JSONObject>() {
//                @Override
//                public void onResponse(JSONObject response) {
//                    Log.d("myapp", "the response is" + response.toString());
//                    try {
//                        progressDialog.dismiss();
//                        JSONObject json = new JSONObject(String.valueOf(response));
//                        JSONArray result = json.getJSONArray("userUtilities");
//                        // JSONObject utilities=result.getJSONObject("Utilities");
//                        data= new ArrayList<>();
//                        String message=json.getString("message");
//                        if(message.equals("Utility Found for this User")){
//                            for (int i=0;i<result.length();i++){
//                                JSONObject userdata=result.getJSONObject(i);
//                                if(userdata.getString("isActive").equals("Expired")) {
//                                    JSONObject Utilities = userdata.getJSONObject("Utilities");
//                                    String title = Utilities.getString("Title");
//                                    String image = Utilities.getString("image");
//                                    String correcturl = image.replaceAll("\\\\", "");
//
//                                    UtilityCardsModel st = new UtilityCardsModel(correcturl, title);
//
//                                    data.add(st);
//                                    courseRV = rootView.findViewById(R.id.idRVCourse);
//                                    courseRV.setHasFixedSize(true);
//                                    courseRV.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
//                                    mAdapter = new InActiveDealsAdapter(getActivity(), data);
//                                    courseRV.setAdapter(mAdapter);
//                                }
//                            }
//                        }
//                        else{
//                            Toast.makeText(getApplicationContext(), "Failed to Load Data!", Toast.LENGTH_SHORT).show();
//                            progressDialog.dismiss();
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                        Toast.makeText(getApplicationContext(), "Failed to Load Data!!!", Toast.LENGTH_SHORT).show();
//                        progressDialog.dismiss();
//                    }
//
//                }
//            }, new com.android.volley.Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    progressDialog.dismiss();
//                    Toast.makeText(getApplicationContext(), "Check Your Internet Connection!", Toast.LENGTH_SHORT).show();
//
//                }
//            }
//            ) { @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<>();
//                headers.put("Content-Type", "application/json");
//                return headers;
//            }
//            };
//            mqueue4.add(jsonObjRequest4);
//
//        } catch (JSONException jsonException) {
//            jsonException.printStackTrace();
//            Toast.makeText(getApplicationContext(), "Incorrect email or password", Toast.LENGTH_SHORT).show();
//            progressDialog.dismiss();
//        }
//    }
}