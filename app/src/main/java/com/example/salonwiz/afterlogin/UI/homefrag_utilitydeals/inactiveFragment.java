package com.example.salonwiz.afterlogin.UI.homefrag_utilitydeals;

import static com.example.salonwiz.BaseUrl.baseurl;
import static com.facebook.FacebookSdk.getApplicationContext;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.salonwiz.R;
import com.example.salonwiz.afterlogin.filingcabinet.Utilititesinput.card_model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class inactiveFragment extends Fragment {
    private RecyclerView courseRV;
    public static final int REQUEST_CODE = 101;
    private inactiveadapter mAdapter;
    private ArrayList<card_model> data;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inactive, container, false);

        SharedPreferences sh = getActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        String id = sh.getString("id", "");
        courseRV = rootView.findViewById(R.id.idRVCourse);
        getutilities(id,rootView);

        return rootView;
    }

    public void getutilities(String User,View rootView){
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
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
                        data= new ArrayList<>();
                        String message=json.getString("message");
                        if(message.equals("Utility Found for this User")){
                            for (int i=0;i<result.length();i++){
                                JSONObject userdata=result.getJSONObject(i);
                                if(userdata.getString("isActive").equals("Expired")) {
                                    JSONObject Utilities = userdata.getJSONObject("Utilities");
                                    String title = Utilities.getString("Title");
                                    String image = Utilities.getString("image");
                                    String correcturl = image.replaceAll("\\\\", "");

                                    card_model st = new card_model(correcturl, title);

                                    data.add(st);
                                    courseRV = rootView.findViewById(R.id.idRVCourse);
                                    courseRV.setHasFixedSize(true);
                                    courseRV.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                                    mAdapter = new inactiveadapter(getActivity(), data);
                                    courseRV.setAdapter(mAdapter);
                                }
                            }
                        }
                        else{
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
                    Toast.makeText(getApplicationContext(), "Check Your Internet Connection!", Toast.LENGTH_SHORT).show();

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