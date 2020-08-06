package com.hasan.order_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
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
import android.text.Html;
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
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import com.itextpdf.text.pdf.languages.BanglaGlyphRepositioner;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.w3c.dom.Document;

import java.io.File;
import java.io.FileNotFoundException;
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
        Paint titlePaint=new Paint();
        Paint titlePaint1=new Paint();
        Paint totalPain=new Paint();

        PdfDocument.PageInfo myPageInfo1=new PdfDocument.PageInfo.Builder(1200,2010,1).create();
        PdfDocument.Page myPage1=pdfDocument.startPage(myPageInfo1);
        Canvas canvas=myPage1.getCanvas();

        titlePaint.setTextAlign(Paint.Align.CENTER);
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
        titlePaint.setTextSize(50);
        canvas.drawText("মেসার্স লিহান এন্টারপ্রাইজ",pageWidth/2,75,titlePaint);

        titlePaint1.setTextAlign(Paint.Align.CENTER);
        titlePaint1.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
        titlePaint1.setTextSize(40);
        canvas.drawText("রাধানগর বাঁজার, শ্রীপুর, মাগুরা",pageWidth/2,125,titlePaint1);

        titlePaint1.setTextAlign(Paint.Align.CENTER);
        titlePaint1.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
        titlePaint1.setTextSize(40);
        canvas.drawText("মোবাঃ ০১৭২৫-৮৭৪৯৯৭ , ০১৯২০-৬৪১৩৭৯",pageWidth/2,175,titlePaint1);

        titlePaint.setTextAlign(Paint.Align.CENTER);
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
        titlePaint.setTextSize(60);
        canvas.drawText("Order Details",pageWidth/2,300,titlePaint);

        myPaint.setTextAlign(Paint.Align.LEFT);
        myPaint.setTextSize(22f);
        myPaint.setColor(Color.BLACK);
        canvas.drawText("Cus. Details : "+textView.getText().toString(),30,350,myPaint);
        canvas.drawText("Order Date: "+textView1.getText().toString(),30,400,myPaint);
        canvas.drawText("Order By: "+currentFood.getName(),600,400,myPaint);

        myPaint.setStyle(Paint.Style.STROKE);
        myPaint.setStrokeWidth(2);
        canvas.drawRect(20,425,pageWidth-20,505,myPaint);
        myPaint.setTextAlign(Paint.Align.LEFT);
        myPaint.setStyle(Paint.Style.FILL);
        canvas.drawText("Si. No. ",40,475,myPaint);
        canvas.drawText("Item Desc. ",200,475,myPaint);
        canvas.drawText("Price ",700,475,myPaint);
        canvas.drawText("Qnt. ",870,475,myPaint);
        canvas.drawText("Total. ",950,475,myPaint);

        int x=0;
        for (int i=0;i<adapter.getItemCount();i++){
            canvas.drawText(""+(i+1),40,x+550,myPaint);
            canvas.drawText(""+adapter.getItem(i).getProductName(),200,x+550,myPaint);
            canvas.drawText(""+adapter.getItem(i).getPrice(),700,x+550,myPaint);
            canvas.drawText(""+adapter.getItem(i).getQuantity(),870,x+550,myPaint);
            Locale locale=new Locale("en","BD");
            NumberFormat fmt=NumberFormat.getCurrencyInstance(locale);
            double total=Double.parseDouble(adapter.getItem(i).getQuantity())*Double.parseDouble(adapter.getItem(i).getPrice());
            canvas.drawText(""+fmt.format(total),950,x+550,myPaint);
            x+=50;

        }


        totalPain.setTextSize(30f);
        canvas.drawLine(680,(x+550),pageWidth-20,(x+550),myPaint);
        canvas.drawText("Sub Total",700,(x+550+50),totalPain);
        canvas.drawText(":",900,(x+550+50),totalPain);
        canvas.drawText(""+textView2.getText().toString(),950,(x+550+50),totalPain);

        //canvas.drawText(""+x,1050,1900,myPaint);

        canvas.drawLine(680,1900,pageWidth-20,1900,myPaint);
        canvas.drawText("Signature",750,1950,myPaint);


        canvas.drawLine(5,5,1195,5,myPaint);
        canvas.drawLine(5,5,5,2005,myPaint);
        canvas.drawLine(5,2005,1195,2005,myPaint);
        canvas.drawLine(1195,5,1195,2005,myPaint);






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
                        samiulViewHolder.samiulQuantity.setText("Quantity: "+order.getQuantity()+"pic");
                        //samiulViewHolder.samiulPrice.setText(order.getPrice());
                        Locale locale=new Locale("en","BD");
                        NumberFormat fmt=NumberFormat.getCurrencyInstance(locale);
                        double x=Double.parseDouble(order.getPrice())*Double.parseDouble(order.getQuantity());
                        samiulViewHolder.samiulPrice.setText(fmt.format(x));

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

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),List_All_Order.class));
        finish();
    }
}
