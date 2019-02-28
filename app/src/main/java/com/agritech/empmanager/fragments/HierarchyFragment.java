package com.agritech.empmanager.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.agritech.empmanager.HierarchyProfileActivity;
import com.agritech.empmanager.MyProfileActivity;
import com.agritech.empmanager.R;
import com.agritech.empmanager.ViewEmployeeActivity;
import com.agritech.empmanager.databinding.FragmentHierarchyBinding;
import com.agritech.empmanager.fastpojo.FastEmployee;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter.listeners.OnClickListener;


public class HierarchyFragment extends Fragment {

    private OnHierarchyFragmentInteractionListener mListener;


    FragmentHierarchyBinding binding;

    FastAdapter fastAdapter;
    ItemAdapter itemAdapter;
    FirebaseFirestore db;

    StorageReference storageReference;

    public HierarchyFragment() {
        // Required empty public constructor
    }


    String UID;

    public static HierarchyFragment setArguments(String uid) {

        HierarchyFragment fragment = new HierarchyFragment();
        Bundle bundle = new Bundle();
        bundle.putString("uid", uid);

        fragment.setArguments(bundle);


        return fragment;

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        UID = getArguments().getString("uid");

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_hierarchy, container, false);

        storageReference = FirebaseStorage.getInstance().getReference().child("profilePic/");


        db = FirebaseFirestore.getInstance();
        binding.rvHNumbers.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvHNumbers.setHasFixedSize(true);

        itemAdapter = new ItemAdapter();


        fastAdapter = FastAdapter.with(itemAdapter);

        fastAdapter.setHasStableIds(true);

        binding.rvHNumbers.setItemAnimator(new DefaultItemAnimator());

        binding.rvHNumbers.setAdapter(fastAdapter);
        binding.rvHNumbers.setEmptyView(binding.tvEmptyView);


        binding.tvEmptyView.setVisibility(View.GONE);

        Log.v("hiiii", UID);


       /* db.collection("Employees").whereEqualTo("reportingToUID", UID)
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) {
                        //Log.w(TAG, "listen:error", e);
                        return;
                    }

                    for (DocumentChange dc : snapshots.getDocumentChanges()) {
                        if (dc.getType() == DocumentChange.Type.ADDED) {

                            FastEmployee employee = dc.getDocument().toObject(FastEmployee.class);
                            employee.sRef = storageReference.child(employee.uid + ".jpg");

                            itemAdapter.add(employee);


                        }
                    }

                });*/


        db.collection("Employees").whereEqualTo("reportingToUID", UID)
                .orderBy("fName")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot dc : task.getResult()) {

                            FastEmployee employee = dc.toObject(FastEmployee.class);
                            employee.sRef = storageReference.child(employee.uid + ".jpg");

                            Log.v("hiiii", employee.fName);

                            itemAdapter.add(employee);

                        }
                    } else {

                        Log.v("errorhiiii", task.getException().getMessage());

                    }
                });


        fastAdapter.withSelectable(true);
        fastAdapter.withOnClickListener((OnClickListener<FastEmployee>) (v, adapter, item, position) -> {

            //ViewEmployeeActivity.start(getActivity(), item, v.findViewById(R.id.ivProfile));

            HierarchyProfileActivity.start(getActivity(), item.uid, item.fName + " " + item.lName);

            return true;
        });

        return binding.getRoot();

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnHierarchyFragmentInteractionListener) {
            mListener = (OnHierarchyFragmentInteractionListener) context;
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


    public interface OnHierarchyFragmentInteractionListener {
        // TODO: Update argument type and name
        void onHierarchyFragmentInteraction();
    }
}
