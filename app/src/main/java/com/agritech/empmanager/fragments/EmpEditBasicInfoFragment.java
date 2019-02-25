package com.agritech.empmanager.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.agritech.empmanager.R;
import com.agritech.empmanager.databinding.FragmentEmpEditBasicInfoBinding;
import com.agritech.empmanager.pojo.Emp;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;



public class EmpEditBasicInfoFragment extends Fragment {

    private OnEmpEditBasicInfoFragmentInteractionListener mListener;


    FragmentEmpEditBasicInfoBinding binding;


    public EmpEditBasicInfoFragment() {
        // Required empty public constructor
    }

    public static EmpEditBasicInfoFragment setArguments(Emp emp) {

        EmpEditBasicInfoFragment fragment = new EmpEditBasicInfoFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelable("emp",emp);

        fragment.setArguments(bundle);

        return fragment;

    }


    Emp emp;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        emp = getArguments().getParcelable("emp");

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_emp_edit_basic_info, container, false);

        binding.setEmp(emp);

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
                mListener.onEmpEditBasicInfoFragmentInteraction(firstName, lastName, esiNumber, pfNumber,v);
            }


        });


        return binding.getRoot();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnEmpEditBasicInfoFragmentInteractionListener) {
            mListener = (OnEmpEditBasicInfoFragmentInteractionListener) context;
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

    public interface OnEmpEditBasicInfoFragmentInteractionListener {
        void onEmpEditBasicInfoFragmentInteraction(String fName, String lName, String esiNumber, String pfNumber, View v);
    }
}
