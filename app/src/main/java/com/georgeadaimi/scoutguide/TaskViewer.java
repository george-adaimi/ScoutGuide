package com.georgeadaimi.scoutguide;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class TaskViewer extends Fragment {
	private static TaskViewerAdapter mAdapter;
    public static DataBaseHelper myDbHelper;
    EditText taskText;
    Button add;
    static String strtext;
    
	public TaskViewer() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
		strtext = getArguments().getString(HomeFragment.EXTRA_MESSAGE);
        final View view = inflater.inflate(R.layout.taskviewer_list, container,
            false);
        //getActivity().getActionBar().setTitle(strtext);
        
        	
    			
    			myDbHelper = new DataBaseHelper(getActivity().getApplicationContext());
    	        try {
    	       	 
    	        	myDbHelper.createDataBase();
    	 
    	 	} catch (IOException ioe) {
    	 
    	 		throw new Error("Unable to create database");
    	 
    	 	}
    	 
    	 	try {
    	 		myDbHelper.openDataBase();
    	 
    	 	}catch(SQLException sqle){
    	 
    	 		throw sqle;
    	 
    	 	}
    	 	
    	 	refreshTaskTitles( view);
    	 	taskText = (EditText) view.findViewById(R.id.editText1);
    	 	add=(Button) view.findViewById(R.id.button1);
    	 	add.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					addTaskNow(view);
				}
			});
    	 	return view;
    }
	
	public void addTaskNow(View v) {
        String s = taskText.getText().toString();
        if (s.equalsIgnoreCase("")) {
            Toast.makeText(getActivity(), "Enter the task description first!!",
                    Toast.LENGTH_LONG).show();
        } else {
            Task task = new Task(s, 0);
            myDbHelper.addTask(strtext, task);
            Log.d("tasker", "data added");
            taskText.setText("");
            mAdapter.add(task);
            mAdapter.notifyDataSetChanged();
        }
    }
	
	public void refreshTaskTitles(View view)
    {
    	final List<Task> list = myDbHelper.getAllTasks(strtext);
		// Create an adapter to bind the items with the view
		mAdapter = new TaskViewerAdapter(getActivity(), R.layout.taskitem_chechkbox,list);
		
		ListView listViewToDo = (ListView) view.findViewById(R.id.listView1);
		listViewToDo.setAdapter(mAdapter);
		listViewToDo.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long arg3) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(getActivity())
			    .setTitle("Delete entry")
			    .setMessage("Are you sure you want to delete this entry?")
			    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int which) { 
			            // continue with delete
			        	if(TaskViewer.myDbHelper.deleteTask(TaskViewer.strtext, list.get(position)))
						{
							mAdapter.taskList.remove(position);
							mAdapter.notifyDataSetChanged();
						}
						else
						{
							Toast.makeText(getActivity(), "Error Deleting", Toast.LENGTH_SHORT).show();
						}
			        }
			     })
			    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int which) { 
			            // do nothing
			        	dialog.cancel();
			        }
			     })
			    .setIcon(android.R.drawable.ic_dialog_alert)
			     .show();
				return false;
			}
		});
    }
}
