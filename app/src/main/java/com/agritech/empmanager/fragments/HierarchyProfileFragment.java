package com.agritech.empmanager.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.transition.Slide;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.agritech.empmanager.R;
import com.agritech.empmanager.databinding.FragmentHierarchyProfileBinding;
import com.agritech.empmanager.pojo.Emp;
import com.agritech.empmanager.utils.GlideApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class HierarchyProfileFragment extends Fragment {

    FragmentHierarchyProfileBinding binding;

    private OnFragmentInteractionListener mListener;

    StorageReference hRef,eRef;


    public HierarchyProfileFragment() {
        // Required empty public constructor
    }


    public static HierarchyProfileFragment setArguments(Emp emp) {

        HierarchyProfileFragment fragment = new HierarchyProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("emp", emp);
        fragment.setArguments(bundle);

        fragment.setEnterTransition(new Slide(Gravity.BOTTOM));
        fragment.setExitTransition(new Slide(Gravity.TOP));

        return fragment;

    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_hierarchy_profile, container, false);

        hRef = FirebaseStorage.getInstance().getReference().child("hr_icons");
        eRef = FirebaseStorage.getInstance().getReference().child("emp_icons");


        GlideApp.with(binding.ivProfile).load(eRef.child("profile.png")).into(binding.ivProfile);
        GlideApp.with(binding.ivMyAsserts).load(eRef.child("asserts.png")).into(binding.ivMyAsserts);
        GlideApp.with(binding.ivMyTeam).load(eRef.child("team.png")).into(binding.ivMyTeam);

        GlideApp.with(binding.ivMyLeaves).load(eRef.child("leave.png")).into(binding.ivMyLeaves);
        GlideApp.with(binding.ivMyHollidays).load(eRef.child("holiday.png")).into(binding.ivMyHollidays);
        GlideApp.with(binding.ivAllowances).load(eRef.child("bill.png")).into(binding.ivAllowances);

        GlideApp.with(binding.ivHierarchy).load(eRef.child("hierarchy.png")).into(binding.ivHierarchy);
        GlideApp.with(binding.ivAttendence).load(eRef.child("attendence.png")).into(binding.ivAttendence);
        GlideApp.with(binding.ivSales).load(eRef.child("sales.png")).into(binding.ivSales);


        return binding.getRoot();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
