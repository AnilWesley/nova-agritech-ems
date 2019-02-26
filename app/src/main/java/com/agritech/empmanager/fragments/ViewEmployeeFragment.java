package com.agritech.empmanager.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.agritech.empmanager.EditEmployee;
import com.agritech.empmanager.R;
import com.agritech.empmanager.databinding.FragmentViewEmployeeBinding;
import com.agritech.empmanager.pojo.Emp;
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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.transition.Slide;

import static android.app.Activity.RESULT_OK;


public class ViewEmployeeFragment extends Fragment {

    private OnViewEmployeeFragmentInteractionListener mListener;

    FragmentViewEmployeeBinding binding;
    AppCompatActivity activity;
    FirebaseFirestore db;

    Emp emp;


    public ViewEmployeeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        emp = getArguments().getParcelable("emp");

        super.onCreate(savedInstanceState);
    }

    public static ViewEmployeeFragment setArguments(Emp emp) {

        ViewEmployeeFragment fragment = new ViewEmployeeFragment();
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_view_employee, container, false);

        binding.setEmp(emp);

        setEditable();

        return binding.getRoot();
    }

    private void setEditable() {

        binding.aivEditBasicInfo.setOnClickListener(v -> {

            if (mListener != null) {
                mListener.onViewEmployeeFragmentInteractionEditInfo(EditEmployee.EDIT_BASIC_INFO);
            }

        });

        binding.aivEditWorkInfo.setOnClickListener(v -> {

            if (mListener != null) {
                mListener.onViewEmployeeFragmentInteractionEditInfo(EditEmployee.EDIT_WORK_INFO);
            }

        });

        binding.aivEditLeaveInfo.setOnClickListener(v -> {

            if (mListener != null) {
                mListener.onViewEmployeeFragmentInteractionEditInfo(EditEmployee.EDIT_LEAVE_INFO);
            }

        });

        binding.aivEditPersonalInfo.setOnClickListener(v -> {

            if (mListener != null) {
                mListener.onViewEmployeeFragmentInteractionEditInfo(EditEmployee.EDIT_PERSONAL_INFO);
            }

        });


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


    public interface OnViewEmployeeFragmentInteractionListener {
        // TODO: Update argument type and name
        void onViewEmployeeFragmentInteraction(String title);

        void onViewEmployeeFragmentInteractionEditInfo(String type);
    }
}
