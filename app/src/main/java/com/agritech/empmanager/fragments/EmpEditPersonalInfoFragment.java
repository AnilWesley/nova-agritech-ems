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
import com.agritech.empmanager.databinding.FragmentEmpEditPersonalInfoBinding;
import com.agritech.empmanager.pojo.Emp;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;


public class EmpEditPersonalInfoFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    private OnEmpEditPersonalInfoFragmentInteractionListener mListener;


    FragmentEmpEditPersonalInfoBinding binding;


    public EmpEditPersonalInfoFragment() {
        // Required empty public constructor
    }

    public static EmpEditPersonalInfoFragment setArguments(Emp emp) {

        EmpEditPersonalInfoFragment fragment = new EmpEditPersonalInfoFragment();

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

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_emp_edit_personal_info, container, false);

        binding.setEmp(emp);


        ArrayAdapter arrayAdapter= new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.mStatus));
        binding.etMaritalStatus.setAdapter(arrayAdapter);
        binding.etMaritalStatus.setInputType(0);

        binding.etMaritalStatus.setOnClickListener(v -> {
            arrayAdapter.getFilter().filter(null);
            binding.etMaritalStatus.showDropDown();
        });

        binding.etDateOfBirth.setOnClickListener(v -> {

            Calendar calendar = Calendar.getInstance();

            new DatePickerDialog(
                    getActivity(),
                    EmpEditPersonalInfoFragment.this,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            ).show();

        });


        binding.acbSave.setOnClickListener(v -> {

            //General information

            String phone = binding.etMobileNumber.getText().toString();
            String dob = binding.etDateOfBirth.getText().toString();

            String maritalStatus = binding.etMaritalStatus.getText().toString();

            String address = binding.etAddress.getText().toString();


            if (phone.isEmpty()) {
                Snackbar.make(binding.getRoot(), "Phone is empty", Snackbar.LENGTH_LONG).show();
                return;
            }

            if (dob.isEmpty()) {
                Snackbar.make(binding.getRoot(), "Date Of Birth is empty", Snackbar.LENGTH_LONG).show();
                return;
            }


            if (maritalStatus.isEmpty()) {
                Snackbar.make(binding.getRoot(), "Marital Status is empty", Snackbar.LENGTH_LONG).show();
                return;
            }

            if (address.isEmpty()) {
                Snackbar.make(binding.getRoot(), "Address is empty", Snackbar.LENGTH_LONG).show();
                return;
            }


            if (mListener != null) {
                mListener.onEmpEditPersonalInfoFragmentInteraction(phone, dob, maritalStatus, address, v);
            }


        });


        return binding.getRoot();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnEmpEditPersonalInfoFragmentInteractionListener) {
            mListener = (OnEmpEditPersonalInfoFragmentInteractionListener) context;
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

        binding.etDateOfBirth.setText(dayOfMonth+"/"+(month+1)+"/"+year);


    }

    public interface OnEmpEditPersonalInfoFragmentInteractionListener {
        void onEmpEditPersonalInfoFragmentInteraction(String phone, String dob, String mStatus, String address, View v);
    }
}
