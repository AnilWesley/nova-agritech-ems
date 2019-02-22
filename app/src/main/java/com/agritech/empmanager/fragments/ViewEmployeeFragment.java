package com.agritech.empmanager.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.agritech.empmanager.R;
import com.agritech.empmanager.databinding.FragmentProfileBinding;
import com.agritech.empmanager.databinding.FragmentViewEmployeeBinding;
import com.agritech.empmanager.textdrawable.ColorGenerator;
import com.agritech.empmanager.textdrawable.TextDrawable;
import com.agritech.empmanager.utils.GlideApp;
import com.agritech.empmanager.utils.PrefUtilities;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.CompositePermissionListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.karumi.dexter.listener.single.SnackbarOnDeniedPermissionListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import static android.app.Activity.RESULT_OK;


public class ViewEmployeeFragment extends Fragment {

    private OnViewEmployeeFragmentInteractionListener mListener;

    FragmentViewEmployeeBinding binding;
    AppCompatActivity activity;
    FirebaseFirestore db;

    StorageReference storageRef;

    String name = "Profile", designation, emailId, department;


    public ViewEmployeeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    public static ViewEmployeeFragment setArguments(String emp_uid) {

        ViewEmployeeFragment fragment = new ViewEmployeeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("emp_uid", emp_uid);
        fragment.setArguments(bundle);

        return fragment;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_view_employee, container, false);

        String UID = getArguments().getString("emp_uid");


        storageRef = FirebaseStorage.getInstance().getReference().child("profilePic/").child(UID + ".jpg");

        //for create home button
        activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(binding.toolbar);
        db = FirebaseFirestore.getInstance();

        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);


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


        if (mListener != null) {
            //String uid = PrefUtilities.with(getActivity()).getUserId();

            db.collection("Employees").document(UID).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {


                        name = document.getString("fName") + " " + document.getString("lName");
                        designation = document.getString("designation");

                        department = document.getString("department");


                        emailId = document.getString("email");

                        binding.tvName.setText(name);
                        binding.tvDesignation.setText(designation);
                        binding.tvEmailId.setText(emailId);

                        binding.tvDepartmentName.setText(department);


                        TextDrawable drawable = TextDrawable.builder()
                                .beginConfig()
                                .height(200)
                                .width(200)
                                .endConfig()
                                .buildRect(name.charAt(0) + "", ColorGenerator.MATERIAL.getColor(name));

                        GlideApp.with(binding.ivProfile).load(storageRef).placeholder(drawable).into(binding.ivProfile);


                        //Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        //Log.d(TAG, "No such document");
                    }
                } else {
                    //Log.d(TAG, "get failed with ", task.getException());
                }
            });

        }


        return binding.getRoot();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnViewEmployeeFragmentInteractionListener) {
            mListener = (OnViewEmployeeFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                //imageUri = result.getUri();

                //changeProfilePic(result.getUri());

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

    private void changeProfilePic(Uri uri) {


    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.view_employee_fragment_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int menuId = item.getItemId();

        if (menuId == android.R.id.home) {

            activity.finish();

        } else if (menuId == R.id.menuProfilePic) {

            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setActivityTitle("Profile Pic")
                    .setCropShape(CropImageView.CropShape.RECTANGLE)
                    .setFixAspectRatio(true)
                    .setRequestedSize(600, 600)
                    .setAspectRatio(1, 1)
                    .start(getActivity(), ViewEmployeeFragment.this);


        }

        return super.onOptionsItemSelected(item);
    }


    public interface OnViewEmployeeFragmentInteractionListener {
        // TODO: Update argument type and name
        void onViewEmployeeFragmentInteraction(String title);
    }
}
