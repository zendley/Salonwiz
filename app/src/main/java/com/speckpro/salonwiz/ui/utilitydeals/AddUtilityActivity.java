package com.speckpro.salonwiz.ui.utilitydeals;

import static com.speckpro.salonwiz.retrofit.BaseUrl.baseurl_loa;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DownloadManager;
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

import com.github.barteksc.pdfviewer.PDFView;
import com.speckpro.salonwiz.R;
import com.speckpro.salonwiz.newmodels.ApiResponse;
import com.speckpro.salonwiz.newmodels.UtilitiesModel;
import com.speckpro.salonwiz.retrofit.ApiClient;
import com.speckpro.salonwiz.retrofit.ApiService;

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

public class AddUtilityActivity extends AppCompatActivity {

    DatePickerDialog datePickerDialog;
    private String isPaid;
    private String Patha,Pathb;
    private ImageView latestbill,loaform;
    private Button latestbills,loaforms;
    private TextView downloadform;
    public static List<String> s;
    private Spinner spinner;
    String userutilityid;
    private static final int PICKFILE_REQUEST_CODE = 300;
    private static final int PICKLOA_REQUEST_CODE = 400;
    private String loaFilePath = "", billFilePath = "";
    private PDFView pdfView;

    private ProgressBar progressBar;
    private ApiService apiService;
    SharedPreferences sharedpreferences;
    String id;
    String email;
    String token;
    String utilityTitle;
    String utilitySupplier;
    String utilityExpireDate;
    List<String> supplierslist;

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
        setContentView(R.layout.activity_add_utility);

        TextView textView =findViewById(R.id.toolbar_titletext);
        textView.setText("Fill Form");
        textView.setTextColor(Color.BLACK);
        ImageView backarrow=findViewById(R.id.toolbar_backarrow);
        backarrow.setColorFilter(ContextCompat.getColor(AddUtilityActivity.this, R.color.black), android.graphics.PorterDuff.Mode.SRC_IN);        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        apiService = ApiClient.getClient().create(ApiService.class);
        progressBar = findViewById(R.id.progressBar_fillUtilityForm);

        sharedpreferences = getSharedPreferences("MySalonSharedPref", MODE_PRIVATE);
        token = sharedpreferences.getString("token", "");
        id = sharedpreferences.getString("id", "");
        email=sharedpreferences.getString("email","");

        Spinner supplier=findViewById(R.id.popup_utilsupplierlist);
        TextView titl=findViewById(R.id.popup_titleutil);
        TextView expdate=findViewById(R.id.popup_utilcontractexpdate);
        TextView latestbillpath=findViewById(R.id.popup_latestbillimagetextpath);
        latestbill=findViewById(R.id.popup_latestbillimage);
        loaform=findViewById(R.id.popup_loaformimage);
        latestbills=findViewById(R.id.popup_latestbilluploadbtn);
        loaforms=findViewById(R.id.popup_loaformuploadbtn);
        Button popupsubmit=findViewById(R.id.popup_utilitysubmit);
        RadioButton simpleRadioButton=findViewById(R.id.popup_latestbillradio1);

        titl.setText(utilityTitle);

        utilityTitle = getIntent().getExtras().getString("title");
        userutilityid = getIntent().getExtras().getString("id");

       // Toast.makeText(AddUtilityActivity.this, "uid"+userutilityid+" Title"+utilityTitle, Toast.LENGTH_SHORT).show();

        supplierslist = new ArrayList<>();
        getSuppliersListForUtility();

        downloadform=findViewById(R.id.popup_utildownloadloaform);
        downloadform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(AddUtilityActivity.this);
                progressDialog.setCancelable(false); // set cancelable to false
                progressDialog.setMessage("Please Wait"); // set message
                progressDialog.show();
                String filename = "LOAForm";
                String downloadUrlOfImage =baseurl_loa+"storage/LOAForm.doc";
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

        loaforms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    try {
                        PackageManager pm = getPackageManager();
                        int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, getPackageName());
                        if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                            final CharSequence[] options = {"Take Photo", "Choose From Gallery","Choose File","Cancel"};
                            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(AddUtilityActivity.this);
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
                            Toast.makeText(AddUtilityActivity.this, "Camera Permission error", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(AddUtilityActivity.this, "Camera Permission error", Toast.LENGTH_SHORT).show();
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
                                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(AddUtilityActivity.this);
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
                                Toast.makeText(AddUtilityActivity.this, "Camera Permission error", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(AddUtilityActivity.this, "Camera Permission error", Toast.LENGTH_SHORT).show();
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
                            final CharSequence[] options = {"Take Photo", "Choose From Gallery", "Choose File","Cancel"};
                            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(AddUtilityActivity.this);
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
                                    }  else if (options[item].equals("Choose File")) {
                                        dialog.dismiss();
                                        getFileChooserIntent(PICKFILE_REQUEST_CODE);
                                    } else if (options[item].equals("Cancel")) {
                                        dialog.dismiss();
                                    }
                                }
                            });
                            builder.show();
                        } else
                            Toast.makeText(AddUtilityActivity.this, "Camera Permission error", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(AddUtilityActivity.this, "Camera Permission error", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                } else {
                    requestPermission();
                    if (checkPermission()) {
                        try {
                            PackageManager pm = getPackageManager();
                            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, getPackageName());
                            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                                final CharSequence[] options = {"Take Photo", "Choose From Gallery", "Choose File","Cancel"};
                                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(AddUtilityActivity.this);
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
                                Toast.makeText(AddUtilityActivity.this, "Camera Permission error", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(AddUtilityActivity.this, "Camera Permission error", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        popupsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPaid="";
                if(simpleRadioButton.isChecked()){
                    isPaid="1";
                } else {
                    isPaid="0";
                }

                String path1, path2;

                if(billFilePath.isEmpty()) {
                    path1 = Patha;
                } else {
                    path1 = billFilePath;
                }

                if (loaFilePath.isEmpty()) {
                    path2 = Pathb;
                } else {
                    path2 = loaFilePath;
                }

                utilitySupplier = supplier.getSelectedItem().toString();
                utilityExpireDate = expdate.getText().toString();

                //call api function
                addUtility(userutilityid, path1, path2, isPaid, utilitySupplier, utilityTitle, utilityExpireDate);
            }
        });

        expdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(AddUtilityActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(year, monthOfYear, dayOfMonth);

                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                expdate.setText(format.format(calendar.getTime()));

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
    }

    private void getFileChooserIntent(int REQUEST_CODE) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(intent, REQUEST_CODE);
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

                    Uri selectedImage = getImageUri(AddUtilityActivity.this,photos);
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
                    Uri selectedImage = getImageUri(AddUtilityActivity.this,photo);
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

            }  else if(requestCode == 400) {
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

    private void getSuppliersListForUtility(){
        Call<ArrayList<UtilitiesModel>> call = apiService.getAllUtilities("application/json", "Bearer "+token);
        call.enqueue(new Callback<ArrayList<UtilitiesModel>>() {
            @Override
            public void onResponse(Call<ArrayList<UtilitiesModel>> call, Response<ArrayList<UtilitiesModel>> response) {
                if(response.isSuccessful()){
                    assert response.body() != null;
                    for (int i = 0; i < response.body().size(); i++) {
                        if (response.body().get(i).getTitle().equals(utilityTitle)) {
                            supplierslist = response.body().get(i).getSupplier();
                        }

                        spinner=findViewById(R.id.popup_utilsupplierlist);
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(AddUtilityActivity.this,
                                android.R.layout.simple_spinner_item, supplierslist);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(dataAdapter);

                        progressBar.setVisibility(View.GONE);
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(AddUtilityActivity.this, "" + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<UtilitiesModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                //Toast.makeText(UtilitiesInputSelection.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addUtility(String uid, String billPath, String loaPath, String billpaid, String supplier, String utility, String expiredate){
        progressBar.setVisibility(View.VISIBLE);

        RequestBody uidbody = RequestBody.create(MediaType.parse("multipart/form-data"), uid);

        RequestBody billPaidBody = RequestBody.create(MediaType.parse("multipart/form-data"), billpaid);

        RequestBody supplierBody = RequestBody.create(MediaType.parse("multipart/form-data"), supplier);

        RequestBody utilityBody = RequestBody.create(MediaType.parse("multipart/form-data"), utility);

        RequestBody expiredateBody = RequestBody.create(MediaType.parse("multipart/form-data"), expiredate);

        File uploadBill = new File(billPath);
        RequestBody billBody = RequestBody.create(MediaType.parse("multipart/form-data"), uploadBill);
        MultipartBody.Part billPart = MultipartBody.Part.createFormData("lastbill", uploadBill.getName(), billBody);

        File uploadloa = new File(loaPath);
        RequestBody loaBody = RequestBody.create(MediaType.parse("multipart/form-data"), uploadloa);
        MultipartBody.Part loaPart = MultipartBody.Part.createFormData("loaform", uploadloa.getName(), loaBody);

        Call<ApiResponse> call = apiService.updateUtility("application/json", "Bearer "+token, uidbody,
                billPaidBody, supplierBody, utilityBody, expiredateBody, billPart, loaPart);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if(response.isSuccessful()){
                    if(response.body().getMessage().equals("Added Successfully")){
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(AddUtilityActivity.this, "Utility Added Successfully!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(AddUtilityActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    if (response.body() != null) {
                        Toast.makeText(AddUtilityActivity.this, "Something Went Wrong, Couldn't add Utility!" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(AddUtilityActivity.this, "Something Went Wrong, Couldn't add Utility!" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(AddUtilityActivity.this, "Failed to add Utility, Due to "+t.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
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