package com.speckpro.salonwiz.afterlogin.filingcabinet.Utilititesinput;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.request.RequestOptions;
import com.speckpro.salonwiz.GlideApp;
import com.speckpro.salonwiz.R;

import java.util.List;

public class inputselectionadapter extends RecyclerView.Adapter<inputselectionadapter.ViewHolder> {
    private final List<inputselection_model> stList;
    private Context context;
    public inputselectionadapter(List<inputselection_model> inputselection_models) {
        this.stList = inputselection_models;

    }


    @NonNull
    @Override
    public inputselectionadapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.inputselcard_cardlayout,parent,false);
        ViewHolder holdernew=new ViewHolder(v);
        return holdernew;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,int position) {
         /*   if(arrayList!=null&arrayList.size()>0){
                holder.checkBox.setText(arrayList.get(position));
                if(holder.checkBox.isChecked()){
                    arrayList_0.add(arrayList.get(position));

                }else {
                    arrayList_0.remove(arrayList.get(position));
                }
                checkboxListener.oncheckboxchange(arrayList_0);
            }*/
        final int pos = position;

        holder.text.setText(stList.get(position).getText());
       // Glide.with(holder.()).load(stList.get(position).getUrl()).into(holder.drawable);

       // holder.tvEmailId.setText(stList.get(position).getEmailId());
       // holder.drawable.setImageResource(stList.get(position).getDrawable());
        holder.checkBox.setChecked(stList.get(position).isSelected());

        holder.checkBox.setTag(stList.get(position));
        Log.d("URL", stList.get(position).getUrl());
       // Utils.fetchSvg(context, stList.get(position).getUrl(),holder.drawable);
        GlideApp.with(holder.itemView.getContext()).load(stList.get(position).getUrl()).apply(RequestOptions.centerCropTransform()).into(holder.drawable);
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                inputselection_model contact = (inputselection_model) cb.getTag();

                contact.setSelected(cb.isChecked());
                stList.get(pos).setSelected(cb.isChecked());

            //    Toast.makeText(
               //         v.getContext(),
               //         "Clicked on Checkbox: " + cb.getText() + " is "
                     //           + cb.isChecked(), Toast.LENGTH_LONG).show();
            }
        });

        }



    @Override
    public int getItemCount() {
        return stList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        private final ImageView drawable;
        private final TextView text;

        public ViewHolder(@NonNull View itemView) {


            super(itemView);
            checkBox=itemView.findViewById(R.id.gascheck);
            drawable = itemView.findViewById(R.id.util_cardimage);
            text = itemView.findViewById(R.id.util_cardtext);
        }
    }


    public List<inputselection_model> getStudentist() {
        return stList;
    }
}
