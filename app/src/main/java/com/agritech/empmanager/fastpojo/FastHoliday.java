package com.agritech.empmanager.fastpojo;

import android.view.View;

import com.agritech.empmanager.R;
import com.agritech.empmanager.databinding.SingleDepartmentBinding;
import com.agritech.empmanager.databinding.SingleHolidayBinding;
import com.agritech.empmanager.utils.Constants;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;

/*Used for holidays Teams ,Depts*/


public class FastHoliday extends AbstractItem<FastHoliday, FastHoliday.ViewHolder> {

    public String name;
    public long time;
    public String uuid;

    public FastHoliday() {
    }


    //The unique ID for this type of item
    @Override
    public int getType() {
        return Constants.FAST_HOLIDAY_ID;
    }

    //The layout to be used for this type of item
    @Override
    public int getLayoutRes() {
        return R.layout.single_holiday;
    }

    @Override
    public ViewHolder getViewHolder(@NonNull View v) {
        return new ViewHolder(v);
    }

    /**
     * our ViewHolder
     */
    protected static class ViewHolder extends FastAdapter.ViewHolder<FastHoliday> {

        SingleHolidayBinding binding;

        public ViewHolder(View view) {
            super(view);
            binding = SingleHolidayBinding.bind(view);
        }

        @Override
        public void bindView(FastHoliday holiday, List<Object> payloads) {

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(holiday.time);

            binding.tvName.setText(holiday.name);

            String date = new SimpleDateFormat("MMM dd").format(calendar.getTime());

            String day = new SimpleDateFormat("EEE").format(calendar.getTime());

            binding.tvDay.setText(day);
            binding.tvDate.setText(date);


        }

        @Override
        public void unbindView(FastHoliday holiday) {
        }
    }


}
