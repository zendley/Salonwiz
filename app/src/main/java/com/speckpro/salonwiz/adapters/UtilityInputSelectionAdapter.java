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

import com.speckpro.salonwiz.R;
import com.speckpro.salonwiz.models.UtilityInputSelectionModel;

import java.util.List;

public class UtilityInputSelectionAdapter extends RecyclerView.Adapter<UtilityInputSelectionAdapter.ViewHolder> {
    private final List<UtilityInputSelectionModel> stList;
    private Context context;

    public UtilityInputSelectionAdapter(List<UtilityInputSelectionModel> UtilityInputSelectionModels) {
        this.stList = UtilityInputSelectionModels;
    }

    @NonNull
    @Override
    public UtilityInputSelectionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.inputselcard_cardlayout,parent,false);
        ViewHolder holdernew=new ViewHolder(v);
        return holdernew;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,int position) {
        final int pos = position;

        holder.text.setText(stList.get(position).getText());
        holder.checkBox.setChecked(stList.get(position).isSelected());
        holder.checkBox.setTag(stList.get(position));
        //GlideApp.with(holder.itemView.getContext()).load(stList.get(position).getUrl()).apply(RequestOptions.centerCropTransform()).into(holder.drawable);

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                UtilityInputSelectionModel contact = (UtilityInputSelectionModel) cb.getTag();

                contact.setSelected(cb.isChecked());
                stList.get(pos).setSelected(cb.isChecked());
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

    public List<UtilityInputSelectionModel> getStudentist() {
        return stList;
    }

}
