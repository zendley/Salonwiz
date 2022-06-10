package com.speckpro.salonwiz.loginauth;

import static com.speckpro.salonwiz.BaseUrl.baseurl;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.speckpro.salonwiz.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class aftergooglefacebookregisteration extends AppCompatActivity {

    CheckBox terms;
    private EditText pnumber,buisnessname,buisnessaddress,postcode;
    private String fbfname,fblname,fbemail,fbprofileurl;
    Button aftefbgooglelgsubmit;

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

        setContentView(R.layout.activity_aftergooglefacebookregisteration);
        //  Intent intent = getIntent();
    /*    TextView textView =findViewById(R.id.toolbar_titletext);
        textView.setText("Register");
        textView.setTextColor(Color.BLACK);
        ImageView backarrow=findViewById(R.id.toolbar_backarrow);
        backarrow.setBackgroundResource(R.drawable.ice_baseline_arrow_back_24_black);
        backarrow.setImageDrawable(getResources().getDrawable(R.drawable.ice_baseline_arrow_back_24_black));
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent s = new Intent(aftergooglefacebookregisteration.this, login.class);
                startActivity(s);
            }
        });*/
        //  fbprofileurl = intent.getStringExtra("fprofileURL");
        buisnessname=findViewById(R.id.rbuisname);
        buisnessaddress=findViewById(R.id.rbuisaddress);
        postcode=findViewById(R.id.rpostcode);
        terms=findViewById(R.id.rtermscheck);
        EditText fbpnumber=findViewById(R.id.rphoneno);

        aftefbgooglelgsubmit=findViewById(R.id.fbgoogleregisterbtn);
        aftefbgooglelgsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((!terms.isChecked())||postcode.getText().toString().isEmpty() || fbpnumber.getText().toString().isEmpty()|| buisnessname.getText().toString().isEmpty()|| buisnessaddress.getText().toString().isEmpty()) {
                    Toast.makeText(aftergooglefacebookregisteration.this, "Please dont leave blank values", Toast.LENGTH_SHORT).show();
                } else{
                    fbfname = getIntent().getStringExtra("ffirstName");
                    fblname = getIntent().getStringExtra("flastName");
                    fbemail = getIntent().getStringExtra("femail");
                    Log.v("Data For Popup Post",fbfname+" "+fblname+fbemail+fbpnumber.getText().toString()+buisnessname.getText().toString()+buisnessaddress.getText().toString()+postcode.getText().toString());
                    postData(fbfname,fblname,fbemail,fbpnumber.getText().toString(), buisnessname.getText().toString(),buisnessaddress.getText().toString(),postcode.getText().toString());
                }
            }
        });
    }

    @Override
    public void onBackPressed() {

    }

    private void postData(String sfname,String slname,String semail,String spnumber,String sbuisnessname,String sbuisnessaddress,String postcode) {

        //Log.v("Data For Popup Post",sfname+" "+slname+semail+spnumber+sbuisnessname+sbuisnessaddress+postcode);

        final ProgressDialog progressDialog = new ProgressDialog(aftergooglefacebookregisteration.this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait"); // set message
        progressDialog.show(); // show progress dialog

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseurl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        fbgregistermodelclass modal = new fbgregistermodelclass(sfname,slname,semail,spnumber,sbuisnessname,sbuisnessaddress,postcode);
        Call<fbgregistermodelclass> call = retrofitAPI.dbgregister(modal);
        call.enqueue(new Callback<fbgregistermodelclass>() {
            @Override
            public void onResponse(Call<fbgregistermodelclass> call, Response<fbgregistermodelclass> response) {
                progressDialog.dismiss();
                if(response.isSuccessful()){
                    //  Toast.makeText(aftergooglefacebookregisteration.this, "Data added to API", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(aftergooglefacebookregisteration.this, login.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(aftergooglefacebookregisteration.this, "Failed", Toast.LENGTH_SHORT).show();
                }
                fbgregistermodelclass responseFromAPI = response.body();
            }
            @Override
            public void onFailure(Call<fbgregistermodelclass> call, Throwable t) {
                Toast.makeText(aftergooglefacebookregisteration.this, "Failed"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean validate() {
        boolean temp=true;
        if(!terms.isChecked()){
            Toast.makeText(aftergooglefacebookregisteration.this,"Please accept terms",Toast.LENGTH_SHORT).show();
            temp=false;
        }
        return temp;
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