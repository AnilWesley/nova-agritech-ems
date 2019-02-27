package com.agritech.empmanager.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.agritech.empmanager.R;
import com.agritech.empmanager.databinding.FragmentDepartmentsBinding;
import com.agritech.empmanager.fastpojo.FastHoliday;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter.listeners.OnLongClickListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;


public class HolidaysFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    private OnHolidaysFragmentInteractionListener mListener;

    FragmentDepartmentsBinding binding;


    FastAdapter fastAdapter;
    ItemAdapter itemAdapter;
    FirebaseFirestore db;

    String note = "";
    boolean hasNote = false;

    EditText etDate;

    public HolidaysFragment() {
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


        db.collection("holidays")
                .whereEqualTo("year", 2019)
                .orderBy("time")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot dc : task.getResult()) {

                            //Log.v("errorhiii",""+dc.getString("name"));

                            FastHoliday holiday = dc.toObject(FastHoliday.class);

                            holiday.uuid = dc.getId();

                            itemAdapter.add(holiday);

                        }
                    } else {

                        Log.v("error", task.getException().getMessage());

                    }
                });


        fastAdapter.withSelectable(true);
        fastAdapter.withOnLongClickListener((OnLongClickListener<FastHoliday>) (v, adapter, item, position) -> {

            showDialog("Edit holiday", 2, item);

            return true;
        });

        return binding.getRoot();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_holidays_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menuAdd) {

            showDialog("Add Holiday", 1, null);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnHolidaysFragmentInteractionListener) {
            mListener = (OnHolidaysFragmentInteractionListener) context;
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
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        int y = Calendar.getInstance().get(Calendar.YEAR);

        if (y == year) {
            etDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
        } else {
            etDate.setError("Not this year");
        }

    }


    public interface OnHolidaysFragmentInteractionListener {
        void onHolidaysFragmentInteraction(String uri);
    }


    public void showDialog(String title, int type, FastHoliday holiday) {

        final Dialog dialog = new Dialog(getActivity());


        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_holiday);

        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);


        TextView tvTitle = dialog.findViewById(R.id.tvTitle);
        tvTitle.setText(title);

        EditText etName = dialog.findViewById(R.id.etName);
        etDate = dialog.findViewById(R.id.etDate);

        AppCompatImageView aivDelete = dialog.findViewById(R.id.aivDelete);

        if (type==1){
            aivDelete.setVisibility(View.INVISIBLE);
        }

        aivDelete.setOnClickListener(v -> {

            db.collection("holidays").document(holiday.uuid).delete();
            dialog.dismiss();

        });

        if (type == 2) {

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(holiday.time);

            etName.setText(holiday.name);

            String date = new SimpleDateFormat("dd/MM/yyyy").format(calendar.getTime());

            etDate.setText(date);

        }

        Button dialogButton = dialog.findViewById(R.id.acbSave);
        dialogButton.setOnClickListener(v -> {

            if (etName.getText().toString().isEmpty()) {
                etName.setError("Name should't be empty");
                return;
            }

            if (etDate.getText().toString().isEmpty()) {
                etDate.setError("Date should't be empty");
                return;
            }

            Map<String, Object> map = new HashMap<>();


            Calendar calendar = Calendar.getInstance();

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

            try {

                map.put("name", etName.getText().toString());
                map.put("year", calendar.get(Calendar.YEAR));
                calendar.setTime(sdf.parse(etDate.getText().toString()));
                map.put("time", calendar.getTimeInMillis());

                if (type == 1) {
                    db.collection("holidays").add(map);
                } else
                    db.collection("holidays").document(holiday.uuid).update(map);

                dialog.dismiss();

            } catch (ParseException e) {
                etDate.setError("Date error");
                e.printStackTrace();
            }


        });


        etDate.setOnClickListener(v -> {

            Calendar calendar = Calendar.getInstance();

            new DatePickerDialog(
                    getActivity(),
                    HolidaysFragment.this,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            ).show();

        });

        dialog.show();

    }


}
