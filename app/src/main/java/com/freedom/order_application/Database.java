package com.freedom.order_application;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteAssetHelper {
    private static final String DB_NAME="MyDatabase.db";
    private static final int DB_VERSION=4;
    public Database(Context context) {
        super(context,DB_NAME,null, DB_VERSION);
    }
    public List<Order> getCarts(){
        SQLiteDatabase db=getReadableDatabase();
        SQLiteQueryBuilder qb=new SQLiteQueryBuilder();

        String[] sqlSelect={"ProductId","ProductName","Quantity","Price","QntType","Free"};
        String sqlTable="OrderDetails";

        qb.setTables(sqlTable);
        Cursor c=qb.query(db,sqlSelect,null,null,null,null,null);

        final List<Order> result=new ArrayList<>();
        if (c.moveToFirst()){
            do{
                result.add(new Order(c.getString(c.getColumnIndex("ProductId")),
                        c.getString(c.getColumnIndex("ProductName")),
                        c.getString(c.getColumnIndex("Quantity")),
                        c.getString(c.getColumnIndex("Price")),
                        c.getString(c.getColumnIndex("QntType")),
                        c.getString(c.getColumnIndex("Free"))


                ));
            }while (c.moveToNext());
        }
        return result;
    }
    public void addToCart(Order order){
        SQLiteDatabase db=getReadableDatabase();
        String query=String.format("INSERT INTO OrderDetails(ProductId,ProductName,Quantity,Price,QntType,Free) VALUES ('%s','%s','%s','%s','%s','%s');",
                order.getProductId(),
                order.getProductName(),
                order.getQuantity(),
                order.getPrice(),
                order.getQntType(),
                order.getFree()
        );
        db.execSQL(query);
    }
    public void cleanCart(){
        SQLiteDatabase db=getReadableDatabase();
        String query=String.format("DELETE FROM OrderDetails");
        db.execSQL(query);
    }
}
