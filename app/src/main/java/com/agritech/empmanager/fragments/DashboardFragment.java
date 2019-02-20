package com.agritech.empmanager.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.agritech.empmanager.AddEmployeeActivity;
import com.agritech.empmanager.LoginActivity;
import com.agritech.empmanager.R;
import com.agritech.empmanager.databinding.FragmentDashbordBinding;
import com.agritech.empmanager.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

public class DashboardFragment extends Fragment {

    private OnDashboardFragmentInteractionListener mListener;

    FragmentDashbordBinding binding;


    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashbord, container, false);

        //for create home button
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(binding.toolbar);

        activity.setTitle("Dash board");

        return binding.getRoot();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDashboardFragmentInteractionListener) {
            mListener = (OnDashboardFragmentInteractionListener) context;
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        if (Constants.type.equals("1"))
            inflater.inflate(R.menu.hr_dashboard_menu, menu);

        else
            inflater.inflate(R.menu.dashboard_menu, menu);


        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int menuId = item.getItemId();

        if (menuId == R.id.menuAddEmployee) {

            if (mListener != null)
                mListener.onDashboardFragmentInteractionAddEmployee();

        } else if (menuId == R.id.menuViewEmployees){

            if (mListener != null)
                mListener.onDashboardFragmentInteractionViewEmployees();


        } else if (menuId == R.id.menuLogout){

            if (mListener != null)
                mListener.onDashboardFragmentInteractionLogout();


        }

        return super.onOptionsItemSelected(item);
    }

    public interface OnDashboardFragmentInteractionListener {
        // TODO: Update argument type and name
        void onDashboardFragmentInteraction(Uri uri);
        void onDashboardFragmentInteractionViewEmployees();
        void onDashboardFragmentInteractionAddEmployee();
        void onDashboardFragmentInteractionLogout();
    }
}
