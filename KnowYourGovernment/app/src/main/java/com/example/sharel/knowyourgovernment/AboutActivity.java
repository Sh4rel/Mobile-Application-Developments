package com.example.sharel.knowyourgovernment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Sharel on 2/14/2017.
 */

public class AboutActivity  extends AppCompatActivity{

     TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        textView= (TextView) findViewById(R.id.aboutText);

       Intent intent = getIntent();
        if (intent.hasExtra(Intent.EXTRA_TEXT)) {
            textView.setText("\tKnow Your Government"+"\n\n ©2017, Sharel Clavy Pereira"+"\n\n\t Version 1.0");
        }

        if(getActionBar()!=null){
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
               NavUtils.navigateUpFromSameTask(this);
               Toast.makeText(this, "Redirecting to Home.", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
