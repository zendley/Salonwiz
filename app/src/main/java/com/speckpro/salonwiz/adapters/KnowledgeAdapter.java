package com.speckpro.salonwiz.adapters;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.bumptech.glide.Glide;
import com.speckpro.salonwiz.R;
import com.speckpro.salonwiz.newmodels.KnowledgeModel;

import java.util.ArrayList;

public class KnowledgeAdapter extends RecyclerView.Adapter<KnowledgeAdapter.Viewholder>{

    private final Context context;
    private final ArrayList<KnowledgeModel> knowledgeArrayList;
    DatePickerDialog datePickerDialog;
    String dirPath, fileName;

    // Constructor
    public KnowledgeAdapter(Context context, ArrayList<KnowledgeModel> knowledgeArrayList) {
        this.context = context;
        this.knowledgeArrayList = knowledgeArrayList;
    }

    @NonNull
    @Override
    public KnowledgeAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.renderview_faq, parent, false);
        return new KnowledgeAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KnowledgeAdapter.Viewholder holder, int position) {
        // to set data to textview and imageview of each card layout
        KnowledgeModel model = knowledgeArrayList.get(position);
        holder.title.setText(model.getName());
        holder.descipt.setText(model.getCreatedAt());

        String imgUrl = "http://portal.mysalonmanager.co.uk/uploads/"+model.getImage();

        Glide.with(context).load(imgUrl).centerCrop().into(holder.drawable);

        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(context);
                progressDialog.setCancelable(false); // set cancelable to false
                progressDialog.setMessage("Please Wait"); // set message
                progressDialog.show();
                AndroidNetworking.download(model.getImage(), dirPath, fileName)
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
    }

    @Override
    public int getItemCount() {
        if(knowledgeArrayList!=null && knowledgeArrayList.size()>0) {
            return knowledgeArrayList.size();
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