package com.speckpro.salonwiz.ui.services;

import static com.speckpro.salonwiz.retrofit.BaseUrl.baseurl;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.speckpro.salonwiz.R;
import com.speckpro.salonwiz.adapters.ServicesAdapter;
import com.speckpro.salonwiz.models.ServicesModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ServicesActivity extends AppCompatActivity {

    private String Status;
    private TextView basict,premt,ultrat;
    private ArrayList<ServicesModel> data;
    private RecyclerView recyclerViewServices;
    private ServicesAdapter servicesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_services);

        recyclerViewServices = findViewById(R.id.recyclerView_services);

        TextView textView =findViewById(R.id.toolbar_titletext);
        textView.setText("Management");
        ImageView backarrow=findViewById(R.id.toolbar_backarrow);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

       // getServices();

    }

    public void getServices(){
        final ProgressDialog progressDialog = new ProgressDialog(ServicesActivity.this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait"); // set message
        progressDialog.show(); // show progress dialog
        String url2=baseurl+"api/packages/getall";
        RequestQueue mqueue4 = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjRequest4 = new JsonObjectRequest(Request.Method.GET,
                url2, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("myappservices", "the response is" + response.toString());
                try {
                    progressDialog.dismiss();
                    JSONObject json = new JSONObject(String.valueOf(response));
                    JSONArray result = json.getJSONArray("Data");
                    // JSONObject utilities=result.getJSONObject("Utilities");
                    data= new ArrayList<>();
                    String message=json.getString("message");
                    if(message.equals("Package Details Found")){
                        for (int i=0;i<result.length();i++){
                            // ArrayList<String> s=new ArrayList<>();
                            JSONObject userdata=result.getJSONObject(i);
//                            if(userdata.getString("Type").equals("Articles")){
                            // JSONObject Utilities=userdata.getJSONObject("Utilities");
                                String title=userdata.getString("Title");
                                String amount=userdata.getString("Amount");
                                String duration=userdata.getString("Duration");

                                ServicesModel st =new ServicesModel(title, amount, duration);
                                data.add(st);

                            recyclerViewServices =findViewById(R.id.recyclerView_services);
                            recyclerViewServices.setHasFixedSize(true);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(ServicesActivity.this);
                            recyclerViewServices.setLayoutManager(layoutManager);
                            servicesAdapter = new ServicesAdapter(ServicesActivity.this, data);
                            recyclerViewServices.setAdapter(servicesAdapter);
                            }
                        }
//                   }
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
    protected void onResume() {
        super.onResume();
        if(data!=null && data.size()>0) {
            data.clear();
            getServices();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

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