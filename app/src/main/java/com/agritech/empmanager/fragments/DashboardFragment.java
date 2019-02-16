package com.agritech.empmanager.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.agritech.empmanager.R;
import com.agritech.empmanager.databinding.FragmentDashbordBinding;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

public class DashboardFragment extends Fragment {

    private OnDashboardFragmentInteractionListener mListener;

    FragmentDashbordBinding binding;

    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashbord, container, false);

        return binding.getRoot();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onDashboardFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDashboardFragmentInteractionListener) {
            mListener = (OnDashboardFragmentInteractionListener) context;
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


    public interface OnDashboardFragmentInteractionListener {
        // TODO: Update argument type and name
        void onDashboardFragmentInteraction(Uri uri);
    }
}
