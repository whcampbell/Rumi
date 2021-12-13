package com.example.rumi;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;


public class dashboard extends Fragment {
    private ArrayList<DashConvo> convoList;
    private RecyclerView reView;
    private RecyclerAdapter.RecyclerClickListenter listener;
    private RecyclerAdapter adapter;
    private FirebaseFirestore db;


    public dashboard() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        TextView houseTextView = view.findViewById(R.id.houseNumber);

        EditText enterText = view.findViewById(R.id.editTextPostHead);
        Button postButton = view.findViewById(R.id.button6);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enterHead = enterText.getText().toString();
                Context context = getContext();
                String message = "Enter text to post a message";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, message, duration);

                if (enterHead.equals("")) {
                    toast.show();
                } else {
                    addToDatabase(enterHead);
                    convoList.add(0, new DashConvo(enterHead, "No comments yet"));
                    adapter.notifyDataSetChanged();
                    enterText.setText("");
                }
            }
        });

        houseTextView.setText(MainActivity.getHouseName());

        reView = view.findViewById(R.id.Recycler);
        convoList = new ArrayList<>();

        db = FirebaseFirestore.getInstance();
        CollectionReference cr = db.collection("Houses").document(MainActivity.houseNumber)
                .collection("Conversations");

        cr.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    convoList.clear();
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        String hString = doc.getId();
                        String cString = doc.getString("comment1");
                        if (cString == null || cString.equals("")) {
                            cString = "No comments yet";
                        }
                        convoList.add(0, new DashConvo(hString, cString));
                    }
                }
            }
        });

        if (convoList.size() == 0) {
            convoList.add(new DashConvo("Pretty quiet in here...", "Add a conversation?"));
        }

        setAdapter();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        CollectionReference cr = db.collection("Houses").document(MainActivity.houseNumber)
                .collection("Conversations");

        cr.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    convoList.clear();
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        String hString = doc.getId();
                        String cString = doc.getString("comment1");
                        if (cString == null || cString.equals("")) {
                            cString = "No comments yet";
                        }
                        convoList.add(0, new DashConvo(hString, cString));
                    }
                    if (convoList.size() == 0) {
                        convoList.add(new DashConvo("Pretty quiet in here...", "Add a conversation?"));
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });

    }

    private void setAdapter() {
        setOnClickListener();
        adapter = new RecyclerAdapter(convoList, listener);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        reView.setLayoutManager(layoutManager);
        reView.setItemAnimator(new DefaultItemAnimator());
        reView.setAdapter(adapter);
    }

    private void setOnClickListener() {
        listener = new RecyclerAdapter.RecyclerClickListenter() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getActivity(), ConvoActivity.class);
                intent.putExtra("head", convoList.get(position).getConvoHead());
                startActivity(intent);
            }
        };
    }

    private void addToDatabase(String header) {
        DocumentReference dr = db.collection("Houses").document(MainActivity.houseNumber)
                .collection("Conversations").document(header);
        HashMap<String, Object> map = new HashMap<>();
        dr.set(map);
    }

}