package com.georgeadaimi.scoutguide;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TaskManagerAdapter extends ArrayAdapter<String>{
	/**
	 * Adapter context
	 */
	Context mContext;

	/**
	 * Adapter View layout
	 */
	int mLayoutResourceId;
	
	public TaskManagerAdapter(Context context, int resource) {
		super(context, resource,R.id.section_label);
		// TODO Auto-generated constructor stub
		mContext = context;
		mLayoutResourceId = resource;
	}

		@Override 
		  public View getView(int position, View convertView, ViewGroup parent) {  
			View view = super.getView(position, convertView, parent); 
			
			if (view == null) {
				LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
				view = inflater.inflate(mLayoutResourceId, parent, false);
			}
			String currentItem = getItem(position);
			
			view.setTag(currentItem);
			   if(position % 2 == 0){  
			    view.setBackgroundColor(Color.rgb(238, 233, 233));
			   }
			   else {
			    view.setBackgroundColor(Color.rgb(255, 255, 255));
			   }
			   
			   final TextView title= (TextView) view.findViewById(R.id.section_label);
			   title.setText(currentItem);
			title.setTextSize(25);
			   return view;
		}

}
