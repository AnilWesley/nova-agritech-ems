package com.agritech.empmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.agritech.empmanager.databinding.ActivityHomeNewBinding;
import com.agritech.empmanager.fragments.HomeNewFragment;
import com.agritech.empmanager.pojo.Emp;
import com.agritech.empmanager.pojo.NewEmpBasic;
import com.agritech.empmanager.textdrawable.ColorGenerator;
import com.agritech.empmanager.textdrawable.TextDrawable;
import com.agritech.empmanager.utils.GlideApp;
import com.agritech.empmanager.utils.PrefUtilities;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class HomeNewActivity extends AppCompatActivity implements HomeNewFragment.OnHomeNewFragmentInteractionListener {

    ActivityHomeNewBinding binding;

    TextDrawable drawable;

    StorageReference storageRef;

    FirebaseFirestore db;

    String UID, name;
    Emp emp;


    public static void start(Context context) {
        context.startActivity(new Intent(context, HomeNewActivity.class));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home_new);

        setSupportActionBar(binding.toolbar);


        UID = PrefUtilities.with(this).getUserId();
        name = PrefUtilities.with(this).getName();


        storageRef = FirebaseStorage.getInstance().getReference().child("profilePic/").child(UID + ".jpg");


        drawable = TextDrawable.builder()
                .beginConfig()
                .height(200)
                .width(200)
                .endConfig()
                .buildRect(name.charAt(0) + "", ColorGenerator.MATERIAL.getColor(name));

        GlideApp.with(binding.ivProfile).load(storageRef).placeholder(drawable).into(binding.ivProfile);

        binding.pbLoading.setIndeterminateTintList(ColorStateList.valueOf(ColorGenerator.MATERIAL.getColor(name)));

        binding.tvName.setText(name);

        db = FirebaseFirestore.getInstance();

        db.collection("Employees").document(UID).get().addOnCompleteListener(this, task -> {

            binding.pbLoading.setVisibility(View.GONE);

            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {


                    emp = document.toObject(Emp.class);

                    binding.tvDesignation.setText(emp.designation);


                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.homeContainer, new HomeNewFragment())
                            .commit();


                    //Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                } else {
                    //Log.d(TAG, "No such document");
                }
            } else {
                //Log.d(TAG, "get failed with ", task.getException());
            }
        });


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

    }

    @Override
    public void onHomeNewFragmentInteraction(String title) {

    }

    @Override
    public void onHomeNewFragmentInteractionEditInfo(String type) {

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
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.dashboard_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menuLogout) {
            FirebaseAuth.getInstance().signOut();
            LoginActivity.start(this, 0);
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

    public void startMyLeavesActivity(View view) {

        LeaveActivity.start(this);

    }
}
