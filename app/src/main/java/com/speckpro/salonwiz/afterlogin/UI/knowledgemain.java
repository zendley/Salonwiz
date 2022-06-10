package com.speckpro.salonwiz.afterlogin.UI;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.speckpro.salonwiz.R;
import com.speckpro.salonwiz.afterlogin.knowledgebase.knowledgebase_article;
import com.speckpro.salonwiz.afterlogin.knowledgebase.knowledgebase_faq;
import com.speckpro.salonwiz.afterlogin.knowledgebase.knowledgebase_tutorial;

public class knowledgemain extends AppCompatActivity {

    private CardView cardFaq, cardArticles, cardTutorials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(knowledgemain.this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            knowledgemain.this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(knowledgemain.this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            knowledgemain.this.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_knowledgemain);
        String text="Knowledge Base";
        ImageView backarrow=findViewById(R.id.toolbar_backarrow);
        backarrow.setVisibility(View.GONE);
        TextView textView =findViewById(R.id.toolbar_titletext);
        textView.setText(text);

        cardFaq = findViewById(R.id.cardViewFaq);
        cardArticles = findViewById(R.id.cardViewArticles);
        cardTutorials = findViewById(R.id.cardViewTutorials);

        cardFaq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(knowledgemain.this, knowledgebase_faq.class);
                startActivity(intent);
            }
        });

        cardArticles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(knowledgemain.this, knowledgebase_article.class);
                startActivity(intent);
            }
        });

        cardTutorials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(knowledgemain.this, knowledgebase_tutorial.class);
                startActivity(intent);
            }
        });
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