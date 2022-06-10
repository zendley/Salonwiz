package com.speckpro.salonwiz.loginauth;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

import static com.speckpro.salonwiz.BaseUrl.baseurl;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.speckpro.salonwiz.R;
import com.speckpro.salonwiz.afterlogin.maindashboard;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class login extends AppCompatActivity {

    private TextView registertxt;
    private TextView forgetpass;
    private Button signinbtn;
    private Button signInButton,facebooklogin;
    private String femail,ffirstName,flastName,fprofileURL;
    CallbackManager callbackManager;
    String email, password;
    private GoogleSignInClient mGoogleSignInClient;
    private EditText id,pass;
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
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (email != null && password != null) {
            Intent i = new Intent(login.this, maindashboard.class);
            startActivity(i);
        }
    }

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

        checkPermission();
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.i("Full Detail", loginResult.toString());
                        setFacebookData(loginResult);
                        //Log.i("Response", femail+ffirstName+flastName);
                        Intent s=new Intent(login.this,aftergooglefacebookregisteration.class);
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
                LoginManager.getInstance().logInWithReadPermissions(login.this, Arrays.asList("public_profile","email"));
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
                    Toast.makeText(login.this, "Please dont leave blank values", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(validate()) {
                    postdatawithvolley(id.getText().toString(), pass.getText().toString());
                }
            }
        });

        registertxt=findViewById(R.id.registertext);
        registertxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(login.this, register.class);
                startActivity(i);
            }
        });

        forgetpass =findViewById(R.id.forgotpass);
        forgetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                final Dialog dialog = new Dialog(login.this);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setContentView(R.layout.popup_forgetpass);
                EditText forgetemail=dialog.findViewById(R.id.resetpassedittext);
                Button dialogButton = dialog.findViewById(R.id.resetpass);
                ImageButton closebtn= dialog.findViewById(R.id.crossdismiss);
                closebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                       /* if(EMAIL_ADDRESS_PATTERN.matcher(forgetemail.getText().toString()).matches()){
                            forgetpasspost(forgetemail.getText().toString());

                            Toast.makeText(getApplicationContext(),"Dismissed..!!",Toast.LENGTH_SHORT).show();

                        }else{
                            Toast.makeText(login.this,"Invalid Email Address",Toast.LENGTH_SHORT).show();
                        }*/
                    }
                });
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(EMAIL_ADDRESS_PATTERN.matcher(forgetemail.getText().toString()).matches()){
                            forgetpasspost(forgetemail.getText().toString());
                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Dismissed..!!",Toast.LENGTH_SHORT).show();

                        }else{
                            Toast.makeText(login.this,"Invalid Email Address",Toast.LENGTH_SHORT).show();
                        }
                       // forgetpasspost(forgetemail.getText().toString());
                      //  dialog.dismiss();
                       // Toast.makeText(getApplicationContext(),"Dismissed..!!",Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show();
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
                getuserdatawithemail(personEmail,fname,lname);
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

    private void postdatawithvolley(String Email, String Password){
        final ProgressDialog progressDialog = new ProgressDialog(login.this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait"); // set message
        progressDialog.show(); // show progress dialog
        String url2=baseurl+"api/user/login";
        try {
            JSONObject object1=new JSONObject();
            object1.put("Email",Email);
            object1.put("Password",Password);
            RequestQueue mqueue4 = Volley.newRequestQueue(getApplicationContext());
            JsonObjectRequest jsonObjRequest4 = new JsonObjectRequest(Request.Method.POST,
                    url2, object1, new com.android.volley.Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("myapp", "the response is" + response.toString());
                    try {
                        progressDialog.dismiss();
                        JSONObject json = new JSONObject(String.valueOf(response));
                        JSONArray result = json.getJSONArray("User");
                        JSONObject userdata=result.getJSONObject(0);
                        String message=json.getString("message");
                        if(message.equals("Login Successfully")){
                            //   Getting Logged in User Data Returned From login API
                            String id=userdata.getString("_id");
                            String email=userdata.getString("Email");
                            String Password=userdata.getString("Password");
                            String InitLogin=userdata.getString("InitLogin");
                            String Role=userdata.getString("Role");
                            String city=userdata.getString("City");
                            String BusinessAddress=userdata.getString("BusinessAddress");
                            String ContactNumber=userdata.getString("ContactNumber");
                            String FirstName=userdata.getString("FirstName");
                            String LastName=userdata.getString("LastName");
                            String PostCode=userdata.getString("PostCode");
                            String SocialMedia=userdata.getString("SocialMedia");
                            String Filling=userdata.getString("Filling");
                            String BusinessName=userdata.getString("BusinessName");
                            String image=userdata.getString("Image");

                            //Preference to Store User Credentials
                            SharedPreferences sharedpreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();

                            editor.putBoolean("Registered", true);
                            editor.putBoolean("islogged", true);
                            editor.putString("Image",image);
                            editor.putString("id", id);
                            editor.putString("email", email);
                            editor.putString("Password", Password);
                            editor.putString("InitLogin", InitLogin);
                            editor.putString("City",city);
                            editor.putString("Role", Role);
                            editor.putString("BusinessAddress", BusinessAddress);
                            editor.putString("ContactNumber", ContactNumber);
                            editor.putString("FirstName", FirstName);
                            editor.putString("LastName", LastName);
                            editor.putString("PostCode", PostCode);
                            editor.putString("SocialMedia", SocialMedia);
                            editor.putString("Filling", Filling);
                            editor.putString("BusinessName", BusinessName);
                            editor.apply();

                            Toast.makeText(getApplicationContext(), "Login Succesfully", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(login.this,maindashboard.class);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Incorrect email or password", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Incorrect email or password", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Incorrect email or password", Toast.LENGTH_SHORT).show();

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

        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
            Toast.makeText(getApplicationContext(), "Incorrect email or password", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
    }

    private void postData(String Email, String Password) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseurl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        LoginResponse modal = new LoginResponse(Email,Password);
        Call<LoginResponse> call = retrofitAPI.createPost(modal);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(login.this, "Login Successfull", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(login.this, "Incorrect Username or Password", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(login.this, "Login Failed"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
                            getuserdatawithemail(femail,ffirstName,flastName);

                            //TODO put your code here
                        } catch (JSONException e) {
                            Toast.makeText(login.this,e.toString(), Toast.LENGTH_SHORT).show();
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
            Toast.makeText(login.this,"Invalid Email Address",Toast.LENGTH_SHORT).show();
            temp=false;
        }
        return temp;
    }

    public void forgetpasspost(String email){
        final ProgressDialog progressDialog = new ProgressDialog(login.this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait"); // set message
        progressDialog.show(); // show progress dialog
        String url2=baseurl+"api/user/resetpassword";
        try {
            JSONObject object1=new JSONObject();
            object1.put("Email",email);
            RequestQueue mqueue6 = Volley.newRequestQueue(getApplicationContext());
            JsonObjectRequest jsonObjRequest6 = new JsonObjectRequest(Request.Method.POST,
                    url2, object1, new com.android.volley.Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("myapp", "the response is" + response.toString());
                    try {
                        JSONObject json = new JSONObject(String.valueOf(response));

                        boolean message=json.getBoolean("success");
                        if(message){
                            Toast.makeText(getApplicationContext(), "New Password has been sent to your email", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "email does not exist or registered", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "email does not exist or registered", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "email does not exist or registered", Toast.LENGTH_SHORT).show();
                }
            }
            ) { @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
            };
            mqueue6.add(jsonObjRequest6);
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
            Toast.makeText(getApplicationContext(), "email does not exist or registered", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
    }

    public void getuserdatawithemail(String email,String fname,String lname){
        final ProgressDialog progressDialog = new ProgressDialog(login.this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait"); // set message
        progressDialog.show(); // show progress dialog
        String url2=baseurl+"api/user/getoneuser";
        try {
            JSONObject object1=new JSONObject();
            object1.put("Email",email);
            RequestQueue mqueue5 = Volley.newRequestQueue(getApplicationContext());
            JsonObjectRequest jsonObjRequest5 = new JsonObjectRequest(Request.Method.POST,
                    url2, object1, new com.android.volley.Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("myapp", "the response is" + response.toString());
                    try {
                        JSONObject json = new JSONObject(String.valueOf(response));
                        JSONArray result = json.getJSONArray("Data");
                        JSONObject userdata=result.getJSONObject(0);
                        String message=json.getString("message");
                        if(message.equals("Users Found")){
                            String id=userdata.getString("_id");
                            String email=userdata.getString("Email");
                            String Password=userdata.getString("Password");
                            String InitLogin=userdata.getString("InitLogin");
                            String Role=userdata.getString("Role");
                            String BusinessAddress=userdata.getString("BusinessAddress");
                            String ContactNumber=userdata.getString("ContactNumber");
                            String FirstName=userdata.getString("FirstName");
                            String LastName=userdata.getString("LastName");
                            String PostCode=userdata.getString("PostCode");
                            String SocialMedia=userdata.getString("SocialMedia");
                            String Filling=userdata.getString("Filling");
                            String BusinessName=userdata.getString("BusinessName");
                            SharedPreferences sharedpreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();

                            editor.putBoolean("Registered", true);
                            editor.putBoolean("islogged", true);
                            editor.putString("id", id);
                            editor.putString("email", email);
                            editor.putString("Password", Password);
                            editor.putString("InitLogin", InitLogin);
                            editor.putString("Role", Role);
                            editor.putString("BusinessAddress", BusinessAddress);
                            editor.putString("ContactNumber", ContactNumber);
                            editor.putString("FirstName", FirstName);
                            editor.putString("LastName", LastName);
                            editor.putString("PostCode", PostCode);
                            editor.putString("SocialMedia", SocialMedia);
                            editor.putString("Filling", Filling);
                            editor.putString("BusinessName", BusinessName);
                            editor.apply();
                            Toast.makeText(getApplicationContext(), "Login Succesfully", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(login.this,maindashboard.class);
                            startActivity(intent);
                            progressDialog.dismiss();
                        } else{
                            Toast.makeText(getApplicationContext(), "Going for registration", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            Intent intents=new Intent(login.this,aftergooglefacebookregisteration.class);
                            intents.putExtra("femail",email);
                            intents.putExtra("ffirstName",fname);
                            intents.putExtra("flastName",lname);
                            startActivity(intents);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Data Exception", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //  progressDialog.dismiss();
                    //  Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), "Going for registration", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    Intent intents=new Intent(login.this,aftergooglefacebookregisteration.class);
                    intents.putExtra("femail",email);
                    intents.putExtra("ffirstName",fname);
                    intents.putExtra("flastName",lname);
                    startActivity(intents);
                }
            }) { @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
            };
            mqueue5.add(jsonObjRequest5);
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
            Toast.makeText(getApplicationContext(), "jsonException", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
    }
    private boolean checkPermission() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            int result = ContextCompat.checkSelfPermission(login.this, READ_EXTERNAL_STORAGE);
            int result1 = ContextCompat.checkSelfPermission(login.this, WRITE_EXTERNAL_STORAGE);
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