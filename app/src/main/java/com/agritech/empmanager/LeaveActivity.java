package com.agritech.empmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.agritech.empmanager.databinding.SingleLeaveBinding;
import com.agritech.empmanager.pojo.Leave;
import com.agritech.empmanager.utils.RecyclerViewItemClickListener;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

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

    FirestoreRecyclerAdapter adapter;

    Calendar calendar;

    SimpleDateFormat sdf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_leave);

        binding.rvLeaves.setLayoutManager(new LinearLayoutManager(this));
        binding.rvLeaves.setHasFixedSize(true);

        sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

        setSupportActionBar(binding.toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("My Leaves");

        calendar = Calendar.getInstance();

        UID = getIntent().getStringExtra("uid");
        forHR = getIntent().getBooleanExtra("forHR", false);

        Query query = FirebaseFirestore.getInstance()
                .collection("leaves")
                .whereEqualTo("year", String.valueOf(Calendar.getInstance().get(Calendar.YEAR)))
                .whereEqualTo("appliedByUID", UID)
                .orderBy("fromDate");

        FirestoreRecyclerOptions<Leave> options = new FirestoreRecyclerOptions.Builder<Leave>()
                .setQuery(query, snapshot -> {

                    Leave leave = snapshot.toObject(Leave.class);

                    calendar.setTimeInMillis(leave.fromDate);
                    leave.fromDateAs = sdf.format(calendar.getTime());

                    calendar.setTimeInMillis(leave.toDate);
                    leave.toDateAs = sdf.format(calendar.getTime());


                    return leave;
                })
                .build();


        adapter = new FirestoreRecyclerAdapter<Leave, ViewHolder>(options) {
            @Override
            public void onBindViewHolder(ViewHolder holder, int position, Leave model) {

                holder.binding.setLeave(model);

                holder.binding.getRoot().setTag(model);

            }

            @Override
            public ViewHolder onCreateViewHolder(ViewGroup group, int i) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(group.getContext()).inflate(R.layout.single_leave, group, false);

                return new ViewHolder(view);
            }
        };

        binding.rvLeaves.setItemAnimator(new DefaultItemAnimator());

        binding.rvLeaves.setAdapter(adapter);

        binding.rvLeaves.setEmptyView(binding.tvEmptyView);


        binding.tvEmptyView.setVisibility(View.GONE);

        binding.rvLeaves.addOnItemTouchListener(new RecyclerViewItemClickListener(this,
                (v, position) -> {

                    ViewLeaveActivity.start(LeaveActivity.this, forHR, (Leave) v.getTag());


                }));


    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }


    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
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

        SingleLeaveBinding binding;

        public ViewHolder(View view) {
            super(view);
            binding = SingleLeaveBinding.bind(view);
        }

    }


}
