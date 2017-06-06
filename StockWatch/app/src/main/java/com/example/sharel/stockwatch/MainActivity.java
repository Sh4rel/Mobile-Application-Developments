package com.example.sharel.stockwatch;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,View.OnLongClickListener {

    private List<Stock> stocks = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerViewAdpater rAdapter;
    private SwipeRefreshLayout swiper;
    private static final String TAG = "MainActivity";
    private String market_url = "http://www.marketwatch.com/investing/stock/";
    DatabaseHandler databaseHandler;
    private boolean network;
    private boolean duplicate=false;
    private boolean d= false;

    ArrayList<String[]> stockList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        rAdapter = new RecyclerViewAdpater(stocks, this);

        recyclerView.setAdapter(rAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        databaseHandler = new DatabaseHandler(this);
        stockList = databaseHandler.loadStocks(); //returns stock name and symbol here in arraylist of string
        databaseHandler.dumpLog();

        if (networkCheck(this)) {
            String[] stocksDb;

            for (int i = 0; i < stockList.size(); i++) {
                stocksDb = stockList.get(i);
                Log.d(TAG, "onCreate: " + stocksDb[0] + stocksDb[1]);
                //stocks.add(new Stock(stocksDb[0],stocksDb[1], 123.4, 34.4, 45.5));
                new StockDataDownloader(this).execute(stocksDb[0]);

            }

        }

        //refresh data on swipe
        swiper = (SwipeRefreshLayout) findViewById(R.id.swiper);
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (networkCheck(MainActivity.this)) {
                Log.d(TAG, "Swiper - onRefresh: ");
                Toast.makeText(MainActivity.this, "Refreshing the stocks", Toast.LENGTH_SHORT).show();
                if (networkCheck(MainActivity.this)) {
                    String[] stocksDb;

                    for (int i = 0; i < stockList.size(); i++) {
                        stocksDb = stockList.get(i);
                        new StockDataDownloader(MainActivity.this).execute(stocksDb[0]);

                    }
                }

                }                swiper.setRefreshing(false);
            }
        });
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
            Toast.makeText(context, "You are NOT Connected to the Internet!", Toast.LENGTH_LONG).show();
            return false;
        }
    }


    @Override
    public void onClick(View v) {
        int pos = recyclerView.getChildLayoutPosition(v);
        Stock s = stocks.get(pos);
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(market_url + s.getStockSymbol().trim()));
        startActivity(browserIntent);
    }

    @Override
    public boolean onLongClick(View v) {
        //Delete the stock from the database
        final int pos = recyclerView.getChildLayoutPosition(v);
        final Stock s = stocks.get(pos);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Delete Stock Symbol " + s.getStockSymbol());
        builder.setTitle("Delete Stock");
        builder.setIcon(R.drawable.ic_delete);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                stocks.remove(pos);
                 databaseHandler.deleteStock(s.getStockSymbol());
                rAdapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, s.getCompanyName() + " is deleted!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                dialog.dismiss();
            }
        });


        AlertDialog dialog = builder.create();
        dialog.show();
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.addmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addStock:
                maintainAddStock();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void maintainAddStock() {
        network = networkCheck(this);

        if (!network) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Stocks cannot be added without a network connection!");
            builder.setTitle("No Network Connection");
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final EditText et = new EditText(this);
            et.setInputType(InputType.TYPE_CLASS_TEXT);
            et.setGravity(Gravity.CENTER_HORIZONTAL);
             et.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
            builder.setView(et);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Toast.makeText(MainActivity.this, "Stock Symbol: " + et.getText(), Toast.LENGTH_SHORT).show();
                   // addStockSymbol(et.getText().toString());
                    //Invoke async name downloader
                    new StockNameDownloader(MainActivity.this).execute(et.getText().toString());
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                    dialog.dismiss();
                }
            });

            builder.setMessage("Please enter a Stock Symbol");
            builder.setTitle("Stock Selection");

            AlertDialog dialog = builder.create();
            dialog.show();
        }


    }

    //Dialog pop up
    //add if null return bad stock alert
    //check if duplicate
    //create async downloader
    //sort the stock
    //add to the database
    //Database

    public void addStockSymbol(ArrayList<Stock> stSymbol) {
        Log.d(TAG, "addStockSymbol: ");
       // String symbolVal, companyVal;
       final  ArrayList<String[]> dups = databaseHandler.loadStocks();
        Stock val;
        ArrayList<String[]> list =new ArrayList<>();
        final String[] stockVal=new String[2];
        int pos;
        if (stSymbol == null || stSymbol.isEmpty()) {
            badStock();
        }
         if(stSymbol!=null && !stSymbol.isEmpty() && stSymbol.size() ==1){
           Stock dupSym = stSymbol.get(0);


            for (int i=0;i<dups.size();i++ ){
                String[] dVal = dups.get(i);
                if(dupSym.getStockSymbol().equals(dVal[0])){
                    duplicate=true;
                    duplicateStock(dVal[0]);
                    break;
                }
            }

        }
         if (stSymbol!=null && !stSymbol.isEmpty() && stSymbol.size()==1 && !duplicate) {


                     for (int i =0; i<stSymbol.size();i++) {
                   val = stSymbol.get(i);
                stockVal[0] = val.getStockSymbol();
                stockVal[1] = val.getCompanyName();
                list.add(stockVal);

               databaseHandler.addStock(list);
                //for new stock added
             new StockDataDownloader(MainActivity.this).execute(stockVal[0]);
            }


        }
        else if( stSymbol!=null && stSymbol.size() >1){
             final ArrayList<String> arrayList = new ArrayList<>();
            for (int i = 0; i < stSymbol.size(); i++) {
                Stock st = stSymbol.get(i);
                arrayList.add( st.getStockSymbol()+" - "+ st.getCompanyName());
            }

           final  CharSequence[] sArray = arrayList.toArray(new CharSequence[arrayList.size()]);
           final  ArrayList<String[]> list2 =new ArrayList<>();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Choose the Company");

            builder.setItems(sArray, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    String pos = sArray[which].toString();
                     int hyphen = pos.indexOf("-");
                    stockVal[0] = pos.substring(0,hyphen-1);
                    stockVal[1]=pos.substring(hyphen+2);
                    list2.add(stockVal);

                    //duplicate
                    for (int i=0;i<dups.size();i++ ){
                        String[] dVal = dups.get(i);
                        if(stockVal[0].equals(dVal[0])){
                            d=true;
                            duplicateStock(dVal[0]);
                            break;
                        }
                    }

                   if(!d) {
                       databaseHandler.addStock(list2);
                       new StockDataDownloader(MainActivity.this).execute(stockVal[0]);
                   }
                }
            });

            builder.setNegativeButton("Nevermind", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

        }
    }

    public void updateStock(ArrayList<Stock> stock){
        ArrayList<String[]> dbStock = databaseHandler.loadStocks();
        String[] name;
        String cn="";
        ArrayList<String> sArr=new ArrayList<>();
        for(int i =0;i <stocks.size();i++){
            Stock s1=stocks.get(i);
            sArr.add(s1.getCompanyName());

        }

          for (int i = 0; i < stock.size(); i++) {
                Stock s = stock.get(i);
                String sym = s.getStockSymbol();
                for (int j = 0; j < dbStock.size(); j++) {
                    name = dbStock.get(j);
                    if (sym.contains(name[0])) {
                        cn = name[1];
                        break;
                    }
                }
                double p = s.getPrice();
                double r = s.getChangePercentage();
                double c = s.getPriceChange();
                boolean f = s.isChangeValue();
              if(sArr.contains(cn)){
        //      stocks.remove(i);
        //  stocks.add(new Stock(sym, cn, p, c, r, f));

}
              else{
                  stocks.add(new Stock(sym, cn, p, c, r, f));

              }


          }
        Collections.sort(stocks);
        rAdapter.notifyDataSetChanged();
    }

    private void badStock(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("No match found for this stock symbol!");
        builder.setTitle("No match");
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void duplicateStock(String sym){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Stock "+sym+" already exists in the list!");
        builder.setTitle("Duplicate Stock");
        builder.setIcon(R.drawable.ic_action_name);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}