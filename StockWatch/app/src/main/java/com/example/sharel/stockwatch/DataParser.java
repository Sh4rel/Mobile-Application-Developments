package com.example.sharel.stockwatch;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by Sharel on 2/25/2017.
 */

public class DataParser {
    public ArrayList<Stock> parseJSON(String s) {

        ArrayList<Stock> stockList = new ArrayList<>();
        try {
            JSONArray jObjMain = new JSONArray(s);
            boolean changeValue=false;
            for (int i = 0; i < jObjMain.length(); i++) {
                JSONObject jCountry = (JSONObject) jObjMain.get(i);
                String ticker = jCountry.getString("t");
                double tradePrice = Double.parseDouble(jCountry.getString("l"));

                double priceChange = Double.parseDouble(jCountry.getString("c").substring(1));
                if(jCountry.getString("c").substring(0).contains("-")){
                     changeValue = true;

                }

                double changePercent = Double.parseDouble(jCountry.getString("cp").substring(1));

                //add it to the list to show on screen
                stockList.add(new Stock(ticker,tradePrice,priceChange,changePercent,changeValue));

            }
            return stockList;
        } catch (Exception e) {
            Log.d(TAG, "parseJSON: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
