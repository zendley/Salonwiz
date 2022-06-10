package com.speckpro.salonwiz.afterlogin.knowledgebase;

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

import java.util.ArrayList;

public class tut_adapter extends RecyclerView.Adapter<tut_adapter.Viewholder>{

    private final Context context;
    private final ArrayList<mode_faq> courseModelArrayList;
    DatePickerDialog datePickerDialog;
    String dirPath, fileName;

    // Constructor
    public tut_adapter(Context context, ArrayList<mode_faq> courseModelArrayList) {
        this.context = context;
        this.courseModelArrayList = courseModelArrayList;
    }

    @NonNull
    @Override
    public tut_adapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.renderview_faq, parent, false);
        return new tut_adapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull tut_adapter.Viewholder holder, int position) {
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

        Glide.with(context).load(model.getUrl()).into(holder.drawable);
    }


    @Override
    public int getItemCount() {
        // this method is used for showing number
        // of card items in recycler view.
        return courseModelArrayList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private final ImageView drawable;
        private final ImageView download;
        private final TextView title;
        private final TextView descipt;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            drawable = itemView.findViewById(R.id.faq_image);
            download = itemView.findViewById(R.id.faqget_download);
            title = itemView.findViewById(R.id.faq_title);
            descipt=itemView.findViewById(R.id.faq_description);
        }
    }
}