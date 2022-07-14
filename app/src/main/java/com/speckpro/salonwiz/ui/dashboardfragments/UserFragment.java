package com.speckpro.salonwiz.ui.dashboardfragments;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.speckpro.salonwiz.R;
import com.speckpro.salonwiz.login.Login;
import com.speckpro.salonwiz.retrofit.ApiClient;
import com.speckpro.salonwiz.retrofit.ApiService;
import com.speckpro.salonwiz.ui.knowledgebase.Knowledgebase;
import com.speckpro.salonwiz.ui.user.ContactUs;
import com.speckpro.salonwiz.ui.user.ForgetPassword;
import com.speckpro.salonwiz.ui.user.PersonnalSettings;
import com.speckpro.salonwiz.ui.user.TermsAndConditions;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserFragment extends Fragment {

    private RelativeLayout personaluser, userSocial, passwordUser, contactUs,knowledgeitem,termsandcondition;
    private RelativeLayout logtbn;
    private GoogleSignInClient mGoogleSignInClient;

    private ProgressBar progressBar;
    private ApiService apiService;
    SharedPreferences sharedpreferences;
    String token, fName, lName, buisnessname, fullname;
    GoogleSignInAccount googleAccount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user, container, false);

        apiService = ApiClient.getClient().create(ApiService.class);
        progressBar = rootView.findViewById(R.id.progressBar_user);

        sharedpreferences =  getContext().getSharedPreferences("MySalonSharedPref", MODE_PRIVATE);
        token = sharedpreferences.getString("token", "");
        fName = sharedpreferences.getString("FirstName", "");
        lName = sharedpreferences.getString("LastName", "");
        buisnessname= sharedpreferences.getString("BussinessName","");
        String fullname=fName+" "+lName;

        TextView username = rootView.findViewById(R.id.userloggedin_name);
        TextView buisnessnames = rootView.findViewById(R.id.userloggedin_occupt);
        TextView textView = rootView.findViewById(R.id.toolbar_titletext);
        textView.setVisibility(View.GONE);
        RelativeLayout layout= rootView.findViewById(R.id.toolbar_userinfo);
        layout.setVisibility(View.VISIBLE);
        ImageView backarrow= rootView.findViewById(R.id.toolbar_backarrow);
        backarrow.setVisibility(View.GONE);

        buisnessnames.setVisibility(View.VISIBLE);
        username.setVisibility(View.VISIBLE);
        buisnessnames.setText(buisnessname);
        username.setText(fullname);

        personaluser= rootView.findViewById(R.id.userpersonal_layout);
        personaluser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent s = new Intent(getContext(), PersonnalSettings.class);
                startActivity(s);
            }
        });

      /*  userSocial = rootView.findViewById(R.id.layout_SocialMedia);
        userSocial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent s = new Intent(getContext(), SocialMedia.class);
                startActivity(s);
            }
        });*/

        termsandcondition= rootView.findViewById(R.id.layout_AppInformation);
        termsandcondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent s = new Intent(getContext(), TermsAndConditions.class);
                startActivity(s);
            }
        });

        knowledgeitem= rootView.findViewById(R.id.layout_knowledge);
        knowledgeitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent s = new Intent(getContext(), Knowledgebase.class);
                startActivity(s);
            }
        });
        passwordUser= rootView.findViewById(R.id.layout_Password);
        passwordUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent s = new Intent(getContext(), ForgetPassword.class);
                startActivity(s);
            }
        });

        contactUs = rootView.findViewById(R.id.layout_ContactUs);
        contactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent s = new Intent(getContext(), ContactUs.class);
                startActivity(s);
            }
        });

        logtbn= rootView.findViewById(R.id.userlogout_layout);
        GoogleSignInOptions gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);

        googleAccount = GoogleSignIn.getLastSignedInAccount(getContext());
        if (googleAccount != null) {
            String personName = googleAccount.getDisplayName();
            String personGivenName = googleAccount.getGivenName();
            String personFamilyName = googleAccount.getFamilyName();
            String personEmail = googleAccount.getEmail();
            String personId = googleAccount.getId();
            Uri personPhoto = googleAccount.getPhotoUrl();
        }

        logtbn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
//                SharedPreferences preferences = getContext().getSharedPreferences("MySalonSharedPref", MODE_PRIVATE);
//                SharedPreferences.Editor editor = preferences.edit();
//                editor.clear();
//                editor.commit();
//                startActivity(new Intent(getContext(), Splash.class));
            }
        });
        return rootView;
    }

    private void logout() {
        progressBar.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = apiService.logout("application/json", "Bearer "+token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                Log.d("TAG","Register: "+response);
                if(response.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    //get user details from customer api
                    assert response.body() != null;
                    if(!response.message().equals("Tokens Revoked")) {
                        SharedPreferences preferences = getContext().getSharedPreferences("MySalonSharedPref", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.commit();
                        AccessToken.setCurrentAccessToken(null);
                        if (LoginManager.getInstance() != null) {
                            LoginManager.getInstance().logOut();
                        }
                        if(googleAccount != null){
                            signOut();
                        } else {
                            Intent i = new Intent(getContext(), Login.class);
                            startActivity(i);
                        }
                    } else {
                        Toast.makeText(getContext(), "Error Occured! "+response.body().toString(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
//                    Toast.makeText(Register.this, "Something went wrong!" + response.body().get(0).getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.d("TAG","RegisterFail"+t);
                Toast.makeText(getContext(), "Check your Internet Connection! RegisterFail Due to: "+t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener((Activity) getContext(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getContext(), "Signed out Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(getContext(), Login.class);
                        startActivity(intent);

                    }
                });
    }
}