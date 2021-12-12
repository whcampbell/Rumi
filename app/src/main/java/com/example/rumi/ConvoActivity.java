package com.example.rumi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ConvoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convo);
        String head = getIntent().getStringExtra("head");
        TextView text = findViewById(R.id.textHead);
        text.setText(head);
    }

    public void onClickCancel(View view) {
        finish();
    }

}