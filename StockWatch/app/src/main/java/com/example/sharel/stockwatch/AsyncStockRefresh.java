package com.example.sharel.stockwatch;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sharel on 2/25/2017.
 */

public class AsyncStockRefresh extends AsyncTask<String,Void,String> {
    List<Stock> stocks= new ArrayList<>();
    private int count;


    private static final String TAG = "AsyncStockRefresh";
    private MainActivity mainActivity;
    private RecyclerViewAdpater recyclerViewAdpater;
    private SwipeRefreshLayout swiper;
    private String dataURL="www.google.com";


    public AsyncStockRefresh(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public AsyncStockRefresh(MainActivity ma, List<Stock> st, SwipeRefreshLayout sw) {
        mainActivity=ma;
        stocks=st;
        swiper=sw;
    }

    @Override
    protected void onPostExecute(String s) {
        //Stock stock = parseJSON(s); // create a class to parse json
                  //s.replace("\n//","");
        //return it to the main activity in addStovks method



       // ------------------------------
        //check for list returned to be empty or just 1
        //if many display the list and select 1


//        recyclerViewAdpater.notifyDataSetChanged();
    //    swiper.setRefreshing(false);
    }

    @Override
    protected String doInBackground(String... params) {
        ////////////////////////////////////
       //in stock downloader //params[0]
        //params[1]
        //uri.parse .buildUpon .appendqueryparamater(lhs,rhs) .build .toString
        //http://finanace

        /////////////////////////////////


        Uri dataUri = Uri.parse(dataURL);
        String urlToUse = dataUri.toString();
        Log.d(TAG, "doInBackground: " + urlToUse);
        HttpURLConnection conn;
        StringBuilder sb = new StringBuilder();
        try {

            //dataurl = createdataurl(symbol)
            URL url = new URL(urlToUse);

             conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            //addded in class
            //conn.connect();

            //new String builder obj

            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

            //call the parser

            Log.d(TAG, "doInBackground: " + sb.toString());

        } catch (Exception e) {
            Log.e(TAG, "doInBackground: ", e);
            return null;
        }
        finally {
            //if()
                //close reader obj and connection.
        }

        Log.d(TAG, "doInBackground: " + sb.toString());

        return sb.toString();
    }

    private ArrayList<Stock> parseJSON(String s) {

        ArrayList<Stock> stockList = new ArrayList<>();
        try {
            JSONArray jObjMain = new JSONArray(s);
            count = jObjMain.length();


            for (int i = 0; i < jObjMain.length(); i++) {
                JSONObject jCountry = (JSONObject) jObjMain.get(i);
                String name = jCountry.getString("name");
                String capital = jCountry.getString("capital");


             //add it to the list to show on screen
              /*  stockList.add(
                        new Stock());*/

            }
            return stockList;
        } catch (Exception e) {
            Log.d(TAG, "parseJSON: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

}
