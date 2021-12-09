package com.example.rumi;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class agreements extends Fragment {

    public agreements() {
        // Required empty public constructor
    }

    /**
     * AgreementItem - categories like rent, pets, house rules, etc
     * Addendum - content of agreements like "I, Mollie, will pay $400/month for rent"
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_agreements, container, false);
        Button newAgreementButton = view.findViewById(R.id.newAgreementButton);

        newAgreementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), newAgreementActivity.class);
                startActivity(intent);
            }
        });

        // TODO: populate list with Agreement objects using info from DB
        return view;
    }

    // AgreementItem class with title and list of Addendum objects
    public class AgreementItem {

        private String title;
        private String body;

        public AgreementItem(String newTitle, String newBody) {
            this.title = newTitle;
            this.body = newBody;
        }

        public void editBody(String newBody) {
            this.body = newBody;
        }

    }

}