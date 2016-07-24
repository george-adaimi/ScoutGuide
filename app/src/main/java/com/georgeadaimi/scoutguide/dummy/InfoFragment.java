package com.georgeadaimi.scoutguide.dummy;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.georgeadaimi.scoutguide.CampSiteActivity;
import com.georgeadaimi.scoutguide.R;
import com.georgeadaimi.scoutguide.Reservation;
import com.georgeadaimi.scoutguide.Reservation_viewAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InfoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button reserve;

    Reservation_viewAdapter adapter;
    private OnFragmentInteractionListener mListener;

    public InfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InfoFragment newInstance(String param1, String param2) {
        InfoFragment fragment = new InfoFragment();
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
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_info, container, false);
        reserve = (Button)view.findViewById(R.id.reserveButton);
        reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showReserveDialog();
            }
        });
        List<Reservation> data = new ArrayList<>();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.reservation_recycler);
        adapter = new Reservation_viewAdapter(data, getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        String owner = "Owner: "+CampSiteActivity.myEmplacement.getOwner();
        String telephone = "Phone: "+CampSiteActivity.myEmplacement.getPhone();
        String capacity = "Capacity: "+CampSiteActivity.myEmplacement.getCapacity();
        String type = "Type: "+CampSiteActivity.myEmplacement.getType();
        String city = "City: "+CampSiteActivity.myEmplacement.getCity();
        String district = "District: "+CampSiteActivity.myEmplacement.getDistrict();

        TextView ownerInfo= (TextView) view.findViewById(R.id.ownerInfo);
        TextView phoneInfo= (TextView) view.findViewById(R.id.telephoneInfo);
        TextView capacityInfo= (TextView) view.findViewById(R.id.capacityInfo);
        TextView typeInfo= (TextView) view.findViewById(R.id.typeInfo);
        TextView cityInfo= (TextView) view.findViewById(R.id.cityInfo);
        TextView districtInfo= (TextView) view.findViewById(R.id.districtInfo);

        ownerInfo.setText(owner);
        phoneInfo.setText(telephone);
        capacityInfo.setText(capacity);
        typeInfo.setText(type);
        cityInfo.setText(city);
        districtInfo.setText(district);
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

    void showReserveDialog()
    {
        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.timedate_picker, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final DatePicker startDate = (DatePicker) promptsView.findViewById(R.id.EnddatePicker);
        final DatePicker endDate = (DatePicker) promptsView.findViewById(R.id.StartdatePicker);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                // edit text
                                String start = startDate.getDayOfMonth()+"/"+startDate.getMonth()+"/"+startDate.getYear();
                                String end = endDate.getDayOfMonth()+"/"+endDate.getMonth()+"/"+endDate.getYear();
                                Reservation newReserve = new Reservation(start,end);
                                adapter.insert(adapter.getItemCount(),newReserve);
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

}
