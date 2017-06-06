package com.example.sharel.networkgateway;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Sharel on 4/18/2017.
 */

public class NewsService extends Service {
    private static String ACTION_MSG_TO_SERVICE="ACTION_MSG_TO_SERVICE";
    private static String ACTION_NEWS_STORY = "ACTION_NEWS_STORY";
    ServiceReceiver serviceReceiver;
    private boolean running = true;
    private static final String TAG = "NewsService";
    ArrayList<Articles> storyList = new ArrayList<>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "NewsService: onStartCommand: Service started");

        serviceReceiver = new ServiceReceiver();
        IntentFilter filter = new IntentFilter(ACTION_MSG_TO_SERVICE);
        registerReceiver(serviceReceiver, filter);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(running){
                     while (storyList.isEmpty()){
                         try{
                             Thread.sleep(250);
                         }
                         catch (InterruptedException e){
                             e.printStackTrace();
                         }
                     }

                     if(!storyList.isEmpty()){

                         Intent intent = new Intent();
                         intent.setAction(ACTION_NEWS_STORY);
                         intent.putExtra("STORY_LIST",storyList);
                         sendBroadcast(intent);

                         //Resume from here!!!!!
                     }
                    storyList.clear();
                    Log.d(TAG, "run: ");
                }
            }
        }).start();

        return START_STICKY;
    }

    public void setArticles(ArrayList<Articles> articlesList){
        if(!articlesList.isEmpty()){
            storyList.clear();
            storyList = articlesList;
        }

    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Destroying the service of NewsService!", Toast.LENGTH_SHORT).show();
        unregisterReceiver(serviceReceiver);
        running=false;
        super.onDestroy();
    }

     public class ServiceReceiver extends BroadcastReceiver{

         @Override
         public void onReceive(Context context, Intent intent) {

             if(intent.getAction().equals(ACTION_MSG_TO_SERVICE)){

                   String sourceId = intent.getStringExtra("SOURCE_OBJECT");
                 new NewsArticleDownloader(NewsService.this).execute(sourceId);
             }


         }
     }



}
