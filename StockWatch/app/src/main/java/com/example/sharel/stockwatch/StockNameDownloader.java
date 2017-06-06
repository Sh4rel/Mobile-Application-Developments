package com.example.sharel.stockwatch;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.security.auth.login.LoginException;

/**
 * Created by Sharel on 2/25/2017.
 */

public class StockNameDownloader extends AsyncTask<String,Void,ArrayList<Stock>> {
    private static final String TAG = "StockNameDownloader";
    private final String dataURL = "http://stocksearchapi.com/api";
    private String api_key="2d89c66fb63860ddc48b637410b38882ea01252f";
    private String search_text;
    private MainActivity mainActivity;
    private ArrayList<Stock> stringList;

    public StockNameDownloader(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        this.stringList=new ArrayList<>();
    }

    @Override
    protected ArrayList<Stock> doInBackground(String... params) {

        search_text = params[0] ;

        Uri dataUri = Uri.parse(dataURL).buildUpon()
                         .appendQueryParameter("api_key", api_key)
                .appendQueryParameter("search_text", search_text).build();

        String urlToUse = dataUri.toString();

        Log.d(TAG, "doInBackground: " + urlToUse);

        HttpURLConnection conn;
        StringBuilder sb = new StringBuilder();
        try {

            URL url = new URL(urlToUse);

            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            //added in class
            conn.connect();

            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

            //call the parser
            NameParser nameparser = new NameParser();
             stringList = nameparser.parseJSON(sb.toString());

            Log.d(TAG, "doInBackground: " + sb.toString());

        } catch (Exception e) {
            Log.e(TAG, "doInBackground: ", e);
            return null;
        }

        finally {
            //if(){
                //conn.disconnect();
                //close reader obj and connection.

            //}
        }

        Log.d(TAG, "doInBackground: " + sb.toString());

        return stringList;
    }


    @Override
    protected void onPostExecute(ArrayList<Stock> stringList) {
        mainActivity.addStockSymbol(stringList);
    }
}
