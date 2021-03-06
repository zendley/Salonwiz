package com.speckpro.salonwiz.ui.services;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.speckpro.salonwiz.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ServiceDetailActivity extends AppCompatActivity {

    private Button getpackage;
    private String packagename,id,currentdate;
    private Boolean status;

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
        setContentView(R.layout.activity_service_detail);
        SharedPreferences sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);

        id = sh.getString("id", "");
        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
        Date todayDate = new Date();
        currentdate = currentDate.format(todayDate);
        Intent i=getIntent();
        packagename=i.getStringExtra("packagetitle");
        status=i.getBooleanExtra("status",false);
        getpackage=findViewById(R.id.market_basicpackagebtn);
        if (status){
            getpackage.setClickable(false);
            getpackage.setBackground(getResources().getDrawable(R.drawable.themebtn3disabled));
        }

        getpackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (status){
                    Toast.makeText(getApplicationContext(), "Package already requested",
                            Toast.LENGTH_LONG).show();
                }else{
                Log.v("Data For Popup Post",id+" "+currentdate+" "+packagename);
                //postData(id,currentdate,packagename);
                }
            }
        });

        TextView textView =findViewById(R.id.toolbar_titletext);
        textView.setText(packagename);

        ImageView backarrow=findViewById(R.id.toolbar_backarrow);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

//    private void postData(String userid, String Date, String packagename) {
//        //Log.v("Data For Popup Post",userid+" "+Date+" "+packagename);
//        final ProgressDialog progressDialog = new ProgressDialog(ServiceDetailActivity.this);
//        progressDialog.setCancelable(false);
//        progressDialog.setMessage("Please Wait");
//        progressDialog.show();
//
//        RequestBody User =
//                RequestBody.create(MediaType.parse("multipart/form-data"), userid);
//
//        RequestBody status =
//                RequestBody.create(MediaType.parse("multipart/form-data"),"Pending");
//        RequestBody packagetitle =
//                RequestBody.create(MediaType.parse("multipart/form-data"), packagename);
//        RequestBody currentdate =
//                RequestBody.create(MediaType.parse("multipart/form-data"), Date);
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(baseurl)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
//        Call<ResponseBody> call = retrofitAPI.useradd(User,status,packagetitle,currentdate);
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                if(response!=null){
//                    //Log.d("TAG", "onResponse: "+response.body());
//                    //Log.d("Response Message", "onResponse: "+response.message());
//                    Toast.makeText(ServiceDetailActivity.this, "Package Request Sent Successfully!", Toast.LENGTH_SHORT).show();
//                    finish();
////                    Intent intent=new Intent(getApplicationContext(),services.class);
////                    intent.putExtra("packagestatus","basicPending");
////                    startActivity(intent);
//                }else{
//                    Toast.makeText(ServiceDetailActivity.this, "Data not added, Contact to Speckpro", Toast.LENGTH_SHORT).show();
//                }
//            }
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Toast.makeText(ServiceDetailActivity.this, "Failed"+t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
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