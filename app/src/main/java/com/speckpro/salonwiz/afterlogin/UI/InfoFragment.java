package com.speckpro.salonwiz.afterlogin.UI;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.speckpro.salonwiz.R;
import com.speckpro.salonwiz.afterlogin.knowledgebase.knowledgebase_article;
import com.speckpro.salonwiz.afterlogin.knowledgebase.knowledgebase_faq;
import com.speckpro.salonwiz.afterlogin.knowledgebase.knowledgebase_tutorial;


public class InfoFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        String text="Knowledge Base";
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
        View rootView = inflater.inflate(R.layout.fragment_info, container, false);
        Button faq= rootView.findViewById(R.id.knowledge_faq);
        Button art= rootView.findViewById(R.id.knowledge_article);
        Button tut= rootView.findViewById(R.id.knowledge_tutorials);

        tut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(rootView.getContext(), knowledgebase_tutorial.class);
                startActivity(intent);
            }
        });

        art.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(rootView.getContext(), knowledgebase_article.class);
                startActivity(intent);
            }
        });

        faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(rootView.getContext(), knowledgebase_faq.class);
                startActivity(intent);
            }
        });
        ImageView backarrow= rootView.findViewById(R.id.toolbar_backarrow);
        backarrow.setVisibility(View.GONE);
        TextView textView = rootView.findViewById(R.id.toolbar_titletext);
        textView.setText(text);
        return rootView;

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