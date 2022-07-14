package com.speckpro.salonwiz.ui.user;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.speckpro.salonwiz.R;

public class ForgetPassword extends AppCompatActivity {

    SharedPreferences userPreferences;
    private String id, email, password;
    private EditText etOldPassword, etNewPassword, etConfirmNewPassword;
    private Button btnSubmitPassword;
    private String oldPassword, newPassword, confirmNewPassword;
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
        setContentView(R.layout.activity_forget_password);

        TextView textView =findViewById(R.id.toolbar_titletext);
        textView.setText("Password");
        ImageView backarrow=findViewById(R.id.toolbar_backarrow);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        userPreferences = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        id = userPreferences.getString("id", "");
        email = userPreferences.getString("email", "");
        password = userPreferences.getString("Password", "");

        etOldPassword = findViewById(R.id.editText_oldPassword);
        etNewPassword = findViewById(R.id.editText_newPassword);
        etConfirmNewPassword = findViewById(R.id.editText_confirmNewPassword);

        btnSubmitPassword = findViewById(R.id.button_updatePassword);
        btnSubmitPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oldPassword = etOldPassword.getText().toString();
                newPassword = etNewPassword.getText().toString();
                confirmNewPassword = etConfirmNewPassword.getText().toString();

                if(oldPassword.isEmpty() || newPassword.isEmpty() || confirmNewPassword.isEmpty()){
                    Toast.makeText(ForgetPassword.this, "Enter All Fields!", Toast.LENGTH_SHORT).show();
                }
//                else  if(!oldPassword.equals(password)){
//                    Toast.makeText(ForgetPassword.this, "Enter Correct Old Password!"+password, Toast.LENGTH_SHORT).show();
//                }
                else  if(!confirmNewPassword.equals(newPassword)){
                    Toast.makeText(ForgetPassword.this, "Confirm Password does not match!", Toast.LENGTH_SHORT).show();
                } else if(newPassword.length() < 4) {
                        Toast.makeText(ForgetPassword.this, "Password lenght should be more than 4 characters!", Toast.LENGTH_SHORT).show();
                } else {
                    ////updatePassword(oldPassword, newPassword, email);
                }

            }
        });


    }

//    private void updatePassword(String oldPassword, String newPassword, String email) {
//        final ProgressDialog progressDialog = new ProgressDialog(ForgetPassword.this);
//        progressDialog.setCancelable(false); // set cancelable to false
//        progressDialog.setMessage("Please Wait"); // set message
//        progressDialog.show(); // show progress dialog
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(baseurl)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        RequestBody oldPass =
//                RequestBody.create(MediaType.parse("multipart/form-data"), oldPassword);
//
//        RequestBody newPass =
//                RequestBody.create(MediaType.parse("multipart/form-data"),  newPassword);
//        RequestBody useremail =
//                RequestBody.create(MediaType.parse("multipart/form-data"), email);
//
//        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
//
//        Call<ResponseBody> call = retrofitAPI.updatePassword(oldPass,newPass,useremail);
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                progressDialog.dismiss();
//                Toast.makeText(ForgetPassword.this, "Password Updated", Toast.LENGTH_SHORT).show();
//                if(response.isSuccessful()){
//                    progressDialog.dismiss();
//                    Log.i(TAG, response.toString());
//                    SharedPreferences sharedpreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
//                    SharedPreferences.Editor editor = sharedpreferences.edit();
//                    editor.putString("Password", newPassword);
//                    editor.apply();
//                    Intent i = new Intent(ForgetPassword.this, PersonnalSettings.class);
//                    finish();
//                    overridePendingTransition(0, 0);
//                    startActivity(i);
//                    overridePendingTransition(0, 0);
//                }
//                else {
//                    progressDialog.dismiss();
//                    Toast.makeText(getApplicationContext(),"Password Update Failed!",Toast.LENGTH_SHORT);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                progressDialog.dismiss();
//                Toast.makeText(getApplicationContext(),"Check Your Internet Connection!",Toast.LENGTH_SHORT);
//            }
//        });
//    }

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