package com.agritech.empmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.agritech.empmanager.databinding.ActivityDepartmentsBinding;
import com.agritech.empmanager.fragments.DepartmentsFragment;
import com.agritech.empmanager.fragments.EmployeesFragment;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class DepartmentsActivity extends AppCompatActivity implements DepartmentsFragment.OnDepartmentsFragmentInteractionListener {

    ActivityDepartmentsBinding binding;

    public static void start(Context context) {
        context.startActivity(new Intent(context, DepartmentsActivity.class));
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_departments);


        setSupportActionBar(binding.toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportFragmentManager().beginTransaction().replace(R.id.departmentsContainer, new DepartmentsFragment()).commit();

        setTitle("Departments");

    }



  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.view_employee_fragment_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }*/


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int menuId = item.getItemId();

        if (menuId == android.R.id.home) {

            finish();

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onDepartmentsFragmentInteraction(Uri uri) {

    }
}



