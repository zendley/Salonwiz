package com.speckpro.salonwiz.ui.filingcabinet;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.speckpro.salonwiz.R;
import com.speckpro.salonwiz.adapters.UtilitiesAdapter;
import com.speckpro.salonwiz.models.UtilityModel;
import com.speckpro.salonwiz.newmodels.FilingModel;
import com.speckpro.salonwiz.retrofit.ApiClient;
import com.speckpro.salonwiz.retrofit.ApiService;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;

public class UtilityBillsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewBills;
    private UtilitiesAdapter utilitiesAdapter;
    private ArrayList<FilingModel> billsArrayList;

    private ProgressBar progressBar;
    private ApiService apiService;
    private TextView textViewNoData;
    SharedPreferences sharedpreferences;
    String token;

    private DatePickerDialog datePickerDialog;
    private TextView dateButton;
    private Button generalupload;
    private EditText search;
    private RecyclerView courseRV,courseRVs;
    public static final int REQUEST_CODE = 101;
    private final ArrayList<UtilityModel> data= new ArrayList<>();
    private String syear,smonth,sday,scompdate;
    private UtilitiesAdapter courseAdapter,courseAdapters;
    private String email;

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
        setContentView(R.layout.activity_utility_bills);

        apiService = ApiClient.getClient().create(ApiService.class);
        progressBar = findViewById(R.id.progressBar_filingbills);
        textViewNoData = findViewById(R.id.textView_noDataBills);
        recyclerViewBills = findViewById(R.id.recyclerview_utilityBills);

        sharedpreferences =  getSharedPreferences("MySalonSharedPref", MODE_PRIVATE);
        token = sharedpreferences.getString("token", "");
        billsArrayList = new ArrayList();

        dateButton = findViewById(R.id.datePickerButton);
        String searchdate=dateButton.getText().toString();
        search=findViewById(R.id.utilitiesbill_searchtext);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
            }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // getdata(email,true);
            }
            @Override
            public void afterTextChanged(Editable arg0) {
                //  getdata(email,true);
                if(arg0.length()>0) {
                    String searchtext=search.getText().toString();
                    searchtext=searchtext.toLowerCase();
                   // getUtilityFilingsSearch(searchtext);

                    initDatePicker();
                } else {
                    initDatePicker();
                    // getUtilityFilings();
                }
            }
        });

        generalupload=findViewById(R.id.utilitybills_uploadbutton);
        generalupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent s = new Intent(UtilityBillsActivity.this, AddUtilityBills.class);
                startActivity(s);
            }
        });

        Button uplaodfiletoolbar=findViewById(R.id.toolbar_uploadfile);
        uplaodfiletoolbar.setVisibility(View.VISIBLE);
        uplaodfiletoolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent s = new Intent(UtilityBillsActivity.this, AddUtilityBills.class);
                startActivity(s);
            }
        });

        TextView textView =findViewById(R.id.toolbar_titletext);
        textView.setText("Utility Bills");
        ImageView backarrow=findViewById(R.id.toolbar_backarrow);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        dateButton.setText(getTodaysDate());

        initDatePicker();

        //getUtilityFilings();
    }

    private String getTodaysDate()
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeFullDateString(day,month,year);
    }

    private void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                String date =  makeFullDateString(day, month, year);
                dateButton.setText(date);
                syear=String.valueOf(year);
                smonth=formatdoubledigit(month);
                if(day<10)
                    sday=formatdoubledigit(day);
                else
                    sday=String.valueOf(day);

                scompdate=makeFullDateString(day, Integer.parseInt(smonth),year);
                getUtilityFilingsSearchCalendar(scompdate, search.getText().toString().toLowerCase(), true);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, day, month);
        syear=String.valueOf(year);
        smonth=formatdoubledigit(month);
        if(day<10)
            sday=formatdoubledigit(day);
        else
            sday=String.valueOf(day);

        scompdate=makeFullDateString(day, Integer.parseInt(smonth),year);

        getUtilityFilingsSearchCalendar(scompdate, search.getText().toString().toLowerCase(), true);
    }

    public void openDatePicker(View view)
    {
        datePickerDialog.show();
    }

    private void getUtilityFilings(){
        billsArrayList.clear();
        progressBar.setVisibility(View.VISIBLE);
        textViewNoData.setVisibility(View.GONE);
        recyclerViewBills.setVisibility(View.GONE);

        Call<ArrayList<FilingModel>> call = apiService.getAllFilings( "application/json", "Bearer "+token, "Utility");
        call.enqueue(new Callback<ArrayList<FilingModel>>() {
            @Override
            public void onResponse(Call<ArrayList<FilingModel>> call, retrofit2.Response<ArrayList<FilingModel>> response) {
                if(response.isSuccessful()){
                    assert response.body() != null;
                    for (int i = 0; i < response.body().size(); i++) {
                        billsArrayList.add(new FilingModel(
                                response.body().get(i).getId(),
                                response.body().get(i).getUserId(),
                                response.body().get(i).getTitle(),
                                response.body().get(i).getCategory(),
                                response.body().get(i).getImage(),
                                response.body().get(i).getCreatedAt(),
                                response.body().get(i).getUpdatedAt()));
                    }
                    if (billsArrayList!=null && billsArrayList.isEmpty()) {
                        progressBar.setVisibility(View.GONE);
                        textViewNoData.setVisibility(View.VISIBLE);
                        recyclerViewBills.setVisibility(View.GONE);
                        textViewNoData.setText("No Data Uploaded!");
                    } else {
                        progressBar.setVisibility(View.GONE);
                        textViewNoData.setVisibility(View.GONE);
                        recyclerViewBills.setVisibility(View.VISIBLE);

                        recyclerViewBills.setLayoutManager(new GridLayoutManager(UtilityBillsActivity.this,2));
                        utilitiesAdapter = new UtilitiesAdapter(UtilityBillsActivity.this, billsArrayList);
                        recyclerViewBills.setAdapter(utilitiesAdapter);
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    textViewNoData.setVisibility(View.VISIBLE);
                    recyclerViewBills.setVisibility(View.GONE);
                    textViewNoData.setText("Something went Wrong, Couldn't load Data!");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<FilingModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                recyclerViewBills.setVisibility(View.GONE);
                textViewNoData.setVisibility(View.VISIBLE);

                textViewNoData.setText("Check your internet connection, Load Data Failed!");

            }
        });
    }

    private void getUtilityFilingsSearch(String searchtext){
        billsArrayList.clear();
        progressBar.setVisibility(View.VISIBLE);
        textViewNoData.setVisibility(View.GONE);
        recyclerViewBills.setVisibility(View.GONE);

        Call<ArrayList<FilingModel>> call = apiService.getAllFilings( "application/json", "Bearer "+token, "Utiltity");
        call.enqueue(new Callback<ArrayList<FilingModel>>() {
            @Override
            public void onResponse(Call<ArrayList<FilingModel>> call, retrofit2.Response<ArrayList<FilingModel>> response) {
                if(response.isSuccessful()){
                    assert response.body() != null;
                    for (int i = 0; i < response.body().size(); i++) {
                        if (response.body().get(i).getTitle().toLowerCase().contains(searchtext)) {
                            billsArrayList.add(new FilingModel(
                                    response.body().get(i).getId(),
                                    response.body().get(i).getUserId(),
                                    response.body().get(i).getTitle(),
                                    response.body().get(i).getCategory(),
                                    response.body().get(i).getImage(),
                                    response.body().get(i).getCreatedAt(),
                                    response.body().get(i).getUpdatedAt()));
                        }
                    }
                    if (billsArrayList!=null && billsArrayList.isEmpty()) {
                        progressBar.setVisibility(View.GONE);
                        textViewNoData.setVisibility(View.VISIBLE);
                        recyclerViewBills.setVisibility(View.GONE);
                        textViewNoData.setText("No Data Uploaded Today!");
                    } else {
                        progressBar.setVisibility(View.GONE);
                        textViewNoData.setVisibility(View.GONE);
                        recyclerViewBills.setVisibility(View.VISIBLE);

                        recyclerViewBills.setLayoutManager(new GridLayoutManager(UtilityBillsActivity.this,2));
                        utilitiesAdapter = new UtilitiesAdapter(UtilityBillsActivity.this, billsArrayList);
                        recyclerViewBills.setAdapter(utilitiesAdapter);
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    textViewNoData.setVisibility(View.VISIBLE);
                    recyclerViewBills.setVisibility(View.GONE);
                    textViewNoData.setText("Something went Wrong, Couldn't load Data!");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<FilingModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                recyclerViewBills.setVisibility(View.GONE);
                textViewNoData.setVisibility(View.VISIBLE);

                textViewNoData.setText("Check your internet connection, Load Data Failed!");
            }
        });
    }

    private void getUtilityFilingsSearchCalendar(String scompdate, String searchtext, boolean b){
        billsArrayList.clear();
        progressBar.setVisibility(View.VISIBLE);
        textViewNoData.setVisibility(View.GONE);
        recyclerViewBills.setVisibility(View.GONE);

        Call<ArrayList<FilingModel>> call = apiService.getAllFilings( "application/json", "Bearer "+token, "Utiltity");
        call.enqueue(new Callback<ArrayList<FilingModel>>() {
            @Override
            public void onResponse(Call<ArrayList<FilingModel>> call, retrofit2.Response<ArrayList<FilingModel>> response) {
                if(response.isSuccessful()){
                    assert response.body() != null;
                        billsArrayList.clear();
                        for (int i = 0; i < response.body().size(); i++) {
                            String cdate = response.body().get(i).getCreatedAt();
                            cdate = cdate.substring(0, 10);
                            if (cdate.equals(scompdate)) {
                                if (!searchtext.isEmpty() && response.body().get(i).getTitle().toLowerCase().contains(searchtext)) {
                                    billsArrayList.add(new FilingModel(
                                            response.body().get(i).getId(),
                                            response.body().get(i).getUserId(),
                                            response.body().get(i).getTitle(),
                                            response.body().get(i).getCategory(),
                                            response.body().get(i).getImage(),
                                            response.body().get(i).getCreatedAt(),
                                            response.body().get(i).getUpdatedAt()));
                                } else {
                                    billsArrayList.add(new FilingModel(
                                            response.body().get(i).getId(),
                                            response.body().get(i).getUserId(),
                                            response.body().get(i).getTitle(),
                                            response.body().get(i).getCategory(),
                                            response.body().get(i).getImage(),
                                            response.body().get(i).getCreatedAt(),
                                            response.body().get(i).getUpdatedAt()));
                                }
                            }
                        }
//                    }
                    if (billsArrayList!=null && billsArrayList.isEmpty()) {
                        progressBar.setVisibility(View.GONE);
                        textViewNoData.setVisibility(View.VISIBLE);
                        recyclerViewBills.setVisibility(View.GONE);
                        if(scompdate.equals(getTodaysDate())) {
                            textViewNoData.setText("No Data Uploaded Today!");
                        } else {
                            textViewNoData.setText("No Data Uploaded On "+scompdate);
                        }
                    } else {
                        progressBar.setVisibility(View.GONE);
                        textViewNoData.setVisibility(View.GONE);
                        recyclerViewBills.setVisibility(View.VISIBLE);

                        recyclerViewBills.setLayoutManager(new GridLayoutManager(UtilityBillsActivity.this,2));
                        utilitiesAdapter = new UtilitiesAdapter(UtilityBillsActivity.this, billsArrayList);
                        recyclerViewBills.setAdapter(utilitiesAdapter);
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    textViewNoData.setVisibility(View.VISIBLE);
                    recyclerViewBills.setVisibility(View.GONE);
                    textViewNoData.setText("Something went Wrong, Couldn't load Data!");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<FilingModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                recyclerViewBills.setVisibility(View.GONE);
                textViewNoData.setVisibility(View.VISIBLE);

                textViewNoData.setText("Check your internet connection, Load Data Failed!");
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        smonth=formatdoubledigit(month);
        scompdate=makeFullDateString(day, Integer.parseInt(smonth),year);

        getUtilityFilingsSearchCalendar(scompdate, search.getText().toString(), true);
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

    private String makeFullDateString(int day, int month, int year)
    {
        return  year+"-"+formatdoubledigit(month)+"-"+formatdoubledigit(day);
    }

    private String makeDateString(int day, int month, int year)
    {
        return getMonthFormat(month) + " " + year;
    }

    private String formatdoubledigit(int month){
        if(month == 1)
            return "01";
        if(month == 2)
            return "02";
        if(month == 3)
            return "03";
        if(month == 4)
            return "04";
        if(month == 5)
            return "05";
        if(month == 6)
            return "06";
        if(month == 7)
            return "07";
        if(month == 8)
            return "08";
        if(month == 9)
            return "09";
        if(month == 10)
            return "10";
//        if(month == 11)
//            return "11";

        return String.valueOf(month);
    }
    private String getMonthFormat(int month)
    {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";

        //default should never happen
        return "JAN";
    }

}