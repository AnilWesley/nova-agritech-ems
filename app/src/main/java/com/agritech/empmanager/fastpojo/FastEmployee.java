package com.agritech.empmanager.fastpojo;

import android.util.Log;
import android.view.View;

import com.agritech.empmanager.R;
import com.agritech.empmanager.databinding.SingleEmployeeBinding;
import com.agritech.empmanager.textdrawable.ColorGenerator;
import com.agritech.empmanager.textdrawable.TextDrawable;
import com.agritech.empmanager.utils.Constants;
import com.agritech.empmanager.utils.GlideApp;
import com.google.firebase.storage.StorageReference;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

import androidx.annotation.NonNull;


public class FastEmployee extends AbstractItem<FastEmployee, FastEmployee.ViewHolder> {

    public String fName;
    public String lName;
    public String email;
    public String designation;
    public String department;
    public String uid;

    public StorageReference sRef;

    public FastEmployee() {
    }

    public FastEmployee(String fName, String lName, String designation, String department) {

        this.fName = fName;
        this.lName = lName;
        this.designation = designation;
        this.department = department;

    }

    //The unique ID for this type of item
    @Override
    public int getType() {
        return Constants.FAST_EMPLOYEE_ID;
    }

    //The layout to be used for this type of item
    @Override
    public int getLayoutRes() {
        return R.layout.single_employee;
    }

    @Override
    public ViewHolder getViewHolder(@NonNull View v) {
        return new ViewHolder(v);
    }

    /**
     * our ViewHolder
     */
    protected static class ViewHolder extends FastAdapter.ViewHolder<FastEmployee> {

        SingleEmployeeBinding binding;

        public ViewHolder(View view) {
            super(view);
            binding = SingleEmployeeBinding.bind(view);
        }

        @Override
        public void bindView(FastEmployee employee, List<Object> payloads) {

            TextDrawable drawable = TextDrawable.builder()
                    .beginConfig()
                    .height(100)
                    .width(100)
                    .endConfig()
                    .buildRect(employee.fName.charAt(0) + "", ColorGenerator.MATERIAL.getColor(employee.fName));
            GlideApp.with(binding.ivProfile).load(employee.sRef).placeholder(drawable).into(binding.ivProfile);


            //binding.ivProfile.setImageDrawable(drawable);

            binding.setEmployee(employee);

        }

        @Override
        public void unbindView(FastEmployee item) {

            binding.setEmployee(null);
        }
    }







}