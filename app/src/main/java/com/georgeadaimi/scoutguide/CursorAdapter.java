package com.georgeadaimi.scoutguide;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class CursorAdapter extends ArrayAdapter<Emplacement>{
	/**
	 * Adapter context
	 */
	Context mContext;

	/**
	 * Adapter View layout
	 */
	int mLayoutResourceId;
	
	private  List<View> views=new ArrayList<View>();
	
	  public CursorAdapter(Context context, int resource) {
		super(context, resource,R.id.textView3);
		// TODO Auto-generated constructor stub
		mContext = context;
		mLayoutResourceId = resource;
	}

	@Override 
	  public View getView(int position, View convertView, ViewGroup parent) {  
//		if(views.size()!=0 && views.get(position) != null)
//		 {
//			 return views.get(position);
//		 }
		   //get reference to the row
		   View view = super.getView(position, convertView, parent); 
		final Emplacement currentItem = getItem(position);
		
		if (view == null) {
			LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
			view = inflater.inflate(mLayoutResourceId, parent, false);
		}
		views.add(position, view);
	   //check for odd or even to set alternate colors to the row background
	   view.setTag(currentItem);
	   if(position % 2 == 0){  
	    view.setBackgroundColor(Color.rgb(238, 233, 233));
	   }
	   else {
	    view.setBackgroundColor(Color.rgb(255, 255, 255));
	   }
	   
	   final TextView campsiteName= (TextView) view.findViewById(R.id.textView3);
	   final TextView campsiteCity= (TextView) view.findViewById(R.id.city);
	   final TextView campsiteType= (TextView) view.findViewById(R.id.textView2);
	   final TextView campsiteCapacity= (TextView) view.findViewById(R.id.capacity);
	   campsiteName.setText(currentItem.getName());
	   campsiteCity.setText(currentItem.getCity());
	   campsiteType.setText(currentItem.getType());
	   campsiteCapacity.setText(currentItem.getCapacity());
	   return view;  
	  }
	 }  
