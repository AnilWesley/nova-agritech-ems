package com.agritech.empmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.agritech.empmanager.databinding.ActivityMyProfileBinding;
import com.agritech.empmanager.fastpojo.FastEmployee;
import com.agritech.empmanager.fragments.MyProfileFragment;
import com.agritech.empmanager.fragments.ViewEmployeeFragment;
import com.agritech.empmanager.pojo.Emp;
import com.agritech.empmanager.textdrawable.ColorGenerator;
import com.agritech.empmanager.textdrawable.TextDrawable;
import com.agritech.empmanager.utils.GlideApp;
import com.agritech.empmanager.utils.PrefUtilities;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MyProfileActivity extends AppCompatActivity implements MyProfileFragment.OnViewEmployeeFragmentInteractionListener {


    ActivityMyProfileBinding binding;

    FirebaseFirestore db;

    StorageReference storageRef;
    TextDrawable drawable;
    Emp emp;

    String UID, name;



    public static void start(Context context, String uid, String name) {
        Intent intent = new Intent(context, MyProfileActivity.class);
        intent.putExtra("emp_uid", uid);
        intent.putExtra("emp_name", name);

        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_profile);
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

        GlideApp.with(binding.ivProfile).load(storageRef).placeholder(drawable).into(binding.ivProfile);


        binding.pbLoading.setIndeterminateTintList(ColorStateList.valueOf(ColorGenerator.MATERIAL.getColor(name)));


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

            binding.pbLoading.setVisibility(View.GONE);

            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {


                    emp = document.toObject(Emp.class);

                    binding.tvDesignation.setText(emp.designation);


                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.viewEmployeeContainer, MyProfileFragment.setArguments(emp))
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

    @Override
    public void onViewEmployeeFragmentInteraction(String title) {

    }

    @Override
    public void onViewEmployeeFragmentInteractionEditInfo(String type) {

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int menuId = item.getItemId();

        if (menuId == android.R.id.home) {

            finish();

        }
        return super.onOptionsItemSelected(item);
    }



}
