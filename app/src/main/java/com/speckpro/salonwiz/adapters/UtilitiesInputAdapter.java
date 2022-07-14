package com.speckpro.salonwiz.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.request.RequestOptions;
import com.speckpro.salonwiz.R;
import com.speckpro.salonwiz.newmodels.UtilitiesModel;
import com.speckpro.salonwiz.utils.GlideApp;

import java.util.ArrayList;
import java.util.List;

public class UtilitiesInputAdapter extends RecyclerView.Adapter<UtilitiesInputAdapter.ViewHolder> {

    private ArrayList<UtilitiesModel> utilitiesArrayList;
    private ArrayList<UtilitiesModel> selectedUtilitiesArrayList;
    private Context context;

    public UtilitiesInputAdapter(Context context, ArrayList<UtilitiesModel> utilitiesArrayList) {
        this.context = context;
        this.utilitiesArrayList = utilitiesArrayList;
        selectedUtilitiesArrayList = new ArrayList<>();
    }

    @NonNull
    @Override
    public UtilitiesInputAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.inputselcard_cardlayout,parent,false);
        ViewHolder holdernew=new ViewHolder(v);
        return holdernew;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,int position) {
        final int pos = position;

        holder.text.setText(utilitiesArrayList.get(position).getTitle());
//        holder.checkBox.setChecked(utilitiesArrayList.get(position).isSelected());
        holder.checkBox.setTag(utilitiesArrayList.get(position));

        String imgUrl = "http://portal.mysalonmanager.co.uk/uploads/"+utilitiesArrayList.get(position).getImage();

        GlideApp.with(holder.itemView.getContext()).load(imgUrl).apply(RequestOptions.centerCropTransform()).into(holder.drawable);

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                UtilitiesModel utilitiesModel = new UtilitiesModel();
                utilitiesModel.setTitle(holder.text.getText().toString());
                utilitiesModel.setIsChecked("1");
                selectedUtilitiesArrayList.add(utilitiesModel);
            }
        });
    }

    @Override
    public int getItemCount() {
        return utilitiesArrayList.size();
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

    public List<UtilitiesModel> getUtilitiesList() {
        return selectedUtilitiesArrayList;
    }

}
