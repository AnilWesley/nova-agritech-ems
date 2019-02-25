package com.agritech.empmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.agritech.empmanager.databinding.ActivityEmployeesBinding;
import com.agritech.empmanager.fragments.EmployeesFragment;

public class EmployeesActivity extends AppCompatActivity implements EmployeesFragment.OnEmployeesFragmentInteractionListener {


    public static void start(Context context) {
        context.startActivity(new Intent(context, EmployeesActivity.class));
    }


    public static void start(Context context, String dept) {

        Intent intent = new Intent(context, EmployeesActivity.class);
        intent.putExtra("dept", dept);
        context.startActivity(intent);

    }

    ActivityEmployeesBinding binding;

    String dept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_employees);

        setSupportActionBar(binding.toolbar);

        dept = getIntent().getStringExtra("dept");

        getSupportFragmentManager().beginTransaction().replace(R.id.employeeContainer, EmployeesFragment.setArguments(dept)).commit();


        setTitle("Employees (" + dept + ")");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    @Override
    public void onEmployeesFragmentInteraction() {

    }
}
