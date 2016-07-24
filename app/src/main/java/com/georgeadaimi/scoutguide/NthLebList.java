package com.georgeadaimi.scoutguide;

import java.net.MalformedURLException;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.http.NextServiceFilterCallback;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilter;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterRequest;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import android.app.AlertDialog;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class NthLebList extends Fragment {
	private MobileServiceClient mClient;
	private MobileServiceTable<Emplacement> mToDoTable;
	private ProgressBar mProgressBar;
	private CursorAdapter mAdapter;
	AsyncTask<Void, Void, Void> task;
	public NthLebList(){
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.listview, container,
            false);
        //getActivity().getActionBar().setTitle("North Lebanon");
        
        mProgressBar = (ProgressBar) view.findViewById(R.id.loadingProgressBar);

		// Initialize the progress bar
		mProgressBar.setVisibility(ProgressBar.GONE);
        
        try {
        	// Create the Mobile Service Client instance, using the provided
        	// Mobile Service URL and key
			mClient = new MobileServiceClient(
					"https://scoutguide.azurewebsites.net",
					getActivity()
			).withFilter(new ProgressFilter());

        	    // Get the Mobile Service Table instance to use
        	    mToDoTable = mClient.getTable(Emplacement.class);
        	    
        	 // Create an adapter to bind the items with the view
    			mAdapter = new CursorAdapter(getActivity(), R.layout.listview_item);
    			
    			ListView listViewToDo = (ListView) view.findViewById(R.id.listView1);
    			listViewToDo.setAdapter(mAdapter);
			TextView delete = (TextView) view.findViewById(R.id.deleteText);
			delete.setVisibility(View.INVISIBLE);
    			// Load the items from the Mobile Service
    			refreshItemsFromTable();
        	} catch (MalformedURLException e) {
        	    createAndShowDialog(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
        	}
        return view;
    }
	/**
	 * Creates a dialog and shows it
	 * 
	 * @param exception
	 *            The exception to show in the dialog
	 * @param title
	 *            The dialog title
	 */
	private void createAndShowDialog(Exception exception, String title) {
		Throwable ex = exception;
		if(exception.getCause() != null){
			ex = exception.getCause();
		}
		createAndShowDialog(ex.getMessage(), title);
	}
	
	/**
	 * Refresh the list with the items in the Mobile Service Table
	 */
	private void refreshItemsFromTable() {

		// Get the items that weren't marked as completed and add them in the
		// adapter
	       task= new AsyncTask<Void, Void, Void>() {

	            @Override
	            protected Void doInBackground(Void... params) {
	                try {
	                    final MobileServiceList<Emplacement> result = mToDoTable.where().field("district").eq("North Lebanon").execute().get();
	                    getActivity().runOnUiThread(new Runnable() {

	                        @Override
	                        public void run() {
	                            mAdapter.clear();

	                            for (Emplacement item : result) {
	                                mAdapter.add(item);
	                            }
	                        }
	                    });
	                } catch (Exception exception) {
	                    createAndShowDialog(exception, "Error");
	                }
	                return null;
	            }
	        }.execute();
	}
	/**
	 * Creates a dialog and shows it
	 * 
	 * @param message
	 *            The dialog message
	 * @param title
	 *            The dialog title
	 */
	private void createAndShowDialog(String message, String title) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		builder.setMessage(message);
		builder.setTitle(title);
		builder.create().show();
	}
	
	private class ProgressFilter implements ServiceFilter {

        @Override
        public ListenableFuture<ServiceFilterResponse> handleRequest(
                ServiceFilterRequest request, NextServiceFilterCallback next) {

        	getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (mProgressBar != null) mProgressBar.setVisibility(ProgressBar.VISIBLE);
                }
            });

            SettableFuture<ServiceFilterResponse> result = SettableFuture.create();
            try {
                ServiceFilterResponse response = next.onNext(request).get();
                result.set(response);
            } catch (Exception exc) {
                result.setException(exc);
            }

          dismissProgressBar();
          return result;
        }

      private void dismissProgressBar() {
          getActivity().runOnUiThread(new Runnable() {

              @Override
              public void run() {
                  if (mProgressBar != null) mProgressBar.setVisibility(ProgressBar.GONE);
              }
          });
        }
    }
	@Override
	public void onStop() {
		super.onStop();

		//check the state of the task
		if(task != null && task.getStatus() == AsyncTask.Status.RUNNING)
			task.cancel(true);
	}
}

