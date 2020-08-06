package com.hasan.order_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Cart extends AppCompatActivity {

    RecyclerView recyclerView;
    FirebaseDatabase database;
    DatabaseReference requests;
    RecyclerView.LayoutManager layoutManager;

    TextView total_price;
    Button place_order;
    List<Order> cart=new ArrayList<>();
    CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        database=FirebaseDatabase.getInstance();
        requests=database.getReference("Requests");

        recyclerView=findViewById(R.id.list_cart);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        total_price=findViewById(R.id.total);
        place_order=findViewById(R.id.btn_place_order);

        place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cart.size()>0)
                    showAlertDialog();
                else
                    Toast.makeText(getApplicationContext(),"Your cart is empty",Toast.LENGTH_SHORT).show();

            }
        });

        loadListFood();
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(Cart.this);
        builder.setTitle("");
        builder.setMessage("Enter Location");

        final EditText editAddress=new EditText(Cart.this);
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        editAddress.setLayoutParams(layoutParams);
        builder.setView(editAddress);

        Calendar calendar=Calendar.getInstance();
        final String currentDate= DateFormat.getDateInstance().format(calendar.getTime());


        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (editAddress.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"Please enter order location",Toast.LENGTH_SHORT).show();
                }else{
                    Request1 request=new Request1(
                            Common.currentUser.getPhone(),
                            Common.currentUser.getName(),
                            editAddress.getText().toString(),
                            currentDate,
                            total_price.getText().toString(),
                            cart
                    );

                    requests.child(String.valueOf(System.currentTimeMillis()))
                            .setValue(request);
                    new Database(getBaseContext()).cleanCart();
                    Toast.makeText(Cart.this,"Thank You, Place order successfully",Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void loadListFood() {
        cart=new Database(this).getCarts();
        adapter=new CartAdapter(cart,this);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        //calculate total price
        double total=0;
        for (Order order:cart)
            total+=(Double.parseDouble(order.getPrice()))*(Double.parseDouble(order.getQuantity()));


        Locale locale=new Locale("en","BD");
        NumberFormat fmt=NumberFormat.getCurrencyInstance(locale);

        total_price.setText(fmt.format(total));
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getTitle().equals(Common.DELETE)){
            deleteCart(item.getOrder());
        }
        return true;
    }

    private void deleteCart(int position) {
        cart.remove(position);

        new Database(this).cleanCart();

        for (Order item:cart)
            new Database(this).addToCart(item);

        loadListFood();
    }
}
