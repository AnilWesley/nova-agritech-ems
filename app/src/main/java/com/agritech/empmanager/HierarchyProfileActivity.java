package com.agritech.empmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.agritech.empmanager.databinding.ActivityHierarchyProfileBinding;
import com.agritech.empmanager.fragments.HierarchyProfileFragment;
import com.agritech.empmanager.pojo.Emp;
import com.agritech.empmanager.textdrawable.ColorGenerator;
import com.agritech.empmanager.textdrawable.TextDrawable;
import com.agritech.empmanager.utils.GlideApp;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class HierarchyProfileActivity extends AppCompatActivity implements HierarchyProfileFragment.OnFragmentInteractionListener {

    ActivityHierarchyProfileBinding binding;

    FirebaseFirestore db;

    StorageReference storageRef;
    TextDrawable drawable;
    Emp emp;

    String UID, name;


    public static void start(Context context, String uid, String name) {
        Intent intent = new Intent(context, HierarchyProfileActivity.class);
        intent.putExtra("emp_uid", uid);
        intent.putExtra("emp_name", name);

        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_hierarchy_profile);

        setSupportActionBar(binding.toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        UID = getIntent().getStringExtra("emp_uid");
        name = getIntent().getStringExtra("emp_name");
        binding.tvName.setText(name);

        storageRef = FirebaseStorage.getInstance().getReference().child("profilePic/").child(UID + ".jpg");

        drawable = TextDrawable.builder()
                .beginConfig()
                .height(200)
                .width(200)
                .endConfig()
                .buildRect(name.charAt(0) + "", ColorGenerator.MATERIAL.getColor(name));

        GlideApp.with(binding.ivProfilePic).load(storageRef).placeholder(drawable).into(binding.ivProfilePic);


        binding.pBarUpload.setIndeterminateTintList(ColorStateList.valueOf(ColorGenerator.MATERIAL.getColor(name)));


        binding.appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    binding.toolbarLayout.setTitle(name);
                    isShow = true;
                } else if (isShow) {
                    binding.toolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });

        db = FirebaseFirestore.getInstance();

        db.collection("Employees").document(UID).get().addOnCompleteListener(this, task -> {

            binding.pBarUpload.setVisibility(View.GONE);

            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {


                    emp = document.toObject(Emp.class);

                    binding.tvDesignation.setText(emp.designation);


                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.hierarchyProfileContainer, HierarchyProfileFragment.setArguments(emp))
                            .commit();


                    //Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                } else {
                    //Log.d(TAG, "No such document");
                }
            } else {
                //Log.d(TAG, "get failed with ", task.getException());
            }
        });

    }

    public void startProfileActivity(View view) {

        MyProfileActivity.start(this, emp.uid, emp.fName + " " + emp.lName);

    }

    public void startHolidaysActivity(View view) {

        HolidaysActivity.start(this, false);

    }

    public void startHRHolidaysActivity(View view) {

        HolidaysActivity.start(this, true);

    }

    public void startHRNewEmpActivity(View view) {

        AddEmployeeActivity.start(this);

    }

    public void startHRDeptActivity(View view) {

        DepartmentsActivity.start(this);

    }

    public void startHRTeamsActivity(View view) {

        TeamsActivity.start(this);

    }


    public void startHierarchyActivity(View view) {

        HierarchyActivity.start(this, UID);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void startMyTeamsActivity(View view) {

        if (emp.teams == null) {
            Snackbar.make(binding.getRoot(), "No teams available", Snackbar.LENGTH_LONG).show();
        } else if (emp.teams.size() > 1) {
            MyTeamsActivity.start(this);
        } else if (emp.teams.size() == 1) {
            EmployeesActivity.start(this, emp.teams.get(0), EmployeesActivity.INTENT_ACTION_TEAM, false);
        } else {
            Snackbar.make(binding.getRoot(), "No teams available", Snackbar.LENGTH_LONG).show();
        }

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
