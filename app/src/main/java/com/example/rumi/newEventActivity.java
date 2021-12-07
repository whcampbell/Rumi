package com.example.rumi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

public class newEventActivity extends AppCompatActivity {
    boolean pm = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_pm:
                if (checked)
                    pm = true;
                    break;
            case R.id.radio_am:
                if (checked)
                    pm = false;
                    break;
        }
    }

    public void onClickCreate(View view) {
        String name = ((EditText)findViewById(R.id.eTextName)).getText().toString();
        String location = ((EditText)findViewById(R.id.eTextLocation)).getText().toString();
        String date = ((EditText)findViewById(R.id.editTextDate)).getText().toString();
        String time = ((EditText)findViewById(R.id.editTextTime)).getText().toString();
        if (pm) {
            time = time + "pm";
        } else {
            time = time + "am";
        }

        // TODO add to database
        //  may involve parsing date/time for format

        finish();
    }

    public void onClickCancel(View view) {
        finish();
    }
}