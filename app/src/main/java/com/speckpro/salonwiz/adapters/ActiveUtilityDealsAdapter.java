package com.speckpro.salonwiz.adapters;

import static android.content.Context.MODE_PRIVATE;

import static com.speckpro.salonwiz.retrofit.BaseUrl.baseurl_images;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.request.RequestOptions;
import com.speckpro.salonwiz.R;
import com.speckpro.salonwiz.newmodels.ApiResponse;
import com.speckpro.salonwiz.newmodels.DealsModel;
import com.speckpro.salonwiz.newmodels.UserUtilitiesModel;
import com.speckpro.salonwiz.retrofit.ApiClient;
import com.speckpro.salonwiz.retrofit.ApiService;
import com.speckpro.salonwiz.utils.GlideApp;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActiveUtilityDealsAdapter extends RecyclerView.Adapter<ActiveUtilityDealsAdapter.Viewholder>{

    private final Context context;
    private final ArrayList<UserUtilitiesModel> courseModelArrayList;
    private ApiService apiService;
    private SharedPreferences sharedpreferences;
    private String token;
    private String dealId, utilityId;
    private ArrayList<DealsModel> dealsArrayList;
    boolean isDealAvailable = false;
    private Viewholder viewHold;

    // Constructor
    public ActiveUtilityDealsAdapter(Context context, ArrayList<UserUtilitiesModel> courseModelArrayList) {
        this.context = context;
        this.courseModelArrayList = courseModelArrayList;
        apiService = ApiClient.getClient().create(ApiService.class);
        sharedpreferences = context.getSharedPreferences("MySalonSharedPref", MODE_PRIVATE);
        token = sharedpreferences.getString("token", "");
        dealsArrayList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ActiveUtilityDealsAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.renderview_utilities_cardlayout, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActiveUtilityDealsAdapter.Viewholder holder, int position) {
        // to set data to textview and imageview of each card layout
        viewHold = holder;

        UserUtilitiesModel model = courseModelArrayList.get(position);
        holder.text.setText(model.getUtiltity());
        String imageUrl = baseurl_images+""+model.getImage();
        GlideApp.with(holder.itemView.getContext()).load(imageUrl).apply(RequestOptions.fitCenterTransform()).into(holder.drawable);

        //request deal
        if(model.getIsAccepted() == 0 && model.getDealRequested()==0){ // scenario 1
            holder.fillform.setText("Request Deal");
            holder.fillform.setEnabled(true);
            holder.fillform.setClickable(true);
        } else if(model.getDealRequested()==1 && model.getIsAccepted() == 0) { // scenario 2
            holder.fillform.setText("Deal Requested");
            holder.fillform.setEnabled(false);
            holder.fillform.setClickable(false);
        } else if(getUtilityDeals(model.getId().toString())){// scenario 3
            //check if there is any deal offered for this utility by the admin
            holder.fillform.setText("Accept Deal");
            holder.fillform.setEnabled(true);
            holder.fillform.setClickable(true);
        } else if(model.getIsAccepted()== 1 && model.getDealRequested()==0){ // scenario 4
            holder.fillform.setText("Deal Accepted");
            holder.fillform.setEnabled(false);
            holder.fillform.setClickable(false);
        }

        holder.fillform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(model.getDealRequested()==0 && model.getIsAccepted() == 0) { //request deal via api
                    requestUtilityDeal(model.getId()); //sending utility id as parameter
                } else if(model.getDealRequested()==1 && model.getIsAccepted() == 0){ //deal requested, do nothing

                } else if(model.getIsAccepted()== 1 && model.getDealRequested()==0) { //deal accepted, do nothing

                } else if(isDealAvailable){ //deals available, requested deal are now available,

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return courseModelArrayList.size();
    }

    public String getPath(Uri uri,View view) {
        String path = null;
        String[] projection = {MediaStore.Files.FileColumns.DATA};
        Cursor cursor = view.getContext().getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) {
            path = uri.getPath();
        } else {
            cursor.moveToFirst();
            int column_index = cursor.getColumnIndexOrThrow(projection[0]);
            path = cursor.getString(column_index);
            cursor.close();
        }
        return ((path == null || path.isEmpty()) ? (uri.getPath()) : path);
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private final ImageView drawable;
        private final TextView text;
        private final CardView card;
        private final Button fillform;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            drawable = itemView.findViewById(R.id.util_cardimage);
            text = itemView.findViewById(R.id.util_cardtext);
            card=itemView.findViewById(R.id.utility_cardlayout);
            fillform=itemView.findViewById(R.id.getutilitydetail_fillform);
        }
    }

    private boolean getUtilityDeals(String id){
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setIndeterminate(true);
        dialog.show();
        Call<ArrayList<DealsModel>> call = apiService.getdealsOfUtility("application/json", "Bearer "+token, id);
        call.enqueue(new Callback<ArrayList<DealsModel>>() {
            @Override
            public void onResponse(Call<ArrayList<DealsModel>> call, Response<ArrayList<DealsModel>> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    if(response.body()!=null) {
                        for (int i = 0; i < response.body().size(); i++) {
                            dealsArrayList.add(new DealsModel(
                                    response.body().get(i).getId(),
                                    response.body().get(i).getDeal(),
                                    response.body().get(i).getDesc(),
                                    response.body().get(i).getUtilId(),
                                    response.body().get(i).getIsAccepted(),
                                    response.body().get(i).getCreatedAt(),
                                    response.body().get(i).getUpdatedAt()));
                            // Toast.makeText(getContext(), "" + response.body().get(i).getImage().getSrc(), Toast.LENGTH_SHORT).show();
                        }

                        if (dealsArrayList!=null && dealsArrayList.isEmpty()) {
                            isDealAvailable = true;
                        } else {
                            isDealAvailable = false;
                        }
                        isDealAvailable = false;
                    }
//                    Toast.makeText(context, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    isDealAvailable = false;
                    Toast.makeText(context, "Something went wrong, Couldn't Get Available Deals!" + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<DealsModel>> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(context, "Check your internet connection, Get Available Deals Load Failed!", Toast.LENGTH_SHORT).show();
            }
        });
        return isDealAvailable;
    }

    private void requestUtilityDeal(Integer id){
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setIndeterminate(true);
        dialog.show();
        Call<ApiResponse> call = apiService.requestUserUtility("application/json", "Bearer "+token, id);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    Toast.makeText(context, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    viewHold.fillform.setText("Deal Requested");
                    viewHold.fillform.setEnabled(false);
                    viewHold.fillform.setClickable(false);
                } else {
                    Toast.makeText(context, "Something went wrong, Couldn't Send request!" + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(context, "Check your internet connection, Send Request Failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}