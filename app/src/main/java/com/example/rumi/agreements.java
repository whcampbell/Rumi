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
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class agreements extends Fragment {

    private View view;
    private Button newAgreementButton;
    private FirebaseFirestore db;
    private int RequestCode = 1;

    private ArrayAdapter adapter;
    private ArrayList<String> agreementsArr = new ArrayList<String>();
    private String houseID;
    private String action = "add";
    private DocumentReference docRef = null;

    public agreements(FirebaseFirestore db) {
        this.db = db;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // get houseID
        // TODO: no hardcoding, use SP instead
        houseID = "testHouse";

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_agreements, container, false);
        adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, agreementsArr);
        newAgreementButton = view.findViewById(R.id.newAgreementButton);

        newAgreementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                action = "add";
                Intent intent = new Intent(getActivity(), newAgreementActivity.class);
                startActivityForResult(intent, RequestCode);
            }
        });

        // populate list with Agreement objects using info from DB
        populateListView(adapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // populate list with Agreement objects using info from DB

        populateListView(adapter);
    }

    // This method use SimpleAdapter to show data in ListView.
    private void populateListView(ArrayAdapter adapter) {

        // traverse db to this house's agreements
        CollectionReference agreementsRef = db.collection("Houses").document(houseID)
                .collection("agreements");

        // TODO: fill agreementList using db

        LayoutInflater inflater = getLayoutInflater();
        ListView lv = inflater.inflate(R.layout.events_dialog, null).findViewById(R.id.list);
        lv.setAdapter(adapter);

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
                                    String agreementLine = "";
                                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                                        agreementLine += entry.getValue() + "\n";
                                        if (i % 2 == 0) {
                                            agreementsArr.add(agreementLine);
                                            agreementLine = "";
                                        }
                                        ++i;
                                    }
                                    agreementsArr.add(agreementLine);
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
                // TODO: send to edit note activity
                // save button should update db rather than add to it - get document reference then use ref.update(newInfo)
                // get doc reference, pass to onActivityResult somehow
                // docRef = ----------------;
                action = "edit";
                Intent intent = new Intent(getActivity(), editAgreementActivity.class);
                startActivityForResult(intent, RequestCode);
                // delete below line
                Toast.makeText(getContext(), "You clicked " + clickItemObj.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

  @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){

        if (requestCode == RequestCode && resultCode == RESULT_OK && data != null) {
            // get the variables from the New Agreement Activity
            Serializable t = data.getExtras().getSerializable("title");
            String title = t.toString();

            Serializable b = data.getExtras().getSerializable("body");
            String body = b.toString();

            // add values into the Map
            Map<String, Object> agreement = new HashMap<String, Object>();
            agreement.put("title", title);
            agreement.put("body", body);

            // traverse db to this house's agreements
            CollectionReference agreementsRef = db.collection("Houses").document(houseID)
                    .collection("agreements");

            if (action == "edit") {
                if (docRef != null) {
                    docRef.set(agreement); // TODO: test if doc is updated
                }
                else {
                    Log.e("Err", "No such document");
                }
            }
            else {
                agreementsRef.add(agreement);
            }

        }

    }

}