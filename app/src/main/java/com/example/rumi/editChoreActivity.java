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

public class editChoreActivity extends AppCompatActivity {

    private String title;
    private String body;
    private EditText editTextPerson;
    private EditText editTextTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_chore);

        Intent intent = getIntent();

        // EditTexts show current title/body
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextPerson = findViewById(R.id.editTextPerson);

        editTextTitle.setText(intent.getStringExtra("chore").trim(), TextView.BufferType.EDITABLE);
        editTextPerson.setText(intent.getStringExtra("person").trim(), TextView.BufferType.EDITABLE);
    }

    public void onClickSave(View view) {
        // get user inputs
        title = ((EditText)findViewById(R.id.editTextTitle)).getText().toString();
        body = ((EditText)findViewById(R.id.editTextPerson)).getText().toString();

        // send info back, then add to database
        Intent output = new Intent();
        output.putExtra("chore", title);
        output.putExtra("person", body);
        output.putExtra("action", "edit");
        setResult(RESULT_OK, output);

        finish();
    }

    public void onClickCancel(View view) {
        finish();
    }

    public void onClickDelete(View view) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String docId = getIntent().getStringExtra("docId");
        if (docId != "") {
            DocumentReference docRef = db.collection("Houses").document(MainActivity.houseNumber)
                    .collection("chores").document(docId);
            docRef.delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("err", "DocumentSnapshot successfully deleted!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("err", "Error deleting document", e);
                        }
                    });
        } else {
            Log.e("err", "Error: docId is null");
        }

        // send info back
        Intent output = new Intent();
        output.putExtra("action", "delete");
        setResult(RESULT_OK, output);

        finish();
    }
}