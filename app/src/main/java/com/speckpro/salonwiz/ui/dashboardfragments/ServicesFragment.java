package com.speckpro.salonwiz.ui.dashboardfragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.speckpro.salonwiz.R;
import com.speckpro.salonwiz.ui.MainDashboard;
import com.speckpro.salonwiz.ui.services.ServicesActivity;


public class ServicesFragment extends Fragment {

    private final MainDashboard obj=new MainDashboard();
    private Button marketbtn;

    private CardView cardViewMarketing, cardViewAccount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =inflater.inflate(R.layout.fragment_services, container, false);
        TextView textView = rootView.findViewById(R.id.toolbar_titletext);
        textView.setText("Management");
        ImageView backarrow= rootView.findViewById(R.id.toolbar_backarrow);
        backarrow.setVisibility(View.GONE);

            cardViewMarketing = rootView.findViewById(R.id.cardViewMarketing);
            cardViewMarketing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getContext(), ServicesActivity.class);
                    startActivity(i);
                }
            });

        cardViewAccount = rootView.findViewById(R.id.cardViewAccounting);
        cardViewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Coming Soon!", Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }
}