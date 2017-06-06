package com.example.sharel.knowyourgovernment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;



public class RecyclerViewHolder extends RecyclerView.ViewHolder{
    TextView official;
    TextView officialName;

    public RecyclerViewHolder(View view) {
        super(view);
        officialName = (TextView)view.findViewById(R.id.officalname);
        official = (TextView)view.findViewById(R.id.offical);
    }
}
