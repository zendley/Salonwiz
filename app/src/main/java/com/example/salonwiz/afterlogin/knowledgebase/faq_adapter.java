package com.example.salonwiz.afterlogin.knowledgebase;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
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

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.bumptech.glide.Glide;
import com.example.salonwiz.R;
import com.example.salonwiz.afterlogin.UI.homefrag_utilitydeals.allfrag_adapter;
import com.example.salonwiz.afterlogin.UI.homefrag_utilitydeals.allfrag_model;
import com.example.salonwiz.afterlogin.UI.homefrag_utilitydeals.utilityformfilling;

import java.util.ArrayList;

public class faq_adapter extends RecyclerView.Adapter<faq_adapter.Viewholder>{

    private final Context context;
    private final ArrayList<mode_faq> courseModelArrayList;
    DatePickerDialog datePickerDialog;
    String dirPath, fileName;

    // Constructor
    public faq_adapter(Context context, ArrayList<mode_faq> courseModelArrayList) {
        this.context = context;
        this.courseModelArrayList = courseModelArrayList;
    }

    @NonNull
    @Override
    public faq_adapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.renderview_faq, parent, false);
        return new faq_adapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull faq_adapter.Viewholder holder, int position) {
        // to set data to textview and imageview of each card layout
        mode_faq model = courseModelArrayList.get(position);
        holder.title.setText(model.getTitle());
        holder.descipt.setText(model.getDescription());

        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(context);
                progressDialog.setCancelable(false); // set cancelable to false
                progressDialog.setMessage("Please Wait"); // set message
                progressDialog.show();
                AndroidNetworking.download(model.getUrl(), dirPath, fileName)
                        .build()
                        .startDownload(new DownloadListener() {
                            @Override
                            public void onDownloadComplete() {
                                progressDialog.dismiss();

                                Toast.makeText(context, "DownLoad Complete", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(ANError anError) {
                                Toast.makeText(context, "DownLoad Failed,Image does not exist", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();

                            }
                        });
            }
        });

        Glide.with(context).load(model.getUrl()).centerCrop().into(holder.drawable);
    }

    @Override
    public int getItemCount() {
        if(courseModelArrayList!=null && courseModelArrayList.size()>0) {
            return courseModelArrayList.size();
        }
        return 0;
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private final ImageView drawable;
        private final ImageView download;
        private final TextView title;
        private final TextView descipt;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            download = itemView.findViewById(R.id.faqget_download);
            drawable = itemView.findViewById(R.id.faq_image);
            title = itemView.findViewById(R.id.faq_title);
            descipt=itemView.findViewById(R.id.faq_description);
        }
    }
}