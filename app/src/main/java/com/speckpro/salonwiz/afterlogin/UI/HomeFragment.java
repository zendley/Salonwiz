package com.speckpro.salonwiz.afterlogin.UI;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.speckpro.salonwiz.R;
import com.speckpro.salonwiz.afterlogin.UI.homefrag_utilitydeals.utilitydeals;
import com.speckpro.salonwiz.afterlogin.filingcabinet.filincabinet_main;
import com.speckpro.salonwiz.afterlogin.maindashboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment {

    private RelativeLayout filin,services_lin,utilitylinear,messengerlinear;
    private TextView name,buisnessnames;
   // public static ViewPager viewPager;
   // SlideViewPagerAdapter adapter;

    ViewPager viewPager;
    TabLayout indicator;
    List<Integer> imagesList;
    List<String> textList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag((Activity) getContext(), WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag((Activity) getContext(), WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        SharedPreferences sh =  this.getActivity().getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String fName = sh.getString("FirstName", "");
        String lName = sh.getString("LastName", "");
        String buisnessname= sh.getString("BusinessName","");
        String fullname=fName+" "+lName;

        filin = rootView.findViewById(R.id.layout_cardFilingLayout);
        services_lin = rootView.findViewById(R.id.layout_CardServicesLayout);
        utilitylinear = rootView.findViewById(R.id.layout_cardUtilityDealsLayout);
        messengerlinear = rootView.findViewById(R.id.layout_CardDigitalAssistantLayout);

        name = rootView.findViewById(R.id.loggedin_name);
        buisnessnames = rootView.findViewById(R.id.loggedin_occupt);
        name.setText(fullname);
        buisnessnames.setText(buisnessname);

        viewPager = rootView.findViewById(R.id.viewPager);
        indicator = rootView.findViewById(R.id.indicator);

        imagesList = new ArrayList<>();
        imagesList.add(R.drawable.ic_ban1);
        imagesList.add(R.drawable.ic_ban2);
        imagesList.add(R.drawable.ic_ban3);
        imagesList.add(R.drawable.ic_get_deal);

        textList = new ArrayList<>();
        textList.add("Take Control Of All Your Utility Bills\nOn The GO!");
        textList.add("Offer instant customer service easily\nwith Digital Assistant");
        textList.add("Benefit from our bespoke marketing\nand accountancy services");
        textList.add("Get the best utility deals and enjoy\nmassive discounts on the go!");

        viewPager.setAdapter(new SliderAdapter(getContext(), imagesList, textList));
        indicator.setupWithViewPager(viewPager, true);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(), 3000, 4000);

//        viewPager=(ViewPager) rootView.findViewById(R.id.home_slider);
//        adapter=new SlideViewPagerAdapter(rootView.getContext());
//        viewPager.setAdapter(adapter);
        if (isOpenAlread())
        {
         //   Intent intent=new Intent(rootView.getContext(),maindashboard.class);
         //   intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
         //   startActivity(intent);
        }
        else
        {
            SharedPreferences.Editor editor=rootView.getContext().getSharedPreferences("slide",MODE_PRIVATE).edit();
            editor.putBoolean("slide",true);
            editor.commit();
        }

        utilitylinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), utilitydeals.class);
                startActivity(i);
            }
        });

        messengerlinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maindashboard.bottomNavigationView.setSelectedItemId(R.id.messenger);
            }
        });

        services_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maindashboard.bottomNavigationView.setSelectedItemId(R.id.support);
            }
        });

        filin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), filincabinet_main.class);
                startActivity(i);
            }
        });

        return rootView;
    }
    private boolean isOpenAlread() {

        SharedPreferences sharedPreferences=getContext().getSharedPreferences("slide",MODE_PRIVATE);
        boolean result=sharedPreferences.getBoolean("slide",false);
        return result;

    }

    private class SliderTimer extends TimerTask {

        @Override
        public void run() {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (viewPager.getCurrentItem() < imagesList.size() - 1) {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    } else {
                        viewPager.setCurrentItem(0);
                    }
                }
            });
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