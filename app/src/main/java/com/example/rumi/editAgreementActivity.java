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
                    .collection("agreements").document(docId);
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

        finish();
    }
}