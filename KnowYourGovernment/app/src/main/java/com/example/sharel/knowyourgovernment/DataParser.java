package com.example.sharel.knowyourgovernment;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Sharel on 4/6/2017.
 */

public class DataParser {
    private static final String TAG = "DataParser";
    public void parseJSON(String s) {


        ArrayList<Official> officialList = new ArrayList<>();
        try {
            JSONArray jObjMain = new JSONArray(s);
            for (int i = 0; i < jObjMain.length(); i++) {
                JSONObject jCountry = (JSONObject) jObjMain.get(i);
             /*   String ticker = jCountry.getString("t");
                double tradePrice = Double.parseDouble(jCountry.getString("l"));

                double priceChange = Double.parseDouble(jCountry.getString("c").substring(1));
                if(jCountry.getString("c").substring(0).contains("-")){
                    changeValue = true;

                }

                double changePercent = Double.parseDouble(jCountry.getString("cp").substring(1));

                //add it to the list to show on screen
*/
            }
            return ;
        } catch (Exception e) {
            Log.d(TAG, "parseJSON: " + e.getMessage());
            e.printStackTrace();
            return ;
        }
    }
}
