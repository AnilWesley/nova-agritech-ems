package com.agritech.empmanager.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.agritech.empmanager.EmployeesActivity;
import com.agritech.empmanager.R;
import com.agritech.empmanager.databinding.FragmentDepartmentsBinding;
import com.agritech.empmanager.fastpojo.FastDept;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter.listeners.OnClickListener;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;


public class EmpEducationFragment extends Fragment {

    private OnDepartmentsFragmentInteractionListener mListener;

    FragmentDepartmentsBinding binding;


    FastAdapter fastAdapter;
    ItemAdapter itemAdapter;
    FirebaseFirestore db;


    public EmpEducationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_departments, container, false);


        db = FirebaseFirestore.getInstance();
        binding.rvDepartments.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvDepartments.setHasFixedSize(true);

        itemAdapter = new ItemAdapter();


        fastAdapter = FastAdapter.with(itemAdapter);

        fastAdapter.setHasStableIds(true);

        binding.rvDepartments.setItemAnimator(new DefaultItemAnimator());

        binding.rvDepartments.setAdapter(fastAdapter);
        binding.rvDepartments.setEmptyView(binding.tvEmptyView);


        binding.tvEmptyView.setVisibility(View.GONE);


        db.collection("departments").orderBy("name")
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) {
                        //Log.w(TAG, "listen:error", e);
                        return;
                    }

                    for (DocumentChange dc : snapshots.getDocumentChanges()) {
                        if (dc.getType() == DocumentChange.Type.ADDED) {

                            FastDept employee = dc.getDocument().toObject(FastDept.class);

                            itemAdapter.add(employee);


                        }
                    }

                });


        fastAdapter.withSelectable(true);
        fastAdapter.withOnClickListener((OnClickListener<FastDept>) (v, adapter, item, position) -> {
            EmployeesActivity.start(getActivity(), item.name,EmployeesActivity.INTENT_ACTION_DEPT,true);
            return true;
        });


        return binding.getRoot();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDepartmentsFragmentInteractionListener) {
            mListener = (OnDepartmentsFragmentInteractionListener) context;
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


    public interface OnDepartmentsFragmentInteractionListener {
        void onDepartmentsFragmentInteraction(Uri uri);
    }
}
