package com.example.sharel.stockwatch;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sharel on 2/27/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper{
    private static final String TAG = "DatabaseHandler";

    private SQLiteDatabase database;

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    // DB Name
    private static final String DATABASE_NAME = "StockAppDB";
    // DB Table Name
    private static final String TABLE_NAME = "StockWatch";
    ///DB Columns
    private static final String STOCK_SYMBOL = "StockSymbol";
    private static final String COMPANY_NAME = "CompanyName";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        database = getWritableDatabase();
        Log.d(TAG, "DatabaseHandler: ");
    }

    // DB Table Create Code
    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    STOCK_SYMBOL + " TEXT not null unique," +
                    COMPANY_NAME + " TEXT not null)";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


    }


    //Arraylist of String
       public ArrayList<String[]> loadStocks(){
           ArrayList<String[]>  sList = new ArrayList<>();
        Cursor cursor = database.query(
                TABLE_NAME,  // The table to query
                new String[]{STOCK_SYMBOL, COMPANY_NAME}, // The columns to return
                null, // The columns for the WHERE clause
                null, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                null); // The sort order

        if(cursor!=null){
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {

                String[] stockList = new String[2];

                String symbol = cursor.getString(0);
                String company = cursor.getString(1);
                stockList[0] = symbol;
                stockList[1] = company;

                sList.add(stockList);
                cursor.moveToNext();
            }
            cursor.close();
        }

        return sList;
    }

    public void addStock(ArrayList<String[]> stock) {
        String[] st ;

        for (int i = 0; i < stock.size(); i++) {
            st = stock.get(i);

            Log.d(TAG, "\naddStock: Adding " + st[0]);

            ContentValues values = new ContentValues();
            values.put(STOCK_SYMBOL, st[0]);
            values.put(COMPANY_NAME, st[1]);

            long key = database.insert(TABLE_NAME, null, values);
            Log.d(TAG, "addStock: Add Complete " + key);
        }
    }



    public void deleteStock(String name) {
        Log.d(TAG, "deleteStock: Deleting Stock" + name);
        int cnt = database.delete(TABLE_NAME, STOCK_SYMBOL + " = ?", new String[]{name});
        Log.d(TAG, "deleteStock: Delete complete. " + cnt);
    }

    public void updateStock(Stock stock) {
        ContentValues values = new ContentValues();
        values.put(STOCK_SYMBOL, stock.getStockSymbol());
        values.put(COMPANY_NAME, stock.getCompanyName());

        long key = database.update(
                TABLE_NAME, values, STOCK_SYMBOL + " = ?", new String[]{stock.getStockSymbol()});

        Log.d(TAG, "updateStock: " + key);
    }



    public void dumpLog() {
        Cursor cursor = database.rawQuery("select * from " + TABLE_NAME, null);
        if (cursor != null) {
            cursor.moveToFirst();

            Log.d(TAG, "dumpLog: vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv");
            for (int i = 0; i < cursor.getCount(); i++) {
                String stocksymbol = cursor.getString(0);
                String company = cursor.getString(1);


                Log.d(TAG, "dumpLog: " +
                        String.format("%s %-18s", STOCK_SYMBOL + ":", stocksymbol) +
                        String.format("%s %-18s", COMPANY_NAME + ":", company) );
                        cursor.moveToNext();
            }
            cursor.close();
        }

        Log.d(TAG, "dumpLog: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
    }
}
