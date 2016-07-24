package com.georgeadaimi.scoutguide;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.database.SQLException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * A placeholder fragment containing a simple view.
 */
public class HomeFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
	public static final String EXTRA_MESSAGE= "com.example.scoutguide.HomeFragment.title";
    private static final String ARG_SECTION_NUMBER = "section_number";
    private ProgressBar mProgressBar;
	static DataBaseHelper myDbHelper;
	private static TaskManagerAdapter mAdapter;
	static private boolean longClick=false;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static HomeFragment newInstance(int sectionNumber) {
    	HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.listview, container, false);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.loadingProgressBar);
        //getActivity().getActionBar().setTitle("TODO List");
		// Initialize the progress bar
		mProgressBar.setVisibility(ProgressBar.GONE);
		// Create an adapter to bind the items with the view
		mAdapter = new TaskManagerAdapter(getActivity(), R.layout.home_fragment);
		
		ListView listViewToDo = (ListView) rootView.findViewById(R.id.listView1);
		listViewToDo.setAdapter(mAdapter);
		
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
 	refreshTaskTitles();
 	listViewToDo.setOnItemClickListener(new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			if(!longClick){
				Fragment fragment =null;
		      	 FragmentManager fragmentManager = getActivity().getFragmentManager();
		         FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		         fragment = new TaskViewer();
		         if(fragment !=null)
		      	   {
		        	 Bundle args = new Bundle();
		        	 args.putString(EXTRA_MESSAGE, parent.getItemAtPosition(position).toString());
		        	 fragment.setArguments(args);
		      		 fragmentTransaction.replace(R.id.container, fragment);
		             fragmentTransaction.addToBackStack(null);
		             
		             fragmentTransaction.commit();
					   MainActivity.fab.setVisibility(View.INVISIBLE);
					   MainActivity.fab.setClickable(false);
		      	   }
				//Toast.makeText(getActivity(), parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
				
			}
		}
	});
 	
 	listViewToDo.setOnItemLongClickListener(new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
			// TODO Auto-generated method stub
			longClick=true;
			new AlertDialog.Builder(getActivity())
		    .setTitle("Delete entry")
		    .setMessage("Are you sure you want to delete this entry?")
		    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		             //continue with delete
		        	if(myDbHelper.deleteTable(parent.getItemAtPosition(position).toString()))
					{
						refreshTaskTitles();
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
			longClick=false;
			return true;
		}
	});
        return rootView;
    }

    public static void refreshTaskTitles()
    {
    	List<String> list = new ArrayList<String>();
    	list = myDbHelper.getTaskTitles();
    	
    	mAdapter.clear();

        for (String item : list) {
        	if(!item.equals("android_metadata") && !item.equals("sqlite_sequence"))
            mAdapter.add(item);
        }
    }
}

