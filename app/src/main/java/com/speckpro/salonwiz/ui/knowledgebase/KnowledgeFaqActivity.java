package com.speckpro.salonwiz.ui.knowledgebase;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.speckpro.salonwiz.R;
import com.speckpro.salonwiz.adapters.FaqAdapter;
import com.speckpro.salonwiz.models.FaqModel;

import java.util.ArrayList;

public class KnowledgeFaqActivity extends AppCompatActivity {
    private RecyclerView recyclerViewFaq;
    public static final int REQUEST_CODE = 101;
    private FaqAdapter faqAdapter;
    private ArrayList<FaqModel> faqModelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(KnowledgeFaqActivity.this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            KnowledgeFaqActivity.this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(KnowledgeFaqActivity.this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            KnowledgeFaqActivity.this.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_knowledge_faq);

        TextView textView =findViewById(R.id.toolbar_titletext);
        textView.setText("FAQs");
        ImageView backarrow=findViewById(R.id.toolbar_backarrow);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        faqModelArrayList = new ArrayList<>();
        faqModelArrayList.add(new FaqModel("How Secure is Salon Wizz?", "With its bank-level 256-bit encrypted EV SSL connection and encrypted triple backed-up storage, Salon Wizz is highly secure.\n" +
                "Safety and privacy of our customers’ data is everything to us. With a single lapse in security procedures we would lose the trust of our customers – our most important asset. Our clients include financial organizations, law offices and medical institutions among others that need our complete attention on security measures. \n" +
                "All connections on the public and internal networks are done over SSL encryption. We are monitoring login attempts to our system, while all the developers work with absolutely no access to client data. Salon Wizz is an official Amazon AWS Select Technology partner and our solution is built on top of Amazon Web Services infrastructure. \n" +
                "For file storage we use their S3 service which has 99.999999999% durability of objects over a given year. Every object is stored in three physically separate Availability Zones. In conjunction with daily snapshot and transaction log the data is effectively backed up in real time and can be restored to any point in time.\n"));

        faqModelArrayList.add(new FaqModel("Does Salon Wizz have a Desktop App?", "Yes, we have a web app where users can create an account, upload documents and add their utility details."));
        faqModelArrayList.add(new FaqModel("What will happen to posts that I made on my classic Page?", "All of your previous posts will be preserved on your Page, and insights will still be available for them."));
        faqModelArrayList.add(new FaqModel("Is there any content that won't migrate with my account?", "Some of your stories may not migrate.\n" +
                "Information on your classic Page, such as contact info, may not transition. Your bio may need to be shortened. You can edit this information by going into your About section and editing the relevant fields.\n" +
                "Your messages, ads and other features from classic Pages will migrate, but may be in different places.\n"));
        faqModelArrayList.add(new FaqModel("What features do I still have access to in new Pages?", "You will still have access to the following features: Feed, Instagram account linking, advertising, monetization, followers, insights, tools (such as Inbox, Stories, Groups, events), blue badge, and Page management by multiple people. See more available features.\n" +
                "Scheduling posts is not available yet on new Pages, but can be accessed through other tools. You can schedule posts to your new Page from Meta Business Suite and Creator Studio.\n"));
        faqModelArrayList.add(new FaqModel("Do you have any discounts for non-profits organizations?", "Yes! Please contact our sales team for more information. To provide the discount, a sales rep will need a copy of your 501(c)3."));

        recyclerViewFaq =findViewById(R.id.faq_idRVCourse);
        faqAdapter = new FaqAdapter(KnowledgeFaqActivity.this, faqModelArrayList);
        recyclerViewFaq.setLayoutManager(new LinearLayoutManager(KnowledgeFaqActivity.this));
        recyclerViewFaq.setAdapter(faqAdapter);

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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