package com.agritech.empmanager.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.agritech.empmanager.R;
import com.agritech.empmanager.databinding.FragmentProfileBinding;
import com.agritech.empmanager.databinding.FragmentViewEmployeeBinding;
import com.agritech.empmanager.textdrawable.ColorGenerator;
import com.agritech.empmanager.textdrawable.TextDrawable;
import com.agritech.empmanager.utils.PrefUtilities;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;


public class ViewEmployeeFragment extends Fragment {

    private OnViewEmployeeFragmentInteractionListener mListener;

    FragmentViewEmployeeBinding binding;
    AppCompatActivity activity;
    FirebaseFirestore db;

    String name = "Profile", designation, emailId,department;


    public ViewEmployeeFragment() {
        // Required empty public constructor
    }

    public static ViewEmployeeFragment setArguments(String emp_uid) {

        ViewEmployeeFragment fragment = new ViewEmployeeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("emp_uid",emp_uid);
        fragment.setArguments(bundle);

        return fragment;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_view_employee, container, false);

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

            db.collection("Employees").document(getArguments().getString("emp_uid")).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {




                        name = document.getString("fName")+" "+document.getString("lName");
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
                        //GlideApp.with(binding.ivProfile).load(drawable).into(binding.ivProfile);

                        binding.ivProfile.setImageDrawable(drawable);


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
    public boolean onOptionsItemSelected(MenuItem item) {

        int menuId = item.getItemId();

        if (menuId == android.R.id.home) {

            activity.finish();

        }

        return super.onOptionsItemSelected(item);
    }



    public interface OnViewEmployeeFragmentInteractionListener {
        // TODO: Update argument type and name
        void onViewEmployeeFragmentInteraction(String title);
    }
}
