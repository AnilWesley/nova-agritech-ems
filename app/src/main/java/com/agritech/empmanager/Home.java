package com.agritech.empmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.agritech.empmanager.databinding.ActivityHomeBinding;
import com.agritech.empmanager.fragments.CalendarFragment;
import com.agritech.empmanager.fragments.DashboardFragment;
import com.agritech.empmanager.fragments.ProfileFragment;
import com.agritech.empmanager.fragments.TeamsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Home extends AppCompatActivity implements
        DashboardFragment.OnDashboardFragmentInteractionListener,
        CalendarFragment.OnCalendarFragmentInteractionListener,
        ProfileFragment.OnProfileFragmentInteractionListener {

    ActivityHomeBinding binding;
    private FirebaseAuth mAuth;


    public static void start(Context context) {
        context.startActivity(new Intent(context, Home.class));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        binding.bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        loadFragment(new DashboardFragment());


    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = item -> {
        //Fragment fragment;
        switch (item.getItemId()) {
            case R.id.menuDashbord:


                loadFragment(new DashboardFragment());
                return true;

            case R.id.menuCalender:

                loadFragment(new CalendarFragment());
                return true;
            case R.id.menuProfile:
                loadFragment(new ProfileFragment());
                return true;
        }
        return false;
    };


    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.homeContainer, fragment);
        transaction.commit();
    }


    @Override
    public void onDashboardFragmentInteraction(Uri uri) {

    }

    @Override
    public void onDashboardFragmentInteractionViewEmployees() {
        EmployeesActivity.start(this);
    }

    @Override
    public void onDashboardFragmentInteractionDepartments() {
        DepartmentsActivity.start(this);
    }

    @Override
    public void onDashboardFragmentInteractionAddEmployee() {

        AddEmployeeActivity.start(this);

    }

    @Override
    public void onDashboardFragmentInteractionTeams() {

        TeamsActivity.start(this);


    }

    @Override
    public void onDashboardFragmentInteractionLogout() {
        FirebaseAuth.getInstance().signOut();
        LoginActivity.start(this, 0);
        finish();
    }

    @Override
    public void onCalendarFragmentInteraction(String uri) {
        setTitle(uri);
    }

    @Override
    public void onProfileFragmentInteraction(String title) {

    }
}
