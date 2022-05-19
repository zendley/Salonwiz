package com.example.salonwiz.afterlogin.filingcabinet.generalcard;

import static android.content.ContentValues.TAG;
import static androidx.core.content.PackageManagerCompat.LOG_TAG;

import static com.example.salonwiz.BaseUrl.baseurl;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.example.salonwiz.afterlogin.filingcabinet.Utilititesinput.cardadapter;
import com.example.salonwiz.afterlogin.filingcabinet.general_main;
import com.example.salonwiz.afterlogin.maindashboard;
import com.example.salonwiz.loginauth.login;
import com.example.salonwiz.retrofit.RetrofitAPI;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.util.Constants;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class gencard_adapter extends RecyclerView.Adapter<gencard_adapter.Viewholder> {

    private final Context context;
    private final ArrayList<gencard_model> courseModelArrayList;
    String dirPath, filePath, fileName;
    DatePickerDialog datePickerDialog;
    private Viewholder viewholder;
    private final String callingFor = "layout";
    private PDFView popupPdfView;

    public gencard_adapter(Context context, ArrayList<gencard_model> courseModelArrayList) {
        this.context = context;
        this.courseModelArrayList = courseModelArrayList;
    }

    @NonNull
    @Override
    public gencard_adapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fileget_cardlayout, parent, false);
        return new Viewholder(view);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull gencard_adapter.Viewholder holder, int position) {
        viewholder = holder;
        gencard_model model = courseModelArrayList.get(position);
        holder.text.setText(model.getText());

        //setting pdf and images
        if(model.getDrawable().endsWith(".pdf") || model.getDrawable().contains(".docx") || model.getDrawable().contains(".doc") ){
            holder.drawable.setImageDrawable(context.getResources().getDrawable(R.drawable.doc));
        }  else {
            holder.drawable.setImageDrawable(context.getResources().getDrawable(R.drawable.jpg));
        }

        holder.options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.cardOptions.setVisibility(View.VISIBLE);
            }
        });

        holder.textViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(view.getContext());
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.popup_delete);
                TextView title = dialog.findViewById(R.id.textView_dialogTitle);
                Button yesButton = dialog.findViewById(R.id.popup_logoutbtnyes);
                Button noButton = dialog.findViewById(R.id.popup_logoutbtnno);
                title.setText(holder.text.getText().toString());
                yesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        deletefile(model.getId(),v);
                        holder.cardOptions.setVisibility(View.GONE);
                    }
                });

                noButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        holder.cardOptions.setVisibility(View.GONE);
                    }
                });

                dialog.show();
            }
        });

        dirPath = Environment.getExternalStorageDirectory() + "/Image";
        filePath = Environment.getExternalStorageDirectory() + "/PdfFiles";

        String fileName = model.getDrawable().substring(model.getDrawable().lastIndexOf('/') + 1);

        holder.textViewDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.cardOptions.setVisibility(View.GONE);
                final ProgressDialog progressDialog = new ProgressDialog(context);
                progressDialog.setCancelable(false); // set cancelable to false
                progressDialog.setMessage("Please Wait"); // set message
                progressDialog.show();
                String filename = fileName;
                if (filename.contains(".jpg") || filename.contains(".png") || filename.contains(".jpeg")) {
                    String downloadUrlOfImage = model.getDrawable();
                    File direct =
                            new File(Environment
                                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                                    .getAbsolutePath() + "/" + "" + "/");

                    if (!direct.exists()) {
                        direct.mkdir();
                        Log.d("asd", "dir created for first time");
                    }

                    DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                    Uri downloadUri = Uri.parse(downloadUrlOfImage);
                    DownloadManager.Request request = new DownloadManager.Request(downloadUri);
                    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                            .setAllowedOverRoaming(false)
                            .setTitle(filename)
                            .setMimeType("image/jpeg")
                            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                            .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES,
                                    File.separator + "" + filename);

                    dm.enqueue(request);
                    progressDialog.dismiss();
                } else {
                    String downloadUrlOfFile = model.getDrawable();
                    File direct =
                            new File(Environment
                                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                                    .getAbsolutePath() + "/" + "" + "/");

                    if (!direct.exists()) {
                        direct.mkdir();
                        Log.d("asd", "dir created for first time");
                    }

                    DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                    Uri downloadUri = Uri.parse(downloadUrlOfFile);
                    DownloadManager.Request request = new DownloadManager.Request(downloadUri);
                    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                            .setAllowedOverRoaming(false)
                            .setTitle(filename)
                            .setMimeType("image/pdf")
                            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOCUMENTS,
                                    File.separator + "" + filename);

                    dm.enqueue(request);
                    progressDialog.dismiss();
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.cardOptions.setVisibility(View.GONE);
            }
        });


    }
    private void deletefile(String id,View view) {
        final ProgressDialog progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait");
        progressDialog.show();
        SharedPreferences sh = view.getContext().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        String email = sh.getString("email", "");

        progressDialog.dismiss();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseurl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestBody ID =
                RequestBody.create(MediaType.parse("multipart/form-data"), id);
        RequestBody Email=
                RequestBody.create(MediaType.parse("multipart/form-data"), email);

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call<ResponseBody> call = retrofitAPI.deletefile(Email,ID);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.i("response",response.toString());
                if(response.isSuccessful()){
                    //   Toast.makeText(general_uploadfile.this, "Data added to API", Toast.LENGTH_SHORT).show();
                    //Log.d("TAG", "onResponse: "+response.body());
                    Log.d("Response Message", "onResponse: "+response.message());
                    progressDialog.dismiss();
                    Toast.makeText(view.getContext(), "Deleted Successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(view.getContext(), "Failed to Delete Data!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(view.getContext(), "Failed"+t.getMessage(), Toast.LENGTH_SHORT).show();
                //Log.d("TAG", "onResponse: "+t);
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public int getItemCount() {
        return courseModelArrayList.size();
    }

    static class Viewholder extends RecyclerView.ViewHolder {
        private final ImageView drawable;
        private final ImageView download;
        private final ImageView options;
        private final TextView text;
        private final TextView textViewDownload;
        private final TextView textViewDelete;
        private final CardView card;
        private final CardView cardOptions;
        private final RelativeLayout cardLayout;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            drawable = itemView.findViewById(R.id.fcutil_cardimage);
            download = itemView.findViewById(R.id.fileget_download);
            options = itemView.findViewById(R.id.imageView_imageOptions);
            text = itemView.findViewById(R.id.fcutil_cardtext);
            textViewDownload = itemView.findViewById(R.id.textView_textDownload);
            textViewDelete = itemView.findViewById(R.id.textView_textDelete);
            card=itemView.findViewById(R.id.fcutility_cardlayout);
            cardOptions=itemView.findViewById(R.id.cardView_optionsMore);
            cardLayout=itemView.findViewById(R.id.layout_cardFilingLayout);
        }
    }
}

