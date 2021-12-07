package com.example.rumi;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.ArrayList;

public class CalendarDialog extends AppCompatDialogFragment {
    private String date;

    public CalendarDialog(String date) {
        this.date = date;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        ArrayList<String> testEvents = new ArrayList<>();
        String e1 = "First Event\n5:30, Edward Klief";
        String e2 = "Second Event\n2:00, Mosse Humanities";
        String e3 = "Third Event\n8:00, CS 1240";
        testEvents.add(e1);
        testEvents.add(e2);
        testEvents.add(e3);


        LayoutInflater inflater = getLayoutInflater();
        View jankView = inflater.inflate(R.layout.events_dialog, null);
        ListView lv = jankView.findViewById(R.id.list);

        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, testEvents);

        lv.setAdapter(adapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(date)
                .setView(jankView)
                .setPositiveButton("New Event", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getActivity(), newEventActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        return builder.create();
    }
}
