package com.speckpro.salonwiz.ui.knowledgebase;

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

public class Knowledgebase extends AppCompatActivity {

    private CardView cardFaq, cardArticles, cardTutorials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(Knowledgebase.this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            Knowledgebase.this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(Knowledgebase.this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            Knowledgebase.this.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_knowledgebasemain);
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
                Intent intent=new Intent(Knowledgebase.this, KnowledgeFaqActivity.class);
                startActivity(intent);
            }
        });

        cardArticles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Knowledgebase.this, KnowledgeArticlesActivity.class);
                startActivity(intent);
            }
        });

        cardTutorials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Knowledgebase.this, KnowledgeTutorialsActivity.class);
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