package com.speckpro.salonwiz.ui.dashboardfragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.speckpro.salonwiz.R;
import com.speckpro.salonwiz.retrofit.ApiClient;
import com.speckpro.salonwiz.retrofit.ApiService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MessengerFragment extends Fragment {

    private EditText senttext;
    private final String whatsappnumber="+923402572010";
    private Button sendtextbtn;

    private ProgressBar progressBar;
    private ApiService apiService;
    SharedPreferences sharedpreferences;
    String token, typedMessage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =inflater.inflate(R.layout.fragment_messenger, container, false);
        TextView textView = rootView.findViewById(R.id.toolbar_titletext);
        textView.setText("Digital Assistant");
        ImageView backarrow= rootView.findViewById(R.id.toolbar_backarrow);
        backarrow.setVisibility(View.GONE);

        apiService = ApiClient.getClient().create(ApiService.class);
        progressBar = rootView.findViewById(R.id.progressBar_support);

        sharedpreferences =  getContext().getSharedPreferences("MySalonSharedPref", MODE_PRIVATE);
        token = sharedpreferences.getString("token", "");

        senttext = rootView.findViewById(R.id.messenger_input);
        sendtextbtn = rootView.findViewById(R.id.messenger_sendbtn);
        sendtextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(senttext.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Type a message to send!", Toast.LENGTH_SHORT).show();
                } else {
                    typedMessage = senttext.getText().toString();
                    addSupport();
                }
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

    private void addSupport(){
        progressBar.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = apiService.getAssistance("application/json", "Bearer "+token);
        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    assert response.body() != null;
//                    if(response.body().toString().contains("Added Successfully")) {
                        Toast.makeText(getContext(), "Support Request Sent!", Toast.LENGTH_SHORT).show();
                        startSupportChat(typedMessage);
//                    } else {
//                        progressBar.setVisibility(View.GONE);
//                        Toast.makeText(getContext(), "Failed to send assistance request!", Toast.LENGTH_SHORT).show();
//                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Something went Wrong, Couldn't send assistance request!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }
}