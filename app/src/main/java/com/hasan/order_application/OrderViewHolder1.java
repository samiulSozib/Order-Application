package com.hasan.order_application;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OrderViewHolder1 extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView order_id,order_address,order_phone;
    public Button delete,show_details;
    private ItemClickListener itemClickListener;
    public OrderViewHolder1(@NonNull View itemView) {
        super(itemView);
        order_id=itemView.findViewById(R.id.order_id);
        order_address=itemView.findViewById(R.id.order_address);
        order_phone=itemView.findViewById(R.id.date);
        delete=itemView.findViewById(R.id.delete_order);
        show_details=itemView.findViewById(R.id.show_order_details);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);
    }
}
