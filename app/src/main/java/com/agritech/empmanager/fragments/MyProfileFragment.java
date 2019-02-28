package com.agritech.empmanager.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.agritech.empmanager.EditEmployee;
import com.agritech.empmanager.R;
import com.agritech.empmanager.databinding.FragmentMyProfileBinding;
import com.agritech.empmanager.databinding.FragmentViewEmployeeBinding;
import com.agritech.empmanager.pojo.Emp;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.transition.Slide;


public class MyProfileFragment extends Fragment {

    private OnViewEmployeeFragmentInteractionListener mListener;

    FragmentMyProfileBinding binding;
    FirebaseFirestore db;

    Emp emp;


    public MyProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        emp = getArguments().getParcelable("emp");

        super.onCreate(savedInstanceState);
    }

    public static MyProfileFragment setArguments(Emp emp) {

        MyProfileFragment fragment = new MyProfileFragment();
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_profile, container, false);

        binding.setEmp(emp);

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


    public interface OnViewEmployeeFragmentInteractionListener {
        // TODO: Update argument type and name
        void onViewEmployeeFragmentInteraction(String title);

        void onViewEmployeeFragmentInteractionEditInfo(String type);
    }
}
