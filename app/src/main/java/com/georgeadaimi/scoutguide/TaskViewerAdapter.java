package com.georgeadaimi.scoutguide;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class TaskViewerAdapter extends ArrayAdapter<Task>{
	/**
	 * Adapter context
	 */
	Context mContext;

	/**
	 * Adapter View layout
	 */
	int mLayoutResourceId;
	List<Task> taskList=new ArrayList<Task>();
	
	public TaskViewerAdapter(Context context, int resource,
			List<Task> objects) {
		super(context, resource,R.id.nothing,objects);
		// TODO Auto-generated constructor stub
		mContext = context;
		mLayoutResourceId = resource;
		this.taskList=objects;
	}

		@Override 
		  public View getView(final int position, View convertView, ViewGroup parent) {  
			super.getView(position, convertView, parent); 
			CheckBox chk = null;
			if (convertView == null) {
				LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
				convertView = inflater.inflate(mLayoutResourceId, parent, false);
				chk = (CheckBox) convertView.findViewById(R.id.checkBox1);
                convertView.setTag(chk);
                chk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;
                        Task changeTask = (Task) cb.getTag();
                        changeTask.setStatus(cb.isChecked() == true ? 1 : 0);
                        TaskViewer.myDbHelper.updateTask(TaskViewer.strtext,changeTask);
//                        Toast.makeText(
//                                mContext,
//                                "Clicked on Checkbox: " + cb.getText() + " is "
//                                        + cb.isChecked(), Toast.LENGTH_SHORT)
//                                .show();
                    }
                });
               
            } else {
                chk = (CheckBox) convertView.getTag();
            }
			   if(position % 2 == 0){  
			    convertView.setBackgroundColor(Color.rgb(238, 233, 233));
			   }
			   else {
			    convertView.setBackgroundColor(Color.rgb(255, 255, 255));
			   }
			   
			   Task current=taskList.get(position);
			   chk.setText(current.getTaskName());
			   chk.setChecked(current.getStatus()==1?true:false);
			   chk.setTag(current);
	            Log.d("listener", String.valueOf(current.getId()));
			   return convertView;
		}

}
