package com.agritech.empmanager.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.agritech.empmanager.R;
import com.agritech.empmanager.databinding.FragmentHomeNewBinding;
import com.agritech.empmanager.pojo.Emp;
import com.agritech.empmanager.utils.GlideApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.transition.Slide;


public class HomeNewFragment extends Fragment {

    private OnHomeNewFragmentInteractionListener mListener;

    FragmentHomeNewBinding binding;

    StorageReference hRef,eRef;


    public HomeNewFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_new, container, false);

        hRef = FirebaseStorage.getInstance().getReference().child("hr_icons");
        eRef = FirebaseStorage.getInstance().getReference().child("emp_icons");


        GlideApp.with(binding.menuAddNewEmp).load(hRef.child("new_emp.png")).into(binding.menuAddNewEmp);
        GlideApp.with(binding.menuDept).load(hRef.child("department.png")).into(binding.menuDept);
        GlideApp.with(binding.menuTeam).load(hRef.child("team.png")).into(binding.menuTeam);

        GlideApp.with(binding.ivAssets).load(hRef.child("asserts.png")).into(binding.ivAssets);
        GlideApp.with(binding.ivHolidays).load(hRef.child("holiday.png")).into(binding.ivHolidays);
        GlideApp.with(binding.ivLeaves).load(hRef.child("leave.png")).into(binding.ivLeaves);


        /*Emp icons*/


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


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnHomeNewFragmentInteractionListener) {
            mListener = (OnHomeNewFragmentInteractionListener) context;
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


    public interface OnHomeNewFragmentInteractionListener {
        // TODO: Update argument type and name
        void onHomeNewFragmentInteraction(String title);

        void onHomeNewFragmentInteractionEditInfo(String type);
    }
}
