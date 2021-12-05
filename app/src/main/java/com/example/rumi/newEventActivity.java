package com.example.rumi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class newEventActivity extends AppCompatActivity {
    boolean pm = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

    }

    public void onClickCreate(View view) {
        finish();
    }
}