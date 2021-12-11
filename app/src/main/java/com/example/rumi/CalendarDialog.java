package com.example.rumi;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class CalendarDialog extends AppCompatDialogFragment {
    private String date;
    private FirebaseFirestore dBase;

    public CalendarDialog(String date, FirebaseFirestore database) {
        this.dBase= database;
        this.date = date;
    }

    public CalendarDialog() {

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        ArrayList<String> testEvents = new ArrayList<>();


        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, testEvents);

        LayoutInflater inflater = getLayoutInflater();
        View jankView = inflater.inflate(R.layout.events_dialog, null);
        ListView lv = jankView.findViewById(R.id.list);

        lv.setAdapter(adapter);

        DocumentReference eventsRef = dBase.collection("Houses").document("testHouse")
                .collection("events").document(date);

        eventsRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot doc = task.getResult();
                            if (doc.exists()) {
                                Map<String, Object> map = doc.getData();
                                int i = 1;
                                String eventLine = "";
                                for (Map.Entry<String, Object> entry : map.entrySet()) {

                                    eventLine += entry.getValue() + "\n";
                                    if (i % 3 == 0) {
                                        testEvents.add(eventLine);
                                        eventLine = "";
                                    }
                                    ++i;
                                }
                                testEvents.add(eventLine);
                                adapter.notifyDataSetChanged();
                            } else {
                                Log.e("Err", "No such document");
                            }
                        } else {
                            Log.e(TAG, "Error getting documents, ", task.getException());
                        }

                    }
                });




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
