package com.example.sharel.networkgateway;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static String ACTION_NEWS_STORY="ACTION_NEWS_STORY";
    private static String ACTION_MSG_TO_SERVICE="ACTION_MSG_TO_SERVICE";
    NewsReceiver newsReceiver;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private MyPageAdapter pageAdapter;
    private List<Fragment> fragments;
    private ViewPager pager;
    private static final String TAG = "MainActivity";
    private HashMap<String,Sources> srchashMap ;
    private ArrayList<String> items = new ArrayList<>();
    private List categoryList = new ArrayList();
    Menu menuList;
    ListAdapter listAdapter;
    private String currentNews;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(this, "creating", Toast.LENGTH_SHORT).show();

        //start NewService service
        Intent intent = new Intent(MainActivity.this, NewsService.class);
        startService(intent);

         newsReceiver=new NewsReceiver();
         IntentFilter filter1 = new IntentFilter(ACTION_NEWS_STORY);
         registerReceiver(newsReceiver, filter1);

        //Drawer and View Pager setup
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);


        /*
        items.add("CNN");
        items.add("Newsweek");
        items.add("BuzzFeed");*/


        listAdapter = new ArrayAdapter<>(this, R.layout.drawer_list_item, items);
        mDrawerList.setAdapter(listAdapter);

        mDrawerList.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //code here
                selectItem(position);

            }
        });

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.string.drawer_open,
                R.string.drawer_close);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //view pager and fragments setup
        fragments = getFragments();

        pageAdapter = new MyPageAdapter(getSupportFragmentManager());
        pager = (ViewPager) findViewById(R.id.viewpager);
        pager.setAdapter(pageAdapter);

        srchashMap = new HashMap<>();
        //download all categories
        new NewsSourceDownloader(this).execute("");

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
         menuList=menu;
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            Log.d(TAG, "onOptionsItemSelected: mDrawerToggle " + item);
            return true;
        }
        Toast.makeText(this, "Selected " + item, Toast.LENGTH_SHORT).show();

        //Category selected from Options Menu
        new NewsSourceDownloader(this).execute(item.toString());
        Log.d(TAG, "onOptionsItemSelected: Options Menu: "+item.toString());

        return super.onOptionsItemSelected(item);

    }

    private void selectItem(int position) {
        Toast.makeText(this, "You picked "+items.get(position), Toast.LENGTH_SHORT).show();
        pager.setBackground(null);
        setTitle(items.get(position));
        currentNews = items.get(position);

        Intent intent = new Intent();
        intent.setAction(ACTION_MSG_TO_SERVICE);  //ACTION_MSG_TO_SVC

            Sources src = srchashMap.get(items.get(position));
            intent.putExtra("SOURCE_OBJECT",src.getId());
            sendBroadcast(intent);
            mDrawerLayout.closeDrawer(mDrawerList);


    }

    private void reDoFragments(ArrayList<Articles> list) {
        //Set title to the source
        int size = list.size();
        Log.d(TAG, "reDoFragments: Start");
        Toast.makeText(this, "redoFragments", Toast.LENGTH_SHORT).show();
        for (int i = 0; i < pageAdapter.getCount(); i++)
            pageAdapter.notifyChangeInPosition(i);
            fragments.clear();
            for (int i = 0; i < list.size(); i++) {
            Articles a = list.get(i);
            fragments.add(NewsFragment.newInstance(this,a,i,size));
        }

        pageAdapter.notifyDataSetChanged();
        pager.setCurrentItem(0);
    }


    @Override
    protected void onDestroy() {
        Intent intent1 = new Intent(MainActivity.this, NewsReceiver.class);
        stopService(intent1);
        //added
        unregisterReceiver(newsReceiver);
        super.onDestroy();
    }

    private List<Fragment> getFragments() {
        List<Fragment> fList = new ArrayList<Fragment>();
        return fList;
    }

    public void setSources(ArrayList<Sources> sourcesArrayList, List cList){
            if(!sourcesArrayList.isEmpty() ){
                srchashMap.clear();
                items.clear();

                for (int i=0;i<sourcesArrayList.size();i++){
                    Sources  s= sourcesArrayList.get(i);
                    items.add(s.getSource());
                    srchashMap.put(s.getSource(),s);
                }
            }

            if(cList.isEmpty()){
                categoryList.clear();
                categoryList.add("all");

            }
            else if(categoryList.isEmpty()){
                categoryList = cList;
                categoryList.add("all");
            }

            else if(!cList.isEmpty())
            {
                for (int k=0;k<cList.size();k++){
                    if (!categoryList.contains(cList.get(k))){
                        categoryList.add(cList.get(k));
                    }
                }
            }


        menuList.clear();
        for(int j=0;j<categoryList.size();j++){
             menuList.add(categoryList.get(j).toString());
             Log.d(TAG, "setSources: "+menuList.size());
        }

        listAdapter = new ArrayAdapter<>(this, R.layout.drawer_list_item, items);
        mDrawerList.setAdapter(listAdapter);
    }

    //MyPageAdapter class

    private class MyPageAdapter extends FragmentPagerAdapter {
        private long baseId = 0;


        public MyPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public long getItemId(int position) {
            // give an ID different from position when position has been changed
            return baseId + position;
        }

        /**
         * Notify that the position of a fragment has been changed.
         * Create a new ID for each position to force recreation of the fragment
         * @param n number of items which have been changed
         */
        public void notifyChangeInPosition(int n) {
            // shift the ID returned by getItemId outside the range of all previous fragments
            baseId += getCount() + n;
        }

    }

    //Receiver class
    public class NewsReceiver  extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("ACTION_NEWS_STORY")){
                ArrayList<Articles> array =  (ArrayList<Articles>) intent.getSerializableExtra("STORY_LIST");
                Log.d(TAG, "onReceive: NewsReceiver");
                reDoFragments(array);
            }
            Toast.makeText(context, "In NewReceiver Class", Toast.LENGTH_SHORT).show();
        }
    }

    public void openPicassoPhoto(String url, ImageView im) {
        final ImageView imageView = im;
        final String photUrl = url;
        if (url != null) {
            Picasso picasso = new Picasso.Builder(this).listener(new Picasso.Listener() {
                @Override public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                    // Here we try https if the http image attempt failed
                    final String changedUrl = photUrl.replace("http:", "https:");
                    picasso.load(changedUrl)
                            .error(R.drawable.brokenimage)
                            .placeholder(R.drawable.placeholder)
                            .into(imageView);
                }
            }).build();
            picasso.load(url)
                    .resize(360,1060)
                    .centerInside()
                    .error(R.drawable.brokenimage)
                    .placeholder(R.drawable.placeholder)
                    .into(imageView);
            Log.d(TAG, "openPicassoPhoto: "+url);

        } else {
            Picasso.with(this).load(url)
                    .error(R.drawable.brokenimage)
                    .into(imageView);

        }
    }




}

