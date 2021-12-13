package com.example.rumi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

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

        editTextTitle.setText(intent.getStringExtra("title").trim(), TextView.BufferType.EDITABLE);
        editTextBody.setText(intent.getStringExtra("body").trim(), TextView.BufferType.EDITABLE);
    }

    public void onClickSave(View view) {
        // get user inputs
        title = ((EditText)findViewById(R.id.editTextTitle)).getText().toString();
        body = ((EditText)findViewById(R.id.editTextBody)).getText().toString();

        // send info back, then add to database
        Intent output = new Intent();
        output.putExtra("title", title);
        output.putExtra("body", body);
        output.putExtra("action", "edit");
        setResult(RESULT_OK, output);

        finish();
    }

    public void onClickCancel(View view) {
        finish();
    }

    public void onClickDelete(View view) {
        // send info back
        Intent output = new Intent();
        output.putExtra("action", "delete");
        Log.d("err", "index w/i delete = " + getIntent().getIntExtra("index", -1));
        output.putExtra("index", getIntent().getIntExtra("index", -1));
        setResult(RESULT_OK, output);

        finish();
    }
}