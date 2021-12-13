package com.example.rumi;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class chores extends Fragment {

    private ArrayAdapter adapter;
    private ListView lv;
    private View view;
    private Button newChoreButton;
    private FirebaseFirestore db;
    private int RequestCode = 1;

    private ArrayList<String> choresArr = new ArrayList<String>();
    private String houseID;
    private String docId = "";

     public chores(FirebaseFirestore db) {
        this.db = db;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // get houseID
        houseID = MainActivity.houseNumber;
        Log.d(TAG, "houseID = " + houseID);

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_chores, container, false);

        lv = view.findViewById(R.id.listView);
        adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, choresArr);
        lv.setAdapter(adapter);

        // populate list with Agreement objects using info from DB
        populateListView();

        newChoreButton = view.findViewById(R.id.newChore);
        newChoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), newChoreActivity.class);
                startActivityForResult(intent, RequestCode);
            }
        });

        return view;
    }

    // fill agreementsArr using db, then notify data set changed
    private void populateListView() {

        choresArr.clear();

        // traverse db to this house's agreements
        CollectionReference agreementsRef = db.collection("Houses").document(houseID)
                .collection("chores");

        // fill agreementArr for lv using db, notify data set changed for each existing doc
        agreementsRef.get()
                .addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot queryDocs = (QuerySnapshot) task.getResult();
                            for (QueryDocumentSnapshot doc : queryDocs) {
                                if (doc.exists()) {
                                    Map<String, Object> map = doc.getData();
                                    int i = 1;
                                    String choreLine = "";
                                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                                        choreLine = entry.getValue() + "\n" + choreLine;
                                        if (i % 2 == 0) {
                                            choresArr.add(choreLine);
                                        }
                                        ++i;
                                    }
                                    adapter.notifyDataSetChanged();
                                } else {
                                    Log.e("Err", "No such document");
                                }
                            }
                        } else {
                            Log.e(TAG, "Error getting documents, ", task.getException());
                        }
                    }
                });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
                Object clickItemObj = adapterView.getAdapter().getItem(index);
                String[] curChore = clickItemObj.toString().split("\n", 2);
                // get docId
                Task<QuerySnapshot> colRef = db.collection("Houses").document(houseID)
                        .collection("chores")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d(TAG, "CA[0] = " + curChore[0] + " =? " + document.getData().get("chore"));
                                        if (document.getData().get("chore").equals(curChore[0])) {
                                            docId = document.getId();
                                            Log.d(TAG, "docId = " + docId + " => " + document.getData().get("chore"));
                                            // send to edit note activity, use putExtra() to pass current agreement info
                                            Intent intent = new Intent(getActivity(), editChoreActivity.class);
                                            intent.putExtra("chore", curChore[0]);
                                            intent.putExtra("person", curChore[1]);
                                            Log.d(TAG, "passing to intent: docId = " + docId);
                                            intent.putExtra("docId", docId);
                                            startActivityForResult(intent, RequestCode);
                                        }
                                    }
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){

        if (requestCode == RequestCode && resultCode == RESULT_OK && data != null) {

            // get action type
            String action = data.getStringExtra("action");
            Log.d(TAG,"action = " + action);

            // if not delete (add or edit)
            if (!action.equals("delete")) {

                // get the variables from the New Agreement Activity
                Serializable t = data.getExtras().getSerializable("chore");
                String title = t.toString();

                Serializable b = data.getExtras().getSerializable("person");
                String body = b.toString();

                // add values into the Map
                Map<String, Object> chore = new HashMap<String, Object>();
                chore.put("chore", title);
                chore.put("person", body);

                // traverse db to this house's agreements
                CollectionReference agreementsRef = db.collection("Houses").document(houseID)
                        .collection("chores");

                if (action.equals("edit")) {
                    if (docId != null) {
                        agreementsRef.document(docId).set(chore);
                    } else {
                        Log.e("Err", "No such document");
                    }
                } else if (action.equals("add")) {
                    agreementsRef.add(chore);
                    Log.d(TAG, "added to db");
                }
            }
            // update list view
            populateListView();
        }

    }

}