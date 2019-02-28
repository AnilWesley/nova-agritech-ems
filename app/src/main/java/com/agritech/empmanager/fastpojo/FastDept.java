package com.agritech.empmanager.fastpojo;

import android.view.View;

import com.agritech.empmanager.R;
import com.agritech.empmanager.databinding.SingleDepartmentBinding;
import com.agritech.empmanager.utils.Constants;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

import androidx.annotation.NonNull;

/*Used for holidays Teams ,Depts*/


public class FastDept extends AbstractItem<FastDept, FastDept.ViewHolder> {

    public String name;
    public int count;

    public FastDept(String s) {
        this.name = s;
        this.count = 0;
    }

    public FastDept() {
    }

    //The unique ID for this type of item
    @Override
    public int getType() {
        return Constants.FAST_DEPT_ID;
    }

    //The layout to be used for this type of item
    @Override
    public int getLayoutRes() {
        return R.layout.single_department;
    }

    @Override
    public ViewHolder getViewHolder(@NonNull View v) {
        return new ViewHolder(v);
    }

    /**
     * our ViewHolder
     */
    protected static class ViewHolder extends FastAdapter.ViewHolder<FastDept> {

        SingleDepartmentBinding binding;

        public ViewHolder(View view) {
            super(view);
            binding = SingleDepartmentBinding.bind(view);
        }

        @Override
        public void bindView(FastDept dept, List<Object> payloads) {

            binding.setDept(dept);

        }

        @Override
        public void unbindView(FastDept item) {

            binding.setDept(null);
        }
    }







}
