package com.agritech.empmanager.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.agritech.empmanager.MyProfileActivity;
import com.agritech.empmanager.R;
import com.agritech.empmanager.ViewEmployeeActivity;
import com.agritech.empmanager.databinding.FragmentEmployeesBinding;
import com.agritech.empmanager.fastpojo.FastEmployee;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter.listeners.OnClickListener;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

public class EmployeesFragment extends Fragment implements SearchView.OnQueryTextListener {

    private OnEmployeesFragmentInteractionListener mListener;

    FragmentEmployeesBinding binding;

    FastAdapter fastAdapter;
    ItemAdapter itemAdapter;
    FirebaseFirestore db;

    StorageReference storageReference;

    public EmployeesFragment() {
        // Required empty public constructor
    }

    public static EmployeesFragment setArguments(String dept, boolean forHR) {

        EmployeesFragment fragment = new EmployeesFragment();

        Bundle bundle = new Bundle();
        bundle.putString("dept", dept);
        bundle.putBoolean("forHR", forHR);

        fragment.setArguments(bundle);


        return fragment;


    }

    String dept;

    Boolean forHR;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dept = getArguments().getString("dept");
        forHR = getArguments().getBoolean("forHR");

        if (forHR)
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


        db.collection("Employees").whereEqualTo("department", dept)
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

            if (forHR)
                ViewEmployeeActivity.start(getActivity(), item, v.findViewById(R.id.ivProfile));
            else
                MyProfileActivity.start(getActivity(), item.uid,item.fName+" "+item.lName);

            return true;

        });


        return binding.getRoot();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnEmployeesFragmentInteractionListener) {
            mListener = (OnEmployeesFragmentInteractionListener) context;
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

        inflater.inflate(R.menu.menu_employee_fragment, menu);


        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        SearchView searchView = (SearchView) searchItem.getActionView();


        searchView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        searchView.setOnQueryTextListener(this);

        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        itemAdapter.filter(newText);

        itemAdapter.getItemFilter().withFilterPredicate((item, constraint) -> {

            FastEmployee employee = (FastEmployee) item;


            return employee.fName.toLowerCase().contains(String.valueOf(constraint).toLowerCase())
                    || employee.lName.toLowerCase().contains(String.valueOf(constraint).toLowerCase())
                    || employee.department.toLowerCase().contains(String.valueOf(constraint).toLowerCase())
                    || employee.designation.toLowerCase().contains(String.valueOf(constraint).toLowerCase());
        });

        return true;
    }

    public interface OnEmployeesFragmentInteractionListener {
        // TODO: Update argument type and name
        void onEmployeesFragmentInteraction();
    }
}
