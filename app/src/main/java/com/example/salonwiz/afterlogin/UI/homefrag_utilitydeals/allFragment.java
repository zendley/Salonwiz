package com.example.salonwiz.afterlogin.UI.homefrag_utilitydeals;

import static com.example.salonwiz.BaseUrl.baseurl;
import static com.facebook.FacebookSdk.getApplicationContext;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.salonwiz.R;
import com.example.salonwiz.afterlogin.filingcabinet.Utilititesinput.card_model;
import com.example.salonwiz.afterlogin.filingcabinet.Utilititesinput.cardadapter;
import com.example.salonwiz.afterlogin.filingcabinet.Utilititesinput.inputselection_model;
import com.example.salonwiz.afterlogin.filingcabinet.Utilititesinput.inputselectionadapter;
import com.example.salonwiz.afterlogin.filingcabinet.Utilititesinput.utilities_inputselection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class allFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView courseRV;
    public static final int REQUEST_CODE = 101;
    private cardadapter mAdapter;
    private ArrayList<card_model> data;
    private String id;
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_all, container, false);
        SharedPreferences sh = getActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        id = sh.getString("id", "");
        //  boolean gasStatus =((intent.extras.get("gasStatus")).toString()).toBoolean();
        courseRV = rootView.findViewById(R.id.idRVCourse);
        getutilities(id,rootView);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (Build.VERSION.SDK_INT >= 26) {
            ft.setReorderingAllowed(false);
        }
        ft.detach(this).attach(this).commit();
        return rootView;
    }

    @Override
    public void onResume(){
        super.onResume();
        //OnResume Fragment
        getutilities(id,rootView);
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
                                ArrayList<String> s=new ArrayList<>();
                                JSONObject userdata=result.getJSONObject(i);

                                JSONObject Utilities=userdata.getJSONObject("Utilities");
                                String title=Utilities.getString("Title");
                                String image=Utilities.getString("image");
                                String correcturl=image.replaceAll("\\\\","");
                                Boolean l=true;
                                if(userdata.getString("isActive").equals("Expired")||userdata.getString("isActive").equals("activated")){
                                    l=false;
                                }
                                card_model st =new card_model(correcturl,title,l);

                                data.add(st);
                                courseRV = rootView.findViewById(R.id.idRVCourse);
                                courseRV.setHasFixedSize(true);
                                courseRV.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
                                mAdapter = new cardadapter(getActivity(),data);
                                courseRV.setAdapter(mAdapter);
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