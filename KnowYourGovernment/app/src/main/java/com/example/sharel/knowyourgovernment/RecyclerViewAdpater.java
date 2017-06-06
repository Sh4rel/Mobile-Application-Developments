package com.example.sharel.knowyourgovernment;

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


    private MainActivity mainActivity;
    private static final String TAG = "RecyclerViewAdpater";
    private List<Official> officials = new ArrayList<>();

    public RecyclerViewAdpater( List<Official> official, MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        this.officials=official;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");
       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.activity, parent, false);

        view.setOnClickListener(mainActivity);
        view.setOnLongClickListener(mainActivity);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: ");
        Official official = officials.get(position);
      holder.official.setText(official.getOffice());
        holder.officialName.setText(official.getName());
    }

    @Override
    public int getItemCount() {
        return officials.size();
    }
}