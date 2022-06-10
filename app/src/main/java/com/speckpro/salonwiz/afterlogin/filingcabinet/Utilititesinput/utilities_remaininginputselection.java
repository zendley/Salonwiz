package com.speckpro.salonwiz.afterlogin.filingcabinet.Utilititesinput;

import static com.speckpro.salonwiz.BaseUrl.baseurl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.speckpro.salonwiz.R;
import com.speckpro.salonwiz.afterlogin.UI.homefrag_utilitydeals.utilitydeals;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class utilities_remaininginputselection extends AppCompatActivity implements checkboxesListener  {

    private CheckBox gascheck,wastecheck,electriccheck,insurencecheck,telephonecheck,broadbandcheck,watercheck,cardtermcheck;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<inputselection_model> studentList;
    private Button btnSelection;
    private ArrayList<inputselection_model> data;
    private final ArrayList<String> remainingtitles= new ArrayList<String>();
    private final ArrayList<String> totaltitles= new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utilities_remaininginputselection);
        SharedPreferences sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        String id = sh.getString("id", "");
        btnSelection = findViewById(R.id.util_submitbtn);

        //  studentList.clear();

        studentList = new ArrayList<inputselection_model>();
        getuserutilities(id);

        //getutilities(id);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void commitutilities(String title){
        SharedPreferences sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        String id = sh.getString("id", "");
        Log.v("Data For Popup Post",title+" "+id);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseurl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestBody User =
                RequestBody.create(MediaType.parse("multipart/form-data"),id);

        RequestBody UtilitiesTitle =
                RequestBody.create(MediaType.parse("multipart/form-data"),title);
        Log.d("dataof", User.toString()+ UtilitiesTitle);
        userutilitiesretrofitapi retrofitAPI = retrofit.create(userutilitiesretrofitapi.class);
        Call<ResponseBody> call = retrofitAPI.useraddinput(User,UtilitiesTitle);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response!=null){
                    Log.d("TAG", "onResponse: "+response.body());
                    Log.d("Response Message", "onResponse: "+response.message());
                } else {
                    Toast.makeText(utilities_remaininginputselection.this, "Data not added,Contact to Speckpro", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(utilities_remaininginputselection.this, "Failed"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getuserutilities(String User){
        final ProgressDialog progressDialog = new ProgressDialog(utilities_remaininginputselection.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait");
        progressDialog.show();
        String url2=baseurl+"api/Userutilities/getUtilitiesOfOneUser";
        try {
            JSONObject object1=new JSONObject();
            object1.put("User",User);
            RequestQueue mqueue4 = Volley.newRequestQueue(getApplicationContext());
            JsonObjectRequest jsonObjRequest4 = new JsonObjectRequest(Request.Method.POST,
                    url2, object1, new com.android.volley.Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("GetUserUtilities", "the response is" + response.toString());
                    try {
                        progressDialog.dismiss();
                        JSONObject json = new JSONObject(String.valueOf(response));
                        JSONArray result = json.getJSONArray("userUtilities");
                        // JSONObject utilities=result.getJSONObject("Utilities");
                        //  data= new ArrayList<>();
                        String message=json.getString("message");
                        if(message.equals("Utility Found for this User")){
                            for (int i=0;i<result.length();i++){
                                JSONObject userdata=result.getJSONObject(i);
                                JSONObject Utilities=userdata.getJSONObject("Utilities");
                                String title=Utilities.getString("Title");
                                //Log.d("Titles", title);
                                remainingtitles.add(title);
                            }
                            for (int c=0;c<remainingtitles.size();c++){
                                //Log.d("remaining titles", remainingtitles.get(0));
                                //Log.i("remaining titles", remainingtitles.get(c));
                            }
                            getutilities(User);
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Failed to Load Data!", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Failed to Load Data!!!", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Check Your Internet Connection!", Toast.LENGTH_SHORT).show();
                }
            }
            ) { @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
            };
            mqueue4.add(jsonObjRequest4);
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
            Toast.makeText(getApplicationContext(), "Incorrect email or password", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
    }

    public void getutilities(String User){
        final ProgressDialog progressDialog = new ProgressDialog(utilities_remaininginputselection.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait");
        progressDialog.show();
        String url3=baseurl+"api/Utilities/getall";
        JSONObject object6=new JSONObject();
        //object1.put("User",User);
        RequestQueue mqueue6 = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjRequest6 = new JsonObjectRequest(Request.Method.GET,
                url3,null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("myapp", "the response is" + response.toString());
                try {
                    progressDialog.dismiss();
                    JSONObject json = new JSONObject(String.valueOf(response));
                    JSONArray result = json.getJSONArray("Data");

                    String message=json.getString("message");
                    if(message.equals("Utilities Details Found")){
                        for (int i=0;i<result.length();i++){
                            JSONObject userdata=result.getJSONObject(i);
                            String vtitle=userdata.getString("Title");
                            String title=userdata.getString("Title");
                            String image=userdata.getString("image");
                            String correcturl=image.replaceAll("\\\\","");
                            totaltitles.add(title);
                        }

                        for(int v=0;v<totaltitles.size();v++){
                            for (int b=0;b<remainingtitles.size();b++){
                                if(totaltitles.get(v).equals(remainingtitles.get(b))){
                                    totaltitles.remove(v);
                                }
                            }
                        }

                        for (int j=0;j<totaltitles.size();j++){
                            //  Log.d("remaining titles", remainingtitles.get(0));
                            Log.i("Total titles", totaltitles.get(j));
                        }

                        for (int i=0;i<result.length();i++){
                            JSONObject userdata=result.getJSONObject(i);
                            String vtitle=userdata.getString("Title");

                            for (int s=0;s<totaltitles.size();s++) {
                                if (totaltitles.get(s).equals(vtitle)) {
                                    String title = userdata.getString("Title");
                                    String image = userdata.getString("image");
                                    String correcturl = image.replaceAll("\\\\", "");
                                    inputselection_model st = new inputselection_model(correcturl, title, false);
                                    studentList.add(st);
                                    mRecyclerView = findViewById(R.id.idRVCourse);
                                    mRecyclerView.setHasFixedSize(true);
                                    mRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));

                                    mAdapter = new inputselectionadapter(studentList);
                                    mRecyclerView.setAdapter(mAdapter);
                                    btnSelection.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            SharedPreferences sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
                                            String id = sh.getString("id", "");
                                            String data = "";
                                            List<String> list = new ArrayList<>();
                                            List<inputselection_model> stList = ((inputselectionadapter) mAdapter)
                                                    .getStudentist();

                                            for (int i = 0; i < stList.size(); i++) {
                                                inputselection_model singleStudent = stList.get(i);
                                                if (singleStudent.isSelected() == true) {
                                                    data = data + "," + singleStudent.getText();
                                                    commitutilities(singleStudent.getText());
                                                    list.add(singleStudent.getText());
                                                }
                                            }
                                            //Log.d("DatainString", data + id);
                                            Intent intent = new Intent(utilities_remaininginputselection.this, utilitydeals.class);
                                            intent.putStringArrayListExtra("utilnameslist", (ArrayList<String>) list);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                                }
                            }
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Failed to Load Data!", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Failed to Load Data!!!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Check Your Internet Connection!", Toast.LENGTH_SHORT).show();
            }
        }
        ) { @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            HashMap<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            return headers;
        }
        };
        mqueue6.add(jsonObjRequest6);
    }
    @Override
    public void oncheckboxchange(ArrayList<String> arrayList) {

    }

}