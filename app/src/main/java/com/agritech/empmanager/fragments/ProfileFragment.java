package com.agritech.empmanager.fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.agritech.empmanager.LoginActivity;
import com.agritech.empmanager.R;
import com.agritech.empmanager.databinding.FragmentCalendarBinding;
import com.agritech.empmanager.databinding.FragmentProfileBinding;
import com.agritech.empmanager.mcalendar.CompactCalendarView;
import com.agritech.empmanager.mcalendar.domain.Event;
import com.agritech.empmanager.textdrawable.ColorGenerator;
import com.agritech.empmanager.textdrawable.TextDrawable;
import com.agritech.empmanager.utils.GlideApp;
import com.agritech.empmanager.utils.PrefUtilities;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;


public class ProfileFragment extends Fragment {

    private OnProfileFragmentInteractionListener mListener;

    FragmentProfileBinding binding;
    AppCompatActivity activity;
    FirebaseFirestore db;

    String name = "Profile", designation, emailId;

    StorageReference storageRef;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);

        //for create home button
        activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(binding.toolbar);
        db = FirebaseFirestore.getInstance();


        storageRef = FirebaseStorage.getInstance().getReference().child("profilePic/").child(PrefUtilities.with(getActivity()).getUserId() + ".jpg");

        Drawable drawable = TextDrawable.builder()
                .beginConfig()
                .height(200)
                .width(200)
                .endConfig()
                .buildRect(name.charAt(0) + "", ColorGenerator.MATERIAL.getColor(name));

        GlideApp.with(binding.ivProfile).load(storageRef).placeholder(drawable).into(binding.ivProfile);


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
            String uid = PrefUtilities.with(getActivity()).getUserId();


            db.collection("Employees").document(uid).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        name = document.getString("name");
                        designation = document.getString("designation");
                        emailId = document.getString("email");

                        binding.tvName.setText(name);
                        binding.tvDesignation.setText(designation);
                        binding.tvEmailId.setText(emailId);

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
        if (context instanceof OnProfileFragmentInteractionListener) {
            mListener = (OnProfileFragmentInteractionListener) context;
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

    public interface OnProfileFragmentInteractionListener {
        // TODO: Update argument type and name
        void onProfileFragmentInteraction(String title);
    }
}
