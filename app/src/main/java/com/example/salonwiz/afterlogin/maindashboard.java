package com.example.salonwiz.afterlogin;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

import static com.example.salonwiz.BaseUrl.baseurl;
import static com.facebook.FacebookSdk.getApplicationContext;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.salonwiz.R;
import com.example.salonwiz.Splash;
import com.example.salonwiz.afterlogin.UI.HomeFragment;
import com.example.salonwiz.afterlogin.UI.InfoFragment;
import com.example.salonwiz.afterlogin.UI.MessengerFragment;
import com.example.salonwiz.afterlogin.UI.SupportFragment;
import com.example.salonwiz.afterlogin.UI.UserFragment;
import com.example.salonwiz.afterlogin.UI.homefrag_utilitydeals.activeadapter;
import com.example.salonwiz.afterlogin.UI.notification;
import com.example.salonwiz.afterlogin.filingcabinet.Utilititesinput.card_model;
import com.example.salonwiz.afterlogin.filingcabinet.Utilititesinput.utilities_inputselection;
import com.example.salonwiz.afterlogin.filingcabinet.Utilititesinput.utilities_uploadselecteddata;
import com.example.salonwiz.loginauth.login;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class maindashboard extends AppCompatActivity {

    public static BottomNavigationView bottomNavigationView;
    public Fragment selectedFragment;
    private Button continuepopup;
    private SharedPreferences sharedPreferences;
    private boolean s;
    private String tag="home";

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if(tag.equals("home") ) {
                logoutPopup();
        } else {
            if(count==0){
                logoutPopup();
            } else {
                getSupportFragmentManager().popBackStack();
            }
            tag = "home";
            bottomNavigationView.setSelectedItemId(R.id.home);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maindashboard);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
        }
        sharedPreferences = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);

        String email = sharedPreferences.getString("email", null);
        String user=sharedPreferences.getString("id", null);
        System.out.println(email);
        checkpopup(user);

        bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setItemIconTintList(null);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectedFragment = null;
                switch (item.getItemId())
                {
                    case R.id.home:
                       // Toast.makeText(maindashboard.this,"Home",Toast.LENGTH_SHORT).show();
                        selectedFragment = new HomeFragment();
                        tag ="home";
                        break;
                    case R.id.information:
                        selectedFragment = new notification();
                        tag ="info";
                        break;
                    case R.id.messenger:
                        selectedFragment = new MessengerFragment();
                        tag ="messenger";
                        break;
                    case R.id.support:
                        selectedFragment = new SupportFragment();
                        tag ="support";
                        break;
                    case R.id.user:
                        selectedFragment = new UserFragment();
                        tag ="user";
                        break;
                }
                changeFragment(selectedFragment, tag);
                return true;
            }
        });
    }

    private boolean checkPermission() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            int result = ContextCompat.checkSelfPermission(maindashboard.this, READ_EXTERNAL_STORAGE);
            int result1 = ContextCompat.checkSelfPermission(maindashboard.this, WRITE_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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

    public void logoutPopup(){

        Dialog dialog = new Dialog(maindashboard.this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.popup_logout);

        Button yesButton = dialog.findViewById(R.id.popup_logoutbtnyes);
        Button noButton = dialog.findViewById(R.id.popup_logoutbtnno);

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finishAffinity();
                finish();
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void popup(){
        Dialog dialog = new Dialog(maindashboard.this);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.popup_fillutilitiescontinue);
        Button dialogButton = dialog.findViewById(R.id.popup_utilitycontinuebtn);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(maindashboard.this, utilities_inputselection.class);
                startActivity(intent);
            }
        });
        dialog.show();
    }

    public boolean checkpopup(String User){
        String url2=baseurl+"api/Userutilities/getUtilitiesOfOneUser";
        try {
            JSONObject object1=new JSONObject();
            object1.put("User",User);
            RequestQueue mqueue4 = Volley.newRequestQueue(getApplicationContext());
            JsonObjectRequest jsonObjRequest4 = new JsonObjectRequest(Request.Method.POST,
                    url2, object1, new com.android.volley.Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("USERID", "the response is" + User);
                    Log.d("myapp", "the response is" + response.toString());
                    try {

                        JSONObject json = new JSONObject(String.valueOf(response));
                        JSONArray result = json.getJSONArray("userUtilities");
                        String message=json.getString("message");
                        if(message.equals("Utility Found for this User")){
                            Log.d("MEssage", message);
                           if(result.length()>0){
                               Log.d("USERUTIL", "True");
                               s=true;
                           }else{
                               Log.d("USERUTIL", "False");
                               popup();
                               s=false;
                           }
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Incorrect email or password", Toast.LENGTH_SHORT).show();
                            s=false;
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Incorrect email or password", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
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
            Toast.makeText(getApplicationContext(), "Check Your Internet Connection!", Toast.LENGTH_SHORT).show();
        }
        return s;
    }

    private void requestPermission() {
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
            ActivityCompat.requestPermissions(maindashboard.this, new String[]{WRITE_EXTERNAL_STORAGE}, 2296);
        }
    }

    public void changeFragment(Fragment fragment, String tag) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(tag).commit();
    }

    public void fragmentnext(Fragment sd){

        selectedFragment=sd;
    }
}