package com.hasan.order_application;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Admin_Side extends AppCompatActivity {

    Button show_order_button,add_product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__side);

        show_order_button=findViewById(R.id.show_all_order);
        add_product=findViewById(R.id.add_product_id);

        show_order_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),List_All_Order.class));
                finish();
            }
        });

        add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Add_Product.class));
                finish();
            }
        });
//        Intent service=new Intent(Admin_Side.this,ListenOrder.class);
//        startService(service);

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(this);
        alertDialogBuilder.setIcon(R.drawable.alert);
        alertDialogBuilder.setTitle("Alert");
        alertDialogBuilder.setMessage("Do you want to exit?");



        alertDialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        alertDialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        alertDialogBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog=alertDialogBuilder.create();
        alertDialog.show();
    }

    public void all_order_food_id(View view) {

        startActivity(new Intent(getApplicationContext(),All_Order_Food_List.class));
        finish();
    }
}
