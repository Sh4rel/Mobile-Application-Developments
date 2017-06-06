package com.example.sharel.stockwatch;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Sharel on 2/25/2017.
 */

public class RecyclerViewHolder extends RecyclerView.ViewHolder{
    TextView companyName;
    TextView stockPrice;
    TextView stockPercent;
    TextView priceChange;
    TextView stockSymbol;

    public RecyclerViewHolder(View view) {
        super(view);
        stockSymbol = (TextView)view.findViewById(R.id.stockSymbol);
        companyName = (TextView) view.findViewById(R.id.compName);
        stockPrice = (TextView) view.findViewById(R.id.sValue);
        priceChange = (TextView) view.findViewById(R.id.priceChange);
        stockPercent= (TextView)view.findViewById(R.id.sPercent);
    }
}
