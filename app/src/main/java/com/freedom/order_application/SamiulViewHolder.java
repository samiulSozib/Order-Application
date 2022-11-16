package com.freedom.order_application;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SamiulViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView samiulName,samiulQuantity,samiulPrice,samiulType,samiulFree;


    public SamiulViewHolder(@NonNull View itemView) {
        super(itemView);
        samiulName=itemView.findViewById(R.id.samiul_name);
        samiulQuantity=itemView.findViewById(R.id.samiul_quantity);
        samiulPrice=itemView.findViewById(R.id.samiul_price);
        samiulType=itemView.findViewById(R.id.samiul_quantiy_type);
        samiulFree=itemView.findViewById(R.id.samiul_free);
    }

    @Override
    public void onClick(View v) {

    }
}
