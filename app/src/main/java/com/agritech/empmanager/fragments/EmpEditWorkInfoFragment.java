package com.agritech.empmanager.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.agritech.empmanager.R;
import com.agritech.empmanager.databinding.FragmentEmpEditWorkInfoBinding;
import com.google.android.material.snackbar.Snackbar;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;


public class EmpEditWorkInfoFragment extends Fragment {

    private OnEmpEditWorkInfoFragmentInteractionListener mListener;


    FragmentEmpEditWorkInfoBinding binding;


    public EmpEditWorkInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_emp_edit_work_info, container, false);


        binding.acbSave.setOnClickListener(v -> {

            //General information

            String firstName = binding.etFirstName.getText().toString();
            String lastName = binding.etLastName.getText().toString();

            String esiNumber = binding.etESINumber.getText().toString();

            String pfNumber = binding.etPFNumber.getText().toString();


            if (firstName.isEmpty()) {
                Snackbar.make(binding.getRoot(), "First Name is empty", Snackbar.LENGTH_LONG).show();
                return;
            }

            if (lastName.isEmpty()) {
                Snackbar.make(binding.getRoot(), "Last Name is empty", Snackbar.LENGTH_LONG).show();
                return;
            }


            if (esiNumber.isEmpty()) {
                Snackbar.make(binding.getRoot(), "ESI Number is empty", Snackbar.LENGTH_LONG).show();
                return;
            }

            if (pfNumber.isEmpty()) {
                Snackbar.make(binding.getRoot(), "PF Number is empty", Snackbar.LENGTH_LONG).show();
                return;
            }


            if (mListener != null) {
                mListener.onEmpEditWorkInfoFragmentInteraction(firstName, lastName, esiNumber, pfNumber,v);
            }


        });


        return binding.getRoot();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnEmpEditWorkInfoFragmentInteractionListener) {
            mListener = (OnEmpEditWorkInfoFragmentInteractionListener) context;
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

    public interface OnEmpEditWorkInfoFragmentInteractionListener {
        void onEmpEditWorkInfoFragmentInteraction(String fName, String lName, String esiNumber, String pfNumber, View v);
    }
}
