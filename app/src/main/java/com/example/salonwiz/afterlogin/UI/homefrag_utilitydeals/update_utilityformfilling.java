package com.example.salonwiz.afterlogin.UI.homefrag_utilitydeals;

import static com.example.salonwiz.BaseUrl.baseurl;
import static com.facebook.FacebookSdk.getApplicationContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.graphics.drawable.Drawable;
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
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.request.RequestOptions;
import com.example.salonwiz.GlideApp;
import com.example.salonwiz.R;
import com.example.salonwiz.afterlogin.filingcabinet.Utilititesinput.card_model;
import com.example.salonwiz.afterlogin.filingcabinet.Utilititesinput.cardadapter;
import com.example.salonwiz.afterlogin.filingcabinet.Utilititesinput.userutilitiesretrofitapi;
import com.github.barteksc.pdfviewer.PDFView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class update_utilityformfilling extends AppCompatActivity {
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
        setContentView(R.layout.activity_update_utilityformfilling);
        Patha="";
        TextView textView =findViewById(R.id.toolbar_titletext);
        textView.setText("Update Form");
        textView.setTextColor(Color.BLACK);
        ImageView backarrow=findViewById(R.id.toolbar_backarrow);
        backarrow.setColorFilter(ContextCompat.getColor(update_utilityformfilling.this, R.color.black), android.graphics.PorterDuff.Mode.SRC_IN);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        SharedPreferences sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        String id = sh.getString("id", "");
        String email=sh.getString("email","");

        Spinner supplier=findViewById(R.id.upopup_utilsupplierlist);
        TextView titl=findViewById(R.id.upopup_titleutil);
        TextView expdate=findViewById(R.id.upopup_utilcontractexpdate);
        TextView latestbillpath=findViewById(R.id.upopup_latestbillimagetextpath);
        latestbill=findViewById(R.id.upopup_latestbillimage);
        loaform=findViewById(R.id.upopup_loaformimage);
        latestbills=findViewById(R.id.upopup_latestbilluploadbtn);
        loaforms=findViewById(R.id.upopup_loaformuploadbtn);

        getdataforspinner(id,getIntent().getExtras().getString("title"));
        Button popupsubmit=findViewById(R.id.upopup_utilitysubmit);
        RadioButton simpleRadioButton=findViewById(R.id.upopup_latestbillradio1);
        RadioButton simpleRadioButton1=findViewById(R.id.upopup_latestbillradio2);
        setdefaultdata(id,getIntent().getExtras().getString("title"));

        popupsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPaid="";
                if(simpleRadioButton.isChecked()){
                    isPaid="true";
                }else{
                    isPaid="false";
                }
                Log.v("Data For Popup Post",Patha+" "+Pathb+" "+getIntent().getExtras().getString("title")+supplier.getSelectedItem().toString()+id+expdate.getText().toString()+isPaid+email);

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

                if(path1.equals("")){
                    postpopupdatawithoutimages(userutilityid,getIntent().getExtras().getString("title"),supplier.getSelectedItem().toString(),id,expdate.getText().toString(),isPaid);
                } else{
                 postpopupData(userutilityid,path1,getIntent().getExtras().getString("title"),supplier.getSelectedItem().toString(),id,expdate.getText().toString(),isPaid);
                }
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
                            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(update_utilityformfilling.this);
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
                            Toast.makeText(update_utilityformfilling.this, "Camera Permission error", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(update_utilityformfilling.this, "Camera Permission error", Toast.LENGTH_SHORT).show();
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
                                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(update_utilityformfilling.this);
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
                                Toast.makeText(update_utilityformfilling.this, "Camera Permission error", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(update_utilityformfilling.this, "Camera Permission error", Toast.LENGTH_SHORT).show();
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
                            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(update_utilityformfilling.this);
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
                            Toast.makeText(update_utilityformfilling.this, "Camera Permission error", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(update_utilityformfilling.this, "Camera Permission error", Toast.LENGTH_SHORT).show();
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
                                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(update_utilityformfilling.this);
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
                                Toast.makeText(update_utilityformfilling.this, "Camera Permission error", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(update_utilityformfilling.this, "Camera Permission error", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        titl.setText(getIntent().getExtras().getString("title"));
        expdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(update_utilityformfilling.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                expdate.setText((monthOfYear + 1) + "/" +dayOfMonth + "/" + year);
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

    private void setdefaultdata(String User,String titles) {
        final ProgressDialog progressDialog = new ProgressDialog(update_utilityformfilling.this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait"); // set message
        progressDialog.show(); // show progress dialog
        String url2=baseurl+"api/Userutilities/getUtilitiesOfOneUser";
        try {
            JSONObject object1=new JSONObject();
            object1.put("User",User);
            RequestQueue mqueue4 = Volley.newRequestQueue(getApplicationContext());
            JsonObjectRequest jsonObjRequest4 = new JsonObjectRequest(Request.Method.POST,
                    url2, object1, new com.android.volley.Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("myapp", "the response is" + response.toString());
                    try {
                        progressDialog.dismiss();
                        JSONObject json = new JSONObject(String.valueOf(response));
                        JSONArray result = json.getJSONArray("userUtilities");
                        // JSONObject utilities=result.getJSONObject("Utilities");
                        String message=json.getString("message");
                        if(message.equals("Utility Found for this User")){
                            for (int i=0;i<result.length();i++){
                                JSONObject userdata=result.getJSONObject(i);
                                JSONObject Utilities=userdata.getJSONObject("Utilities");
                                String title=Utilities.getString("Title");
                                JSONArray suppl=Utilities.getJSONArray("Supplier");
                                JSONObject userutil=userdata.getJSONObject("userUtility");
                                if(title.equals(titles)){
                                    String cv=userutil.getString("LastBill");
                                    String cb=userutil.getString("LOAForm");
                                    latestbillurl=cv.replaceAll("\\\\","");
                                    loaformurl=cb.replaceAll("\\\\","");
                                    ispaidget=userutil.getString("IsPaid");
                                    getexpdate=userutil.getString("ContractExpiryDate");
                                    spinner=findViewById(R.id.popup_utilsupplierlist);
                                    JSONObject Utilitiesa=userutil.getJSONObject("Utilities");
                                    suppliername=Utilitiesa.getString("Supplier");
                                    titleget=Utilitiesa.getString("Title");
                                    GlideApp.with(update_utilityformfilling.this).load(latestbillurl).apply(RequestOptions.centerCropTransform()).into(latestbill);
                                    GlideApp.with(update_utilityformfilling.this).load(loaformurl).apply(RequestOptions.centerCropTransform()).into(loaform);
                                    //  list.remove(suppliername);
                                    for (int kl=0;kl<list.size();kl++){
                                        Log.d("spinnerarraylist", list.get(kl));
                                        if(list.get(kl).equals(suppliername)){
                                            list.remove(kl);
                                        }
                                    }
                                    list.add(0, suppliername);

                                    Log.d("data",latestbillurl+loaformurl+ispaidget+getexpdate+suppliername+titleget);
                                    String currentString = getexpdate;
                                    String[] separated = currentString.split("-");
                                    String byear=separated[0]; // this will contain "Fruit"
                                    String bmonth=separated[1];
                                    String xday=separated[2];
                                    String bday=xday.substring(0,2);
                                    TextView dexpdate=findViewById(R.id.upopup_utilcontractexpdate);
                                    dexpdate.setText(bmonth + "/"
                                            +bday + "/" + byear);

                                    RadioButton simpleRadioButton=findViewById(R.id.upopup_latestbillradio1);
                                    RadioButton simpleRadioButton1=findViewById(R.id.upopup_latestbillradio2);
                                    if(ispaidget.equals("true")){
                                        simpleRadioButton.setChecked(true);
                                    } else {
                                        simpleRadioButton1.setChecked(true);
                                    }
                                    String blastfileName = latestbillurl.substring(latestbillurl.lastIndexOf('/') + 1);
                                    String bloaformName = latestbillurl.substring(latestbillurl.lastIndexOf('/') + 1);
                                    final ProgressDialog progressDialog = new ProgressDialog(update_utilityformfilling.this);
                                    progressDialog.setCancelable(false); // set cancelable to false
                                    progressDialog.setMessage("Please Wait"); // set message
                                    progressDialog.show();

                                    String filename = blastfileName;
                                    String downloadUrlOfImage =latestbillurl;
                                    File direct =
                                            new File(Environment
                                                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                                                    .getAbsolutePath() + "/" + "" + "/");

                                    if (!direct.exists()) {
                                        direct.mkdir();
                                        Log.d("asd", "dir created for first time");
                                    }

                                    DownloadManager dm = (DownloadManager) getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
                                    Uri downloadUri = Uri.parse(downloadUrlOfImage);
                                    DownloadManager.Request request = new DownloadManager.Request(downloadUri);
                                    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                                            .setAllowedOverRoaming(false)
                                            .setTitle(filename)
                                            .setMimeType("image/jpeg")
                                            .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES,
                                                    File.separator + "" +filename);
                                    dm.enqueue(request);
                                    progressDialog.dismiss();

                                    String mfilename = bloaformName;
                                    String downloadUrlOfImages =loaformurl;
                                    File directs =
                                            new File(Environment
                                                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                                                    .getAbsolutePath() + "/" + "" + "/");

                                    if (!directs.exists()) {
                                        directs.mkdir();
                                        Log.d("asd", "dir created for first time");
                                    }

                                    DownloadManager dms = (DownloadManager) getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
                                    Uri downloadUris = Uri.parse(downloadUrlOfImages);
                                    DownloadManager.Request requests = new DownloadManager.Request(downloadUris);
                                    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                                            .setAllowedOverRoaming(false)
                                            .setTitle(mfilename)
                                            .setMimeType("image/jpeg")
                                            .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES,
                                                    File.separator + "" +mfilename);

                                    dms.enqueue(requests);
                                    progressDialog.dismiss();
                                }
                            }
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Incorrect email or password", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Incorrect email or password", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Incorrect email or password", Toast.LENGTH_SHORT).show();
                }
            }
            ) { @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
            };
            mqueue4.add(jsonObjRequest4);

        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
            Toast.makeText(getApplicationContext(), "Incorrect email or password", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
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
                    Uri selectedImage = getImageUri(update_utilityformfilling.this,photos);
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

                    Uri selectedImage = getImageUri(update_utilityformfilling.this,photo);
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

    private void postpopupdatawithoutimages(String userutilids,String title,String supplier,String userid,String expdate,String ispaid){

            final ProgressDialog progressDialog = new ProgressDialog(update_utilityformfilling.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Please Wait");
            progressDialog.show();
            Log.v("Data For Popup Post",title+supplier+userid+expdate+ispaid);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseurl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            RequestBody User =
                    RequestBody.create(MediaType.parse("multipart/form-data"), userid);
            RequestBody UtilitiesTitle =
                    RequestBody.create(MediaType.parse("multipart/form-data"), title);
            RequestBody userutilid =
                    RequestBody.create(MediaType.parse("multipart/form-data"), userutilids);
            RequestBody UtilitiesSupplier =
                    RequestBody.create(MediaType.parse("multipart/form-data"), supplier);
            RequestBody ContractExpiryDate =
                    RequestBody.create(MediaType.parse("multipart/form-data"), expdate);
            RequestBody IsPaid=
                    RequestBody.create(MediaType.parse("multipart/form-data"), isPaid);

            userutilitiesretrofitapi retrofitAPI = retrofit.create(userutilitiesretrofitapi.class);

            Call<ResponseBody> call = retrofitAPI.userutilupdatewithoutimages(userutilid,User,UtilitiesTitle,UtilitiesSupplier,ContractExpiryDate,IsPaid);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    progressDialog.dismiss();
                    // this method is called when we get response from our api.
                    if(response!=null){
                        Toast.makeText(update_utilityformfilling.this, "Utility Added", Toast.LENGTH_SHORT).show();
                        Log.d("TAG", "onResponse: "+response.body());
                        Log.d("Response Message", "onResponse: "+response.message());
                        Intent intent=new Intent(getApplicationContext(), utilitydeals.class);
                        // intent.putExtra("packagestatus","basicPending");
                        startActivity(intent);
                    }else{
                        Toast.makeText(update_utilityformfilling.this, "Data not added,Contact to Speckpro", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(update_utilityformfilling.this, "This Utility is already submitted", Toast.LENGTH_SHORT).show();
                }
            });
    }

    private void postpopupData(String userutilids,String image1path,String title,String supplier,String userid,String expdate,String ispaid) {
        final ProgressDialog progressDialog = new ProgressDialog(update_utilityformfilling.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait");
        progressDialog.show();
        Log.v("Data For Popup Post",image1path+" "+title+supplier+userid+expdate+ispaid);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseurl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        File image1a = new File(image1path);
        RequestBody bill =
                RequestBody.create(MediaType.parse("multipart/form-data"), image1a);

        MultipartBody.Part image1=MultipartBody.Part.createFormData("image",image1a.getName(),bill);

        RequestBody User =
                RequestBody.create(MediaType.parse("multipart/form-data"), userid);
        RequestBody UtilitiesTitle =
                RequestBody.create(MediaType.parse("multipart/form-data"), title);
        RequestBody userutilid =
                RequestBody.create(MediaType.parse("multipart/form-data"), userutilids);
        RequestBody UtilitiesSupplier =
                RequestBody.create(MediaType.parse("multipart/form-data"), supplier);
        RequestBody ContractExpiryDate =
                RequestBody.create(MediaType.parse("multipart/form-data"), expdate);
        RequestBody IsPaid=
                RequestBody.create(MediaType.parse("multipart/form-data"), isPaid);

        userutilitiesretrofitapi retrofitAPI = retrofit.create(userutilitiesretrofitapi.class);

        Call<ResponseBody> call = retrofitAPI.userutilupdateupdate(userutilid,User,UtilitiesTitle,UtilitiesSupplier,ContractExpiryDate,IsPaid,image1);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();
                if(response!=null){
                    Toast.makeText(update_utilityformfilling.this, "Utility Added", Toast.LENGTH_SHORT).show();
                    Log.d("TAG", "onResponse: "+response.body());
                    Log.d("Response Message", "onResponse: "+response.message());
                    Intent intent=new Intent(getApplicationContext(), utilitydeals.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(update_utilityformfilling.this, "Data not added,Contact to Speckpro", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(update_utilityformfilling.this, "This Utility is already submitted", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void getdataforspinner(String User,String titles){
        final ProgressDialog progressDialog = new ProgressDialog(update_utilityformfilling.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait");
        progressDialog.show();
        String url2=baseurl+"api/Userutilities/getUtilitiesOfOneUser";
        try {
            JSONObject object1=new JSONObject();
            object1.put("User",User);
            RequestQueue mqueue4 = Volley.newRequestQueue(getApplicationContext());
            JsonObjectRequest jsonObjRequest4 = new JsonObjectRequest(Request.Method.POST,
                    url2, object1, new com.android.volley.Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("myapp", "the response is" + response.toString());
                    try {
                        progressDialog.dismiss();
                        JSONObject json = new JSONObject(String.valueOf(response));
                        JSONArray result = json.getJSONArray("userUtilities");
                        // JSONObject utilities=result.getJSONObject("Utilities");
                        String message=json.getString("message");
                        if(message.equals("Utility Found for this User")){
                            for (int i=0;i<result.length();i++){
                                JSONObject userdata=result.getJSONObject(i);
                                JSONObject Utilities=userdata.getJSONObject("Utilities");
                                String title=Utilities.getString("Title");
                                JSONArray suppl=Utilities.getJSONArray("Supplier");
                                JSONObject userutil=userdata.getJSONObject("userUtility");
                                if(title.equals(titles)){
                                    userutilityid=userutil.getString("_id");

                                    for(int v=0;v<suppl.length();v++){
                                        list.add(suppl.getString(v));
                                    }
                                    spinner=findViewById(R.id.upopup_utilsupplierlist);
                                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(update_utilityformfilling.this,
                                            android.R.layout.simple_spinner_item, list);
                                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spinner.setAdapter(dataAdapter);
                                    Log.d("Spinner Data", list.toString());
                                }
                            }
                        } else{
                            Toast.makeText(getApplicationContext(), "Failed to Load Data!", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Failed to Load Data!!!", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Check your Internet Connection!", Toast.LENGTH_SHORT).show();
                }
            }) { @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
            };
            mqueue4.add(jsonObjRequest4);

        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
            Toast.makeText(getApplicationContext(), "Failed to Load Data!!", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
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