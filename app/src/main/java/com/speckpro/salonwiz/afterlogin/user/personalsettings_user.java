package com.speckpro.salonwiz.afterlogin.user;

import static com.facebook.login.widget.ProfilePictureView.TAG;
import static com.speckpro.salonwiz.BaseUrl.baseurl;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.speckpro.salonwiz.R;
import com.speckpro.salonwiz.afterlogin.filingcabinet.Utilititesinput.userutilitiesretrofitapi;
import com.speckpro.salonwiz.retrofit.RetrofitAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
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

public class personalsettings_user extends AppCompatActivity {

    private EditText fname,lname,dob,bname,city,baddress,supplier;
    ImageView imagetoshow;
    Button savedocument,updateprofile,changeimage;
    TextView browsedocument;
    String path="";
    String id;
    private TextView updatepicture;
    private GoogleSignInClient mGoogleSignInClient;
    private SharedPreferences sh;
    private String email, password;

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
        changeimage=findViewById(R.id.textView_updateAvatar);
        imagetoshow=findViewById(R.id.profpicture_userpersonal);
        updateprofile=findViewById(R.id.updateprogile_personal);
        updatepicture=findViewById(R.id.textView_importFromInstaAndFb);
        lname=findViewById(R.id.editText_lastName);
        fname=findViewById(R.id.editText_firstName);
        dob=findViewById(R.id.editText_dob);
        bname=findViewById(R.id.editText_businessName);
        city=findViewById(R.id.editText_cityName);
        baddress=findViewById(R.id.editText_officeAddress);
        supplier=findViewById(R.id.editText_supplierName);
        fname.setTextColor(Color.BLACK);
        lname.setTextColor(Color.BLACK);
        dob.setTextColor(Color.BLACK);
        city.setTextColor(Color.BLACK);
        baddress.setTextColor(Color.BLACK);
        supplier.setTextColor(Color.BLACK);

        sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        id = sh.getString("id", "");
        String image=sh.getString("Image","");
        email = sh.getString("email", "");
        password = sh.getString("Password", "");
        String InitLogin = sh.getString("InitLogin", "");
        String Role = sh.getString("Role", "");
        String BusinessName = sh.getString("BusinessName", "");
        String BusinessAddress = sh.getString("BusinessAddress", "");
        String ContactNumber = sh.getString("ContactNumber", "");
        String FirstName = sh.getString("FirstName", "");
        String LastName = sh.getString("LastName", "");
        String PostCode = sh.getString("PostCode", "");
        String SocialMedia = sh.getString("SocialMedia", "");
        String Filling = sh.getString("Filling", "");
        String City = sh.getString("City", "");
        String postcode = sh.getString("PostCode", "");
        String correcturl=image.replaceAll("\\\\","");
        //Log.d("PREFData",image+"---"+correcturl+"  "+City);
        Glide.with(personalsettings_user.this).load(correcturl).into(imagetoshow);

        //<OLD CODE>
//        //   fname.setText(FirstName);
//        fname.setText(FirstName);
//        //  lname.setText(LastName);
//        lname.setText(LastName);
//        //   bname.setText(BusinessName);
//        bname.setText(BusinessName);
//        // baddress.setText(BusinessAddress);
//        baddress.setText(BusinessAddress);
//        city.setText(City);

        //<--NEW CODE-->
        if(!ContactNumber.equals("null")) {
            dob.setText(ContactNumber);
        } else {
            dob.setHint("Phone Number");
        }
        if(!FirstName.equals("null")) {
            fname.setText(FirstName);
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

        updatepicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!path.equals("")){
                    updateimage(path,email);
                } else {
                    Toast.makeText(personalsettings_user.this,"Please Select Profile Photo First",Toast.LENGTH_SHORT);
                }
            }
        });
        changeimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });
        imagetoshow=findViewById(R.id.profpicture_userpersonal);
        browsedocument=findViewById(R.id.textView_updateAvatar);
        imagetoshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });

        updateprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fname.getText().toString().isEmpty()&&lname.getText().toString().isEmpty() && city.getText().toString().isEmpty() && bname.getText().toString().isEmpty()&& baddress.getText().toString().isEmpty()) {
                    Toast.makeText(personalsettings_user.this, "Please dont leave blank values", Toast.LENGTH_SHORT).show();
                }else {
                    Log.v("Data For Popup Post", fname.getText().toString() + lname.getText().toString() + email + city.getText().toString() + bname.getText().toString() + baddress.getText().toString());
                    postData(fname.getText().toString(), lname.getText().toString(), email, city.getText().toString(), bname.getText().toString(), baddress.getText().toString(),dob.getText().toString());
                }
            }
        });

        TextView textView =findViewById(R.id.toolbar_titletext);
        textView.setText("Personal Settings");
        ImageView backarrow=findViewById(R.id.toolbar_backarrow);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @SuppressLint("LongLogTag")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 1) {
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    imagetoshow=findViewById(R.id.profpicture_userpersonal);
                    imagetoshow.setImageURI(selectedImage);
                    String[] filePath = { MediaStore.Images.Media.DATA };
                    Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePath[0]);
                    String picturePath = c.getString(columnIndex);
                    c.close();
                    Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                    //Log.w("path of image from gallery", picturePath+"");
                    path=picturePath;
                    // filename=findViewById(R.id.heading_generalfile);
                    String name=path.substring(path.lastIndexOf("/")+1);
                    //filename.setText(name);
                    String fileextension = name.substring(name.lastIndexOf("."));
                    String filenamewithoutextension= name.substring(0, name.lastIndexOf(".")-0);
                    updateimage(path,email);
                }
            }
        } catch(Exception e){
            Log.e("FileSelectorActivity", "File select error", e);
        }
    }

    private void updateimage(String image1path,String userid) {
        final ProgressDialog progressDialog = new ProgressDialog(personalsettings_user.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait");
        progressDialog.show();
        //Log.v("Data For Popup Post",image1path+userid);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseurl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        File image1a = new File(image1path);
        RequestBody bill = RequestBody.create(MediaType.parse("multipart/form-data"), image1a);
        MultipartBody.Part image1=MultipartBody.Part.createFormData("image",image1a.getName(),bill);
        RequestBody User =
                RequestBody.create(MediaType.parse("multipart/form-data"), userid);
        userutilitiesretrofitapi retrofitAPI = retrofit.create(userutilitiesretrofitapi.class);

        Call<ResponseBody> call = retrofitAPI.signupimage(User,image1);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();
                if(response!=null){
                    //postdatawithvolley(email, password);
                    Toast.makeText(personalsettings_user.this, "Image Updated", Toast.LENGTH_SHORT).show();
                   // Log.d("TAG", "onResponse: "+response.body());
                    Log.d("TAG", "ImageResponse: "+response.message());
                } else {
                    Toast.makeText(personalsettings_user.this, "Data not added,Contact to Speckpro", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(personalsettings_user.this, "This Utility is already submitted", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void postData(String sfname, String slname, String semail,String city, String sbuisnessname, String sbuisnessaddress,String Phonenumber) {

        final ProgressDialog progressDialog = new ProgressDialog(personalsettings_user.this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait"); // set message
        progressDialog.show(); // show progress dialog
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseurl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestBody mfname =
                RequestBody.create(MediaType.parse("multipart/form-data"), sfname);
        RequestBody mcontactnmber =
                RequestBody.create(MediaType.parse("multipart/form-data"), Phonenumber);
        RequestBody mlname =
                RequestBody.create(MediaType.parse("multipart/form-data"), slname);
        RequestBody memail =
                RequestBody.create(MediaType.parse("multipart/form-data"), semail);
        RequestBody mcity =
                RequestBody.create(MediaType.parse("multipart/form-data"), city);
        RequestBody msbname=
                RequestBody.create(MediaType.parse("multipart/form-data"), sbuisnessname);
        RequestBody msaddress=
                RequestBody.create(MediaType.parse("multipart/form-data"), sbuisnessaddress);

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        Call<ResponseBody> call = retrofitAPI.updateprofile(memail,mfname,mlname,msbname,msaddress,mcity,mcontactnmber);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();
               Toast.makeText(personalsettings_user.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                if(response.isSuccessful()){
                    progressDialog.dismiss();
                    Log.i(TAG, response.toString());
                    SharedPreferences sharedpreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("BusinessAddress", sbuisnessaddress);
                    editor.putString("FirstName", sfname);
                    editor.putString("LastName", slname);
                    editor.putString("BusinessName", sbuisnessname);
                    editor.putString("PostCode", city);
                    editor.putString("ContactNumber", Phonenumber);
                    editor.apply();
                    Intent i = new Intent(personalsettings_user.this, personalsettings_user.class);
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(i);
                    overridePendingTransition(0, 0);
                }
                else {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Check Your Internet Connection!",Toast.LENGTH_SHORT);
            }
        });
    }
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


    private void postdatawithvolley(String Email, String Password){
        final ProgressDialog progressDialog = new ProgressDialog(personalsettings_user.this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait"); // set message
        progressDialog.show(); // show progress dialog
        String url2=baseurl+"api/user/login";
        try {
            JSONObject object1=new JSONObject();
            object1.put("Email",Email);
            object1.put("Password",Password);
            RequestQueue mqueue4 = Volley.newRequestQueue(getApplicationContext());
            JsonObjectRequest jsonObjRequest4 = new JsonObjectRequest(Request.Method.POST,
                    url2, object1, new com.android.volley.Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("myapp", "the response is" + response.toString());
                    try {
                        progressDialog.dismiss();
                        JSONObject json = new JSONObject(String.valueOf(response));
                        JSONArray result = json.getJSONArray("User");
                        JSONObject userdata=result.getJSONObject(0);
                        String message=json.getString("message");
                        if(message.equals("Login Successfully")){
                            //   Getting Logged in User Data Returned From login API
                            String id=userdata.getString("_id");
                            String email=userdata.getString("Email");
                            String Password=userdata.getString("Password");
                            String InitLogin=userdata.getString("InitLogin");
                            String Role=userdata.getString("Role");
                            String city=userdata.getString("City");
                            String BusinessAddress=userdata.getString("BusinessAddress");
                            String ContactNumber=userdata.getString("ContactNumber");
                            String FirstName=userdata.getString("FirstName");
                            String LastName=userdata.getString("LastName");
                            String PostCode=userdata.getString("PostCode");
                            String SocialMedia=userdata.getString("SocialMedia");
                            String Filling=userdata.getString("Filling");
                            String BusinessName=userdata.getString("BusinessName");
                            String image=userdata.getString("Image");

                            //Preference to Store User Credentials
                            SharedPreferences sharedpreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();

                            editor.putBoolean("Registered", true);
                            editor.putBoolean("islogged", true);
                            editor.putString("Image",image);
                            editor.putString("id", id);
                            editor.putString("email", email);
                            editor.putString("Password", Password);
                            editor.putString("InitLogin", InitLogin);
                            editor.putString("City",city);
                            editor.putString("Role", Role);
                            editor.putString("BusinessAddress", BusinessAddress);
                            editor.putString("ContactNumber", ContactNumber);
                            editor.putString("FirstName", FirstName);
                            editor.putString("LastName", LastName);
                            editor.putString("PostCode", PostCode);
                            editor.putString("SocialMedia", SocialMedia);
                            editor.putString("Filling", Filling);
                            editor.putString("BusinessName", BusinessName);
                            editor.apply();

                            //Toast.makeText(getApplicationContext(), "Login Succesfully", Toast.LENGTH_SHORT).show();
                            //Intent intent=new Intent(personalsettings_user.this, maindashboard.class);
                            //startActivity(intent);
                        }
                        else{
                            //Toast.makeText(getApplicationContext(), "Incorrect email or password", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        //Toast.makeText(getApplicationContext(), "Incorrect email or password", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    //Toast.makeText(getApplicationContext(), "Incorrect email or password", Toast.LENGTH_SHORT).show();
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

}