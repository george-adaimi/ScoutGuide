package com.georgeadaimi.scoutguide;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.net.MalformedURLException;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * } interface
 * to handle interaction events.
 * Use the {@link AddCampsiteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddCampsiteFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private MobileServiceClient mClient;
    private MobileServiceTable<Emplacement> mToDoTable;

    EditText campsiteName;
    EditText campsiteOwner;
    EditText campsiteCity;
    EditText campsitePhone;
    EditText campsiteCapacity;
    EditText campsiteDistrict;
    EditText campsiteRemarks;
    Spinner typeSpinner;
    Spinner districtSpinner;
    Spinner capacitySpinner;
    Button submit;
    public AddCampsiteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddCampsiteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddCampsiteFragment newInstance(String param1, String param2) {
        AddCampsiteFragment fragment = new AddCampsiteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_add_campsite, container, false);
        campsiteName = (EditText) rootView.findViewById(R.id.campsiteName);
        campsiteCity = (EditText) rootView.findViewById(R.id.campsiteCity);
        campsiteOwner = (EditText) rootView.findViewById(R.id.campsiteOwner);
        campsitePhone = (EditText) rootView.findViewById(R.id.owner_tel);
        campsiteCapacity = (EditText) rootView.findViewById(R.id.campsite_capacity);
        campsiteRemarks = (EditText) rootView.findViewById(R.id.campsiteRemarks);
        typeSpinner = (Spinner) rootView.findViewById(R.id.typeSpinner);
        districtSpinner = (Spinner) rootView.findViewById(R.id.districtSpinner);
        capacitySpinner = (Spinner) rootView.findViewById(R.id.capacitySpinner);
        submit = (Button) rootView.findViewById(R.id.submit);
        try {
            // Create the Mobile Service Client instance, using the provided
            // Mobile Service URL and key
            mClient = new MobileServiceClient(
                    "https://scoutguide.azurewebsites.net",
                    getActivity()
            );

            // Get the Mobile Service Table instance to use
            mToDoTable = mClient.getTable(Emplacement.class);

            // Create an adapter to bind the items with the view

        } catch (MalformedURLException e) {
            createAndShowDialog(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
        }
        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                addItem(null);

            }
        });
        return rootView;
    }
    // Insert the new item
    public void addItem(View view) {

        boolean inserted = true;
        // Create a new item
        final Emplacement item = new Emplacement();

        item.setName(campsiteName.getText().toString());
        item.setCity(campsiteCity.getText().toString());
        item.setType(typeSpinner.getSelectedItem().toString());
        item.setOwner(campsiteOwner.getText().toString());
        item.setDistrict(districtSpinner.getSelectedItem().toString());
        item.setPhone(campsitePhone.getText().toString());
        if(campsiteCapacity.getText().toString().isEmpty())
        {
            item.setCapacity("NA");
        }
        else{
            item.setCapacity(campsiteCapacity.getText().toString()+" "+capacitySpinner.getSelectedItem().toString());
        }
        item.setRemarks(campsiteRemarks.getText().toString());
        try{
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        mToDoTable.insert(item).get();

                    } catch (Exception exception) {
                        createAndShowDialog(exception, "Error");
                    }
                    return null;
                }
            }.execute();
        }catch(Exception exception)
        {
            inserted=false;
            Toast.makeText(getActivity().getApplicationContext(), "Error Adding Campsite", Toast.LENGTH_SHORT).show();
        }
        if(inserted)
        {
            Toast.makeText(getActivity().getApplicationContext(), "Successfully Added", Toast.LENGTH_SHORT).show();
            //onBackPressed();

        }
    }

    private void createAndShowDialog(String message, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity().getApplicationContext());

        builder.setMessage(message);
        builder.setTitle(title);
        builder.create().show();
    }
    private void createAndShowDialog(Exception exception, String title) {
        Throwable ex = exception;
        if(exception.getCause() != null){
            ex = exception.getCause();
        }
        createAndShowDialog(ex.getMessage(), title);
    }


}
