package com.agritech.empmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.agritech.empmanager.databinding.ActivityEditEmployeeBinding;
import com.agritech.empmanager.fragments.EmpEditBasicInfoFragment;
import com.agritech.empmanager.fragments.EmpEditLeaveInfoFragment;
import com.agritech.empmanager.fragments.EmpEditPersonalInfoFragment;
import com.agritech.empmanager.fragments.EmpEditWorkInfoFragment;
import com.agritech.empmanager.fragments.SelectReportingToFragment;
import com.agritech.empmanager.pojo.Emp;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditEmployee extends AppCompatActivity implements
        EmpEditBasicInfoFragment.OnEmpEditBasicInfoFragmentInteractionListener,
        EmpEditWorkInfoFragment.OnEmpEditWorkInfoFragmentInteractionListener,
        EmpEditLeaveInfoFragment.OnEmpEditLeaveInfoFragmentInteractionListener,
        EmpEditPersonalInfoFragment.OnEmpEditPersonalInfoFragmentInteractionListener,
        SelectReportingToFragment.OnSelectReportingToFragmentInteractionListener {

    public static final String EDIT_BASIC_INFO = "emp_edit_basic_info";
    public static final String EDIT_WORK_INFO = "emp_edit_work_info";
    public static final String EDIT_LEAVE_INFO = "emp_edit_leave_info";
    public static final String EDIT_PERSONAL_INFO = "emp_edit_personal_info";

    SelectReportingToFragment selectReportingToFragment;

    public static void start(Context context, String type, Emp emp) {

        Intent intent = new Intent(context, EditEmployee.class);
        intent.putExtra("type", type);
        intent.putExtra("emp", emp);
        context.startActivity(intent);

    }


    Emp emp;

    String type;
    FirebaseFirestore db;


    ActivityEditEmployeeBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_employee);

        setSupportActionBar(binding.toolbar);

        type = getIntent().getStringExtra("type");
        emp = getIntent().getParcelableExtra("emp");


        db = FirebaseFirestore.getInstance();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (type.equals(EDIT_BASIC_INFO)) {

            setTitle("Edit basic info");

            getSupportFragmentManager().beginTransaction().replace(R.id.editEmployeeContainer, EmpEditBasicInfoFragment.setArguments(emp)).commit();

        } else if (type.equals(EDIT_WORK_INFO)) {
            selectReportingToFragment = new SelectReportingToFragment();

            setTitle("Edit work info");

            getSupportFragmentManager().beginTransaction().replace(R.id.editEmployeeContainer, EmpEditWorkInfoFragment.setArguments(emp), "frag_edit_work_info").commit();

        } else if (type.equals(EDIT_LEAVE_INFO)) {

            setTitle("Edit leave info");

            getSupportFragmentManager().beginTransaction().replace(R.id.editEmployeeContainer, EmpEditLeaveInfoFragment.setArguments(emp)).commit();

        } else if (type.equals(EDIT_PERSONAL_INFO)) {

            setTitle("Edit personal info");

            getSupportFragmentManager().beginTransaction().replace(R.id.editEmployeeContainer, EmpEditPersonalInfoFragment.setArguments(emp)).commit();

        }


    }


    @Override
    public void onEmpEditBasicInfoFragmentInteraction(String fName, String lName, String esiNumber, String pfNumber, View v) {

        v.setEnabled(false);

        Map<String, Object> user = new HashMap<>();
        user.put("fName", fName);
        user.put("lName", lName);
        user.put("esiNumber", esiNumber);
        user.put("pfNumber", pfNumber);

        db.collection("Employees").document(emp.uid).update(user)
                .addOnSuccessListener(documentReference -> {

                    finish();

                })
                .addOnFailureListener(e -> {

                    v.setEnabled(true);
                    Snackbar.make(binding.getRoot(), "Error, " + e.getMessage(), Snackbar.LENGTH_LONG).show();


                });


    }


    @Override
    public void onEmpEditWorkInfoFragmentInteraction(String reportingToName, String reportingToUID, String department, String designation, String sourceOfHire, View v) {


        v.setEnabled(false);

        Map<String, Object> user = new HashMap<>();
        user.put("designation", designation);
        user.put("department", department);
        user.put("sourceOfHire", sourceOfHire);
        user.put("reportingToName", reportingToName);
        user.put("reportingToUID", reportingToUID);

        db.collection("Employees").document(emp.uid).update(user)
                .addOnSuccessListener(documentReference -> {

                    finish();

                })
                .addOnFailureListener(e -> {

                    v.setEnabled(true);
                    Snackbar.make(binding.getRoot(), "Error, " + e.getMessage(), Snackbar.LENGTH_LONG).show();


                });

    }

    @Override
    public void onEmpEditWorkInfoFragmentInteractionShowReportingTo() {

        selectReportingToFragment.show(getSupportFragmentManager(), "select_reporting_to");

    }

    @Override
    public void onSelectReportingToFragmentInteraction(String uid, String name) {

        EmpEditWorkInfoFragment fragment = (EmpEditWorkInfoFragment) getSupportFragmentManager().findFragmentByTag("frag_edit_work_info");

        if (fragment != null)
            fragment.updateReportingTo(uid, name);


        selectReportingToFragment.dismiss();


    }


    @Override
    public void onEmpEditLeaveInfoFragmentInteraction(String casualLeaves, String sickLeaves, String annualLeaves, String maternalLeaves, String paternalLeaves, View v) {


        v.setEnabled(false);

        Map<String, Object> user = new HashMap<>();
        user.put("casualLeaves", casualLeaves);
        user.put("sickLeaves", sickLeaves);
        user.put("annualLeaves", annualLeaves);
        user.put("maternalLeaves", maternalLeaves);
        user.put("paternalLeaves", paternalLeaves);

        db.collection("Employees").document(emp.uid).update(user)
                .addOnSuccessListener(documentReference -> {

                    finish();

                })
                .addOnFailureListener(e -> {

                    v.setEnabled(true);
                    Snackbar.make(binding.getRoot(), "Error, " + e.getMessage(), Snackbar.LENGTH_LONG).show();


                });


    }

    @Override
    public void onEmpEditPersonalInfoFragmentInteraction(String phone, String dob, String mStatus, String address, View v) {

        v.setEnabled(false);

        Map<String, Object> user = new HashMap<>();
        user.put("phone", phone);
        user.put("dob", dob);
        user.put("maritalStatus", mStatus);
        user.put("address", address);

        db.collection("Employees").document(emp.uid).update(user)
                .addOnSuccessListener(documentReference -> {

                    finish();

                })
                .addOnFailureListener(e -> {

                    v.setEnabled(true);
                    Snackbar.make(binding.getRoot(), "Error, " + e.getMessage(), Snackbar.LENGTH_LONG).show();


                });


    }
}
