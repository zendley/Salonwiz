package com.example.salonwiz.afterlogin.UI.homefrag_utilitydeals;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.salonwiz.GlideApp;
import com.example.salonwiz.R;

import java.util.ArrayList;

public class allfrag_adapter extends RecyclerView.Adapter<allfrag_adapter.Viewholder>{

private final Context context;
private final ArrayList<allfrag_model> courseModelArrayList;
        DatePickerDialog datePickerDialog;

// Constructor
public allfrag_adapter(Context context, ArrayList<allfrag_model> courseModelArrayList) {
        this.context = context;
        this.courseModelArrayList = courseModelArrayList;
        }

@NonNull
@Override
public allfrag_adapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.utilities_cardlayout, parent, false);
        return new Viewholder(view);
        }

@Override
public void onBindViewHolder(@NonNull allfrag_adapter.Viewholder holder, int position) {
        // to set data to textview and imageview of each card layout
    allfrag_model model = courseModelArrayList.get(position);
        holder.fillform.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        Intent intent=new Intent(v.getContext(), utilityformfilling.class);
        intent.putExtra("title",model.getText());
        intent.putStringArrayListExtra("spinner",model.getSpinner());
        v.getContext().startActivity(intent);
        }
        });
        holder.text.setText(model.getText());
        // holder.courseRatingTV.setText("" + model.getCourse_rating());
        // holder.drawable.setImageResource(model.getDrawable());
       // Glide.with(context).load(model.getDrawable()).into(holder.drawable);
        GlideApp.with(holder.itemView.getContext()).load(model.getDrawable()).apply(RequestOptions.centerCropTransform()).into(holder.drawable);

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






// View holder class for initializing of
// your views such as TextView and Imageview.
public class Viewholder extends RecyclerView.ViewHolder {
    private final ImageView drawable;
    private final TextView text;
    private final CardView card;
    private final Button fillform;
    private ArrayList<String> spinner;

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