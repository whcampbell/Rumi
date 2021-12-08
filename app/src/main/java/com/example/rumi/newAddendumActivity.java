package com.example.rumi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class newAddendumActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_addendum);

    }

    public void onClickCreate(View view) {
        String body = ((EditText)findViewById(R.id.editTextBody)).getText().toString();
        String signer = ((EditText)findViewById(R.id.editTextSigner)).getText().toString();

        // TODO: add to database

        finish();
    }

    public void onClickCancel(View view) {
        finish();
    }
}