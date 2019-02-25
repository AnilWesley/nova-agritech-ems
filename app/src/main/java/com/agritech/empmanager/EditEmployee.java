package com.agritech.empmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.agritech.empmanager.databinding.ActivityEditEmployeeBinding;
import com.agritech.empmanager.fragments.EmpEditBasicInfoFragment;
import com.agritech.empmanager.pojo.Emp;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditEmployee extends AppCompatActivity implements EmpEditBasicInfoFragment.OnEmpEditBasicInfoFragmentInteractionListener {

    public static final String EDIT_BASIC_INFO = "emp_edit_basic_info";
    public static final String EDIT_Work_INFO = "emp_edit_work_info";


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
}
