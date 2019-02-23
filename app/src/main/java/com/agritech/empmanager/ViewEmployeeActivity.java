package com.agritech.empmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.agritech.empmanager.databinding.ActivityViewEmployeeBinding;
import com.agritech.empmanager.fastpojo.FastEmployee;
import com.agritech.empmanager.fragments.ViewEmployeeFragment;
import com.agritech.empmanager.textdrawable.ColorGenerator;
import com.agritech.empmanager.textdrawable.TextDrawable;
import com.agritech.empmanager.utils.GlideApp;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class ViewEmployeeActivity extends AppCompatActivity implements ViewEmployeeFragment.OnViewEmployeeFragmentInteractionListener {


    ActivityViewEmployeeBinding binding;
    FirebaseFirestore db;

    StorageReference storageRef;

    String name = "Profile", designation, emailId, department;

    String UID;
    TextDrawable drawable;


    public static void start(Context context, FastEmployee fastEmployee, View view) {

        //fastEmployee.uid
        Intent intent = new Intent(context, ViewEmployeeActivity.class);
        intent.putExtra("emp_uid", fastEmployee.uid);
        intent.putExtra("emp_name", fastEmployee.fName + " " + fastEmployee.lName);

        //ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) context, view, view.getTransitionName());

        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_employee);
        setSupportActionBar(binding.toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        storageRef = FirebaseStorage.getInstance().getReference().child("profilePic/").child(UID + ".jpg");

        UID = getIntent().getStringExtra("emp_uid");
        name = getIntent().getStringExtra("emp_name");
        binding.tvName.setText(name);

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

        db.collection("Employees").document(UID).get().addOnCompleteListener(this,task -> {

            binding.pbLoading.setVisibility(View.GONE);

            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {


                    designation = document.getString("designation");

                    department = document.getString("department");


                    emailId = document.getString("email");

                    binding.tvDesignation.setText(designation);


                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.viewEmployeeContainer, ViewEmployeeFragment.setArguments(UID))
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {


                binding.pBarUpload.setVisibility(View.VISIBLE);


                UploadTask uploadTask = storageRef.putFile(result.getUri());
                uploadTask.addOnFailureListener(exception -> {

                    binding.pBarUpload.setVisibility(View.GONE);

                }).addOnSuccessListener(taskSnapshot -> {
                    binding.pBarUpload.setVisibility(View.GONE);
                    binding.ivProfile.setImageURI(result.getUri());
                });


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();

                Log.v("error", error.getMessage());

            }

        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.view_employee_fragment_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int menuId = item.getItemId();

        if (menuId == android.R.id.home) {

            finish();

        } else if (menuId == R.id.menuProfilePic) {

            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setActivityTitle("Profile Pic")
                    .setCropShape(CropImageView.CropShape.RECTANGLE)
                    .setFixAspectRatio(true)
                    .setRequestedSize(600, 600)
                    .setAspectRatio(1, 1)
                    .start(this);


        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onViewEmployeeFragmentInteraction(String title) {

    }

    @Override
    public void onViewEmployeeFragmentInteractionEditInfo() {

    }


}
