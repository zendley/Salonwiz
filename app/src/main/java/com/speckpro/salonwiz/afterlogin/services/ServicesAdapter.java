package com.speckpro.salonwiz.afterlogin.services;

import static com.speckpro.salonwiz.BaseUrl.baseurl;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.speckpro.salonwiz.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.Viewholder>{

    private final Context context;
    private final ArrayList<mode_services> courseModelArrayList;
    DatePickerDialog datePickerDialog;
    String dirPath, fileName;

    // Constructor
    public ServicesAdapter(Context context, ArrayList<mode_services> courseModelArrayList) {
        this.context = context;
        this.courseModelArrayList = courseModelArrayList;
    }

    @NonNull
    @Override
    public ServicesAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.renderview_services, parent, false);
        return new ServicesAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServicesAdapter.Viewholder holder, int position) {
        // to set data to textview and imageview of each card layout
        SharedPreferences sh = context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        String id = sh.getString("id", "");
        mode_services model = courseModelArrayList.get(position);

        holder.title.setText(model.getTitle());
        //holder.status.setText("("+model.getStatus()+")");
        if(model.getTitle().equals("Premium")){
            holder.charges.setText("The Premium package is focused on medium to large business with aggressive marketing goals and eager to want us to manage their entire marketing activities.");
        } else if(model.getTitle().equals("Ultra")){
            holder.charges.setText("The Ultra package is focused on small to medium business ready to ramp up for increased awareness, leads and customers.");
        } else if(model.getTitle().equals("Basic")) {
            holder.charges.setText("The Basic package is ideal for small to medium businesses looking to get started at a steady, sustainable pace.");
        } else {
            holder.charges.setText("");
        }

        holder.cardViewService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean h= holder.status.getVisibility() == View.VISIBLE;
                Intent s=new Intent(context, service_basic.class);
                s.putExtra("packagetitle", model.getTitle());
                s.putExtra("status",h);
                context.startActivity(s);
              //  context.startActivity(new Intent(context, service_basic.class).putExtra("packagetitle", model.getTitle()));
            }
        });
        checkstatus(id,holder.title,holder.status);

    }

    @Override
    public int getItemCount() {
        return courseModelArrayList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView status;
        private final TextView charges;
        private final CardView cardViewService;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            cardViewService = itemView.findViewById(R.id.cardViewServices);
            title = itemView.findViewById(R.id.service_titletext);
            status = itemView.findViewById(R.id.service_packageStatus);
            charges=itemView.findViewById(R.id.service_amountDuration);
        }
    }
    public void checkstatus(String id,TextView title,TextView status){
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait");
        progressDialog.show();
        String url3=baseurl+"api/UserServices/getall";
        JSONObject object6=new JSONObject();
        //object1.put("User",User);
        RequestQueue mqueue6 = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjRequest6 = new JsonObjectRequest(Request.Method.GET,
                url3,null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d("myapp", "the response is" + response.toString());
                JSONObject json = null;
                try {

                    json = new JSONObject(String.valueOf(response));
                    String message=json.getString("message");
                    JSONArray Data=json.getJSONArray("Data");
                    for (int i=0;i<Data.length();i++){
                        JSONObject userdata= Data.getJSONObject(i);
                        JSONArray User=userdata.getJSONArray("User");
                        JSONObject Userid=User.getJSONObject(0);
                        String gid=Userid.getString("_id");
                        if(gid.equals(id)){
                            String gpackage=userdata.getString("Package");
                            String gStatus=userdata.getString("Status");
                            if(gpackage.equals(title.getText().toString())){
                                status.setText("("+gStatus+")");
                                status.setVisibility(View.VISIBLE);

                            }
                        }

                    }
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                }


            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(context, "Check Your Internet Connection!", Toast.LENGTH_SHORT).show();
            }
        }
        ) { @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            progressDialog.dismiss();
            HashMap<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            return headers;
        }
        };
        mqueue6.add(jsonObjRequest6);
    }
}