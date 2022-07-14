package com.speckpro.salonwiz.adapters;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
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
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.barteksc.pdfviewer.PDFView;
import com.speckpro.salonwiz.R;
import com.speckpro.salonwiz.newmodels.ApiResponse;
import com.speckpro.salonwiz.newmodels.FilingModel;
import com.speckpro.salonwiz.retrofit.ApiClient;
import com.speckpro.salonwiz.retrofit.ApiService;

import java.io.File;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UtilitiesAdapter extends RecyclerView.Adapter<UtilitiesAdapter.Viewholder> {

    private final Context context;
    private final ArrayList<FilingModel> filingArrayList;
    String dirPath, filePath;
    private ApiService apiService;
    SharedPreferences sharedpreferences;
    String token;
    int positionitem;

    public UtilitiesAdapter(Context context, ArrayList<FilingModel> filingArrayList) {
        this.context = context;
        this.filingArrayList = filingArrayList;
        apiService = ApiClient.getClient().create(ApiService.class);
        sharedpreferences = context.getSharedPreferences("MySalonSharedPref", MODE_PRIVATE);
        token = sharedpreferences.getString("token", "");
    }

    @NonNull
    @Override
    public UtilitiesAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fileget_cardlayout, parent, false);
        return new Viewholder(view);
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "SetJavaScriptEnabled"})
    @Override
    public void onBindViewHolder(@NonNull UtilitiesAdapter.Viewholder holder, int position) {
        FilingModel model = filingArrayList.get(position);
        holder.text.setText(model.getTitle());

        if(!model.getImage().isEmpty()) {
            //setting pdf and images
            if (model.getImage().endsWith(".pdf") || model.getImage().contains(".docx") || model.getImage().contains(".doc")  || model.getImage().contains(".docs")) {
                holder.drawable.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_documents));
            } else {
                String imageUrl = "http://portal.mysalonmanager.co.uk/uploads/"+model.getImage();
                Glide.with(context).load(imageUrl)
                        .fitCenter()
                        .into(holder.drawable);
            }
        }

        holder.options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.cardOptions.setVisibility(View.VISIBLE);
            }
        });

        holder.drawable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.cardOptions.setVisibility(View.GONE);
            }
        });

        holder.textViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                positionitem = holder.getAdapterPosition();
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
                        deletefile(String.valueOf(model.getId()));
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

        String fileName = model.getImage().substring(model.getImage().lastIndexOf('/') + 1);

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
                    String downloadUrlOfImage = "http://portal.mysalonmanager.co.uk/uploads/"+model.getImage();
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
                    String downloadUrlOfFile = "http://portal.mysalonmanager.co.uk/uploads/"+model.getImage();
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

    private void deletefile(String id) {
        Call<ApiResponse> call = apiService.deleteFiling("application/json", "Bearer "+token, id);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                Log.d("TAG","Register: "+response);
                if(response.isSuccessful()) {
                    assert response.body() != null;
                    ApiResponse apiResponse = response.body();
                    if(apiResponse.getMessage().equals("Deleted Successfully")) {
                        Toast.makeText(context, "Utility Deleted Successfully!", Toast.LENGTH_SHORT).show();

                        filingArrayList.remove(positionitem);
                        notifyItemRemoved(positionitem);
                        notifyItemRangeChanged(positionitem, filingArrayList.size());
                    } else {
                        Toast.makeText(context, "Error Occured! "+response.body().toString(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Something went Wrong, Couldn't delete Utility!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.d("TAG","RegisterFail"+t);
                Toast.makeText(context, "Check your Internet Connection! Delete Utility Failed!"+t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return filingArrayList.size();
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
       //private final RelativeLayout cardLayout;
        private final PDFView pdfViewFile;
        private final WebView webViewFile;

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
           //cardLayout=itemView.findViewById(R.id.layout_cardFilingLayout);
            pdfViewFile = itemView.findViewById(R.id.pdfView_cardFileGet);
            webViewFile = itemView.findViewById(R.id.webView_webFileGet);
        }
    }

    public class AppWebViewClients extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            view.loadUrl(url);
            return true;
        }
        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
        }
    }


}

