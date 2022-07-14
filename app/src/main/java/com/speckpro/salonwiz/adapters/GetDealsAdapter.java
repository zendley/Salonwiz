package com.speckpro.salonwiz.adapters;

import static com.speckpro.salonwiz.retrofit.BaseUrl.baseurl;
import static com.facebook.FacebookSdk.getApplicationContext;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.speckpro.salonwiz.R;
import com.speckpro.salonwiz.models.model_getdeals;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GetDealsAdapter extends RecyclerView.Adapter<GetDealsAdapter.Viewholder>{

    private final Context context;
    private final ArrayList<model_getdeals> courseModelArrayList;

    // Constructor
    public GetDealsAdapter(Context context, ArrayList<model_getdeals> courseModelArrayList) {
        this.context = context;
        this.courseModelArrayList = courseModelArrayList;
    }

    @NonNull
    @Override
    public GetDealsAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_getdealrender, parent, false);
        return new GetDealsAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GetDealsAdapter.Viewholder holder, int position) {
        // to set data to textview and imageview of each card layout
        model_getdeals model = courseModelArrayList.get(position);
       // if(courseModelArrayList.size()>0){
        holder.title.setText(model.getTitle());
        holder.description.setText(model.getDescription());
        holder.getdeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setdeal(model.getTitle(),model.getDescription(), model.getId(), v);
            }
        });
      //  }else{           holder.nolist.setVisibility(View.VISIBLE);        }
        // holder.courseRatingTV.setText("" + model.getCourse_rating());
        // holder.drawable.setImageResource(model.getDrawable());
      //  Glide.with(context).load(model.getDrawable()).into(holder.drawable);
    }


    @Override
    public int getItemCount() {
        // this method is used for showing number
        // of card items in recycler view.
        return courseModelArrayList.size();
    }

   /* private void uploadFile(File latestbill,RequestBody loaform, String ) {

        //creating request body for file
        RequestBody requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(fileUri)), file);
        RequestBody descBody = RequestBody.create(MediaType.parse("text/plain"), desc);

        //The gson builder
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();


        //creating retrofit object
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://odl-saloonwizz-app.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        //creating our api
        Api api = retrofit.create(Api.class);

        //creating a call and calling the upload image method
        Call<MyResponse> call = api.uploadImage(requestFile, descBody);

        //finally performing the call
        call.enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (!response.body().error) {
                    Toast.makeText(getApplicationContext(), "File Uploaded Successfully...", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Some error occurred...", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }*/

    public void setdeal(String stitle,String sdescription,String User,View rootView){


        //  courseRV = (RecyclerView) rootView.findViewById(R.id.getdealrecycler);
        final ProgressDialog progressDialog = new ProgressDialog(rootView.getContext());
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait"); // set message
        progressDialog.show(); // show progress dialog
        String url2=baseurl+"api/Userutilities/AcceptDeal";
        //  Spinner myspiner=findViewById(R.id.spinner6);
        //  String b=myspiner.getSelectedItem().toString();
        try {
            JSONObject object1=new JSONObject();
            object1.put("Id",User);
            object1.put("Title",stitle);
            object1.put("Description",sdescription);
            RequestQueue mqueue4 = Volley.newRequestQueue(getApplicationContext());
            JsonObjectRequest jsonObjRequest4 = new JsonObjectRequest(Request.Method.POST,
                    url2, object1, new com.android.volley.Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    progressDialog.dismiss();
                    Log.d("myapp", "the response is" + response.toString());
                 //   Toast.makeText(getApplicationContext(), "Deal is Subscribed", Toast.LENGTH_SHORT).show();

                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Deal is Already Subscribed", Toast.LENGTH_SHORT).show();

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
            progressDialog.dismiss();
            jsonException.printStackTrace();
            Toast.makeText(getApplicationContext(), "Deal is Already Subscribed", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }

    }


    // View holder class for initializing of
    // your views such as TextView and Imageview.
    public class Viewholder extends RecyclerView.ViewHolder {
        private final TextView title;
        private TextView nolist;
        private final TextView description;
        private final Button getdeal;


        public Viewholder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.s1);
            description = itemView.findViewById(R.id.s2);
            getdeal=itemView.findViewById(R.id.s3);
            //nolist=itemView.findViewById(R.id.getdeal_nolist);
            //   courseRatingTV = itemView.findViewById(R.id.idTVCourseRating);
        }

    }

}