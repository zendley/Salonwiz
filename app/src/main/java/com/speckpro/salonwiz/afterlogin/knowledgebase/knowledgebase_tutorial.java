package com.speckpro.salonwiz.afterlogin.knowledgebase;

import static com.speckpro.salonwiz.BaseUrl.baseurl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.speckpro.salonwiz.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class knowledgebase_tutorial extends AppCompatActivity {

    private RecyclerView courseRV;
    public static final int REQUEST_CODE = 101;
    private faq_adapter mAdapter;
    private ArrayList<mode_faq> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(knowledgebase_tutorial.this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            knowledgebase_tutorial.this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(knowledgebase_tutorial.this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            knowledgebase_tutorial.this.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_knowledgebase_tutorial);
        TextView textView =findViewById(R.id.toolbar_titletext);
        textView.setText("Tutorials");
        ImageView backarrow=findViewById(R.id.toolbar_backarrow);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getfaqs();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void getfaqs(){
        final ProgressDialog progressDialog = new ProgressDialog(knowledgebase_tutorial.this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait"); // set message
        progressDialog.show(); // show progress dialog
        String url2=baseurl+"api/KnowledgedBase/getall";
        RequestQueue mqueue4 = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjRequest4 = new JsonObjectRequest(Request.Method.GET,
                url2, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("myapp", "the response is" + response.toString());
                try {
                    progressDialog.dismiss();
                    JSONObject json = new JSONObject(String.valueOf(response));
                    JSONArray result = json.getJSONArray("Data");
                    // JSONObject utilities=result.getJSONObject("Utilities");
                    data= new ArrayList<>();
                    String message=json.getString("message");
                    if(message.equals("Files Found")){
                        for (int i=0;i<result.length();i++){
                            // ArrayList<String> s=new ArrayList<>();
                            JSONObject userdata=result.getJSONObject(i);
                            if(userdata.getString("Type").equals("BASE")){
                                // JSONObject Utilities=userdata.getJSONObject("Utilities");
                                String title=userdata.getString("Title");
                                String desciption=userdata.getString("Description");
                                String image=userdata.getString("URL");
                                String correcturl=image.replaceAll("\\\\","");
                                // studentList.add(correcturl,title,false);
                                mode_faq st =new mode_faq(title,desciption,correcturl);
                                data.add(st);

                                courseRV =findViewById(R.id.tut_idRVCourse);
                                courseRV.setHasFixedSize(true);
                                courseRV.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
                                mAdapter = new faq_adapter(knowledgebase_tutorial.this,data);
                                courseRV.setAdapter(mAdapter);
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
        mqueue4.add(jsonObjRequest4);
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