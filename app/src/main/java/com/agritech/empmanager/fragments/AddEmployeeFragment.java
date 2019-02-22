package com.agritech.empmanager.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.agritech.empmanager.R;
import com.agritech.empmanager.databinding.FragmentAddEmployeeBinding;
import com.agritech.empmanager.databinding.FragmentAddEmployeeNewBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddEmployeeFragment.OnAddEmployeeFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class AddEmployeeFragment extends Fragment {

    private OnAddEmployeeFragmentInteractionListener mListener;


    FragmentAddEmployeeNewBinding binding;
    FirebaseFirestore db;

    public AddEmployeeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_employee_new, container, false);

        db = FirebaseFirestore.getInstance();

        binding.acbSave.setOnClickListener(v -> {

            //General information

            String firstName = binding.etFirstName.getText().toString();
            String lastName = binding.etLastName.getText().toString();

            String emailId = binding.etEmailId.getText().toString();

            String designation = binding.etDesignation.getText().toString();
            String department = binding.etDepartment.getText().toString();


            //String designation = binding.etDesignation.getText().toString();


            if (firstName.isEmpty()) {
                Snackbar.make(binding.getRoot(), "First Name is empty", Snackbar.LENGTH_LONG).show();
                return;
            }

            if (lastName.isEmpty()) {
                Snackbar.make(binding.getRoot(), "Last Name is empty", Snackbar.LENGTH_LONG).show();
                return;
            }


            if (emailId.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(emailId).matches()) {
                Snackbar.make(binding.getRoot(), "Email id is empty or incorrect", Snackbar.LENGTH_LONG).show();
                return;
            }


            if (designation.isEmpty()) {
                Snackbar.make(binding.getRoot(), "Designation is empty", Snackbar.LENGTH_LONG).show();
                return;
            }

            if (department.isEmpty()) {
                Snackbar.make(binding.getRoot(), "Department is empty", Snackbar.LENGTH_LONG).show();
                return;
            }


            v.setEnabled(false);


            Map<String, Object> user = new HashMap<>();
            user.put("fName", firstName);
            user.put("lName", lastName);
            user.put("email", emailId);
            user.put("designation", designation);
            user.put("department", department);

            String docId = String.valueOf(new Random().nextInt(3) + 1);

            db.collection("metadata/employees/createEmployee").document(docId).set(user)
                    .addOnSuccessListener(documentReference -> {

                        if (mListener != null)
                            mListener.onAddEmployeeFragmentInteraction();

                    })
                    .addOnFailureListener(e -> {

                        v.setEnabled(true);
                        Snackbar.make(binding.getRoot(), "Error, " + e.getMessage(), Snackbar.LENGTH_LONG).show();


                    });

        });



        ArrayAdapter arrayAdapter= new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.eventtypes));
        binding.etDepartment.setAdapter(arrayAdapter);
        binding.etDepartment.setInputType(0);



        binding.etDepartment.setOnClickListener(v -> binding.etDepartment.showDropDown());

     /*   binding.etDepartment.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                binding.etDepartment.showDropDown();
        });
*/

        return binding.getRoot();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAddEmployeeFragmentInteractionListener) {
            mListener = (OnAddEmployeeFragmentInteractionListener) context;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnAddEmployeeFragmentInteractionListener {
        // TODO: Update argument type and name
        void onAddEmployeeFragmentInteraction();
    }
}
