package com.speckpro.salonwiz.ui.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.speckpro.salonwiz.R;
import com.speckpro.salonwiz.newmodels.ApiResponse;
import com.speckpro.salonwiz.retrofit.ApiClient;
import com.speckpro.salonwiz.retrofit.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonnalSettings extends AppCompatActivity {

    private EditText fname,lname,dob,bname,city,baddress,supplier;
    ImageView imagetoshow;
    Button savedocument,updateprofile,changeimage;
    TextView browsedocument, textViewNameChar;
    String path="";
    String id;
    private TextView updatepicture;
    private GoogleSignInClient mGoogleSignInClient;
    private SharedPreferences sh;
    private String email, password;

    private ProgressBar progressBar;
    private ApiService apiService;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_personalsettings_user);

        TextView textView =findViewById(R.id.toolbar_titletext);
        textView.setText("Personal Settings");
        ImageView backarrow=findViewById(R.id.toolbar_backarrow);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        apiService = ApiClient.getClient().create(ApiService.class);
        progressBar = findViewById(R.id.progressBar_updateUser);

        changeimage=findViewById(R.id.textView_updateAvatar);
        imagetoshow=findViewById(R.id.profpicture_userpersonal);
        updateprofile=findViewById(R.id.updateprogile_personal);
        updatepicture=findViewById(R.id.textView_importFromInstaAndFb);
        textViewNameChar=findViewById(R.id.textView_nameChar);
        lname=findViewById(R.id.editText_lastName);
        fname=findViewById(R.id.editText_firstName);
        dob=findViewById(R.id.editText_dob);
        bname=findViewById(R.id.editText_businessName);
        city=findViewById(R.id.editText_cityName);
        baddress=findViewById(R.id.editText_officeAddress);
        supplier=findViewById(R.id.editText_supplierName);

        sh = getSharedPreferences("MySalonSharedPref", Context.MODE_PRIVATE);
        id = sh.getString("id", "");
        String image=sh.getString("Image","");
        email = sh.getString("email", "");
        token = sh.getString("token", "");
        password = sh.getString("Password", "");
        String InitLogin = sh.getString("InitLogin", "");
        String Role = sh.getString("Role", "");
        String BusinessName = sh.getString("BussinessName", "");
        String BusinessAddress = sh.getString("BussinessAddress", "");
        String ContactNumber = sh.getString("ContactNumber", "");
        String firstName = sh.getString("FirstName", "");
        String LastName = sh.getString("LastName", "");
        String SocialMedia = sh.getString("SocialMedia", "");
        String Filling = sh.getString("Filling", "");
        String City = sh.getString("City", "");
        String postcode = sh.getString("PostCode", "");
        String correcturl=image.replaceAll("\\\\","");
        //Log.d("PREFData",image+"---"+correcturl+"  "+City);
//        Glide.with(PersonnalSettings.this).load(correcturl).into(imagetoshow);

        //<--NEW CODE-->
        if(!ContactNumber.equals("null")) {
            dob.setText(ContactNumber);
        } else {
            dob.setHint("Phone Number");
        }
        if(!firstName.equals("null")) {
            fname.setText(firstName);
            textViewNameChar.setText(String.valueOf(firstName.charAt(0)));
        } else {
            fname.setHint("Robert");
        }
        //  lname.setText(LastName);
        if(!LastName.equals("null")){
            lname.setText(LastName);
        } else {
            lname.setHint("Williams");
        }
        //   bname.setText(BusinessName);
        if(!BusinessName.equals("null")){
            bname.setText(BusinessName);
        } else {
            bname.setHint("Business Name");
        }
        // baddress.setText(BusinessAddress);
        if(!BusinessAddress.equals("null")){
            baddress.setText(BusinessAddress);
        } else {
            baddress.setHint("Buisness Address");
        }
        if(!postcode.equals("null")){
            city.setText(postcode);
        } else {
            city.setHint("Post Code");
        }

//        updatepicture.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(!path.equals("")){
////                    updateimage(path,email);
//                } else {
//                    Toast.makeText(PersonnalSettings.this,"Please Select Profile Photo First",Toast.LENGTH_SHORT);
//                }
//            }
//        });

//        changeimage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(intent, 1);
//            }
//        });

//        imagetoshow=findViewById(R.id.profpicture_userpersonal);
//        browsedocument=findViewById(R.id.textView_updateAvatar);
//        imagetoshow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(intent, 1);
//            }
//        });

        updateprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fname.getText().toString().isEmpty() && lname.getText().toString().isEmpty() && city.getText().toString().isEmpty() && bname.getText().toString().isEmpty()&& baddress.getText().toString().isEmpty()) {
                    Toast.makeText(PersonnalSettings.this, "Please dont leave blank values", Toast.LENGTH_SHORT).show();
                }else {
                    String firstname = fname.getText().toString().trim();
                    String lastname = lname.getText().toString().trim();
                    String phone = dob.getText().toString().trim();
                    String businessName = bname.getText().toString().trim();
                    String businessAddress = baddress.getText().toString().trim();
                    String postcode = city.getText().toString().trim();

                    if (postcode.isEmpty() && firstname.isEmpty() && lastname.isEmpty() && email.isEmpty()&& password.isEmpty() && phone.isEmpty() && businessName.isEmpty()&& businessAddress.isEmpty()) {
                        Toast.makeText(PersonnalSettings.this, "Please dont leave blank values", Toast.LENGTH_SHORT).show();
                    } else {
                        updateUser(email, firstname, lastname, phone, businessName, businessAddress, postcode);
                    }
                }
            }
        });
    }

    private void updateUser(String email, String fname, String lname, String pnumber, String buisnessname, String buisnessaddress, String postcode) {
        progressBar.setVisibility(View.VISIBLE);
        Call<ApiResponse> call = apiService.updateUser("application/json", "Bearer "+token, email, fname, lname, pnumber, buisnessname, buisnessaddress, postcode);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
//                Log.d("TAG","User Update: "+response);
                if(response.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    //get user details from customer api
                    assert response.body() != null;
                    if(response.body().getMessage().equals("Updated Successfully")) {

                        sharedpreferences = getSharedPreferences("MySalonSharedPref", MODE_PRIVATE);
                        editor = sharedpreferences.edit();

                        editor.putBoolean("islogged", true);
                        editor.putString("id", id);
                        editor.putString("email", email);
                        editor.putString("token", token);
                        editor.putString("FirstName", fname);
                        editor.putString("LastName", lname);
                        editor.putString("DOB", pnumber);
                        editor.putString("City", postcode);
                        editor.putString("ContactNumber", pnumber);
                        editor.putString("BussinessName", buisnessname);
                        editor.putString("BussinessAddress", buisnessaddress);
                        editor.putString("PostCode", postcode);
                        editor.apply();

                        startActivity(new Intent(PersonnalSettings.this, PersonnalSettings.class));
                        finish();
                    } else {
                        Toast.makeText(PersonnalSettings.this, "Error Occured! "+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(PersonnalSettings.this, "Something went wrong!" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
//                Log.d("TAG","RegisterFail"+t);
                Toast.makeText(PersonnalSettings.this, "Check your Internet Connection! RegisterFail Due to: "+t, Toast.LENGTH_SHORT).show();
            }
        });
    }


//    @SuppressLint("LongLogTag")
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        try {
//            if (requestCode == 1) {
//                if (resultCode == RESULT_OK) {
//                    Uri selectedImage = data.getData();
//                    imagetoshow=findViewById(R.id.profpicture_userpersonal);
//                    imagetoshow.setImageURI(selectedImage);
//                    String[] filePath = { MediaStore.Images.Media.DATA };
//                    Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
//                    c.moveToFirst();
//                    int columnIndex = c.getColumnIndex(filePath[0]);
//                    String picturePath = c.getString(columnIndex);
//                    c.close();
//                    Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
//                    //Log.w("path of image from gallery", picturePath+"");
//                    path=picturePath;
//                    // filename=findViewById(R.id.heading_generalfile);
//                    String name=path.substring(path.lastIndexOf("/")+1);
//                    //filename.setText(name);
//                    String fileextension = name.substring(name.lastIndexOf("."));
//                    String filenamewithoutextension= name.substring(0, name.lastIndexOf(".")-0);
//                    //updateimage(path,email);
//                }
//            }
//        } catch(Exception e){
//            Log.e("FileSelectorActivity", "File select error", e);
//        }
//    }

//    private void updateimage(String image1path,String userid) {
//        final ProgressDialog progressDialog = new ProgressDialog(PersonnalSettings.this);
//        progressDialog.setCancelable(false);
//        progressDialog.setMessage("Please Wait");
//        progressDialog.show();
//        //Log.v("Data For Popup Post",image1path+userid);
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(baseurl)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        File image1a = new File(image1path);
//        RequestBody bill = RequestBody.create(MediaType.parse("multipart/form-data"), image1a);
//        MultipartBody.Part image1=MultipartBody.Part.createFormData("image",image1a.getName(),bill);
//        RequestBody User =
//                RequestBody.create(MediaType.parse("multipart/form-data"), userid);
//        userutilitiesretrofitapi retrofitAPI = retrofit.create(userutilitiesretrofitapi.class);
//
//        Call<ResponseBody> call = retrofitAPI.signupimage(User,image1);
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                progressDialog.dismiss();
//                if(response!=null){
//                    //postdatawithvolley(email, password);
//                    Toast.makeText(PersonnalSettings.this, "Image Updated", Toast.LENGTH_SHORT).show();
//                   // Log.d("TAG", "onResponse: "+response.body());
//                    Log.d("TAG", "ImageResponse: "+response.message());
//                } else {
//                    Toast.makeText(PersonnalSettings.this, "Data not added,Contact to Speckpro", Toast.LENGTH_SHORT).show();
//                }
//            }
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                progressDialog.dismiss();
//                Toast.makeText(PersonnalSettings.this, "This Utility is already submitted", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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