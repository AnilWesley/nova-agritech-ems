package com.agritech.empmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.agritech.empmanager.databinding.ActivityDepartmentsBinding;
import com.agritech.empmanager.databinding.ActivityHierarchyBinding;
import com.agritech.empmanager.fragments.DepartmentsFragment;
import com.agritech.empmanager.fragments.HierarchyFragment;

public class HierarchyActivity extends AppCompatActivity implements HierarchyFragment.OnHierarchyFragmentInteractionListener {


    public static void start(Context context, String UID) {
        Intent intent = new Intent(context, HierarchyActivity.class);
        intent.putExtra("uid", UID);
        context.startActivity(intent);
    }


    ActivityHierarchyBinding binding;


    String UID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_hierarchy);


        UID = getIntent().getStringExtra("uid");

        setSupportActionBar(binding.toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportFragmentManager().beginTransaction().replace(R.id.hierarchyContainer, HierarchyFragment.setArguments(UID)).commit();

        setTitle("Hierarchy");



    }


    @Override
    public void onHierarchyFragmentInteraction() {

    }
}
