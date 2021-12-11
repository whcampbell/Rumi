package com.example.rumi;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;




public class newPaymentActivity extends AppCompatActivity {

    float amount;
    Date dueDate;
    String userToPay;
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

        DatePicker date = findViewById(R.id.datePicker1);
        dueDate = getDateFromDatePicker(date);


        Intent output = new Intent();
        output.putExtra("amount", amount);
        output.putExtra("paid", false);
        output.putExtra("duedate", dueDate);
        output.putExtra("payer", userToPay);
        setResult(RESULT_OK, output);

        finish();


    }

    /**
     *
     * @param datePicker
     * @return a java.util.Date
     */
    public static java.util.Date getDateFromDatePicker(DatePicker datePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }



}
