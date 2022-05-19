package com.example.salonwiz.afterlogin.UI.homefrag_utilitydeals;

import static com.example.salonwiz.BaseUrl.baseurl;
import static com.facebook.FacebookSdk.getApplicationContext;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.salonwiz.GlideApp;
import com.example.salonwiz.R;
import com.example.salonwiz.afterlogin.filingcabinet.Utilititesinput.card_model;
import com.example.salonwiz.afterlogin.filingcabinet.Utilititesinput.utilities_inputselection;
import com.example.salonwiz.afterlogin.maindashboard;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class activeadapter extends RecyclerView.Adapter<activeadapter.Viewholder>{

    private final Context context;
    private final ArrayList<card_model> courseModelArrayList;
    private RecyclerView courseRV;
    private Boolean checkdeallist=false;
    private getdeals_adapter mAdapter;
    private ArrayList<model_getdeals> data;
    private ArrayList<model_getdeals> datas;
    DatePickerDialog datePickerDialog;

    public activeadapter(Context context, ArrayList<card_model> courseModelArrayList) {
        this.context = context;
        this.courseModelArrayList = courseModelArrayList;
    }

    @NonNull
    @Override
    public activeadapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.utilities_cardlayout, parent, false);
        return new activeadapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull activeadapter.Viewholder holder, int position) {
        // to set data to textview and imageview of each card layout
        card_model model = courseModelArrayList.get(position);
        holder.fillform.setText("Get Deal");
        holder.fillform.setClickable(true);
        holder.fillform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sh = view.getContext().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
                String id = sh.getString("id", "");
                checkgetlist(model.getText(),id,view);
                if(checkdeallist){
                final Dialog dialog = new Dialog(view.getContext());
                //    dialog.setCancelable(false)
                dialog.setContentView(R.layout.popup_getdeal);
                TextView atitle,adescription;
                atitle=dialog.findViewById(R.id.deal_acceptedtitle);
                adescription=dialog.findViewById(R.id.deal_Accepteddescription);

                RecyclerView alist=dialog.findViewById(R.id.getdealrecycler);
                ImageView close= dialog.findViewById(R.id.get_dealpopupexit);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                getlist(model.getText(),id,view,dialog,atitle,adescription);
                checkdeallist=false;
                dialog.show();
                }else
                {
                    Toast.makeText(getApplicationContext(), "No Deals Available", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.text.setText(model.getText());
        GlideApp.with(holder.itemView.getContext()).load(model.getDrawable()).apply(RequestOptions.centerCropTransform()).into(holder.drawable);
    }


    @Override
    public int getItemCount() {
        return courseModelArrayList.size();
    }

    public String getPath(Uri uri, View view) {

        String path = null;
        String[] projection = {MediaStore.Files.FileColumns.DATA};
        Cursor cursor = view.getContext().getContentResolver().query(uri, projection, null, null, null);

        if (cursor == null) {
            path = uri.getPath();
        } else {
            cursor.moveToFirst();
            int column_index = cursor.getColumnIndexOrThrow(projection[0]);
            path = cursor.getString(column_index);
            cursor.close();
        }

        return ((path == null || path.isEmpty()) ? (uri.getPath()) : path);
    }

    public void checkgetlist(String stitle,String User,View rootView){
        final ProgressDialog progressDialog = new ProgressDialog(rootView.getContext());
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait"); // set message
        progressDialog.show(); // show progress dialog

        String url22=baseurl+"api/Userutilities/getUtilitiesOfOneUser";
        try {
            JSONObject object12=new JSONObject();
            object12.put("User",User);
            RequestQueue mqueue42 = Volley.newRequestQueue(getApplicationContext());
            JsonObjectRequest jsonObjRequest4 = new JsonObjectRequest(Request.Method.POST,
                    url22, object12, new com.android.volley.Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("myapp", "the response is" + response.toString());
                    try {

                        JSONObject json = new JSONObject(String.valueOf(response));
                        JSONArray result = json.getJSONArray("active");
                        datas= new ArrayList<>();
                        String message=json.getString("message");
                        if(message.equals("Utility Found for this User")){
                            for (int i=0;i<result.length();i++){
                                JSONObject userdata=result.getJSONObject(i);
                                JSONObject utilities=userdata.getJSONObject("Utilities");
                                JSONObject userutil=userdata.getJSONObject("userUtility");
                                JSONArray deallist=userutil.getJSONArray("DealList");
                                String title = utilities.getString("Title");
                                if(title.equals(stitle)){


                                    checkdeallist= deallist.length() > 0;




                                }
                            }
                        }
                        else{
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "No Deals Avaialble Yet else", Toast.LENGTH_SHORT).show();


                        }
                        progressDialog.dismiss();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "No Deals Avaialble Yet", Toast.LENGTH_SHORT).show();

                    }
                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) { @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
            };
            mqueue42.add(jsonObjRequest4);
        } catch (JSONException jsonException) {
            progressDialog.dismiss();
            jsonException.printStackTrace();
            Toast.makeText(getApplicationContext(), jsonException.toString(), Toast.LENGTH_SHORT).show();

        }
    }

    public void getlist(String stitle,String User,View rootView,Dialog dialog,TextView t,TextView d){

        final ProgressDialog progressDialog = new ProgressDialog(rootView.getContext());
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
                        JSONArray result = json.getJSONArray("active");
                        data= new ArrayList<>();
                        String message=json.getString("message");
                        if(message.equals("Utility Found for this User")){
                            for (int i=0;i<result.length();i++){
                                JSONObject userdata=result.getJSONObject(i);
                                JSONObject utilities=userdata.getJSONObject("Utilities");
                                JSONObject userutil=userdata.getJSONObject("userUtility");
                                JSONArray deallist=userutil.getJSONArray("DealList");
                                    String title = utilities.getString("Title");
                                    if(title.equals(stitle)){

                                        model_getdeals st;

                                        for (int c=0;c<deallist.length();c++){
                                        JSONObject userdatas=deallist.getJSONObject(c);
                                        String ctitle=userdatas.getString("Title");
                                        String cdescription=userdatas.getString("Description");
                                        String cid=userutil.getString("_id");
                                        st = new model_getdeals(ctitle,cdescription,cid);
                                        data.add(st);


                                    }
                                        if (userutil.has("Deal")) {
                                            JSONObject deal=userutil.getJSONObject("Deal");
                                            String tit=deal.getString("Title");
                                            String desc=deal.getString("Description");
                                            t.setText(tit);
                                            d.setText(desc);
                                        }

                                    }
                            }
                                    courseRV = dialog.findViewById(R.id.getdealrecycler);
                                    courseRV.setHasFixedSize(true);
                                    courseRV.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
                                    mAdapter = new getdeals_adapter(rootView.getContext(), data);
                                    courseRV.setAdapter(mAdapter);
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "No Deals Avaialble Yet else", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "No Deals Avaialble Yet", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getApplicationContext(), jsonException.toString(), Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
    }
    public class Viewholder extends RecyclerView.ViewHolder {
        private final ImageView drawable;
        private final TextView text;
        private final CardView card;
        private final Button fillform;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            drawable = itemView.findViewById(R.id.util_cardimage);
            text = itemView.findViewById(R.id.util_cardtext);
            card=itemView.findViewById(R.id.utility_cardlayout);
            fillform=itemView.findViewById(R.id.getutilitydetail_fillform);
            //   courseRatingTV = itemView.findViewById(R.id.idTVCourseRating);
        }
    }
}