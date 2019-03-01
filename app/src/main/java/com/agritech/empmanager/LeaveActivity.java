package com.agritech.empmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.agritech.empmanager.databinding.ActivityLeaveBinding;
import com.agritech.empmanager.databinding.SingleDepartmentBinding;
import com.agritech.empmanager.fastpojo.FastLeave;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Calendar;

public class LeaveActivity extends AppCompatActivity {

    public static void start(Context context, String uid, boolean forHR) {

        Intent intent = new Intent(context, LeaveActivity.class);
        intent.putExtra("uid", uid);
        intent.putExtra("forHR", forHR);

        context.startActivity(intent);
    }

    ActivityLeaveBinding binding;

    String UID;
    boolean forHR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_leave);

        setSupportActionBar(binding.toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("My Leaves");

        UID = getIntent().getStringExtra("uid");
        forHR = getIntent().getBooleanExtra("forHR", false);


        Query query = FirebaseFirestore.getInstance()

                .collection("leaves")
                .whereEqualTo("year", String.valueOf(Calendar.getInstance().get(Calendar.YEAR)))
                .whereEqualTo("appliedByUID", UID)
                .orderBy("fromDate");

        FirestoreRecyclerOptions<FastLeave> options = new FirestoreRecyclerOptions.Builder<FastLeave>()
                .setQuery(query, FastLeave.class)
                .build();


        FirestoreRecyclerAdapter adapter = new FirestoreRecyclerAdapter<FastLeave, ViewHolder>(options) {
            @Override
            public void onBindViewHolder(ViewHolder holder, int position, FastLeave model) {
                // Bind the Chat object to the ChatHolder
                // ...
            }

            @Override
            public ViewHolder onCreateViewHolder(ViewGroup group, int i) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(group.getContext()).inflate(R.layout.single_leave, group, false);

                return new ViewHolder(view);
            }
        };


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (!forHR)
            getMenuInflater().inflate(R.menu.fragment_holidays_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menuAdd) {
            NewLeaveActivity.start(this);
        } else if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        SingleDepartmentBinding binding;

        public ViewHolder(View view) {
            super(view);
            binding = SingleDepartmentBinding.bind(view);
        }

    }


}
