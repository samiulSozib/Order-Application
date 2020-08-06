package com.hasan.order_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class List_All_Order extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase database;
    DatabaseReference requests;

    FirebaseRecyclerAdapter<Request1,OrderViewHolder1> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list__all__order);

        database=FirebaseDatabase.getInstance();
        requests=database.getReference("Requests");


        recyclerView=findViewById(R.id.orderList);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadOrders();
    }

    private void loadOrders() {
        adapter=new FirebaseRecyclerAdapter<Request1, OrderViewHolder1>(
                Request1.class,
                R.layout.order_layout1,
                OrderViewHolder1.class,
                requests
        ) {
            @Override
            protected void populateViewHolder(OrderViewHolder1 orderViewHolder, final Request1 request, final int i) {
                orderViewHolder.order_phone.setText(request.getCurrentDate());
                orderViewHolder.order_address.setText(request.getAddress());
                //orderViewHolder.order_id.setText(adapter.getRef(i).getKey());
                orderViewHolder.order_id.setText("Order By : "+request.getName());

                orderViewHolder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder=new AlertDialog.Builder(List_All_Order.this);
                        builder.setTitle("Delete Order");
                        builder.setMessage("Do You Want To Delete This Order ?");

                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseDatabase.getInstance().getReference()
                                        .child("Requests")
                                        .child(getRef(i).getKey())
                                        //.removeValue()
                                        .setValue(null)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    Toast.makeText(getApplicationContext(),"Successfully Deleted",Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                            }
                        });

                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.create().show();
                        /*
                        FirebaseDatabase.getInstance().getReference()
                                .child("Requests")
                                .child(getRef(i).getKey())
                                //.removeValue()
                                .setValue(null)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(getApplicationContext(),"Successfully Deleted",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                         */
                    }

                });

                orderViewHolder.show_details.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                            Intent order_show=new Intent(List_All_Order.this,Order_show.class);
                            order_show.putExtra("Requests",adapter.getRef(i).getKey());
                            startActivity(order_show);
                            finish();
                    }
                });

                orderViewHolder.setItemClickListener(new ItemClickListener() {
                    final Request1 clickItem=request;
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(getApplicationContext(),""+adapter.getRef(position).getKey(),Toast.LENGTH_SHORT).show();
                        //Intent order_show=new Intent(OrderStatus.this,Order_show.class);
                        //order_show.putExtra("Requests",adapter.getRef(position).getKey());
                        //startActivity(order_show);

                    }

                });



            }
        };
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),Admin_Side.class));
        finish();
    }


}
