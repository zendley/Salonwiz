package com.speckpro.salonwiz.register;

import android.app.Activity;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.speckpro.salonwiz.R;
import com.speckpro.salonwiz.login.Login;
import com.speckpro.salonwiz.newmodels.RegisterModel;
import com.speckpro.salonwiz.retrofit.ApiClient;
import com.speckpro.salonwiz.retrofit.ApiService;
import com.speckpro.salonwiz.ui.MainDashboard;

import java.util.ArrayList;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends AppCompatActivity {
    private TextView signintext;
    private EditText etfname,etlname,etemail,etpass,etcpass,etpnumber,etbuisnessname,etbuisnessaddress,etpostcode;
    private CheckBox terms;
    private Button registerbtn;
    private TextView responseTV;
    private Button signInButton;

    private ProgressBar progressBar;
    private ApiService apiService;
    SharedPreferences sharedpreferences;

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

        apiService = ApiClient.getClient().create(ApiService.class);
        progressBar = findViewById(R.id.progressBar_register);

        etfname = findViewById(R.id.rfirstname);
        etlname = findViewById(R.id.rsecondname);
        etemail = findViewById(R.id.remail);
        etpass = findViewById(R.id.rrpass);
        etcpass = findViewById(R.id.rrconfirmpass);
        etpnumber = findViewById(R.id.rphoneno);
        etbuisnessname = findViewById(R.id.rbuisname);
        etbuisnessaddress = findViewById(R.id.rbuisaddress);
        etpostcode = findViewById(R.id.rpostcode);
        terms = findViewById(R.id.rtermscheck);
        registerbtn = findViewById(R.id.rregisterbtn);
        responseTV = findViewById(R.id.responsetv);

        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstname = etfname.getText().toString().trim();
                String lastname = etlname.getText().toString().trim();
                String email = etemail.getText().toString().trim();
                String password = etpass.getText().toString().trim();
                String cpassword = etcpass.getText().toString().trim();
                String phone = etpnumber.getText().toString().trim();
                String businessName = etbuisnessname.getText().toString().trim();
                String businessAddress = etbuisnessaddress.getText().toString().trim();
                String postcode = etpostcode.getText().toString().trim();

                if (postcode.isEmpty() && firstname.isEmpty() && lastname.isEmpty() && email.isEmpty()&& password.isEmpty() && phone.isEmpty() && businessName.isEmpty()&& businessAddress.isEmpty()) {
                    Toast.makeText(Register.this, "Please dont leave blank values", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(validate()) {
                    registerUser(email, password, firstname, lastname, phone, businessName, businessAddress, postcode);
                }
            }
        });

        signintext=findViewById(R.id.rsignintext);
        signintext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Register.this, Login.class);
                startActivity(i);
            }
        });
    }

    private void registerUser(String email, String pass, String fname, String lname, String pnumber, String buisnessname, String buisnessaddress, String postcode) {
        progressBar.setVisibility(View.VISIBLE);
        Call<ArrayList<RegisterModel>> call = apiService.register("application/json", email, pass, fname, lname, pnumber, buisnessname, buisnessaddress, postcode);
        call.enqueue(new Callback<ArrayList<RegisterModel>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<RegisterModel>> call, @NonNull Response<ArrayList<RegisterModel>> response) {
                Log.d("TAG","Register: "+response);
                if(response.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    //get user details from customer api
                    assert response.body() != null;
                    if(response.body().get(0).getData().getToken()!=null) {
                        startActivity(new Intent(Register.this, Login.class));
                        finish();
                    } else {
                        Toast.makeText(Register.this, "Error Occured! "+response.body().get(0).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
//                    Toast.makeText(Register.this, "Something went wrong!" + response.body().get(0).getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ArrayList<RegisterModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.d("TAG","RegisterFail"+t);
                Toast.makeText(Register.this, "Check your Internet Connection! RegisterFail Due to: "+t, Toast.LENGTH_SHORT).show();
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
            Intent intent=new Intent(getApplicationContext(), MainDashboard.class);
            startActivity(intent);
        } catch (ApiException e) {
            Log.w("Error", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    public boolean validate() {
        boolean temp=true;
        String checkemail = etemail.getText().toString();
        String password=etpass.getText().toString();
        String confirmpass=etcpass.getText().toString();
        if(!EMAIL_ADDRESS_PATTERN.matcher(checkemail).matches()){
            Toast.makeText(Register.this,"Invalid Email Address",Toast.LENGTH_SHORT).show();
            temp=false;
        }
        else if(!password.equals(confirmpass)){
            Toast.makeText(Register.this,"Password Not matching",Toast.LENGTH_SHORT).show();
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