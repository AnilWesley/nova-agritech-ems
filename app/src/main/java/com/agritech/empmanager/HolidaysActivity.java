package com.agritech.empmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.agritech.empmanager.databinding.ActivityHolidaysBinding;
import com.agritech.empmanager.fragments.HolidaysFragment;

public class HolidaysActivity extends AppCompatActivity implements HolidaysFragment.OnHolidaysFragmentInteractionListener {

    ActivityHolidaysBinding binding;

    public static void start(Context context,boolean forHR) {

        Intent intent = new Intent(context,HolidaysActivity.class);
        intent.putExtra("forHR",forHR);
        context.startActivity(intent);
    }


    boolean forHR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_holidays);

        forHR = getIntent().getBooleanExtra("forHR",false);

        setSupportActionBar(binding.toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportFragmentManager().beginTransaction().replace(R.id.holidaysContainer, HolidaysFragment.setArguments(forHR)).commit();

        setTitle("Holidays");




    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int menuId = item.getItemId();

        if (menuId == android.R.id.home) {

            finish();

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onHolidaysFragmentInteraction(String note) {

        new AlertDialog.Builder(this).setTitle("Info").setMessage(note).setPositiveButton("Ok", null).show();


    }
}
