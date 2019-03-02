package com.agritech.empmanager.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.agritech.empmanager.R;
import com.agritech.empmanager.databinding.FragmentEmpEditWorkInfoBinding;
import com.agritech.empmanager.pojo.Emp;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;


public class EmpEditWorkInfoFragment extends Fragment {

    private OnEmpEditWorkInfoFragmentInteractionListener mListener;


    FragmentEmpEditWorkInfoBinding binding;

    Emp emp;

    ArrayAdapter arrayAdapter;


    public EmpEditWorkInfoFragment() {
        // Required empty public constructor
    }


    public static EmpEditWorkInfoFragment setArguments(Emp emp) {

        EmpEditWorkInfoFragment fragment = new EmpEditWorkInfoFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelable("emp", emp);

        fragment.setArguments(bundle);

        return fragment;

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        emp = getArguments().getParcelable("emp");

        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_emp_edit_work_info, container, false);

        binding.etReportingTo.setOnClickListener(v -> mListener.onEmpEditWorkInfoFragmentInteractionShowReportingTo());

        binding.setEmp(emp);


        arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.eventtypes));

        binding.etDepartment.setAdapter(arrayAdapter);
        binding.etDepartment.setInputType(0);

        binding.etDepartment.setOnClickListener(v -> {

            arrayAdapter.getFilter().filter(null);

            binding.etDepartment.showDropDown();
        });


            binding.acbSave.setOnClickListener(v -> {

                //General information

                String reportingToName = binding.etReportingTo.getText().toString();
                String department = binding.etDepartment.getText().toString();

                String designation = binding.etDesignation.getText().toString();

                String sourceOfHair = binding.etSourceofHair.getText().toString();


                if (department.isEmpty()) {
                    Snackbar.make(binding.getRoot(), "Department To is empty", Snackbar.LENGTH_LONG).show();
                    return;
                }

                if (designation.isEmpty()) {
                    Snackbar.make(binding.getRoot(), "Designation is empty", Snackbar.LENGTH_LONG).show();
                    return;
                }


                if (sourceOfHair.isEmpty()) {
                    Snackbar.make(binding.getRoot(), "Source Of Hair is empty", Snackbar.LENGTH_LONG).show();
                    return;
                }

          /*  if (pfNumber.isEmpty()) {
                Snackbar.make(binding.getRoot(), "PF Number is empty", Snackbar.LENGTH_LONG).show();
                return;
            }
*/

                if (mListener != null) {
                    mListener.onEmpEditWorkInfoFragmentInteraction(reportingToName, emp.reportingToUID, department, designation, sourceOfHair, v);
                }


            });


            return binding.getRoot();
        }


        @Override
        public void onAttach (Context context){
            super.onAttach(context);
            if (context instanceof OnEmpEditWorkInfoFragmentInteractionListener) {
                mListener = (OnEmpEditWorkInfoFragmentInteractionListener) context;
            } else {
                throw new RuntimeException(context.toString()
                        + " must implement OnFragmentInteractionListener");
            }
        }

        @Override
        public void onDetach () {
            super.onDetach();
            mListener = null;
        }

        public void updateReportingTo (String uid, String name){

            binding.etReportingTo.setText(name);

            emp.reportingToName = name;
            emp.reportingToUID = uid;


        }

        public interface OnEmpEditWorkInfoFragmentInteractionListener {
            void onEmpEditWorkInfoFragmentInteraction(String reportingToName, String reportingToUID, String department, String designation, String sourceOfHire, View v);

            void onEmpEditWorkInfoFragmentInteractionShowReportingTo();

        }
    }
