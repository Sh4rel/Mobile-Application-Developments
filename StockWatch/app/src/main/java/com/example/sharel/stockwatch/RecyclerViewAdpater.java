package com.example.sharel.stockwatch;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sharel on 2/25/2017.
 */

public class RecyclerViewAdpater extends RecyclerView.Adapter<RecyclerViewHolder>{

   private List<Stock> stocks=new ArrayList<>();
    private MainActivity mainActivity;
    private static final String TAG = "RecyclerViewAdpater";

    public RecyclerViewAdpater(List<Stock> stocks, MainActivity mainActivity) {
        this.stocks = stocks;
        this.mainActivity = mainActivity;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");
       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.stockwatch, parent, false);

        view.setOnClickListener(mainActivity);
        view.setOnLongClickListener(mainActivity);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: ");

        Stock stock = stocks.get(position);
        if (stock.isChangeValue()) {

            holder.companyName.setText(stock.getCompanyName());
            holder.companyName.setTextColor(Color.RED);
            holder.stockSymbol.setText(stock.getStockSymbol());
            holder.stockSymbol.setTextColor(Color.RED);


            String price = Double.toString(stock.getPrice());
            holder.stockPrice.setText(price);
            holder.stockPrice.setTextColor(Color.RED);

            String priceChange = Double.toString(stock.getPriceChange());
            String pc ="\u25BC" +priceChange;

            holder.priceChange.setText(pc);
            holder.priceChange.setTextColor(Color.RED);


            String percent = Double.toString(stock.getChangePercentage());
            String p = "(" + percent + "%)";
            holder.stockPercent.setText(p);
            holder.stockPercent.setTextColor(Color.RED);
        }
        else if(!stock.isChangeValue()){

            holder.companyName.setText(stock.getCompanyName());
            holder.companyName.setTextColor(Color.GREEN);

            holder.stockSymbol.setText(stock.getStockSymbol());
            holder.stockSymbol.setTextColor(Color.GREEN);

            String price = Double.toString(stock.getPrice());
            holder.stockPrice.setText(price);
            holder.stockPrice.setTextColor(Color.GREEN);


            String priceChange = Double.toString(stock.getPriceChange());
           String  pc1 ="\u25B2" +priceChange;
            holder.priceChange.setText(pc1);
            holder.priceChange.setTextColor(Color.GREEN);



            String percent = Double.toString(stock.getChangePercentage());
            String p = "(" + percent + "%)";
            holder.stockPercent.setText(p);
            holder.stockPercent.setTextColor(Color.GREEN);


        }


    }

    @Override
    public int getItemCount() {
        return stocks.size();
    }
}
