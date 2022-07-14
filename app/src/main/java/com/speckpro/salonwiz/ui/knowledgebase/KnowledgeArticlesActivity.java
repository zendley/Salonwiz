package com.speckpro.salonwiz.ui.knowledgebase;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.speckpro.salonwiz.R;
import com.speckpro.salonwiz.adapters.KnowledgeAdapter;
import com.speckpro.salonwiz.newmodels.KnowledgeModel;
import com.speckpro.salonwiz.retrofit.ApiClient;
import com.speckpro.salonwiz.retrofit.ApiService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KnowledgeArticlesActivity extends AppCompatActivity {

    private RecyclerView recyclerViewKnowledge;
    public static final int REQUEST_CODE = 101;
    private KnowledgeAdapter mAdapter;
    private ArrayList<KnowledgeModel> knowledgeArrayList;

    private ProgressBar progressBar;
    private ApiService apiService;
    private TextView textViewNoData;
    SharedPreferences sharedpreferences;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(KnowledgeArticlesActivity.this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            KnowledgeArticlesActivity.this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(KnowledgeArticlesActivity.this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            KnowledgeArticlesActivity.this.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_knowledge_articles);

        apiService = ApiClient.getClient().create(ApiService.class);
        progressBar = findViewById(R.id.progressBar_knowledgebase);
        textViewNoData = findViewById(R.id.textView_noDataKnowledge);

        sharedpreferences =  getSharedPreferences("MySalonSharedPref", MODE_PRIVATE);
        token = sharedpreferences.getString("token", "");

        TextView textView =findViewById(R.id.toolbar_titletext);
        textView.setText("Articles");
        ImageView backarrow=findViewById(R.id.toolbar_backarrow);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerViewKnowledge =findViewById(R.id.recyclerView_Knowledge);
        recyclerViewKnowledge.setHasFixedSize(true);
        recyclerViewKnowledge.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));

        getArticles();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void getArticles(){
        progressBar.setVisibility(View.VISIBLE);
        textViewNoData.setVisibility(View.GONE);
        recyclerViewKnowledge.setVisibility(View.GONE);
        knowledgeArrayList = new ArrayList();
        Call<ArrayList<KnowledgeModel>> call = apiService.getKnowledgebase("application/json", "Bearer "+token, "article");
        call.enqueue(new Callback<ArrayList<KnowledgeModel>>() {

            @Override
            public void onResponse(Call<ArrayList<KnowledgeModel>> call, Response<ArrayList<KnowledgeModel>> response) {
                if(response.isSuccessful()){
                    assert response.body() != null;
                    if(response.body()!=null) {
                        for (int i = 0; i < response.body().size(); i++) {
                            knowledgeArrayList.add(new KnowledgeModel(
                                    response.body().get(i).getId(),
                                    response.body().get(i).getName(),
                                    response.body().get(i).getCate(),
                                    response.body().get(i).getImage(),
                                    response.body().get(i).getCreatedAt(),
                                    response.body().get(i).getUpdatedAt()));
                            // Toast.makeText(getContext(), "" + response.body().get(i).getImage().getSrc(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    if (knowledgeArrayList!=null && knowledgeArrayList.isEmpty()) {
                        progressBar.setVisibility(View.GONE);
                        textViewNoData.setVisibility(View.VISIBLE);
                        recyclerViewKnowledge.setVisibility(View.GONE);
                        textViewNoData.setText("No Articles Found!");

//                        Toast.makeText(KnowledgeArticlesActivity.this, "No Articles Found!", Toast.LENGTH_SHORT).show();
                    } else {

                        progressBar.setVisibility(View.GONE);
                        textViewNoData.setVisibility(View.GONE);
                        recyclerViewKnowledge.setVisibility(View.VISIBLE);
                        recyclerViewKnowledge.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
                        mAdapter = new KnowledgeAdapter(KnowledgeArticlesActivity.this, knowledgeArrayList);
                        recyclerViewKnowledge.setAdapter(mAdapter);
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    textViewNoData.setVisibility(View.VISIBLE);
                    recyclerViewKnowledge.setVisibility(View.GONE);
                    textViewNoData.setText("Something went Wrong, Couldn't load Articles!");

                    //Toast.makeText(KnowledgeArticlesActivity.this, "" + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<KnowledgeModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                recyclerViewKnowledge.setVisibility(View.GONE);
                textViewNoData.setVisibility(View.VISIBLE);
//                Log.d("TAG",""+t.toString());
                textViewNoData.setText("Check your internet connection, Load Articles Failed!");
                Toast.makeText(KnowledgeArticlesActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

//    public void getArticles(){
//        final ProgressDialog progressDialog = new ProgressDialog(KnowledgeArticlesActivity.this);
//        progressDialog.setCancelable(false); // set cancelable to false
//        progressDialog.setMessage("Please Wait"); // set message
//        progressDialog.show(); // show progress dialog
//        String url2=baseurl+"api/KnowledgedBase/getall";
//        RequestQueue mqueue4 = Volley.newRequestQueue(getApplicationContext());
//        JsonObjectRequest jsonObjRequest4 = new JsonObjectRequest(Request.Method.GET,
//                url2, null, new com.android.volley.Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                Log.d("myapp", "the response is" + response.toString());
//                try {
//                    progressDialog.dismiss();
//                    JSONObject json = new JSONObject(String.valueOf(response));
//                    JSONArray result = json.getJSONArray("Data");
//                    // JSONObject utilities=result.getJSONObject("Utilities");
//                    data= new ArrayList<>();
//                    String message=json.getString("message");
//                    if(message.equals("Files Found")){
//                        for (int i=0;i<result.length();i++){
//                            // ArrayList<String> s=new ArrayList<>();
//                            JSONObject userdata=result.getJSONObject(i);
//                            if(userdata.getString("Type").equals("Articles")){
//                                // JSONObject Utilities=userdata.getJSONObject("Utilities");
//                                String title=userdata.getString("Title");
//                                String desciption=userdata.getString("Description");
//                                String image=userdata.getString("URL");
//                                String correcturl=image.replaceAll("\\\\","");
//                                KnowledgeModel st =new KnowledgeModel(title,desciption,correcturl);
//                                data.add(st);
//
//                                courseRV =findViewById(R.id.art_idRVCourse);
//                                courseRV.setHasFixedSize(true);
//                                courseRV.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
//                                mAdapter = new KnowledgeAdapter(KnowledgeArticlesActivity.this,data);
//                                courseRV.setAdapter(mAdapter);
//
//                            }}
//                    }
//                    else{
//                        Toast.makeText(getApplicationContext(), "Incorrect email or password", Toast.LENGTH_SHORT).show();
//                        progressDialog.dismiss();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Toast.makeText(getApplicationContext(), "Failed to Load Data!!!", Toast.LENGTH_SHORT).show();
//                    progressDialog.dismiss();
//                }
//
//            }
//        }, new com.android.volley.Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                progressDialog.dismiss();
//                Toast.makeText(getApplicationContext(), "Check Your Internet Connection!", Toast.LENGTH_SHORT).show();
//
//            }
//        }
//        ) { @Override
//        public Map<String, String> getHeaders() throws AuthFailureError {
//            HashMap<String, String> headers = new HashMap<>();
//            headers.put("Content-Type", "application/json");
//            return headers;
//        }
//        };
//        mqueue4.add(jsonObjRequest4);
//
//    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
}