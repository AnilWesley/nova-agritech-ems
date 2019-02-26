package com.agritech.empmanager.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.agritech.empmanager.R;
import com.agritech.empmanager.ViewEmployeeActivity;
import com.agritech.empmanager.databinding.FragmentEmployeesBinding;
import com.agritech.empmanager.fastpojo.FastEmployee;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter.listeners.OnClickListener;
import com.mikepenz.fastadapter.listeners.OnLongClickListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

public class TeamNumbersFragment extends Fragment {

    private OnTeamNumbersFragmentInteractionListener mListener;

    FragmentEmployeesBinding binding;

    FastAdapter fastAdapter;
    ItemAdapter itemAdapter;
    FirebaseFirestore db;

    StorageReference storageReference;

    public TeamNumbersFragment() {
        // Required empty public constructor
    }

    public static TeamNumbersFragment setArguments(String team) {

        TeamNumbersFragment fragment = new TeamNumbersFragment();

        Bundle bundle = new Bundle();
        bundle.putString("team", team);

        fragment.setArguments(bundle);


        return fragment;


    }

    String team;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        team = getArguments().getString("team");

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_employees, container, false);

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


        db.collection("Employees").whereArrayContains("teams", team)
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

                });


        fastAdapter.withSelectable(true);
        fastAdapter.withOnClickListener((OnClickListener<FastEmployee>) (v, adapter, item, position) -> {

            ViewEmployeeActivity.start(getActivity(), item, v.findViewById(R.id.ivProfile));


            return true;
        });


        fastAdapter.withOnLongClickListener((OnLongClickListener<FastEmployee>) (v, adapter, item, position) -> {

            new AlertDialog.Builder(getActivity())
                    .setTitle("Delete from " + team)
                    .setMessage("Do you want to delete " + item.fName + " " + item.lName + " from this team?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        FirebaseFirestore.getInstance()
                                .collection("Employees")
                                .document(item.uid)
                                .update("teams", FieldValue.arrayRemove(team))
                                .addOnCompleteListener(task -> itemAdapter.remove(position))
                                .addOnFailureListener(e -> Toast.makeText(getActivity(), "Please try again", Toast.LENGTH_SHORT).show());
                    })
                    .setNegativeButton("Cancel", null).show();

            return true;
        });


        return binding.getRoot();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnTeamNumbersFragmentInteractionListener) {
            mListener = (OnTeamNumbersFragmentInteractionListener) context;
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

        inflater.inflate(R.menu.fragment_team_numbers_menu, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menuAdd) {

            if (mListener != null)
                mListener.onTeamNumbersFragmentInteraction();

        }


        return super.onOptionsItemSelected(item);
    }


    public interface OnTeamNumbersFragmentInteractionListener {
        // TODO: Update argument type and name
        void onTeamNumbersFragmentInteraction();
    }
}
