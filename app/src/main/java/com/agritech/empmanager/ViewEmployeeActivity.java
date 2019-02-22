package com.agritech.empmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.agritech.empmanager.fastpojo.FastEmployee;
import com.agritech.empmanager.fragments.EmployeesFragment;
import com.agritech.empmanager.fragments.ViewEmployeeFragment;

public class ViewEmployeeActivity extends AppCompatActivity implements ViewEmployeeFragment.OnViewEmployeeFragmentInteractionListener {

    public static void start(Context context, FastEmployee fastEmployee) {

        //fastEmployee.uid

        Intent intent = new Intent(context, ViewEmployeeActivity.class);
        intent.putExtra("emp_uid", fastEmployee.uid);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_employee);


        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.viewEmployeeContainer, ViewEmployeeFragment.setArguments(getIntent().getStringExtra("emp_uid")))
                .commit();


    }

    @Override
    public void onViewEmployeeFragmentInteraction(String title) {

    }
}
