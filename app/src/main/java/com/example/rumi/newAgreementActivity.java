package com.example.rumi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class newAgreementActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_agreement);

    }

    public void onClickCreate(View view) {
        String title = ((EditText)findViewById(R.id.editTextTitle)).getText().toString();

        // TODO: add to database

        finish();
    }

    public void onClickCancel(View view) {
        finish();
    }
}