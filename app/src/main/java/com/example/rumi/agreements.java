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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class agreements extends Fragment {

    private View view;
    private Button newAgreementButton;
    private FirebaseFirestore db;
    private int RequestCode = 1;

    public agreements(FirebaseFirestore db) {
        this.db = db;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_agreements, container, false);
        newAgreementButton = view.findViewById(R.id.newAgreementButton);

        newAgreementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), newAgreementActivity.class);
                startActivityForResult(intent, RequestCode);
            }
        });

        // populate list with Agreement objects using info from DB
        populateListView();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // populate list with Agreement objects using info from DB
        populateListView();
    }

    // This method use SimpleAdapter to show data in ListView.
    private void populateListView() {

        // traverse db to this house's agreements TODO: no hardcoding for house, get from SP instead
        CollectionReference agreementsRef = db.collection("Houses").document("testHouse")
                .collection("agreements");

        // fill agreementList using db
        ArrayList<String> agreementsArr = new ArrayList<String>();

        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, agreementsArr);
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
                // need to make new activity.java and activity.xml
                // copy new_agreement.xml and change to "edit agreement"
                // EditTexts should show current title/body
                // cancel is the same
                // done should update db rather than add to it
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

            // traverse db to this house's agreements TODO: no hardcoding for house, get from SP instead
            CollectionReference agreementsRef = db.collection("Houses").document("testHouse")
                    .collection("agreements");

            agreementsRef.add(agreement); // TODO: how to edit doc not just add new doc
        }

    }

}