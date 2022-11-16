package com.freedom.order_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FoodDetails extends AppCompatActivity {

    TextView foodName,foodDescription;
    EditText fPrice,fFree;
    Button floatingActionButton;
    ElegantNumberButton numberButton;
    String foodId="";

    FirebaseDatabase database;
    DatabaseReference fooods;

    Food currentFood;
    Spinner spinner;

    final String[] type={"Select Type","পিচ","কার্টুন","পলি","বস্তা","জার","ডজন","বক্স"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);

        database=FirebaseDatabase.getInstance();
        fooods=database.getReference("Foods");

        foodName=findViewById(R.id.food_name);
        fPrice=findViewById(R.id.food_price);
        foodDescription=findViewById(R.id.food_description);
        floatingActionButton=findViewById(R.id.btn_cart);
        numberButton=findViewById(R.id.number_button);
        fFree=findViewById(R.id.food_item_free);

        spinner=findViewById(R.id.food_item_type_spinner);
        final ArrayAdapter<String> adapter=new ArrayAdapter<>(FoodDetails.this,android.R.layout.simple_spinner_item,type);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fPrice.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"Please Enter Price",Toast.LENGTH_SHORT).show();
                }else if(spinner.getSelectedItem().toString().equals("Select Type")){
                    Toast.makeText(getApplicationContext(),"Please Select Product Quantity Type",Toast.LENGTH_SHORT).show();
                }
                else{
                    new Database(getBaseContext()).addToCart(new Order(
                            foodId,
                            currentFood.getName(),
                            numberButton.getNumber(),
                            //currentFood.getPrice()
                            fPrice.getText().toString(),
                            //fType.getText().toString(),
                            spinner.getSelectedItem().toString(),
                            fFree.getText().toString()


                    ));

                    Toast.makeText(getApplicationContext(),"Added to cart",Toast.LENGTH_SHORT).show();
                }

            }
        });

        //final static String price=fPrice.getText().toString();


        if (getIntent()!=null){
            foodId=getIntent().getStringExtra("FoodId");
        }
        if (!foodId.isEmpty()){
            getDetailsFood(foodId);
        }
    }

    private void getDetailsFood(final String foodId) {
        fooods.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentFood=dataSnapshot.getValue(Food.class);

                //fPrice.setText(currentFood.setPrice(price));
                foodName.setText(currentFood.getName());
                foodDescription.setText(currentFood.getDescription());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
