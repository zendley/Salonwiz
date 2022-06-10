package com.speckpro.salonwiz.afterlogin.UI;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.speckpro.salonwiz.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class MessengerFragment extends Fragment {

    private EditText senttext;
    private final String whatsappnumber="+923402572010";
    private Button sendtextbtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =inflater.inflate(R.layout.fragment_messenger, container, false);
        TextView textView = rootView.findViewById(R.id.toolbar_titletext);
        textView.setText("Digital Assistant");
        ImageView backarrow= rootView.findViewById(R.id.toolbar_backarrow);
        backarrow.setVisibility(View.GONE);
        senttext=rootView.findViewById(R.id.messenger_input);
        sendtextbtn=rootView.findViewById(R.id.messenger_sendbtn);
        sendtextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSupportChat(senttext.getText().toString());
                Toast.makeText(getContext(), "Clicked", Toast.LENGTH_SHORT).show();
//                openWhatsApp(whatsappnumber,senttext.getText().toString());
            }
        });

        return rootView;
    }


    private void startSupportChat(String text) {

        try {
            String headerReceiver = "";// Replace with your message.
            String bodyMessageFormal = "";// Replace with your message.
            String whatsappContain = headerReceiver + bodyMessageFormal;
            String trimToNumner = "+923402572010"; //10 digit number
            Intent intent = new Intent ( Intent.ACTION_VIEW );
            intent.setData ( Uri.parse ( "https://wa.me/" + trimToNumner + "/?text=" + text ) );
            startActivity ( intent );
        } catch (Exception e) {
            e.printStackTrace ();
        }

    }

    private void openWhatsApp(String contact,String mensaje){
        String url = null;
        try {
            url = "https://api.whatsapp.com/send?phone=" + contact+"&text=" + URLEncoder.encode(mensaje, "UTF-8");
            PackageManager pm = getActivity().getPackageManager();
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            getActivity().startActivity(i);
        } catch (UnsupportedEncodingException unsupportedEncodingException) {
            unsupportedEncodingException.printStackTrace();
        } catch (PackageManager.NameNotFoundException nameNotFoundException) {
            nameNotFoundException.printStackTrace();
        }
    }
}