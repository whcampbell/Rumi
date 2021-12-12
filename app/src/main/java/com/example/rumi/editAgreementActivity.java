package com.example.rumi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class editAgreementActivity extends AppCompatActivity {

    private String title;
    private String body;
    private EditText editTextBody;
    private EditText editTextTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_agreement);

        Intent intent = getIntent();

        // EditTexts show current title/body
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextBody = findViewById(R.id.editTextBody);

        editTextTitle.setText(intent.getStringExtra("title"), TextView.BufferType.EDITABLE);
        editTextBody.setText(intent.getStringExtra("body"), TextView.BufferType.EDITABLE);
    }

    public void onClickSave(View view) {
        // get user inputs
        title = ((EditText)findViewById(R.id.editTextTitle)).getText().toString();
        body = ((EditText)findViewById(R.id.editTextBody)).getText().toString();

        // send info back, then add to database
        Intent output = new Intent();
        output.putExtra("title", title);
        output.putExtra("body", body);
        setResult(RESULT_OK, output);

        finish();
    }

    public void onClickCancel(View view) {
        finish();
    }
}