package com.example.rumi;


import android.os.Bundle;

import androidx.fragment.app.Fragment;


import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;


public class payments extends Fragment {


    private Button addPaymentButton;


    public payments() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payments, container, false);
        addPaymentButton = view.findViewById(R.id.addPaymentButton);

        addPaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PaymentsDialogAdd payLog = new PaymentsDialogAdd();
                payLog.show(getChildFragmentManager(), "events dialog");


            }
        });

        return view;



    }
}