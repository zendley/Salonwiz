package com.speckpro.salonwiz.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.speckpro.salonwiz.R;
import com.speckpro.salonwiz.models.FaqModel;

import java.util.ArrayList;

public class FaqAdapter extends RecyclerView.Adapter<FaqAdapter.Viewholder> {

    private final Context context;
    private final ArrayList<FaqModel> faqModelArrayList;

    public FaqAdapter(Context context, ArrayList<FaqModel> faqModelArrayList) {
        this.context = context;
        this.faqModelArrayList = faqModelArrayList;
    }

    @NonNull
    @Override
    public FaqAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_faqslist, parent, false);
        return new FaqAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FaqAdapter.Viewholder holder, int position) {

        FaqModel faqModel = faqModelArrayList.get(position);
        holder.quest.setText(faqModel.getQuestion());

        holder.quest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.ans.getVisibility()==View.GONE) {
                    holder.ans.setText(faqModel.getAnswer());
                    holder.ans.setVisibility(View.VISIBLE);
                    holder.drawable.setRotation(90);
                } else {
                    holder.ans.setText("");
                    holder.ans.setVisibility(View.GONE);
                    holder.drawable.setRotation(360);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return faqModelArrayList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private final ImageView drawable;
        private final TextView quest;
        private final TextView ans;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            drawable = itemView.findViewById(R.id.imageView_next);
            quest = itemView.findViewById(R.id.textView_faqQuest);
            ans = itemView.findViewById(R.id.textView_faqAns);
        }
    }

}