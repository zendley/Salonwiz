package com.speckpro.salonwiz.ui;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.speckpro.salonwiz.R;
import com.speckpro.salonwiz.newmodels.UserUtilitiesModel;
import com.speckpro.salonwiz.retrofit.ApiClient;
import com.speckpro.salonwiz.retrofit.ApiService;
import com.speckpro.salonwiz.ui.dashboardfragments.HomeFragment;
import com.speckpro.salonwiz.ui.dashboardfragments.MessengerFragment;
import com.speckpro.salonwiz.ui.dashboardfragments.NotificationsFragment;
import com.speckpro.salonwiz.ui.dashboardfragments.ServicesFragment;
import com.speckpro.salonwiz.ui.dashboardfragments.UserFragment;
import com.speckpro.salonwiz.ui.utilitydeals.input.UtilitiesInputSelection;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainDashboard extends AppCompatActivity {

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static BottomNavigationView bottomNavigationView;
    public Fragment selectedFragment;
    private Button continuepopup;
    private SharedPreferences sharedPreferences;
    private boolean s;
    private String tag="home";

    private ArrayList<UserUtilitiesModel> utilitiesArrayList;
    private ProgressBar progressBar;
    private ApiService apiService;
    String id, token;

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if(tag.equals("home") ) {
            appClosePopup();
        } else {
            if(count==0){
                appClosePopup();
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

        apiService = ApiClient.getClient().create(ApiService.class);
        progressBar = findViewById(R.id.progressBar_dashboard);
        sharedPreferences = getSharedPreferences("MySalonSharedPref", Context.MODE_PRIVATE);
        token=sharedPreferences.getString("token", null);

        checkUserUtilities(token);

        verifyStoragePermissions(MainDashboard.this);

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
                        selectedFragment = new NotificationsFragment();
                        tag ="info";
                        break;
                    case R.id.messenger:
                        selectedFragment = new MessengerFragment();
                        tag ="messenger";
                        break;
                    case R.id.support:
                        selectedFragment = new ServicesFragment();
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
            int result = ContextCompat.checkSelfPermission(MainDashboard.this, READ_EXTERNAL_STORAGE);
            int result1 = ContextCompat.checkSelfPermission(MainDashboard.this, WRITE_EXTERNAL_STORAGE);
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

    public void appClosePopup(){
        Dialog dialog = new Dialog(MainDashboard.this);
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
        Dialog dialog = new Dialog(MainDashboard.this);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.popup_fillutilitiescontinue);
        Button dialogButton = dialog.findViewById(R.id.popup_utilitycontinuebtn);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainDashboard.this, UtilitiesInputSelection.class);
                startActivity(intent);
            }
        });
        dialog.show();
    }

    private void checkUserUtilities(String token){
        utilitiesArrayList = new ArrayList();
        Call<ArrayList<UserUtilitiesModel>> call = apiService.getUserAllUtilities("application/json", "Bearer "+token);
        call.enqueue(new Callback<ArrayList<UserUtilitiesModel>>() {
            @Override
            public void onResponse(Call<ArrayList<UserUtilitiesModel>> call, Response<ArrayList<UserUtilitiesModel>> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                        for (int i = 0; i < response.body().size(); i++) {
                            utilitiesArrayList.add(new UserUtilitiesModel(
                                    response.body().get(i).getId(),
                                    response.body().get(i).getUtiltity(),
                                    response.body().get(i).getSupplier(),
                                    response.body().get(i).getExpirationdate(),
                                    response.body().get(i).getBillpaid(),
                                    response.body().get(i).getLoaForm(),
                                    response.body().get(i).getLastBill(),
                                    response.body().get(i).getUserId(),
                                    response.body().get(i).getIsAccepted(),
                                    response.body().get(i).getDealRequested(),
                                    response.body().get(i).getIsFilled(),
                                    response.body().get(i).getCreatedAt(),
                                    response.body().get(i).getUpdatedAt(),
                                    response.body().get(i).getImage()));
                        }
                    if (utilitiesArrayList != null && utilitiesArrayList.isEmpty()) {
                        progressBar.setVisibility(View.GONE);
                        s=true;
                        popup();
//                        Toast.makeText(KnowledgeArticlesActivity.this, "No Articles Found!", Toast.LENGTH_SHORT).show();
                    } else {
                        progressBar.setVisibility(View.GONE);
                        s=false;
                    }
                } else {
                    s=false;
                    progressBar.setVisibility(View.GONE);
                    //Toast.makeText(KnowledgeArticlesActivity.this, "" + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<UserUtilitiesModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                s=false;
//                Log.d("TAG",""+t.toString());
                Toast.makeText(MainDashboard.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
            ActivityCompat.requestPermissions(MainDashboard.this, new String[]{WRITE_EXTERNAL_STORAGE}, 2296);
        }
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    public void changeFragment(Fragment fragment, String tag) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(tag).commit();
    }

    public void fragmentnext(Fragment sd){
        selectedFragment=sd;
    }
}