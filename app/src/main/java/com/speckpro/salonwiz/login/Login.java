package com.speckpro.salonwiz.login;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.speckpro.salonwiz.R;
import com.speckpro.salonwiz.newmodels.LoginModel;
import com.speckpro.salonwiz.newmodels.ResetPasswordModel;
import com.speckpro.salonwiz.register.AfterRegisterGoogleFacebook;
import com.speckpro.salonwiz.register.Register;
import com.speckpro.salonwiz.retrofit.ApiClient;
import com.speckpro.salonwiz.retrofit.ApiService;
import com.speckpro.salonwiz.ui.MainDashboard;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Login extends AppCompatActivity {

    private TextView registertxt;
    private TextView forgetpass;
    private Button signinbtn;
    private Button signInButton,facebooklogin;
    private String femail, ffirstName, flastName, fprofileURL;
    CallbackManager callbackManager;
    String email, password;
    private GoogleSignInClient mGoogleSignInClient;
    private EditText id,pass;

    private ProgressBar progressBar;
    private ApiService apiService;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

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

        if (SDK_INT >= 19 && SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        setContentView(R.layout.activity_login);
        apiService = ApiClient.getClient().create(ApiService.class);
        progressBar = findViewById(R.id.progressBar_login);

        checkPermission();
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.i("Full Detail", loginResult.toString());
                        setFacebookData(loginResult);
                        //Log.i("Response", femail+ffirstName+flastName);
                        Intent s=new Intent(Login.this, AfterRegisterGoogleFacebook.class);
                        s.putExtra("femail",femail);
                        s.putExtra("ffirstName",ffirstName);
                        s.putExtra("flastName",flastName);
                        startActivity(s);
                    }
                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        //Log.e("MYAPP", "exception", exception);
                    }
                });

        facebooklogin=findViewById(R.id.login_facebookbtn);
        facebooklogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().setLoginBehavior(LoginBehavior.WEB_ONLY);
                LoginManager.getInstance().logInWithReadPermissions(Login.this, Arrays.asList("public_profile","email"));
            }
        });

        id=findViewById(R.id.loginid);
        pass=findViewById(R.id.loginpass);
        signInButton=findViewById(R.id.logingooglebtn);
        
        GoogleSignInOptions gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.logingooglebtn:
                        resultLauncher.launch(new Intent(mGoogleSignInClient.getSignInIntent()));
                        break;
                }
            }
        });

        signinbtn=findViewById(R.id.signinbtn);
        signinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (id.getText().toString().isEmpty() && pass.getText().toString().isEmpty() ) {
                    Toast.makeText(Login.this, "Please dont leave blank values", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(validate()) {

                    String email = id.getText().toString().trim();
                    String password = pass.getText().toString().trim();
                    loginUser(email, password);

//                    postdatawithvolley(id.getText().toString(), pass.getText().toString());
                }
            }
        });

        registertxt=findViewById(R.id.registertext);
        registertxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login.this, Register.class);
                startActivity(i);
            }
        });

        forgetpass =findViewById(R.id.forgotpass);
        forgetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                final Dialog dialog = new Dialog(Login.this);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setContentView(R.layout.popup_forgetpass);
                EditText forgetemail=dialog.findViewById(R.id.resetpassedittext);
                Button dialogButton = dialog.findViewById(R.id.resetpass);
                ImageButton closebtn= dialog.findViewById(R.id.crossdismiss);
                closebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(EMAIL_ADDRESS_PATTERN.matcher(forgetemail.getText().toString()).matches()){
                            resetPassword(forgetemail.getText().toString());
                            dialog.dismiss();
                        } else {
                            Toast.makeText(Login.this,"Invalid Email Address",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.show();
            }
        });
    }

    private void loginUser(String email, String password) {
        progressBar.setVisibility(View.VISIBLE);
        Call<ArrayList<LoginModel>> call = apiService.login("application/json", email, password);
        call.enqueue(new Callback<ArrayList<LoginModel>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<LoginModel>> call, @NonNull Response<ArrayList<LoginModel>> response) {
                Log.d("TAG","LOGIN"+ response);

                if(response.isSuccessful()) {
                    progressBar.setVisibility(View.VISIBLE);
                    assert response.body() != null;
                    //get user details from customer api
                    if(response.body().get(0).getOriginal().getStatus().equals("Success")){
                        String id = String.valueOf(response.body().get(0).getOriginal().getData().getUser().getId());
                        String email = response.body().get(0).getOriginal().getData().getUser().getEmail();
                        String token = response.body().get(0).getOriginal().getData().getToken();
                        String FirstName = response.body().get(0).getOriginal().getData().getUser().getFirstName();
                        String LastName = response.body().get(0).getOriginal().getData().getUser().getLastName();
                        String DOB = response.body().get(0).getOriginal().getData().getUser().getFirstName();
                        String City = String.valueOf(response.body().get(0).getOriginal().getData().getUser().getDob());
                        String ContactNumber = response.body().get(0).getOriginal().getData().getUser().getContactNumber();
                        String BussinessName = response.body().get(0).getOriginal().getData().getUser().getBussinessName();
                        String BussinessAddress = response.body().get(0).getOriginal().getData().getUser().getBussinessAddress();
                        String PostCode = response.body().get(0).getOriginal().getData().getUser().getPostCode();
                        String email_verified_at = String.valueOf(response.body().get(0).getOriginal().getData().getUser().getEmailVerifiedAt());
                        String Image = String.valueOf(response.body().get(0).getOriginal().getData().getUser().getImage());
                        String SocialMedia = String.valueOf(response.body().get(0).getOriginal().getData().getUser().getSocialMedia());
                        String service = String.valueOf(response.body().get(0).getOriginal().getData().getUser().getService());
                        String packageName = response.body().get(0).getOriginal().getData().getUser().getPackage();
                        String pdate = response.body().get(0).getOriginal().getData().getUser().getPdate();
                        String created_at = String.valueOf(response.body().get(0).getOriginal().getData().getUser().getCreatedAt());
                        String updated_at = response.body().get(0).getOriginal().getData().getUser().getUpdatedAt();

                        //Preference to Store User Credentials
                        sharedpreferences = getSharedPreferences("MySalonSharedPref", MODE_PRIVATE);
                        editor = sharedpreferences.edit();

                        editor.putBoolean("Registered", true);
                        editor.putBoolean("islogged", true);
                        editor.putString("Image", String.valueOf(Image));
                        editor.putString("id", id);
                        editor.putString("email", email);
                        editor.putString("token", token);
                        editor.putString("FirstName", FirstName);
                        editor.putString("LastName", LastName);
                        editor.putString("DOB", DOB);
                        editor.putString("City", City);
                        editor.putString("ContactNumber", ContactNumber);
                        editor.putString("BussinessName", BussinessName);
                        editor.putString("BussinessAddress", BussinessAddress);
                        editor.putString("PostCode", PostCode);
                        editor.putString("email_verified_at", email_verified_at);
                        editor.putString("SocialMedia", SocialMedia);
                        editor.putString("service", service);
                        editor.putString("packageName", packageName);
                        editor.putString("pdate", pdate);
                        editor.putString("created_at", created_at);
                        editor.putString("updated_at", updated_at);
                        editor.apply();

                        startActivity(new Intent(Login.this, MainDashboard.class));
                        finish();
                    } else {
                        Toast.makeText(Login.this, "Error Occured! "+response.body().get(0).getOriginal().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    Toast.makeText(Login.this, "Something went wrong!" + response.message(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ArrayList<LoginModel>> call, Throwable t) {
                progressBar.setVisibility(View.VISIBLE);
                Log.d("TAG","LoginFail"+t);
                Toast.makeText(Login.this, "Check your Internet Connection!, Failed Due to: "+ t, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void resetPassword(String email) {
        progressBar.setVisibility(View.VISIBLE);
        Call<ResetPasswordModel> call = apiService.resetPassword("application/json", "token", email);
        call.enqueue(new Callback<ResetPasswordModel>() {
            @Override
            public void onResponse(@NonNull Call<ResetPasswordModel> call, @NonNull Response<ResetPasswordModel> response) {
                Log.d("TAG","LOGIN"+ response);

                if(response.isSuccessful()) {
                    progressBar.setVisibility(View.VISIBLE);
                    assert response.body() != null;
                    //get user details from customer api
                    if(response.body().getOriginal().getMessage().equals("Updated Successfully")){
                        //reset password
                        Toast.makeText(Login.this, "Reset Password Email sent to your mail address!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Login.this, "email does not exist or registered!", Toast.LENGTH_SHORT).show();
                       // Toast.makeText(Login.this, "Error Occured! "+response.body().getOriginal().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    Toast.makeText(Login.this, "Something went wrong!" + response.message(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ResetPasswordModel> call, Throwable t) {
                progressBar.setVisibility(View.VISIBLE);
                Log.d("TAG","LoginFail"+t);
                Toast.makeText(Login.this, "Check your Internet Connection!, Failed Due to: "+ t, Toast.LENGTH_SHORT).show();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            //    Toast.makeText(this, "Google Sign in Successfull", Toast.LENGTH_SHORT).show();
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
            if (acct != null) {
                String personName = acct.getDisplayName();
                String personGivenName = acct.getGivenName();
                String personFamilyName = acct.getFamilyName();
                String personEmail = acct.getEmail();
                String personId = acct.getId();
                String[] arr=personName.split(" ");

                String fname=arr[0];
                String lname=arr[1];
                //Log.w("googledata",  fname+" "+lname+personEmail);
                ////getuserdatawithemail(personEmail,fname,lname);
                Uri personPhoto = acct.getPhotoUrl();
                femail=personEmail;
                ffirstName=fname;
                flastName=lname;
                //Log.i("Response", femail+ffirstName+flastName);
            }
        } catch (ApiException e) {
            Log.w("Error", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (email != null && password != null) {
            Intent i = new Intent(Login.this, MainDashboard.class);
            startActivity(i);
        }
    }

    private void setFacebookData(final LoginResult loginResult) {
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            Log.i("ResponseObject", object.toString());
                            Log.i("OnlyResponse", response.toString());
                            femail = object.getString("email");
                            ffirstName = object.getString("first_name");
                            flastName = object.getString("last_name");
                            Log.i("Response", femail+ffirstName+flastName);
                            ////getuserdatawithemail(femail,ffirstName,flastName);

                            //TODO put your code here
                        } catch (JSONException e) {
                            Toast.makeText(Login.this,e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,email,first_name,last_name");
        request.setParameters(parameters);
        request.executeAsync();
    }

    public boolean validate() {
        boolean temp=true;
        String checkemail = id.getText().toString();
        if(!EMAIL_ADDRESS_PATTERN.matcher(checkemail).matches()){
            Toast.makeText(Login.this,"Invalid Email Address",Toast.LENGTH_SHORT).show();
            temp=false;
        }
        return temp;
    }

//    public void getuserdatawithemail(String email,String fname,String lname){
//        final ProgressDialog progressDialog = new ProgressDialog(Login.this);
//        progressDialog.setCancelable(false); // set cancelable to false
//        progressDialog.setMessage("Please Wait"); // set message
//        progressDialog.show(); // show progress dialog
//        String url2=baseurl+"api/user/getoneuser";
//        try {
//            JSONObject object1=new JSONObject();
//            object1.put("Email",email);
//            RequestQueue mqueue5 = Volley.newRequestQueue(getApplicationContext());
//            JsonObjectRequest jsonObjRequest5 = new JsonObjectRequest(Request.Method.POST,
//                    url2, object1, new com.android.volley.Response.Listener<JSONObject>() {
//                @Override
//                public void onResponse(JSONObject response) {
//                    Log.d("myapp", "the response is" + response.toString());
//                    try {
//                        JSONObject json = new JSONObject(String.valueOf(response));
//                        JSONArray result = json.getJSONArray("Data");
//                        JSONObject userdata=result.getJSONObject(0);
//                        String message=json.getString("message");
//                        if(message.equals("Users Found")){
//                            String id=userdata.getString("_id");
//                            String email=userdata.getString("Email");
//                            String Password=userdata.getString("Password");
//                            String InitLogin=userdata.getString("InitLogin");
//                            String Role=userdata.getString("Role");
//                            String BusinessAddress=userdata.getString("BusinessAddress");
//                            String ContactNumber=userdata.getString("ContactNumber");
//                            String FirstName=userdata.getString("FirstName");
//                            String LastName=userdata.getString("LastName");
//                            String PostCode=userdata.getString("PostCode");
//                            String SocialMedia=userdata.getString("SocialMedia");
//                            String Filling=userdata.getString("Filling");
//                            String BusinessName=userdata.getString("BusinessName");
//                            SharedPreferences sharedpreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
//                            SharedPreferences.Editor editor = sharedpreferences.edit();
//
//                            editor.putBoolean("Registered", true);
//                            editor.putBoolean("islogged", true);
//                            editor.putString("id", id);
//                            editor.putString("email", email);
//                            editor.putString("Password", Password);
//                            editor.putString("InitLogin", InitLogin);
//                            editor.putString("Role", Role);
//                            editor.putString("BusinessAddress", BusinessAddress);
//                            editor.putString("ContactNumber", ContactNumber);
//                            editor.putString("FirstName", FirstName);
//                            editor.putString("LastName", LastName);
//                            editor.putString("PostCode", PostCode);
//                            editor.putString("SocialMedia", SocialMedia);
//                            editor.putString("Filling", Filling);
//                            editor.putString("BusinessName", BusinessName);
//                            editor.apply();
//                            Toast.makeText(getApplicationContext(), "Login Succesfully", Toast.LENGTH_SHORT).show();
//                            Intent intent=new Intent(Login.this, MainDashboard.class);
//                            startActivity(intent);
//                            progressDialog.dismiss();
//                        } else{
//                            Toast.makeText(getApplicationContext(), "Going for registration", Toast.LENGTH_SHORT).show();
//                            progressDialog.dismiss();
//                            Intent intents=new Intent(Login.this, AfterRegisterGoogleFacebook.class);
//                            intents.putExtra("femail",email);
//                            intents.putExtra("ffirstName",fname);
//                            intents.putExtra("flastName",lname);
//                            startActivity(intents);
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                        Toast.makeText(getApplicationContext(), "Data Exception", Toast.LENGTH_SHORT).show();
//                        progressDialog.dismiss();
//                    }
//                }
//            }, new com.android.volley.Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    //  progressDialog.dismiss();
//                    //  Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
//                    Toast.makeText(getApplicationContext(), "Going for registration", Toast.LENGTH_SHORT).show();
//                    progressDialog.dismiss();
//                    Intent intents=new Intent(Login.this, AfterRegisterGoogleFacebook.class);
//                    intents.putExtra("femail",email);
//                    intents.putExtra("ffirstName",fname);
//                    intents.putExtra("flastName",lname);
//                    startActivity(intents);
//                }
//            }) { @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<>();
//                headers.put("Content-Type", "application/json");
//                return headers;
//            }
//            };
//            mqueue5.add(jsonObjRequest5);
//        } catch (JSONException jsonException) {
//            jsonException.printStackTrace();
//            Toast.makeText(getApplicationContext(), "jsonException", Toast.LENGTH_SHORT).show();
//            progressDialog.dismiss();
//        }
//    }

    private boolean checkPermission() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            int result = ContextCompat.checkSelfPermission(Login.this, READ_EXTERNAL_STORAGE);
            int result1 = ContextCompat.checkSelfPermission(Login.this, WRITE_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
        }
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