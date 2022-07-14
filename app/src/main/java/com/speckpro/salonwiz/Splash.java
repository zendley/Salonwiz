package com.speckpro.salonwiz;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;
import static com.speckpro.salonwiz.retrofit.BaseUrl.baseurl;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.speckpro.salonwiz.login.Login;
import com.speckpro.salonwiz.ui.MainDashboard;
import com.speckpro.salonwiz.register.AfterRegisterGoogleFacebook;
import com.speckpro.salonwiz.register.Register;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Splash extends AppCompatActivity {

    private View footerView;
    private View mainView;
    private boolean isUp;
    private final int anim_duration = 1100;
    private Button loginbtn;
    private Button registerbtn;
    private Button signInButton;
    private Button facebooklogin;
    CallbackManager callbackManager;
    private GoogleSignInClient mGoogleSignInClient;
    private String femail,ffirstName,flastName,fprofileURL;

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sh = getSharedPreferences("MySalonSharedPref", Context.MODE_PRIVATE);
        boolean loggedstatus = sh.getBoolean("islogged", false);
        if(loggedstatus){
            Intent intent=new Intent(getApplicationContext(), MainDashboard.class);
            startActivity(intent);
        }
    }

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

        setContentView(R.layout.activity_splash);
        // checkPermission();
        // requestPermission();

        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        Log.i("ResponseObject", loginResult.toString());
                        setFacebookData(loginResult);
                        Log.i("Response", femail+ffirstName+flastName);
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Log.e("MYAPP", "exception", exception);
                    }
                });

        facebooklogin=findViewById(R.id.facebookbtn_splash);
        facebooklogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().setLoginBehavior(LoginBehavior.WEB_ONLY);
                LoginManager.getInstance().logInWithReadPermissions(Splash.this, Arrays.asList("public_profile","email"));
            }
        });

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
                        //  signIn();
                        resultLauncher.launch(new Intent(mGoogleSignInClient.getSignInIntent()));
                        break;
                    // ...
                }
            }
        });

        footerView = findViewById(R.id.footerview);
        mainView = findViewById(R.id.mainview);
        loginbtn=findViewById(R.id.signinbtn);
        registerbtn=findViewById(R.id.registerbtn);
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Splash.this, Login.class);
                startActivity(i);
            }
        });

        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Splash.this, Register.class);
                startActivity(i);
            }
        });

        // initialize as invisible (could also do in xml)
        footerView.setVisibility(View.INVISIBLE);
        // isUp = false;
        // slideUp(mainView , footerView);
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                slideUp(mainView , footerView);
            }
        }, 300);
    }
    public void slideUp(View mainView , View footer_view){
        footer_view.setVisibility(View.VISIBLE);
        TranslateAnimation animate_footer = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                footer_view.getHeight(),  // fromYDelta
                0);                // toYDelta
        animate_footer.setDuration(anim_duration);
        animate_footer.setFillAfter(true);
        footer_view.startAnimation(animate_footer);

        mainView.setVisibility(View.VISIBLE);
        TranslateAnimation animate_main = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,  // fromYDelta
                (0-footer_view.getHeight()));                // toYDelta
        animate_main.setDuration(anim_duration);
        animate_main.setFillAfter(true);
        mainView.startAnimation(animate_main);
    }
    public void slideDown(View mainView , View footer_view){
        TranslateAnimation animate_footer = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
                footer_view.getHeight()); // toYDelta
        animate_footer.setDuration(anim_duration);
        animate_footer.setFillAfter(true);
        footer_view.startAnimation(animate_footer);


        TranslateAnimation animate_main = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                (0-footer_view.getHeight()),  // fromYDelta
                0);                // toYDelta
        animate_main.setDuration(anim_duration);
        animate_main.setFillAfter(true);
        mainView.startAnimation(animate_main);
    }

    ActivityResultLauncher<Intent> resultLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode()==Activity.RESULT_OK){
                Intent intent=result.getData();

                // The Task returned from this call is always completed, no need to attach
                // a listener.
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(intent);
                handleSignInResult(task);


            }
        }
    });

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2296) {
            if (SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    // perform action when allow permission success
                } else {
                    Toast.makeText(this, "Allow permission for storage access!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            //   Toast.makeText(this, "Sign in Successfull", Toast.LENGTH_SHORT).show();
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
                Log.w("googledata",  fname+" "+lname+personEmail);
                Uri personPhoto = acct.getPhotoUrl();
                femail=personEmail;
                ffirstName=fname;
                flastName=lname;
                // Log.i("Response", femail+ffirstName+flastName);
                Log.i("Response", femail+ffirstName+flastName);
                getuserdatawithemail(femail,ffirstName,flastName);
            }

        } catch (ApiException e) {
            Log.w("Error", "signInResult:failed code=" + e.getStatusCode());
        }
    }
    private void setFacebookData(final LoginResult loginResult) {
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {

                        // Application code
                        try {
                            Log.i("ResponseObject", object.toString());
                            femail = object.getString("email");
                            ffirstName = object.getString("first_name");
                            flastName = object.getString("last_name");
                            Log.i("Response", femail+ffirstName+flastName);
                            getuserdatawithemail(femail,ffirstName,flastName);

                            //TODO put your code here
                        } catch (JSONException e) {
                            Toast.makeText(Splash.this,e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,email,first_name,last_name");
        request.setParameters(parameters);
        request.executeAsync();

    }



    public void getuserdatawithemail(String email,String fname,String lname){
        final ProgressDialog progressDialog = new ProgressDialog(Splash.this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait"); // set message
        progressDialog.show(); // show progress dialog
        String url2=baseurl+"api/user/getoneuser";
        try {
            JSONObject object1=new JSONObject();
            object1.put("Email",email);
            RequestQueue mqueue9 = Volley.newRequestQueue(getApplicationContext());
            JsonObjectRequest jsonObjRequest9 = new JsonObjectRequest(Request.Method.POST,
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
                            //   Getting Logged in User Data Returned From login API
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

                            //Preference to Store User Credentials
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
                            //   Toast.makeText(getApplicationContext(), "Login Succesful", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(Splash.this, MainDashboard.class);
                            startActivity(intent);
                            progressDialog.dismiss();

                        }
                        else{
                            // Toast.makeText(getApplicationContext(), "Data Not Found", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(Splash.this, AfterRegisterGoogleFacebook.class);
                            intent.putExtra("femail",email);
                            intent.putExtra("ffirstName",fname);
                            intent.putExtra("flastName",lname);
                            startActivity(intent);
                            progressDialog.dismiss();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Data Not Found", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //  progressDialog.dismiss();
                    //   Toast.makeText(getApplicationContext(), "Data Not Found", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), "Going for registration", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    Intent intents=new Intent(Splash.this, AfterRegisterGoogleFacebook.class);
                    intents.putExtra("femail",email);
                    intents.putExtra("ffirstName",fname);
                    intents.putExtra("flastName",lname);
                    startActivity(intents);
                }
            }
            ) { @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
            };
            mqueue9.add(jsonObjRequest9);

        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
            Toast.makeText(getApplicationContext(), "Data Not Found", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
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

    @Override
    public void onBackPressed() {

    }


   /* public void onSlideViewButtonClick(View view) {
        if (isUp) {
            slideDown(mainView , footerView);
            myButton.setText("Slide up");
        } else {
            slideUp(mainView , footerView);
            myButton.setText("Slide down");
        }
        isUp = !isUp;
    }*/

    private boolean checkPermission() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            int result = ContextCompat.checkSelfPermission(Splash.this, READ_EXTERNAL_STORAGE);
            int result1 = ContextCompat.checkSelfPermission(Splash.this, WRITE_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestPermission() {
        if(!checkPermission()){
            if (SDK_INT >= Build.VERSION_CODES.R) {
                try {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.setData(Uri.parse(String.format("package:%s",getApplicationContext().getPackageName())));
                    startActivityForResult(intent, 2296);
                } catch (Exception e) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                    startActivityForResult(intent, 2296);
                }
            } else {
                //below android 11
                ActivityCompat.requestPermissions(Splash.this, new String[]{WRITE_EXTERNAL_STORAGE}, 2296);
            }
        }
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(Splash.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {


                    }
                });
    }

}