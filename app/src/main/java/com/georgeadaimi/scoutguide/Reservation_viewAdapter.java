package com.georgeadaimi.scoutguide;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

/**
 * Created by georgeadaimi on 4/8/16.
 */
public class Reservation_viewAdapter extends RecyclerView.Adapter<Reservation_ViewHolder> {
    List<Reservation> list = Collections.emptyList();
    Context context;

    public Reservation_viewAdapter(List<Reservation> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public Reservation_ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.reservation_row_layout, parent, false);
        Reservation_ViewHolder holder = new Reservation_ViewHolder(v);
        return holder;

    }

    @Override
    public void onBindViewHolder(Reservation_ViewHolder holder, int position) {

        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
        holder.start.setText(list.get(position).startDate);
        holder.end.setText(list.get(position).endDate);

        //animate(holder);

    }

    @Override
    public int getItemCount() {
        //returns the number of elements the RecyclerView will display
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    // Insert a new item to the RecyclerView on a predefined position
    public void insert(int position, Reservation data) {
        list.add(position, data);
        notifyItemInserted(position);
    }

    // Remove a RecyclerView item containing a specified Data object
    public void remove(Reservation data) {
        int position = list.indexOf(data);
        list.remove(position);
        notifyItemRemoved(position);
    }

}
