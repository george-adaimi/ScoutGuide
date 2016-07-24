package com.georgeadaimi.scoutguide;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
//import org.apache.http.*;
//import org.apache.http.client.*;
//import org.apache.http.client.methods.*;
//import org.apache.http.impl.client.*;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.BlobContainerPermissions;
import com.microsoft.azure.storage.blob.BlobContainerPublicAccessType;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.azure.storage.blob.ListBlobItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PhotosFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PhotosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhotosFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private GridView mGridView;
    private ProgressBar mProgressBar;
    private GridViewAdapter mGridAdapter;
    private ArrayList<GridItem> mGridData;
    private String FEED_URL = "http://javatechig.com/?json=get_recent_posts&count=45";

    public PhotosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PhotosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PhotosFragment newInstance(String param1, String param2) {
        PhotosFragment fragment = new PhotosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blank, container,
                false);
        mGridView = (GridView) view.findViewById(R.id.gridView);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        //Initialize with empty data
        mGridData = new ArrayList<>();
        mGridAdapter = new GridViewAdapter(getActivity(), R.layout.grid_item_layout, mGridData);
        mGridView.setAdapter(mGridAdapter);

        //Start download
        new BlobGettingStartedTask().execute(FEED_URL);
        mProgressBar.setVisibility(View.VISIBLE);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    //Downloading data asynchronously
    public class BlobGettingStartedTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            try {
                // Setup the cloud storage account.
                CloudStorageAccount account = CloudStorageAccount
                        .parse(CampSiteActivity.storageConnectionString);

                // Create a blob service client
                CloudBlobClient blobClient = account.createCloudBlobClient();

                // Get a reference to a container
                // The container name must be lower case
                // Append a random UUID to the end of the container name so that
                // this sample can be run more than once in quick succession.
                CloudBlobContainer container = blobClient.getContainerReference("blobbasicscontainer"
                        + UUID.randomUUID().toString().replace("-", ""));

                // Create the container if it does not exist
                container.createIfNotExists();

                // Make the container public
                // Create a permissions object
                BlobContainerPermissions containerPermissions = new BlobContainerPermissions();

                // Include public access in the permissions object
                containerPermissions
                        .setPublicAccess(BlobContainerPublicAccessType.CONTAINER);

                // Set the permissions on the container
                container.uploadPermissions(containerPermissions);

                // Upload 3 blobs
                // Get a reference to a blob in the container
                CloudBlockBlob blob1 = container
                        .getBlockBlobReference("blobbasicsblob1");

                // Upload text to the blob
                blob1.uploadText("Hello, World1");

                // Get a reference to a blob in the container
                CloudBlockBlob blob2 = container
                        .getBlockBlobReference("blobbasicsblob2");

                // Upload text to the blob
                blob2.uploadText("Hello, World2");

                // Get a reference to a blob in the container
                CloudBlockBlob blob3 = container
                        .getBlockBlobReference("blobbasicsblob3");

                // Upload text to the blob
                blob3.uploadText("Hello, World3");

                // Download the blob
                // For each item in the container
                for (ListBlobItem blobItem : container.listBlobs()) {
                    // If the item is a blob, not a virtual directory
                    if (blobItem instanceof CloudBlockBlob) {
                        // Download the text
                        CloudBlockBlob retrievedBlob = (CloudBlockBlob) blobItem;
                    }
                }

                // List the blobs in a container, loop over them and
                // output the URI of each of them
                for (ListBlobItem blobItem : container.listBlobs()) {
                }

                // Delete the blobs
                blob1.deleteIfExists();
                blob2.deleteIfExists();
                blob3.deleteIfExists();

                // Delete the container
                container.deleteIfExists();
            } catch (Throwable t) {
            }
            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            // Download complete. Let us update UI
            if (result == 1) {
                mGridAdapter.setGridData(mGridData);
            } else {
                Toast.makeText(getActivity(), "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
            mProgressBar.setVisibility(View.GONE);
        }
    }

    String streamToString(InputStream stream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
        String line;
        String result = "";
        while ((line = bufferedReader.readLine()) != null) {
            result += line;
        }

        // Close stream
        if (null != stream) {
            stream.close();
        }
        return result;
    }

    /**
     * Parsing the feed results and get the list
     * @param result
     */
    private void parseResult(String result) {
        try {
            JSONObject response = new JSONObject(result);
            JSONArray posts = response.optJSONArray("posts");
            GridItem item;
            for (int i = 0; i < posts.length(); i++) {
                JSONObject post = posts.optJSONObject(i);
                String title = post.optString("title");
                item = new GridItem();
                item.setTitle(title);
                JSONArray attachments = post.getJSONArray("attachments");
                if (null != attachments && attachments.length() > 0) {
                    JSONObject attachment = attachments.getJSONObject(0);
                    if (attachment != null)
                        item.setImage(attachment.getString("url"));
                }
                mGridData.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    // Create a File object for storing the photo
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return image;
    }
}
