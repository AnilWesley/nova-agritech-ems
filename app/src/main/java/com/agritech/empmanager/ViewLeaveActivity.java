package com.agritech.empmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.agritech.empmanager.databinding.ActivityViewLeaveBinding;
import com.agritech.empmanager.pojo.Leave;
import com.agritech.empmanager.textdrawable.ColorGenerator;
import com.agritech.empmanager.textdrawable.TextDrawable;
import com.agritech.empmanager.utils.GlideApp;
import com.agritech.empmanager.utils.PrefUtilities;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class ViewLeaveActivity extends AppCompatActivity {

    public static void start(Context context, boolean isHigherAuthority, Leave leave) {

        Intent intent = new Intent(context, ViewLeaveActivity.class);
        intent.putExtra("leave", leave);
        intent.putExtra("isHigherAuthority", isHigherAuthority);

        context.startActivity(intent);
    }


    ActivityViewLeaveBinding binding;

    boolean isHigherAuthority;
    Leave leave;

    StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_leave);


        setSupportActionBar(binding.toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Leave");


        isHigherAuthority = getIntent().getBooleanExtra("isHigherAuthority", false);
        leave = getIntent().getParcelableExtra("leave");

        binding.setLeave(leave);


        if (isHigherAuthority) {
            binding.llControls.setVisibility(View.VISIBLE);

            if (leave.status.equals("Accepted")) {
                binding.acbAccept.setEnabled(false);

                binding.llAcceptedBy.setVisibility(View.VISIBLE);

                storageRef = FirebaseStorage.getInstance().getReference().child("profilePic/").child(leave.statusUpdateByUID + ".jpg");

                TextDrawable drawable = TextDrawable.builder()
                        .beginConfig()
                        .height(200)
                        .width(200)
                        .endConfig()
                        .buildRect(leave.statusUpdateByName.charAt(0) + "", ColorGenerator.MATERIAL.getColor(leave.statusUpdateByName));

                GlideApp.with(binding.ivProfile).load(storageRef).placeholder(drawable).into(binding.ivProfile);


            }

        }


    }


    public boolean onCreateOptionsMenu(Menu menu) {

        if (!isHigherAuthority)
            getMenuInflater().inflate(R.menu.view_leave_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menuDelete) {

            FirebaseFirestore.getInstance().collection("leaves").document(leave.uuid).delete();
            finish();

        } else if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


    public void acceptLeave(View view) {

        Map<String, Object> map = new HashMap<>();
        map.put("status", "Accepted");
        map.put("statusUpdateByName", PrefUtilities.with(this).getName());
        map.put("statusUpdateByUID", PrefUtilities.with(this).getUserId());

        FirebaseFirestore.getInstance().collection("leaves").document(leave.uuid).update(map);

        finish();

    }

    public void rejectLeave(View view) {

        if (leave.status.equals("Accepted")) {

            if (PrefUtilities.with(this).getUserId().equals(leave.statusUpdateByUID)) {
                FirebaseFirestore.getInstance().collection("leaves").document(leave.uuid).delete();
                finish();
            } else {
                Snackbar.make(binding.getRoot(),"Only "+leave.statusUpdateByName+" can reject this leave",Snackbar.LENGTH_SHORT).show();
            }

        } else {
            FirebaseFirestore.getInstance().collection("leaves").document(leave.uuid).delete();
            finish();
        }

    }
}
