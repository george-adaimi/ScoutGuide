package com.georgeadaimi.scoutguide;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by georgeadaimi on 4/8/16.
 */
public class Reservation_ViewHolder extends RecyclerView.ViewHolder {
    CardView cv;
    TextView start;
    TextView end;

    Reservation_ViewHolder(View itemView) {
        super(itemView);
        cv = (CardView) itemView.findViewById(R.id.cardView);
        start = (TextView) itemView.findViewById(R.id.start);
        end = (TextView) itemView.findViewById(R.id.end);
    }
}