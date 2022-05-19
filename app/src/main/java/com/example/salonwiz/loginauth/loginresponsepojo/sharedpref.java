package com.example.salonwiz.loginauth.loginresponsepojo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.example.salonwiz.R;
import com.example.salonwiz.Splash;
import com.example.salonwiz.afterlogin.maindashboard;

public class sharedpref extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharedpref);
        Boolean Registered;
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Registered = sharedPref.getBoolean("Registered", false);

        if (!Registered)
        {
            startActivity(new Intent(this, Splash.class));
        }else {
            startActivity(new Intent(this, maindashboard.class));
        }
    }
}