package com.agritech.empmanager.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.agritech.empmanager.R;
import com.agritech.empmanager.ViewEmployeeActivity;
import com.agritech.empmanager.databinding.FragmentEmployeesBinding;
import com.agritech.empmanager.databinding.FragmentSelectReportingToBinding;
import com.agritech.empmanager.fastpojo.FastEmployee;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter.listeners.OnClickListener;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;


public class SelectReportingToFragment extends BottomSheetDialogFragment {

    private OnSelectReportingToFragmentInteractionListener mListener;


    FragmentSelectReportingToBinding binding;

    FastAdapter fastAdapter;
    ItemAdapter itemAdapter;
    FirebaseFirestore db;

    StorageReference storageReference;


    public SelectReportingToFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_select_reporting_to, container, false);


        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference().child("profilePic/");
        binding.rvEmployees.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvEmployees.setHasFixedSize(true);

        itemAdapter = new ItemAdapter();


        fastAdapter = FastAdapter.with(itemAdapter);

        fastAdapter.setHasStableIds(true);

        binding.rvEmployees.setItemAnimator(new DefaultItemAnimator());

        binding.rvEmployees.setAdapter(fastAdapter);
        binding.rvEmployees.setEmptyView(binding.tvEmptyView);


        binding.tvEmptyView.setVisibility(View.GONE);


        db.collection("Employees").orderBy("fName")
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) {
                        Log.w("hiiii", "listen:error", e);
                        return;
                    }

                    for (DocumentChange dc : snapshots.getDocumentChanges()) {
                        if (dc.getType() == DocumentChange.Type.ADDED) {

                            FastEmployee employee = dc.getDocument().toObject(FastEmployee.class);
                            employee.sRef = storageReference.child(employee.uid + ".jpg");

                            itemAdapter.add(employee);


                        }
                    }

                });


        fastAdapter.withSelectable(true);
        fastAdapter.withOnClickListener((OnClickListener<FastEmployee>) (v, adapter, item, position) -> {

            if (mListener != null)
                mListener.onSelectReportingToFragmentInteraction(item.uid, item.fName + " " + item.lName);

            return true;
        });


        return binding.getRoot();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSelectReportingToFragmentInteractionListener) {
            mListener = (OnSelectReportingToFragmentInteractionListener) context;
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


    public interface OnSelectReportingToFragmentInteractionListener {
        // TODO: Update argument type and name
        void onSelectReportingToFragmentInteraction(String uid, String name);
    }
}
