package com.speckpro.salonwiz.afterlogin.knowledgebase;

import static com.speckpro.salonwiz.BaseUrl.baseurl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
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

public class knowledgebase_faq extends AppCompatActivity {
    private RecyclerView courseRV;
    public static final int REQUEST_CODE = 101;
    private FaqAdapter faqAdapter;
    private faq_adapter mAdapter;
    private ArrayList<mode_faq> data;
    private ArrayList<FaqModel> faqModelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(knowledgebase_faq.this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            knowledgebase_faq.this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(knowledgebase_faq.this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            knowledgebase_faq.this.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_knowledgebase_faq);

        TextView textView =findViewById(R.id.toolbar_titletext);
        textView.setText("FAQs");
        ImageView backarrow=findViewById(R.id.toolbar_backarrow);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        faqModelArrayList = new ArrayList<>();
        faqModelArrayList.add(new FaqModel("How Secure is Salon Wizz?", "With its bank-level 256-bit encrypted EV SSL connection and encrypted triple backed-up storage, Salon Wizz is highly secure.\n" +
                "Safety and privacy of our customers’ data is everything to us. With a single lapse in security procedures we would lose the trust of our customers – our most important asset. Our clients include financial organizations, law offices and medical institutions among others that need our complete attention on security measures. \n" +
                "All connections on the public and internal networks are done over SSL encryption. We are monitoring login attempts to our system, while all the developers work with absolutely no access to client data. Salon Wizz is an official Amazon AWS Select Technology partner and our solution is built on top of Amazon Web Services infrastructure. \n" +
                "For file storage we use their S3 service which has 99.999999999% durability of objects over a given year. Every object is stored in three physically separate Availability Zones. In conjunction with daily snapshot and transaction log the data is effectively backed up in real time and can be restored to any point in time.\n"));

        faqModelArrayList.add(new FaqModel("Does Salon Wizz have a Desktop App?", "Yes, we have a web app where users can create an account, upload documents and add their utility details."));
        faqModelArrayList.add(new FaqModel("What will happen to posts that I made on my classic Page?", "All of your previous posts will be preserved on your Page, and insights will still be available for them."));
        faqModelArrayList.add(new FaqModel("Is there any content that won't migrate with my account?", "Some of your stories may not migrate.\n" +
                "Information on your classic Page, such as contact info, may not transition. Your bio may need to be shortened. You can edit this information by going into your About section and editing the relevant fields.\n" +
                "Your messages, ads and other features from classic Pages will migrate, but may be in different places.\n"));
        faqModelArrayList.add(new FaqModel("What features do I still have access to in new Pages?", "You will still have access to the following features: Feed, Instagram account linking, advertising, monetization, followers, insights, tools (such as Inbox, Stories, Groups, events), blue badge, and Page management by multiple people. See more available features.\n" +
                "Scheduling posts is not available yet on new Pages, but can be accessed through other tools. You can schedule posts to your new Page from Meta Business Suite and Creator Studio.\n"));
        faqModelArrayList.add(new FaqModel("Do you have any discounts for non-profits organizations?", "Yes! Please contact our sales team for more information. To provide the discount, a sales rep will need a copy of your 501(c)3."));

        courseRV =findViewById(R.id.faq_idRVCourse);
        faqAdapter = new FaqAdapter(knowledgebase_faq.this, faqModelArrayList);
        courseRV.setLayoutManager(new LinearLayoutManager(knowledgebase_faq.this));
        courseRV.setAdapter(faqAdapter);

        //getfaqs();

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    public void getfaqs(){
        final ProgressDialog progressDialog = new ProgressDialog(knowledgebase_faq.this);
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
                            if(userdata.getString("Type").equals("FAQS")){
                                // JSONObject Utilities=userdata.getJSONObject("Utilities");
                                String title=userdata.getString("Title");
                                String desciption=userdata.getString("Description");
                                String image=userdata.getString("URL");
                                String correcturl=image.replaceAll("\\\\","");
                                mode_faq st =new mode_faq(title,desciption,correcturl);
                                data.add(st);

                                courseRV =findViewById(R.id.faq_idRVCourse);
                                courseRV.setHasFixedSize(true);
                                courseRV.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
                                mAdapter = new faq_adapter(knowledgebase_faq.this,data);
                                courseRV.setAdapter(mAdapter);
                            }}
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