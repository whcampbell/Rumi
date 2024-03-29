package com.example.rumi;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class calendar extends Fragment {

    CalendarView calendar;
    TextView title;
    FirebaseFirestore dBase;

    public calendar() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        calendar = view.findViewById(R.id.calendar);
        title = view.findViewById(R.id.CalText);
        dBase = FirebaseFirestore.getInstance();


        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(
                    @NonNull CalendarView calendarView, int year, int month, int day) {
                String selected = (month + 1) + "/" + day + "/" + year;

                CalendarDialog calLog = new CalendarDialog(selected, dBase);

                calLog.show(getChildFragmentManager(), "events dialog");

            }
        });

    }

}