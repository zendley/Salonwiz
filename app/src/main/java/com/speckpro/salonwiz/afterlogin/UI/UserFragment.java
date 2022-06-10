package com.speckpro.salonwiz.afterlogin.UI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.speckpro.salonwiz.afterlogin.user.ContactUs;
import com.speckpro.salonwiz.afterlogin.user.ForgetPassword;
import com.speckpro.salonwiz.afterlogin.user.personalsettings_user;
import com.speckpro.salonwiz.afterlogin.user.termsandconditions;
import com.speckpro.salonwiz.loginauth.login;


public class UserFragment extends Fragment {

    private RelativeLayout personaluser, userSocial, passwordUser, contactUs,knowledgeitem,termsandcondition;
    private RelativeLayout logtbn;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user, container, false);
        SharedPreferences sh =  this.getActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        String fName = sh.getString("FirstName", "");
        String lName = sh.getString("LastName", "");
        String buisnessname= sh.getString("BusinessName","");
        String fullname=fName+" "+lName;

        personaluser= rootView.findViewById(R.id.userpersonal_layout);
        personaluser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent s = new Intent(getContext(), personalsettings_user.class);
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
                Intent s = new Intent(getContext(), termsandconditions.class);
                startActivity(s);
            }
        });

        knowledgeitem= rootView.findViewById(R.id.layout_knowledge);
        knowledgeitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent s = new Intent(getContext(), knowledgemain.class);
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

        TextView username = rootView.findViewById(R.id.userloggedin_name);
        TextView buisnessnames = rootView.findViewById(R.id.userloggedin_occupt);
        buisnessnames.setText(buisnessname);
        username.setText(fullname);
        TextView textView = rootView.findViewById(R.id.toolbar_titletext);
        textView.setVisibility(View.GONE);
        RelativeLayout layout= rootView.findViewById(R.id.toolbar_userinfo);
        layout.setVisibility(View.VISIBLE);
        ImageView backarrow= rootView.findViewById(R.id.toolbar_backarrow);
        backarrow.setVisibility(View.GONE);

        logtbn= rootView.findViewById(R.id.userlogout_layout);
        GoogleSignInOptions gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getContext());
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
        }
        logtbn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.userlogout_layout:
                        SharedPreferences preferences = getContext().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.commit();
                        AccessToken.setCurrentAccessToken(null);
                        if (LoginManager.getInstance() != null) {
                            LoginManager.getInstance().logOut();
                        }
                        Intent i = new Intent(getContext(),login.class);
                        signOut();
                        startActivity(i);
                        break;
                }
            }
        });
        return rootView;
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener((Activity) getContext(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getContext(), "Signed out Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(getContext(), login.class);
                        startActivity(intent);

                    }
                });
    }
}