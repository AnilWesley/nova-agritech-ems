package com.agritech.empmanager.fragments;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.agritech.empmanager.R;
import com.agritech.empmanager.databinding.FragmentEmpEducationBinding;
import com.agritech.empmanager.fastpojo.FastEmpEducation;
import com.agritech.empmanager.fastpojo.FastEmpWork;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter.listeners.OnClickListener;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;


public class EmpWorkFragment extends Fragment {

    private OnEmpEducationFragmentInteractionListener mListener;

    FragmentEmpEducationBinding binding;


    FastAdapter fastAdapter;
    ItemAdapter itemAdapter;
    FirebaseFirestore db;

    String empId;

    CollectionReference cf;


    public EmpWorkFragment() {
        // Required empty public constructor
    }


    public static EmpWorkFragment setArguments(String empId) {

        EmpWorkFragment fragment = new EmpWorkFragment();
        Bundle bundle = new Bundle();
        bundle.putString("emp_id", empId);

        fragment.setArguments(bundle);

        return fragment;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        empId = getArguments().getString("emp_id");
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_emp_education, container, false);


        Log.v("emp_id",empId);

        cf = FirebaseFirestore.getInstance().collection("Employees/" + empId + "/work");
        binding.rvEmpEducation.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvEmpEducation.setHasFixedSize(true);

        itemAdapter = new ItemAdapter();


        fastAdapter = FastAdapter.with(itemAdapter);

        fastAdapter.setHasStableIds(true);

        binding.rvEmpEducation.setItemAnimator(new DefaultItemAnimator());

        binding.rvEmpEducation.setAdapter(fastAdapter);
        binding.rvEmpEducation.setEmptyView(binding.tvEmptyView);


        binding.tvEmptyView.setVisibility(View.GONE);


        cf.orderBy("year", Query.Direction.DESCENDING)
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) {
                        //Log.w(TAG, "listen:error", e);
                        return;
                    }

                    for (DocumentChange dc : snapshots.getDocumentChanges()) {
                        if (dc.getType() == DocumentChange.Type.ADDED) {



                            FastEmpWork employee = dc.getDocument().toObject(FastEmpWork.class);

                            itemAdapter.add(employee);


                        }
                    }

                });


        fastAdapter.withSelectable(true);
        fastAdapter.withOnClickListener((OnClickListener<FastEmpEducation>) (v, adapter, item, position) -> {

            showDialog("Add Holiday", 2, item, position);

            return true;
        });


        return binding.getRoot();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnEmpEducationFragmentInteractionListener) {
            mListener = (OnEmpEducationFragmentInteractionListener) context;
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
        inflater.inflate(R.menu.fragment_holidays_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menuAdd) {

            showDialog("Add Holiday", 1, null, 0);
        }

        return super.onOptionsItemSelected(item);
    }


    public interface OnEmpEducationFragmentInteractionListener {
        void onEmpEducationFragmentInteraction(Uri uri);
    }


    public void showDialog(String title, int type, FastEmpEducation empEducation, int position) {

        final Dialog dialog = new Dialog(getActivity());


        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_education);

        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);


        TextView tvTitle = dialog.findViewById(R.id.tvTitle);
        tvTitle.setText(title);

        EditText etUniversity = dialog.findViewById(R.id.etUniversity);
        EditText etInstitution = dialog.findViewById(R.id.etInstitution);
        EditText etDegree = dialog.findViewById(R.id.etDegree);
        EditText etYear = dialog.findViewById(R.id.etYear);
        EditText etInterests = dialog.findViewById(R.id.etInterests);
        EditText etRemarks = dialog.findViewById(R.id.etRemarks);

        AppCompatImageView aivDelete = dialog.findViewById(R.id.aivDelete);

        if (type == 1) {
            aivDelete.setVisibility(View.INVISIBLE);
        }

        aivDelete.setOnClickListener(v -> {

            cf.document(empEducation.uuid).delete();
            dialog.dismiss();

            itemAdapter.remove(position);

        });

        if (type == 2) {

            etUniversity.setText(empEducation.university);
            etInstitution.setText(empEducation.institution);
            etDegree.setText(empEducation.degree);
            etYear.setText(empEducation.year+"");
            etInterests.setText(empEducation.interests);
            etRemarks.setText(empEducation.remarks);
        }

        Button dialogButton = dialog.findViewById(R.id.acbSave);
        dialogButton.setOnClickListener(v -> {

            if (etUniversity.getText().toString().isEmpty()) {
                etUniversity.setError("University should't be empty");
                return;
            }

            if (etInstitution.getText().toString().isEmpty()) {
                etInstitution.setError("Institution should't be empty");
                return;
            }

            if (etDegree.getText().toString().isEmpty()) {
                etDegree.setError("Degree should't be empty");
                return;
            }

            if (etYear.getText().toString().isEmpty()) {
                etYear.setError("Year should't be empty");
                return;
            }

            if (etInterests.getText().toString().isEmpty()) {
                etInterests.setError("Interests should't be empty");
                return;
            }

            if (etRemarks.getText().toString().isEmpty()) {
                etRemarks.setError("Remarks should't be empty");
                return;
            }

            String uuid = cf.document().getId();

            Map<String, Object> map = new HashMap<>();
            map.put("university", etUniversity.getText().toString());
            map.put("institution", etInstitution.getText().toString());
            map.put("degree", etDegree.getText().toString());
            map.put("year", Integer.parseInt(etYear.getText().toString()));
            map.put("interests", etInterests.getText().toString());
            map.put("remarks", etRemarks.getText().toString());
            map.put("uuid", uuid);

            if (type == 1) {
                cf.document(uuid).set(map);
            } else {
                itemAdapter.remove(position);
                cf.document(empEducation.uuid).update(map);
            }


            dialog.dismiss();



        });



        dialog.show();

    }


}
