package com.example.salonwiz.loginauth;

import static com.example.salonwiz.BaseUrl.baseurl;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import android.widget.TextView;
import android.widget.Toast;


import com.example.salonwiz.R;
import com.example.salonwiz.afterlogin.maindashboard;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.awt.font.TextAttribute;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class register extends AppCompatActivity {
    private TextView signintext;
    private EditText fname,lname,email,pass,cpass,pnumber,buisnessname,buisnessaddress,postcode;
    private CheckBox terms;
    private Button registerbtn;
    private TextView responseTV;
    private Button signInButton;
    public final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9+._%-+]{1,256}" +
                    "@" +
                    "[a-zA-Z0-9][a-zA-Z0-9-]{0,64}" +
                    "(" +
                    "." +
                    "[a-zA-Z0-9][a-zA-Z0-9-]{0,25}" +
                    ")+"
    );

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
        setContentView(R.layout.activity_register);

        fname=findViewById(R.id.rfirstname);
        lname=findViewById(R.id.rsecondname);
        email=findViewById(R.id.remail);
        pass=findViewById(R.id.rrpass);
        cpass=findViewById(R.id.rrconfirmpass);
        pnumber=findViewById(R.id.rphoneno);
        buisnessname=findViewById(R.id.rbuisname);
        buisnessaddress=findViewById(R.id.rbuisaddress);
        registerbtn=findViewById(R.id.rregisterbtn);
        responseTV=findViewById(R.id.responsetv);
        postcode=findViewById(R.id.rpostcode);
        terms=findViewById(R.id.rtermscheck);
        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (postcode.getText().toString().isEmpty() && fname.getText().toString().isEmpty() && lname.getText().toString().isEmpty() && email.getText().toString().isEmpty()&& pass.getText().toString().isEmpty() && pnumber.getText().toString().isEmpty() && buisnessname.getText().toString().isEmpty()&& buisnessaddress.getText().toString().isEmpty()) {
                    Toast.makeText(register.this, "Please dont leave blank values", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(validate()) {
                    postData(fname.getText().toString(), lname.getText().toString(), email.getText().toString(), pass.getText().toString(), pnumber.getText().toString(), buisnessname.getText().toString(), buisnessaddress.getText().toString(),postcode.getText().toString());
                }
            }
        });

        signintext=findViewById(R.id.rsignintext);
        signintext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(register.this, login.class);
                startActivity(i);
            }
        });
    }

    ActivityResultLauncher<Intent> resultLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode()==Activity.RESULT_OK){
                Intent intent=result.getData();
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(intent);
                handleSignInResult(task);
            }
        }
    });

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Toast.makeText(this, "Sign in Successfull", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(getApplicationContext(), maindashboard.class);
            startActivity(intent);
        } catch (ApiException e) {
            Log.w("Error", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void postData(String sfname, String slname, String semail, String spass, String spnumber, String sbuisnessname, String sbuisnessaddress, String postcode) {
        final ProgressDialog progressDialog = new ProgressDialog(register.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait");
        progressDialog.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseurl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        registermodelclass modal = new registermodelclass(sfname, slname,semail,spass,spnumber,sbuisnessname,sbuisnessaddress,postcode);
        Call<registermodelclass> call = retrofitAPI.createPost(modal);
        call.enqueue(new Callback<registermodelclass>() {
            @Override
            public void onResponse(Call<registermodelclass> call, Response<registermodelclass> response) {
                progressDialog.dismiss();
                Toast.makeText(register.this, "Your acccount has been Registered.Please Login", Toast.LENGTH_LONG).show();
                Intent intent=new Intent(register.this,login.class);
                startActivity(intent);
                registermodelclass responseFromAPI = response.body();
            }

            @Override
            public void onFailure(Call<registermodelclass> call, Throwable t) {
                Toast.makeText(register.this, "Failed"+t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }


    public boolean validate() {
        boolean temp=true;
        String checkemail = email.getText().toString();
        String password=pass.getText().toString();
        String confirmpass=cpass.getText().toString();
        if(!EMAIL_ADDRESS_PATTERN.matcher(checkemail).matches()){
            Toast.makeText(register.this,"Invalid Email Address",Toast.LENGTH_SHORT).show();
            temp=false;
        }
        else if(!password.equals(confirmpass)){
            Toast.makeText(register.this,"Password Not matching",Toast.LENGTH_SHORT).show();
            temp=false;
        }
//        else if(!terms.isChecked()){
//            Toast.makeText(register.this,"Please accept terms",Toast.LENGTH_SHORT).show();
//            temp=false;
//        }
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