package com.freedom.order_application;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class ListenOrder extends Service implements ChildEventListener {

    FirebaseDatabase db;
    DatabaseReference order;


    @Override
    public void onCreate() {
        super.onCreate();
        db=FirebaseDatabase.getInstance();
        order=db.getReference("Requests");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        order.addChildEventListener(this);
        return super.onStartCommand(intent, flags, startId);
    }

    public ListenOrder() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        //Trigger Here

        Request1 request1=dataSnapshot.getValue(Request1.class);
        showNotification(dataSnapshot.getKey(),request1);
    }

    private void showNotification(String key, Request1 request1) {
        Intent intent=new Intent(getBaseContext(),Order_show.class);
        PendingIntent contentIntent=PendingIntent.getActivity(getBaseContext(),0,intent,0);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(getBaseContext());
        builder.setAutoCancel(true).setDefaults(Notification.DEFAULT_ALL).setTicker("Samiul")
        .setContentInfo("New Order Placed")
                .setContentText("You Have New Order #"+key).setSmallIcon(R.mipmap.ic_launcher).setContentIntent(contentIntent);

        NotificationManager manager= (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);

        int randomInt=new Random().nextInt(9999-1)+1;
        manager.notify(randomInt,builder.build());

    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
}
