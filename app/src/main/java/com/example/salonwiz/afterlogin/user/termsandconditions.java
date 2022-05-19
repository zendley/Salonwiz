package com.example.salonwiz.afterlogin.user;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.salonwiz.R;

public class termsandconditions extends AppCompatActivity {
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
        desciption.setText("Our services consist of all the Salon Wizz services we provide now or in the future, including the Salon Wizz website, web application, mobile application and backend automation tool (“Salon Wizz” and the “Service”). Certain features of the Service may be subject to additional guidelines, terms, or rules, which will be posted on the Service in connection with such features. All such additional terms, guidelines, and rules are incorporated by reference into these Terms.\n" +
                "You may receive an invoice solely for Salon Wizz. However, if Salon Wizz is included with your Xero plan and is connected to your Xero organization, then the Service will be included in your invoice for that Xero plan. If you receive a Salon Wizz or Xero invoice, you are an “Invoiced User”. If you access or use Salon Wizz, but do not receive a Salon Wizz or Xero invoice, you are an “Additional User” of the applicable Invoiced User’s Salon Wizz account. When we say “Company”, “we”, “our” or “us”, we are talking about the entity you contract with for the Service, which is an affiliate of Xero Limited (Salon Wizz Inc.’s parent company). This entity is set out on the applicable Salon Wizz or Xero invoice.\n" +
                "If you are an Invoiced User, for each of your Salon Wizz accounts, you are responsible for notifying Additional Users of the entity you have entered into these Terms with. You must do so before they access or use the applicable Salon Wizz account. If you are an Additional User, for each Salon Wizz account, you must confirm the identity of this entity with the applicable Invoiced User before accessing or using the Salon Wizz account.\n" +
                "THESE TERMS OF USE (THESE “TERMS”) SET FORTH THE LEGALLY BINDING TERMS AND CONDITIONS THAT GOVERN YOUR USE OF THE SERVICE. BY ACCESSING OR USING THE SERVICE, YOU ARE ACCEPTING THESE TERMS (ON BEHALF OF YOURSELF OR THE ENTITY THAT YOU REPRESENT), AND YOU REPRESENT AND WARRANT THAT YOU HAVE THE RIGHT, AUTHORITY, AND CAPACITY TO ENTER INTO THESE TERMS (ON BEHALF OF YOURSELF OR THE ENTITY THAT YOU REPRESENT). YOU MAY NOT ACCESS OR USE THE SERVICE OR ACCEPT THE TERMS IF YOU ARE NOT AT LEAST THE AGE OF MAJORITY IN THE JURISDICTION IN WHICH YOU RESIDE AND WORK. IF YOU DO NOT AGREE WITH ALL OF THE PROVISIONS OF THESE TERMS, DO NOT ACCESS AND/OR USE THE SERVICE.\n" +
                "THIS SERVICE IS FOR BUSINESS USE ONLY AND MAY NOT BE USED FOR PERSONAL, HOUSEHOLD OR CONSUMER PURPOSES. THESE TERMS REQUIRE THE USE OF ARBITRATION (SECTION 12.4) ON AN INDIVIDUAL BASIS TO RESOLVE DISPUTES, RATHER THAN JURY TRIALS OR CLASS ACTIONS, AND ALSO LIMIT THE REMEDIES AVAILABLE TO YOU IN THE EVENT OF A DISPUTE.\n\n" +
                "1.\tThe Service\n" +
                "1.1 Service Description\n" +
                "Salon Wizz is a document automation tool that collects key business documents (by automatically collecting documents from third party websites and by accepting documents that are manually uploaded by users), organizes and securely stores the documents in the cloud, extracts data from the documents, and publishes the documents and associated data to third party accounting and cloud storage platforms.\n" +
                "\n\n" +
                "2.\tAccounts\n" +
                "\n" +
                "2.1\tAccount Creation\n" +
                "In order to use certain features of the Service, you must register for an account (“Account”) and provide certain information about yourself as prompted by the account registration form. You represent and warrant that: (a) all required registration information you submit is truthful and accurate; (b) you will maintain the accuracy of such information. You may delete your Account at any time, for any reason, by following the instructions on the Service. Company may suspend or terminate your Account in accordance with Section 11.\n" +
                "\n2.2 Account Responsibilities\n" +
                "You are responsible for maintaining the confidentiality of your Account login information and are fully responsible for all activities that occur under your Account. You agree to immediately notify us of any unauthorized use, or suspected unauthorized use of your Account or any other breach of security. Company cannot and will not be liable for any loss or damage arising from your failure to comply with the above requirements.\n\n" +
                "3.\tAccess to the Service\n" +
                "3.1 License\n" +
                "Subject to these Terms, Company grants you a non-transferable, non-exclusive, revocable, limited license to use and access the Service.\n" +
                "\n3.2 Certain Restrictions\n" +
                "The rights granted to you in these Terms are subject to the following restrictions: (a) you shall not license, sell, rent, lease, transfer, assign, distribute, host, or otherwise commercially exploit the Service, whether in whole or in part, or any content displayed on the Service; (b) you shall not modify, make derivative works of, disassemble, reverse compile or reverse engineer any part of the Service; (c) you shall not access the Service in order to build a similar or competitive website, product, or service; and (d) except as expressly stated herein, no part of the Service may be copied, reproduced, distributed, republished, downloaded, displayed, posted or transmitted in any form or by any means. Unless otherwise indicated, any future release, update, or other addition to functionality of the Service shall be subject to these Terms. All copyright and other proprietary notices on the Service (or on any content displayed on the Service) must be retained on all copies thereof.\n" +
                "\n3.3 Modification\n" +
                "Company reserves the right, at any time, to modify, suspend, or discontinue the Service (in whole or in part) with or without notice to you. You agree that Company will not be liable to you or to any third party for any modification, suspension, or discontinuation of the Service or any part thereof.\n" +
                "\n3.4 Ownership of Rights by Company\n" +
                "Excluding any Customer Data (defined below), you acknowledge that all the intellectual property rights, including copyrights, patents, trademarks, and trade secrets, in the Service and its content are owned by or licensed to Company or Company’s suppliers. Neither these Terms (nor your access to the Service) transfers to you or any third party any rights, title or interest in or to such intellectual property rights, except for the limited rights expressly set forth in Section 3.1. Company and its suppliers reserve all rights not expressly granted in these Terms. There are no implied licenses granted under these Terms.\n" +
                "\n3.5 Your Ownership of Customer Data\n" +
                "As between you and the Company, you retain all ownership and intellectual property rights in and to the Customer Data (defined below), and without limiting your obligations in Section 5.2, you grant to Company a non-exclusive, worldwide, royalty-free, irrevocable, fully paid-up right to use, process and transmit Customer Data to provide the Service. You agree that Company may collect and analyze data and other information relating to the provision, use and performance of the Service (including information concerning Customer Data and data derived therefrom), and during and after the Term of these Terms, Company may: (i) use such data and information to improve and enhance the Services for other development, diagnostic and corrective purposes in connection with the Service, or other Company offerings; and (ii) disclose such data solely in aggregated or other de-identified form in connection with its business.\n\n" +
                "4.\tCustomer Data\n" +
                "\n4.1 Customer Data\n" +
                "“Customer Data” means any and all information (including login or other account credentials), statements and other content, records, or files that you submit to, or use with, the Service (e.g., content in the user’s profile or postings) and any information statements and other content, records, or files collected on your behalf through the Service (including information belonging to your clients or other third parties on whose behalf you act). As between you and the Company, you are solely responsible for the Customer Data and, except as expressly set out in Company’s Privacy Policy, you assume all risks associated with use of the Customer Data, including any reliance on its accuracy, completeness or usefulness by others, or any disclosure of the Customer Data that personally identifies you or any third party. You hereby represent and warrant that the Customer Data does not violate our Acceptable Use Policy (defined in Section 5.2). You may not represent or imply to others that your Customer Data is in any way provided, sponsored or endorsed by Company. Because you alone are responsible for the Customer Data, you may expose yourself to liability if, for example, the Customer Data violates the Acceptable Use Policy. Company may, but is not obligated to, backup any Customer Data, and the Customer Data may be deleted at any time without prior notice. You are solely responsible for creating and maintaining your own backup copies of your Customer Data if you desire.\n\n" +
                "\n4.2 Acceptable Use Policy\n" +
                "The following terms constitute our \"Acceptable Use Policy\":\n" +
                "You represent, warrant and agree that you have obtained, and will maintain at all times in the course of your use of the Service, all right and authority (including from any customers or other third parties) as required for the transfer of Customer Data to Company, and as necessary to permit Company to lawfully access and use such Customer Data for the purposes of rendering the Services, including any third party authorizations required for the access and use by Company of your (or your clients’ and customers’) credentials to access accounts maintained at financial or other institutions.\n" +
                "Without limiting Section 5.2(a), you agree not to use the Service to collect, upload, transmit, display, or distribute any Customer Data (i) that violates any third-party right, including any copyright, trademark, patent, trade secret, moral right, privacy right, right of publicity, or any other intellectual property or proprietary right; (ii) that is unlawful, harassing, abusive, tortious, threatening, harmful, invasive of another’s privacy, vulgar, defamatory, false, intentionally misleading, trade libelous, pornographic, obscene, patently offensive, promotes racism, bigotry, hatred, or physical harm of any kind against any group or individual or is otherwise objectionable; (iii) that is harmful to minors in any way; or (iv) that is in violation of any law, regulation, or obligations or restrictions imposed by any third party.\n" +
                "In addition, you agree not to: (i) upload, transmit, or distribute to or through the Service any computer viruses, worms, or any software intended to damage or alter a computer system or data; (ii) send through the Service unsolicited or unauthorized advertising, promotional materials, junk mail, spam, chain letters, pyramid schemes, or any other form of duplicative or unsolicited messages, whether commercial or otherwise; (iii) use the Service to harvest, collect, gather or assemble information or data regarding other users, including e-mail addresses, without their consent; (iv) interfere with, disrupt, or create an undue burden on servers or networks connected to the Service, or violate the regulations, policies or procedures of such networks; (v) attempt to gain unauthorized access to the Service (or to other computer systems or networks connected to or used together with the Service), whether through password mining or any other means; (vi) harass or interfere with any other user’s use and enjoyment of the Service; or (vi) use software or automated agents or scripts to produce multiple accounts on the Service, or to generate automated searches, requests, or queries to (or to strip, scrape, or mine data from) the Service (provided, however, that we conditionally grant to the operators of public search engines revocable permission to use spiders to copy materials from the Service for the sole purpose of and solely to the extent necessary for creating publicly available searchable indices of the materials, but not caches or archives of such materials, subject to the parameters set forth in our robots.txt file).\n" +
                "\n4.3 Data Extraction\n" +
                "Company offers a service that extracts some data from documents in your Account (“Data Extraction”). Data Extraction may be performed by software and/or human teams. While we strive to consistently extract accurate and timely data, we may sometimes encounter technical or other difficulties. Company is not responsible for the accuracy, timeliness, deletion, processing, conversion, storage or delivery of this data or any reliance on this data. Company also does not review this extracted data for accuracy or legality.\n" +
                "\n4.4 Enforcement\n" +
                "We reserve the right (but have no obligation) to review any Customer Data, and to investigate and/or take appropriate action against you in our sole discretion if you violate the Acceptable Use Policy or any other provision of these Terms or otherwise create liability for us or any other person. Such action may include removing or modifying your Customer Data, terminating your Account in accordance with Section 11, and/or reporting you to law enforcement authorities.\n" +
                "\n4.5 Feedback\n" +
                "If you provide Company with any feedback or suggestions regarding the Service (“Feedback”), you hereby assign to Company all rights in such Feedback and agree that Company shall have the right to use and fully exploit such Feedback and related information in any manner it deems appropriate. Company will treat any Feedback you provide to Company as non-confidential and non-proprietary. You agree that you will not submit to Company any information or ideas that you consider to be confidential or proprietary.\n");
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