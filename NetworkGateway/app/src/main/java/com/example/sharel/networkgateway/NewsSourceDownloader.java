package com.example.sharel.networkgateway;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ListAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Sharel on 4/18/2017.
 */

public class NewsSourceDownloader extends AsyncTask<String,Void,ArrayList<Sources>> {

    private String dataURL ="https://newsapi.org/v1/sources";
    private String key="75612a4d212a4604bdc854f666430b5e";
    private static final String TAG = "NewsSourceDownloader";

    private  ArrayList<Sources> sourceArrayList;
    private  MainActivity mainActivity;
   private  String cat;


    public NewsSourceDownloader(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        this.sourceArrayList = new ArrayList<>();
    }

    @Override
    protected ArrayList<Sources> doInBackground(String... params) {
        if(params[0].isEmpty() || params[0].equals("all"))
        cat = "";
        else
            cat =params[0];

        Uri dataUri = Uri.parse(dataURL).buildUpon()
                .appendQueryParameter("language", "en")
                .appendQueryParameter("country", "us")
                .appendQueryParameter("category", cat)
                .appendQueryParameter("apiKey",key).build();

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

            //new String builder obj
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

            //call the parser
           // DataParser dataParser = new DataParser();
           // dataParser.parseJSON(sb.toString().replace("\n//",""));
            String s = sb.toString().replace("\n//","");



            try {
                JSONObject object= new JSONObject(s);
                JSONArray js =new JSONArray(object.getString("sources"));
                    for (int i=0;i<js.length();i++){
                        JSONObject jObj = (JSONObject) js.get(i);
                            String name =  jObj.getString("name");
                        String id = jObj.getString("id");
                        String desc = jObj.getString("description");
                        String sourceUrl = jObj.getString("url");
                        String category = jObj.getString("category");

                        sourceArrayList.add(new Sources(id,name,desc,sourceUrl,category));
                    }

                Log.d(TAG, "doInBackground: sourceArrayList Size is "+sourceArrayList.size());

            } catch (Exception e) {
                Log.d(TAG, "parseJSON: " + e.getMessage());
                e.printStackTrace();

            }

            Log.d(TAG, "doInBackground: " + sb.toString());

        }
        catch (FileNotFoundException e){
            Log.e(TAG, "doInBackground: FileNotFoundException ",e );
        }
        catch (Exception e) {
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
        return sourceArrayList;
    }




    @Override
    protected void onPostExecute(ArrayList<Sources> s) {
        List categoryList = new ArrayList();
        if (s != null) {
            try {
                Sources temp = s.get(0);
                categoryList.add(temp.getCategory());

                for (int j = 1; j < s.size(); j++) {
                    Sources sources = s.get(j);
                    if (!categoryList.contains(sources.getCategory())) {
                        categoryList.add(sources.getCategory());
                    }
                }


                //push to MainActivity
                if (!categoryList.isEmpty() && !s.isEmpty()) {
                    mainActivity.setSources(s, categoryList);

                }
            }
            catch (Exception e){
                Log.e(TAG, "Error: onPostExecute: ", e );
                e.printStackTrace();
            }
        }
    }

}
