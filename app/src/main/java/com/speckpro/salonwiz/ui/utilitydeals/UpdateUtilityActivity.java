package com.speckpro.salonwiz.ui.utilitydeals;

import static com.speckpro.salonwiz.retrofit.BaseUrl.baseurl_images;
import static com.speckpro.salonwiz.retrofit.BaseUrl.baseurl_loa;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
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
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.request.RequestOptions;
import com.github.barteksc.pdfviewer.PDFView;
import com.speckpro.salonwiz.R;
import com.speckpro.salonwiz.newmodels.ApiResponse;
import com.speckpro.salonwiz.newmodels.UserUtilitiesModel;
import com.speckpro.salonwiz.newmodels.UtilitiesModel;
import com.speckpro.salonwiz.retrofit.ApiClient;
import com.speckpro.salonwiz.retrofit.ApiService;
import com.speckpro.salonwiz.utils.GlideApp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateUtilityActivity extends AppCompatActivity {
    DatePickerDialog datePickerDialog;
    private String isPaid;
    private String Patha,Pathb;
    private ImageView latestbill,loaform;
    private Button latestbills,loaforms;
    public static List<String> s;
    private Spinner spinner;
    private final List<String> list = new ArrayList<String>();
    String userutilityid,suppliername,latestbillurl,loaformurl,getexpdate,ispaidget,titleget;
    private static final int PICKFILE_REQUEST_CODE = 300;
    private static final int PICKLOA_REQUEST_CODE = 400;
    private String loaFilePath = "", billFilePath = "";
    private PDFView pdfView;

    private TextView txtTitle, txtExpDate, txtLatestBillPath;
    private RadioButton paidYes, paidNo;
    private Button btnUpdate;
    private ProgressBar progressBar;
    private ApiService apiService;
    SharedPreferences sharedpreferences;
    String id, email, token, utilityTitle, utilitySupplier, utilityExpireDate;
    List<String> supplierslist;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_update_utility);

        TextView textView =findViewById(R.id.toolbar_titletext);
        textView.setText("Update Form");
        textView.setTextColor(Color.BLACK);
        ImageView backarrow=findViewById(R.id.toolbar_backarrow);
        backarrow.setColorFilter(ContextCompat.getColor(UpdateUtilityActivity.this, R.color.black), android.graphics.PorterDuff.Mode.SRC_IN);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        apiService = ApiClient.getClient().create(ApiService.class);
        progressBar = findViewById(R.id.progressBar_updateUtilityForm);

        sharedpreferences = getSharedPreferences("MySalonSharedPref", MODE_PRIVATE);
        token = sharedpreferences.getString("token", "");
        id = sharedpreferences.getString("id", "");
        email=sharedpreferences.getString("email","");
        Patha="";

        utilityTitle = getIntent().getExtras().getString("title");
        spinner = findViewById(R.id.upopup_utilsupplierlist);
        txtTitle = findViewById(R.id.upopup_titleutil);
        txtExpDate = findViewById(R.id.upopup_utilcontractexpdate);
        txtLatestBillPath = findViewById(R.id.upopup_latestbillimagetextpath);
        paidYes = findViewById(R.id.upopup_latestbillradio1);
        paidNo = findViewById(R.id.upopup_latestbillradio2);
        latestbill=findViewById(R.id.upopup_latestbillimage);
        loaform=findViewById(R.id.upopup_loaformimage);
        latestbills=findViewById(R.id.upopup_latestbilluploadbtn);
        loaforms=findViewById(R.id.upopup_loaformuploadbtn);
        btnUpdate = findViewById(R.id.upopup_utilitysubmit);

        utilityTitle = getIntent().getExtras().getString("title");
        txtTitle.setText(utilityTitle);

        getSuppliersListForUtility();

        loaforms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    try {
                        PackageManager pm = getPackageManager();
                        int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, getPackageName());
                        if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                            final CharSequence[] options = {"Take Photo", "Choose From Gallery","Choose File","Cancel"};
                            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(UpdateUtilityActivity.this);
                            builder.setTitle("Select Option");
                            builder.setItems(options, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int item) {
                                    if (options[item].equals("Take Photo")) {
                                        dialog.dismiss();
                                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        startActivityForResult(intent,2);
                                    } else if (options[item].equals("Choose From Gallery")) {
                                        dialog.dismiss();
                                        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                        startActivityForResult(pickPhoto,3);
                                    } else if (options[item].equals("Choose File")) {
                                        dialog.dismiss();
                                        getFileChooserIntent(PICKLOA_REQUEST_CODE);
                                    } else if (options[item].equals("Cancel")) {
                                        dialog.dismiss();
                                    }
                                }
                            });
                            builder.show();
                        } else
                            Toast.makeText(UpdateUtilityActivity.this, "Camera Permission error", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(UpdateUtilityActivity.this, "Camera Permission error", Toast.LENGTH_SHORT).show();
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
                                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(UpdateUtilityActivity.this);
                                builder.setTitle("Select Option");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int item) {
                                        if (options[item].equals("Take Photo")) {
                                            dialog.dismiss();
                                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                            startActivityForResult(intent,2);
                                        } else if (options[item].equals("Choose From Gallery")) {
                                            dialog.dismiss();
                                            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                            startActivityForResult(pickPhoto,3);
                                        } else if (options[item].equals("Choose File")) {
                                            dialog.dismiss();
                                            getFileChooserIntent(PICKLOA_REQUEST_CODE);
                                        } else if (options[item].equals("Cancel")) {
                                            dialog.dismiss();
                                        }
                                    }
                                });
                                builder.show();
                            } else
                                Toast.makeText(UpdateUtilityActivity.this, "Camera Permission error", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(UpdateUtilityActivity.this, "Camera Permission error", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        latestbills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    try {
                        PackageManager pm = getPackageManager();
                        int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, getPackageName());
                        if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                            final CharSequence[] options = {"Take Photo", "Choose From Gallery","Choose File","Cancel"};
                            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(UpdateUtilityActivity.this);
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
                                        startActivityForResult(pickPhoto,4);
                                    } else if (options[item].equals("Choose File")) {
                                        dialog.dismiss();
                                        getFileChooserIntent(PICKFILE_REQUEST_CODE);
                                    } else if (options[item].equals("Cancel")) {
                                        dialog.dismiss();
                                    }
                                }
                            });
                            builder.show();
                        } else
                            Toast.makeText(UpdateUtilityActivity.this, "Camera Permission error", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(UpdateUtilityActivity.this, "Camera Permission error", Toast.LENGTH_SHORT).show();
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
                                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(UpdateUtilityActivity.this);
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
                                            startActivityForResult(pickPhoto,4);
                                        } else if (options[item].equals("Choose File")) {
                                            dialog.dismiss();
                                            getFileChooserIntent(PICKFILE_REQUEST_CODE);
                                        } else if (options[item].equals("Cancel")) {
                                            dialog.dismiss();
                                        }
                                    }
                                });
                                builder.show();
                            } else
                                Toast.makeText(UpdateUtilityActivity.this, "Camera Permission error", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(UpdateUtilityActivity.this, "Camera Permission error", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        txtExpDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(UpdateUtilityActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(year, monthOfYear, dayOfMonth);

                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                txtExpDate.setText(format.format(calendar.getTime()));
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPaid="";
                if(paidYes.isChecked()){
                    isPaid="1";
                }else{
                    isPaid="0";
                }

                String path1, path2;

                if(billFilePath.isEmpty()) {
                    path1 = Patha;
                } else {
                    path1 = billFilePath;
                }

                utilitySupplier = spinner.getSelectedItem().toString();
                utilityExpireDate = txtExpDate.getText().toString();

                if(path1.equals("")) {
                    updateUtilityWithoutBill(userutilityid, isPaid, utilitySupplier, utilityTitle, utilityExpireDate);
                } else {
                    updateUtilityWithBill(userutilityid, path1, isPaid, utilitySupplier, utilityTitle, utilityExpireDate);
                }
            }
        });

    }

    private void getFileChooserIntent(int REQUEST_CODE) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(intent, REQUEST_CODE);
    }

    private void getSuppliersListForUtility() {
        progressBar.setVisibility(View.VISIBLE);
        supplierslist = new ArrayList<>();
        Call<ArrayList<UtilitiesModel>> call = apiService.getAllUtilities("application/json", "Bearer "+token);
        call.enqueue(new Callback<ArrayList<UtilitiesModel>>(){
            @Override
            public void onResponse(Call<ArrayList<UtilitiesModel>> call, Response<ArrayList<UtilitiesModel>> response) {
                if(response.isSuccessful()){
                    assert response.body() != null;
                    for (int i = 0; i < response.body().size(); i++) {
                        if (response.body().get(i).getTitle().equals(utilityTitle)) {
                            supplierslist = response.body().get(i).getSupplier();
                        }

                        if(supplierslist!=null && !supplierslist.isEmpty()) {
                            progressBar.setVisibility(View.GONE);
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(UpdateUtilityActivity.this,
                                    android.R.layout.simple_spinner_item, supplierslist);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner.setAdapter(dataAdapter);
                        }
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(UpdateUtilityActivity.this, "" + response.message(), Toast.LENGTH_SHORT).show();
                }

                getUserUtility(utilityTitle);
            }

            @Override
            public void onFailure(Call<ArrayList<UtilitiesModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                //Toast.makeText(UtilitiesInputSelection.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getUserUtility(String title){
        progressBar.setVisibility(View.VISIBLE);
        Call<ArrayList<UserUtilitiesModel>> call = apiService.getUserAllUtilities("application/json", "Bearer "+token);
        call.enqueue(new Callback<ArrayList<UserUtilitiesModel>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<ArrayList<UserUtilitiesModel>> call, Response<ArrayList<UserUtilitiesModel>> response) {
                String id;
                String utility;
                String supplier;
                String expireDate = null;
                int isPaid = 0;
                String loaForm = null;
                String lastBill = null;
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    for (int i = 0; i < response.body().size(); i++) {
                        if(title.equals(response.body().get(i).getUtiltity())) {
                            userutilityid = response.body().get(i).getId().toString();
                            utility = response.body().get(i).getUtiltity();
                            supplier = response.body().get(i).getSupplier();
                            expireDate = response.body().get(i).getExpirationdate();
                            isPaid = response.body().get(i).getBillpaid();
                            loaForm = response.body().get(i).getLoaForm().replaceAll("\\\\","");
                            lastBill = response.body().get(i).getLastBill().replaceAll("\\\\","");
                        }
                        progressBar.setVisibility(View.GONE);

                        if(expireDate!=null && !expireDate.isEmpty()) {
                            txtExpDate.setText(expireDate);
                        }

                        if(isPaid == 1){
                            paidYes.setChecked(true);
                        } else {
                            paidNo.setChecked(true);
                        }

                        GlideApp.with(UpdateUtilityActivity.this).load(baseurl_images+""+lastBill).apply(RequestOptions.centerCropTransform()).into(latestbill);
                        GlideApp.with(UpdateUtilityActivity.this).load(baseurl_loa+""+loaForm).apply(RequestOptions.centerCropTransform()).into(loaform);

                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<UserUtilitiesModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
//                Log.d("TAG",""+t.toString());
            }
        });
    }

    private boolean checkPermission() {
        // Permission is not granted
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                9);
    }

    @SuppressLint({"LongLogTag", "Range"})
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode==4) {
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    latestbill.setImageURI(selectedImage);
                    String[] filePaths = { MediaStore.Images.Media.DATA };
                    Cursor c = getContentResolver().query(selectedImage,filePaths, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePaths[0]);
                    String picturePath = c.getString(columnIndex);
                    c.close();
                    Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                    Log.w("path of image from gallery", picturePath+"");
                    Patha=picturePath;
                }
            } else if (requestCode ==1) {
                if (resultCode == RESULT_OK) {
                    Bitmap photos = (Bitmap)data.getExtras().get("data");
                    latestbill.setImageBitmap(photos);
                    Uri selectedImage = getImageUri(UpdateUtilityActivity.this,photos);
                    latestbill.setImageURI(selectedImage);
                    String[] filePath = { MediaStore.Images.Media.DATA };
                    Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePath[0]);
                    String picturePath = c.getString(columnIndex);
                    c.close();
                    Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                    Log.w("path of image from gallery", picturePath+"");
                    Patha=picturePath;
                }
            } else if (requestCode ==2) {
                if (resultCode == RESULT_OK) {
                    Bitmap photo = (Bitmap)data.getExtras().get("data");
                    loaform.setImageBitmap(photo);

                    Uri selectedImage = getImageUri(UpdateUtilityActivity.this,photo);
                    loaform.setImageURI(selectedImage);
                    String[] filePath = { MediaStore.Images.Media.DATA };
                    Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePath[0]);
                    String picturePath = c.getString(columnIndex);
                    c.close();
                    Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                    Log.w("path of image from gallery", picturePath+"");
                    Pathb=picturePath;
                }
            } else if (requestCode ==3) {
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    loaform.setImageURI(selectedImage);
                    String[] filePath = { MediaStore.Images.Media.DATA };
                    Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePath[0]);
                    String picturePath = c.getString(columnIndex);
                    c.close();
                    Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                    Log.w("path of image from gallery", picturePath+"");
                    Pathb=picturePath;
                }
            } else if(requestCode == 300) {
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

                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
                billFilePath = file.getPath() + "/documents/" + displayName;

            } else if(requestCode == 400) {
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

                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
                loaFilePath = file.getPath() + "/documents/" + displayName;

            }
        } catch(Exception e){
            Log.e("FileSelectorActivity", "File select error", e);
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage,   "IMG_" + Calendar.getInstance().getTime(), null);
        return Uri.parse(path);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void updateUtilityWithBill(String uid, String billPath, String billpaid, String supplier, String utility, String expiredate){
        progressBar.setVisibility(View.VISIBLE);

        RequestBody uidbody = RequestBody.create(MediaType.parse("multipart/form-data"), uid);

        RequestBody billPaidBody = RequestBody.create(MediaType.parse("multipart/form-data"), billpaid);

        RequestBody supplierBody = RequestBody.create(MediaType.parse("multipart/form-data"), supplier);

        RequestBody utilityBody = RequestBody.create(MediaType.parse("multipart/form-data"), utility);

        RequestBody expiredateBody = RequestBody.create(MediaType.parse("multipart/form-data"), expiredate);

        File uploadBill = new File(billPath);
        RequestBody billBody = RequestBody.create(MediaType.parse("multipart/form-data"), uploadBill);
        MultipartBody.Part billPart = MultipartBody.Part.createFormData("lastbill", uploadBill.getName(), billBody);

        Call<ApiResponse> call = apiService.updateUtilityWithBill("application/json", "Bearer "+token, uidbody,
                billPaidBody, supplierBody, utilityBody, expiredateBody, billPart);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    if(response.body().getMessage().contains("Successfully")){
                        Toast.makeText(UpdateUtilityActivity.this, "Utility Updated Successfully!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(UpdateUtilityActivity.this, "Failed to Update Utility!"+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    if (response.body() != null) {
                        Toast.makeText(UpdateUtilityActivity.this, "Something Went Wrong, Couldn't update Utility!" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(UpdateUtilityActivity.this, "Failed to update Utility, Due to "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUtilityWithoutBill(String uid, String billpaid, String supplier, String utility, String expiredate){
        progressBar.setVisibility(View.VISIBLE);

        RequestBody uidBody = RequestBody.create(MediaType.parse("multipart/form-data"), uid);

        RequestBody billPaidBody = RequestBody.create(MediaType.parse("multipart/form-data"), billpaid);

        RequestBody supplierBody = RequestBody.create(MediaType.parse("multipart/form-data"), supplier);

        RequestBody utilityBody = RequestBody.create(MediaType.parse("multipart/form-data"), utility);

        RequestBody expiredateBody = RequestBody.create(MediaType.parse("multipart/form-data"), expiredate);

        Call<ApiResponse> call = apiService.updateUtilityWithoutBill("application/json", "Bearer "+token, uidBody,
                billPaidBody, supplierBody, utilityBody, expiredateBody);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    if(response.body().getMessage().contains("Successfully")){
                        Toast.makeText(UpdateUtilityActivity.this, "Utility Updated Successfully!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(UpdateUtilityActivity.this, "Failed to Update Utility!"+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    if (response.body() != null) {
                        Toast.makeText(UpdateUtilityActivity.this, "Something Went Wrong, Couldn't update Utility!" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(UpdateUtilityActivity.this, "Failed to update Utility, Due to "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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