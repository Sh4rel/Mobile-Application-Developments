package com.example.sharel.knowyourgovernment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,View.OnLongClickListener {

    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
     RecyclerViewAdpater recyclerViewAdpater;
    private List<Official> officialList = new ArrayList<>();
    private  Official official;
    private Locator locator;
    private String zip;
    private boolean network;
    TextView header;
    private static final String NO_LOC = "No Data for Location";
    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_activity);
        header = (TextView) findViewById(R.id.header);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);

        network = networkCheck(this);
        if (!network) {
            header.setText(NO_LOC);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Data cannot be loaded/accessed without an internet connection.");
            builder.setTitle("No Network Connection");
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {

            //recycler view setup
           recyclerViewAdpater = new RecyclerViewAdpater(officialList, this);
            recyclerView.setAdapter(recyclerViewAdpater);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            locator = new Locator(this);

            ///load data
            officialList.add(new Official("Donald Trump","President of the United States","Republican",
                    "NYC, USA", "312-223-2311", "dtrump@president.usa.com","www.trump.com",
                    ""));
            officialList.add(new Official("Mike Pence","Vice-President of the United States","Democratic",
                    "NYC, USA", "312-223-2311", "mpence@president.usa.com","www.mike.com",
                    ""));
            officialList.add(new Official("Hillary Clinton","United States Senate","Democratic",
                    "NYC, USA", "312-223-2311", "hclin@president.usa.com","www.hillcill.com",
                    ""));
            officialList.add(new Official("Ducky Tamma","United States Senate","Republican",
                    "NYC, USA", "312-223-2311", "dtamma@president.usa.com","www.tamma.com",
                    ""));
            officialList.add(new Official("Bruce Rauner","Governor","Republican",
                    "NYC, USA", "312-223-2311", "dtamma@president.usa.com","www.tamma.com",
                    ""));
            officialList.add(new Official("Ducky Tamma","United States Senate","Democratic",
                    "NYC, USA", "312-223-2311", "dtamma@president.usa.com","www.tamma.com",
                    ""));
          }
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick: ");
        Toast.makeText(this, "You clicked short", Toast.LENGTH_SHORT).show();
        int pos = recyclerView.getChildLayoutPosition(v);
        Official o = officialList.get(pos);
        Intent intent=new Intent(MainActivity.this, OfficalActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, OfficalActivity.class.getSimpleName());
       intent.putExtra("LOCATION", header.getText());
        intent.putExtra("Person",o);
        Log.d(TAG, "onClick: "+o.getOffice());
       startActivity(intent);
    }

    @Override
    public boolean onLongClick(View v) {
        Toast.makeText(this, "You clicked long", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu: ");
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
        //super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected: ");
        switch (item.getItemId()) {

            case R.id.app_bar_search:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                final EditText et = new EditText(this);
                et.setInputType(InputType.TYPE_CLASS_TEXT);
                et.setGravity(Gravity.CENTER_HORIZONTAL);

                builder.setView(et);
               // builder.setIcon(R.drawable);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                     doLocationName(et.getText().toString());
                        Toast.makeText(MainActivity.this, "Searching for "+et.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("NO WAY", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

                builder.setMessage("Enter a city & state, or a ZIP");
                builder.setTitle("Details");

                AlertDialog dialog = builder.create();
                dialog.show();
                return true;

            case R.id.about:
                Intent intent1=new Intent( MainActivity.this, AboutActivity.class);
                intent1.putExtra(Intent.EXTRA_TEXT, AboutActivity.class.getSimpleName());
                startActivity(intent1);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

      //Network Connectivity check
    public boolean networkCheck(Context context) {
        if (context == null) {
            return true;
        }
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
           // Toast.makeText(context, "You are NOT Connected to the Internet!", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Log.d(TAG, "onRequestPermissionsResult: CALL: " + permissions.length);
        Log.d(TAG, "onRequestPermissionsResult: PERM RESULT RECEIVED");

        if (requestCode == 5) {
            Log.d(TAG, "onRequestPermissionsResult: permissions.length: " + permissions.length);
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG, "onRequestPermissionsResult: HAS PERM");
                        locator.setUpLocationManager();
                        locator.determineLocation();
                    } else {
                        Toast.makeText(this, "Location permission was denied - cannot determine address", Toast.LENGTH_LONG).show();
                        Log.d(TAG, "onRequestPermissionsResult: NO PERM");
                    }
                }
            }
        }
        Log.d(TAG, "onRequestPermissionsResult: Exiting onRequestPermissionsResult");
    }

    //call from the Locator class
     String doAddress(double latitude, double longitude) {

        Log.d(TAG, "doAddress: Lat: " + latitude + ", Lon: " + longitude);

        List<Address> addresses;
        for (int times = 0; times < 3; times++) {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                Log.d(TAG, "doAddress: Getting address now");


                addresses = geocoder.getFromLocation(latitude, longitude, 1);


                StringBuilder sb = new StringBuilder();
                for (Address ad : addresses) {
                    Log.d(TAG, "doLocation: " + ad);
                    zip = ad.getPostalCode();

                    sb.append("\nAddress\n\n");
                    for (int i = 0; i < ad.getMaxAddressLineIndex(); i++)
                        sb.append("\t" + ad.getAddressLine(i) + "\n");

                    sb.append("\t" + ad.getCountryName() + " (" + ad.getCountryCode() + ")\n");

                }

                Log.d(TAG, "Address is: "+sb.toString());
                //call data downloader

                if(zip!=null)
                new CivicInfoDownloader(this).execute(zip);

                return sb.toString();
            } catch (IOException e) {
                Log.d(TAG, "doAddress: " + e.getMessage());

            }
            Toast.makeText(this, "GeoCoder service is slow - please wait", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(this, "GeoCoder service timed out - please try again", Toast.LENGTH_LONG).show();
        return null;
    }
    public void noLocationAvailable() {
        Toast.makeText(this, "No location providers were available", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        locator.shutdown();
        super.onDestroy();
    }

    public void doLocationName( String loc) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses, addresses1;

            addresses = geocoder.getFromLocationName(loc, 1);
            if (addresses.size() == 0) {
                header.setText(NO_LOC);
                Toast.makeText(this, "Address cannot be acquired for given location.", Toast.LENGTH_SHORT).show();
                // ((TextView) findViewById(R.id.textView)).setText("Nothing Found");
                return;
            }

            for (Address address : addresses) {
             //   for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                    Log.d(TAG, "doLocationName:address.getMaxAddressLineIndex()="+address.getMaxAddressLineIndex());
                    Log.d(TAG, "doLocationName: "+address);
                    addresses1=geocoder.getFromLocation(address.getLatitude(), address.getLongitude(),1);
                    displayAddresses(addresses1);

              //  }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayAddresses(List<Address> addresses) {
        StringBuilder sb = new StringBuilder();
        if (addresses.size() == 0) {
            header.setText(NO_LOC);
            Toast.makeText(this, "Address cannot be acquired for given location.", Toast.LENGTH_SHORT).show();
            // ((TextView) findViewById(R.id.textView)).setText("Nothing Found");
            return;
        }

        for (Address address : addresses) {
            Log.d(TAG, "displayAddresses: "+address);
            for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                sb.append(address.getAddressLine(i) + "\n");
                zip = address.getPostalCode();
            }
            sb.append("\n");
        }

        if(zip!=null){
            new CivicInfoDownloader(this).execute(zip);

        }
        // zip = "60616";
        Log.d(TAG, "displayAddresses: "+sb.toString());
    }

    public  void loadData(String cityCode, ArrayList<Official> displayList){
        if(cityCode==null || cityCode.isEmpty()){
            header.setText(NO_LOC);
        }
        else{
            header.setText(cityCode);
        }

        officialList.clear();
      //  if(!displayList.isEmpty()) {
            officialList = displayList;
            Log.d(TAG, "loadData: ");
            recyclerViewAdpater = new RecyclerViewAdpater(officialList, this);
            recyclerView.setAdapter(recyclerViewAdpater);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
           //  recyclerViewAdpater.notifyDataSetChanged();
      //  }
    }
}
