package com.example.sharel.networkgateway;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Sharel on 4/21/2017.
 */

public class NewsFragment extends Fragment {

    private static final String TAG = "NewsFragment";

    public static final String ARTICLE = "ARTICLE";
    public static final String POSITION = "POSITION";
    public static final String TOTAL = "TOTAL";


    TextView textView, textView1, textView2,textView3, textView4;
    ImageView imageView;
   static MainActivity ma;

    public static final NewsFragment newInstance(MainActivity m,Articles a, int pos, int size)
    {
        ma = m;
        NewsFragment f = new NewsFragment();
        Bundle bdl = new Bundle(1);
        bdl.putSerializable(ARTICLE, a);
        bdl.putInt(POSITION, pos);
        bdl.putInt(TOTAL, size);

        f.setArguments(bdl);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "NewsFragment: onCreateView: ");

        final Articles articles = (Articles) getArguments().getSerializable("ARTICLE");
        int position  = getArguments().getInt("POSITION");
        int length  = getArguments().getInt("TOTAL");


        View v = inflater.inflate(R.layout.fragment_news, container, false);
        textView=(TextView) v.findViewById(R.id.source);
        textView1=(TextView) v.findViewById(R.id.description);
        imageView = (ImageView)v.findViewById( R.id.imageNews);
        textView2=(TextView) v.findViewById(R.id.authorName);
        textView3=(TextView) v.findViewById(R.id.publishDate);
        textView4=(TextView) v.findViewById(R.id.position);

        textView1.setMovementMethod(new ScrollingMovementMethod());

        ma.openPicassoPhoto(articles.getUrlToImage(),imageView);
        textView.setText(articles.getTitle());
        textView1.setText(articles.getDesc());


        if(articles.getAuthor().equals("null") || articles.getAuthor().isEmpty())
            textView2.setText("");

        else
        textView2.setText(articles.getAuthor());

        if(articles.getPublishDate().equals("null") || articles.getPublishDate().isEmpty())
            textView3.setText("");

        else {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'",Locale.ENGLISH);
            SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.ENGLISH);
            try{
                Date d = sdf.parse(articles.getPublishDate());
                String formattedTime = output.format(d);
                textView3.setText(formattedTime);
            }
            catch (ParseException e){
                e.printStackTrace();
            }

        }

        position=position+1;
        String pos = position +" of "+ length;
        textView4.setText(pos);


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: textView:"+textView.getText().toString() + articles.getUrl());
               clickNews(v, articles.getUrl());

            }
        });

        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: textView1:"+textView.getText().toString() + articles.getUrl());
                clickNews(v, articles.getUrl());

            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: imageView:"+textView.getText().toString() + articles.getUrl());
                clickNews(v, articles.getUrl());

            }
        });

        return v;
    }
public void clickNews(View v, String url){
    Intent i = new Intent(Intent.ACTION_VIEW);

    i.setData(Uri.parse(url));
    startActivity(i);
}

}
