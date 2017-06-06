package com.example.sharel.knowyourgovernment;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

/**
 * Created by Sharel on 4/3/2017.
 */

public class PhotoDetailActivity extends AppCompatActivity{
    TextView pOffice;
    TextView pName;
    TextView pLoc;
    ImageView photo;
    ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        if(getActionBar()!=null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

       pOffice= (TextView)findViewById(R.id.pOffice);
        pName = (TextView)findViewById(R.id.pName);
        pLoc = (TextView)findViewById(R.id.pLoc);
        photo = (ImageView) findViewById(R.id.Picture);
       constraintLayout =  (ConstraintLayout)findViewById(R.id.photoactivity);

        Intent intent = getIntent();
        pName.setText(intent.getStringExtra("Name"));
        pOffice.setText(intent.getStringExtra("Office"));
        pLoc.setText(intent.getStringExtra("Location"));



     String url =  intent.getStringExtra("Picture");
        String color = intent.getStringExtra("Color");

        switch (color){
            case "Red": constraintLayout.setBackgroundColor(Color.RED);
                break;
            case "Blue": constraintLayout.setBackgroundColor(Color.BLUE);
                     break;
            case "Black": constraintLayout.setBackgroundColor(Color.BLACK);
                break;
        }

        final String photUrl =url;

        if (url != null) {
            Picasso picasso = new Picasso.Builder(this).listener(new Picasso.Listener() {
                @Override public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                    // Here we try https if the http image attempt failed
                    final String changedUrl = photUrl.replace("http:", "https:");
                    picasso.load(changedUrl)
                            .error(R.drawable.brokenimage)
                            .placeholder(R.drawable.placeholder)
                            .into(photo);
                }
            }).build();
            picasso.load(url)
                    .resize(360,360)
                    .centerInside()
                    .error(R.drawable.brokenimage)
                    .placeholder(R.drawable.placeholder)
                    .into(photo);
        }
        else {
            Picasso.with(this).load(url)
                    .error(R.drawable.brokenimage)
                    .placeholder(R.drawable.missingimage)
                    .into(photo);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                Intent intent = NavUtils.getParentActivityIntent(this);
                intent.putExtra("Loc",pLoc.getText());
                Toast.makeText(this, "Redirecting to Home.", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
