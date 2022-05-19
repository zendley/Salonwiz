package com.example.salonwiz.afterlogin.filingcabinet.Utilititesinput;

import static com.example.salonwiz.BaseUrl.baseurl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.salonwiz.R;


import java.io.File;
import java.util.Calendar;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Selectedcards extends AppCompatActivity {
    private CardView gascheck,wastecheck,electriccheck,insurencecheck,telephonecheck,broadbandcheck,watercheck,cardtermcheck;
    private ImageView gasutil,wasteutil,electricutil,insurenceutil,telephoneutil,broadbandutil,waterutil,cardtermutil;
    private TextView gastitle,wastetitle,electrictitle,insurencetitle,telephonetitle,broadbandtitle,watertitle,cardtermtitle;
    private DatePickerDialog datePickerDialog;
    private Button popupsubmit;
    private ImageView latestbill,loaform;
    private final int GALLERY = 1;
    private String currentPhotoPath;
    private static final String IMAGE_DIRECTORY = "/demonuts_upload_gallery";
    private int i=0;
    private String postexpdate,isPaid;
    private String Patha,Pathb;
    private File photofilelatest;
    private File photofileloa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectedcards);

        boolean gasStatus = getIntent().getExtras().getBoolean("gasStatus");
        boolean wasteStatus = getIntent().getExtras().getBoolean("wasteStatus");
        boolean electricStatus = getIntent().getExtras().getBoolean("electricStatus");
        boolean insurenceStatus = getIntent().getExtras().getBoolean("insurenceStatus");
        boolean telephoneStatus = getIntent().getExtras().getBoolean("telephoneStatus");
        boolean broadbandStatus = getIntent().getExtras().getBoolean("broadbandStatus");
        boolean waterStatus = getIntent().getExtras().getBoolean("waterStatus");
        boolean cardtermStatus = getIntent().getExtras().getBoolean("cardtermStatus");

        gascheck=findViewById(R.id.ugas_card);
        wastecheck=findViewById(R.id.uwaste_card);
        electriccheck=findViewById(R.id.uelectric_card);
        insurencecheck=findViewById(R.id.uinsurence_card);
        telephonecheck=findViewById(R.id.utelephone_card);
        broadbandcheck=findViewById(R.id.ubroadband_card);
        watercheck=findViewById(R.id.udrinking_card);
        cardtermcheck=findViewById(R.id.ucardterminal_card);

        gasutil=findViewById(R.id.sutil_gas);
        wasteutil=findViewById(R.id.sutil_waste);
        electricutil=findViewById(R.id.sutil_electric);
        insurenceutil=findViewById(R.id.sutil_insurence);
        telephoneutil=findViewById(R.id.sutil_telephone);
        broadbandutil=findViewById(R.id.sutil_broadband);
        waterutil=findViewById(R.id.sutil_drinkingwater);
        cardtermutil=findViewById(R.id.sutil_cardterm);

        gastitle=findViewById(R.id.sutil_gastitle);
        wastetitle=findViewById(R.id.sutil_wastetitle);
        electrictitle=findViewById(R.id.sutil_electrictitle);
        insurencetitle=findViewById(R.id.sutil_insurencetitle);
        telephonetitle=findViewById(R.id.sutil_telephonetitle);
        broadbandtitle=findViewById(R.id.sutil_broadbandtitle);
        watertitle=findViewById(R.id.sutil_drinkingtitle);
        cardtermtitle=findViewById(R.id.sutil_cardtermtitle);

        if(gasStatus){
            if(i==0){
                gasutil.setBackgroundResource(R.drawable.ic_gas);
                gastitle.setText("Gas");
                i++;

            }
            else if(i==1){
                electricutil.setBackgroundResource(R.drawable.ic_gas);
                electrictitle.setText("Gas");
                i++;

            }
            else if(i==2){
                insurenceutil.setBackgroundResource(R.drawable.ic_gas);
                insurencetitle.setText("Gas");
                i++;

            }
            else if(i==3){
                wasteutil.setBackgroundResource(R.drawable.ic_gas);
                wastetitle.setText("Gas");
                i++;
            }
            else if(i==4){
                telephoneutil.setBackgroundResource(R.drawable.ic_gas);
                telephonetitle.setText("Gas");
                i++;

            }
            else if(i==5){
                broadbandutil.setBackgroundResource(R.drawable.ic_gas);
                broadbandtitle.setText("Gas");
                i++;


            }

            else if(i==6){
                waterutil.setBackgroundResource(R.drawable.ic_gas);
                watertitle.setText("Gas");
                i++;

            }
            else if(i==7){
                cardtermutil.setBackgroundResource(R.drawable.ic_gas);
                cardtermtitle.setText("Gas");
                i++;
            }
        }

        if(electricStatus){
            if(i==0){
                gasutil.setBackgroundResource(R.drawable.ic_electric);
                gastitle.setText("Electric");
                i++;

            }
            else if(i==1){
                electricutil.setBackgroundResource(R.drawable.ic_electric);
                electrictitle.setText("Electric");
                i++;

            }
            else if(i==2){
                insurenceutil.setBackgroundResource(R.drawable.ic_electric);
                insurencetitle.setText("Electric");
                i++;

            }
            else if(i==3){
                wasteutil.setBackgroundResource(R.drawable.ic_electric);
                wastetitle.setText("Electric");
                i++;
            }
            else if(i==4){
                telephoneutil.setBackgroundResource(R.drawable.ic_electric);
                telephonetitle.setText("Electric");
                i++;

            }
            else if(i==5){
                broadbandutil.setBackgroundResource(R.drawable.ic_electric);
                broadbandtitle.setText("Electric");
                i++;


            }

            else if(i==6){
                waterutil.setBackgroundResource(R.drawable.ic_electric);
                watertitle.setText("Electric");
                i++;

            }
            else if(i==7){
                cardtermutil.setBackgroundResource(R.drawable.ic_electric);
                cardtermtitle.setText("Electric");
                i++;
            }
        }
        if(insurenceStatus){
            if(i==0){
                gasutil.setBackgroundResource(R.drawable.ic_insurence);
                gastitle.setText("Insurence Emp and Public");
                i++;

            }
            else if(i==1){
                electricutil.setBackgroundResource(R.drawable.ic_insurence);
                electrictitle.setText("Insurence Emp and Public");
                i++;

            }
            else if(i==2){
                insurenceutil.setBackgroundResource(R.drawable.ic_insurence);
                insurencetitle.setText("Insurence Emp and Public");
                i++;

            }
            else if(i==3){
                wasteutil.setBackgroundResource(R.drawable.ic_insurence);
                wastetitle.setText("Insurence Emp and Public");
                i++;
            }
            else if(i==4){
                telephoneutil.setBackgroundResource(R.drawable.ic_insurence);
                telephonetitle.setText("Insurence Emp and Public");
                i++;

            }
            else if(i==5){
                broadbandutil.setBackgroundResource(R.drawable.ic_insurence);
                broadbandtitle.setText("Insurence Emp and Public");
                i++;


            }

            else if(i==6){
                waterutil.setBackgroundResource(R.drawable.ic_insurence);
                watertitle.setText("Insurence Emp and Public");
                i++;

            }
            else if(i==7){
                cardtermutil.setBackgroundResource(R.drawable.ic_insurence);
                cardtermtitle.setText("Insurence Emp and Public");
                i++;
            }
        }
        if(wasteStatus){
            if(i==0){
                gasutil.setBackgroundResource(R.drawable.ic_waste);
                gastitle.setText("Waste Bin Disposal");
                i++;

            }
            else if(i==1){
                electricutil.setBackgroundResource(R.drawable.ic_waste);
                electrictitle.setText("Waste Bin Disposal");
                i++;

            }
            else if(i==2){
                insurenceutil.setBackgroundResource(R.drawable.ic_waste);
                insurencetitle.setText("Waste Bin Disposal");
                i++;

            }
            else if(i==3){
                wasteutil.setBackgroundResource(R.drawable.ic_waste);
                wastetitle.setText("Waste Bin Disposal");
                i++;
            }
            else if(i==4){
                telephoneutil.setBackgroundResource(R.drawable.ic_waste);
                telephonetitle.setText("Waste Bin Disposal");
                i++;

            }
            else if(i==5){
                broadbandutil.setBackgroundResource(R.drawable.ic_waste);
                broadbandtitle.setText("Waste Bin Disposal");
                i++;


            }

            else if(i==6){
                waterutil.setBackgroundResource(R.drawable.ic_waste);
                watertitle.setText("Waste Bin Disposal");
                i++;

            }
            else if(i==7){
                cardtermutil.setBackgroundResource(R.drawable.ic_waste);
                cardtermtitle.setText("Waste Bin Disposal");
                i++;
            }
        }
        if(telephoneStatus){
            if(i==0){
                gasutil.setBackgroundResource(R.drawable.ic_telephone);
                gastitle.setText("Telephone");
                i++;

            }
            else if(i==1){
                electricutil.setBackgroundResource(R.drawable.ic_telephone);
                electrictitle.setText("Telephone");
                i++;

            }
            else if(i==2){
                insurenceutil.setBackgroundResource(R.drawable.ic_telephone);
                insurencetitle.setText("Telephone");
                i++;

            }
            else if(i==3){
                wasteutil.setBackgroundResource(R.drawable.ic_telephone);
                wastetitle.setText("Telephone");
                i++;
            }
            else if(i==4){
                telephoneutil.setBackgroundResource(R.drawable.ic_telephone);
                telephonetitle.setText("Telephone");
                i++;

            }
            else if(i==5){
                broadbandutil.setBackgroundResource(R.drawable.ic_telephone);
                broadbandtitle.setText("Telephone");
                i++;


            }

            else if(i==6){
                waterutil.setBackgroundResource(R.drawable.ic_telephone);
                watertitle.setText("Telephone");
                i++;

            }
            else if(i==7){
                cardtermutil.setBackgroundResource(R.drawable.ic_telephone);
                cardtermtitle.setText("Telephone");
                i++;
            }
        }
        if(broadbandStatus){
            if(i==0){
                gasutil.setBackgroundResource(R.drawable.ic_broadband);
                gastitle.setText("Broadband");
                i++;

            }
            else if(i==1){
                electricutil.setBackgroundResource(R.drawable.ic_broadband);
                electrictitle.setText("Broadband");
                i++;

            }
            else if(i==2){
                insurenceutil.setBackgroundResource(R.drawable.ic_broadband);
                insurencetitle.setText("Broadband");
                i++;

            }
            else if(i==3){
                wasteutil.setBackgroundResource(R.drawable.ic_broadband);
                wastetitle.setText("Broadband");
                i++;
            }
            else if(i==4){
                telephoneutil.setBackgroundResource(R.drawable.ic_broadband);
                telephonetitle.setText("Broadband");
                i++;

            }
            else if(i==5){
                broadbandutil.setBackgroundResource(R.drawable.ic_broadband);
                broadbandtitle.setText("Broadband");
                i++;


            }

            else if(i==6){
                waterutil.setBackgroundResource(R.drawable.ic_broadband);
                watertitle.setText("Broadband");
                i++;

            }
            else if(i==7){
                cardtermutil.setBackgroundResource(R.drawable.ic_broadband);
                cardtermtitle.setText("Broadband");
                i++;
            }
        }
        if(waterStatus){
            if(i==0){
                gasutil.setBackgroundResource(R.drawable.ic_drinkingwater);
                gastitle.setText("Drinking Water Supply and Waste");
                i++;

            }
            else if(i==1){
                electricutil.setBackgroundResource(R.drawable.ic_drinkingwater);
                electrictitle.setText("Drinking Water Supply and Waste");
                i++;

            }
            else if(i==2){
                insurenceutil.setBackgroundResource(R.drawable.ic_drinkingwater);
                insurencetitle.setText("Drinking Water Supply and Waste");
                i++;

            }
            else if(i==3){
                wasteutil.setBackgroundResource(R.drawable.ic_drinkingwater);
                wastetitle.setText("Drinking Water Supply and Waste");
                i++;
            }
            else if(i==4){
                telephoneutil.setBackgroundResource(R.drawable.ic_drinkingwater);
                telephonetitle.setText("Drinking Water Supply and Waste");
                i++;

            }
            else if(i==5){
                broadbandutil.setBackgroundResource(R.drawable.ic_drinkingwater);
                broadbandtitle.setText("Drinking Water Supply and Waste");
                i++;


            }

            else if(i==6){
                waterutil.setBackgroundResource(R.drawable.ic_drinkingwater);
                watertitle.setText("Drinking Water Supply and Waste");
                i++;

            }
            else if(i==7){
                cardtermutil.setBackgroundResource(R.drawable.ic_drinkingwater);
                cardtermtitle.setText("Drinking Water Supply and Waste");
                i++;
            }
        }
        if(cardtermStatus){
            if(i==0){
                gasutil.setBackgroundResource(R.drawable.ic_cardterminal);
                gastitle.setText("Card Terminal");
                i++;

            }
            else if(i==1){
                electricutil.setBackgroundResource(R.drawable.ic_cardterminal);
                electrictitle.setText("Card Terminal");
                i++;

            }
            else if(i==2){
                insurenceutil.setBackgroundResource(R.drawable.ic_cardterminal);
                insurencetitle.setText("Card Terminal");
                i++;

            }
            else if(i==3){
                wasteutil.setBackgroundResource(R.drawable.ic_cardterminal);
                wastetitle.setText("Card Terminal");
                i++;
            }
            else if(i==4){
                telephoneutil.setBackgroundResource(R.drawable.ic_cardterminal);
                telephonetitle.setText("Card Terminal");
                i++;

            }
            else if(i==5){
                broadbandutil.setBackgroundResource(R.drawable.ic_cardterminal);
                broadbandtitle.setText("Card Terminal");
                i++;


            }

            else if(i==6){
                waterutil.setBackgroundResource(R.drawable.ic_cardterminal);
                watertitle.setText("Card Terminal");
                i++;

            }
            else if(i==7){
                cardtermutil.setBackgroundResource(R.drawable.ic_cardterminal);
                cardtermtitle.setText("Card Terminal");
                i++;
            }
        }
        if(i==1){
            electriccheck.setVisibility(View.GONE);
            insurencecheck.setVisibility(View.GONE);
            wastecheck.setVisibility(View.GONE);
            telephonecheck.setVisibility(View.GONE);
            broadbandcheck.setVisibility(View.GONE);
            watercheck.setVisibility(View.GONE);
            cardtermcheck.setVisibility(View.GONE);

        }
        if(i==2){
            insurencecheck.setVisibility(View.GONE);
            wastecheck.setVisibility(View.GONE);
            telephonecheck.setVisibility(View.GONE);
            broadbandcheck.setVisibility(View.GONE);
            watercheck.setVisibility(View.GONE);
            cardtermcheck.setVisibility(View.GONE);

        }
        if(i==3){

            wastecheck.setVisibility(View.GONE);
            telephonecheck.setVisibility(View.GONE);
            broadbandcheck.setVisibility(View.GONE);
            watercheck.setVisibility(View.GONE);
            cardtermcheck.setVisibility(View.GONE);

        }
        if(i==4){

            telephonecheck.setVisibility(View.GONE);
            broadbandcheck.setVisibility(View.GONE);
            watercheck.setVisibility(View.GONE);
            cardtermcheck.setVisibility(View.GONE);

        }
        if(i==5){

            broadbandcheck.setVisibility(View.GONE);
            watercheck.setVisibility(View.GONE);
            cardtermcheck.setVisibility(View.GONE);

        }
        if(i==6){

            watercheck.setVisibility(View.GONE);
            cardtermcheck.setVisibility(View.GONE);

        }
        if(i==7){

            cardtermcheck.setVisibility(View.GONE);

        }






        gascheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                popup(gastitle.getText().toString());



            }
        });
        electriccheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                popup(electrictitle.getText().toString());



            }
        });
        insurencecheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                popup(insurencetitle.getText().toString());



            }
        });
        wastecheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                popup(wastetitle.getText().toString());



            }
        });
        telephonecheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                popup(telephonetitle.getText().toString());



            }
        });
        broadbandcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                popup(broadbandtitle.getText().toString());



            }
        });
        watercheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                popup(watertitle.getText().toString());



            }
        });
        cardtermcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                popup(cardtermtitle.getText().toString());



            }
        });

    }

    public void popup(String title){

// Retrieving the value using its keys the file name
// must be same in both saving and retrieving the data
        SharedPreferences sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);

// The value will be default as empty string because for
// the very first time when the app is opened, there is nothing to show
        String id = sh.getString("id", "");

// We can then use the data
     //   name.setText(s1);
    //    age.setText(String.valueOf(a));

        final Dialog dialog = new Dialog(Selectedcards.this);
        dialog.setContentView(R.layout.popup_getutilitydetails);
        RadioButton simpleRadioButton=dialog.findViewById(R.id.popup_latestbillradio1);


        Spinner supplier=dialog.findViewById(R.id.popup_utilsupplierlist);
        TextView titl=dialog.findViewById(R.id.popup_titleutil);
        popupsubmit=dialog.findViewById(R.id.popup_utilitysubmit);
        titl.setText(title);
        TextView expdate=dialog.findViewById(R.id.popup_utilcontractexpdate);
        TextView latestbillpath=dialog.findViewById(R.id.popup_latestbillimagetextpath);
        latestbill=dialog.findViewById(R.id.popup_latestbillimage);
        loaform=dialog.findViewById(R.id.popup_loaformimage);

        popupsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPaid="";
                if(simpleRadioButton.isChecked()){
                    isPaid="true";
                }else{
                    isPaid="false";
                }
                Log.v("Data For Popup Post",Patha+" "+Pathb+" "+title+supplier.getSelectedItem().toString()+id+expdate.getText().toString()+isPaid);
                postpopupData(Patha,Pathb,title,supplier.getSelectedItem().toString(),id,expdate.getText().toString(),isPaid);
            }
        });
        loaform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                //intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
                //intent.setType("image/*");
                //startActivityForResult(Intent.createChooser(intent, "Select Picture"),2);
               // dispatchTakePictureIntent(photofilelatest,"1");
                Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2);

            }
        });
        latestbill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);

                //    Intent myfileintent=new Intent(Intent.ACTION_GET_CONTENT);
              //  myfileintent.setType("*/*");
              //  startActivityForResult(myfileintent,10);
               /* Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);*/


             /*   Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(galleryIntent, 1);*/
              //  dispatchTakePictureIntent(photofileloa,"2");


            }
        });
        expdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(v.getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                expdate.setText((monthOfYear + 1)+ "/"
                                        + dayOfMonth  + "/" + year);
                                postexpdate=expdate.getText().toString();
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });








        dialog.show();
    }

    private void postpopupData(String image1path,String image2path,String title,String supplier,String userid,String expdate,String ispaid) {

        // below line is for displaying our progress bar.
        Log.v("Data For Popup Post",image1path+" "+image2path+" "+title+supplier+userid+expdate+ispaid);

        // on below line we are creating a retrofit
        // builder and passing our base url
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseurl)
                // as we are sending data in json format so
                // we have to add Gson converter factory
                .addConverterFactory(GsonConverterFactory.create())
                // at last we are building our retrofit builder.
                .build();
        //pass it like this
        File image1a = new File(image1path);
        File image2a = new File(image2path);
        RequestBody bill =
                RequestBody.create(MediaType.parse("multipart/form-data"), image1a);

        RequestBody form =
                RequestBody.create(MediaType.parse("multipart/form-data"), image2a);

// MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part image1=MultipartBody.Part.createFormData("image",image1a.getName(),bill);
        MultipartBody.Part image2=MultipartBody.Part.createFormData("image",image2a.getName(),form);
// add another part within the multipart request
        RequestBody User =
                RequestBody.create(MediaType.parse("multipart/form-data"), userid);

        RequestBody UtilitiesTitle =
                RequestBody.create(MediaType.parse("multipart/form-data"), title);
        RequestBody UtilitiesSupplier =
                RequestBody.create(MediaType.parse("multipart/form-data"), supplier);
        RequestBody ContractExpiryDate =
                RequestBody.create(MediaType.parse("multipart/form-data"), expdate);
        RequestBody IsPaid=
                RequestBody.create(MediaType.parse("multipart/form-data"), isPaid);

    //    service.updateProfile(id, fullName, body, other);
        // below line is to create an instance for our retrofit api class.
        userutilitiesretrofitapi retrofitAPI = retrofit.create(userutilitiesretrofitapi.class);

        // passing data from our text fields to our modal class.
       // registermodelclass modal = new registermodelclass(sfname, slname,semail,spass,spnumber,sbuisnessname,sbuisnessaddress,postcode);

        // calling a method to create a post and passing our modal class.
        Call<ResponseBody> call = retrofitAPI.useradd(User,UtilitiesTitle,UtilitiesSupplier,ContractExpiryDate,IsPaid,image1,image2);

        // on below line we are executing our method.
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // this method is called when we get response from our api.
                if(response!=null){
                Toast.makeText(Selectedcards.this, "Data added to API", Toast.LENGTH_SHORT).show();
                Log.d("TAG", "onResponse: "+response.body());
                Log.d("Response Message", "onResponse: "+response.message());
                }else{
                    Toast.makeText(Selectedcards.this, "Data not added,Contact to Speckpro", Toast.LENGTH_SHORT).show();
                }
              //  Log.d("TAG", "onResponse: meta: " + response.body().getMeta().getStatus());
                // below line is for hiding our progress bar.
                //loadingPB.setVisibility(View.GONE);

                // on below line we are setting empty text
                // to our both edit text.
                // fname.setText("");
                // nameEdt.setText("");

                // we are getting response from our body
                // and passing it to our modal class.
         //       registermodelclass responseFromAPI = response.body();

                // on below line we are getting our data from modal class and adding it to our string.
                // String responseString = "Response Code : " + response.code() + "\nFirstName : " + responseFromAPI.getFirstName() + "\n" + "LastName : " + responseFromAPI.getLastName()+ "\n" + "Email : " + responseFromAPI.getEmail()+ "\n" + "Password : " + responseFromAPI.getPassword()+ "\n" + "ContactNumber : " + responseFromAPI.getContactNumber()+ "\n" + "BusinessName : " + responseFromAPI.getBusinessName()+ "\n" + "BusinessAddress : " + responseFromAPI.getBusinessAddress()+ "\n" + "PostCode : " + responseFromAPI.getPostCode();

                // below line we are setting our
                // string to our text view.
                // responseTV.setText(responseString);
            }


            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // setting text to our text view when
                // we get error response from API.
                // responseTV.setText("Error found is : " + t.getMessage());
                Toast.makeText(Selectedcards.this, "Failed"+t.getMessage(), Toast.LENGTH_SHORT).show();
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
                 //   Bitmap bitmap= BitmapFactory.decodeFile(pathToFile);


                    Uri selectedImage = data.getData();
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



                   // Uri selectedImageUri = data.getData();
                 //   Patha=getRealPathFromURI(selectedImageUri);
                    /*
                    // Get the path from the Uri
                    String path="";
                    path=selectedImageUri.getEncodedPath();
                  //  Log.v("Path",path);
                   // Log.e("path",path);

                    if (path != null) {
                        File f = new File(path);
                        photofilelatest= new File((new File(path)).getAbsolutePath());
                       Patha=(new File(path)).getAbsolutePath();
                        selectedImageUri = Uri.fromFile(f);
                    }*/

                   // Log.e("Patha",Patha);
                  //  Log.e("actualpath",path);
                //    Log.e("actualpath",selectedImageUri.getPath());

             //       latestbill.setImageURI(selectedImageUri);
                    // Set the image in ImageView
                   // ImageView((ImageView) findViewById(R.id.imgView)).setImageURI(selectedImageUri);

                   /* if (data != null) {
                        Uri contentURI = data.getData();
                        try {

                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                            // imageView.setImageBitmap(bitmap);
                           // String path = saveImage(bitmap);
                            String path= (String) data.getExtras().get("data");
                            Patha=path;

                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(Selectedcards.this, "Failed!", Toast.LENGTH_SHORT).show();
                        }


                    }*/
                }}
                if (requestCode == 2) {
                    if (resultCode == RESULT_OK) {


                      //  Uri selectedImageUri = data.getData();
                       // Pathb=getRealPathFromURI(selectedImageUri);
                        // Get the path from the Uri
                      /*  String path="";
                        path=selectedImageUri.getEncodedPath();
                        //  Log.v("Path",path);
                        // Log.e("path",path);

                        if (path != null) {
                            File f = new File(path);
                            photofileloa=new File(path);
                            selectedImageUri = Uri.fromFile(f);
                        }
                        Pathb=(new File(path)).getAbsolutePath();
                        // Log.e("Patha",Patha);
                        //  Log.e("actualpath",path);
                        //    Log.e("actualpath",selectedImageUri.getPath());

                        latestbill.setImageURI(selectedImageUri);*/
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
                      //  loaform.setImageURI(selectedImageUri);
                        /* Uri selectedImageUri = data.getData();
                        // Get the path from the Uri

                        //  final String path = getPath(Selectedcards.this,selectedImageUri);
                        String path = "";
                    //    path = getPath(selectedImageUri);
                        //    Log.e("Path",path);
                        if (path != null) {
                            File f = new File(path);
                            selectedImageUri = Uri.fromFile(f);
                        }

                        Pathb = selectedImageUri.toString();

                        //   Log.e("Pathb",Pathb);
                        //    Log.e("actualpath",selectedImageUri.getPath());

                        // Set the image in ImageView
                        // ImageView((ImageView) findViewById(R.id.imgView)).setImageURI(selectedImageUri);*/
                    }
                }

            } catch(Exception e){
                Log.e("FileSelectorActivity", "File select error", e);
            }
        }
    /*public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }*/
    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
      //  Cursor cursor = getApplicationContext().getContentResolver().query(MediaStore.Images.Media.INTERNAL_CONTENT_URI,
         //       column, sel, new String[]{id}, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }



    }

