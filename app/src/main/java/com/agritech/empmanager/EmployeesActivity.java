package com.agritech.empmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.agritech.empmanager.databinding.ActivityEmployeesBinding;
import com.agritech.empmanager.fragments.EmployeesFragment;
import com.agritech.empmanager.fragments.SelectReportingToFragment;
import com.agritech.empmanager.fragments.TeamNumbersFragment;
import com.agritech.empmanager.fragments.TeamsFragment;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class EmployeesActivity extends AppCompatActivity implements
        EmployeesFragment.OnEmployeesFragmentInteractionListener,
        TeamNumbersFragment.OnTeamNumbersFragmentInteractionListener,
        SelectReportingToFragment.OnSelectReportingToFragmentInteractionListener{


    public static void start(Context context) {
        context.startActivity(new Intent(context, EmployeesActivity.class));
    }


    public static final String INTENT_ACTION_TEAM = "intent_action_team";
    public static final String INTENT_ACTION_DEPT = "intent_action_dept";

    SelectReportingToFragment selectReportingToFragment;

    public static void start(Context context, String name, String action, boolean isHR) {

        Intent intent = new Intent(context, EmployeesActivity.class);
        intent.setAction(action);
        intent.putExtra("name", name);
        intent.putExtra("forHR", isHR);
        context.startActivity(intent);

    }

    ActivityEmployeesBinding binding;

    String name, action;
    boolean forHR = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_employees);

        setSupportActionBar(binding.toolbar);

        action = getIntent().getAction();
        name = getIntent().getStringExtra("name");
        forHR = getIntent().getBooleanExtra("forHR",false);

        setTitle("Employees (" + name + ")");


        if (action.equals(INTENT_ACTION_DEPT)) {

            getSupportFragmentManager().beginTransaction().replace(R.id.employeeContainer, EmployeesFragment.setArguments(name,forHR)).commit();

        } else {
            selectReportingToFragment = new SelectReportingToFragment();

            getSupportFragmentManager().beginTransaction().replace(R.id.employeeContainer, TeamNumbersFragment.setArguments(name,forHR)).commit();

        }


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    @Override
    public void onEmployeesFragmentInteraction() {

    }


    @Override
    public void onTeamNumbersFragmentInteraction() {

        if (selectReportingToFragment != null)
            selectReportingToFragment.show(getSupportFragmentManager(), "select_team_numbers");

    }

    @Override
    public void onSelectReportingToFragmentInteraction(String uid, String name) {

        FirebaseFirestore.getInstance().collection("Employees").document(uid).update("teams", FieldValue.arrayUnion(this.name));

        if (selectReportingToFragment != null)
            selectReportingToFragment.dismiss();


    }
}
