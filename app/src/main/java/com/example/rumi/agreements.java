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
import java.util.List;
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

    private ArrayAdapter adapter;
    private ListView lv;
    private View view;
    private Button newAgreementButton;
    private FirebaseFirestore db;
    private int RequestCode = 1;

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
        houseID = MainActivity.houseNumber;
        Log.e(TAG, "houseID = " + houseID);

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_agreements, container, false);

        lv = view.findViewById(R.id.listView);
        adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, agreementsArr);
        lv.setAdapter(adapter);

        // populate list with Agreement objects using info from DB
        populateListView(adapter, lv);

        newAgreementButton = view.findViewById(R.id.newAgreementButton);
        newAgreementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                action = "add";
                Intent intent = new Intent(getActivity(), newAgreementActivity.class);
                startActivityForResult(intent, RequestCode);
            }
        });

        return view;
    }

    // fill agreementsArr using db, then notify data set changed
    private void populateListView(ArrayAdapter adapter, ListView lv) {

        adapter.notifyDataSetChanged();

        // traverse db to this house's agreements
        CollectionReference agreementsRef = db.collection("Houses").document(houseID)
                .collection("agreements");

        // TODO: fill agreementArr using db
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
                                        agreementLine = entry.getValue() + "\n" + agreementLine;
                                        if (i % 2 == 0) {
                                            agreementsArr.add(agreementLine);
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
                // TODO: get doc reference
                //docRef = db.collection("Houses").document(houseID)
                //        .collection("agreements").document("----------------");
                action = "edit";
                // send to edit note activity, use putExtra() to pass current agreement info
                Intent intent = new Intent(getActivity(), editAgreementActivity.class);
                String[] curAgreement = clickItemObj.toString().split("\n", 2);
                intent.putExtra("title", curAgreement[0]);
                intent.putExtra("body", curAgreement[1]);
                startActivityForResult(intent, RequestCode);
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
                    docRef.set(agreement);
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