package com.freedom.order_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

public class Order_show extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference reference;
    TextView textView,textView1,textView2;
    String id="";
    Request1 currentFood;
    Button button;

    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Order, SamiulViewHolder> adapter;

    int pageWidth=1200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_show);

        database=FirebaseDatabase.getInstance();
        reference=database.getReference("Requests");

        textView=findViewById(R.id.show_order_address1);
        textView1=findViewById(R.id.show_order_name1);
        textView2=findViewById(R.id.show_order_total1);

        recycler_menu=findViewById(R.id.show_order_details);
        recycler_menu.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);
        button=findViewById(R.id.make_pdf_id);

        if (getIntent()!=null){
            id=getIntent().getStringExtra("Requests");
        }
        if (!id.isEmpty()){
            loadOrderFull(id);
            loadInList();
        }

        //pdf create------------------------
        button=findViewById(R.id.make_pdf_id);
        Dexter.withActivity(this).withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                createPDFfile(Comn.getAppPath(Order_show.this)+textView.getText().toString()+".pdf");
                            }
                        });
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();
        //pdf create------------------------
    }

    private void createPDFfile(String path) {
        if (new File(path).exists())
            new File(path).delete();


        PdfDocument pdfDocument=new PdfDocument();

        Paint myPaint=new Paint();
        Paint myPaint1=new Paint();
        Paint titlePaint=new Paint();
        Paint titlePaint1=new Paint();
        Paint totalPain=new Paint();
        Paint total=new Paint();

        PdfDocument.PageInfo myPageInfo1=new PdfDocument.PageInfo.Builder(1200,2010,1).create();
        PdfDocument.Page myPage1=pdfDocument.startPage(myPageInfo1);
        Canvas canvas=myPage1.getCanvas();

        titlePaint.setTextAlign(Paint.Align.CENTER);
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
        //titlePaint.setTextSize(50);
        titlePaint.setTextSize(30);
        //canvas.drawText("মেসার্স লিহান এন্টারপ্রাইজ",pageWidth/2,75,titlePaint);
        canvas.drawText("মেসার্স লিহান এন্টারপ্রাইজ",pageWidth/2,55,titlePaint);

        titlePaint1.setTextAlign(Paint.Align.CENTER);
        titlePaint1.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
        //titlePaint1.setTextSize(40);
        titlePaint1.setTextSize(21);
        canvas.drawText("রাধানগর বাঁজার, শ্রীপুর, মাগুরা",pageWidth/2,95,titlePaint1);

        titlePaint1.setTextAlign(Paint.Align.CENTER);
        titlePaint1.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
        //titlePaint1.setTextSize(40);
        //titlePaint1.setTextSize(20);
        titlePaint1.setTextSize(30);
        canvas.drawText("মোবাঃ ০১৭২৫-৮৭৪৯৯৭ , ০১৯২০-৬৪১৩৭৯",pageWidth/2,130,titlePaint1);

        titlePaint.setTextAlign(Paint.Align.CENTER);
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
        //titlePaint.setTextSize(60);
        titlePaint.setTextSize(30);
        canvas.drawText("Order Details",pageWidth/2,170,titlePaint);

        myPaint.setTextAlign(Paint.Align.LEFT);
        myPaint.setTextSize(25f);
        //myPaint.setTextSize(20f);
        myPaint.setColor(Color.BLACK);
        //
        myPaint1.setTextAlign(Paint.Align.LEFT);
        //myPaint.setTextSize(22f);
        myPaint1.setTextSize(22f);
        myPaint1.setColor(Color.BLACK);
        myPaint1.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
        //
        canvas.drawText("Cus. Details : "+textView.getText().toString(),120,220,myPaint1);
        canvas.drawText("Order Date: "+textView1.getText().toString(),120,250,myPaint1);
        canvas.drawText("Order By: "+currentFood.getName(),450,250,myPaint1);
        canvas.drawText("Delivery Date: ",700,250,myPaint1);

        myPaint.setStyle(Paint.Style.STROKE);
        myPaint.setStrokeWidth(2);
        //canvas.drawRect(20,425,pageWidth-20,505,myPaint);
        canvas.drawRect(120,275,pageWidth-110,330,myPaint);
        myPaint.setTextAlign(Paint.Align.LEFT);
        myPaint.setStyle(Paint.Style.FILL);

        total.setTextAlign(Paint.Align.LEFT);
        total.setStyle(Paint.Style.FILL);
        total.setTextSize(25f);
        total.setColor(Color.BLACK);
        total.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));




//        canvas.drawText("Si. ",40,475,myPaint);
//        canvas.drawText("Item Desc. ",100,475,myPaint);
//        canvas.drawText("Free ",575,475,myPaint );
//        canvas.drawText("Price ",650,475,myPaint);
//        canvas.drawText("Qnt. ",800,475,myPaint);
//        canvas.drawText("Total. ",950,475,myPaint);

        canvas.drawText("Si. ",130,305,myPaint);
        canvas.drawText("Item Desc. ",180,305,myPaint);
        canvas.drawText("Free ",580,305,myPaint );
        canvas.drawText("Price ",660,305,myPaint);
        canvas.drawText("Qnt. ",790,305,myPaint);
        canvas.drawText("Total. ",930,305,total);

        int x=0;
        for (int i=0;i<adapter.getItemCount();i++){
            canvas.drawText(""+(i+1),130,x+365,myPaint);
            canvas.drawText(""+adapter.getItem(i).getProductName(),180,x+365,myPaint);
            canvas.drawText(""+adapter.getItem(i).getFree(),580,x+365,myPaint);
            canvas.drawText(""+adapter.getItem(i).getPrice(),660,x+365,myPaint);
            canvas.drawText(""+adapter.getItem(i).getQuantity(),790,x+365,titlePaint1);
            canvas.drawText(""+adapter.getItem(i).getQntType(),810,x+365,myPaint);
            Locale locale=new Locale("en","BD");
            NumberFormat fmt=NumberFormat.getCurrencyInstance(locale);
            double _total=Double.parseDouble(adapter.getItem(i).getQuantity())*Double.parseDouble(adapter.getItem(i).getPrice());
            //canvas.drawText(""+fmt.format(total),870,x+365,myPaint);
            canvas.drawText(String.valueOf(_total),930,x+365,total);
            canvas.drawLine(120,(x+375),pageWidth-110,(x+375),myPaint);
            x+=35;

        }


        totalPain.setTextSize(30f);
        totalPain.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
        canvas.drawLine(580,(x+365),pageWidth-120,(x+365),myPaint);
        canvas.drawText("Sub Total",580,(x+365+35),totalPain);
        canvas.drawText(":",750,(x+365+35),totalPain);
        canvas.drawText(""+textView2.getText().toString(),930,(x+365+35),totalPain);

        //canvas.drawText(""+x,1050,1900,myPaint);

        canvas.drawLine(680,1350,pageWidth-20,1350,myPaint);
        canvas.drawText("Signature",750,1400,titlePaint1);

        //note

        canvas.drawText("Note: . . . . . . ",120,1400,titlePaint1);



//        canvas.drawLine(5,5,1195,5,myPaint);
//        canvas.drawLine(5,5,5,2005,myPaint);
//        canvas.drawLine(5,2005,1195,2005,myPaint);
//        canvas.drawLine(1195,5,1195,2005,myPaint);






        pdfDocument.finishPage(myPage1);

        try {
            pdfDocument.writeTo(new FileOutputStream(path));
        } catch (IOException e) {
            e.printStackTrace();
        }

        pdfDocument.close();
        Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();

    }


    private void loadInList() {
        adapter =
                new FirebaseRecyclerAdapter<Order, SamiulViewHolder>(
                        Order.class,
                        R.layout.samiul_layout,
                        SamiulViewHolder.class,
                        reference.child(id).child("foods")
                ) {
                    @Override
                    protected void populateViewHolder(SamiulViewHolder samiulViewHolder, Order order, int position) {
                        //viewHolder..setText(model.getName());
                        samiulViewHolder.samiulName.setText("Name: "+order.getProductName());
                        samiulViewHolder.samiulQuantity.setText("Quantity: "+order.getQuantity());
                        //samiulViewHolder.samiulPrice.setText(order.getPrice());
                        Locale locale=new Locale("en","BD");
                        NumberFormat fmt=NumberFormat.getCurrencyInstance(locale);
                        double x=Double.parseDouble(order.getPrice())*Double.parseDouble(order.getQuantity());
                        //samiulViewHolder.samiulPrice.setText(fmt.format(x));
                        samiulViewHolder.samiulPrice.setText(String.valueOf(x));
                        samiulViewHolder.samiulType.setText(order.getQntType());
                        samiulViewHolder.samiulFree.setText("Free : "+order.getFree());

                    }
                };
        //setAdapter
        recycler_menu.setAdapter(adapter);
    }

    private void loadOrderFull(String id) {

            reference.child(id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        currentFood=dataSnapshot.getValue(Request1.class);
                        textView.setText(currentFood.getAddress());
                        textView1.setText(currentFood.getCurrentDate());
                        textView2.setText(currentFood.getTotal());
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


    }

//    @Override
//    public void onBackPressed() {
//        startActivity(new Intent(getApplicationContext(),List_All_Order.class));
//        finish();
//    }
}
