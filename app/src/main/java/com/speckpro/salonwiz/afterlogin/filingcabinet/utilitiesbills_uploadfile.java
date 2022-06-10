package com.speckpro.salonwiz.afterlogin.filingcabinet;

import static android.os.Build.VERSION.SDK_INT;
import static com.speckpro.salonwiz.BaseUrl.baseurl;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.github.barteksc.pdfviewer.PDFView;
import com.speckpro.salonwiz.R;
import com.speckpro.salonwiz.UriUtils;
import com.speckpro.salonwiz.retrofit.RetrofitAPI;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class utilitiesbills_uploadfile extends AppCompatActivity {

    CardView browsedocument;
    ImageView imagetoshow;
    Button savedocument;
    private String path,currentdate,title,type,fileextension,filenamewithoutextension;
    EditText filename;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private static final int PICKFILE_REQUEST_CODE = 300;
    private String filePath;
    private PDFView pdfView;
    private String fileType="Image";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SDK_INT >= 19 && SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_utilitiesbills_uploadfile);

        SimpleDateFormat currentDate = new SimpleDateFormat("MM/dd/yyyy");
        Date todayDate = new Date();
        currentdate = currentDate.format(todayDate);
        //title="GENERAL";
        type="UTILITYBILL";

        imagetoshow=findViewById(R.id.imagetoshowinutilitiesbills);
        browsedocument=findViewById(R.id.utilitiesbills_filecard);
        savedocument=findViewById(R.id.utilitiesbillsuploadbutton);
        browsedocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    try {
                        PackageManager pm = getPackageManager();
                        int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, getPackageName());
                        if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                            final CharSequence[] options = {"Take Photo", "Choose From Gallery","Choose File","Cancel"};
                            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(utilitiesbills_uploadfile.this);
                            builder.setTitle("Select Option");
                            builder.setItems(options, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int item) {
                                    if (options[item].equals("Take Photo")) {
                                        dialog.dismiss();
                                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        startActivityForResult(intent,1);
                                    } else if (options[item].equals("Choose From Gallery")) {
                                        dialog.dismiss();
                                        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                        startActivityForResult(pickPhoto,2);
                                    } else if (options[item].equals("Choose File")) {
                                        dialog.dismiss();
                                        getFileChooserIntent();
                                    }  else if (options[item].equals("Cancel")) {
                                        dialog.dismiss();
                                    }
                                }
                            });
                            builder.show();
                        } else
                            Toast.makeText(utilitiesbills_uploadfile.this, "Camera Permission error", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(utilitiesbills_uploadfile.this, "Camera Permission error", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                } else {
                    requestPermission();
                    if (checkPermission()) {
                        try {
                            PackageManager pm = getPackageManager();
                            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, getPackageName());
                            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                                final CharSequence[] options = {"Take Photo", "Choose From Gallery","Choose File","Cancel"};
                                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(utilitiesbills_uploadfile.this);
                                builder.setTitle("Select Option");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int item) {
                                        if(options[item].equals("Take Photo")) {
                                            dialog.dismiss();
                                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                            startActivityForResult(intent,1);
                                        } else if (options[item].equals("Choose From Gallery")) {
                                            dialog.dismiss();
                                            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                            startActivityForResult(pickPhoto,2);
                                        } else if (options[item].equals("Choose File")) {
                                            dialog.dismiss();
                                            getFileChooserIntent();
                                        } else if (options[item].equals("Cancel")) {
                                            dialog.dismiss();
                                        }
                                    }
                                });
                                builder.show();
                            } else
                                Toast.makeText(utilitiesbills_uploadfile.this, "Camera Permission error", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(utilitiesbills_uploadfile.this, "Camera Permission error", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        filename = findViewById(R.id.heading_utilitiesbillsfile);
        savedocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fileType.equals("Image")) {
                    Log.v("Data For Genearl Post", path + fileextension + filenamewithoutextension + " " + currentdate + " " + title + type);
                    if (!filename.getText().toString().isEmpty()) {
                        postpopupData(path, title, type, currentdate);
                    } else {
                        Toast.makeText(utilitiesbills_uploadfile.this, "Please Enter Filename", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if(!filename.getText().toString().isEmpty()) {
                        postpopupData(filePath, title, type, currentdate);
                    } else {
                        Toast.makeText(utilitiesbills_uploadfile.this, "Please Enter Filename", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        TextView textView =findViewById(R.id.toolbar_titletext);
        textView.setText("Upload File");
        ImageView backarrow=findViewById(R.id.toolbar_backarrow);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void postpopupData(String image1path,String title,String type,String date) {
        final ProgressDialog progressDialog = new ProgressDialog(utilitiesbills_uploadfile.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait");
        progressDialog.show();
        SharedPreferences sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        filename=findViewById(R.id.heading_utilitiesbillsfile);
        String filetitlegive=filename.getText().toString();
        String email = sh.getString("email", "");
        Log.v("Data For Popup Post",filename.getText().toString()+image1path+" "+title+type+date+email);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseurl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        File image1a = new File(image1path);
        RequestBody image =
                RequestBody.create(MediaType.parse("multipart/form-data"), image1a);
        MultipartBody.Part image1=MultipartBody.Part.createFormData("image",image1a.getName(),image);
        RequestBody Title =
                RequestBody.create(MediaType.parse("multipart/form-data"),filetitlegive);
        RequestBody Type =
                RequestBody.create(MediaType.parse("multipart/form-data"), type);
        RequestBody Date =
                RequestBody.create(MediaType.parse("multipart/form-data"), date);
        RequestBody Email=
                RequestBody.create(MediaType.parse("multipart/form-data"), email);

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call<ResponseBody> call = retrofitAPI.generalsubmit(Date,Type,Title,Email,image1);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.i("response",response.toString());
                if(response.isSuccessful()){
                    Log.d("TAG", "onResponse: "+response.body());
                    Log.d("Response Message", "onResponse: "+response.message());
                    progressDialog.dismiss();
                    Toast.makeText(utilitiesbills_uploadfile.this, "Added Successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(utilitiesbills_uploadfile.this, "Failed to Add Data!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(utilitiesbills_uploadfile.this, "Failed"+t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("TAG", "onResponse: "+t);
                progressDialog.dismiss();

            }
        });
    }

    @SuppressLint({"LongLogTag", "Range"})
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode ==1) {
                if (resultCode == RESULT_OK) {
                    //   Bitmap bitmap= BitmapFactory.decodeFile(pathToFile);
                    Bitmap photo = (Bitmap)data.getExtras().get("data");
                    imagetoshow.setImageBitmap(photo);
                    Uri selectedImage = getImageUri(utilitiesbills_uploadfile.this,photo);
                    imagetoshow.setImageURI(selectedImage);
                    String[] filePath = { MediaStore.Images.Media.DATA };
                    Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePath[0]);
                    String picturePath = c.getString(columnIndex);
                    c.close();
                    Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                    Log.w("path of image from gallery", picturePath+"");
                    path=picturePath;
                }
            } else if (requestCode ==2) {
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    imagetoshow.setImageURI(selectedImage);
                    String[] filePath = { MediaStore.Images.Media.DATA };
                    Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePath[0]);
                    String picturePath = c.getString(columnIndex);
                    c.close();
                    Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                    Log.w("path of image from gallery", picturePath+"");
                    path=picturePath;
                }
            } else if(requestCode == 300){
                Uri uri = data.getData();
                String uriString = uri.toString();
                File myFile = new File(uriString);
                String uriPath = uri.getPath();
                String displayName = null;

                if (uriString.startsWith("content://")) {
                    Cursor cursor = null;
                    try {
                        cursor = getApplicationContext().getContentResolver().query(uri, null, null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {
                            displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                        }
                    } finally {
                        cursor.close();
                    }
                } else if (uriString.startsWith("file://")) {
                    displayName = myFile.getName();
                }

                filePath = UriUtils.getPathFromUri(this,uri);
                Toast.makeText(this, "" + filePath, Toast.LENGTH_SHORT).show();
                fileType="File";
            }
        } catch(Exception e) {
            Log.e("FileSelectorActivity", "File select error", e);
        }
    }

    private static boolean fileExists(String filePath) {
        File file = new File(filePath);

        return file.exists();
    }

    private void getFileChooserIntent() {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            startActivityForResult(intent, PICKFILE_REQUEST_CODE);
    }

    private boolean checkPermission() {
        // Permission is not granted
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                PERMISSION_REQUEST_CODE);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "IMG_" + Calendar.getInstance().getTime(), null);
        return Uri.parse(path);
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
}