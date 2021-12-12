package com.example.rumi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class newAgreementActivity extends AppCompatActivity {

    private String title;
    private String body;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_agreement);
    }

    public void onClickCreate(View view) {
        // get user inputs
        title = ((EditText)findViewById(R.id.editTextTitle)).getText().toString();
        body = ((EditText)findViewById(R.id.editTextBody)).getText().toString();

        // send info back, then add to database
        Intent output = new Intent();
        output.putExtra("title", title);
        output.putExtra("body", body);
        output.putExtra("action", "add");
        setResult(RESULT_OK, output);

        finish();
    }

    public void onClickCancel(View view) {
        finish();
    }
}