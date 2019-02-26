package com.agritech.empmanager.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.agritech.empmanager.R;
import com.agritech.empmanager.databinding.FragmentEmpEditBasicInfoBinding;
import com.agritech.empmanager.databinding.FragmentEmpEditLeaveInfoBinding;
import com.agritech.empmanager.pojo.Emp;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;


public class EmpEditLeaveInfoFragment extends Fragment {

    private OnEmpEditLeaveInfoFragmentInteractionListener mListener;


    FragmentEmpEditLeaveInfoBinding binding;


    public EmpEditLeaveInfoFragment() {
        // Required empty public constructor
    }

    public static EmpEditLeaveInfoFragment setArguments(Emp emp) {

        EmpEditLeaveInfoFragment fragment = new EmpEditLeaveInfoFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelable("emp", emp);

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

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_emp_edit_leave_info, container, false);

        binding.setEmp(emp);

        binding.acbSave.setOnClickListener(v -> {

            //General information

            String casualLeaves = binding.etCasualLeaves.getText().toString();
            String sickLeaves = binding.etSickLeaves.getText().toString();

            String annualLeaves = binding.etAnnualLeaves.getText().toString();

            String maternalLeaves = binding.etMaternalLeaves.getText().toString();

            String paternalLeaves = binding.etPaternalLeaves.getText().toString();


            if (casualLeaves.isEmpty()) {
                Snackbar.make(binding.getRoot(), "Casual Leaves is empty", Snackbar.LENGTH_LONG).show();
                return;
            }

            if (sickLeaves.isEmpty()) {
                Snackbar.make(binding.getRoot(), "Sick Leaves is empty", Snackbar.LENGTH_LONG).show();
                return;
            }


            if (annualLeaves.isEmpty()) {
                Snackbar.make(binding.getRoot(), "Annual Leaves is empty", Snackbar.LENGTH_LONG).show();
                return;
            }

            if (maternalLeaves.isEmpty()) {
                Snackbar.make(binding.getRoot(), "Maternal Leaves is empty", Snackbar.LENGTH_LONG).show();
                return;
            }

            if (paternalLeaves.isEmpty()) {
                Snackbar.make(binding.getRoot(), "Paternal Leaves is empty", Snackbar.LENGTH_LONG).show();
                return;
            }


            if (mListener != null) {
                mListener.onEmpEditLeaveInfoFragmentInteraction(casualLeaves, sickLeaves, annualLeaves, maternalLeaves,paternalLeaves, v);
            }


        });


        return binding.getRoot();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnEmpEditLeaveInfoFragmentInteractionListener) {
            mListener = (OnEmpEditLeaveInfoFragmentInteractionListener) context;
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

    public interface OnEmpEditLeaveInfoFragmentInteractionListener {
        void onEmpEditLeaveInfoFragmentInteraction(String casualLeaves, String sickLeaves, String annualLeaves, String maternalLeaves,String paternalLeaves, View v);
    }
}
