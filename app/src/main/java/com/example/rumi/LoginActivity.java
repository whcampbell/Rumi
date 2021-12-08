package com.example.rumi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onClickLogin(View view) {
        EditText editUser = findViewById(R.id.editTextUser);
        EditText editPass = findViewById(R.id.editTextPass);
        String user = editUser.getText().toString();
        String pass = editPass.getText().toString();


        Context context = getApplicationContext();
        String badInput = "Username or Password Incorrect";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, badInput, duration);

        if (user == null || user.length() == 0) {
            toast.show();
            editUser.setText("");
            editPass.setText("");
            return;
        }

        if (pass == null || pass.length() == 0) {
            toast.show();
            editUser.setText("");
            editPass.setText("");
            return;
        }

        // TODO pull database info from matching username
        // check that passwords match

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();

    }

}