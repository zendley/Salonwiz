package com.example.salonwiz.afterlogin.filingcabinet.Utilititesinput;

import static com.example.salonwiz.BaseUrl.baseurl;
import static com.facebook.login.widget.ProfilePictureView.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.salonwiz.R;
import com.example.salonwiz.afterlogin.UI.homefrag_utilitydeals.utilitydeals;
import com.example.salonwiz.afterlogin.maindashboard;
import com.example.salonwiz.loginauth.login;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class utilities_inputselection extends AppCompatActivity implements checkboxesListener {

    private CheckBox gascheck,wastecheck,electriccheck,insurencecheck,telephonecheck,broadbandcheck,watercheck,cardtermcheck;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<inputselection_model> studentList;
    private Button btnSelection;
    private ArrayList<inputselection_model> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utilities_inputselection);
        SharedPreferences sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        String id = sh.getString("id", "");
        btnSelection = findViewById(R.id.util_submitbtn);
        studentList = new ArrayList<inputselection_model>();
        getutilities(id);

//        TextView textView = (TextView)findViewById(R.id.toolbar_titletext);
//        textView.setText("Add Utility Deals");
//        textView.setVisibility(View.GONE);
//        ImageView backarrow=(ImageView)findViewById(R.id.toolbar_backarrow);
//        backarrow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });

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
                }else{
                    Toast.makeText(utilities_inputselection.this, "Data not added,Contact to Speckpro", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(utilities_inputselection.this, "Failed"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getutilities(String User){
        final ProgressDialog progressDialog = new ProgressDialog(utilities_inputselection.this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait"); // set message
        progressDialog.show(); // show progress dialog
        String url2=baseurl+"api/Utilities/getall";
        JSONObject object1=new JSONObject();
        //object1.put("User",User);
        RequestQueue mqueue4 = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjRequest4 = new JsonObjectRequest(Request.Method.GET,
                url2,null, new com.android.volley.Response.Listener<JSONObject>() {
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
                            String title=userdata.getString("Title");
                            String image=userdata.getString("image");
                            String correcturl=image.replaceAll("\\\\","");
                            inputselection_model st =new inputselection_model(correcturl,title,false);
                            studentList.add(st);
                            mRecyclerView = findViewById(R.id.idRVCourse);
                            mRecyclerView.setHasFixedSize(true);
                            mRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));

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
                                    Log.d("DatainString", data+id);


                                    Intent intent=new Intent(utilities_inputselection.this, utilitydeals.class);
                                    intent.putStringArrayListExtra("utilnameslist", (ArrayList<String>) list);
                                    startActivity(intent);
                                }
                            });
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
        mqueue4.add(jsonObjRequest4);
    }
    @Override
    public void oncheckboxchange(ArrayList<String> arrayList) {

    }
}