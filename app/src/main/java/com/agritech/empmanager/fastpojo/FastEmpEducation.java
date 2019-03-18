package com.agritech.empmanager.fastpojo;

import android.view.View;

import com.agritech.empmanager.R;
import com.agritech.empmanager.databinding.SingleEmpEducationBinding;
import com.agritech.empmanager.utils.Constants;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

import androidx.annotation.NonNull;

/*Used for holidays Teams ,Depts*/


public class FastEmpEducation extends AbstractItem<FastEmpEducation, FastEmpEducation.ViewHolder> {

    public String university;
    public String institution;
    public String degree;
    public int year;
    public String interests;
    public String remarks;
    public String uuid;

    public FastEmpEducation() {
    }

    //The unique ID for this type of item
    @Override
    public int getType() {
        return Constants.FAST_EMP_EDUCATION;
    }

    //The layout to be used for this type of item
    @Override
    public int getLayoutRes() {
        return R.layout.single_emp_education;
    }

    @Override
    public ViewHolder getViewHolder(@NonNull View v) {
        return new ViewHolder(v);
    }

    /**
     * our ViewHolder
     */
    protected static class ViewHolder extends FastAdapter.ViewHolder<FastEmpEducation> {

        SingleEmpEducationBinding binding;

        public ViewHolder(View view) {
            super(view);
            binding = SingleEmpEducationBinding.bind(view);
        }

        @Override
        public void bindView(FastEmpEducation dept, List<Object> payloads) {

            binding.setEmpEdu(dept);

        }

        @Override
        public void unbindView(FastEmpEducation item) {

            binding.setEmpEdu(null);
        }
    }







}
