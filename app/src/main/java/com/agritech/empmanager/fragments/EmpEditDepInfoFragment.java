package com.agritech.empmanager.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;

import com.agritech.empmanager.R;
import com.agritech.empmanager.databinding.FragmentEmpEditDepInfoBinding;
import com.agritech.empmanager.pojo.Emp;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;


public class EmpEditDepInfoFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    private OnEmpEditDepInfoFragmentInteractionListener mListener;


    FragmentEmpEditDepInfoBinding binding;


    public EmpEditDepInfoFragment() {
        // Required empty public constructor
    }

    public static EmpEditDepInfoFragment setArguments(Emp emp) {

        EmpEditDepInfoFragment fragment = new EmpEditDepInfoFragment();

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

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_emp_edit_dep_info, container, false);

        binding.setEmp(emp);


        binding.etDependentDateOfBirth.setOnClickListener(v -> {

            Calendar calendar = Calendar.getInstance();

            new DatePickerDialog(
                    getActivity(),
                    EmpEditDepInfoFragment.this,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            ).show();

        });


        binding.acbSave.setOnClickListener(v -> {

            //General information

            String name = binding.etDepName.getText().toString();
            String dob = binding.etDependentDateOfBirth.getText().toString();

            String relationship = binding.etRelationship.getText().toString();

            String occupation = binding.etOccupation.getText().toString();


            if (name.isEmpty()) {
                Snackbar.make(binding.getRoot(), "Name is empty", Snackbar.LENGTH_LONG).show();
                return;
            }

            if (dob.isEmpty()) {
                Snackbar.make(binding.getRoot(), "Date Of Birth is empty", Snackbar.LENGTH_LONG).show();
                return;
            }


            if (relationship.isEmpty()) {
                Snackbar.make(binding.getRoot(), "Relationship is empty", Snackbar.LENGTH_LONG).show();
                return;
            }

            if (occupation.isEmpty()) {
                Snackbar.make(binding.getRoot(), "Occupation is empty", Snackbar.LENGTH_LONG).show();
                return;
            }


            if (mListener != null) {
                mListener.onEmpEditDepInfoFragmentInteraction(name, relationship, dob, occupation, v);
            }


        });


        return binding.getRoot();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnEmpEditDepInfoFragmentInteractionListener) {
            mListener = (OnEmpEditDepInfoFragmentInteractionListener) context;
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

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        binding.etDependentDateOfBirth.setText(dayOfMonth + "/" + (month + 1) + "/" + year);


    }

    public interface OnEmpEditDepInfoFragmentInteractionListener {
        void onEmpEditDepInfoFragmentInteraction(String name, String relationship, String dob, String occupation, View v);
    }
}
