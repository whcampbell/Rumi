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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
        simpleAdapterListView();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // populate list with Agreement objects using info from DB
        simpleAdapterListView();
    }

    // This method use SimpleAdapter to show data in ListView.
    private void simpleAdapterListView() {

        // fill agreementList using db
        ArrayList<String> titleArr = new ArrayList<>();
        ArrayList<String> bodyArr = new ArrayList<>();

        db.collection("agreement")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.e(TAG, document.getId() + " => " + document.getData());
                                titleArr.add((String)document.getData().get("title"));
                                bodyArr.add((String)document.getData().get("body"));
                            }
                        } else {
                            Log.e(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

        // building agreementList to get ready for listView
        ArrayList<Map<String,Object>> agreementList = new ArrayList<Map<String,Object>>();;

        int titleLen = titleArr.size();
        for(int i =0; i < titleLen; i++) {
            Map<String,Object> listItemMap = new HashMap<String,Object>();
            listItemMap.put("title", titleArr.get(i));
            listItemMap.put("body", bodyArr.get(i));
            agreementList.add(listItemMap);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(getContext(),agreementList,android.R.layout.simple_list_item_2,
                new String[]{"title","body"},new int[]{android.R.id.text1,android.R.id.text2});

        ListView listView = (ListView)view.findViewById(R.id.listView);
        listView.setAdapter(simpleAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
                Object clickItemObj = adapterView.getAdapter().getItem(index);
                Toast.makeText(getContext(), "You clicked " + clickItemObj.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){

        if (requestCode == RequestCode && resultCode == RESULT_OK && data != null) {
            //get the variables from the New Agreement Activity
            Serializable t = data.getExtras().getSerializable("title");
            String title = t.toString();

            Serializable b = data.getExtras().getSerializable("body");
            String body = b.toString();

            //add values into the Map
            Map<String, Object> agreement = new HashMap<String, Object>();
            agreement.put("title", title);
            agreement.put("body", body);

            db.collection("agreement").add(agreement);
        }

    }

}