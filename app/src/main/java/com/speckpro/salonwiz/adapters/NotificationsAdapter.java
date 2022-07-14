package com.speckpro.salonwiz.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.speckpro.salonwiz.R;
import com.speckpro.salonwiz.newmodels.NotificationsModel;

import java.util.ArrayList;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.Viewholder> {

    private final Context context;
    private final ArrayList<NotificationsModel> notificationsArrayList;

    public NotificationsAdapter(Context context, ArrayList<NotificationsModel> notificationsArrayList) {
        this.context = context;
        this.notificationsArrayList = notificationsArrayList;
    }

    @NonNull
    @Override
    public NotificationsAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.renderview_notification, parent, false);
        return new NotificationsAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationsAdapter.Viewholder holder, int position) {

        NotificationsModel notifications = notificationsArrayList.get(position);

        holder.textViewNotification.setText(notifications.getDescription());

        if(notifications.getIsRead()==1){
            holder.layoutMain.setBackgroundColor(context.getResources().getColor(R.color.white));
            holder.imageViewNotificationOptions.setVisibility(View.GONE);
        } else {
            holder.layoutMain.setBackgroundColor(context.getResources().getColor(R.color.white));
            holder.imageViewNotificationOptions.setVisibility(View.VISIBLE);
        }

        holder.imageViewNotificationOptions.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                args.putString("id", String.valueOf(notifications.getId()));
                BottomSheetDialogFragment bottomSheetDialogFragment = new BottomsheetDialog();
                bottomSheetDialogFragment.setArguments(args);
                bottomSheetDialogFragment.show(((FragmentActivity)context).getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return notificationsArrayList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        private final RelativeLayout layoutMain;
        private final TextView textViewNotification;
        private final ImageView imageViewNotificationOptions;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            layoutMain = itemView.findViewById(R.id.layout_renderNotifications);
            textViewNotification = itemView.findViewById(R.id.textView_notification);
            imageViewNotificationOptions = itemView.findViewById(R.id.imageView_notificationOptions);
        }
    }

}
