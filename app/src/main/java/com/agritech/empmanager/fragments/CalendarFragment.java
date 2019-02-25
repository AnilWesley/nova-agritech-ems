package com.agritech.empmanager.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.agritech.empmanager.R;
import com.agritech.empmanager.databinding.FragmentCalendarBinding;
import com.agritech.empmanager.mcalendar.CompactCalendarView;
import com.agritech.empmanager.mcalendar.domain.Event;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class CalendarFragment extends Fragment {

    private OnCalendarFragmentInteractionListener mListener;

    FragmentCalendarBinding binding;
    AppCompatActivity activity;


    public CalendarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_calendar, container, false);

        //for create home button
        activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(binding.toolbar);

        setToolbarTitle("February 2019");


        binding.mCalenderView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                List<Event> events = binding.mCalenderView.getEvents(dateClicked);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {

                //setToolbarTitle("hii");
                setToolbarTitle("" + (new SimpleDateFormat("MMMM yyyy")).format(firstDayOfNewMonth));
                //Toast.makeText(activity, "" + firstDayOfNewMonth, Toast.LENGTH_SHORT).show();

            }
        });

        return binding.getRoot();
    }


    public void setToolbarTitle(String title) {

        mListener.onCalendarFragmentInteraction(title);

        //Toast.makeText(activity, ""+title, Toast.LENGTH_SHORT).show();
        //activity.getSupportActionBar().setTitle("toolbar title");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCalendarFragmentInteractionListener) {
            mListener = (OnCalendarFragmentInteractionListener) context;
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

    public interface OnCalendarFragmentInteractionListener {
        // TODO: Update argument type and name
        void onCalendarFragmentInteraction(String title);
    }
}
