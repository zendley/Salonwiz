package com.example.salonwiz.afterlogin.knowledgebase;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
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
import com.example.salonwiz.R;

import java.io.File;
import java.util.ArrayList;

public class article_adapter extends RecyclerView.Adapter<article_adapter.Viewholder>{

    private final Context context;
    private final ArrayList<mode_faq> courseModelArrayList;
    DatePickerDialog datePickerDialog;
    String dirPath, fileName;

    // Constructor
    public article_adapter(Context context, ArrayList<mode_faq> courseModelArrayList) {
        this.context = context;
        this.courseModelArrayList = courseModelArrayList;
    }

    @NonNull
    @Override
    public article_adapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.renderview_faq, parent, false);
        return new article_adapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull article_adapter.Viewholder holder, int position) {
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
                        String filename = fileName;
                        String downloadUrlOfImage =model.getUrl();
                        File direct =
                                new File(Environment
                                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                                        .getAbsolutePath() + "/" + "Images" + "/");


                        if (!direct.exists()) {
                            direct.mkdir();
                            Log.d("asd", "dir created for first time");
                        }

                        DownloadManager dm = (DownloadManager) v.getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                        Uri downloadUri = Uri.parse(downloadUrlOfImage);
                        DownloadManager.Request request = new DownloadManager.Request(downloadUri);
                        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                                .setAllowedOverRoaming(false)
                                .setTitle(filename)
                                .setMimeType("image/jpeg")
                                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                                .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES,
                                        File.separator + "Images" + File.separator + filename);

                        dm.enqueue(request);
                        progressDialog.dismiss();
                    }
                });

        Glide.with(context).load(model.getUrl()).into(holder.drawable);
    }


    @Override
    public int getItemCount() {
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