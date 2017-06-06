package com.example.sharel.stockwatch;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Sharel on 2/25/2017.
 */

public class StockDataDownloader extends AsyncTask<String,Void,ArrayList<Stock>> {
    private String dataURL ="http://finance.google.com/finance/info" ;
    private static final String TAG = "StockDataDownloader";
    private  String searchText;
    ArrayList<Stock> stockArrayList;
    private MainActivity mainActivity;

    public StockDataDownloader(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        this.stockArrayList = new ArrayList<>();
    }

    @Override
    protected ArrayList<Stock> doInBackground(String... params) {
        searchText = params[0] ;

        Uri dataUri = Uri.parse(dataURL).buildUpon()
                .appendQueryParameter("client", "ig")
                .appendQueryParameter("q", searchText).build();

        String urlToUse = dataUri.toString();

        Log.d(TAG, "doInBackground: " + urlToUse);

        HttpURLConnection conn;
        StringBuilder sb = new StringBuilder();
        try {

            //dataurl = createdataurl(symbol)
            URL url = new URL(urlToUse);

            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            //added in class
            conn.connect();

            //new String ubilder obj

            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

            //call the parser
        DataParser dataParser = new DataParser();
         stockArrayList = dataParser.parseJSON(sb.toString().replace("\n//",""));

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

        return stockArrayList;
    }

    @Override
    protected void onPostExecute(ArrayList<Stock> stocks) {
        mainActivity.updateStock(stocks);
    }
}
