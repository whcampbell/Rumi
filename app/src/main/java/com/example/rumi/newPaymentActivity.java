package com.example.rumi;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;




public class newPaymentActivity extends AppCompatActivity {

    float amount;
    LocalDate dueDate;
    String userToPay;
    Boolean reocurring;
    String freq;
    Intent intent;

    public newPaymentActivity(){

        intent = getIntent();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_payment);

    }

    public void onClickCancel(View view){
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void sendPaymentInfoBack (View view) {

        EditText Price = findViewById(R.id.Price);
        float amount = Float.parseFloat(Price.getText().toString());




        Intent output = new Intent();
        output.putExtra("amount", amount);
        output.putExtra("dudate", dueDate);
        output.putExtra("payer", userToPay);
        output.putExtra("reocurring", reocurring);
        output.putExtra("reocurancefreq", freq);
        setResult(RESULT_OK, output);

        finish();


    }


}
