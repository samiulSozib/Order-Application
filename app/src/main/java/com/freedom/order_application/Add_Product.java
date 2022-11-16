package com.freedom.order_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Add_Product extends AppCompatActivity {

    EditText product_name,product_description;
    Spinner spinner;
    Button add_button;

    FirebaseDatabase database;
    DatabaseReference reference,category;

    FirebaseRecyclerAdapter<Category, MenuViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__product);

        product_description=findViewById(R.id.product_description_id);
        product_name=findViewById(R.id.product_name_id);
        spinner=findViewById(R.id.spinner);
        add_button=findViewById(R.id.add_product_button_id);

        database=FirebaseDatabase.getInstance();
        reference=database.getReference("Foods");

        /////
        category=database.getReference("Category");
        category.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final List<String> categories = new ArrayList<String>();
                categories.add(0,"Select Categories");
                for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {
                    String cat = areaSnapshot.child("Name").getValue(String.class);
                    categories.add(cat);
                }

                ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(Add_Product.this, android.R.layout.simple_spinner_item, categories);
                areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(areasAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /////

        /*

        List<String> categories=new ArrayList<>();
        categories.add(0,"Select Categories");
        categories.add("Fresh");
        categories.add("Deco");
        categories.add("Crap");




        ArrayAdapter<String> dataAdapter;
        dataAdapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(dataAdapter);



         */


        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String p_name=product_name.getText().toString().trim();
                String p_des=product_description.getText().toString().trim();
                String spinner_item=spinner.getSelectedItem().toString();

                if (p_name.equals("")){
                    Toast.makeText(getApplicationContext(),"Please Enter Product name",Toast.LENGTH_SHORT).show();
                }else if(spinner_item.equals("Select Categories")){
                    //
                    Toast.makeText(getApplicationContext(),"Please Select a Category",Toast.LENGTH_SHORT).show();
                }else{
                    //Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();

                    String key=reference.push().getKey();

                    Food food=new Food(p_name,p_des,null,spinner_item);
                    reference.child(key).setValue(food);

                    Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();

                    product_name.setText("");
                    product_description.setText("");

                }

            }
        });



    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),Admin_Side.class));
        finish();
    }
}
