package com.example.rumi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ConvoActivity extends AppCompatActivity {
    FirebaseFirestore db;
    DocumentReference dr;
    ArrayList<String> comms;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convo);

        db = FirebaseFirestore.getInstance();

        String head = getIntent().getStringExtra("head");
        TextView text = findViewById(R.id.textHead);
        text.setText(head);

        comms = new ArrayList<>();
        ListView commList = findViewById(R.id.commentList);
        adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, comms);
        commList.setAdapter(adapter);

        dr = db.collection("Houses").document(MainActivity.houseNumber)
                .collection("Conversations").document(head);

        dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if (doc.exists()) {
                        Map<String, Object> map = doc.getData();
                        for(Map.Entry entry : map.entrySet()) {
                            comms.add(0, (String)entry.getValue());
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.e("err", "error finding document");
                    }
                }
            }
        });


    }

    public void onClickCancel(View view) {
        finish();
    }

    public void onClickAdd(View view) {

        EditText newCommText = findViewById(R.id.editTextNewComment);
        String newComm = newCommText.getText().toString();

        if (newComm.equals("")) {
            return;
        }

        newCommText.setText("");

        dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if (doc.exists()) {
                        Map<String, Object> map = doc.getData();
                        int index = map.size();
                        map.put("comment" + (index + 1), newComm);
                        dr.update(map);
                        comms.add(newComm);
                        adapter.notifyDataSetChanged();
                    } else {
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("comment1", newComm);
                        dr.set(map);
                        comms.add(newComm);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

}