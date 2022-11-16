package com.freedom.order_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class All_Order_Food_List extends AppCompatActivity {

    FirebaseRecyclerAdapter<Request1,OrderViewHolder1> adapter;
    FirebaseDatabase database;
    DatabaseReference requests;

    ListView recycler_foods;
//    RecyclerView.LayoutManager layoutManager;
//
//    FirebaseRecyclerAdapter<Order, SamiulViewHolder> adapter1;

    List<Order> orderList;

    All_Order_Food_List_Adapter all_order_food_list_adapter;
    //String key="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all__order__food__list);

        orderList=new ArrayList<>();


        all_order_food_list_adapter=new All_Order_Food_List_Adapter(this,orderList);

        database=FirebaseDatabase.getInstance();
        requests=database.getReference("Requests");

        recycler_foods=findViewById(R.id.all_food_list_recycler);


        adapter=new FirebaseRecyclerAdapter<Request1, OrderViewHolder1>(
                Request1.class,
                R.layout.order_layout1,
                OrderViewHolder1.class,
                requests
        ) {
            @Override
            protected void populateViewHolder(OrderViewHolder1 orderViewHolder1, Request1 request1, int i) {

            }
        };

            requests.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        String key=dataSnapshot1.getKey();

                        //
                        assert key != null;
                        requests.child(key).child("foods").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                                    Order order=dataSnapshot1.getValue(Order.class);

                                    //

                                    //

                                    orderList.add(order);
                                }


                                Collections.sort(orderList,new ProductSort());




                                recycler_foods.setAdapter(all_order_food_list_adapter);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        //
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



//        for(int i=0;i<adapter.getItemCount();i++){
//            adapter1=new FirebaseRecyclerAdapter<Order, SamiulViewHolder>(
//                    Order.class,
//                    R.layout.samiul_layout,
//                    SamiulViewHolder.class,
//                    requests.child(adapter.getRef(i).getKey()).child("foods")
//                    //requests.child(adapter.getRef(0).getKey()).child("foods")
//            ) {
//                @Override
//                protected void populateViewHolder(SamiulViewHolder samiulViewHolder, Order order, int i) {
//                    samiulViewHolder.samiulName.setText("Name :"+order.getProductName());
//                    samiulViewHolder.samiulQuantity.setText("Quantity :"+order.getQuantity());
//                    Locale locale=new Locale("en","BD");
//                    NumberFormat fmt=NumberFormat.getCurrencyInstance(locale);
//                    double x=Double.parseDouble(order.getPrice())*Double.parseDouble(order.getQuantity());
//                    samiulViewHolder.samiulPrice.setText(fmt.format(x));
//                }
//            };
//
//            recycler_foods.setAdapter(adapter1);
//
//        }

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),Admin_Side.class));
        finish();
    }


    private class ProductSort implements Comparator<Order>{

        @Override
        public int compare(Order o1, Order o2) {
            return o1.getProductName().compareTo(o2.getProductName());
        }
    }
}
