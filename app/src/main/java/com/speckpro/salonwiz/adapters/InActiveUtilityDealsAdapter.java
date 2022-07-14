package com.speckpro.salonwiz.adapters;

import static com.speckpro.salonwiz.retrofit.BaseUrl.baseurl_images;

import android.app.DatePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.request.RequestOptions;
import com.speckpro.salonwiz.R;
import com.speckpro.salonwiz.newmodels.UserUtilitiesModel;
import com.speckpro.salonwiz.utils.GlideApp;

import java.util.ArrayList;

public class InActiveUtilityDealsAdapter extends RecyclerView.Adapter<InActiveUtilityDealsAdapter.Viewholder>{

    private final Context context;
    private final ArrayList<UserUtilitiesModel> courseModelArrayList;
    DatePickerDialog datePickerDialog;

    // Constructor
    public InActiveUtilityDealsAdapter(Context context, ArrayList<UserUtilitiesModel> courseModelArrayList) {
        this.context = context;
        this.courseModelArrayList = courseModelArrayList;
    }

    @NonNull
    @Override
    public InActiveUtilityDealsAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.renderview_utilities_cardlayout, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InActiveUtilityDealsAdapter.Viewholder holder, int position) {
        // to set data to textview and imageview of each card layout
        UserUtilitiesModel model = courseModelArrayList.get(position);
        holder.text.setText(model.getUtiltity());
        String imageUrl = baseurl_images+""+model.getImage();
        GlideApp.with(holder.itemView.getContext()).load(imageUrl).apply(RequestOptions.fitCenterTransform()).into(holder.drawable);

        if(model.getIsAccepted() == 0 && model.getDealRequested()==0){
            holder.fillform.setText("Request Deal");
        } else if(model.getDealRequested()==1) {
            holder.fillform.setText("Deal Requested");
        } else if(model.getIsAccepted()== 1 && model.getDealRequested()==0){
            holder.fillform.setText("Accept Deal");
        }
        holder.fillform.setEnabled(false);
        holder.fillform.setClickable(false);
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
}