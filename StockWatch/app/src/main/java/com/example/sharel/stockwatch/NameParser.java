package com.example.sharel.stockwatch;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by Sharel on 2/25/2017.
 */

public class NameParser {
    public ArrayList<Stock> parseJSON(String s) {

        ArrayList<Stock> stockList = new ArrayList<>();
        try {
            JSONArray jObjMain = new JSONArray(s);

            for (int i = 0; i < jObjMain.length(); i++) {
                JSONObject jCountry = (JSONObject) jObjMain.get(i);
                String symbol = jCountry.getString("company_symbol");
                String name = jCountry.getString("company_name");

                //add it to the list to show on screen
                //stockList.add(symbol +"-"+name);
                stockList.add(new Stock(symbol,name));

            }
            return stockList;
        } catch (Exception e) {
            Log.d(TAG, "parseJSON: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
