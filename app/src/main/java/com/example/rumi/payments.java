package com.example.rumi;


import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class payments extends Fragment {

    private View view;
    private float retamount=0;
    private Button addPaymentButton;
    private FirebaseFirestore db;
    private int RequestCode = 1;


    public payments(FirebaseFirestore db) {
        this.db = db;
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_payments, container, false);
        TextView middle = view.findViewById(R.id.middle);
        addPaymentButton = view.findViewById(R.id.addPaymentButton);



        db.collection("payment")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.e(TAG, document.getId() + " => " + document.getData());
                                middle.setText((String)document.getData().get("amount"));
                            }
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


            Map<String, Object> payment = new HashMap<String, Object>();
            payment.put("amount", amount);

            //add any values you want into the Map

            db.collection("payment").add(payment);
        }

    }
}