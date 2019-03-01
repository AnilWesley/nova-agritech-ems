package com.agritech.empmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;

import com.agritech.empmanager.databinding.ActivityNewLeaveBinding;
import com.agritech.empmanager.fastpojo.FastEmployee;
import com.agritech.empmanager.fragments.EmpEditPersonalInfoFragment;
import com.agritech.empmanager.utils.PrefUtilities;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.Document;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class NewLeaveActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    public static void start(Context context) {
        context.startActivity(new Intent(context, NewLeaveActivity.class));
    }

    ActivityNewLeaveBinding binding;
    FirebaseFirestore db;

    boolean isItFromDate = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_leave);

        db = FirebaseFirestore.getInstance();

        setSupportActionBar(binding.toolbar);

        setTitle("New Leaves");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ArrayAdapter arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.leaveType));
        binding.etLeaveType.setAdapter(arrayAdapter);
        binding.etLeaveType.setInputType(0);

        binding.etLeaveType.setOnClickListener(v -> binding.etLeaveType.showDropDown());


        binding.etLeaveType.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                binding.tilLeaveType.setHelperText("");

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                db.collection("leaves")
                        .whereEqualTo("year", String.valueOf(Calendar.getInstance().get(Calendar.YEAR)))
                        .whereEqualTo("leaveType", s.toString())
                        .whereEqualTo("status", "Accepted")
                        .get()
                        .addOnCompleteListener(task -> {

                            if (task.isSuccessful()) {

                                int size = task.getResult().size();

                                binding.tilLeaveType.setHelperText("Used " + s.toString() + ": " + size);


                            } else {

                                Log.v("errorhiiii", task.getException().getMessage());

                            }

                        });

            }
        });


        binding.etFromDate.setOnClickListener(v -> {

            Calendar calendar = Calendar.getInstance();

            new DatePickerDialog(
                    NewLeaveActivity.this,
                    NewLeaveActivity.this,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            ).show();

            isItFromDate = true;


        });


        binding.etToDate.setOnClickListener(v -> {

            Calendar calendar = Calendar.getInstance();

            new DatePickerDialog(
                    NewLeaveActivity.this,
                    NewLeaveActivity.this,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            ).show();

            isItFromDate = false;

        });


    }

    public void applyForLeave(View view) {


        String leaveType = binding.etLeaveType.getText().toString();


        String fromDate = binding.etFromDate.getText().toString();
        String toDate = binding.etToDate.getText().toString();


        String description = binding.etDescription.getText().toString();

        String subject = binding.etSubject.getText().toString();

        if (subject.isEmpty()) {
            Snackbar.make(binding.getRoot(), "Subject is empty", Snackbar.LENGTH_LONG).show();
            return;
        }

        if (leaveType.isEmpty()) {
            Snackbar.make(binding.getRoot(), "Leave type is empty", Snackbar.LENGTH_LONG).show();
            return;
        }

        if (fromDate.isEmpty()) {
            Snackbar.make(binding.getRoot(), "From date is empty", Snackbar.LENGTH_LONG).show();
            return;
        }

        if (toDate.isEmpty()) {
            Snackbar.make(binding.getRoot(), "To date is empty", Snackbar.LENGTH_LONG).show();
            return;
        }

        if (description.isEmpty()) {
            Snackbar.make(binding.getRoot(), "Description is empty", Snackbar.LENGTH_LONG).show();
            return;
        }


        Map<String, Object> leaveMap = new HashMap<>();


        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

        try {


            leaveMap.put("leaveType", leaveType);

            leaveMap.put("appliedOn", sdf.format(calendar.getTime()));

            leaveMap.put("year", String.valueOf(calendar.get(Calendar.YEAR)));

            leaveMap.put("fromDate", sdf.parse(fromDate).getTime());

            leaveMap.put("toDate", sdf.parse(toDate).getTime());

            leaveMap.put("subject", subject);

            leaveMap.put("description", description);

            leaveMap.put("status", "Applied");

            leaveMap.put("appliedByUID", PrefUtilities.with(this).getUserId());
            leaveMap.put("appliedByName", PrefUtilities.with(this).getName());


            DocumentReference document =  db.collection("leaves").document();

            leaveMap.put("uuid",document.getId());

            document.set(leaveMap);


        } catch (ParseException e) {
            e.printStackTrace();
        }


        finish();


    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        if (isItFromDate){
            binding.etFromDate.setText(dayOfMonth+"/"+(month+1)+"/"+year);

        }else {
            binding.etToDate.setText(dayOfMonth+"/"+(month+1)+"/"+year);

        }

    }
}
