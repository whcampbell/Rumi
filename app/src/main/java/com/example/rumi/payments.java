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
import com.google.firebase.firestore.DocumentSnapshot;
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
    public static ArrayList<String> housemates = new ArrayList<String>();





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_payments, container, false);
        TextView middle = view.findViewById(R.id.middle);
        addPaymentButton = view.findViewById(R.id.addPaymentButton);
        unpaidList = view.findViewById(R.id.unpaidList);
        paidList = view.findViewById(R.id.paidList);
        housemates = getHousemates();





        display();

        return view;



    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){

        if (requestCode == RequestCode && resultCode == RESULT_OK && data != null) {
            //get the variables from the New Payment Activity
            Serializable s = data.getExtras().getSerializable("amount");
            String amount = String.format("%.2f", s);

            s = data.getExtras().getSerializable("paid");
            String paid = s.toString();

            s = data.getExtras().getSerializable("duedate");
            String dueDate = s.toString();

            s = data.getExtras().getSerializable("payer");
            String payer = s.toString();


            Map<String, Object> payment = new HashMap<String, Object>();
            payment.put("amount", amount);
            payment.put("paid", paid);
            payment.put("duedate", dueDate);
            payment.put("payer", payer);
            payment.put("payee", MainActivity.username);


            //add any values you want into the Map

            db.collection("Houses").document(MainActivity.houseNumber).collection("payments").add(payment);
            display();
        }

    }


    public ArrayList<String> getHousemates(){
        ArrayList<String> housemates = new ArrayList<String>();
        db.collection("Houses").document(MainActivity.houseNumber).collection("user").get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                for(DocumentSnapshot document : task.getResult()){
                    housemates.add(document.getId());
                }
            }
        });
        return housemates;
    }

    public void display(){

        TextView middle = view.findViewById(R.id.middle);
        addPaymentButton = view.findViewById(R.id.addPaymentButton);
        unpaidList = view.findViewById(R.id.unpaidList);
        paidList = view.findViewById(R.id.paidList);






        db.collection("Houses").document(MainActivity.houseNumber).collection("payments")
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
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                String amount = (String)document.getData().get("amount");
                                String dueDate = (String)document.getData().get("duedate");
                                String []dueDateArr = dueDate.split(" ");
                                String payer = (String)document.getData().get("payer");
                                String payee = (String)document.getData().get("payee");
                                if(document != null && document.getData().get("paid").equals("false")){
                                    unpaid.add(String.format("%s owes $%s to %s by %s %s %s %s", payer, amount, payee, dueDateArr[0], dueDateArr[1], dueDateArr[2], dueDateArr[5]));
                                    unpaidId.add(document);
                                }else{
                                    paid.add(String.format("%s paid $%s to %s by %s", payer, amount, payee, dueDate));
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
                                    db.collection("Houses").document(MainActivity.houseNumber).collection("payments").document(docId).update(payment);
                                    display();
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
                                    db.collection("Houses").document(MainActivity.houseNumber).collection("payments").document(docId).delete();
                                    display();
                                }
                            });
                        } else {
                            Log.d(TAG, "Error getting documents.", task.getException());
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