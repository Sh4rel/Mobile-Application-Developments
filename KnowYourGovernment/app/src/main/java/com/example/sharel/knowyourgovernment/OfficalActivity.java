package com.example.sharel.knowyourgovernment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.util.Linkify;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.*;


/**
 * Created by Sharel on 4/3/2017.
 */

public class OfficalActivity extends AppCompatActivity {
    TextView header;
    TextView office;
    TextView name;
    TextView party;
    TextView email;
    TextView phone;
    TextView website;
    TextView address;
    Official official;
    ImageButton imageButton, im1, im2, im3, im4;
    String location;
    String color;
    private static final String TAG = "OfficalActivity";
    private static final String NO_DATA = "No Data Provided";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official);



        if(getActionBar()!=null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }


        ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.layoutPerson);

        header = (TextView) findViewById(R.id.location);
        office = (TextView) findViewById(R.id.office);
        name = (TextView) findViewById(R.id.name);
        party = (TextView) findViewById(R.id.party);
        website = (TextView) findViewById(R.id.url);
        email = (TextView) findViewById(R.id.email);
        phone = (TextView) findViewById(R.id.phone);
        address = (TextView) findViewById(R.id.address);
        imageButton = (ImageButton) findViewById(R.id.imageButton);
        im1 = (ImageButton) findViewById(R.id.imageButton3);
        im2 = (ImageButton) findViewById(R.id.imageButton4);
        im3 = (ImageButton) findViewById(R.id.imageButton5);
        im4 = (ImageButton) findViewById(R.id.imageButton6);


        Intent intent = getIntent();
        location = intent.getStringExtra("LOCATION");

        header.setText(location);
        official = (Official) intent.getSerializableExtra("Person");

        Log.d(TAG, "Official Activity: onCreate: " + official.getOffice());
        office.setText(official.getOffice());
        name.setText(official.getName());
        String value = "("+ official.getParty()+")";
        party.setText(value);

       //address
        if(official.getAddress()==null || official.getAddress().isEmpty()){
            address.setText(NO_DATA);

        }
        else{
            address.setText(official.getAddress());
        }
        //email
        if(official.getEmail()==null || official.getEmail().isEmpty()){
            email.setText(NO_DATA);

        }
        else{
            email.setText(official.getEmail());
        }

        //phone
        if(official.getPhone()==null || official.getPhone().isEmpty()){
            phone.setText(NO_DATA);

        }
        else{
            phone.setText(official.getPhone());
        }

        //website
        if(official.getWebsite()==null || official.getWebsite().isEmpty()){
            website.setText(NO_DATA);
        }
        else{
            website.setText(official.getWebsite());
        }

        if(official.getPhoto().equals("")){
            imageButton.setImageResource(R.drawable.missingimage);
        }
        else if (official.getPhoto() != null || (!official.getPhoto().isEmpty())) {
            openPicassoPhoto(official.getPhoto());
        }


        if(official.getYouTube()== null || official.getYouTube().isEmpty()){
            im1.setVisibility(View.INVISIBLE);
        }
        else{
            im1.setVisibility(View.VISIBLE);
            im1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(OfficalActivity.this, "Redirecting to YouTube", Toast.LENGTH_LONG).show();
                    youTubeClicked(official.getYouTube());
                }
            });
        }

        if(official.getFaceBook()== null){
            im2.setVisibility(View.INVISIBLE);
        }
        else{
            im2.setVisibility(View.VISIBLE);
            im2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(OfficalActivity.this, "Redirecting to FB", Toast.LENGTH_LONG).show();
                    facebookClicked(official.getFaceBook());
                }
            });
        }

        if(official.getTwitter()== null){
            im4.setVisibility(View.INVISIBLE);
        }
        else{
            im4.setVisibility(View.VISIBLE);
            im4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(OfficalActivity.this, "Redirecting to Twitter", Toast.LENGTH_LONG).show();
                    twitterClicked(official.getTwitter());
                }
            });
        }
        if(official.getgPLus()== null){
            im3.setVisibility(View.INVISIBLE);
        }
        else{
            im3.setVisibility(View.VISIBLE);
            im3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(OfficalActivity.this, "Redirecting to GooglePlus", Toast.LENGTH_LONG).show();
                    googlePlusClicked(official.getgPLus());
                }
            });
        }
        //change to switch here

        if (official.getParty().equals("Republican")) {
            constraintLayout.setBackgroundColor(Color.RED);
            color="Red";
        } else if (official.getParty().equals("Democratic")) {
            constraintLayout.setBackgroundColor(Color.BLUE);
            color="Blue";
        } else {
            constraintLayout.setBackgroundColor(Color.BLACK);
            color="Black";
        }

        Linkify.addLinks(website, Linkify.WEB_URLS);
        Linkify.addLinks(phone, Linkify.PHONE_NUMBERS);
       Linkify.addLinks(address, Linkify.MAP_ADDRESSES);
        Linkify.addLinks(email, Linkify.EMAIL_ADDRESSES);


        imageButton.setOnClickListener(new View.OnClickListener() {
            // When the button is pressed/clicked, it will run the code below
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick ImageButton: ");
                if( official.getPhoto()!=null ) {
                    if ((!official.getPhoto().equals(""))) {
                        Toast.makeText(OfficalActivity.this, "Opening Photo", Toast.LENGTH_SHORT).show();
                        // Intent is what you use to start another activity
                        Intent myIntent = new Intent(OfficalActivity.this, PhotoDetailActivity.class);
                        myIntent.putExtra("Picture", official.getPhoto());
                        myIntent.putExtra("Name", official.getName());
                        myIntent.putExtra("Office", official.getOffice());
                        myIntent.putExtra("Location", location);
                        myIntent.putExtra("Color",color);
                        startActivity(myIntent);
                    }
                }
            }
        });

    }


    public void openPicassoPhoto(String url) {
        final String photUrl = url;
        if (url != null) {
            Picasso picasso = new Picasso.Builder(this).listener(new Picasso.Listener() {
                @Override public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                    // Here we try https if the http image attempt failed
                    final String changedUrl = photUrl.replace("http:", "https:");
                    picasso.load(changedUrl)
                            .error(R.drawable.brokenimage)
                            .placeholder(R.drawable.placeholder)
                            .into(imageButton);
                }
            }).build();
            picasso.load(url)
                    .resize(180,180)
                    .centerInside()
                    .error(R.drawable.brokenimage)
                    .placeholder(R.drawable.placeholder)
                    .into(imageButton);
            Log.d(TAG, "openPicassoPhoto: "+url);

        } else {
            Picasso.with(this).load(url)
                    .error(R.drawable.brokenimage)
                    .placeholder(R.drawable.missingimage)
                    .into(imageButton);

        }
    }

    public void openPhotoActivity(View v) {
        //  intent.putExtra(Intent.EXTRA_TEXT, OfficalActivity.class.getSimpleName());
        if (official.getPhoto() != null) {
            Intent intent = new Intent(OfficalActivity.this, PhotoDetailActivity.class);
            intent.putExtra("Picture", official.getPhoto());
            intent.putExtra("Name", official.getName());
            intent.putExtra("Office", official.getOffice());
            intent.putExtra("Location", location);
            startActivity(intent);

        }
    }

    public void facebookClicked(String fbId) {
        String FACEBOOK_URL = "https://www.facebook.com/" + fbId;
        String urlToUse;
        PackageManager packageManager = getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                urlToUse = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else {
                //older versions of fb app
                urlToUse = "fb://page/" + fbId;
            }
        } catch (PackageManager.NameNotFoundException e) {
            urlToUse = FACEBOOK_URL;
            //normal web url
        }

        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
        facebookIntent.setData(Uri.parse(urlToUse));
        startActivity(facebookIntent);
    }

    public void twitterClicked(String handle) {
        Intent intent = null;
        try {
            // get the Twitter app if possible
            getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + handle));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            // no Twitter app, revert to browser
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + handle));
        }
        startActivity(intent);
    }

    public void googlePlusClicked(String gPLus) {
        Intent intent = null;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setClassName("com.google.android.apps.plus", "com.google.android.apps.plus.phone.UrlGatewayActivity");
            intent.putExtra("customAppUri", gPLus);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/" + gPLus)));
        }
    }

    public void youTubeClicked(String youId) {
        Intent intent = null;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.google.android.youtube");
            intent.setData(Uri.parse("https://www.youtube.com/" + youId));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/" + youId)));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                Intent intent = NavUtils.getParentActivityIntent(this);
                intent.putExtra("Loc",location);
                Toast.makeText(this, "Redirecting to Home.", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}