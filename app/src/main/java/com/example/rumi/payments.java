package com.example.rumi;


import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class payments extends Fragment {

    private View view;
    private float retamount=0;
    private Button addPaymentButton;
    private int RequestCode = 1;
    private ListView unpaidList;
    private ListView paidList;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_payments, container, false);
        TextView middle = view.findViewById(R.id.middle);
        addPaymentButton = view.findViewById(R.id.addPaymentButton);
        unpaidList = view.findViewById(R.id.unpaidList);
        paidList = view.findViewById(R.id.paidList);






        db.collection("payment")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<String> unpaid = new ArrayList<String>();
                            ArrayList<String> paid = new ArrayList<String>();
                            ArrayList<QueryDocumentSnapshot> unpaidId = new ArrayList<QueryDocumentSnapshot>();
                            ArrayList<QueryDocumentSnapshot> paidId = new ArrayList<QueryDocumentSnapshot>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.e(TAG, document.getId() + " => " + document.getData());
                                if(document != null && document.getData().get("paid").equals("false")){
                                    unpaid.add(String.format("Owe amount: %s", document.getData().get("amount")));
                                    unpaidId.add(document);
                                }else{
                                    paid.add(String.format("Paid amount: %s", document.getData().get("amount")));
                                    paidId.add(document);
                                }
                            }
                            ArrayAdapter adapterString = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, unpaid);
                            unpaidList.setTag(unpaidId);
                            unpaidList.setAdapter(adapterString);
                            unpaidList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Map<String, Object> payment = new HashMap<String, Object>();
                                    payment.put("paid", "true");
                                    ArrayList<QueryDocumentSnapshot> list = (ArrayList<QueryDocumentSnapshot>) unpaidList.getTag();
                                    QueryDocumentSnapshot document = list.get(position);
                                    String docId = document.getId();
                                    db.collection("payment").document(docId).update(payment);
                                    refresh();
                                }
                            });
                            adapterString = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, paid);
                            paidList.setTag(paidId);
                            paidList.setAdapter(adapterString);
                            paidList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                                    Map<String, Object> payment = new HashMap<String, Object>();
                                    payment.put("paid", "false");

                                    ArrayList<QueryDocumentSnapshot> list =  (ArrayList<QueryDocumentSnapshot>) paidList.getTag();
                                    QueryDocumentSnapshot document = list.get(position);
                                    String docId = document.getId();
                                    db.collection("payment").document(docId).update(payment);
                                    refresh();
                                }
                            });
                        } else {
                            Log.e(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

        addPaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), newPaymentActivity.class);
                startActivityForResult(intent, RequestCode);


            }
        });

        return view;



    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){

        if (requestCode == RequestCode && resultCode == RESULT_OK && data != null) {
            //get the variables from the New Payment Activity
            Serializable s = data.getExtras().getSerializable("amount");
            String amount = s.toString();

            s = data.getExtras().getSerializable("paid");
            String paid = s.toString();

            s = data.getExtras().getSerializable("duedate");
            String dueDate = s.toString();

           // s = data.getExtras().getSerializable("payer");
            //String payer = s.toString();


            Map<String, Object> payment = new HashMap<String, Object>();
            payment.put("amount", amount);
            payment.put("paid", paid);
            payment.put("duedate", dueDate);
            //payment.put("payer", payer);

            //add any values you want into the Map

            db.collection("payment").add(payment);
            refresh();
        }

    }
    //output.putExtra("paid", false);
    //        output.putExtra("dudate", dueDate);
    //        output.putExtra("payer", userToPay);
    //        output.putExtra("reocurring", reocurring);
    //        output.putExtra("reocurancefreq", freq);
    public class payment {
        float amount;
        String duedate;
        String payer;
        Boolean reoccurring;
        String reoccurFreq;
        public payment (float amount, String duedate, String payer, Boolean reoccurring, String reoccurFreq){
            this.amount = amount;

        }
    }
    public void refresh(){

        TextView middle = view.findViewById(R.id.middle);
        addPaymentButton = view.findViewById(R.id.addPaymentButton);
        unpaidList = view.findViewById(R.id.unpaidList);
        paidList = view.findViewById(R.id.paidList);






        db.collection("payment")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<String> unpaid = new ArrayList<String>();
                            ArrayList<String> paid = new ArrayList<String>();
                            ArrayList<QueryDocumentSnapshot> unpaidId = new ArrayList<QueryDocumentSnapshot>();
                            ArrayList<QueryDocumentSnapshot> paidId = new ArrayList<QueryDocumentSnapshot>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.e(TAG, document.getId() + " => " + document.getData());
                                if(document != null && document.getData().get("paid").equals("false")){
                                    unpaid.add(String.format("Owe amount: %s", document.getData().get("amount")));
                                    unpaidId.add(document);
                                }else{
                                    paid.add(String.format("Paid amount: %s", document.getData().get("amount")));
                                    paidId.add(document);
                                }
                            }
                            ArrayAdapter adapterString = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, unpaid);
                            unpaidList.setTag(unpaidId);
                            unpaidList.setAdapter(adapterString);
                            unpaidList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Map<String, Object> payment = new HashMap<String, Object>();
                                    payment.put("paid", "true");
                                    ArrayList<QueryDocumentSnapshot> list = (ArrayList<QueryDocumentSnapshot>) unpaidList.getTag();
                                    QueryDocumentSnapshot document = list.get(position);
                                    String docId = document.getId();
                                    db.collection("payment").document(docId).update(payment);
                                    refresh();
                                }
                            });
                            adapterString = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, paid);
                            paidList.setTag(paidId);
                            paidList.setAdapter(adapterString);
                            paidList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                                    Map<String, Object> payment = new HashMap<String, Object>();
                                    payment.put("paid", "false");

                                    ArrayList<QueryDocumentSnapshot> list =  (ArrayList<QueryDocumentSnapshot>) paidList.getTag();
                                    QueryDocumentSnapshot document = list.get(position);
                                    String docId = document.getId();
                                    db.collection("payment").document(docId).update(payment);
                                    refresh();
                                }
                            });
                        } else {
                            Log.e(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

        addPaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), newPaymentActivity.class);
                startActivityForResult(intent, RequestCode);


            }
        });



    }
}