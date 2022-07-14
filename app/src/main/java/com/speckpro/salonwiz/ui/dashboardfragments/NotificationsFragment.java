package com.speckpro.salonwiz.ui.dashboardfragments;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.speckpro.salonwiz.R;
import com.speckpro.salonwiz.adapters.NotificationsAdapter;
import com.speckpro.salonwiz.newmodels.NotificationsModel;
import com.speckpro.salonwiz.retrofit.ApiClient;
import com.speckpro.salonwiz.retrofit.ApiService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NotificationsFragment extends Fragment {

    private RecyclerView recyclerViewNotifications;
    private NotificationsAdapter mAdapter;
    private ArrayList<NotificationsModel> notificationsArrayList;

    private ProgressBar progressBar;
    private ApiService apiService;
    private TextView textViewNoData;
    SharedPreferences sharedpreferences;
    String token;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_notification, container, false);
        ImageView backarrow= rootView.findViewById(R.id.toolbar_backarrow);
        backarrow.setVisibility(View.GONE);
        TextView textView = rootView.findViewById(R.id.toolbar_titletext);
        textView.setText("Notifications");

        recyclerViewNotifications = rootView.findViewById(R.id.recyclerView_notifications);
        apiService = ApiClient.getClient().create(ApiService.class);
        progressBar = rootView.findViewById(R.id.progressBar_notificatins);
        textViewNoData = rootView.findViewById(R.id.textView_noNotification);

        sharedpreferences =  getContext().getSharedPreferences("MySalonSharedPref", MODE_PRIVATE);
        token = sharedpreferences.getString("token", "");

        getNotifications();

        return rootView;
    }

    private void getNotifications(){
        progressBar.setVisibility(View.VISIBLE);
        textViewNoData.setVisibility(View.GONE);
        recyclerViewNotifications.setVisibility(View.GONE);
        notificationsArrayList = new ArrayList();
        Call<ArrayList<NotificationsModel>> call = apiService.getNotifications("application/json", "Bearer "+token);
        call.enqueue(new Callback<ArrayList<NotificationsModel>>() {

            @Override
            public void onResponse(Call<ArrayList<NotificationsModel>> call, Response<ArrayList<NotificationsModel>> response) {
                if(response.isSuccessful()){
                    assert response.body() != null;
                    if(!response.body().isEmpty()) {
                        for (int i = 0; i < response.body().size(); i++) {
                            notificationsArrayList.add(new NotificationsModel(
                                    response.body().get(i).getId(),
                                    response.body().get(i).getDescription(),
                                    response.body().get(i).getIsOf(),
                                    response.body().get(i).getIsFor(),
                                    response.body().get(i).getIsRead(),
                                    response.body().get(i).getCreatedAt(),
                                    response.body().get(i).getUpdatedAt()));
                            // Toast.makeText(getContext(), "" + response.body().get(i).getImage().getSrc(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    if (notificationsArrayList!=null && notificationsArrayList.isEmpty()) {
                        progressBar.setVisibility(View.GONE);
                        textViewNoData.setVisibility(View.VISIBLE);
                        recyclerViewNotifications.setVisibility(View.GONE);
                        textViewNoData.setText("No Notifications Found!");
//                        Toast.makeText(KnowledgeArticlesActivity.this, "No Articles Found!", Toast.LENGTH_SHORT).show();
                    } else {
                        progressBar.setVisibility(View.GONE);
                        textViewNoData.setVisibility(View.GONE);
                        recyclerViewNotifications.setVisibility(View.VISIBLE);

                        mAdapter = new NotificationsAdapter(getContext(), notificationsArrayList);
                        recyclerViewNotifications.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerViewNotifications.setAdapter(mAdapter);
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    textViewNoData.setVisibility(View.VISIBLE);
                    recyclerViewNotifications.setVisibility(View.GONE);
                    textViewNoData.setText("Something went Wrong, Couldn't load Notifications!");

                    //Toast.makeText(KnowledgeArticlesActivity.this, "" + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<NotificationsModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                recyclerViewNotifications.setVisibility(View.GONE);
                textViewNoData.setVisibility(View.VISIBLE);
                textViewNoData.setText("Check your internet connection, Load Notifications Failed!");
                Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
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