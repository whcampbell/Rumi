package com.example.rumi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class newEventActivity extends AppCompatActivity {
    boolean pm = true;
    FirebaseFirestore dBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
        dBase = FirebaseFirestore.getInstance();

    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_pm:
                if (checked)
                    pm = true;
                    break;
            case R.id.radio_am:
                if (checked)
                    pm = false;
                    break;
        }
    }

    public void onClickCreate(View view) {
        String name = ((EditText)findViewById(R.id.eTextName)).getText().toString();
        String location = ((EditText)findViewById(R.id.eTextLocation)).getText().toString();
        String date = ((EditText)findViewById(R.id.editTextDate)).getText().toString();
        String time = ((EditText)findViewById(R.id.editTextTime)).getText().toString();

        Map<String, Object> info = new HashMap<>();

        //SharedPreferences sp = getSharedPreferences("com.example.rumi", Context.MODE_PRIVATE);
        //String houseName = sp.getString("houseName", "");
        String houseNum = MainActivity.houseNumber;

        DocumentReference dr = dBase.collection("Houses").document(houseNum)
                .collection("events").document(date);

        dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if (doc.exists()) {
                        Map<String, Object> map = doc.getData();
                        int count = map.size();
                        info.putAll(map);
                        if (pm) {
                            info.put("event" + (count + 1), "Name: " + name + "\nLocation: " + location + "\nTime: " + time + "pm");
                        } else {
                            info.put("event" + (count + 1), "Name: " + name + "\nLocation: " + location + "\nTime: " + time + "am");
                        }
                        dr.update(info);
                    } else {
                        if (pm) {
                            info.put("event1", "Name: " + name + "\nLocation: " + location + "\nTime: " + time + "pm");
                        } else {
                            info.put("event1", "Name: " + name + "\nLocation: " + location + "\nTime: " + time + "am");
                        }
                        dr.set(info);
                    }
                }
            }
        });

        finish();
    }

    public void onClickCancel(View view) {
        finish();
    }
}