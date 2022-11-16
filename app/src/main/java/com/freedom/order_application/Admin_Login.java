package com.freedom.order_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Admin_Login extends AppCompatActivity implements TextWatcher, CompoundButton.OnCheckedChangeListener{

    EditText phone_number,pin_number;
    Button admin_button;

    FirebaseDatabase database;
    DatabaseReference tbl_admin;

    CheckBox checkBox;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private static final String PREF_NAME="pref";
    private static final String KEY_REMEMBER="remember";
    private static final String KEY_USERNAME="user_name";
    private static final String KEY_PASSWORD="password";

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__login);

        phone_number=findViewById(R.id.phone_number_id);
        pin_number=findViewById(R.id.pin_id);
        admin_button=findViewById(R.id.login_button);
        database=FirebaseDatabase.getInstance();
        tbl_admin=database.getReference("Admin");

        checkBox=findViewById(R.id.checkbox_id);
        progressDialog=new ProgressDialog(this);

        /////////////////////////////
        sharedPreferences=getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();

        if (sharedPreferences.getBoolean(KEY_REMEMBER,false)){
            checkBox.setChecked(true);
        }else {
            checkBox.setChecked(false);
        }

        phone_number.setText(sharedPreferences.getString(KEY_USERNAME,""));
        pin_number.setText(sharedPreferences.getString(KEY_PASSWORD,""));

        phone_number.addTextChangedListener(this);
        pin_number.addTextChangedListener(this);
        checkBox.setOnCheckedChangeListener(this);

        /////////////////////////////

        admin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phone_number.getText().toString().equals("")||pin_number.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"Please enter Phone number and pin",Toast.LENGTH_SHORT).show();
                }else {
                    loginAdmin();
                }
            }
        });
    }

    private void loginAdmin() {
        if (Common.isConnectToInternet(getBaseContext())) {
            progressDialog.setMessage("Logging In.....");
            progressDialog.show();
            tbl_admin.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(phone_number.getText().toString()).exists()) {
                        User user = dataSnapshot.child(phone_number.getText().toString()).getValue(User.class);
                        user.setPhone(phone_number.getText().toString());
                        //if (user.getPassword().equals(pin_number.getText().toString())) {
                        if (pin_number.getText().toString().equals(user.getPassword())) {
                            progressDialog.dismiss();
                            Intent homeIntent = new Intent(getApplicationContext(), Admin_Side.class);
                            Common.currentUser = user;
                            startActivity(homeIntent);
                            finish();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(Admin_Login.this, "Sign in Failed", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(Admin_Login.this, "Data invalid", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }else {
            Toast.makeText(getApplicationContext(),"Please Check Internet Connection",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        managePrefs();
    }
    public void managePrefs(){
        if (checkBox.isChecked()){
            editor.putString(KEY_USERNAME,phone_number.getText().toString().trim());
            editor.putString(KEY_PASSWORD,pin_number.getText().toString().trim());
            editor.putBoolean(KEY_REMEMBER,true);
            editor.apply();
        }else {
            editor.putBoolean(KEY_REMEMBER,false);
            editor.remove(KEY_USERNAME);
            editor.remove(KEY_PASSWORD);
            editor.apply();
        }
    }
}
