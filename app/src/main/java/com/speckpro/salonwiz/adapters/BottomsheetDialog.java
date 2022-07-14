package com.speckpro.salonwiz.adapters;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.speckpro.salonwiz.R;
import com.speckpro.salonwiz.newmodels.ApiResponse;
import com.speckpro.salonwiz.retrofit.ApiClient;
import com.speckpro.salonwiz.retrofit.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BottomsheetDialog extends BottomSheetDialogFragment {

    private ApiService apiService;
    SharedPreferences sharedpreferences;
    String token;

    LinearLayout layoutRead, layoutCancel;
    String id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.notification_bottom_sheet,container,false);

        layoutRead = v.findViewById(R.id.layout_markNotificationRead);
        layoutCancel = v.findViewById(R.id.layout_cancelSheet);

        Bundle mArgs = getArguments();
        id = mArgs != null ? mArgs.getString("id") : null;

        apiService = ApiClient.getClient().create(ApiService.class);
        sharedpreferences =  getContext().getSharedPreferences("MySalonSharedPref", MODE_PRIVATE);
        token = sharedpreferences.getString("token", "");

        //Toast.makeText(getContext(), ""+id, Toast.LENGTH_SHORT).show();

        BottomSheetBehavior bottomSheetBehavior = new BottomSheetBehavior<>();
        layoutRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                markReadNotification(id);
                if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }
            }
        });

        return v;
    }

    private void markReadNotification(String id){
        Call<ApiResponse> call = apiService.markReadNotification("application/json", "Bearer "+token, Integer.parseInt(id));
        call.enqueue(new Callback<ApiResponse>() {

            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if(response.isSuccessful()){
                    assert response.body() != null;
                    if(response.body().getMessage().isEmpty()) {
                        Toast.makeText(getContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "" + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
