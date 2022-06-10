package com.speckpro.salonwiz.afterlogin.filingcabinet.generalcard;

import static com.speckpro.salonwiz.BaseUrl.baseurl;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.speckpro.salonwiz.R;
import com.speckpro.salonwiz.retrofit.RetrofitAPI;
import com.github.barteksc.pdfviewer.PDFView;


import java.io.File;
import java.util.ArrayList;

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

