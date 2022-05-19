package com.example.salonwiz.afterlogin.filingcabinet;

import static com.example.salonwiz.BaseUrl.baseurl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.salonwiz.R;
import com.example.salonwiz.afterlogin.filingcabinet.generalcard.gencard_adapter;
import com.example.salonwiz.afterlogin.filingcabinet.generalcard.gencard_model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class utilitiesbills_main extends AppCompatActivity {

    private DatePickerDialog datePickerDialog;
    private TextView dateButton;
    private Button generalupload;
    private EditText search;
    private RecyclerView courseRV,courseRVs;
    public static final int REQUEST_CODE = 101;
    private final ArrayList<gencard_model> data= new ArrayList<>();
    private String syear,smonth,scompdate;
    private gencard_adapter courseAdapter,courseAdapters;
    private String email;

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
        setContentView(R.layout.activity_utilitiesbills_main);
        SharedPreferences sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        email = sh.getString("email", "");
        getdata(email);

        dateButton = findViewById(R.id.datePickerButton);
        String searchdate=dateButton.getText().toString();
        search=findViewById(R.id.utilitiesbill_searchtext);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
            }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // getdata(email,true);
            }
            @Override
            public void afterTextChanged(Editable arg0) {
                //  getdata(email,true);
                if(arg0.length()>0) {
                    getdataSearch(email);
                } else {
                    getdata(email);
                }
            }
        });

        generalupload=findViewById(R.id.utilitybills_uploadbutton);
        generalupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent s = new Intent(utilitiesbills_main.this, utilitiesbills_uploadfile.class);
                startActivity(s);
            }
        });
        Button uplaodfiletoolbar=findViewById(R.id.toolbar_uploadfile);
        uplaodfiletoolbar.setVisibility(View.VISIBLE);
        uplaodfiletoolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent s = new Intent(utilitiesbills_main.this, utilitiesbills_uploadfile.class);
                startActivity(s);
            }
        });
        TextView textView =findViewById(R.id.toolbar_titletext);
        textView.setText("Utility Bills");
        ImageView backarrow=findViewById(R.id.toolbar_backarrow);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        dateButton.setText(getTodaysDate());
        initDatePicker();
    }

    private String getTodaysDate()
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString( day,month,year);
    }

    private void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                String date = makeDateString(day,month,year);
                dateButton = findViewById(R.id.datePickerButton);
                dateButton.setText(date);
                Log.d("Response", date+month+year);
                SharedPreferences sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
                String emails = sh.getString("email", "");

                syear=String.valueOf(year);
                smonth=formatdoubledigit(month);
                scompdate=syear+"-"+smonth;
                Log.d("Response", scompdate);
                getdataforsearchcalender(emails,scompdate,true);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year,day ,month );
    }

    private String makeDateString(int day, int month, int year)
    {
        return getMonthFormat(month) + " " + year;
    }
    private String formatdoubledigit(int month){
        if(month == 1)
            return "01";
        if(month == 2)
            return "02";
        if(month == 3)
            return "03";
        if(month == 4)
            return "04";
        if(month == 5)
            return "05";
        if(month == 6)
            return "06";
        if(month == 7)
            return "07";
        if(month == 8)
            return "08";
        if(month == 9)
            return "09";
        if(month == 10)
            return "10";
        if(month == 11)
            return "11";

        return "12";
    }
    private String getMonthFormat(int month)
    {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";

        //default should never happen
        return "JAN";
    }

    public void openDatePicker(View view)
    {
        datePickerDialog.show();
    }

    public void getdata(String email){
        data.clear();
        courseRVs = findViewById(R.id.utilitybills_recycler);
        RequestQueue queue = Volley.newRequestQueue(this);
        final String url = baseurl+"api/user/getfilling?email="+email;
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        Log.d("Response", response.toString());
                        data.clear();
                        JSONObject json = null;
                        try {
                            json = new JSONObject(String.valueOf(response));
                            JSONArray result = json.getJSONArray("Data");
                            String asd="UTILITYBILL";
                            for(int i=0;i<result.length();i++){
                                JSONObject userdata=result.getJSONObject(i);
                                if(asd.equals(userdata.getString("Type"))){
                                    String Title=userdata.getString("Title");
                                    String url=userdata.getString("URL");
                                    String correcturl=url.replaceAll("\\\\","");
                                    String id=userdata.getString("_id");
                                    String Date=userdata.getString("Date");
                                    data.add(new gencard_model(correcturl,Title,id));
                                    Log.d("Response", correcturl+"             "+Title);}}

                            gencard_adapter courseAdapters = new gencard_adapter(utilitiesbills_main.this, data);
                            courseRVs.setLayoutManager(new GridLayoutManager(utilitiesbills_main.this,3));
                            courseRVs.setAdapter(courseAdapters);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        );
        queue.add(getRequest);
    }

    public void getdataSearch(String email){
        data.clear();
        courseRVs = findViewById(R.id.utilitybills_recycler);
        RequestQueue queue = Volley.newRequestQueue(this);
        final String url = baseurl+"api/user/getfilling?email="+email;
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        Log.d("Response", response.toString());
                        data.clear();
                        JSONObject json = null;
                        try {
                            search=findViewById(R.id.utilitiesbill_searchtext);
                            String seachtext=search.getText().toString();
                            seachtext=seachtext.toLowerCase();
                            // Boolean x=seachtext.indexOf("Android") !=-1? true: false;
                            json = new JSONObject(String.valueOf(response));
                            JSONArray result = json.getJSONArray("Data");
                            String asd="UTILITIESBILLS";
                            for(int i=0;i<result.length();i++){
                                JSONObject userdata=result.getJSONObject(i);

                                if(userdata.getString("Title").toLowerCase().contains(seachtext)&&asd.equals(userdata.getString("Type"))){

                                    String Title=userdata.getString("Title");
                                    String url=userdata.getString("URL");
                                    String correcturl=url.replaceAll("\\\\","");
                                    String id=userdata.getString("_id");
                                    String Date=userdata.getString("Date");
                                    data.add(new gencard_model(correcturl,Title,id));
                                    Log.d("Response", correcturl+"             "+Title);}}

                            courseAdapters = new gencard_adapter(utilitiesbills_main.this, data);
                            courseRVs.setLayoutManager(new GridLayoutManager(utilitiesbills_main.this,3));
                            courseRVs.setAdapter(courseAdapters);
                            courseAdapters.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        );
        queue.add(getRequest);
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        getdata(email);
//    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getdata(email);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void getdataforsearchcalender(String email,String date,boolean s){
        if(data!=null){
            data.clear();
        }
        courseRV = findViewById(R.id.utilitybills_recycler);
        RequestQueue queue9 = Volley.newRequestQueue(this);
        final String url = baseurl+"api/user/getfilling?email="+email;
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        Log.d("Response", response.toString());

                        JSONObject json = null;
                        try {
                            if(!s){
                                json = new JSONObject(String.valueOf(response));
                                JSONArray result = json.getJSONArray("Data");
                                String asd="UTILITIESBILLS";
                                for(int i=0;i<result.length();i++){
                                    JSONObject userdata=result.getJSONObject(i);
                                    if(asd.equals(userdata.getString("Type"))){
                                        String Title=userdata.getString("Title");
                                        String url=userdata.getString("URL");
                                        String correcturl=url.replaceAll("\\\\","");
                                        String id=userdata.getString("_id");
                                        String Date=userdata.getString("Date");
                                        data.add(new gencard_model(correcturl,Title,id));
                                        Log.d("Response", correcturl+"             "+Title);}}

                                courseAdapter = new gencard_adapter(utilitiesbills_main.this, data);
                                courseRV.setLayoutManager(new GridLayoutManager(utilitiesbills_main.this,3));
                                courseRV.setAdapter(courseAdapter); }
                            else{
                                search=findViewById(R.id.utilitiesbill_searchtext);
                                String seachtext=search.getText().toString();
                                seachtext=seachtext.toLowerCase();
                                // Boolean x=seachtext.indexOf("Android") !=-1? true: false;
                                json = new JSONObject(String.valueOf(response));
                                JSONArray result = json.getJSONArray("Data");
                                String asd="UTILITIESBILLS";
                                for(int i=0;i<result.length();i++){
                                    JSONObject userdata=result.getJSONObject(i);
                                    String cdate=userdata.getString("Date");
                                    cdate=cdate.substring(0,7);
                                    if(cdate.equals(date)&&asd.equals(userdata.getString("Type"))){

                                        String Title=userdata.getString("Title");
                                        String url=userdata.getString("URL");
                                        String correcturl=url.replaceAll("\\\\","");
                                        String id=userdata.getString("_id");
                                        String Date=userdata.getString("Date");
                                        data.add(new gencard_model(correcturl,Title,id));
                                        Log.d("Response", correcturl+"             "+Title);}}

                                gencard_adapter courseAdapter = new gencard_adapter(utilitiesbills_main.this, data);
                                courseRV.setLayoutManager(new GridLayoutManager(utilitiesbills_main.this,3));
                                courseRV.setAdapter(courseAdapter);
                                courseAdapter.notifyDataSetChanged();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        );
        queue9.add(getRequest);
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