package com.agritech.empmanager.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
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

import com.agritech.empmanager.EmployeesActivity;
import com.agritech.empmanager.R;
import com.agritech.empmanager.databinding.FragmentTeamsBinding;
import com.agritech.empmanager.fastpojo.FastDept;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter.listeners.OnClickListener;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;


public class TeamsFragment extends Fragment {

    private OnTeamsFragmentInteractionListener mListener;

    FragmentTeamsBinding binding;


    FastAdapter fastAdapter;
    ItemAdapter itemAdapter;
    FirebaseFirestore db;


    public TeamsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_teams, container, false);


        db = FirebaseFirestore.getInstance();
        binding.rvTeams.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvTeams.setHasFixedSize(true);

        itemAdapter = new ItemAdapter();


        fastAdapter = FastAdapter.with(itemAdapter);

        fastAdapter.setHasStableIds(true);

        binding.rvTeams.setItemAnimator(new DefaultItemAnimator());

        binding.rvTeams.setAdapter(fastAdapter);
        binding.rvTeams.setEmptyView(binding.tvEmptyView);


        binding.tvEmptyView.setVisibility(View.GONE);


        db.collection("teams").orderBy("name")
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
            EmployeesActivity.start(getActivity(), item.name,EmployeesActivity.INTENT_ACTION_TEAM);
            return true;
        });


        return binding.getRoot();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.fragment_teams_menu, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int menuId = item.getItemId();

        if (menuId == R.id.menuAdd) {

            showDialog();

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnTeamsFragmentInteractionListener) {
            mListener = (OnTeamsFragmentInteractionListener) context;
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


    public void showDialog() {
        final Dialog dialog = new Dialog(getActivity());


        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setCancelable(false);
        dialog.setContentView(R.layout.create_team_dialog);

        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);


        EditText etName = dialog.findViewById(R.id.etName);

        Button dialogButton = dialog.findViewById(R.id.acbSaveTeam);
        dialogButton.setOnClickListener(v -> {

            Map<String,String> map = new HashMap<>();

            map.put("name",etName.getText().toString());

            db.collection("teams").add(map).addOnCompleteListener(task -> {

                dialog.dismiss();

            }).addOnFailureListener(e -> {



            });

        });

        dialog.show();

    }


    public interface OnTeamsFragmentInteractionListener {
        void onTeamsFragmentInteraction(Uri uri);
    }
}
