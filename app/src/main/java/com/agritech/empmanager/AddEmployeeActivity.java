package com.agritech.empmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.agritech.empmanager.databinding.ActivityAddEmployeeBinding;
import com.agritech.empmanager.fragments.AddEmployeeFragment;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

public class AddEmployeeActivity extends AppCompatActivity implements AddEmployeeFragment.OnAddEmployeeFragmentInteractionListener {

    public static void start(Context context) {
        context.startActivity(new Intent(context, AddEmployeeActivity.class));
    }

    ActivityAddEmployeeBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_employee);

        setSupportActionBar(binding.toolbar);

        getSupportFragmentManager().beginTransaction().replace(R.id.addEmployeeContainer, new AddEmployeeFragment()).commit();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        if (findViewById(R.id.toolbar_layout) != null) {
            final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.toolbar_layout);
            AppBarLayout appBarLayout = findViewById(R.id.app_bar);
            appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                boolean isShow = true;
                int scrollRange = -1;

                @Override
                public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                    if (scrollRange == -1) {
                        scrollRange = appBarLayout.getTotalScrollRange();
                    }
                    if (scrollRange + verticalOffset == 0) {
                        collapsingToolbarLayout.setTitle("Add Employee");
                        isShow = true;
                    } else if (isShow) {
                        collapsingToolbarLayout.setTitle(" ");
                        isShow = false;
                    }
                }
            });
        } else {
            setTitle("Pro version");
        }

    }

    @Override
    public void onAddEmployeeFragmentInteraction() {

        finish();

    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int menuId = item.getItemId();

        if (menuId == android.R.id.home) {

            finish();

        }
        return super.onOptionsItemSelected(item);
    }



}
