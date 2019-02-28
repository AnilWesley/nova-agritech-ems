package com.agritech.empmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.agritech.empmanager.databinding.ActivityMyTeamsBinding;
import com.agritech.empmanager.fastpojo.FastDept;
import com.agritech.empmanager.fragments.MyProfileFragment;
import com.agritech.empmanager.pojo.Emp;
import com.agritech.empmanager.utils.PrefUtilities;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter.listeners.OnClickListener;

import java.util.ArrayList;

public class MyTeamsActivity extends AppCompatActivity {

    ActivityMyTeamsBinding binding;

    FastAdapter fastAdapter;
    ItemAdapter itemAdapter;

    FirebaseFirestore db;

    String UID;

    public static void start(Context context) {
        context.startActivity(new Intent(context, MyTeamsActivity.class));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_teams);

        UID = PrefUtilities.with(this).getUserId();

        setSupportActionBar(binding.toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        setTitle("My Teams");


        db = FirebaseFirestore.getInstance();
        binding.rvMyTeams.setLayoutManager(new LinearLayoutManager(this));
        binding.rvMyTeams.setHasFixedSize(true);

        itemAdapter = new ItemAdapter();


        fastAdapter = FastAdapter.with(itemAdapter);

        fastAdapter.setHasStableIds(true);

        binding.rvMyTeams.setItemAnimator(new DefaultItemAnimator());

        binding.rvMyTeams.setAdapter(fastAdapter);
        binding.rvMyTeams.setEmptyView(binding.tvEmptyView);


        binding.tvEmptyView.setVisibility(View.GONE);


        db.collection("Employees").document(UID).get().addOnCompleteListener(this, task -> {

            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {


                    Emp emp = document.toObject(Emp.class);

                    ArrayList<String> teams = emp.teams;

                    for (int i=0;i<teams.size();i++)
                        itemAdapter.add(new FastDept(teams.get(i)));


                    //Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                } else {
                    //Log.d(TAG, "No such document");
                }
            } else {
                //Log.d(TAG, "get failed with ", task.getException());
            }
        });

        fastAdapter.withSelectable(true);
        fastAdapter.withOnClickListener((OnClickListener<FastDept>) (v, adapter, item, position) -> {
            EmployeesActivity.start(this, item.name, EmployeesActivity.INTENT_ACTION_TEAM,false);
            return true;
        });


    }
}
