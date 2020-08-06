package com.hasan.order_application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class All_Cart extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase database;
    DatabaseReference requests;

    FirebaseRecyclerAdapter<Request1,OrderViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all__cart);

        database=FirebaseDatabase.getInstance();
        requests=database.getReference("Requests");


        recyclerView=findViewById(R.id.orderList);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadOrders(Common.currentUser.getPhone());
    }

    private void loadOrders(String phone) {
        adapter=new FirebaseRecyclerAdapter<Request1, OrderViewHolder>(
                Request1.class,
                R.layout.order_layout,
                OrderViewHolder.class,
                requests.orderByChild("phone").equalTo(phone)
        ) {
            @Override
            protected void populateViewHolder(OrderViewHolder orderViewHolder, final Request1 request, int i) {
                orderViewHolder.order_phone.setText(request.getCurrentDate());
                orderViewHolder.order_address.setText(request.getAddress());
                orderViewHolder.order_id.setText(adapter.getRef(i).getKey());




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
        startActivity(new Intent(getApplicationContext(),Home.class));
        finish();
        super.onBackPressed();
    }
}
