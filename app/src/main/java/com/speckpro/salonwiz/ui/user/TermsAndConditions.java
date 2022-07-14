package com.speckpro.salonwiz.ui.user;

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

import com.speckpro.salonwiz.R;

public class TermsAndConditions extends AppCompatActivity {
    private TextView desciption;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_termsandconditions);
        desciption=findViewById(R.id.termsandcond_description);
        desciption.setText("Introduction\n" +
                "These Standard Terms And Conditions Written On This Webpage Shall Manage Your Use Of Our Platform, My Salon Manager At Www.Mysalonmanager.Co.Uk These Terms Will Be Applied Fully And Affect To Your Use Of This Platform. By Using This Platform, You Agreed To Accept All Terms And Conditions Written In Here. You Must Not Use This Platform If You Disagree With Any Of These Standard Terms And Conditions. Minors Or People Below 18 Years Old Are Not Allowed To Use This Platform.\n" +
                "\n" +
                "Intellectual Property Rights\n" +
                "Other Than The Content You Own, Under These Terms, My Salon Manager And/Or Its Licensors Own All The Intellectual Property Rights And Materials Contained In This Platform. You Are Granted Limited License Only For Purposes Of Viewing The Material Contained On This Website.\n" +
                "\n" +
                "Restrictions\n" +
                "You Are Specifically Restricted From All Of The Following:\n" +
                "\n" +
                "1: Publishing Any Website Material In Any Other Media;\n" +
                "2: Selling, Sublicensing And/Or Otherwise Commercializing Any Website Material;\n" +
                "3: Publicly Performing And/Or Showing Any Website Material;\n" +
                "4: Using This Platform In Any Way That Is Or May Be Damaging To This Platform;\n" +
                "5: Using This Platform In Any Way That Impacts User Access To This Platform;\n" +
                "6: Using This Platform Contrary To Applicable Laws And Regulations, Or In Any Way May Cause Harm To The Platform, Or To Any Person Or Business Entity;\n" +
                "7: Engaging In Any Data Mining, Data Harvesting, Data Extracting Or Any Other Similar Activity In Relation To This Platform;\n" +
                "8: Using This Platform To Engage In Any Advertising Or Marketing.\n" +
                " \n" +
                "\n" +
                "Certain Areas Of This Website Are Restricted From Being Access By You And My Salon Manager May Further Restrict Access By You To Any Areas Of This Website, At Any Time, In Absolute Discretion. Any User ID And Password You May Have For This Platform Are Confidential And You Must Maintain Confidentiality As Well.\n" +
                "\n" +
                "Your Content\n" +
                "In These Standard Terms And Conditions, “Your Content” Shall Mean Any Images, Documents, Text Or Other Material You Choose To Display On This Platform. By Displaying Your Content, You Grant My Salon Manager A Non-Exclusive, Worldwide Irrevocable, Sub Licensable License To Use, Reproduce, Adapt, Publish, Translate And Distribute It In Any And All Media. Your Content Must Be Your Own And Must Not Be Invading Any Third-Party's Rights. My Salon Manager Reserves The Right To Remove Any Of Your Content From This Platform At Any Time Without Notice.\n" +
                "\n" +
                "No Warranties\n" +
                "This Website Is Provided “As Is,” With All Faults, And My Salon Manager Express No Representations Or Warranties, Of Any Kind Related To This Website Or The Materials Contained On This Website. Also, Nothing Contained On This Website Shall Be Interpreted As Advising You\n" +
                "\n" +
                "Limitation Of Liability\n" +
                "In No Event Shall My Salon Manager, Nor Any Of Its Officers, Directors And Employees, Shall Be Held Liable For Anything Arising Out Of Or In Any Way Connected With Your Use Of This Platform Whether Such Liability Is Under Contract. My Salon Manager, Including Its Officers, Directors And Employees Shall Not Be Held Liable For Any Indirect, Consequential Or Special Liability Arising Out Of Or In Any Way Related To Your Use Of This Platform.\n" +
                "\n" +
                "Indemnification\n" +
                "You Hereby Indemnify To The Fullest Extent My Salon Manager From And Against Any And/Or All Liabilities, Costs, Demands, Causes Of Action, Damages And Expenses Arising In Any Way Related To Your Breach Of Any Of The Provisions Of These Terms.\n" +
                "\n" +
                "Severability\n" +
                "If Any Provision Of These Terms Is Found To Be Invalid Under Any Applicable Law, Such Provisions Shall Be Deleted Without Affecting The Remaining Provisions Herein.\n" +
                "\n" +
                "Variation Of Terms\n" +
                "My Salon Manager Is Permitted To Revise These Terms At Any Time As It Sees Fit, And By Using This Platform You Are Expected To Review These Terms On A Regular Basis.\n" +
                "\n" +
                "Assignment\n" +
                "The Company Is Allowed To Assign, Transfer, And Subcontract Its Rights And/Or Obligations Under These Terms Without Any Notification. However, You Are Not Allowed To Assign, Transfer, Or Subcontract Any Of Your Rights And/Or Obligations Under These Terms.\n" +
                "\n" +
                "Entire Agreement\n" +
                "These Terms Constitute The Entire Agreement Between My Salon Manager And You In Relation To Your Use Of This Platform, And Supersede All Prior Agreements And Understandings.\n" +
                "\n" +
                "Governing Law & Jurisdiction\n" +
                "These Terms Will Be Governed By And Interpreted In Accordance With The Laws Of The State Of Country, And You Submit To The Non-Exclusive Jurisdiction Of The State And Federal Courts Located In Country For The Resolution Of Any Disputes.");
        TextView textView =findViewById(R.id.toolbar_titletext);
        textView.setText("Terms and Conditions");
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        ImageView backarrow=findViewById(R.id.toolbar_backarrow);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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