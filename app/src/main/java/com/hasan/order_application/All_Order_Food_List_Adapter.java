package com.hasan.order_application;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class All_Order_Food_List_Adapter extends ArrayAdapter<Order> {

    private Activity context;
    private List<Order> orderList;


    public All_Order_Food_List_Adapter(@NonNull Activity context, List<Order> orderList) {
        super(context, R.layout.samiul_layout,orderList);
        this.context=context;
        this.orderList=orderList;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //LayoutInflater layoutInflater= context.getLayoutInflater();
        //View view=layoutInflater.inflate(R.layout.today_sample,parent,false);

        LayoutInflater layoutInflater=LayoutInflater.from(context);



        if (convertView==null){
            //layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=layoutInflater.inflate(R.layout.samiul_layout,parent,false);

        }



        Order order=orderList.get(position);
        TextView name=convertView.findViewById(R.id.samiul_name);
        TextView quantity=convertView.findViewById(R.id.samiul_quantity);
        TextView price=convertView.findViewById(R.id.samiul_price);



        name.setText(order.getProductName());
        quantity.setText(order.getQuantity());
        price.setText(order.getPrice());
        price.setVisibility(View.GONE);





        return convertView;
    }
}
