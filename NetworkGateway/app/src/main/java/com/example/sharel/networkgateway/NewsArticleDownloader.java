package com.example.sharel.networkgateway;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sharel on 4/21/2017.
 */

public class NewsArticleDownloader extends AsyncTask<String,Void,ArrayList<Articles>>{


    private String dataURL ="https://newsapi.org/v1/articles";
    private String key="75612a4d212a4604bdc854f666430b5e";
    private static final String TAG = "NewsArticleDownloader";

    private ArrayList<Articles> articlesArrayList;
    private NewsService newsService ;
    private  String source;


    public NewsArticleDownloader(NewsService newsService) {
        this.newsService = newsService;
        this.articlesArrayList = new ArrayList<>();
    }

    @Override
    protected ArrayList<Articles> doInBackground(String... params) {
        Log.d(TAG, "doInBackground: NewsArticleDownloader");
        source =params[0];

        Uri dataUri = Uri.parse(dataURL).buildUpon()
                .appendQueryParameter("source",source)
                .appendQueryParameter("apiKey",key).build();

        String urlToUse = dataUri.toString();

        Log.d(TAG, "doInBackground: NewsArticleDownloader" + urlToUse);

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

            //Parse below the received string
            String s = sb.toString().replace("\n//","");

            try {
                JSONObject object= new JSONObject(s);
                JSONArray jsonArray = (JSONArray) object.get("articles");
                  for (int i=0;i<jsonArray.length();i++){
                       JSONObject jObj = (JSONObject) jsonArray.get(i);
                      String author = jObj.getString("author");
                      String title = jObj.getString("title");
                     String desc = jObj.getString("description");
                     String urlArticle = jObj.getString("url");
                     String urlImage = jObj.getString("urlToImage");
                      String publish = jObj.getString("publishedAt");
                      articlesArrayList.add(new Articles(author,title,desc,urlArticle,urlImage,publish));
                  }

                Log.d(TAG, "doInBackground: articlesArrayList Size is "+articlesArrayList.size());

            } catch (Exception e) {
                Log.d(TAG, "parseJSON: " + e.getMessage());
                e.printStackTrace();
            }

            Log.d(TAG, "doInBackground: NewsArticleDownloader" + sb.toString());
        }
        catch (FileNotFoundException e){
            Log.e(TAG, "doInBackground: FileNotFoundException in NewsArticleDownloader",e );
        }
        catch (Exception e) {
            Log.e(TAG, "doInBackground: NewsArticleDownloader", e);
            return null;
        }
        finally {
            //if(){
            //conn.disconnect();
            //close reader obj and connection.
            //}
        }
        Log.d(TAG, "doInBackground: NewsArticleDownloader" + sb.toString());
        return articlesArrayList;
    }


    @Override
    protected void onPostExecute(ArrayList<Articles> strings) {
        if(strings!=null)
        newsService.setArticles(strings);
        super.onPostExecute(strings);
    }



}
