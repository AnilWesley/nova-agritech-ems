package com.agritech.empmanager.fastpojo;

import android.view.View;

import com.agritech.empmanager.R;
import com.agritech.empmanager.databinding.SingleEmpEducationBinding;
import com.agritech.empmanager.databinding.SingleEmpWorkBinding;
import com.agritech.empmanager.utils.Constants;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

import androidx.annotation.NonNull;


public class FastEmpWork extends AbstractItem<FastEmpWork, FastEmpWork.ViewHolder> {

    public String companyName;
    public String jobTitle;
    public String fromDate;
    public String toDate;
    public String desc;
    public String uuid;

    public FastEmpWork() {
    }

    //The unique ID for this type of item
    @Override
    public int getType() {
        return Constants.FAST_EMP_WORK;
    }

    //The layout to be used for this type of item
    @Override
    public int getLayoutRes() {
        return R.layout.single_emp_work;
    }

    @Override
    public ViewHolder getViewHolder(@NonNull View v) {
        return new ViewHolder(v);
    }

    /**
     * our ViewHolder
     */
    protected static class ViewHolder extends FastAdapter.ViewHolder<FastEmpWork> {

        SingleEmpWorkBinding binding;

        public ViewHolder(View view) {
            super(view);
            binding = SingleEmpWorkBinding.bind(view);
        }

        @Override
        public void bindView(FastEmpWork dept, List<Object> payloads) {

            binding.setEmpWork(dept);

        }

        @Override
        public void unbindView(FastEmpWork item) {

            binding.setEmpWork(null);
        }
    }







}
