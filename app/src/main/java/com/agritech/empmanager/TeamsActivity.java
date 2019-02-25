package com.agritech.empmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.agritech.empmanager.databinding.ActivityTeamsBinding;
import com.agritech.empmanager.fragments.DepartmentsFragment;
import com.agritech.empmanager.fragments.TeamsFragment;

public class TeamsActivity extends AppCompatActivity implements TeamsFragment.OnTeamsFragmentInteractionListener {

    ActivityTeamsBinding binding;

    public static void start(Context context) {
        context.startActivity(new Intent(context, TeamsActivity.class));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_teams);

        setSupportActionBar(binding.toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportFragmentManager().beginTransaction().replace(R.id.teamsContainer, new TeamsFragment()).commit();


        setTitle("Teams");


    }

    @Override
    public void onTeamsFragmentInteraction(Uri uri) {




    }
}
